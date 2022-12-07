package com.people.bpr.dao;

import java.util.List;

import com.people.common.step.EDocProcStep;

public class DaoUpdate {
	/**
	 * 
	 * @param eDocIdxNo 전자문서번호
	 * @param imgSer 이미지일련번호
	 * @param eDocProcSeqV 전자문서처리SEQ
	 * @param eDocProcSGV 전자문서처리상태 1-완료, 2-에러
	 * @return
	 */
	public static int updateEDocFileMng(String eDocIdxNo, int imgSer, int eDocProcSeqV, int eDocProcSGV, EDocProcStep step) {
		return 1;
	}
	
	public static int updateEDocFileMng(String eDocIdxNo, int imgSer, int eDocProcSeqV, int eDocProcSGV) {
		return 1;
	}
	
	public static int updateEDocFileMng(String eDocIdxNo, List<String> docFormCList, String bprImgIdxNo, String teccC) {
		return 1;
	}
	
	public static int updateEDocFileMng(String eDocIdxNo, int eDocProcSGV) {
		
		//eDocProcSGV 2이면 시도회수증가 후 에러건으로 
		return 1;
	}
	
	public static int updateEdocFileMngTryCntPlus(String eDocIdxNo, int imgSer, int eDocProcSeqV, String docDormC) {
		return 1;
	}
}
