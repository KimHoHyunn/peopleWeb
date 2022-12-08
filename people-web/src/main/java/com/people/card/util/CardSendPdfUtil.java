package com.people.card.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.people.card.packet.CardPdfRecvPacket;
import com.people.card.packet.CardPdfSendPacket;
import com.people.common.oldutil.CommonUtil;
import com.people.common.oldutil.FileUtil;
import com.people.common.oldutil.SystemUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardSendPdfUtil {
	private final String serverIp;
	private final int serverPort;
	
	public CardSendPdfUtil() throws IOException {
		Properties props = new Properties();//SystemUtil.getConfigProperties();
		serverIp = props.getProperty("PPR_CARD_SERVER_IP");
		serverPort = CommonUtil.safeObjToInt(props.getProperty("PPR_CARD_SERVER_PORT"));
	}
	
	@Getter
	public static enum SendResult {
		SUCCESS 								("","","card send success"),
		FAIL	 								("999","0707","card send fail"),
		SOCKET_CONNECT_FAIL 					("91","0708","socket connection fail"),
		FILE_COPY_FAIL		 					("92","0705","pdf file copy fail"),
		RESPONSE_DATA_WRONG 					("93","0709","server response data is wrong"),
		FILE_TO_BINARY_FAIL 					("94","0713","pdf file binary make fail"),
		FILE_COUNT_NOT_MATCH 					("95","0714","pdf filecnt not match"),
		PACKET_TOTAL_SIZE_WRONG 				("96","0715","pdf packet total size is wrong"),
		PACKET_TOTAL_SIZE_NEXT_GUBUNZA_WORNG 	("97","0716","pdf packet total size next gubunza data is wrong"),
		PACKET_JSON_SIZE_WRONG 					("98","0717","pdf packet json size is wrong"),
		PACKET_JSON_SIZE_NEXT_GUBUNZA_WORNG 	("99","0718","pdf packet json size next gubunza data is wrong"),
		PACKET_JSON_DATA_WRONG 					("100","0719","pdf packet json data is wrong"),
		PACKET_JSON_DATA_NEXT_GUBUNZA_WORNG 	("101","0720","pdf packet json data next gubunza data is wrong"),
		PACKET_FILE_SIZE_WRONG 					("102","0721","pdf packet pdffile size is wrong"),
		PACKET_FILE_SIZE_NEXT_GUBUNZA_WORNG 	("103","0722","pdf packet pdffile size next gubunza data is wrong"),
		PACKET_FILE_SAVE_ERROR	 				("104","0723","pdf packet pdffile data save error"),
		PPR_SERVER_ERROR					 	("105","0724","card ppr server error"),
		RECEIVE_MSG_NOT_MATCH				 	("106","0725","receive msg not match"),
		;
		
		private final String code;
		private final String eDcoProcErrC;
		private final String eDocErrCtnt;
		
		private SendResult(String code, String eDcoProcErrC, String eDocErrCtnt) {
			this.code = code;
			this.eDcoProcErrC = eDcoProcErrC;
			this.eDocErrCtnt = eDocErrCtnt;
		}
		
	}
	
	public SendResult sendCard(Map<String, Object> map) {
		SendResult sendResult = SendResult.SUCCESS;
		
		String eDocIdxNo = CommonUtil.safeObjToStr(map.get("E_DOC_IDX_NO"));	//전자문서번호
		int    imgSer    = CommonUtil.safeObjToInt(map.get("IMG_SER"));		//이미지일련번호
		String storPathNm = CommonUtil.safeObjToStr(map.get("STOR_PATH_NM"));//저장경로명
		String procFileNm = CommonUtil.safeObjToStr(map.get("PROC_FILE_NM"));//처리파일명
		String trx_br_c = CommonUtil.safeObjToStr(map.get("TRXBRNO"));		//거래점번호
		String op_hwnno = CommonUtil.safeObjToStr(map.get("OPRT_JKW_NO"));	//조작자행번호
		String tecccode = CommonUtil.safeObjToStr(map.get("TECC_C"));		//TECCCODE
		String fileCnt = CommonUtil.safeObjToStr(map.get("FILECNT"));		//파일개수
		String docFormC = CommonUtil.safeObjToStr(map.get("DOC_FORM_C"));	//문서양식코드

		//1 파일복사
		File cardPath = FileUtil.joinPaths(storPathNm, FileUtil.PATH.CARD);
		
		//1.1 복사할 디렉토리 생성
		FileUtil.mkdirs(storPathNm);
		
		
		File orgnPdfFile = new File(storPathNm, procFileNm);
		File cardPdfFile = new File(cardPath, procFileNm);
		
		if(FileUtil.copy(orgnPdfFile, cardPdfFile)) {
			log.info("file copy success");
		} else {
			log.info("file copy fail");
			return SendResult.FAIL;
		}
		
		//2 pdf 파일을 바이너리 문자열로 
		String pdfData = "";
		if(FileUtil.toBinaryString(cardPdfFile, pdfData)) {
			return SendResult.FILE_TO_BINARY_FAIL;
		}
		
		//3 socket 연결
		Socket socket = null;
		try {
			socket = new Socket(serverIp, serverPort);
			socket.setSoTimeout(20*1000);
		} catch (Exception e) {
			log.error(SystemUtil.getExceptionLog(e));
			return SendResult.SOCKET_CONNECT_FAIL;
		}
		
		//4 전송할 json 만들기
		JSONObject sendJsonObj = new JSONObject();
		sendJsonObj.put("corFnCd","regShbDoc");
		sendJsonObj.put("psShpCocd","S001");
		sendJsonObj.put("bneZcd","04");
		sendJsonObj.put("psChlCcd","03");
		sendJsonObj.put("madCrtCcd","01");
		sendJsonObj.put("etCcd","000");
		sendJsonObj.put("crtDt",SystemUtil.nowTime("yyyyMMddHHmmssSS").substring(0,16));
		sendJsonObj.put("crtRmkEn",op_hwnno);
		sendJsonObj.put("tiRmkEn",op_hwnno);
		sendJsonObj.put("psHcd",trx_br_c);
		sendJsonObj.put("crtTmnIpAr",SystemUtil.getHostAddress());
		sendJsonObj.put("eccNo",tecccode);
		sendJsonObj.put("fileCnt",fileCnt);
		sendJsonObj.put("frmCd",docFormC);
		sendJsonObj.put("edocNo",eDocIdxNo);

		String jsonData = sendJsonObj.toJSONString();
		
		//5 전송 packet 준비
		CardPdfSendPacket sendPacket = CardPdfSendPacket.create();
		sendPacket.setValue("DELIMITER1",  "^");
		sendPacket.setValue("JSON_SIZE",   jsonData.length());
		sendPacket.setValue("DELIMITER2",  "^");
		sendPacket.setValue("JSON_DATA",   jsonData, jsonData.length());//가변길이데이터
		sendPacket.setValue("DELIMITER3",  "^");
		sendPacket.setValue("PDF_SIZE",    pdfData.length());
		sendPacket.setValue("DELIMITER4",  "^");
		sendPacket.setValue("PDF_DATA",    pdfData, pdfData.length());//가변길이데이터
		
		sendPacket.setValue("PACKET_SIZE", sendPacket.length());
		
		DataInputStream in = null;
		DataOutputStream out = null;
		ByteArrayInputStream bais = null;
		
		try {
			//송수신 스트림 생성
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			//6 전송패킷 조립하여 전송
			//  전송패킷 조립
			bais = new ByteArrayInputStream(sendPacket.serialize());

			//  전송패킷 스트림으로 보내기
			int len = 0;
			byte[] buf = new byte[1024];
			while((len=bais.read(buf))  != 1) {
				out.write(buf, 0 , len);
			}
			
			//전송
			out.flush();
			
			//전송패킷 로그
			log.info(sendPacket.toString());
			
			//7 응답수신
			byte[] recvBuffer = new byte[295];
			
			//   응답수신하기
			int recvBytes = in.read(recvBuffer);
			String recvMsg = new String(recvBuffer);
			
			//   응답확인
			if(0==recvBytes || CommonUtil.isEmpty(recvMsg)) {
				sendResult = SendResult.RESPONSE_DATA_WRONG;
			} else {
				//응답 수신 패킷 생성
				CardPdfRecvPacket recvPacket = CardPdfRecvPacket.create();
				recvPacket.parse(recvBuffer);
				
				String corCode = "";
				String corMsg = "";
				if(1<recvPacket.getJsonSize()) {
					JSONParser jsonParser = new JSONParser();
					JSONObject recvJsonObj = (JSONObject) jsonParser.parse((String) recvPacket.getValue("JSON_DATA"));
					corCode = recvJsonObj.get("corCode").toString();
					corMsg  = recvJsonObj.get("corMsg").toString();
				}
				
				//응답코드
				if("OK".equals(corCode)) {
					sendResult = SendResult.SUCCESS;
				} else if("S00001".equals(corCode)) {
					sendResult = SendResult.PACKET_TOTAL_SIZE_WRONG;
				} else if("S00002".equals(corCode)) {
					sendResult = SendResult.PACKET_TOTAL_SIZE_NEXT_GUBUNZA_WORNG;
				} else if("S00003".equals(corCode)) {
					sendResult = SendResult.PACKET_JSON_SIZE_WRONG;
				} else if("S00004".equals(corCode)) {
					sendResult = SendResult.PACKET_JSON_SIZE_NEXT_GUBUNZA_WORNG;
				} else if("S00005".equals(corCode)) {
					sendResult = SendResult.PACKET_JSON_DATA_WRONG;
				} else if("S00006".equals(corCode)) {
					sendResult = SendResult.PACKET_JSON_DATA_NEXT_GUBUNZA_WORNG;
				} else if("S00007".equals(corCode)) {
					sendResult = SendResult.PACKET_FILE_SIZE_WRONG;
				} else if("S00008".equals(corCode)) {
					sendResult = SendResult.PACKET_FILE_SIZE_NEXT_GUBUNZA_WORNG;
				} else if("S00009".equals(corCode)) {
					sendResult = SendResult.PACKET_FILE_SAVE_ERROR;
				} else if("P00001".equals(corCode)) {
					sendResult = SendResult.PPR_SERVER_ERROR;
				} else {
					sendResult = SendResult.RECEIVE_MSG_NOT_MATCH;
				}

			}//if(0==recvBytes || CUtil.isEmpty(recvMsg)) {
		} catch (Exception e) {
			log.error(SystemUtil.getExceptionLog(e));
			sendResult = SendResult.FAIL;
		} finally {
			if(CommonUtil.isNotEmpty(bais)) { try {bais.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
			if(CommonUtil.isNotEmpty(in)) { try {in.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
			if(CommonUtil.isNotEmpty(out)) { try {out.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
			if(CommonUtil.isNotEmpty(socket)) { try {socket.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
		}
		
		return sendResult;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
