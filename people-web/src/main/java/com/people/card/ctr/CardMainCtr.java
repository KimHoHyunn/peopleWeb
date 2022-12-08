package com.people.card.ctr;

import java.util.List;
import java.util.Map;

import com.people.common.dao.EdsDao;
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.vo.ConfigVO;
import com.people.card.dao.DaoSelect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardMainCtr {
	private static class SingletonHolder{
		private static final CardMainCtr instance = new CardMainCtr();
	}
	
	public static CardMainCtr getInstance() {
		return SingletonHolder.instance;
	}
	
	private static final String stepName = EdsDao.getEDocProcStep().toString();
	
	private ConfigVO configVO = null;
	public void setConfigVO(ConfigVO configVO) {
		this.configVO = configVO;
	}
	
	public void job() {
		//실행환경 체크
		if("N".equals(configVO.getWorkSkip())) {
			workingStep();
		} else {
			log.info("Step = "+ stepName +", WORK_SKIP = "+configVO.getWorkSkip());
		}
	}
	
	private void workingStep() {
		//작업시작시간
		long startTime = System.currentTimeMillis();
		//-------------------------------------------------------------------------------------------------
		//작업대상 전자문서 리스트를 조회한다.
		//처리단계가 BPR전송(06), 상태는 완료(1) 또는 처리단계가 카드전송(07), 상태는 에러(2) 인 건
		//전자문서구분이 3,5,6,7
		//시도회수 < 3
		//거래일자가 당일 (지난건처리는 별도 데몬으로 처리)
		//등록시간이 특정시간(configVO.getDb2dbgapval) 이전 건 대상
		//한번에 처리건수 제한 (configVO.getTargetMaxCnt) -- rownum으로 조건 걸어서 제한. 페이징처리와 비슷
		List<Map<String, Object>> eDocInfoList = DaoSelect.selectTarget(configVO);
		
		if(OldCommonUtil.isNotEmpty(eDocInfoList)) {
			for(Map<String, Object> eDocInfo : eDocInfoList) {
				CardSendCtr cardSendCtr = new CardSendCtr();
				cardSendCtr.procJobMain(eDocInfo, configVO.getThreadPoolSize());
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		log.info(stepName+" process time = "+(endTime-startTime)+"(ms)");
	}
}
