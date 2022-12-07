package com.people.sample.async.exception;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler{

	@Override
		public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		log.error("Thread Error Exception");
		log.error("exception Message :: " + throwable.getMessage());
		log.error("method name :: " + method.getName());
			for(Object param : obj) {
				log.error("param Val ::: " + param);
			}
		}
}