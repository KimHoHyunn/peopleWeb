package com.people.img.itf;

import com.people.common.step.EDocProcStep;

public class NextImgDaemonStarter {
	
	private static final EDocProcStep E_DOC_PROC_STEP = EDocProcStep.IMG;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaemonRunner.run(NextImgDaemonStarter.class.getSimpleName(), E_DOC_PROC_STEP);
	}

}
