package com.people.img.dao;

public class DaoUpdate {
	/**
	 * 
	 * @param eDocIdxNo 전자문서번호
	 * @param imgSer 이미지일련번호
	 * @param eDocProcSeqV 전자문서처리SEQ
	 * @param eDocProcSGV 전자문서처리상태 1-완료, 2-에러
	 * @return
	 */
	public static int updateEDocFileMng(String eDocIdxNo, int imgSer, int eDocProcSeqV, int eDocProcSGV) {
		return 1;
	}
	
	public static int updateEDocFileMng(String eDocIdxNo, int imgSer, int eDocProcSeqV, String step) {
		return 1;
	}
}
