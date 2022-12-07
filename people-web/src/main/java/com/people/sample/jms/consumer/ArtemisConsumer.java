package com.people.sample.jms.consumer;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.common.consts.MessageType;
import com.people.common.vo.MessageVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ArtemisConsumer { //  implements MessageConverter {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${spring.jms.queue.destination}")
	String destinationQueue;
	
//  @JmsListener(destination = "${spring.jms.queue.destination}", containerFactory = "myFactory")
	@SuppressWarnings("unchecked")
	@JmsListener(destination = "${spring.jms.queue.destination}")
	public void receive(String json){
		log.info("\n\n");
		log.info(checkQueue(destinationQueue));
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			MessageVO messageVO = objectMapper.readValue(json, MessageVO.class);
			
			if(messageVO.getMSGTYPE().equals(MessageType.Type.STR.toString())) {
				String message = messageVO.getMSGJSON();
				log.info("Receive String Message = "+message);
			} else if(messageVO.getMSGTYPE().equals(MessageType.Type.MAP.toString())) {
				Map<String, Object> message = objectMapper.readValue(messageVO.getMSGJSON(), Map.class);
				log.info("Receive Map Message = "+message.toString());
			} else if(messageVO.getMSGTYPE().equals(MessageType.Type.LIST.toString())) {
				List<Map<String, Object>> message = objectMapper.readValue(messageVO.getMSGJSON(), List.class);
				log.info("Receive List Message = "+message.toString());
			}

			log.info("\n\n");
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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