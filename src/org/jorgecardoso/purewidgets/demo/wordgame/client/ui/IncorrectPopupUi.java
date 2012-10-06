package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.HighScoresUi.Style;

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

public class IncorrectPopupUi extends PopupPanel {
	interface Style extends CssResource {
	    String popupPanel();	
	}
	
	@UiField Style style;
	
	private static IncorrectPopupUiUiBinder uiBinder = GWT.create(IncorrectPopupUiUiBinder.class);

	interface IncorrectPopupUiUiBinder extends UiBinder<Widget, IncorrectPopupUi> {
	}

	@UiField
	Label labelUser;
	
	
	public IncorrectPopupUi(String nickname) {
		add(uiBinder.createAndBindUi(this));
		this.setStyleName(style.popupPanel());
		this.labelUser.setText("Tenta outra vez " + nickname + "!");
		
	}
}
