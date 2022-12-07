package com.people.card.util;

import java.io.File;
import java.io.FileReader;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.people.card.vo.EpsBodyVO;
import com.people.card.vo.EpsHeaderVO;
import com.people.common.util.CommonUtil;
import com.people.common.util.SystemUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EpsUtil {
	public static Element makeElement(String key, String value, boolean bCData) {
		Element el = new Element(key);
		if(bCData) {
			el.addContent(new CDATA(value));
		} else {
			el.addContent(value);
		}
		
		return el;
	}
	
	public static String getElementValue(Element el, String key) {
		Element elChild = el.getChild(key);
		
		if(CommonUtil.isEmpty(elChild)) {
			return "";
		} else {
			return elChild.getValue();
		}
	}
	
	public enum ParseResult {
		SUCC,
		FAIL,
		SND_IPSB //전송불가
	}
	
	/**
	 * EAI 통신 결과 XML 파일을 파싱하여 EPSHeaderVO, EPSBodyVO 세팅
	 */
	public static ParseResult parseReceiveXML(File file, EpsHeaderVO ehVO, EpsBodyVO ebVO) {
		ParseResult result = ParseResult.FAIL;
		
		SAXBuilder builder = new SAXBuilder();
		try {
			Document jdomDoc = builder.build(new InputSource(new FileReader(file)));
			Element el_root = jdomDoc.getRootElement();
			Element el_header = el_root.getChild("eps_header");
			Element el_data = el_root.getChild("eps_data");
			Element el_data_flat = el_data.getChild("flat");
			
			ehVO.setEdoc_ui_g		 (getElementValue(el_header, "edoc_ui_g"));			
			ehVO.setEdoc_trm_g		 (getElementValue(el_header, "edoc_trm_g"));
			ehVO.setUi_ver           (getElementValue(el_header, "ui_ver"));           
			ehVO.setIp_adr           (getElementValue(el_header, "ip_adr"));           
			ehVO.setClient_mac_adr   (getElementValue(el_header, "client_mac_adr"));   
			ehVO.setClient_device_id (getElementValue(el_header, "client_device_id")); 
			ehVO.setGrpco_c          (getElementValue(el_header, "grpco_c"));          
			ehVO.setChan_tel_snd_dttm(getElementValue(el_header, "chan_tel_snd_dttm"));
			ehVO.setIn_scr_no        (getElementValue(el_header, "in_scr_no"));        
			ehVO.setIn_svc_id        (getElementValue(el_header, "in_svc_id"));        
			ehVO.setSession_id       (getElementValue(el_header, "session_id"));       
			ehVO.setTrx_br_c         (getElementValue(el_header, "trx_br_c"));         
			ehVO.setOp_hwnno         (getElementValue(el_header, "op_hwnno"));         
			ehVO.setNfit_item_incl_yn(getElementValue(el_header, "nfit_item_incl_yn"));
			ehVO.setGlb_uiq_id       (getElementValue(el_header, "glb_uiq_id"));       
			ehVO.setProc_sys_g       (getElementValue(el_header, "proc_sys_g"));       
			ehVO.setProc_svc_id      (getElementValue(el_header, "proc_svc_id"));      
			ehVO.setTrx_trm_no       (getElementValue(el_header, "trx_trm_no"));       
			ehVO.setSvr_tel_rcv_dttm (getElementValue(el_header, "svr_tel_rcv_dttm")); 
			ehVO.setProc_rslt_yn     (getElementValue(el_header, "proc_rslt_yn"));     
			ehVO.setFail_sys_c       (getElementValue(el_header, "fail_sys_c"));       
			ehVO.setFail_k           (getElementValue(el_header, "fail_k"));           
			ehVO.setErr_c            (getElementValue(el_header, "err_c"));            
			ehVO.setErr_c_ctnt       (getElementValue(el_header, "err_c_ctnt"));       
			ehVO.setAdd_msg          (getElementValue(el_header, "add_msg"));          
			ehVO.setClient_public_key(getElementValue(el_header, "client_public_key"));
			ehVO.setForm_occr_g      (getElementValue(el_header, "form_occr_g"));      
			
			
			
			ebVO.setData_flat_bpr_da_xst_f(getElementValue(el_data_flat, "bpr_da_xst_f"));
			ebVO.setData_flat_bpr_map_cd(getElementValue(el_data_flat, "bpr_map_cd"));
			ebVO.setData_flat_bpr_img_key_vl(getElementValue(el_data_flat, "bpr_img_key_vl"));
			ebVO.setData_flat_bpr_ecc_n(getElementValue(el_data_flat, "bpr_ecc_n"));
			ebVO.setData_flat_bpr_bne_rv_n(getElementValue(el_data_flat, "bpr_bne_rv_n"));
			ebVO.setData_flat_bpr_rv_hcd(getElementValue(el_data_flat, "bpr_rv_hcd"));
			ebVO.setData_flat_bpr_pcd(getElementValue(el_data_flat, "bpr_pcd"));
			ebVO.setData_flat_bpr_rcd(getElementValue(el_data_flat, "bpr_rcd"));
			ebVO.setData_flat_bpr_scn_crt_ccd(getElementValue(el_data_flat, "bpr_scn_crt_ccd"));
			ebVO.setData_flat_bln(getElementValue(el_data_flat, "bln"));
			
			String bprDaXstF = ebVO.getData_flat_bpr_da_xst_f();
			if(CommonUtil.isEmpty(bprDaXstF) || "Y".equals(bprDaXstF) == false) {
				result = ParseResult.SND_IPSB;
			} else if(   CommonUtil.isNotEmpty(ebVO.getData_flat_bpr_map_cd())
					  && CommonUtil.isNotEmpty(ebVO.getData_flat_bpr_img_key_vl())
					  && CommonUtil.isNotEmpty(ebVO.getData_flat_bpr_pcd())
					 ) {
				result = ParseResult.SUCC;
				
			}
			
			
		} catch (Exception e) {
			log.error(SystemUtil.getExceptionLog(e));
		}
		
		return result;
	}
}
