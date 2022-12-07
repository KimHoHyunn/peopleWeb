package com.people.img.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.people.common.vo.ConfigVO;

public class DaoSelect {
	public static List<String> selectTarget(ConfigVO configVO) {
		//대상 전자문서번호를 select하여 리턴한다.
		//select e_doc_idx_no from dgw_next_edocifo_mng 
		//where 스텝이 04(pdf저장)이고 상태가 1(완료)인 것, 스텝이 05(이미지변환)이고 상태가 2(에러)인것
		return new ArrayList<String>();
	}
	
	public static List<Map<String,Object>> selectFileTarget(String eDocIdxNo) {
		/**
		 * 전자문서번호에 대한 서식정보를 조회한다.
		 * - 간편서식이 아닌 것만 조회
		 * 
		 */
		return new ArrayList<Map<String,Object>>();
	}
}
