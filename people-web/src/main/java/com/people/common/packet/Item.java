package com.people.common.packet;

import com.people.common.util.CommonUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
	private final String name;
	private int length;
	private Object value;
	private final Type type;
	private final String refName;
	private String logValue;
	
	public enum Type {
		TXT,
		NUM,
		DAT,
	}
	
	private Item(String name, int length, Type type, String refName, String logValue ) {
		this.name = name;
		this.length = length;
		this.type = type;
		this.refName = refName;
		this.logValue = logValue;
		
		this.setValue("");
	}
	public static Item set(String name, int length, Type type, String refName ) {
		return new Item(name, length, type, refName, "");
	}
	public static Item set(String name, int length, Type type ) {
		return new Item(name, length, type, "", "");
	}
	public static Item set(String name, int length ) {
		return new Item(name, length,Type.TXT, "", "");
	}
	
	public void setValue(Object value) {
		if(CommonUtil.isEmpty(value)) {
			value = ( this.type == Type.NUM ? 0 : "" );
		} 
		this.value = value;
	}
	public Object getValue() {
		if(this.type ==  Type.NUM) {
			if(String.class.isInstance(this.value)) {
				return Long.valueOf(String.valueOf(this.value));
			} else {
				return this.value;
			}
		} else {
			return String.valueOf(this.value);
		}
	}
	
	/**
	 * 전문형식 변환
	 * 문자는 남은길이만큼 ' ' 붙임
	 * 숫자는 남은 길이만큼 0 붙이
	 * 데이트 : 포맷없이 문자
	 */
	public String getData() {
		if(this.type.equals(Type.NUM)) {
			return String.format("%0"+this.length+"d", this.getValue()).substring(0,this.length);
		} else if (this.type.equals(Type.TXT)) {
			return String.format("%0"+this.length+"s", this.getValue()).substring(0,this.length);
		} else {
			return String.valueOf(this.getValue());
		}
	}
	
	
}
