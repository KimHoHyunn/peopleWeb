package com.people.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TestInterceptor implements HandlerInterceptor {

  private void readBody(final HttpServletRequest request, final HttpServletResponse response) {
    
	String reqBody = (String) request.getAttribute("requestBody");
    
        /*
          reqBody 값을 읽어 임의 처리.
        */   
	StringBuffer sb = new StringBuffer();
	sb.append("\n").append("Request URL  = ").append(request.getRequestURL());
	sb.append("\n").append("Request Body = ").append(reqBody);
	log.info(sb.toString());
  }

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) 
  	throws Exception {
	  log.info("\n\npreHandle");
	  this.readBody(request, response);
	  return true;	
  }
}
