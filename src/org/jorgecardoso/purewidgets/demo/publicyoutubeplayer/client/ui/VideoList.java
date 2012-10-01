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
import org.purewidgets.client.htmlwidgets.youtube.json.JsonVideoEntry;
import org.purewidgets.client.htmlwidgets.youtube.Video;
import org.purewidgets.client.htmlwidgets.youtube.VideoAdapter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
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

	//private ArrayList<Video> videos;
	
	private ArrayList<VideoActionEntry> videoActionEntries;

	private String listId;

	private int maxEntries;


	private int cols;


	private int rows;


	private boolean createDownloadButton;


	private boolean createReportButton;
	
	private int maxOrder = 0;
	
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
		
		//videos = new ArrayList<Video>();
		videoActionEntries = new ArrayList<VideoActionEntry>();
		
		this.loadFromLocalStorage();
		
//		for (Video v : this.videos) {
//			VideoActionEntry vae = new VideoActionEntry(v, this.visibleButtonLabel, this.listId, this.createDownloadButton, this.createReportButton);
//			this.videoActionEntries.add(vae);
//		}
		
		/*
		 * Make sure we delete extra videos... 
		 */
		while ( this.videoActionEntries.size() > this.maxEntries ) {
			Log.debugFinest(this, "VideoList " + this.listId + " had too many videos in localstorage. Deleting: " + this.videoActionEntries.get(0).getVideo().getId() );
			this.remove( this.videoActionEntries.get(0).getVideo() );
		}
		
		makeGui();

	}	

	
//	public int getEntryCount() {
//		return this.videos.size();
//	}
	
	public Video removeOldest() {
		Log.debugFinest(this, "Removing oldest video");
		
		VideoActionEntry removedVideo = null;
		if ( null != this.videoActionEntries && this.videoActionEntries.size() > 0 ) {
			removedVideo = this.videoActionEntries.remove(0);
			removedVideo.dispose();
//			for ( VideoActionEntry videoActionEntry : this.videoActionEntries ) {
//				if ( removedVideo.getId().equals(videoActionEntry.getVideo().getId()) ) {
//					this.videoActionEntries.remove(videoActionEntry);
//					videoActionEntry.dispose();
//					break;
//				}
//			}
		
		}
		
		makeGui();
		this.saveToLocalStorage();
		return removedVideo.getVideo();
	}
	
	public void remove(Video v) {
		Log.debugFinest(this, "Removing video " + v.getId());
		for ( VideoActionEntry vae : this.videoActionEntries ) {
			if ( v.getId().equals(vae.getVideo().getId()) ) {
				this.videoActionEntries.remove(vae);
				vae.dispose();
				break;
			}
		}
		
		
//		for ( VideoActionEntry videoActionEntry : this.videoActionEntries ) {
//			if ( v.getId().equals(videoActionEntry.getVideo().getId()) ) {
//				
//				this.videoActionEntries.remove(videoActionEntry);
//				videoActionEntry.dispose();
//				
//				break;
//			}
//		}
		this.makeGui();
		this.saveToLocalStorage();
	}
	
	public void addEntry(Video v) {
		Log.debug(this, "Adding entry: " + v.getId());
		
		/*
		 * Create the VideoActionEntry
		 */
		this.maxOrder++;
		VideoActionEntry rpe = new VideoActionEntry(v, maxOrder, this.visibleButtonLabel, this.listId, this.createDownloadButton, this.createReportButton);
		rpe.setVideoEventListener(this.videoEventListener);
		
		//this.videos.add(v);

		this.videoActionEntries.add(rpe);
		
		
		while ( this.videoActionEntries.size() > this.maxEntries ) {
			Log.debugFinest(this, "VideoList " + this.listId + " had too many videos in localstorage. Deleting: " + this.videoActionEntries.get(0).getVideo().getId() );
			this.remove( this.videoActionEntries.get(0).getVideo() );
		}
		this.saveToLocalStorage();
		makeGui();
		
		
	}

	public boolean isFull() {
		return this.videoActionEntries.size()>= this.maxEntries;
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
		Log.debugFinest(this, "Creaing gui. " + this.videoActionEntries.size() + " video entries in list.");
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
		for ( VideoActionEntry vae : this.videoActionEntries ) {
			JsonVideoEntry jsonVideo = VideoAdapter.fromVideo(vae.getVideo());
			JSONObject jsonOrderedVideo = new JSONObject();
			jsonOrderedVideo.put("video", new JSONString(jsonVideo.toJsonString()));
			jsonOrderedVideo.put("order", new JSONNumber(vae.getOrder()));
			videoSerialized.add( jsonOrderedVideo.toString() );
		}
		Util.getPdApplication().getLocalStorage().saveList("VideoList-"+this.listId+"-videos", videoSerialized);
	}
	
	private void loadFromLocalStorage() {
		ArrayList<String> videosSerialized = Util.getPdApplication().getLocalStorage().loadList("VideoList-"+this.listId+"-videos");
		for ( String videoSerialized : videosSerialized ) {
			JSONValue jsonOrderedVideoValue = JSONParser.parseStrict(videoSerialized);
			JSONObject jsonOrderedVideo = jsonOrderedVideoValue.isObject();
			
			try {
				JSONString jsonVideoString = jsonOrderedVideo.get("video").isString();
				JSONNumber jsonOrderNumber = jsonOrderedVideo.get("order").isNumber();
				
				JsonVideoEntry jsonVideo = JsonVideoEntry.fromJson(jsonVideoString.stringValue());
				Video video = VideoAdapter.fromJSONVideoEntry(jsonVideo);
				int order = (int)jsonOrderNumber.doubleValue();
				
				VideoActionEntry vae = new VideoActionEntry(video, order, this.visibleButtonLabel, this.listId, this.createDownloadButton, this.createReportButton);
				this.videoActionEntries.add(vae);
				
				if ( order > this.maxOrder ) {
					this.maxOrder = order;
				}
				
			} catch (NullPointerException npe) {
				Log.warn(this, "Could not load video list from localstorage", npe);
			}
			
		}
	}
}
