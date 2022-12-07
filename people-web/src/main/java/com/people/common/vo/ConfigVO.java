package com.people.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConfigVO {
	//대상조회 제한 건수
	private int targetMaxCount;
	//데몬 sleep 주기
	private int sleepval;
	//SKIP여부
	private String workSkip;
	//쓰레드풀 사이즈
	private int threadPoolSize;
	//이미지변환대상 지연시간(sec)
	private int db2dbGapval;
	//디지털문서 키별 전속 간격
	private int sendSleepval;
	//계좌관리점 구분 사용여부
	private String bprDaemonG;
	//BPR전송대상(D 개발, T 테스트, O 온라인)
	private String bprSendG;
	//전송대상 from 일자수
	private int fromDay;
	//모든 대상 데이터 조회 여부
	private String allDay;
	
	
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
