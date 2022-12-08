package com.people.html2pdf;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldSystemUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * html 데이터 및 관련 정보로부터 pdf 생성
 * 형광펜 삽입 및 스티커 파일 처리 후 TSA(타임스탬프)등을 처리하기 위한 서버로 multipart 전송
 * 파라미터
 * - htmlData : pdf 파일로 변환할 html data
 * - kubun : pdf1/pdf2 구분
 * - efskey : 전자문서번호(e_doc_idx_no)
 * - highLight : 형광펜 json data
 * - memo : 일반메모
 * - scanData : 스캔관련
 * - backupData : pdf2 파일 재현을 위한 json data(암호화될)
 * - restartkey : pdf2 파일 재처리를 위한 전자문서번호
 * @author mh042
 *
 */

@Slf4j
public class Html2Pdf {
	public void htm2pdf(HttpServletRequest req, HttpServletResponse res) throws Exception{
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-CREDENTALS", "true");
		res.setHeader("Access-Control-Allow-HEADERS", "Content-Type");
		req.setCharacterEncoding("UTF-8");
		
		//환경설정
		String rootPath = req.getSession().getServletContext().getRealPath("/");
		String localhotnm = OldSystemUtil.getHostName();
		
		Properties props = new Properties();//SystemUtil.getConfigProperties();
		String basePath = props.getProperty("PDF_BASEPATH"); //tmp pdf 파일 저정 경로
		String PDF_LOGPATH = props.getProperty("PDF_LOGPATH"); //로그파일 경로
		
		pdflogInit2(PDF_LOGPATH);
		
		String baseUrl = props.getProperty("HTML_RESOURCE");
		String receiveUrl = props.getProperty("RECEIVE_URL");
		
		String licensePath = rootPath + "/WEB-INF/config"+props.getProperty("PDFUTIL_LICENSE");

		if(!checkLicense(licensePath)) {
			log.error(OldSystemUtil.getExceptionLog(null));
		}
	}
	
	private void pdflogInit2(String logPath) {
		
	}
	
	private boolean checkLicense(String licensePath) throws Exception{
		boolean resultStr = false;
		
		String FS = File.separator;
		FileInputStream fis = null;
		if(OldCommonUtil.isEmpty(licensePath)) {
			throw new Exception("license path wrong~");
		} 
		
		Properties props = new Properties();//SystemUtil.getConfigProperties();
		
		
	
		return resultStr;
	}
}
