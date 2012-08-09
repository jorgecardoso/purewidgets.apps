package org.jorgecardoso.purewidgets.demo.instructions.client;

import org.jorgecardoso.purewidgets.demo.instructions.client.ui.MainScreen;
import org.jorgecardoso.purewidgets.demo.instructions.client.ui.WebScreen;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class Instructions implements EntryPoint{
	MainScreen mainScreen;
	
	@Override
	public void onModuleLoad() {
		mainScreen = new MainScreen();
		RootPanel.get().add(mainScreen);
		
		mainScreen.showRandom();
	
	}

}
