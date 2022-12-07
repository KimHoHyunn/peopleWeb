package com.people.common.exception;

import com.people.common.consts.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 5477747503826209422L;
	
	private final ErrorCode errorCode;
    
}