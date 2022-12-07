package com.people.common.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.people.common.consts.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseVO {
//	public ResponseVO(int returnCode, String returnMessage, Object result) {
//		this.returnCode = returnCode;
//		this.returnMessage = returnMessage;
//		this.result = result;
//		// TODO Auto-generated constructor stub
//	}

	private int httpStatus;
	private String resultCode;
	private String resultMessage;
	private List<Map<String, Object>> resultData;
	
	public ResponseVO(ErrorCode errorCode) {
		this.httpStatus = errorCode.getHttpStatus();
		this.resultCode = errorCode.getResultCode();
		this.resultMessage = errorCode.getResultMessage();
		this.resultData = new ArrayList<>();
	}
	
	public void setResultData(String resultName, Object resultObject) {
		Map<String, Object> map = new HashMap<>();
		map.put(resultName, resultObject);
		this.resultData.add(map);
	}
	

    public String toStringDefault() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }    
    public String toStringMultiline() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    public String toStringNoClass() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }    
    public String toStringNoFieldName() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_FIELD_NAMES_STYLE);
    }
    public String toStringShortPrefix() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }    
    public String toStringSimple() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }   

}
