package com.people.card.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.people.common.util.InfCreateUtil;
import com.people.common.util.SystemUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class CardInfCreateUtil implements InfCreateUtil {
	//COMMON PART
	private String c_BPR_PS_SHP_CO_CD = "";
	private String c_BPR_BNE_ZCD = "";
	private String c_BPR_MAP_CD = "";
	private String c_BPR_SCN_CRT_CCD = "";
	private String c_BPR_PS_CHL_CCD = "";
	private String c_BPR_MAD_CRT_CCD = "";
	private String c_BPR_ET_CCD = "";
	private String c_BPR_CRT_DT = "";
	private String c_BPR_TI_DT = "";
	private String c_BPR_CRT_RMK_EN = "";
	private String c_BPR_TI_RMK_EN = "";
	private String c_BPR_PS_HCD = "";
	private String c_BPR_CRT_TMN_IP_AR = "";
	private String c_BPR_FIL_ECR_F = "";
	private String c_BPR_IMG_KEY_SE_F = "";
	private String c_BPR_ECC_N_SE_F = "";
	private String c_BPRS_CD = "";
	private String c_BPR_EGC_F = "";
	private String c_BPR_PCD = "";

	//index key
	private String ik_BPR_IDX_KEY_VL = "";
	
	//META DATA
	private String md_BPR_MTA_COL_TT = "";
	
	//FILE INFO
	private List<String> docFormCList;
	private List<File> fileList;
	
	@Override
	public boolean createInfFile(File infFile) {

		//파일생성할 문자열 생성
		StringBuffer sb = new StringBuffer();
		//common
		sb.append("[COMMON]").append(SP_CHAR);
		sb.append("BPR_PS_SHP_CO_CD=") .append(this.c_BPR_PS_SHP_CO_CD).append(SP_CHAR);
		sb.append("BPR_BNE_ZCD=")      .append(this.c_BPR_BNE_ZCD).append(SP_CHAR);
		sb.append("BPR_MAP_CD=")       .append(this.c_BPR_MAP_CD).append(SP_CHAR);
		sb.append("BPR_SCN_CRT_CCD=")  .append(this.c_BPR_SCN_CRT_CCD).append(SP_CHAR);
		sb.append("BPR_PS_CHL_CCD=")   .append(this.c_BPR_PS_CHL_CCD).append(SP_CHAR);
		sb.append("BPR_MAD_CRT_CCD=")  .append(this.c_BPR_MAD_CRT_CCD).append(SP_CHAR);
		sb.append("BPR_ET_CCD=")       .append(this.c_BPR_ET_CCD).append(SP_CHAR);
		sb.append("BPR_CRT_DT=")       .append(this.c_BPR_CRT_DT).append(SP_CHAR);
		sb.append("BPR_TI_DT=")        .append(this.c_BPR_TI_DT).append(SP_CHAR);
		sb.append("BPR_CRT_RMK_EN=")   .append(this.c_BPR_CRT_RMK_EN).append(SP_CHAR);
		sb.append("BPR_TI_RMK_EN=")    .append(this.c_BPR_TI_RMK_EN).append(SP_CHAR);
		sb.append("BPR_PS_HCD=")       .append(this.c_BPR_PS_HCD).append(SP_CHAR);
		sb.append("BPR_CRT_TMN_IP_AR=").append(this.c_BPR_CRT_TMN_IP_AR).append(SP_CHAR);
		sb.append("BPR_FIL_ECR_F=")    .append(this.c_BPR_FIL_ECR_F).append(SP_CHAR);
		sb.append("BPR_IMG_KEY_SE_F=") .append(this.c_BPR_IMG_KEY_SE_F).append(SP_CHAR);
		sb.append("BPR_ECC_N_SE_F=")   .append(this.c_BPR_ECC_N_SE_F).append(SP_CHAR);
		sb.append("BPRS_CD=")          .append(this.c_BPRS_CD).append(SP_CHAR);
		sb.append("BPR_EGC_F=")        .append(this.c_BPR_EGC_F).append(SP_CHAR);
		sb.append("BPR_PCD=")          .append(this.c_BPR_PCD).append(SP_CHAR);
		sb.append(SP_CHAR);
		
		//index key
		sb.append("[INDEX_KEY]").append(SP_CHAR);
		sb.append("BPR_IDX_KEY_VL=") .append(this.ik_BPR_IDX_KEY_VL).append(SP_CHAR);
		sb.append(SP_CHAR);
		
		//meta data
		sb.append("[META_DATA]").append(SP_CHAR);
		sb.append("BPR_MTA_COL_TT=") .append(this.md_BPR_MTA_COL_TT).append(SP_CHAR);
		sb.append(SP_CHAR);
		
		//FILE INFO
		sb.append("[FILE_INFO]").append(SP_CHAR);
		sb.append("BPR_TI_TO_FIL_CNT=") .append(fileList.size()).append(SP_CHAR);
		for(int i=0;i<fileList.size();i++) {
			File file = fileList.get(i);
			String fileName = file.getName();
			String docFormC = docFormCList.get(i);

			sb.append("BPR_FILE_TT") .append(i+1).append("=");
			sb.append(docFormC).append("|").append("tif").append("|");
			sb.append(fileName).append("|").append(fileName) .append("|").append(SP_CHAR);
		}
		
		//2 파일에 문자열을 쓰고 생성
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(infFile);
			fos.write(sb.toString().getBytes(ENCODING_CHARSET));
		} catch (IOException e) {
			log.error(SystemUtil.getExceptionLog(e));
		} finally {
			if(null != fos) { try {fos.close();} catch(IOException e) {log.error(SystemUtil.getExceptionLog(e));}}
		}
		
		//3 파일이 생성되었는지 확인하여 리턴
		return infFile.exists();
	}
}
