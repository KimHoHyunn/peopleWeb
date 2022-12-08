package com.people.card.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.people.card.vo.EpsBodyVO;
import com.people.card.vo.EpsHeaderVO;
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldFileUtil;
import com.people.common.oldutil.OldSystemUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/** 
 * EAI 통신용 xml header
 * @author mh042
 *
 */
@Setter
@Getter
@Slf4j
public class CardEaiUtil {
	EpsHeaderVO header;
	EpsBodyVO body;
	
	String eai_ip;
	
	public File send(Map<String, Object> map)  {
		//1 EAI Config 정보 가져오기
		Properties props = null;
		try {
			props = new Properties();//SystemUtil.getConfigProperties();
		} catch (Exception e) {
			return null;
		}

		eai_ip = props.getProperty("EAI_IP");
		
		//2 send xml 데이터 조립
		setData(map);
		
		//xml 생성을 위한 데이터 get
		String storPathNm = OldCommonUtil.safeObjToStr(map.get("STOR_PATH_NM"));
		String eDocIdxNo  = OldCommonUtil.safeObjToStr(map.get("E_DOC_IDX_NO"));
		String docFormC   = OldCommonUtil.safeObjToStr(map.get("DOC_FORM_C"));
		
		//xml 생성할 디렉토리 지정
		File xmlPath = OldFileUtil.joinPaths(storPathNm, OldFileUtil.PATH.CARD);
		OldFileUtil.mkdirs(xmlPath.getPath());

		//xml file name
		String sendFileName = "S_"+eDocIdxNo+"_"+docFormC+".xml";
		String recvFileName = "R_"+eDocIdxNo+"_"+docFormC+".xml";
		
		//3 send xml 생성
		File sendFile = makeSendXml(OldFileUtil.joinPaths(xmlPath.getPath(), sendFileName));
		if(sendFile.exists()) {
			log.info("send xml create success");
		} else {
			log.error("send xml create fail");
			return null;
		}
		
		//4 EAI 통신
		String url = "http://"+eai_ip+":8080/inf/SCOM1110A";
		File recvFile = sendEps(url, sendFile, OldFileUtil.joinPaths(xmlPath.getPath(), recvFileName));
		
		if(recvFile.exists()) {
			log.info("receive xml create success");
		} else {
			log.info("receive xml create fail");
			return null;
		}
		
		return recvFile;
	}
	
	private File sendEps(String eaiUrl, File sendFile, File recvFile) {
		FileInputStream fisSend = null;
		InputStreamReader isrSend = null;
		BufferedReader brSend = null;
		
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStreamReader isrRecv = null;
		BufferedReader brRecv = null;
		
		try {
			fisSend = new FileInputStream(sendFile);
			isrSend = new InputStreamReader(fisSend, "utf-8");
			brSend = new BufferedReader(isrSend);
			
			StringBuffer sbSendXml = new StringBuffer(1000);
			char[] buf = new char[1024];
			int numRead = 0;
			while((numRead = brSend.read(buf)) != -1) {
				sbSendXml.append(buf,0,numRead);
			}
			
			URL url = new URL(eaiUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", Integer.toString(sbSendXml.length()));
			
			os = conn.getOutputStream();
			os.write(sbSendXml.toString().getBytes("utf-8"));
			os.flush();
			
			int rc = conn.getResponseCode();
			if(HttpURLConnection.HTTP_OK == rc) {
				isrRecv = new InputStreamReader(conn.getInputStream(), "utf-8");
				brRecv = new BufferedReader(isrRecv);
				
				//수신
				String responseString = "";
				String strLine = "";
				while((strLine = brRecv.readLine()) !=null) {
					responseString+=strLine;
				}
				
				if(OldCommonUtil.isEmpty(responseString)) {
					log.error("Nothing response message");
				} else {
					Document doc = new SAXBuilder().build(new StringReader(responseString));
					
					recvFile = writeDocToFile(doc, recvFile);
				}
			} else {
				log.info("http response code = "+ rc);
			}
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
		} finally {
			if(OldCommonUtil.isNotEmpty(fisSend)) { try {fisSend.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(isrSend)) { try {isrSend.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(brSend)) { try {brSend.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(os)) { try {os.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(isrRecv)) { try {isrRecv.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(brRecv)) { try {brRecv.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
		}
		return recvFile;
	}
	
	private File makeSendXml(File sendFile) {
		Document doc = new Document();
		Element root = new Element("eps");
		
		root.addContent(header.makeHeader());
		root.addContent(body.makeAppData());
		root.addContent(body.makeActData());
		root.addContent(body.makeData());
		doc.setRootElement(root);
		return writeDocToFile(doc, sendFile);
	}
	private File writeDocToFile(Document doc, File file) {
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(file);
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setEncoding("UTF-8");
			f.setIndent("");
			f.setLineSeparator("\r\n");
			f.setTextMode(Format.TextMode.TRIM);
			serializer.setFormat(f);
			serializer.output(doc, out);
			out.flush();
		} catch (IOException e) {
			log.error(OldSystemUtil.getExceptionLog(e));
		} finally {
			if(OldCommonUtil.isNotEmpty(out)) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(OldSystemUtil.getExceptionLog(e));
				}
			}
		}
		return file;
	}
	private void setData(Map<String, Object> map) {
		header = new EpsHeaderVO();
		body = new EpsBodyVO();
		
		//현재시간
		String nowTime = OldCommonUtil.safeObjToStr(map.get("nowTime"));
		if(OldCommonUtil.isEmpty(nowTime)) {
			nowTime = OldSystemUtil.nowTime("yyyyMMddHHmmssSS").substring(0,16);
		}
		
		//중복방지를 위한 seqNum 추가
		String seqNum = OldCommonUtil.safeObjToStr(map.get("seqNum"));
		if(OldCommonUtil.isEmpty(seqNum)) {
			seqNum = "01";
		} else if(1==seqNum.length()) {
			seqNum = "0"+seqNum;
		} else if(2<seqNum.length()) {
			seqNum = seqNum.substring(seqNum.length()-2);
		}
		
		String ip_adr = OldSystemUtil.getHostAddress(); 					//IP
		String chan_tel_snd_dttm = nowTime;							//전문전송일시
		String trx_br_c = OldCommonUtil.safeObjToStr(map.get("TRXBRNO"));	//거래점번호
		String op_hwnno = OldCommonUtil.safeObjToStr(map.get("OPRT_JKW_NO"));	//조작자행번호
		String glb_uiq_id = nowTime+"DGW"+"800"+op_hwnno+seqNum;
		String svr_tel_rcv_dttm = nowTime;							//전문수신일시
		String bpr_ecc_n = OldCommonUtil.safeObjToStr(map.get("TECC_C"));	//TECC Code
		
		//header set
		header.setEdoc_ui_g("01");
		header.setEdoc_trm_g("80");
		header.setUi_ver("1.0.0");
		header.setIp_adr(ip_adr);
		header.setClient_mac_adr("999999999999");
		header.setClient_device_id("");
		header.setGrpco_c("S001");
		header.setChan_tel_snd_dttm(chan_tel_snd_dttm);
		header.setIn_scr_no("543090000");
		header.setIn_svc_id("54309000002");
		header.setSession_id("0");
		header.setTrx_br_c(trx_br_c);
		header.setOp_hwnno(op_hwnno);
		header.setNfit_item_incl_yn("N");
		header.setGlb_uiq_id(glb_uiq_id);
		header.setProc_sys_g("04");
		header.setProc_svc_id("SBPR50521A");
		header.setTrx_trm_no("0000");
		header.setSvr_tel_rcv_dttm(svr_tel_rcv_dttm);
		header.setProc_rslt_yn("0");
		header.setFail_sys_c("");
		header.setFail_k("");
		header.setErr_c("");
		header.setErr_c_ctnt("");
		header.setAdd_msg("");
		header.setClient_public_key("");
		header.setForm_occr_g("");
		
		body.setData_actsys1_bpr_ecc_n(bpr_ecc_n);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
