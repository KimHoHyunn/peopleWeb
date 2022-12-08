package com.people.card.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.people.card.packet.CardPacket;
import com.people.common.util.CommonUtil;
import com.people.common.util.SystemUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardSendUtil {
	private final String serverIP;
	private final int serverPort;
	private final String ecmIP;
	
	public CardSendUtil() throws IOException{
		Properties props = new Properties();//SystemUtil.getConfigProperties();
		
		serverIP = props.getProperty("SERVER_CARD_IP");
		serverPort = CommonUtil.safeObjToInt(props.get("SERVER_CARD_PORT"));
		ecmIP = props.getProperty("SERVER_ECM_IP");
	}
	
	public static final class ERR_C {
		public static final String SUCCESS = "";
		public static final String SOCKET_CONNECT_FAIL = "91";
		public static final String IMG_RESPONSE_WRONG = "93";
		public static final String INF_RESPONSE_WRONG = "94";
		public static final String SEND_FAIL = "99";
	}
	
	public String sendToCare(List<File> imgFileList, File infFile) {
		String errC = ERR_C.SUCCESS;
		
		//1 socket link
		Socket socket = null;
		try {
			socket = new Socket(serverIP, serverPort);
			socket.setSoTimeout(30*1000);
		} catch (Exception e) {
			return ERR_C.SOCKET_CONNECT_FAIL;
		}
		
		DataInputStream in = null;
		DataOutputStream out = null;
		
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			//2 이미지파일 전송
			for(File imgFile : imgFileList) {
				errC = sendTileToCard(imgFile, out, in, "img");
				
				//전송오류이면 중단
				if(ERR_C.SUCCESS.equals(errC) == false) {
					break;
				}
			}
			//3 inf 파일전송
			if(ERR_C.SUCCESS.equals(errC)) {
				errC = sendTileToCard(infFile, out, in, "inf");
			}
			
		} catch (Exception e) {
			if(CommonUtil.isEmpty(errC)) {
				errC = ERR_C.SEND_FAIL;
			}
			log.error(CommonUtil.getEccNo(e));
		} finally {
			if(CommonUtil.isNotEmpty(in)) { try {in.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
			if(CommonUtil.isNotEmpty(out)) { try {out.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
			if(CommonUtil.isNotEmpty(socket)) { try {socket.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
		}
		
		return errC;
	}
	
	/**
	 * 파일을 전송하고 응답을 수신하여 결과 확인
	 * 1 요청 패킷만 전송
	 *   RQ_SO_CCD = S
	 *   DL_CCD = RE
	 * 2 요청패킷+파일데이터 전송
	 *   RQ_SO_CCD = R
	 *   DL_CCD = 00(최초), 01(계속), 02(최종) : 파일을 몇번 나누어서 보내느냐에 따라 다르게 사용
	 *   
	 * @param file
	 * @param out
	 * @param in
	 * @param fileKind
	 * @return
	 */
	private String sendTileToCard(File file, DataOutputStream out, DataInputStream in, String fileKind) throws IOException{
		Vector<byte[]> sendBuffers = readFiletoVector(file);
		
		if(CommonUtil.isEmpty(sendBuffers) || 0 == sendBuffers.size()) {
			return ERR_C.SEND_FAIL;
		}
		
		CardPacket sendPacket = CardPacket.create();
		sendPacket.setValue("RQ_SO_CCD",      "S");
		sendPacket.setValue("TMN_IP_AR",      serverIP);
		sendPacket.setValue("GP_CO_CD",       "S001");
		sendPacket.setValue("SYS_BNE_CCD",    "BIP");
		sendPacket.setValue("FIL_NM",         file.getName());
		sendPacket.setValue("FIL_SZ",         file.length());
		sendPacket.setValue("PACKET_SZ",      CardPacket.PACKET_SZ);
		sendPacket.setValue("DL_CCD",         CardPacket.DL.REQUEST);
		
		//전송후 응답수신할 버퍼
		byte[] recvBuffer = new byte[sendPacket.length()];
		
		//패킬단위로 전송할 회수
		int loopCount = sendBuffers.size();
		int loopIndex = 0;
		
		while(loopIndex < loopCount) {
			if(0 == loopIndex) {
				sendPacket.setValue("RQ_SO_CCD", "S");
				sendPacket.setValue("DL_CCD", CardPacket.DL.REQUEST);
				//패킷의 크기
				sendPacket.setValue("PACKET_SZ", CardPacket.PACKET_SZ);
				
				//전송패킷을 조립하여 보내기
				out.write(sendPacket.serialize());
			} else {
				sendPacket.setValue("RQ_SO_CCD", "R");
				//최초전송
				if(1==loopIndex && 1!=loopCount) {
					sendPacket.setValue("DL_CCD", CardPacket.DL.FIRST);
				} else if(loopIndex == loopCount) {
					//마지막전송
					sendPacket.setValue("DL_CCD", CardPacket.DL.LAST);
				} else {
					//계속 전송
					sendPacket.setValue("DL_CCD", CardPacket.DL.CONTINUE);
				}
				
				//전송할 파일 데이터 가져오기
				byte[] data = sendBuffers.get(loopIndex-1);
				//패킷의 크기
				sendPacket.setValue("PACKET_SZ", data.length);

				//전송패킷을 조립하여 보내기
				out.write(sendPacket.serialize());
				//파일버퍼 보내기
				out.write(data);
			}//if(0 == loopIndex) {
			
			//전송패킷 로그
			if(log.isDebugEnabled()) log.debug(sendPacket.toString());
			
			//전송
			out.flush();
			
			int recvBytes = in.read(recvBuffer);
			//응답전문을 처리할 수신 패킷 생성
			CardPacket recvPacket = CardPacket.create();
			if(CommonUtil.isEmpty(recvBuffer) || recvBuffer.length != recvPacket.length()) {
				return ("img".equals(fileKind)) ? ERR_C.IMG_RESPONSE_WRONG : ERR_C.INF_RESPONSE_WRONG;
			}
			
			recvPacket.parse(recvBuffer);
			//수신패킷 로그
			if(log.isDebugEnabled()) log.debug(recvPacket.toString());
			
			//응답받은 DL_CCD
			String recvDlCcd = (String) recvPacket.getValue("DL_CCD");
			
			if(0 == loopIndex) {
				if(CardPacket.DL.SUCCESS.equals(recvDlCcd)) {
					loopIndex++;
				}
			} else {
				//파일전송패킷에 대한 응답일 경우
				if(CardPacket.DL.SUCCESS.equals(recvDlCcd)) {
					if(recvPacket.getPacketSize() == sendPacket.getPacketSize()) {
						loopIndex++;
					}
				} else {
					//성공이 아닌 경우
					return recvDlCcd;
				}
			}
		}//while(loopIndex < loopCount) {
		
		return ERR_C.SUCCESS;
		
	}
	/**
	 * 파일을 읽어서 byte 배열 리스트로 변환
	 * @param file
	 * @return
	 */
	private Vector<byte[]> readFiletoVector(File file) {
		Vector<byte[]> byteList = new Vector<byte[]>();
		BufferedInputStream bis = null;
		
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			
			byte[] buffer = new byte[CardPacket.PACKET_SZ];
			
			int readBytes = 0;
			while ((readBytes = bis.read(buffer)) > 0) {
				byte[] data = new byte[readBytes];
				System.arraycopy(buffer, 0, data, 0, readBytes);
				byteList.add(data);
			}
			
		} catch (IOException e) {
			log.error(SystemUtil.getExceptionLog(e));
		} finally {
			if(CommonUtil.isNotEmpty(bis)) { try {bis.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
		}
		
		return byteList;
	}
}
