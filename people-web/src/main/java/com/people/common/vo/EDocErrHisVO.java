package com.people.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.people.common.dao.EdsDao;
import com.people.common.oldutil.CommonUtil;

import lombok.Getter;

@Getter
public class EDocErrHisVO {
	
	private String eDocIdxNo;		//전자문서인덱스번호
	private int imgSer;				//이미지일련번호
	private int eDocProcSeqV;		//전자문서처린SEQ
	private int drSer;				//등록일련번호
	private String eDocProcStepCval;//전자문서처리단계코드
	private String eDocProcErrC;	//전자문서처리 에러코드
	private String svrIp;			//서버아이피
	private String eDocErrCtnt;		//전자문서처리 에러내용
	private String drId;			//등록아이디
	private String drDttm;			//등록일시
	private String logFilePath;		//로그파일경로
	
	public boolean setData(String eDocIdxNo, int imgSer, int eDocProcSeqV, String eDocProcErrC, String eDocErrCtnt) {
		this.eDocIdxNo = eDocIdxNo;
		this.imgSer = imgSer;
		this.eDocProcSeqV = eDocProcSeqV;
		this.eDocProcErrC = eDocProcErrC;
		this.eDocProcStepCval = EdsDao.getEDocProcStep().getCval();
		this.eDocErrCtnt = eDocErrCtnt;
		
		return !(    CommonUtil.isEmpty(this.eDocIdxNo)
				  || CommonUtil.isEmpty(this.imgSer)
				  || CommonUtil.isEmpty(this.eDocProcSeqV)
				  || CommonUtil.isEmpty(this.eDocProcErrC)
				  || CommonUtil.isEmpty(this.eDocProcStepCval)
				  || CommonUtil.isEmpty(this.eDocErrCtnt)
				);
		
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
