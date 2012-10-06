package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.IncorrectPopupUi.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class CorrectPopupUi extends PopupPanel {
	interface Style extends CssResource {
	    String popupPanel();	
	}
	
	@UiField Style style;
	
	private static CorrectPopupUiUiBinder uiBinder = GWT.create(CorrectPopupUiUiBinder.class);

	interface CorrectPopupUiUiBinder extends UiBinder<Widget, CorrectPopupUi> {
	}

	@UiField
	Label labelUser;
	
	@UiField
	Label labelDefinition;
	
	public CorrectPopupUi(String nickname, String definition) {
		add(uiBinder.createAndBindUi(this));
		this.setStyleName(style.popupPanel());
		this.labelUser.setText("Parabéns " + nickname + "!");
		this.labelDefinition.setText(definition);
	}
}
