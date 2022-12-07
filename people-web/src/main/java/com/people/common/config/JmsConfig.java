package com.people.common.config;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration.class})
public class JmsConfig {

	  @Value("${spring.artemis.broker-url}")
	  String brokerUrl;

	  @Bean
	  public BrokerService broker() throws Exception {

	    BrokerService broker = new BrokerService();
	    broker.setPersistent(false);
	    broker.setUseJmx(true);
	    broker.addConnector(brokerUrl);
	    return broker;
	  }
	  
	  @Bean
	  public JmsTemplate jmsTemplate() {
	    return new JmsTemplate(new PooledConnectionFactory(brokerUrl));
	  }
	  
	  @Bean
	  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {

	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(new PooledConnectionFactory(brokerUrl));
	    return factory;
	  }

}
