package com.people.bpr.crt.sub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.people.bpr.dao.DaoSelect;
import com.people.bpr.dao.DaoUpdate;
import com.people.common.dao.EdsDao;
import com.people.common.oldutil.CommonUtil;
import com.people.common.oldutil.FileUtil;
import com.people.common.oldutil.SystemUtil;
import com.people.common.vo.SndRstVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BprSendList {
	private Map<String, Object> eDocInfo;
	private String eDocIdxNo;
	private String bprImgUrl;
	private String bprImgSubUrl;
	private int threadPoolSize;
	private String bprSendG;
	private String inIdxPath;	
	
	private static ExecutorService executorService = null;
	private List<Callable<SndRstVO>> futures = null;
	
	public BprSendList(Map<String, Object> eDocInfo, Properties props, int threadPoolSize, String bprSendG) {
		this.eDocInfo		= eDocInfo;
		this.eDocIdxNo		= eDocInfo.get("E_DOC_IDX_NO").toString();
		this.bprSendG		= bprSendG;
		
		String subPath1 = eDocIdxNo.substring(0,8);
		String subPath2 = eDocIdxNo.substring(18,22);
		this.inIdxPath 		= FileUtil.joinPaths(props.getProperty("idx_path"), subPath1, subPath2, eDocIdxNo).getPath();
		this.bprImgUrl		= CommonUtil.safeObjToStr(props.getProperty("BPR_IMG_URL"));
		this.bprImgSubUrl	= CommonUtil.safeObjToStr(props.getProperty("BPR_IMG_SUB_URL"));
		
		log.info(EdsDao.getEDocProcStep().toString());
	}
	
	
	
	public void workJobList() {
		List<Future<SndRstVO>> result = null;
		
		try {
			executorService = Executors.newFixedThreadPool(threadPoolSize);
			
			//전자문서의 서식리스트
			List<Map<String, Object>> targetList = DaoSelect.selectFileTarget(eDocIdxNo);
			
			if(CommonUtil.isNotEmpty(targetList)) {
				
				//대표서식(서식이있는) 데이터에 TECC C 가 없는 서식 모두 첨부하기 위해~
				List<String> mergeDocList = new ArrayList<String>();
				
				futures = new ArrayList<Callable<SndRstVO>>(targetList.size());
				
				//select 문에 의해 첫번째 데이터가 대표서식 데이터라서 
				//역순으로 빼서 첨부 데이터를 생성한다.
				for(int i=targetList.size() - 1 ; i >= 0; i--) {

					Map<String, Object> map = targetList.get(i);
					String docFormC = CommonUtil.safeObjToStr(map.get("DOC_FORM_C"));
					/**
					 * 전자문서 구분
					 * 0 종이
					 * 1 S-TB
					 * 2 디지털 창구 일반
					 * 3 디지털 창구 카드사(은행)
					 * 4 디지털 창구 전표업무
					 * 5 디지털 창구 카드사(카드-카드사 bpr tiff 전송)
					 * 6 디지털 창구 카드사(카드-은행 bpr tiff 전송)
					 * 7 디지털 창구 카드사(카드-카드사 페이퍼리스 PPR pdf 전송)
					 * 9 디지털 창구 여신
					 */
					String eDocG	= CommonUtil.safeObjToStr(map.get("E_DOC_G"));
					String tecc		= CommonUtil.safeObjToStr(map.get("TECC_C"));
					
					//대표서식인가
					boolean isrepDoc = false;
					
					//ECC가 없으면 첨부대상
					if(CommonUtil.isEmpty(tecc)) {
						mergeDocList.add(docFormC);
					} else {
						//isFirst가 true 이면 첫번째 데이터로 대표식이다.
						//하지만 전자문서구분(eDocG)가 7이면 첨부서류로 처리한다.
						if("7".equals(eDocG) && "false".equals(map.get("isFirst"))) {
							mergeDocList.add(docFormC);
						} else if("6".equals(eDocG)) {
							//6이면 디퍼드로 데이터를 받지 않아서 첨부서식으로 처리.
							mergeDocList.add(docFormC);
						} else {
							isrepDoc = true;
							BprSendWork bsWork = null;
							
							if(0 < mergeDocList.size()) {
								//첨부파일이 있는 경우
								bsWork = new BprSendWork(new SndRstVO(), map, bprImgUrl, bprImgSubUrl, docFormC, mergeDocList, eDocInfo, inIdxPath);
								mergeDocList = new ArrayList<String>();
							} else {
								//첨부파일이 없는 경우
								bsWork = new BprSendWork(new SndRstVO(), map, bprImgUrl, bprImgSubUrl, docFormC, null, eDocInfo, inIdxPath);
							}
							futures.add(bsWork);
						}
					}//if(CUtil.isEmpty(tecc)) {

					log.info("isRepDoc = " + isrepDoc );
					
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
			}//if(CUtil.isNotEmpty(targetList)) {
			
			
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
