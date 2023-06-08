package com.people.common.filter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import javax.servlet.*;

import org.springframework.stereotype.Component;

import com.people.common.wrapper.ReadableRequestBodyWrapper;

@Component
public class CopyBodyFilter implements Filter {

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) 
  	throws IOException, ServletException {
    try {
      ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper((HttpServletRequest) request);
      wrapper.setAttribute("requestBody", wrapper.getRequestBody());
      chain.doFilter(wrapper, response);
    } catch (Exception e) {
      chain.doFilter(request, response);
    }
  }

}