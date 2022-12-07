package com.people.sample.restapi;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestApiService {

	@Autowired
	private RestApiDao restApiDao ;
	
	public List<Map<String, Object>> getData(Map<String, Object> params) {
		return restApiDao.getData(params);
	}
}
