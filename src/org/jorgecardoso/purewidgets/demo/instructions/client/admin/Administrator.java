package org.jorgecardoso.purewidgets.demo.instructions.client.admin;

import java.util.ArrayList;


import org.jorgecardoso.purewidgets.demo.instructions.client.admin.ui.ApplicationParameter;
import org.purewidgets.client.application.PDApplication;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class Administrator {
	public static final String PARAM_NAME_ITERATE_SCREEN = "iterate";
	public static final String PARAM_NAME_SCREEN_INTERVAL = "screeninterval";

	
	private String parameterNames[] = {
			PARAM_NAME_ITERATE_SCREEN,
			PARAM_NAME_SCREEN_INTERVAL
	};

	private String parameterDescriptions[] = {
			"(true/false) true: Iterate through the various screens; false: just show one chosen randomly.",
			"(seconds) If iterate=true, How long will each screen be shown?"
			};

	
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
