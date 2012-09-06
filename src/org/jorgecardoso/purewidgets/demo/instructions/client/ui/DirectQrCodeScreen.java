package org.jorgecardoso.purewidgets.demo.instructions.client.ui;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui.PollResultsScreenMessages;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

public class DirectQrCodeScreen extends Composite  {

	private static MainScreenUiBinder uiBinder = GWT.create(MainScreenUiBinder.class);

	interface MainScreenUiBinder extends UiBinder<Widget, DirectQrCodeScreen> {
	}


	@UiField
	Image qrCodeImage;
	
	public DirectQrCodeScreen(Place place) {
		initWidget(uiBinder.createAndBindUi(this));
		
		qrCodeImage.setUrl("https://chart.googleapis.com/chart?cht=qr&chs=500x500&chl="+place.getPlaceInteractionUrl());
			
	}


}
