package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import org.purewidgets.client.application.PDApplication;

public class Util {

	private static PDApplication pdApplication;

	/**
	 * @return the pdApplication
	 */
	public static PDApplication getPdApplication() {
		return pdApplication;
	}

	/**
	 * @param pdApplication the pdApplication to set
	 */
	public static void setPdApplication(PDApplication pdApplication) {
		Util.pdApplication = pdApplication;
	}
	
	
	
}
