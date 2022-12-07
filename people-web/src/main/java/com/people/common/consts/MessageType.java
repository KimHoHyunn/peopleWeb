package com.people.common.consts;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageType {
	
	public enum Type {
		STR,
		MAP,
		LIST,
	}
}
