package com.people.card.vo;

import org.jdom2.Element;

import com.people.card.util.EpsUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EpsBodyVO {
	String appData_call_no;
	String appData_cus_no;
	String appData_itm_g;
	String appData_itm_ctnt;

	String actData_edoc_sno;
	String actData_trx_no;
	String actData_silno;
	String actData_silno_g;
	String actData_cusnm;
	String actData_rq_eps_data;
	String actData_rq;
	String actData_rp;
	String actData_kyejwa;

	String data_actsys1_bpr_ecc_n;
	
	String data_flat_bpr_da_xst_f;
	String data_flat_bpr_map_cd;
	String data_flat_bpr_img_key_vl;
	String data_flat_bpr_ecc_n;
	String data_flat_bpr_bne_rv_n;
	String data_flat_bpr_rv_hcd;
	String data_flat_bpr_pcd;
	String data_flat_bpr_rcd;
	String data_flat_bpr_scn_crt_ccd;
	String data_flat_bln;

	String error_inter_svr_proc_rslt;
	String error_inter_svr_msg_c;
	String error_inter_svr_msg_ctnt;
	
	public Element makeAppData() {
		Element element = new Element("eps_app_data");
		element.addContent(EpsUtil.makeElement("call_sno", this.getAppData_call_no(), false));
		element.addContent(EpsUtil.makeElement("cus_no", this.getAppData_cus_no(), false));
		element.addContent(EpsUtil.makeElement("itm_g", this.getAppData_itm_g(), false));
		element.addContent(EpsUtil.makeElement("itm_ctnt", this.getAppData_itm_ctnt(), false));
		
		return element;
	}
	
	public Element makeActData() {
		Element element = new Element("eps_act_data");
		
		element.addContent(EpsUtil.makeElement("edoc_sno", this.getActData_edoc_sno(), false));
		element.addContent(EpsUtil.makeElement("trx_no", this.getActData_trx_no(), false));
		element.addContent(EpsUtil.makeElement("silno", this.getActData_silno(), false));
		element.addContent(EpsUtil.makeElement("silno_g", this.getActData_silno_g(), false));
		element.addContent(EpsUtil.makeElement("cusnm", this.getActData_cusnm(), false));
		element.addContent(EpsUtil.makeElement("rq_eps_data", this.getActData_rq_eps_data(), false));
		element.addContent(EpsUtil.makeElement("rq", this.getActData_rq(), false));
		element.addContent(EpsUtil.makeElement("rp", this.getActData_rp(), false));
		element.addContent(EpsUtil.makeElement("kyejwa", this.getActData_kyejwa(), false));
		
		return element;
	}
	
	public Element makeData() {
		Element element = new Element("eps_data");
		Element actsys1 = new Element("actsys1");
		Element content = EpsUtil.makeElement("bpr_ecc_n", this.getData_actsys1_bpr_ecc_n(), true);
		
		actsys1.addContent(content);
		element.addContent(actsys1);
		
		return element;
	}
		
}
