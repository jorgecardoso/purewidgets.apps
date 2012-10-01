package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.client.htmlwidgets.youtube.json.JsonVideoEntry;
import org.purewidgets.client.htmlwidgets.youtube.Video;
import org.purewidgets.client.htmlwidgets.youtube.VideoAdapter;

public class PlayHistory {
	private static final int MAX_VIDEOS = 150;
	
	ArrayList<Video> playedVideos;
	
	
	
	
	public PlayHistory() {
		this.playedVideos = new ArrayList<Video>();
		this.loadFromLocalStorage();
	}
	
	public ArrayList<Video> get() {
		return playedVideos;
	}
	
	public boolean contains(Video video) {
		
		for (Video playedVideo : this.playedVideos) {
			if (playedVideo.getId().equals(video.getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void add(Video v) {

		this.playedVideos.add(v);
		
		if ( MAX_VIDEOS < this.playedVideos.size() ) {
			this.playedVideos.remove(0);
		}

		this.saveToLocalStorage();
	}
	
	
	
	

	
	
	private void saveToLocalStorage() {
		ArrayList<String> videoSerialized = new ArrayList<String>();
		for ( Video video : this.playedVideos ) {
			videoSerialized.add(VideoAdapter.fromVideo(video).toJsonString());
		}
		Util.getPdApplication().getLocalStorage().saveList("PlayHistory-videos", videoSerialized);
	}
	
	private void loadFromLocalStorage() {
		ArrayList<String> videosSerialized = Util.getPdApplication().getLocalStorage().loadList("PlayHistory-videos");
		for ( String videoSerialized : videosSerialized ) {
			JsonVideoEntry jsonVideo = JsonVideoEntry.fromJson(videoSerialized);
			this.playedVideos.add( (VideoAdapter.fromJSONVideoEntry(jsonVideo)) );
		}
	}


}
