package com.people.img.ctr.sub;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.people.common.util.CommonUtil;
import com.people.common.util.SystemUtil;
import com.people.common.vo.DirFileVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImgCreateList {
	
	/**
	 * 전자문서번호 목록
	 * - 하나의 전자문서번호에 여러개의 서식이 있다.
	 */
	private List<String> eDocIdxNoList;
	
	//이미지압축율
	private int comprate = 0;
	private int threadPoolSize;
	
	private static ExecutorService executorService = null;
	private List<Callable<DirFileVO>> futures = null;
	
	public ImgCreateList(List<String >eDocIdxNoList, Properties props, int threadPoolSize) {
		
		this.eDocIdxNoList = eDocIdxNoList;
		
		this.comprate = CommonUtil.safeObjToInt(props.getProperty("comprate"), 80);
		this.threadPoolSize = threadPoolSize;
		
		String className = this.getClass().getSimpleName();
		
		log.info(className +", comprate = "+this.comprate+", threadPoolSize = "+this.threadPoolSize);
	}
	
	public void workJobList() {
		try {
			executorService = Executors.newFixedThreadPool(threadPoolSize);
			
			futures = new ArrayList<Callable<DirFileVO>>(eDocIdxNoList.size());
			
			for(String eDocIdxNo : eDocIdxNoList) {
				ImgCreateWork imgCreateWork = new ImgCreateWork(eDocIdxNo, comprate);
				futures.add(imgCreateWork);
			}
			
			executorService.invokeAll(futures);
			
		} catch (Exception e) {
			// TODO: handle exception
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
