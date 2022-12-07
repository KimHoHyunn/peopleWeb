package com.people.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ErrorVO {
	private int httpStatus;
	private String errorCode;
	private String errorMessage;

}
