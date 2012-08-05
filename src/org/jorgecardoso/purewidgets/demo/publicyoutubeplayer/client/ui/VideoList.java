/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Log;
import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Util;
import org.purewidgets.client.widgets.youtube.JsonVideoEntry;
import org.purewidgets.client.widgets.youtube.Video;
import org.purewidgets.client.widgets.youtube.VideoAdapter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class VideoList extends Composite {
	interface Style extends CssResource {
		
	    String left();
	    String right();
	    String cell();
	
	  }
	
	@UiField Style style;
	
	
	private static VideoListUiBinder uiBinder = GWT
			.create(VideoListUiBinder.class);

	interface VideoListUiBinder extends UiBinder<Widget, VideoList> {
	}

	@UiField
	Label uiLabelTitle;
	
	@UiField
	Grid uiMainPanel;
	
	private VideoActionListener videoEventListener;
	
	private String visibleButtonLabel;

	private ArrayList<Video> videos;
	
	private ArrayList<VideoActionEntry> videoActionEntries;

	private String listId;

	private int maxEntries;


	private int cols;


	private int rows;


	private boolean createDownloadButton;


	private boolean createReportButton;
	
	@UiConstructor
	public VideoList(String listId, String title, String visibleButtonLabel,  boolean createDownloadButton, boolean createReportButton, int maxEntries, int cols, int rows) {
		Log.debugFinest(this, "Creating video list for id: " + listId);
		initWidget(uiBinder.createAndBindUi(this));
		this.listId = listId;
		this.uiLabelTitle.setText(title);
		this.maxEntries = maxEntries;
		this.cols = cols;
		this.rows = rows;
		this.visibleButtonLabel = visibleButtonLabel;
		this.createDownloadButton = createDownloadButton;
		this.createReportButton = createReportButton;
		
		videos = new ArrayList<Video>();
		videoActionEntries = new ArrayList<VideoActionEntry>();
		
		this.loadFromLocalStorage();
		
		for (Video v : this.videos) {
			VideoActionEntry vae = new VideoActionEntry(v, this.visibleButtonLabel, this.listId, this.createDownloadButton, this.createReportButton);
			this.videoActionEntries.add(vae);
		}
		
		/*
		 * Make sure we delete extra videos... 
		 */
		while ( this.videos.size() > this.maxEntries ) {
			Log.debugFinest(this, "VideoList " + this.listId + " had too many videos in localstorage. Deleting: " + this.videos.get(0).getId() );
			this.remove( this.videos.get(0) );
		}
		
		makeGui();

	}	

	
//	public int getEntryCount() {
//		return this.videos.size();
//	}
	
	public Video removeOldest() {
		Log.debugFinest(this, "Removing oldest video");
		
		Video removedVideo = null;
		if ( null != this.videos && this.videos.size() > 0 ) {
			removedVideo = this.videos.remove(0);
			for ( VideoActionEntry videoActionEntry : this.videoActionEntries ) {
				if ( removedVideo.getId().equals(videoActionEntry.getVideo().getId()) ) {
					this.videoActionEntries.remove(videoActionEntry);
					videoActionEntry.dispose();
					break;
				}
			}
		
		}
		
		makeGui();
		this.saveToLocalStorage();
		return removedVideo;
	}
	
	public void remove(Video v) {
		Log.debugFinest(this, "Removing video " + v.getId());
		for ( Video video : this.videos ) {
			if ( v.getId().equals(video.getId()) ) {
				this.videos.remove(video);
				break;
			}
		}
		
		
		for ( VideoActionEntry videoActionEntry : this.videoActionEntries ) {
			if ( v.getId().equals(videoActionEntry.getVideo().getId()) ) {
				
				this.videoActionEntries.remove(videoActionEntry);
				videoActionEntry.dispose();
				
				break;
			}
		}
		this.makeGui();
		this.saveToLocalStorage();
	}
	
	public void addEntry(Video v) {
		Log.debug(this, "Adding entry: " + v.getId());
		
		/*
		 * Create the VideoActionEntry
		 */
		VideoActionEntry rpe = new VideoActionEntry(v, this.visibleButtonLabel, this.listId, this.createDownloadButton, this.createReportButton);
		rpe.setVideoEventListener(this.videoEventListener);
		
		this.videos.add(v);

		this.videoActionEntries.add(rpe);
		
		
		while ( this.videos.size() > this.maxEntries ) {
			Log.debugFinest(this, "VideoList " + this.listId + " had too many videos in localstorage. Deleting: " + this.videos.get(0).getId() );
			this.remove( this.videos.get(0) );
		}
		this.saveToLocalStorage();
		makeGui();
		
		
	}

	public boolean isFull() {
		return this.videos.size()>= this.maxEntries;
	}



	/**
	 * 
	 */
	private void print() {
//		for (VideoActionEntry vae : this.videoEntries) {
//			Log.debug(this, vae.toString());
//		}
	}
	
	private void makeGui() {
		Log.debugFinest(this, "Creaing gui. " + this.videos.size() + " videos in list. " + this.videoActionEntries.size() + " video entries in list.");
		this.uiMainPanel.clear();
		int cols = this.cols;
		int rows = this.rows;
		
	
		this.uiMainPanel.resize(rows,cols);

		
		ListIterator<VideoActionEntry> it = this.videoActionEntries.listIterator(this.videoActionEntries.size());
		for ( int r = 0; r < rows; r++ ) {
			for ( int c = 0; c < cols; c++ ) {
				if (it.hasPrevious()) {
					VideoActionEntry vae = it.previous();
					if ( null != vae ) {
						this.uiMainPanel.setWidget(r,  c, vae);
						this.uiMainPanel.getCellFormatter().setStyleName(r, c, style.cell());
						if (c == 0 ) {
							this.uiMainPanel.getCellFormatter().addStyleName(r, c, style.left());
						} else if (c == cols-1) {
							this.uiMainPanel.getCellFormatter().addStyleName(r, c, style.right());
						} 
					}
				}
			}
		}

	}
	
	
	/**
	 * @return the videoEventListener
	 */
	public VideoActionListener getVideoEventListener() {
		return videoEventListener;
	}

	/**
	 * @param videoEventListener the videoEventListener to set
	 */
	public void setVideoEventListener(VideoActionListener videoEventListener) {
		this.videoEventListener = videoEventListener;
		/*
		 * Make sure every entry has our event listener
		 */
		Iterator<Widget> it = this.uiMainPanel.iterator();
		while( it.hasNext() ) {
			VideoActionEntry rpe = (VideoActionEntry) it.next();
			rpe.setVideoEventListener(videoEventListener);
		}
		
		
		for (VideoActionEntry vae : this.videoActionEntries ) {
			vae.setVideoEventListener(videoEventListener);
		}
		
	}
	

	
	private void saveToLocalStorage() {
		ArrayList<String> videoSerialized = new ArrayList<String>();
		for ( Video video : this.videos ) {
			videoSerialized.add(VideoAdapter.fromVideo(video).toJsonString());
		}
		Util.getPdApplication().getLocalStorage().saveList("VideoList-"+this.listId+"-videos", videoSerialized);
	}
	
	private void loadFromLocalStorage() {
		ArrayList<String> videosSerialized = Util.getPdApplication().getLocalStorage().loadList("VideoList-"+this.listId+"-videos");
		for ( String videoSerialized : videosSerialized ) {
			JsonVideoEntry jsonVideo = JsonVideoEntry.fromJson(videoSerialized);
			this.videos.add(VideoAdapter.fromJSONVideoEntry(jsonVideo));
		}
	}
}
