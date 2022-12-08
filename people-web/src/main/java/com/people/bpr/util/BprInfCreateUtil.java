package com.people.bpr.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldFileUtil;
import com.people.common.oldutil.OldInfCreateUtil;
import com.people.common.oldutil.OldSystemUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class BprInfCreateUtil implements OldInfCreateUtil{
	//common
	
	private String c_agtSysC		;//agent시스템코드
	private String c_grpcoC			;//그룹사코드
	private String c_bprUpmuG		;//bpr업무구분
	private String c_imgUpmuG		;//image업무구분
	private String c_agtUpmuG		;//agent업무구분
	private String c_imgJobU		;//image작업유형
	private String c_imgCmmtYn		;//image주석여부
	private String c_scanHwnno		;//스캔행원번호
	private String c_sendHwnno		;//전송행원번호
	private String c_affairsHwnno	;//업무담당자행번호
	private String c_brno			;//점번호
	private String c_affirsBrno		;//업무담당자점번호
	private String c_scanDt			;//스캔일자
	private String c_sendDt			;//전송일자
	private String c_scanPstn		;//스캔위치
	
	//workflow field
	private String wf_wfYn			;//워크플로우업무여부
	
	//index key
	private String if_imgKey		;//이미지 키
	
	//file info
	private String fi_imgJobCnt		;//이미지작업건수
	private List<File> fileList		;//파일목록
	
	//meta data
	private String md_scnGrpcoC		;//접속사 그룹사코드
	private String md_scnBrno		;//접속자 지점번호
	private String md_scnHwnno		;//접속자 행번호
	private String md_dataDrdt		;//전송일자
	private String md_imgKey		;//이미지키 = bpr_e_img_key
	private String md_eccNo			;//TECC Code
	private String md_formC			;//서식코드
	private String md_fileCnt		;//파일건수
	private String md_trxdt			;//영업점거래일자
	private String md_brno			;//영업점번호
	private String md_totCnt		;//파일건수
	private String md_chanU			;//채널유형
	private String md_eDocSer		;//전자문서인덱스번호
	private String md_eDocIndvIdxNo	;//e전자문서개별인덱스번호
	private String md_eDocG			;//전자문서구분
	private String md_scanYn		;//첨부스캔여부
	private String md_ingamYn		;//인감여부
	private String md_memo			;//메모
	
	@Override
	public boolean createInfFile(File infFile) {
		//디렉토리생성
		OldFileUtil.mkdirs(infFile.getParent());
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("[COMMON]").append(SP_CHAR);
		sb.append("agtSysC=")	    .append(c_agtSysC		).append(SP_CHAR);
		sb.append("grpcoC=")		.append(c_grpcoC		).append(SP_CHAR);
		sb.append("bprUpmuG=")	    .append(c_bprUpmuG		).append(SP_CHAR);
		sb.append("imgUpmuG=")	    .append(c_imgUpmuG		).append(SP_CHAR);
		sb.append("agtUpmuG=")	    .append(c_agtUpmuG		).append(SP_CHAR);
		sb.append("imgJobU=")	    .append(c_imgJobU		).append(SP_CHAR);
		sb.append("imgCmmtYn=")	    .append(c_imgCmmtYn		).append(SP_CHAR);
		sb.append("scanHwnno=")	    .append(c_scanHwnno		).append(SP_CHAR);
		sb.append("sendHwnno=")		.append(c_sendHwnno		).append(SP_CHAR);
		sb.append("affairsHwnno=")	.append(c_affairsHwnno	).append(SP_CHAR);
		sb.append("brno=")			.append(c_brno			).append(SP_CHAR);
		sb.append("affirsBrno=")	.append(c_affirsBrno	).append(SP_CHAR);
		sb.append("scanDt=")		.append(c_scanDt		).append(SP_CHAR);
		sb.append("sendDt=")		.append(c_sendDt		).append(SP_CHAR);
		sb.append("scanPstn=")		.append(c_scanPstn		).append(SP_CHAR);
		sb.append(SP_CHAR);

		sb.append("[WORKFLOW_FILELD]").append(SP_CHAR);
		sb.append("wfYn=")	    	.append(wf_wfYn  		).append(SP_CHAR);
		sb.append(SP_CHAR);
		
		sb.append("[INDEX_KEY]").append(SP_CHAR);
		sb.append("imgKey=")	    .append(if_imgKey  		).append(SP_CHAR);
		sb.append(SP_CHAR);


		sb.append("[FILE_INFO]").append(SP_CHAR);
		sb.append("imgJobCnt=")	    .append(fi_imgJobCnt	).append(SP_CHAR);
		StringBuilder sbFileDesc = new StringBuilder();
		for(int i=0;i<fileList.size();i++) {
			File file = fileList.get(i);
			sbFileDesc.append("file").append(i+1).append("=").append(file.getName());
			sbFileDesc.append("^").append(file.length()).append("^");
			
			if(file.getName().toLowerCase().endsWith(".idx")) {
				sbFileDesc.append("idx^1^0101^05^1");
			} else {
				sbFileDesc.append("tif^1^0101^05^1");
			}
			
		}
		if(0 < fileList.size()) {
			sbFileDesc.append(SP_CHAR);
			sb.append(sbFileDesc);
		}

		sb.append("[META_DATA]").append(SP_CHAR);
		sb.append("scnGrpcoC="		).append(md_scnGrpcoC		).append(SP_CHAR);
		sb.append("scnBrno="		).append(md_scnBrno			).append(SP_CHAR);
		sb.append("scnHwnno="		).append(md_scnHwnno		).append(SP_CHAR);
		sb.append("dataDrdt="		).append(md_dataDrdt		).append(SP_CHAR);
		sb.append("imgKey="			).append(md_imgKey			).append(SP_CHAR);
		sb.append("eccNo="			).append(md_eccNo			).append(SP_CHAR);
		sb.append("formC="			).append(md_formC			).append(SP_CHAR);
		sb.append("fileCnt="		).append(md_fileCnt			).append(SP_CHAR);
		sb.append("trxdt="			).append(md_trxdt			).append(SP_CHAR);
		sb.append("brno="			).append(md_brno			).append(SP_CHAR);
		sb.append("totCnt="			).append(md_totCnt			).append(SP_CHAR);
		sb.append("chanU="			).append(md_chanU			).append(SP_CHAR);
		sb.append("eDocSer="		).append(md_eDocSer			).append(SP_CHAR);
		sb.append("eDocIndvIdxNo="	).append(md_eDocIndvIdxNo	).append(SP_CHAR);
		sb.append("eDocG="			).append(md_eDocG			).append(SP_CHAR);
		sb.append("scanYn="			).append(md_scanYn			).append(SP_CHAR);
		sb.append("ingamYn="		).append(md_ingamYn			).append(SP_CHAR);
		sb.append("memo="			).append(OldCommonUtil.safeReplace(md_memo, "\r\n", "\\n")).append(SP_CHAR);

		//전표
		if("0509".equals(c_agtUpmuG)) {
			sb.append("eGyljLine=1000").append(SP_CHAR);
			sb.append("trim=00000000").append(SP_CHAR);
		}
		
		//2 파일에 문자열을 쓰고 생성
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(infFile);
			fos.write(sb.toString().getBytes(ENCODING_CHARSET));
		} catch (IOException e) {
			log.error(OldSystemUtil.getExceptionLog(e));
		} finally {
			if(null != fos) { try {fos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}}
		}
		
		//3 파일이 생성되었는지 확인하여 리턴
		return infFile.exists();
	}

}
