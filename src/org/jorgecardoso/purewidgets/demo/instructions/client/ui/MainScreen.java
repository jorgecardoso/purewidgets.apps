package org.jorgecardoso.purewidgets.demo.instructions.client.ui;

import org.purewidgets.shared.im.Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class MainScreen extends Composite  {

	private static MainScreenUiBinder uiBinder = GWT.create(MainScreenUiBinder.class);

	interface MainScreenUiBinder extends UiBinder<Widget, MainScreen> {
	}
	
	@UiField 
	DeckPanel deckPanel;

	Place place;
	public MainScreen(Place place) {
		this.place = place;
		initWidget(uiBinder.createAndBindUi(this));
		deckPanel.setAnimationEnabled(true);
	}

	@UiFactory
	public EmailButtonScreen createEmailButtonScreen() {
		return new EmailButtonScreen(this.place);
	}
	
	@UiFactory
	public EmailTextScreen createEmailTextScreen() {
		return new EmailTextScreen(this.place);
	}	

	@UiFactory
	public SMSTextScreen createSMSTextScreen() {
		return new SMSTextScreen(this.place);
	}		
	
	@UiFactory
	public SMSButtonScreen createSMSButtonScreen() {
		return new SMSButtonScreen(this.place);
	}	

	@UiFactory
	public WebScreen createWebScreen() {
		return new WebScreen(this.place);
	}		

	@UiFactory
	public QrCodeScreen createQrCodeScreen() {
		return new QrCodeScreen(this.place);
	}	
	
	public void showRandom() {
		int total = deckPanel.getWidgetCount();
		int index = (int)(Math.random()*total);
		this.deckPanel.showWidget(index);
	}


	
	public static Element getElemById(Element parent, String id) {
		if ( parent.getId().equals(id) ) {
			return parent;
		}
		NodeList<Node> nodes = parent.getChildNodes();
		
			for (int i = 0; i < nodes.getLength(); i++ ) {
				Node node = nodes.getItem(i);
				if ( node.getNodeType() == Node.ELEMENT_NODE ) {
									
					Element e1 = getElemById((Element)node, id);
					if ( null != e1 ) {
						return e1;
					}
				
				}
			}
			
		
		return null;
	}

}
