package com.people.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.people.sample.async.exception.AsyncExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/*
 * @Configuration : bean 객체 등록
 * @EnableAsync : 비동기 프로세서 사용선
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer{
	
	//기본 Thread 수 
	private static int TASK_CORE_POOL_SIZE = 10;
	//최대 Thread 수
	private static int TASK_MAX_POOL_SIZE = 100;
	//QUEUE 수
	private static int TASK_QUEUE_CAPACITY = 200;
	//Thread Bean Name
	private final String EXECUTOR_BEAN_NAME = "threadPoolTaskExecutor";
	
	@Resource(name="threadPoolTaskExecutor")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@Bean(name="threadPoolTaskExecutor")
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(TASK_CORE_POOL_SIZE);
		executor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
		executor.setQueueCapacity(TASK_QUEUE_CAPACITY);
		executor.setBeanName(EXECUTOR_BEAN_NAME);
		executor.setWaitForTasksToCompleteOnShutdown(false);
		executor.initialize();
		return executor;
	}
	
	/*
	 * Thread Process도중 에러 발생시
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}
	
	/*
	 * task 생성전에 pool이 찼는지를 체크
	 */
	public Map<String, Object> checkSampleTaskExecute() {
		int result = 1;
		
		log.info("활성 Task 수 :::: " + threadPoolTaskExecutor.getActiveCount());
		
		if(threadPoolTaskExecutor.getActiveCount() >= (TASK_MAX_POOL_SIZE + TASK_QUEUE_CAPACITY)) {
			result = 0;
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("isAble",result);
		map.put("MaxActieCount", TASK_MAX_POOL_SIZE);
		map.put("CurrentActivationCount", threadPoolTaskExecutor.getActiveCount());
		map.put("MaxQueueCount", TASK_QUEUE_CAPACITY);
		map.put("CurrentQueueingCount", threadPoolTaskExecutor.getQueueSize());
		
		return map;
	}
}
