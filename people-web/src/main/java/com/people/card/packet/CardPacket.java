package com.people.card.packet;

import com.people.common.packet.Item;
import com.people.common.packet.Packet;

public class CardPacket extends Packet {
	private CardPacket() {
		super();
	}
	
	public static CardPacket create() {
		CardPacket packet = new CardPacket();

		packet.addItem(Item.set("RQ_SO_CCD",       1));
		packet.addItem(Item.set("TMN_IP_AR",      12));
		packet.addItem(Item.set("GP_CO_CD",        4));
		packet.addItem(Item.set("SYS_BNE_CCD",     3));
		packet.addItem(Item.set("FIL_NM",        256));
		packet.addItem(Item.set("FIL_SZ",         11, Item.Type.NUM));
		packet.addItem(Item.set("PACKET_SZ",       6, Item.Type.NUM));
		packet.addItem(Item.set("DL_CCD",          2));
		
		return packet;
	}
	
	public static final int PACKET_SZ = 65536;
	
	public static final class DL {
		public static final String REQUEST = "RE";
		public static final String FIRST   = "00";
		public static final String CONTINUE = "01";
		public static final String LAST     = "02";
		public static final String SUCCESS  = "SC";
		
	}
	
	public int getPacketSize() {
		return Integer.valueOf(getValue("PACKET_SZ").toString()).intValue();
	}
}
