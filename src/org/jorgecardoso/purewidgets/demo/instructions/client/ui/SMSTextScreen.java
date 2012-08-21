package org.jorgecardoso.purewidgets.demo.instructions.client.ui;

import org.jorgecardoso.purewidgets.demo.instructions.client.ui.resources.Resources;
import org.purewidgets.shared.im.Place;
import org.purewidgets.shared.logging.Log;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

public class SMSTextScreen extends Composite  {

	private static MainScreenUiBinder uiBinder = GWT.create(MainScreenUiBinder.class);

	interface MainScreenUiBinder extends UiBinder<Widget, SMSTextScreen> {
	}
	ScreenMessages msgs = GWT.create(ScreenMessages.class);

	@UiField
	InlineHTML uiInlineHTML;
	
	public SMSTextScreen(Place place) {
		initWidget(uiBinder.createAndBindUi(this));
		
		// Using a TextResource
		uiInlineHTML.setHTML( Resources.INSTANCE.smsTextSvg().getText());
		
		Element e = MainScreen.getElemById(this.getElement(), "title");
		if ( null != e) {
			e.setInnerText(msgs.title());
		}
		
		e = MainScreen.getElemById(this.getElement(), "subtitle");
		if ( null != e) {
			e.setInnerText(msgs.smsAddressTitle(place.getPlacePhoneNumber()));
		}
		
		e = MainScreen.getElemById(this.getElement(), "buttonLabel");
		if ( null != e) {
			e.setInnerText(msgs.textLabel());
		}
		
		e = MainScreen.getElemById(this.getElement(), "buttonReference");
		if ( null != e) {
			e.setInnerText(msgs.buttonReferenceCode());
		}	

		e = MainScreen.getElemById(this.getElement(), "phoneNumber");
		if ( null != e) {
			e.setInnerText(place.getPlacePhoneNumber());
		}		

		e = MainScreen.getElemById(this.getElement(), "smsText1");
		if ( null != e) {
			e.setInnerText(msgs.smsText1(place.getPlaceReferenceCode()));
		}			
		e = MainScreen.getElemById(this.getElement(), "smsText2");
		if ( null != e) {
			e.setInnerText(msgs.smsText2());
		}			
	}


}
