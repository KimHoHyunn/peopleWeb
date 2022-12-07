package com.people.img.ctr;

import java.util.List;

import com.people.common.dao.EdsDao;
import com.people.common.util.CommonUtil;
import com.people.common.vo.ConfigVO;
import com.people.img.dao.DaoSelect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainCtr {
	private static class SingletonHolder{
		private static final MainCtr instance = new MainCtr();
	}
	
	public static MainCtr getInstance() {
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
		//pdf저장(04) 이고 상태가 완료(1) 건 또는 이미지변환(05) 이고 상태가 2(에러) 인 건을 조회
		//처리시도 건수가 3미만인 건 대상
		//거래일자가 당일 (지난건처리는 별도 데몬으로 처리)
		//등록시간이 특정시간(configVO.getDb2dbgapval) 이전 건 대상
		//한번에 처리건수 제한 (configVO.getTargetMaxCnt) -- rownum으로 조건 걸어서 제한. 페이징처리와 비슷
		List<String> eDocIdxNoList = DaoSelect.selectTarget(configVO);
		
		if(CommonUtil.isNotEmpty(eDocIdxNoList)) {
			ImgCreateCtr imgCreateCtr = new ImgCreateCtr();
			imgCreateCtr.procJobMain(eDocIdxNoList, configVO.getThreadPoolSize());
		}
		
		long endTime = System.currentTimeMillis();
		
		log.info(stepName+" process time = "+(endTime-startTime)+"(ms)");
	}
}
