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
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldFileUtil;
import com.people.common.oldutil.OldSystemUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardSendPdfUtil {
	private final String serverIp;
	private final int serverPort;
	
	public CardSendPdfUtil() throws IOException {
		Properties props = new Properties();//SystemUtil.getConfigProperties();
		serverIp = props.getProperty("PPR_CARD_SERVER_IP");
		serverPort = OldCommonUtil.safeObjToInt(props.getProperty("PPR_CARD_SERVER_PORT"));
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
		
		String eDocIdxNo = OldCommonUtil.safeObjToStr(map.get("E_DOC_IDX_NO"));	//??????????????????
		int    imgSer    = OldCommonUtil.safeObjToInt(map.get("IMG_SER"));		//?????????????????????
		String storPathNm = OldCommonUtil.safeObjToStr(map.get("STOR_PATH_NM"));//???????????????
		String procFileNm = OldCommonUtil.safeObjToStr(map.get("PROC_FILE_NM"));//???????????????
		String trx_br_c = OldCommonUtil.safeObjToStr(map.get("TRXBRNO"));		//???????????????
		String op_hwnno = OldCommonUtil.safeObjToStr(map.get("OPRT_JKW_NO"));	//??????????????????
		String tecccode = OldCommonUtil.safeObjToStr(map.get("TECC_C"));		//TECCCODE
		String fileCnt = OldCommonUtil.safeObjToStr(map.get("FILECNT"));		//????????????
		String docFormC = OldCommonUtil.safeObjToStr(map.get("DOC_FORM_C"));	//??????????????????

		//1 ????????????
		File cardPath = OldFileUtil.joinPaths(storPathNm, OldFileUtil.PATH.CARD);
		
		//1.1 ????????? ???????????? ??????
		OldFileUtil.mkdirs(storPathNm);
		
		
		File orgnPdfFile = new File(storPathNm, procFileNm);
		File cardPdfFile = new File(cardPath, procFileNm);
		
		if(OldFileUtil.copy(orgnPdfFile, cardPdfFile)) {
			log.info("file copy success");
		} else {
			log.info("file copy fail");
			return SendResult.FAIL;
		}
		
		//2 pdf ????????? ???????????? ???????????? 
		String pdfData = "";
		if(OldFileUtil.toBinaryString(cardPdfFile, pdfData)) {
			return SendResult.FILE_TO_BINARY_FAIL;
		}
		
		//3 socket ??????
		Socket socket = null;
		try {
			socket = new Socket(serverIp, serverPort);
			socket.setSoTimeout(20*1000);
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
			return SendResult.SOCKET_CONNECT_FAIL;
		}
		
		//4 ????????? json ?????????
		JSONObject sendJsonObj = new JSONObject();
		sendJsonObj.put("corFnCd","regShbDoc");
		sendJsonObj.put("psShpCocd","S001");
		sendJsonObj.put("bneZcd","04");
		sendJsonObj.put("psChlCcd","03");
		sendJsonObj.put("madCrtCcd","01");
		sendJsonObj.put("etCcd","000");
		sendJsonObj.put("crtDt",OldSystemUtil.nowTime("yyyyMMddHHmmssSS").substring(0,16));
		sendJsonObj.put("crtRmkEn",op_hwnno);
		sendJsonObj.put("tiRmkEn",op_hwnno);
		sendJsonObj.put("psHcd",trx_br_c);
		sendJsonObj.put("crtTmnIpAr",OldSystemUtil.getHostAddress());
		sendJsonObj.put("eccNo",tecccode);
		sendJsonObj.put("fileCnt",fileCnt);
		sendJsonObj.put("frmCd",docFormC);
		sendJsonObj.put("edocNo",eDocIdxNo);

		String jsonData = sendJsonObj.toJSONString();
		
		//5 ?????? packet ??????
		CardPdfSendPacket sendPacket = CardPdfSendPacket.create();
		sendPacket.setValue("DELIMITER1",  "^");
		sendPacket.setValue("JSON_SIZE",   jsonData.length());
		sendPacket.setValue("DELIMITER2",  "^");
		sendPacket.setValue("JSON_DATA",   jsonData, jsonData.length());//?????????????????????
		sendPacket.setValue("DELIMITER3",  "^");
		sendPacket.setValue("PDF_SIZE",    pdfData.length());
		sendPacket.setValue("DELIMITER4",  "^");
		sendPacket.setValue("PDF_DATA",    pdfData, pdfData.length());//?????????????????????
		
		sendPacket.setValue("PACKET_SIZE", sendPacket.length());
		
		DataInputStream in = null;
		DataOutputStream out = null;
		ByteArrayInputStream bais = null;
		
		try {
			//????????? ????????? ??????
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			//6 ???????????? ???????????? ??????
			//  ???????????? ??????
			bais = new ByteArrayInputStream(sendPacket.serialize());

			//  ???????????? ??????????????? ?????????
			int len = 0;
			byte[] buf = new byte[1024];
			while((len=bais.read(buf))  != 1) {
				out.write(buf, 0 , len);
			}
			
			//??????
			out.flush();
			
			//???????????? ??????
			log.info(sendPacket.toString());
			
			//7 ????????????
			byte[] recvBuffer = new byte[295];
			
			//   ??????????????????
			int recvBytes = in.read(recvBuffer);
			String recvMsg = new String(recvBuffer);
			
			//   ????????????
			if(0==recvBytes || OldCommonUtil.isEmpty(recvMsg)) {
				sendResult = SendResult.RESPONSE_DATA_WRONG;
			} else {
				//?????? ?????? ?????? ??????
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
				
				//????????????
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
			log.error(OldSystemUtil.getExceptionLog(e));
			sendResult = SendResult.FAIL;
		} finally {
			if(OldCommonUtil.isNotEmpty(bais)) { try {bais.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(in)) { try {in.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(out)) { try {out.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(socket)) { try {socket.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
		}
		
		return sendResult;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
