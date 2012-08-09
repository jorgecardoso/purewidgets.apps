package org.jorgecardoso.purewidgets.demo.instructions.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
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

	public MainScreen() {
		initWidget(uiBinder.createAndBindUi(this));
		deckPanel.setAnimationEnabled(true);
	}

	public void showRandom() {
		int total = deckPanel.getWidgetCount();
		int index = (int)(Math.random()*total);
		this.deckPanel.showWidget(1);
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
