package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProcessingPopup extends PopupPanel  {

	private static ProcessingPopupUiBinder uiBinder = GWT.create(ProcessingPopupUiBinder.class);

	interface ProcessingPopupUiBinder extends UiBinder<Widget, ProcessingPopup> {
	}

	public ProcessingPopup() {
		super(false);
		add(uiBinder.createAndBindUi(this));
	}

	
}
