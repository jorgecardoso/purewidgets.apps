/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui;


import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Log;
import org.purewidgets.client.widgets.PdTagCloud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class VideoScreen extends Composite  {

	private static VideoScreenUiBinder uiBinder = GWT
			.create(VideoScreenUiBinder.class);

	interface VideoScreenUiBinder extends UiBinder<Widget, VideoScreen> {
	}

	@UiField 
	DeckPanel deckPanelTop;
	
	@UiField 
	DeckPanel deckPanelBottom;
	
	@UiField
	HTMLPanel htmlPanel;
	
	@UiField
	SimplePanel simplePanelPlayer;
	
	@UiField
	public VideoList toPlayNext;
	
	@UiField
	public VideoList recentlyPlayed;
	
	@UiField
	public PdTagCloud tagCloud;
	
	public Queue queue;

	private PopupPanel channelLogo;
	
	public VideoScreen() {
		Log.debugFinest(this, "Creating main screen");
		initWidget(uiBinder.createAndBindUi(this));
		
		
		deckPanelBottom.setAnimationEnabled(true);
		deckPanelTop.setAnimationEnabled(true);
		
		queue = new Queue();
		
		channelLogo = new PopupPanel();
		Image logo = new Image("icon_white.png");
		logo.setSize("100px", "100px");
		channelLogo.add( logo );
		channelLogo.setPopupPosition(20, 20);
	}

	public void showVideo() {
		this.deckPanelTop.showWidget(0);
		channelLogo.show();
	}
	
	public void showActivity() {
		Log.debugFinest(this, "Showing Activity screen");
		channelLogo.hide();
		//Log.debug(this,  "Recently played: " + this.recentlyPlayed);
		//Log.debug(this,  "HTML panel: " + this.htmlPanel);
		try {
			this.deckPanelTop.showWidget(1);
			this.deckPanelBottom.showWidget(0);
		} catch (Exception e) {
			Log.error(this, "Error:", e);
		}
	}
	
	public void showNext() {
		Log.debugFinest(this, "Showing Next videos screen");
		channelLogo.hide();
		this.deckPanelTop.showWidget(1);
		this.deckPanelBottom.showWidget(2);
	}
	
	public void showTagCloud() {
		channelLogo.hide();
		this.deckPanelTop.showWidget(1);
		this.deckPanelBottom.showWidget(1);
	}
	
	
	public void setYoutubePlayer(Widget youtubePlayer) {
		simplePanelPlayer.setWidget(youtubePlayer);
	}
	
}
