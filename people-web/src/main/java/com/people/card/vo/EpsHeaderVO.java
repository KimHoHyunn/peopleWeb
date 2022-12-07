package com.people.card.vo;

import org.jdom2.Element;

import com.people.card.util.EpsUtil;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class EpsHeaderVO {
	String edoc_ui_g;
	String edoc_trm_g;
	String ui_ver;
	String ip_adr;
	String client_mac_adr;
	String client_device_id;
	String grpco_c;
	String chan_tel_snd_dttm;
	String in_scr_no;
	String in_svc_id;
	String session_id;
	String trx_br_c;
	String op_hwnno;
	String nfit_item_incl_yn;
	String glb_uiq_id;
	String proc_sys_g;
	String proc_svc_id;
	String trx_trm_no;
	String svr_tel_rcv_dttm;
	String proc_rslt_yn;
	String fail_sys_c;
	String fail_k;
	String err_c;
	String err_c_ctnt;
	String add_msg;
	String client_public_key;
	String form_occr_g;
	
	public Element makeHeader() {
		Element header = new Element("eps_header");
		header.addContent(EpsUtil.makeElement("edoc_ui_g"		 , this.getEdoc_ui_g(), false));
		header.addContent(EpsUtil.makeElement("edoc_trm_g"		 , this.getEdoc_trm_g(), false));
		header.addContent(EpsUtil.makeElement("ui_ver"           , this.getUi_ver(), false));
		header.addContent(EpsUtil.makeElement("ip_adr"           , this.getIp_adr(), false));
		header.addContent(EpsUtil.makeElement("client_mac_adr"   , this.getClient_mac_adr(), false));
		header.addContent(EpsUtil.makeElement("client_device_id" , this.getClient_device_id(), false));
		header.addContent(EpsUtil.makeElement("grpco_c"          , this.getGrpco_c(), false));
		header.addContent(EpsUtil.makeElement("chan_tel_snd_dttm", this.getChan_tel_snd_dttm(), false));
		header.addContent(EpsUtil.makeElement("in_scr_no"        , this.getIn_scr_no(), false));
		header.addContent(EpsUtil.makeElement("in_svc_id"        , this.getIn_svc_id(), false));
		header.addContent(EpsUtil.makeElement("session_id"       , this.getSession_id(), false));
		header.addContent(EpsUtil.makeElement("trx_br_c"         , this.getTrx_br_c(), false));
		header.addContent(EpsUtil.makeElement("op_hwnno"         , this.getOp_hwnno(), false));
		header.addContent(EpsUtil.makeElement("nfit_item_incl_yn", this.getNfit_item_incl_yn(), false));
		header.addContent(EpsUtil.makeElement("glb_uiq_id"       , this.getGlb_uiq_id(), false));
		header.addContent(EpsUtil.makeElement("proc_sys_g"       , this.getProc_sys_g(), false));
		header.addContent(EpsUtil.makeElement("proc_svc_id"      , this.getProc_svc_id(), false));
		header.addContent(EpsUtil.makeElement("trx_trm_no"       , this.getTrx_trm_no(), false));
		header.addContent(EpsUtil.makeElement("svr_tel_rcv_dttm" , this.getSvr_tel_rcv_dttm(), false));
		header.addContent(EpsUtil.makeElement("proc_rslt_yn"     , this.getProc_rslt_yn(), false));
		header.addContent(EpsUtil.makeElement("fail_sys_c"       , this.getFail_sys_c(), false));
		header.addContent(EpsUtil.makeElement("fail_k"           , this.getFail_k(), false));
		header.addContent(EpsUtil.makeElement("err_c"            , this.getErr_c(), false));
		header.addContent(EpsUtil.makeElement("err_c_ctnt"       , this.getErr_c_ctnt(), false));
		header.addContent(EpsUtil.makeElement("add_msg"          , this.getAdd_msg(), false));
		header.addContent(EpsUtil.makeElement("client_public_key", this.getClient_public_key(), false));
		header.addContent(EpsUtil.makeElement("form_occr_g"      , this.getForm_occr_g(), false));

		return header;
		
	}
}
