package com.people.card.itf;

import com.people.common.step.EDocProcStep;

public class NextCardDaemonStarter {
	
	private static final EDocProcStep E_DOC_PROC_STEP = EDocProcStep.CARD;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaemonRunner.run(NextCardDaemonStarter.class.getSimpleName(), E_DOC_PROC_STEP);
	}

}
