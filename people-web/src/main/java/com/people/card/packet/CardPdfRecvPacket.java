package com.people.card.packet;

import com.people.common.packet.Item;
import com.people.common.packet.Packet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardPdfRecvPacket extends Packet {
	private CardPdfRecvPacket() {
		super("UTF-8");
	}
	
	public static CardPdfRecvPacket create() {
		CardPdfRecvPacket packet = new CardPdfRecvPacket();

		packet.addItem(Item.set("PACKET_SIZE", 10, Item.Type.NUM));
		packet.addItem(Item.set("DELIMITER1",   1, Item.Type.TXT));
		packet.addItem(Item.set("JSON_SIZE",   10, Item.Type.NUM));
		packet.addItem(Item.set("DELIMITER2", 	1, Item.Type.TXT));
		packet.addItem(Item.set("JSON_DATA", 	0, Item.Type.DAT, "JSON_SIZE"));//가변길이데이터
		
		return packet;
	}
	
	public long getJsonSize() {
		return Long.valueOf(getValue("JSON_SIZE").toString()).longValue();
	}
}
