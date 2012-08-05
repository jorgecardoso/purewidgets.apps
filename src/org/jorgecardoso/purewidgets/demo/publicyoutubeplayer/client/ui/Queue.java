/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui;

import java.util.ArrayList;

import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Log;
import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.PublicYoutubePlayer;
import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Util;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.widgets.youtube.JsonVideoEntry;
import org.purewidgets.client.widgets.youtube.Video;
import org.purewidgets.client.widgets.youtube.VideoAdapter;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Queue extends PopupPanel {
	private static final int MAX_CHARS = 20;
	private static QueueUiBinder uiBinder = GWT
			.create(QueueUiBinder.class);

	interface QueueUiBinder extends UiBinder<Widget, Queue> {
	}

	@UiField
	VerticalPanel verticalPanelQueue;
	
	
	private ArrayList<Video> videoQueue;
	
	
	public Queue() {
		Log.debug(this, "Creating Queue");
		add(uiBinder.createAndBindUi(this));
		this.setAnimationEnabled(true);
		videoQueue = new ArrayList<Video>();
		this.loadQueueFromLocalStorage();
		this.createGuiQueue();
	}	
	
	public ArrayList<Video> getVideos() {
		return videoQueue;
	}
	
	
	/**
	 * 
	 */
	private void createGuiQueue() {
		this.verticalPanelQueue.clear();
		for ( Video video : this.videoQueue ) {
			int nChars = video.getTitle().length();
			if ( MAX_CHARS > nChars ) {
				this.verticalPanelQueue.add(new Label(video.getTitle() ));
			} else {
				this.verticalPanelQueue.add(new Label(video.getTitle().subSequence(0, MAX_CHARS-3) +"..." ));
			}
		}
	}
	
	
	public void addQueueEntry(Video v) {
		for ( Video video : this.videoQueue ) {
			if ( video.getId().equals(v.getId()) ) {
				return;
			}
		}
		this.videoQueue.add(v);
		this.createGuiQueue();
		
		this.saveQueueToLocalStorage();
	}
	
	public Video getNextVideoFromQueue() {
		if ( this.videoQueue.size() == 0 ) {
			return null;
		}
		Video video = this.videoQueue.remove(0);
		this.createGuiQueue();
		this.saveQueueToLocalStorage();
		return video;
	}
	
	
	private void saveQueueToLocalStorage() {
		ArrayList<String> videoSerialized = new ArrayList<String>();
		for ( Video video : this.videoQueue ) {
			videoSerialized.add(VideoAdapter.fromVideo(video).toJsonString());
		}
		Util.getPdApplication().getLocalStorage().saveList("ToPlayNext-Queue", videoSerialized);
	}
	
	private void loadQueueFromLocalStorage() {
		ArrayList<String> videosSerialized = Util.getPdApplication().getLocalStorage().loadList("ToPlayNext-Queue");
		for ( String videoSerialized : videosSerialized ) {
			JsonVideoEntry jsonVideo = JsonVideoEntry.fromJson(videoSerialized);
			this.videoQueue.add(VideoAdapter.fromJSONVideoEntry(jsonVideo));
		}
	}

}
