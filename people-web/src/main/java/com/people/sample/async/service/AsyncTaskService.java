package com.people.sample.async.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.people.common.oldutil.OldSystemUtil;
import com.people.sample.async.dao.AsyncDao;
import com.people.sample.jms.producer.ArtemisProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("AsyncTaskService")
public class AsyncTaskService {
	
	@Autowired private AsyncDao asyncDao;
    @Autowired private ArtemisProducer artemisProducer;
	
	@Async
	public void jobRunningInBackground(String temp) {
		log.info("Thread Start");
		for(int i=0; i < 30; i++) {
			log.info(temp + "  i ::::: " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("Thread End");
	}
	
	private static ExecutorService executorService = null;
	private List<Callable<Map<String, Object>>> futures = null;
	
	@Async
	public void asyncDbToJms() {
		List<Map<String, Object>> list = asyncDao.getList();
		
		executorService = Executors.newFixedThreadPool(list.size());
		futures = new ArrayList<Callable<Map<String, Object>>>(list.size());
		
		for(Map<String, Object> map : list) {
			Work work = new Work(map);
			futures.add(work);
		}
		
		try {
			List<Future<Map<String, Object>>> result = executorService.invokeAll(futures);
			log.info("result = "+result.toString());
		} catch (InterruptedException e) {
			OldSystemUtil.getExceptionLog(e);
		} finally {
			shutdownThreadPool();
		}
	}
	
	class Work implements Callable<Map<String, Object>>{
		private Map<String, Object> map;
		public Work (Map<String, Object> map) {
			this.map = map;
		}
		@Override
		public Map<String, Object> call() throws IOException{
			artemisProducer.send(this.map);
			this.map.put("result", "OK");
			return this.map;
		}
	}
	
	private void shutdownThreadPool() {
		executorService.shutdown();
		try {
			if(executorService.awaitTermination(5, TimeUnit.SECONDS)) {
				executorService.shutdown();
			}
		} catch (Exception e) {
			executorService.shutdown();
			Thread.currentThread().interrupt();
			OldSystemUtil.getExceptionLog(e);
		}
	}
	
	public List<Map<String, Object>> dbToJms() {
		List<Map<String, Object>> list = asyncDao.getList();
		
		for(Map<String, Object> map : list) {
			artemisProducer.send(map);
		}
		return list;
	}
}
