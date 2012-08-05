package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.SlidingPanel;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MainScreen extends Composite {

	private static MainScreenUiBinder uiBinder = GWT.create(MainScreenUiBinder.class);

	interface MainScreenUiBinder extends UiBinder<Widget, MainScreen> {
	}

	@UiField
	SlidingPanel slidingPanel;
	
//	@UiField
//	Label subtitleLabel;
	
	public MainScreen() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}

	public void show(EBVPollDao poll) {
		PollScreen pollScreen = new PollScreen(poll);
		this.slidingPanel.setWidget(pollScreen);
	}
	
	/**
	 * @return the slidingPanel
	 */
	public SlidingPanel getSlidingPanel() {
		return slidingPanel;
	}


	/**
	 * @param slidingPanel the slidingPanel to set
	 */
	public void setSlidingPanel(SlidingPanel slidingPanel) {
		this.slidingPanel = slidingPanel;
	}

	

}
