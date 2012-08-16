package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationParameter extends Composite {

	private static ApplicationParameterUiBinder uiBinder = GWT
			.create(ApplicationParameterUiBinder.class);

	interface ApplicationParameterUiBinder extends UiBinder<Widget, ApplicationParameter> {
	}
	
	@UiField
	InlineLabel uiParamName;
	
	@UiField
	TextBox uiParamValue;
	
	@UiField
	InlineLabel uiParamDescription;	

	public ApplicationParameter(String name, String value, String description) {
		initWidget(uiBinder.createAndBindUi(this));
		uiParamName.setText(name);
		uiParamValue.setText(value);
		uiParamDescription.setText(description);
	}

	public String getParameterName() {
		return this.uiParamName.getText();
	}
	
	public String getParameterValue() {
		return this.uiParamValue.getText();
	}
}

