package com.people.sample.jms.producer;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.common.consts.MessageType;
import com.people.common.util.SystemUtil;
import com.people.common.vo.MessageVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableJms
public class ArtemisProducer {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${spring.jms.queue.destination}")
	String destinationQueue;
	
	public boolean send(Map<String, Object> map){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			MessageVO messageVO = new MessageVO(); 
			
			String msg = objectMapper.writeValueAsString(map);
			
			messageVO.setMSGTYPE(MessageType.Type.MAP.toString());
			messageVO.setMSGJSON(msg);
			
			String sendMsg = objectMapper.writeValueAsString(messageVO);
			return writeQueue(sendMsg);
		} catch (Exception e) {
			SystemUtil.getExceptionLog(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean send(List<Map<String, Object>> list){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			MessageVO messageVO = new MessageVO(); 
			
			String msg = objectMapper.writeValueAsString(list);

			messageVO.setMSGTYPE(MessageType.Type.LIST.toString());
			messageVO.setMSGJSON(msg);
			
			String sendMsg = objectMapper.writeValueAsString(messageVO);
			return writeQueue(sendMsg);
		} catch (Exception e) {
			SystemUtil.getExceptionLog(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean send(String msg){
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			MessageVO messageVO = new MessageVO(); 

			messageVO.setMSGTYPE(MessageType.Type.STR.toString());
			messageVO.setMSGJSON(msg);
			String sendMsg = objectMapper.writeValueAsString(messageVO);
			return writeQueue(sendMsg);
		} catch (Exception e) {
			SystemUtil.getExceptionLog(e);
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean writeQueue(String msg){
		try {

			//log.info(checkQueue(destinationQueue));
			log.info("Send Message in Producer = " + msg);
			
			jmsTemplate.convertAndSend(destinationQueue, msg);
			return true;
		} catch (Exception e) {
			SystemUtil.getExceptionLog(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public String checkQueue(String queue) {
	    return jmsTemplate.browse(queue, (session, browser) -> {
	        Enumeration<?> messages = browser.getEnumeration();
	        int total = 0;
	        while (messages.hasMoreElements()) {
	            messages.nextElement();
	            total++;
	        }
	        return String.format("Total '%d elements waiting in %s", total, queue);
	    });
	}


}