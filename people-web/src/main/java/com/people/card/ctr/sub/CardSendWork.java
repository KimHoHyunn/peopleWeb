package com.people.card.ctr.sub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.people.card.dao.DaoUpdate;
import com.people.card.util.CardEaiUtil;
import com.people.card.util.CardInfCreateUtil;
import com.people.card.util.CardSendPdfUtil;
import com.people.card.util.CardSendUtil;
import com.people.card.util.EpsUtil;
import com.people.card.vo.EpsBodyVO;
import com.people.card.vo.EpsHeaderVO;
import com.people.common.dao.EdsDao;
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldFileUtil;
import com.people.common.oldutil.OldSystemUtil;
import com.people.common.step.EDocProcStep;
import com.people.common.vo.EDocErrHisVO;
import com.people.common.vo.SndRstVO;

public class CardSendWork implements Callable<SndRstVO> {
	
	private SndRstVO sndRstVO = null;
	private Map<String, Object> map;

	public CardSendWork(SndRstVO sndRstVO, Map<String, Object> map) {
		this.sndRstVO = sndRstVO;
		this.map = map;
	}
	
	public SndRstVO call() throws IOException{
		//전자문서구분이 7이면 pdf를 공전소로 전송
		if("7".equals(map.get("E_DOC_G"))) {
			sendCardPPR() ;
		} else {
			sendCard() ;
		}
		
		return sndRstVO;
	}
	
	private void sendCardPPR() {
		String eDocIdxNo = OldCommonUtil.safeObjToStr(map.get("E_DOC_IDX_NO"));
		int    imgSer    = OldCommonUtil.objectToInt(map.get("IMG_SER"));

		sndRstVO.setEDocIdxNo(eDocIdxNo);
		sndRstVO.setDbResult(false);
		
		//카드전송
		try {
			CardSendPdfUtil csu = new CardSendPdfUtil();
			CardSendPdfUtil.SendResult sendResult = csu.sendCard(map);
			
			if(CardSendPdfUtil.SendResult.SUCCESS == sendResult) {
				int ret = DaoUpdate.updateEDocFileMng(eDocIdxNo, imgSer, 2, 1);
				if(1 == ret) {
					sndRstVO.setStatusCode("1");
					sndRstVO.setMsg("success");
					sndRstVO.setDbResult(true);
				} else {
					EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0706", "card send error");
				}
			} else {
				String errC = "0700";
				String errCtnt = "error";
				sndRstVO.setStatusCode("2");
				sndRstVO.setMsg(errCtnt);
				EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, errC, errCtnt);
			}//if(CardSendPdfUtil.SendResult.SUCCESS == sendResult) {
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 1 데이터 추출
	 * 2 EAI 통신
	 *   2-1 send xml 생성
	 *   2-2 EAI 통신
	 *   2-3 Receive XML 생성
	 * 3 Receive XML로 inf 생성
	 * 4 card 전송 (img 보낸고 inf 보냄)
	 */
	private void sendCard() throws IOException {
		String eDocIdxNo = OldCommonUtil.safeObjToStr(map.get("E_DOC_IDX_NO")); //전자문서번호
		int    imgSer    = OldCommonUtil.safeObjToInt(map.get("IMG_SER"));		//이미지일련번호
		String docFormC = OldCommonUtil.safeObjToStr(map.get("DOC_FORM_C"));	//문서양식코드
		String storPathNm = OldCommonUtil.safeObjToStr(map.get("STOR_PATH_NM"));//저장경로
		String imgIdxNo = OldCommonUtil.safeObjToStr(map.get("IMG_IDX_NO"));	//이미지인덱스번호(BPR이미지인덱스번호)
		String eDocG = OldCommonUtil.safeObjToStr(map.get("E_DOC_G"));			//전자문서구분
		
		sndRstVO.setEDocIdxNo(eDocIdxNo);
		sndRstVO.setDbResult(false);
		
		//1 EAI 통신
		CardEaiUtil ceu = new CardEaiUtil();
		File recvFile = null;
		
		recvFile = ceu.send(map);
		
		if(OldCommonUtil.isEmpty(recvFile)) {
			EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0702", "EPS Send Fail");
			sndRstVO.setStatusCode("2");
			sndRstVO.setMsg("EPS Send Fail");
			return;
		}
		
		
		//2 inf 파일 생성
		EpsHeaderVO header = new EpsHeaderVO();
		EpsBodyVO body = new EpsBodyVO();
		
		EpsUtil.ParseResult parseRst = EpsUtil.parseReceiveXML(recvFile, header, body);
		
		if(EpsUtil.ParseResult.SUCC != parseRst) {
			if(EpsUtil.ParseResult.SND_IPSB == parseRst) {
				EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0712", "send disabled bprDaXstF");
				sndRstVO.setStatusCode("2");
				sndRstVO.setMsg("send disabled bprDaXstF");
			} else {
				EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0703", "EPS Receive xml parse fail");
				sndRstVO.setStatusCode("2");
				sndRstVO.setMsg( "EPS Receive xml parse fail");
			}
			return;
		} 
		
		//tif 파일을 카드전송용 이름으로 변환하기 위해 card 디렉토리에 복사
		String imgFilePath = OldFileUtil.joinPaths(storPathNm, OldFileUtil.PATH.IMAGE).getPath();
		String imgFileName = OldCommonUtil.getInfCodeType(docFormC) + ","+imgIdxNo+".tif";
		
		File sourceImgFile = OldFileUtil.joinPaths(imgFilePath, imgFileName);
		
		if(OldFileUtil.exists(sourceImgFile) == false) {
			EDocErrHisVO errVO = new EDocErrHisVO();
			String eDocErrctnt = "tif file not found";
			
			if(errVO.setData(eDocIdxNo, imgSer, 2, "0704", eDocErrctnt)) {
				
				File searchFile = new File(imgFilePath);
				File[] files = searchFile.listFiles();
				
				if(null == files) {
					EdsDao.insertErrHis(errVO);
				} else {
					File tFile = null;
					for(File targetFile : files ) {
						if(targetFile.getName().contains("-")) {
							tFile = targetFile;
							break;
						}
					}
					
					if(OldFileUtil.exists(tFile)) {
						if(tFile.renameTo(sourceImgFile)) {
							DaoUpdate.updateEDocFileMng(eDocIdxNo, imgSer, 2, 1, EDocProcStep.BPR);
						}
					} else {
						EdsDao.insertErrHis(errVO);
					}//if(FileUtil.exists(tFile)) {
				}//if(null == files) {
			}//if(errVO.setData(eDocIdxNo, imgSer, 2, "0704", eDocErrctnt)) {
		
			sndRstVO.setStatusCode("2");
			sndRstVO.setMsg("file not found");
		}//if(FileUtil.exists(sourceImgFile) == false) {
		
		String bpr_map_cd = body.getData_flat_bpr_map_cd();
		String bpr_scn_crt_ccd = body.getData_flat_bpr_scn_crt_ccd();
		String bpr_crt_dt = OldCommonUtil.safeObjToStr(map.get("BPR_CRT_DT")); //스캔시간
		String bpr_ti_dt = OldCommonUtil.safeObjToStr(map.get("BPR_TI_DT")); //전송시간
		String op_hwnno = header.getOp_hwnno();
		String trx_br_c = body.getData_flat_bpr_rv_hcd(); //전송지점코드이나 카드사 기준점번호
		String proc_rslt_yn = body.getData_flat_bpr_rcd(); //bpr응답코드
		String bpr_pcd = body.getData_flat_bpr_pcd(); //bpr 경로 코드
		String bpr_img_key_vl = body.getData_flat_bpr_img_key_vl();
		
		String hms = OldSystemUtil.nowTime("HHmmss");
		
		String strImgSer = "";
		if(9>=imgSer) {
			strImgSer = "0"+imgSer;
		} else if(99 >= imgSer){
			strImgSer = imgSer+"";
		}
		
		
		String targetImgFilePath = recvFile.getParent();
		String targerImgFileName = bpr_img_key_vl+hms+op_hwnno+strImgSer+".tif";
		File targetImgFile = OldFileUtil.joinPaths(targetImgFilePath, targerImgFileName);
		
		boolean copyResut = OldFileUtil.copy(sourceImgFile, targetImgFile);
		
		if(false == copyResut) {
			EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0705", "tif file copy fail");
			sndRstVO.setStatusCode("2");
			sndRstVO.setMsg("tif file copy fail");
			return;
		} 

		List<File> imgFileList = new ArrayList<File>();
		List<String> docFormCList = new ArrayList<String>();
		
		imgFileList.add(targetImgFile);
		
		
		//코드에서 앞자리가 C이면 C를 제거
		if("5".equals(eDocG) || "6".equals(eDocG)) {
			if(docFormC.toUpperCase().startsWith("C")) {
				docFormC = docFormC.substring(1);
			}
			docFormCList.add(docFormC);
		}
		
		/*-inf 파일 생성정보 설정--------------------------------------------------------------------------------------------*/
		CardInfCreateUtil cicu = new CardInfCreateUtil();
		cicu.setC_BPR_PS_SHP_CO_CD("S001");
		cicu.setC_BPR_BNE_ZCD("04");
		cicu.setC_BPR_MAP_CD(bpr_map_cd);
		cicu.setC_BPR_SCN_CRT_CCD(bpr_scn_crt_ccd);
		cicu.setC_BPR_PS_CHL_CCD("03");
		cicu.setC_BPR_MAD_CRT_CCD("01");
		cicu.setC_BPR_ET_CCD("000");
		cicu.setC_BPR_CRT_DT(bpr_crt_dt);
		cicu.setC_BPR_TI_DT(bpr_ti_dt);
		cicu.setC_BPR_CRT_RMK_EN(op_hwnno);
		cicu.setC_BPR_TI_RMK_EN(op_hwnno);
		cicu.setC_BPR_PS_HCD(trx_br_c);
		cicu.setC_BPR_CRT_TMN_IP_AR("");
		cicu.setC_BPR_FIL_ECR_F("N");
		cicu.setC_BPR_IMG_KEY_SE_F("Y");
		cicu.setC_BPR_ECC_N_SE_F("N");
		cicu.setC_BPRS_CD(proc_rslt_yn);
		cicu.setC_BPR_EGC_F("N");
		cicu.setC_BPR_PCD(bpr_pcd);
		
		//index key 생성
		cicu.setIk_BPR_IDX_KEY_VL(bpr_img_key_vl);
		
		//meta data 생성
		String metaData = "BPR_RV_OR_BN_CCD^7|BPR_ODS_SDC_NR_^"+eDocIdxNo+"|ELC_SDC_KEP_PSI^SOO1";
		cicu.setMd_BPR_MTA_COL_TT(metaData);
		
		//FILE INFO 생성
		cicu.setDocFormCList(docFormCList);
		cicu.setFileList(imgFileList);
		/*-inf 파일 생성정보 설정--------------------------------------------------------------------------------------------*/
		
		//inf 생성시작
		String infFilePath = targetImgFilePath;
		String infFileName = bpr_img_key_vl+hms+strImgSer+".inf";
		File infFile = OldFileUtil.joinPaths(infFilePath, infFileName);
		
		boolean infResult = cicu.createInfFile(infFile);
		
		if(infResult == false) {
			EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0701", "inf file create fail");
			sndRstVO.setStatusCode("2");
			sndRstVO.setMsg( "inf file create fail");
			return;
		}
		
		//카드전송
		try {
			CardSendUtil csu = new CardSendUtil();
			String sendResult = csu.sendToCare(imgFileList, infFile);
			
			if(CardSendUtil.ERR_C.SUCCESS.equals(sendResult)) {
				int ret = DaoUpdate.updateEDocFileMng(eDocIdxNo, imgSer,2,1);
				if(1 == ret) {
					sndRstVO.setStatusCode("1");
					sndRstVO.setMsg("SUCCESS");
					sndRstVO.setDbResult(true);
				} else {
					EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, "0706", "db upate error");
				}
			} else {
				String errcode = "";
				String errmsg = "";
				if(CardSendUtil.ERR_C.SEND_FAIL.equals(sendResult)) {
					errcode = "0707";
					errmsg = "card send fail";
				} else if(CardSendUtil.ERR_C.SOCKET_CONNECT_FAIL.equals(sendResult)) {
					errcode = "0708";
					errmsg = "socket connection fail";
				} else if(CardSendUtil.ERR_C.IMG_RESPONSE_WRONG.equals(sendResult)) {
					errcode = "0709";
					errmsg = "img send - server response data is wrong";
				} else if(CardSendUtil.ERR_C.INF_RESPONSE_WRONG.equals(sendResult)) {
					errcode = "0710";
					errmsg = "inf send - server response data is wrong";
				} else  {
					errcode = "0711";
					errmsg = "etc send error";
				}
				sndRstVO.setStatusCode("2");
				sndRstVO.setMsg("FAIL");
				EdsDao.registerErrHis(eDocIdxNo, imgSer, 2, errcode, errmsg);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		
		
		
		
		
	}
}
