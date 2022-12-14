package com.people.img.itf;

import java.util.Map;

import com.people.common.dao.EdsDao;
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldSystemUtil;
import com.people.common.step.EDocProcStep;
import com.people.common.vo.ConfigVO;
import com.people.img.ctr.MainCtr;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaemonRunner {
	public static void run(final String daemonName, final EDocProcStep eDocProcStep) {
		log.info(daemonName + " start~");
		
		MainCtr mainCtr = null;
		ConfigVO configVO = new ConfigVO();
		
		try {
			//공통DAO의 처리단계 설정
			EdsDao.setEDocProcStep(eDocProcStep);
			
			//데이터베이스초기화
			EdsDao.setDataSource();
			
			//메인컨트롤 인스턴스 가져오기
			mainCtr = MainCtr.getInstance();
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(OldSystemUtil.getExceptionLog(e));
		}
		
		/**
		 * 실제 작업이 run 되는 곳 
		 */
		while(true) {
			try {
				//쓰레드 실행 환경 정보
				Map<String, Object> configMap = EdsDao.selectConfigVal();
				
				//현재 STEP의 실행환경정보를 SET
				eDocProcStep.setConfigInfo(configMap, configVO);
				
				//application 실행환경 정보 SET
				mainCtr.setConfigVO(configVO);
				
				//실제 job 실행
				mainCtr.job();
				
				
			}catch (Exception e) {
				log.error(OldSystemUtil.getExceptionLog(e));
			}finally {
				try {
					long millis = 1000;
					if(!OldCommonUtil.isEmpty(configVO)) {
						millis = configVO.getSleepval();
					}
					Thread.sleep(millis);
				} catch (Exception e2) {
					log.error(OldSystemUtil.getExceptionLog(e2));
				}
			}
		}//while
	}
	
}
