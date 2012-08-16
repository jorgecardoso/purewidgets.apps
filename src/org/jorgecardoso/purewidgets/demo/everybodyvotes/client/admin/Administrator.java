package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin;

import java.util.ArrayList;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin.ui.ApplicationParameter;
import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.PublicYoutubePlayer;
import org.purewidgets.client.application.PDApplication;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class Administrator {
	public static final String PARAM_NAME_SCREEN_INTERVAL = "screeninterval";
	public static final String PARAM_NAME_SUGGEST_PROBABILITY = "suggestprobability";
	
	private String parameterNames[] = {
			PARAM_NAME_SCREEN_INTERVAL,
			PARAM_NAME_SUGGEST_PROBABILITY};

	private String parameterDescriptions[] = {
			"(seconds) The duration of each screen.",
			"(0..1) The probability of showing the suggest screen."};

	
	private PDApplication pdApplication;
	
	private ArrayList<ApplicationParameter> parameters;
	
	public Administrator( PDApplication pdApplication ) {
		this.pdApplication = pdApplication;
		this.parameters = new ArrayList<ApplicationParameter>();
		for ( int i = 0; i < parameterNames.length; i++ ) {
			ApplicationParameter param = new ApplicationParameter(parameterNames[i], 
					pdApplication.getParameterString(parameterNames[i], ""),
					parameterDescriptions[i]);
			this.parameters.add(param);
			RootPanel.get().add(param);
			
		}
		
		Button save = new Button("Save");
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				for ( ApplicationParameter param : Administrator.this.parameters ) {
					Administrator.this.pdApplication.setParameterString(param.getParameterName(), param.getParameterValue());
				}
				
			}
			
		});
		RootPanel.get().add(save);
		
	}
}
