package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import java.util.ArrayList;
import java.util.Date;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.SlidingPanel;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MainScreen extends Composite {

	private static MainScreenUiBinder uiBinder = GWT.create(MainScreenUiBinder.class);

	interface MainScreenUiBinder extends UiBinder<Widget, MainScreen> {
	}

	
	ArrayList<PollScreenInterface> pollScreens;
	
	@UiField
	SlidingPanel slidingPanel;
	SuggestScreen suggestScreen;
	
	public MainScreen() {
		initWidget(uiBinder.createAndBindUi(this));
		this.pollScreens = new ArrayList<PollScreenInterface>();
		this.suggestScreen = new SuggestScreen();
	}

	public void showSuggestScreen() {
			this.slidingPanel.setWidget(this.suggestScreen);
	}
		
	public void show(EBVPollDao poll) {
		
		PollScreenInterface pollScreen = this.get(poll);
		
		if ( null == pollScreen ) {
			long now = new Date().getTime();
			
			if ( poll.getClosesOn() < now ) { // closed poll
				pollScreen = new PollResultsScreen(poll);
				// we cant add this to the list because the results screen must be recreated every time due to google charts
			} else {
				pollScreen = new PollScreen(poll);
				this.pollScreens.add(pollScreen);
			}
			
		}
		
		this.slidingPanel.setWidget(pollScreen.getUi());
	}
	
	
	private PollScreenInterface get(EBVPollDao poll) {
		for ( PollScreenInterface pollScreen : this.pollScreens ) {
			if ( pollScreen.getPoll().getPollId().equals(poll.getPollId()) ) {
				return pollScreen;
			}
		}
		return null;
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
