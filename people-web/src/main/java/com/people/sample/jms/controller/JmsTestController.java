package com.people.sample.jms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.people.sample.jms.producer.ArtemisProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JmsTestController {
    @Autowired
    ArtemisProducer artemisProducer;

    
    @PostMapping("/send/str")
    public ResponseEntity<?> sendStrToJms(@RequestParam String message) {
    	log.info("~~~ message = "+message);
    	//boolean isSend = artemisProducer.send(messageVO);
    	boolean isSend = artemisProducer.send(message);
		return ResponseEntity.ok().body(isSend);
    }
    
    @PostMapping("/send/map")
    public ResponseEntity<?> sendMap() {
    	Map<String, Object> map = new HashMap<>();
    	map.put("msg_1", "테스트 1");
    	map.put("msg_2", "테스트 2");
    	map.put("msg_3", "테스트 3");
    	boolean isSend = artemisProducer.send(map);
		return ResponseEntity.ok().body(isSend);
    }
    
    @PostMapping("/send/list")
    public ResponseEntity<?> sendList() {
    	List<Map<String, Object>> list = new ArrayList<>();
    	Map<String, Object> map = new HashMap<>();
    	map.put("msg_1", "테스트 1");
    	map.put("msg_2", "테스트 2");
    	map.put("msg_3", "테스트 3");
    	list.add(map);
    	map.clear();
    	map.put("msg_11", "테스트 11");
    	map.put("msg_22", "테스트 22");
    	map.put("msg_33", "테스트 33");
    	list.add(map);
    	
    	boolean isSend = artemisProducer.send(list);
		return ResponseEntity.ok().body(isSend);
    }
    
}
