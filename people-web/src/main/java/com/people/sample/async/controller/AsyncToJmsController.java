package com.people.sample.async.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.people.sample.async.service.AsyncTaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AsyncToJmsController {
	
	@Autowired
	private AsyncTaskService asyncTaskService;
	
	@PostMapping(path = "/async/jmsinasync")
	public ResponseEntity<?> asyncDbToJms() {
		log.info(this.getClass().getName());
		asyncTaskService.asyncDbToJms();
		return ResponseEntity.ok().body("");
	}
	
	@PostMapping(path = "/async/jms")
	public ResponseEntity<?> dbToJms() {
		log.info(this.getClass().getName());
		List<Map<String, Object>> list = asyncTaskService.dbToJms();
		return ResponseEntity.ok().body(list);
	}
}
