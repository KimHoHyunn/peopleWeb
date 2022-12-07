package com.people.common.packet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.people.common.util.CommonUtil;

/**
 * 공통전문조립
 * @author mh042
 *
 */
public class Packet {
	//항목리스트
	private ArrayList<Item> items = new ArrayList<Item>();
	
	//항목명
	private HashMap<String, Item> nameAccess = new HashMap<String, Item>();
	
	//String <-> Byte 간 인코딩
	private final String encodingCharsetName;
	
	//로그기록용 항목명과 길의의 최대길이
	private int itemNameMax = 0;
	private int itemLengthMax = 0;
	
	protected Packet() {
		this.encodingCharsetName = null;
	}
	
	protected Packet(String encodingCharsetName) {
		this.encodingCharsetName = encodingCharsetName;
	}
	
	//패킷추가
	public void addItem(Item item) {
		items.add(item);
		if(nameAccess.containsKey(item.getName())) {
			throw new RuntimeException("Duplicated item Name");
		}
		nameAccess.put(item.getName(), item);
		itemNameMax=Math.max(itemNameMax, item.getName().length());
		itemLengthMax=Math.max(itemLengthMax, Integer.toString(item.getLength()).length());
	}
	
	/**
	 * 항목수
	 * @return
	 */
	protected int count() {
		return items.size();
	}
	
	/**
	 * 항목길이의 합
	 */
	public int length() {
		int packetLength = 0;
		for(Item item : items) {
			packetLength += item.getLength();
		}
		return packetLength;
	}
	/**
	 * 항목명으로 항목개체 가져오기
	 */
	private Item getItem(String name) {
		Item item = nameAccess.get(name.toUpperCase());
		if(CommonUtil.isEmpty(item)) {
			throw new RuntimeException("There is no item with the name = " + name);
		}
		return item;
	}
	/**
	 * 항목명으로 값반환
	 */
	public Object getValue(String name) {
		return getItem(name).getValue();
	}
	
	/**
	 * 항목명으로 값설정
	 * 이름, 값
	 */
	public void setValue(String name, Object value) {
		getItem(name).setValue(value);
	}
	public void setValue(String name, Object value, int length) {
		Item item = getItem(name);
		item.setValue(value);
		item.setLength(length);
		itemLengthMax=Math.max(itemLengthMax, Integer.toString(item.getLength()).length());
	}
	
	public void setValue(String name, Object value, int length, String logValue) {
		Item item = getItem(name);
		item.setValue(value);
		item.setLength(length);
		item.setLogValue(logValue);
		itemLengthMax=Math.max(itemLengthMax, Integer.toString(item.getLength()).length());
	}
	
	/**
	 * 전문생성
	 */
	public byte[] serialize () throws UnsupportedEncodingException {
		byte[] data = new byte[this.length()];
		
		StringBuffer sb = new StringBuffer(this.length());
		for(Item item : items) {
			sb.append(item.getData());
		}
		
		if(CommonUtil.isEmpty(encodingCharsetName)) {
			data = sb.toString().getBytes();
		} else {
			data = sb.toString().getBytes(encodingCharsetName);
		}
		return data;
	}
	
	/**
	 * 전문 파싱
	 */
	public void parse(byte[] data) throws UnsupportedEncodingException {
		//가변길이 패킷 때문에 주석처리
		if(CommonUtil.isEmpty(data) /*|| data.length != this.length() */) {
			throw new RuntimeException("Data is null or invalid length");
		}
		
		String strData = "";
		
		if(CommonUtil.isEmpty(encodingCharsetName)) {
			strData = new String(data);
		} else {
			strData = new String(data, encodingCharsetName);
		}
		
		int beginIndex = 0;
		int endIndex = 0;
		for(Item item : items) {
			int len = item.getLength();
			
			if(0 == len && !CommonUtil.isEmpty(item.getRefName()) ) {
				len = Integer.valueOf(getValue(item.getRefName()).toString()).intValue();
			}
			
			endIndex += len;
			item.setValue(strData.substring(beginIndex, endIndex));
			beginIndex += len;
		}
	}
}
