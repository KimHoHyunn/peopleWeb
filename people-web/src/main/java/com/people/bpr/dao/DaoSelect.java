package com.people.bpr.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.people.common.vo.ConfigVO;

public class DaoSelect {
	public static List<Map<String, Object>> selectTarget(ConfigVO configVO) {
		//대상 전자문서번호를 select하여 리턴한다.
		//select e_doc_idx_no from dgw_next_edocifo_mng 
		//where 스텝이 04(pdf저장)이고 상태가 1(완료)인 것, 스텝이 05(이미지변환)이고 상태가 2(에러)인것
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		return resultList;
	}
	
	public static List<Map<String, Object>> selectFileTarget(String eDocIdxNo) {
		/**
		 * 전자문서번호에 대한 서식정보를 조회한다.
		 * - 처리단계가 BPR전송(06), 상태는 완료(1) 또는 처리단계가 카드전송(07), 상태는 에러(2) 인 건
		 * - 전자문서구분이 3,5,6,7
		 * - 시도회수 < 3
		 */
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		return resultList;
	}
}
