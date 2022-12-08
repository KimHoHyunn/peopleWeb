package com.people.bpr.itf;

import java.util.Map;

import com.people.bpr.crt.BprMainCtr;
import com.people.common.dao.EdsDao;
import com.people.common.oldutil.CommonUtil;
import com.people.common.oldutil.SystemUtil;
import com.people.common.step.EDocProcStep;
import com.people.common.vo.ConfigVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BprDaemonRunner {
	public static void run(final String daemonName, final EDocProcStep eDocProcStep) {
		log.info(daemonName + " start~");
		
		BprMainCtr bprMainCtr = null;
		ConfigVO configVO = new ConfigVO();
		
		try {
			//공통DAO의 처리단계 설정
			EdsDao.setEDocProcStep(eDocProcStep);
			
			//데이터베이스초기화
			EdsDao.setDataSource();
			
			//메인컨트롤 인스턴스 가져오기
			bprMainCtr = BprMainCtr.getInstance();
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(SystemUtil.getExceptionLog(e));
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
				bprMainCtr.setConfigVO(configVO);
				
				//실제 job 실행
				bprMainCtr.job();
				
				
			}catch (Exception e) {
				log.error(SystemUtil.getExceptionLog(e));
			}finally {
				try {
					long millis = 1000;
					if(!CommonUtil.isEmpty(configVO)) {
						millis = configVO.getSleepval();
					}
					Thread.sleep(millis);
				} catch (Exception e2) {
					log.error(SystemUtil.getExceptionLog(e2));
				}
			}
		}//while
	}
	
}
