package org.jorgecardoso.purewidgets.demo.instructions.client;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.jorgecardoso.purewidgets.demo.instructions.client.admin.Administrator;
import org.jorgecardoso.purewidgets.demo.instructions.client.ui.MainScreen;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.application.PDApplicationLifeCycle;
import org.purewidgets.shared.im.Place;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Instructions implements EntryPoint, PDApplicationLifeCycle {
	private static final int DEFAULT_SCREEN_INTERVAL = 15; //seconds

	MainScreen mainScreen;
	
	public String placeId;
	
	@Override
	public void onModuleLoad() {
		Logger.getLogger("").setLevel(Level.ALL);
		Logger.getLogger("PuReWidgets").setLevel(Level.INFO);
		Log.get().setLevel(Level.ALL);
		PDApplication.load(this, "Instructions");
		
	
	}

	@Override
	public void onPDApplicationLoaded(final PDApplication pdApplication) {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			//new Admin().run();
			new Administrator(pdApplication);
			return;
		} 
		pdApplication.getInteractionManager().getPlace(pdApplication.getPlaceId(), 
				pdApplication.getApplicationId(), new AsyncCallback<Place>(){

					@Override
					public void onFailure(Throwable caught) {						
					}

					@Override
					public void onSuccess(Place result) {
						mainScreen = new MainScreen(result);
						RootPanel.get().add(mainScreen);
						
						if ( pdApplication.getParameterBoolean(Administrator.PARAM_NAME_ITERATE_SCREEN, true) ) {
							Log.debug(Instructions.this, "Iterating...");
							mainScreen.showRandom();
							
							Timer timer = new Timer() {

								@Override
								public void run() {
									Instructions.this.mainScreen.showNext();
								}
								
							};
							timer.scheduleRepeating(pdApplication.getParameterInt(Administrator.PARAM_NAME_SCREEN_INTERVAL, DEFAULT_SCREEN_INTERVAL)*1000);
							
						} else {
							Log.debug(Instructions.this, "Showing random screen...");
							mainScreen.showRandom();
						}
						
					}
			
		});
		
	}

}
