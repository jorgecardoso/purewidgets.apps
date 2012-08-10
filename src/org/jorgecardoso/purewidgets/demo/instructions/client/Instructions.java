package org.jorgecardoso.purewidgets.demo.instructions.client;

import org.jorgecardoso.purewidgets.demo.instructions.client.ui.MainScreen;
import org.jorgecardoso.purewidgets.demo.instructions.client.ui.WebScreen;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.application.PDApplicationLifeCycle;
import org.purewidgets.client.im.InteractionManagerService;
import org.purewidgets.shared.im.Place;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Instructions implements EntryPoint, PDApplicationLifeCycle {
	MainScreen mainScreen;
	
	public String placeId;
	
	@Override
	public void onModuleLoad() {
		//placeId =  com.google.gwt.user.client.Window.Location.getParameter("placeid");
//		if ( null == placeId ) {
//			placeId = "DefaultPlace";
//		}

		PDApplication.load(this, "Instructions");
		
	
	}

	@Override
	public void onPDApplicationLoaded(PDApplication pdApplication) {
		pdApplication.getInteractionManager().getPlace(pdApplication.getPlaceId(), 
				pdApplication.getApplicationId(), new AsyncCallback<Place>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Place result) {
						mainScreen = new MainScreen(result);
						RootPanel.get().add(mainScreen);
						
						mainScreen.showRandom();
						
					}
			
		});
		
	}

}
