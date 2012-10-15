package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.CorrectPopupUi.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
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
	interface Style extends CssResource {
	    String popupPanel();	
	}
	
	@UiField Style style;
	private static ProcessingPopupUiBinder uiBinder = GWT.create(ProcessingPopupUiBinder.class);

	interface ProcessingPopupUiBinder extends UiBinder<Widget, ProcessingPopup> {
	}

	public ProcessingPopup() {
		super(false);
		add(uiBinder.createAndBindUi(this));
		this.setStyleName(style.popupPanel());
	}
	
	@Override
	public void show() {
		super.show();
		//super.setSize((Window.getClientWidth())+"px", (Window.getClientHeight())+"px");
		
		super.setPopupPosition(10, 0);
		super.getElement().getStyle().setTop(15, Unit.PCT);
		super.getElement().getStyle().setBottom(10, Unit.PX);
		super.getElement().getStyle().setRight(10, Unit.PX);
	}

	
}
