package com.people.common.vo;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirFileVO {
	private String eDocIdxNo;
	private int imgSer;
	private String policy_apln_no;
	private int file_seq;
	private int file_total_page_cnt;
	private File fileInfo_org;
	private File fileInfo_tmp;
	private File fileInfo_out;
	private String file_key;
	private boolean convert_ret = false;
	private boolean db_Ret = false;
	private String output_file_folder;
	private String conv_errMsg;
	private String conv_eTime;
	private Map<String, Object> mapRet;
	private long conv_time;
	
	
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
