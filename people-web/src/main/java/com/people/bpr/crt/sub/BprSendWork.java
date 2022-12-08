package com.people.bpr.crt.sub;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.bpr.dao.DaoUpdate;
import com.people.bpr.util.BprInfCreateUtil;
import com.people.common.dao.EdsDao;
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldFileUtil;
import com.people.common.oldutil.OldSystemUtil;
import com.people.common.vo.EDocErrHisVO;
import com.people.common.vo.SndRstVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BprSendWork implements Callable<SndRstVO> {
	
	private SndRstVO sndRstVO;
	private Map<String, Object> map;
	private String bprImgUrl;
	private String bprImgSubUrl;
	private String masterDocFormC;
	private List<String> mergeDocList;
	private Map<String, Object> eDocInfo;
	private String inIdxPath;
	
	private static final class IMG_JOB_U {
		public static final String IMG = "3";
		public static final String IDX = "2";
	}
	
	private String md_eDocSer;
	private String md_eDocIndvIdxNo;
	private String md_formC;
	private String md_imgKey;
	private String md_eccNo;
	private int    imgSer;
	private String docFormC;
	
	private List<File> fileList = new ArrayList<File>();
	private File infFile = null;
	
	
	
	public BprSendWork(SndRstVO sndRstVO, Map<String, Object> map, String bprImgUrl, String bprImgSubUrl, String masterDocFormC, 
			List<String> mergeDocList, Map<String, Object> eDocInfo, String inIdxPath) {
		this.sndRstVO = sndRstVO;
		this.map = map;
		this.bprImgUrl = bprImgUrl;
		this.bprImgSubUrl = bprImgSubUrl;
		this.masterDocFormC = masterDocFormC;
		this.mergeDocList = mergeDocList;
		this.eDocInfo = eDocInfo;
		this.inIdxPath = inIdxPath;
		
		this.md_eDocSer = OldCommonUtil.safeObjToStr(eDocInfo.get("E_DOC_IDX_NO"));
		this.docFormC   = OldCommonUtil.safeObjToStr(map.get("DOC_FORM_C"));
	}
	
	public SndRstVO call() {
		if(OldCommonUtil.isNotEmpty(md_eDocSer)) {
			sndRstVO.setEDocIdxNo(md_eDocSer);
		}
		
		sndRstVO.setDbResult(false);
		
		//inf 생성
		if(generateInf(IMG_JOB_U.IMG)) {
			
		} else {
			
		}//if(generateInf(IMG_JOB_U.IMG)) {
		
		
		return sndRstVO;
	}
	
	private boolean generateInf(String imgJobU) {
		/* inf 파일 수집-------------------------------------------------------------------------------------------------------*/
		String c_scanHwnno		= OldCommonUtil.safeObjToStr(eDocInfo.get("PORT_JKW_NO"));//스캔행원번호
		String c_sendHwnno		= OldCommonUtil.safeObjToStr(eDocInfo.get("PORT_JKW_NO"));//전송행원번호
		String c_affairsHwnno	= OldCommonUtil.safeObjToStr(eDocInfo.get("PORT_JKW_NO"));//업무담당자행번호
		String md_scnHwnno		= OldCommonUtil.safeObjToStr(eDocInfo.get("PORT_JKW_NO"));//접속자 행번호

		String c_brno			= OldCommonUtil.safeObjToStr(eDocInfo.get("TRXBRNO"));//점번호
		String c_affirsBrno		= OldCommonUtil.safeObjToStr(eDocInfo.get("TRXBRNO"));//업무담당자점번호
		String md_scnBrno		= OldCommonUtil.safeObjToStr(eDocInfo.get("TRXBRNO"));//영업점번호

		String c_scanDt			= OldCommonUtil.safeObjToStr(map.get("DR_DTTM"));//스캔일자
		String c_sendDt			= OldCommonUtil.safeObjToStr(map.get("DR_DTTM"));//전송일자
		String md_memo			= OldCommonUtil.safeObjToStr(eDocInfo.get("MEMO"));//메모

		//index key
		String if_imgKey		= OldCommonUtil.safeObjToStr(map.get("IMG_IDX_NO"));	//이미지 키
		String fi_storPathNm	= OldCommonUtil.safeObjToStr(map.get("STOR_PATH_NM"));	//이미지 저장 경로
		String md_dataDrdt		= OldCommonUtil.safeObjToStr(map.get("TO_DATE"));		//전송일자
		md_imgKey               = OldCommonUtil.safeObjToStr(map.get("IMG_IDX_NO"));
		md_eccNo                = OldCommonUtil.safeObjToStr(map.get("TECC_C"));

		String md_trxdt			= OldCommonUtil.safeObjToStr(map.get("TRXDT"));			//영업점거래일자
		String md_brno			= OldCommonUtil.safeObjToStr(eDocInfo.get("TRXBRNO"));	//영업점번호
		
		md_eDocIndvIdxNo        = OldCommonUtil.safeObjToStr(map.get("E_DOC_INV_IDX_NO"));//전자문서 개발인덱스번호
		String md_eDocG			= OldCommonUtil.safeObjToStr(map.get("E_DOC_G"));//전자문서구분
		imgSer                  = OldCommonUtil.safeObjToInt(map.get("IMG_SER"));
		String bpr_e_upmu_g		= OldCommonUtil.safeObjToStr(map.get("BPR_E_UPMU_G"));
		String md_scanYn		= OldCommonUtil.safeObjToStr(map.get("SCANYN"));//첨부스캔여부
		String md_ingamYn		= OldCommonUtil.safeObjToStr(map.get("INGAMYN"));//인감여부
		
		if(OldCommonUtil.isEmpty(docFormC)) {
			return processNullFormCode(md_eDocIndvIdxNo, imgSer);
		}
		
		//서식코드를 내부표준 형식으로 변경 0-000-0000
		md_formC = OldCommonUtil.getInfCodeType(docFormC);
		
		//ECC 코드 추출 예) '12345121212121212121 < SLKFJOASEJLKAJDFLKASJELKJLD DFJEKJFD5Z >' => '12345121212121212121'
		md_eccNo = OldCommonUtil.getEccNo(md_eccNo);
		/* inf 파일 수집-------------------------------------------------------------------------------------------------------*/
		
		/**
		 * 이미지작업 유형에 따라 경로 및 파일 확장자 처리
		 * IMG(3) 인 경우 : 이미지 
		 * 	- 경로 : /img
		 *  - 확장자 : .tif
		 * IDX(2) 인 경우 : 포스틱메모
		 *  - 경로 : /idx
		 *  - 확장자 : .idx
		 */
		String jobPath = IMG_JOB_U.IDX.equals(imgJobU) ? OldFileUtil.PATH.IDX : OldFileUtil.PATH.IMAGE;
		String jobExtt = IMG_JOB_U.IDX.equals(imgJobU) ? OldFileUtil.EXT.IDX : OldFileUtil.EXT.IMAGE;
		
		//전송할 이미지 파일 리스트 생성 및 존재 확인
		String tmpFileNm = md_formC + "," + md_imgKey + jobExtt;
		File presentFile = OldFileUtil.joinPaths(fi_storPathNm, jobPath, tmpFileNm);
		
		if(IMG_JOB_U.IDX.equals(imgJobU)) {
			//포스틱메모
			File inFile = OldFileUtil.joinPaths(inIdxPath, docFormC+jobExtt);
			if(OldFileUtil.exists(inFile)) {
				OldFileUtil.copy(inFile, presentFile);
				fileList.add(presentFile);
			}
		} else {
			//image
			fileList.add(presentFile);
		}
		
		//첨부서식이 있는 경우 파일 리스트에 추가
		if(OldCommonUtil.isNotEmpty(mergeDocList)) {
			for(String mergeDocFormC : mergeDocList) {
				String tmpSubFileNm = OldCommonUtil.getInfCodeType(mergeDocFormC)+","+md_imgKey+jobExtt;
				String tmpStorPath  = OldCommonUtil.safeReplaceAll(fi_storPathNm, masterDocFormC, mergeDocFormC);
				
				File subFile = OldFileUtil.joinPaths(tmpStorPath, jobPath, tmpSubFileNm);
				if(IMG_JOB_U.IDX.equals(imgJobU)) {
					//포스틱메모
					File inFile = OldFileUtil.joinPaths(inIdxPath, docFormC+jobExtt);
					if(OldFileUtil.exists(inFile)) {
						OldFileUtil.copy(inFile, presentFile);
						fileList.add(presentFile);
					} else {
						continue;
					}
				}
				
				fileList.add(subFile);
			}
			
		}//if(CUtil.isNotEmpty(mergeDocList)) {
		
		
		//이미지 실제 여부 확인
		for(File file : fileList) {
			if(file.exists() == false ) {
				boolean renameResult = processFileNOtExist(imgSer, presentFile.getPath(), file, md_eDocIndvIdxNo);
				if(false == renameResult) {
					return false;
				}
			}
		}
		
		//inf 정보생성---------------------------------------------------------------------------------------------------
		BprInfCreateUtil bicu = new BprInfCreateUtil();
		String c_agtSysC	="01";//agent시스템코드
		String c_grpcoC		="S001";//그룹사코드
		String c_bprUpmuG	="";//bpr업무구분
		String c_imgUpmuG	="";//image업무구분
		String c_agtUpmuG	="";//agent업무구분
		String c_imgJobU	=imgJobU;//image작업유형
		String c_imgCmmtYn	=IMG_JOB_U.IDX.equals(imgJobU) ? "1" : "0" ;//image주석여부
		String c_scanPstn	="5";//스캔위치
		String wf_wfYn		= "";
		
		//bpr 전자업무 구분
		if("01".equals(c_bprUpmuG)) {
			c_bprUpmuG	="01";//bpr업무구분
			c_imgUpmuG	="43";//image업무구분
			c_agtUpmuG	="0154";//agent업무구분
		} else {
			c_bprUpmuG	="05";//bpr업무구분
			if("4".equals(md_eDocG)) {
				c_imgUpmuG	="81";//image업무구분
				c_agtUpmuG	="0509";//agent업무구분
			} else {
				c_imgUpmuG	="82";//image업무구분
				c_agtUpmuG	="0510";//agent업무구분
			}
			wf_wfYn = "1";
		}//if("01".equals(c_bprUpmuG)) {
		
		//common
		bicu.setC_agtSysC	  	(c_agtSysC		);
		bicu.setC_grpcoC		(c_grpcoC		);   
		bicu.setC_bprUpmuG		(c_bprUpmuG		);
		bicu.setC_imgUpmuG		(c_imgUpmuG		);
		bicu.setC_agtUpmuG		(c_agtUpmuG		);
		bicu.setC_imgJobU		(c_imgJobU		);
		bicu.setC_imgCmmtYn		(c_imgCmmtYn	);
		bicu.setC_scanHwnno		(c_scanHwnno	);
		bicu.setC_sendHwnno		(c_sendHwnno	);
		bicu.setC_affairsHwnno	(c_affairsHwnno	);
		bicu.setC_brno			(c_brno			); 
		bicu.setC_affirsBrno	(c_affirsBrno	);
		bicu.setC_scanDt		(c_scanDt		);   
		bicu.setC_sendDt		(c_sendDt		);   
		bicu.setC_scanPstn		(c_scanPstn		);
		
		//workflow
		bicu.setWf_wfYn(wf_wfYn);
		
		//index key
		bicu.setIf_imgKey(if_imgKey);
		
		//file info
		bicu.setMd_fileCnt(String.valueOf(fileList.size()));
		bicu.setFileList(fileList);
		
		//meta data
		bicu.setMd_scnGrpcoC	(c_grpcoC	     );
		bicu.setMd_scnBrno		(md_scnBrno		 );
		bicu.setMd_scnHwnno		(md_scnHwnno	 );
		bicu.setMd_dataDrdt		(md_dataDrdt	 );
		bicu.setMd_imgKey		(md_imgKey		 );
		bicu.setMd_eccNo		(md_eccNo		 );
		bicu.setMd_formC		(md_formC		 );
		bicu.setMd_fileCnt		(String.valueOf(fileList.size()));
		bicu.setMd_trxdt		(md_trxdt		 );
		bicu.setMd_brno			(md_brno		 );
		bicu.setMd_totCnt		(String.valueOf(fileList.size()));
		bicu.setMd_chanU		("A0"			 );
		bicu.setMd_eDocSer		(md_eDocSer		 );
		bicu.setMd_eDocIndvIdxNo(md_eDocIndvIdxNo);
		bicu.setMd_eDocG		(md_eDocG		 );
		bicu.setMd_scanYn		(md_scanYn		 );
		bicu.setMd_ingamYn		(md_ingamYn		 );
		bicu.setMd_memo			(md_memo		 );	
		
		//inf 정보생성---------------------------------------------------------------------------------------------------
		
		//inf 파일 생성
		String infFileName = md_imgKey + OldFileUtil.EXT.INF;
		infFile = OldFileUtil.joinPaths(fi_storPathNm, jobPath, infFileName);
		
		boolean infResult = bicu.createInfFile(infFile);
		
		if(infResult == false) {
			processBprSndWorkFailure("2", "inf file create faile", "0603", "inf not exist", imgSer);
		} 
		
		sendToBprAgent();
		
		return infResult;
	}
	
	private boolean processNullFormCode(String md_eDocIndvIdxNo, int imgSer) {
		
		String errmsg = "error";
		
		sndRstVO.setStatusCode("2");
		sndRstVO.setMsg(errmsg);
		
		EdsDao.registerErrHis(md_eDocIndvIdxNo, imgSer, 2, "0601", errmsg);
		
		increaseTryCntOfMergeDocList();
		
		return false;
	}
	
	private boolean processFileNOtExist(int imgSer, String imgFileNm, File file, String eDocIndvIdxNo) {
		EDocErrHisVO errVO = new EDocErrHisVO();
		
		String eDocErrCtnt = "img file not exist";
		File searchFile = new File(file.getParent());
		File[] files = searchFile.listFiles();
		if(errVO.setData(imgFileNm, imgSer, imgSer, eDocIndvIdxNo, eDocErrCtnt)) {
			if(OldCommonUtil.isEmpty(files)) {
				EdsDao.insertErrHis(errVO);
				increaseTryCntOfMergeDocList();
				sndRstVO.setStatusCode("2");
				sndRstVO.setMsg(eDocErrCtnt);
				
				return false;
			} else {
				File tFile = null;
				
				//rename할 파일 찾기 (대표서식만~) 대표서식은 0-000-0000 형식이 파일명에포함되어져 있음.
				for(File targetFile : files) {
					if(targetFile.getName().contains("-")) {
						tFile = targetFile;
						break;
					}
					
				}
				
				if(OldFileUtil.exists(tFile)) {
					if(tFile.renameTo(file) == false) {
						EdsDao.insertErrHis(errVO);
						increaseTryCntOfMergeDocList();
						sndRstVO.setStatusCode("2");
						sndRstVO.setMsg(eDocErrCtnt + " rename Fail");
						return false;
					}
				} else {
					EdsDao.insertErrHis(errVO);
					increaseTryCntOfMergeDocList();
					sndRstVO.setStatusCode("2");
					sndRstVO.setMsg(eDocErrCtnt);
					return false;
				}//if(FileUtil.exists(tFile)) {
			}
		}//if(errVO.setData(imgFileNm, imgSer, imgSer, eDocIndvIdxNo, eDocErrCtnt)) {
		return true;
	}
	
	/**
	 * BPR Agent 으로 전송 실패한경우 에러 정보 저장
	 * @param statusCode
	 * @param msg
	 * @param eDocProcErrC
	 * @param eDocErrCtnt
	 * @param imgSer
	 */
	private void processBprSndWorkFailure(String statusCode, String msg, String eDocProcErrC, String eDocErrCtnt, int imgSer) {
		sndRstVO.setStatusCode(statusCode);
		sndRstVO.setMsg(msg);
		EdsDao.registerErrHis(md_eDocSer, imgSer, 2, eDocProcErrC, eDocErrCtnt);
		increaseTryCntOfMergeDocList();
	}
	
	private void increaseTryCntOfMergeDocList() {
		if(OldCommonUtil.isNotEmpty(mergeDocList)) {
			for(String mergeDocFormC : mergeDocList) {
				DaoUpdate.updateEdocFileMngTryCntPlus(md_eDocSer, 999, 2, mergeDocFormC );
			}
		}
	}
	
	private void sendToBprAgent() {
		String url = bprImgUrl + bprImgSubUrl;
		String boundary = Long.toString(System.currentTimeMillis());
		
		HttpURLConnection conn = null;
		
		try {
			URL cUrl = new URL(url);
			conn = (HttpURLConnection) cUrl.openConnection();
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
		}
		
		if(null == conn) {
			processBprSndWorkFailure("2", "connect fail", "0604", "connect fail", imgSer);
			return ;
		}
		
		DataOutputStream dos = null;
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data : boundary="+boundary);
			
			//이미지 파일 전송
			dos = new DataOutputStream(conn.getOutputStream());
			for(File file : fileList) {
				dos.writeBytes("--"+boundary+"\r\n");
				dos.writeBytes("Conten-Dispostion:form-data:name=\"imageFile1\";filename=\""+file.getName()+""+"\""+"\r\n");
				dos.writeBytes("\r\n");
			}
			//inf 파일을 읽어서 출석 스트림으로 전송
			writeFileToDataOutStream(infFile, dos);
			
			dos.writeBytes("\r\n");
			dos.writeBytes("--"+boundary+"\r\n");
			dos.flush();
			
			//이미지파일과 inf파일 전송 후 bpr 응답 수신
			is = conn.getInputStream();
			
			byte[] buff = new byte[1024];
			int len = -1;
			bos = new ByteArrayOutputStream();
			while((len = is.read(buff, 0, buff.length)) != -1) {
				bos.write(buff, 0, len);
			}
			
			//응답결과 저장
			String resultStr = new String(bos.toByteArray(), "UTF-8");
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> resultMap = mapper.readValue(resultStr, new TypeReference<Map<String, Object>>() {});
			
			if(null != resultMap) {
				String bprSuccYn = OldCommonUtil.safeObjToStr(resultMap.get("succYn"));
				String bprmsg    = OldCommonUtil.safeObjToStr(resultMap.get("msg"));
				
				if(OldCommonUtil.isNotEmpty(bprSuccYn) && OldCommonUtil.isNotEmpty(bprmsg) && "Y".equals(bprSuccYn)) {
					sndRstVO.setStatusCode("1");
					sndRstVO.setMsg("send success");
					
					List<String> docFormCList = new ArrayList<String>();
					//첫번째 대표서식 추가
					docFormCList.add(docFormC);
					
					//첨부파일이 있는 경우
					if(1<fileList.size()) {
						for(int i=1; i< fileList.size() ; i++) {
							File subFile = fileList.get(i);
							String subDocFormC = subFile.getName().split(",")[0];
							docFormCList.add(OldCommonUtil.safeReplaceAll(subDocFormC, "-", ""));
						}
					}
					
					
					//전송결과를 DB에 반영
					int result = DaoUpdate.updateEDocFileMng(md_eDocSer, docFormCList, md_imgKey, md_eccNo);
					
					if(0 < result) {
						sndRstVO.setStatusCode("1");
						sndRstVO.setMsg("send success");
						sndRstVO.setDbResult(true);
					} else {
						sndRstVO.setStatusCode("3");
						sndRstVO.setMsg("send success is db save error");
						processBprSndWorkFailure("3", md_eDocIndvIdxNo, "0607", "db save fail", imgSer);
					}
				} else {
					sndRstVO.setStatusCode("3");
					sndRstVO.setMsg("send error");
					processBprSndWorkFailure("3", md_eDocIndvIdxNo, "0606", "db save fail", imgSer);
				}//if(CUtil.isNotEmpty(bprSuccYn) && CUtil.isNotEmpty(bprmsg) && "Y".equals(bprSuccYn)) {
			} else {
				sndRstVO.setStatusCode("3");
				sndRstVO.setMsg("send error");
				processBprSndWorkFailure("3", md_eDocIndvIdxNo, "0605", "send fail", imgSer);
			}//if(null != resultMap) {
			
		} catch (Exception e) {
			processBprSndWorkFailure("3", md_eDocIndvIdxNo, "0605", "send fail", imgSer);
		} finally {
			if(OldCommonUtil.isNotEmpty(conn)) { try {conn = null;} catch(Exception e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(bos)) { try {bos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(dos)) { try {dos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(is)) { try {is.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
		}
		
	}
	private void writeFileToDataOutStream(File file, DataOutputStream dos) throws Exception{
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(file));
			
			byte[] buff = new byte[1024];
			int len = -1;
			while((len = dis.read(buff)) != -1) {
				dos.write(buff, 0, len);
			}
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
			throw e;
		} finally {
			if(OldCommonUtil.isNotEmpty(dis)) { try {dis.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
			if(OldCommonUtil.isNotEmpty(dos)) { try {dos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
		}
	}
}
