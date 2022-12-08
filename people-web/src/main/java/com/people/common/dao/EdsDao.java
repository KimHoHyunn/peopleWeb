package com.people.common.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.people.common.oldutil.SystemUtil;
import com.people.common.step.EDocProcStep;
import com.people.common.vo.EDocErrHisVO;

public class EdsDao {
	private static final int PROC_TRY_CNT = 3;
	private static final String LF = "\n";
	
	private static EDocProcStep eDocProcStep ;

	private static DataSource ds = null;
	
	
	protected EdsDao() {}
	
	/**
	 * 처리단계 
	 */
	
	public static void setEDocProcStep(EDocProcStep eDocProcStep) {
		EdsDao.eDocProcStep = eDocProcStep;
	}
	
	public static EDocProcStep getEDocProcStep() {
		return eDocProcStep;
	}
	/**
	 * 데이터소스 초기화
	 */
	public static void setDataSource() throws Exception {
		
		
		Properties props = new Properties();//SystemUtil.getCommonProperties();
		
		//호스트이름가져오기
		String hostName = SystemUtil.getHostName().toUpperCase();
		
		//호스트명으로 실행환경
		
	}
	
	public static DataSource getDataSource() {
		return ds;
	}
	
	
	
	public static Map<String, Object> selectConfigVal(){
		Map<String, Object> result = new HashMap<String, Object>();
		
		//DB에서 Config Table 전체를 Select 한다.
		//key   = config_item_name
		//value = config_value
		result.put("","");
		
		return result;
	}
	
	public static void registerErrHis(String eDocIdxNo, int imgSer, int eDocProcSeqV, String eDocProcErrC, String eDocErrCtnt) {
		//에러정보 저장
	}
	
	public static void insertErrHis(EDocErrHisVO edocErrHisVO) {
		//에러정보 저장
	}
}


