package com.people.sample.logtest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LogTestController {
	
	@Autowired
	private LogTestDao logTestDao;
	
	@GetMapping(path = "/logtest")
    public ResponseEntity<?>  sampleLogFileSeperate(String aa, String bb) {
		
		String test= "log Test... package log~";
		log.info("~~~~~~ test : "+test);
		Map<String, Object> map = new HashMap<String, Object>();
		//restApiDao.getData(map);

		return ResponseEntity.ok().body(logTestDao.getData(map));
    }
}
