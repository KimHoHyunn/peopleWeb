package com.people.sample;

import com.people.common.util.BeanUtils;
import com.people.sample.restapi.RestApiDao;

public class NoAutowireToCallDao {
	
	
	public void test() {
		
		RestApiDao restApiDao = (RestApiDao) BeanUtils.getBean("RestApiDao"); 
	}
}
