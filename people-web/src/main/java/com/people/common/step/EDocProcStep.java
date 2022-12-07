package com.people.common.step;

import java.util.Map;

import com.people.common.util.CommonUtil;
import com.people.common.vo.ConfigVO;

import lombok.Getter;

@Getter
public enum EDocProcStep {
	ZIP ("01","1st pdf File Create") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			setConfigInfoByDefault(configVO, ZIP);
		}
	} ,
	
	PDF ("02","pdf File Create") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("PDF2_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("PDF2_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("PDF2_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("PDF2_THREAD_POOL_SIZE"), 10));
			} else {
				setConfigInfoByDefault(configVO, PDF);
			}
		}
	} ,
	
	TSA ("03","tsa certification") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("TSA_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("TSA_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("TSA_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("TSA_THREAD_POOL_SIZE"), 10));
			} else {
				setConfigInfoByDefault(configVO, TSA);
			}
		}
	} ,
	
	//ECM전송 (진본PDF파일저장완료)
	ECM ("04","ECM(XTORM) Send") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("XTORM_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("XTORM_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("XTORM_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("XTORM_THREAD_POOL_SIZE"), 10));
			} else {
				setConfigInfoByDefault(configVO, TSA);
			}
		}
	} ,
	
	//PDF --> 이미지변환완료
	IMG ("05","Image Convert") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("IMG_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("IMG_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("IMG_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("IMG_THREAD_POOL_SIZE"), 10));
				configVO.setDb2dbGapval(CommonUtil.safeObjToInt(map.get("DB2DB_GAPVAL"), 60));
			} else {
				setConfigInfoByDefault(configVO, IMG);
			}
		}
	} ,
	
	//PDF --> 이미지변환완료 (처리못한 건 처리)
	PAST_IMG ("05","Past Image Convert") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("BEF_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("BEF_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("BEF_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("BEF_THREAD_POOL_SIZE"), 10));
				configVO.setFromDay(CommonUtil.safeObjToInt(map.get("BEF_DS_FROMDT"), 5));
				configVO.setAllDay(CommonUtil.safeObjToStr(map.get("BEF_QUERY_ALL_DAY"), "N"));
			} else {
				setConfigInfoByDefault(configVO, PAST_IMG);
			}
		}
	} ,
	
	//BPR 전송
	//BPR_DAEMON_G = Y 일 때, 계좌관리점 사용구분(AC_GRBRNO = 0) 일 때
	BPR ("06","BPR Send") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("BPR_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("BPR_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("BPR_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("BPR_THREAD_POOL_SIZE"), 10));
				configVO.setSendSleepval(CommonUtil.safeObjToInt(map.get("BPR_SEND_SLEEPVAL"), 10));
				configVO.setBprDaemonG(CommonUtil.safeObjToStr(map.get("BPR_DAEMON_G"), "N"));
				configVO.setBprSendG(CommonUtil.safeObjToStr(map.get("BPR2_SEND_G"), ""));
			} else {
				setConfigInfoByDefault(configVO, BPR);
			}
		}
	} ,
	
	//BPR 전송
	//BPR_DAEMON_G = Y 일 때, 계좌관리점 사용구분(AC_GRBRNO != 0) 일 때
	BPR2 ("06","BPR Send") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("BPR2_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("BPR2_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("BPR2_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("BPR2_THREAD_POOL_SIZE"), 10));
				configVO.setSendSleepval(CommonUtil.safeObjToInt(map.get("BPR2_SEND_SLEEPVAL"), 10));
				configVO.setBprDaemonG(CommonUtil.safeObjToStr(map.get("BPR2_DAEMON_G"), "N"));
				configVO.setBprSendG(CommonUtil.safeObjToStr(map.get("BPR2_SEND_G"), ""));
			} else {
				setConfigInfoByDefault(configVO, BPR2);
			}
		}
	} ,
	
	//BPR 전송 (처리못한 건)
	PAST_BPR ("06","Past BPR Send") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("BEF_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("BEF_DAEMON2_SLEEPVAL"), 1000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("BEF_BPR_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("BEF_THREAD_POOL_SIZE"), 10));
				configVO.setSendSleepval(CommonUtil.safeObjToInt(map.get("BEF_SEND_SLEEPVAL"), 10));
				configVO.setFromDay(CommonUtil.safeObjToInt(map.get("BEF_DS_FROMDT"), 5));
				configVO.setAllDay(CommonUtil.safeObjToStr(map.get("BEF_QUERY_ALL_DAY"), "N"));
				configVO.setBprSendG(CommonUtil.safeObjToStr(map.get("BPR2_SEND_G"), ""));
			} else {
				setConfigInfoByDefault(configVO, PAST_BPR);
			}
		}
	} ,
	
	CARD ("07","Card Send") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setTargetMaxCount(CommonUtil.safeObjToInt(map.get("CARD_MAX_CNT"), 10));
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("CARD_SLEEPVAL"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("CARD_WORK_SKIP"), "N"));
				configVO.setThreadPoolSize(CommonUtil.safeObjToInt(map.get("CARD_THREAD_POOL_SIZE"), 10));
				configVO.setSendSleepval(CommonUtil.safeObjToInt(map.get("CARD_SEND_SLEEPVAL"), 10));
				configVO.setFromDay(CommonUtil.safeObjToInt(map.get("CARD_FROM_DATE"), 0));
			} else {
				setConfigInfoByDefault(configVO, CARD);
			}
		}
	} ,
	
	PAST_CARD ("07","Card Send") {
		@Override
		public void setConfigInfo(Map<String, Object> map, ConfigVO configVO) {
			if(CommonUtil.isEmpty(map)) {
				configVO.setSleepval(CommonUtil.safeObjToInt(map.get("BEF_CARDDAEMON_SLEEP"), 5000));
				configVO.setWorkSkip(CommonUtil.safeObjToStr(map.get("BEF_CARD_WORK_SKIP"), "N"));
				configVO.setAllDay(CommonUtil.safeObjToStr(map.get("BEF_QUERY_ALL_DAY"), "N"));
			} else {
				setConfigInfoByDefault(configVO, PAST_CARD);
			}
		}
	} ,
	
	;
	
	private final String cval;
	private final String label;

	public abstract void setConfigInfo(Map<String, Object> map, ConfigVO configVO);
	
	private static void setConfigInfoByDefault(ConfigVO configVO, EDocProcStep step) {
		configVO.setTargetMaxCount(10);
		configVO.setSleepval(5000);
		configVO.setWorkSkip("N");
		configVO.setThreadPoolSize(10);
		configVO.setSendSleepval(10);
		configVO.setDb2dbGapval(60);
		configVO.setBprDaemonG("N");
		configVO.setBprSendG("");
		configVO.setFromDay(CARD == step ? 0 : 5);
		configVO.setAllDay("N");
	}
	
	private EDocProcStep(String cval, String label) {
		this.cval = cval;
		this.label = label;
	}
	
	@SuppressWarnings("unused")
	private boolean equals(String otherStep) {
		return this.cval.equals(otherStep);
	}
}
