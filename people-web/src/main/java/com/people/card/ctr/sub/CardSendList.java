package com.people.card.ctr.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.people.card.dao.DaoSelect;
import com.people.card.dao.DaoUpdate;
import com.people.common.oldutil.CommonUtil;
import com.people.common.oldutil.SystemUtil;
import com.people.common.step.EDocProcStep;
import com.people.common.vo.SndRstVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardSendList {

	private String eDocIdxNo = ""; //전자문서번호
	private String trxBrno = ""; //거래지점번호
	private String oprtJkwNo = ""; //작업직원번호
	private int threadPoolSize;
	
	private List<Integer> CARD_E_DOC_G = Arrays.asList(3,5,6,7);
	
	private static ExecutorService executorService = null;
	private List<Callable<SndRstVO>> futures = null;
	
	public CardSendList (Map<String, Object> eDocInfo, Properties props, int threadPoolSize) {
		this.eDocIdxNo = CommonUtil.safeObjToStr(eDocInfo.get("E_DOC_IDX_NO"));
		this.trxBrno = CommonUtil.safeObjToStr(eDocInfo.get("TRXBRNO"));
		this.oprtJkwNo = CommonUtil.safeObjToStr(eDocInfo.get("OPRT_JKW_NO"));
		this.threadPoolSize = threadPoolSize;
	}
	
	public void workJobList() {
		List<Future<SndRstVO>> result = null;
		
		try {
			executorService = Executors.newFixedThreadPool(threadPoolSize);
			
			//전자문서의 서식리스트
			List<Map<String, Object>> targetList = DaoSelect.selectFileTarget(eDocIdxNo);
			
			if(CommonUtil.isNotEmpty(targetList)) {
				futures = new ArrayList<Callable<SndRstVO>>(targetList.size());
				
				int seqNum = 0;
				for(Map<String, Object> map : targetList) {
					String step = CommonUtil.safeObjToStr(map.get("E_DOC_PROC_STEP_CVAL"));
					String eccNo = CommonUtil.safeObjToStr(map.get("TECC_C"));
					String eDocG = CommonUtil.safeObjToStr(map.get("E_DOC_G")); //전자문서구분
					
					boolean isCardSend = false;
					//카드전송건인지 확인
					if(EDocProcStep.BPR.equals(step) || EDocProcStep.CARD.equals(step)) {
						if(CommonUtil.isNotEmpty(eccNo)) {
							isCardSend = CARD_E_DOC_G.contains(eDocG);
						} else {
							isCardSend = false;
						}
					}
					
					if(isCardSend) {
						//글로벌ID 중복채번 방지를 위해 sleep
						Thread.sleep(1);
						
						map.put("nowTime", SystemUtil.nowTime("yyyyMMddHHmmssSS"));
						
						CardSendWork cardSendWork = new CardSendWork(new SndRstVO(), map);
						futures.add(cardSendWork);
					}
					
					map.put("seqNum", seqNum++);
					map.put("TRXBRNO", trxBrno);
					map.put("OPRT_JKW_NO", oprtJkwNo);
				}
				
				result = executorService.invokeAll(futures);
				
				if(0 < result.size()) {
					int sucCnt = 0;
					
					for(Future<SndRstVO> retFuture : result) {
						SndRstVO sndRstVO = retFuture.get();
						String statusCode = sndRstVO.getStatusCode();
						String msg = sndRstVO.getMsg();
						
						if("1".equals(statusCode)) {
							sucCnt++;
						} else {
							log.error("error~~");
						}
					}
					
					int eDocProcSGV = 2; //처리상태 : 2=에러, 1=완료
					if(result.size() == sucCnt) {
						eDocProcSGV = 1;
					}
					int ret = DaoUpdate.updateEDocFileMng(eDocIdxNo, eDocProcSGV);
					
					
				} else {
					log.info("Not working");
				}
				
			} else {
				log.info("no target");
			}
			
			
		} catch (Exception e) {
			log.error(SystemUtil.getExceptionLog(e));
		} finally {
			shutdownThreadPool();
		}
	}

	private void shutdownThreadPool() {
		executorService.shutdown();
		try {
			if(!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
				executorService.shutdown();
			}
		} catch (Exception e) {
			executorService.shutdown();
			Thread.currentThread().interrupt();
			log.error(SystemUtil.getExceptionLog(e));
		}
	}
}
