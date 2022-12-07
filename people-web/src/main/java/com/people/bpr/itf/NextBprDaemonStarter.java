package com.people.bpr.itf;

import com.people.common.step.EDocProcStep;

public class NextBprDaemonStarter {
	
	private static final EDocProcStep E_DOC_PROC_STEP = EDocProcStep.BPR;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BprDaemonRunner.run(NextBprDaemonStarter.class.getSimpleName(), E_DOC_PROC_STEP);
	}

}
