package com.people.sample.async.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.config.AsyncConfig;
import com.people.sample.async.service.AsyncTaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@RequestMapping(value = "/async")
public class AsyncController {
	
	@Autowired
	private AsyncTaskService asyncTaskService;
	
	@Resource(name = "asyncConfig")
	private AsyncConfig asyncConfig;
	
	@GetMapping(path = "/async/check")
	public ResponseEntity<?> home() {
		log.info("checkSampleTaskExecute~");
		Map<String, Object> map = asyncConfig.checkSampleTaskExecute();
		return ResponseEntity.ok().body(map);
	}
	
	
//	@RequestMapping(path = "/async/testing", method = RequestMethod.POST)
	@PostMapping(path = "/async/testing")
	public ResponseEntity<?> testing() {
		Map<String, Object> map = asyncConfig.checkSampleTaskExecute();
		try {
			//Task 실행가능 여부 확
			if(Integer.valueOf(map.get("isAble").toString()) == 1) {
				asyncTaskService.jobRunningInBackground("PeopleSoft");
			}else {
				map.put("result","Thread 개수 초과");
			}
		} catch (Exception e) {
			//map.put("result",e.getMessage());	
			map.put("result","Thread 개수 초과");
		}
		return ResponseEntity.ok().body(map);
	}
}
