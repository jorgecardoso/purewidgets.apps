package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.client.widgets.PdTagCloud;
import org.purewidgets.client.widgets.youtube.JsonVideoEntry;
import org.purewidgets.client.widgets.youtube.Video;
import org.purewidgets.client.widgets.youtube.VideoAdapter;
import org.purewidgets.client.widgets.youtube.VideoFeed;
import org.purewidgets.shared.widgets.TagCloud;



import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class VideoSearcher {
	private static final int USER_VIDEO_UPDATE_INTERVAL = 1000*60*60*24; // in milliseconds (daily)
	/*
	 * If the video count reaches this value, we trigger a new search.
	 */
	private static final int MINIMUM_VIDEO_COUNT = 5;
	
	public static final String YOUTUBE_SEARCH_PARAMETERS[] = {"max-results", "orderby", "location", "location-radius", "safeSearch"};
	
	private String youtubeSearchParameters ="";
	
	
	private ArrayList<Video> videoList;
	
	private ArrayList<String> users;
	private HashMap<String, ArrayList<Video>> userVideos;
	
	private PdTagCloud tagCloud;
	
	private String lastSearchTag;

	private int maxVideoDuration;

	private PlayHistory playHistory;
	
	private Timer timer;
	
	private float userVideoPercentage = 0.5f;
	
	private int maxFeaturedVideoDuration;
	private int maxUserVideos;
	private ArrayList<String> blackList;
	
	public VideoSearcher(int maxVideoDuration, String[] featuredUsers, float userVideoPercentage, int maxUserVideos, int maxFeaturedVideoDuration, final PlayHistory playHistory, PdTagCloud tagCloud) {
		this.maxVideoDuration = maxVideoDuration;
		this.maxFeaturedVideoDuration = maxFeaturedVideoDuration;
		this.userVideoPercentage = userVideoPercentage;
		this.maxUserVideos = maxUserVideos;
		this.playHistory = playHistory;
		this.tagCloud = tagCloud;
		this.users = new ArrayList<String>(Arrays.asList(featuredUsers));
		this.userVideos = new HashMap<String, ArrayList<Video>>();
		this.loadUserVideosFromLocalStorage();
		
		this.videoList = new ArrayList<Video>();
		loadFromLocalStorage();
		
		this.blackList = new ArrayList<String>();
		this.loadBlackListFromServerStorage();

		
		for ( int i = 0; i < YOUTUBE_SEARCH_PARAMETERS.length; i++ ) {
			String parameter = Util.getPdApplication().getParameterString("youtube-"+YOUTUBE_SEARCH_PARAMETERS[i], "");
			if ( !parameter.equals("") ) {
				this.youtubeSearchParameters += YOUTUBE_SEARCH_PARAMETERS[i]+"="+parameter+"&";
			}
		}
		
		
		if ( MINIMUM_VIDEO_COUNT >= videoList.size() ) {
			searchVideos();
		}
		Long lastUpdate = Util.getPdApplication().getLocalStorage().getLong("VideoSearcher-lastuservideosupdate");
		long currentTime = System.currentTimeMillis();
		if ( null == lastUpdate || currentTime-lastUpdate > USER_VIDEO_UPDATE_INTERVAL ) {
			Log.debugFinest(this, "Updating user videos");
			Util.getPdApplication().getLocalStorage().setString("VideoSearcher-lastuservideosupdate", currentTime+"");
			this.loadUserVideos();
		}
	}
	
	private void loadBlackListFromServerStorage() {
		String blackListJson = Util.getPdApplication().getParameterString("videoBlackList","[]");
		if ( blackListJson.length() < 2 ) {
			blackListJson = "[]";
		}
		JsArrayString jsArrayBlackList = GenericJson.fromJson(blackListJson);
		
		for ( int i = 0; i < jsArrayBlackList.length(); i++ ) {
			this.blackList.add(jsArrayBlackList.get(i));
		}
	}
	
	private void saveBlackListToServerStorage() {
		Log.debug(this, "Saving black list to server storage");
		JsArrayString jsArrayBlackList = (JsArrayString) JsArrayString.createArray();
		
		for ( String s : this.blackList ) {
			jsArrayBlackList.push(s);
		}
		Util.getPdApplication().setParameterString("videoBlackList", GenericJson.toJsonString(jsArrayBlackList) );
	}

	//http://gdata.youtube.com/feeds/api/users/jorg3cardoso/uploads
	private void loadUserVideos() {
		for ( final String user : this.users ) {
			String url = "http://gdata.youtube.com/feeds/api/users/" + user + "/uploads?max-results="+this.maxUserVideos+"&orderby=published&alt=jsonc&v=2&key=AI39si4VBKUt9aEEwH9balCm6H5Dqf8iAIUemNuFQRmjpWaP8c2XV3tCZLcidL6oW6cdl-84IP5nk2Lsofp1H9Jhj7zgKfAZqg";
			Log.info(this, "Searching for user videos: " + url);
			new VideoFeed(url,
					new AsyncCallback<VideoFeed>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Log.warn(VideoSearcher.this, "Could not fetch videos...");
						}
	
						@Override
						public void onSuccess(VideoFeed result) {
							Log.debug(VideoSearcher.this, "User video search complete. " + result);
							
							VideoSearcher.this.userVideos.put(user, new ArrayList<Video>(result.getVideos()) );
							VideoSearcher.this.filterUserVideos(VideoSearcher.this.userVideos.get(user));
						}
						
					});		
		}
	}
	
	private void searchVideos() {
		this.lastSearchTag = getSearchTag();
		
		/*
		 * We search a random index, although this is not the best approach because it can 
		 * return empty sets... but this way we don't need to know how many results there are
		 * beforehand.
		 */
		int startIndex = (int)(Math.random()*10);
		
		/*
		 * Calculate the duration parameter, according to the maxvideoduration parameter.
		 * see: https://google-developers.appspot.com/youtube/2.0/reference#durationsp
		 */
		String duration = "";
		if ( this.maxVideoDuration < 4*60 ) {
			duration = "short";
		} else if ( this.maxVideoDuration <= 20*60 ) {
			duration = "medium";
		} else {
			duration = "long";
		}
		String url = "http://gdata.youtube.com/feeds/api/videos?duration=" + duration +"&start-index="+ startIndex +"&"+ this.youtubeSearchParameters + "v=2&alt=jsonc&q=" + lastSearchTag +"&key=AI39si4VBKUt9aEEwH9balCm6H5Dqf8iAIUemNuFQRmjpWaP8c2XV3tCZLcidL6oW6cdl-84IP5nk2Lsofp1H9Jhj7zgKfAZqg";
		Log.info(this, "Searching for videos: " + url);
		new VideoFeed(url,
				new AsyncCallback<VideoFeed>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(VideoSearcher.this, "Could not fetch videos...");
					}

					@Override
					public void onSuccess(VideoFeed result) {
						Log.debug(VideoSearcher.this, "Video search complete. " + result);
						VideoSearcher.this.filterVideos(result);
						VideoSearcher.this.saveUserVideosToLocalStorage();
					}
					
				});		
	}
	

	public void filterUserVideos(ArrayList<Video> videos) {
		/*
		 * Filter the blacklist
		 */
		
		Iterator<Video> iterator = videos.iterator();
		while ( iterator.hasNext() ) {
			Video video = iterator.next();
			if ( this.blackList.contains(video.getId()) ) {
				Log.debugFinest(this,"Video in blacklist, ignoring: " + video.getId());
				iterator.remove();
			}
		}
		
		/*
		 * Filter videos according to length
		 */
		Iterator<Video> it = videos.iterator();
	
		while ( it.hasNext() ) {
			Video video = it.next();
			if ( video.getDuration() > this.maxFeaturedVideoDuration ) {
				it.remove();
				Log.debugFinest(this,"Video length "+ video.getDuration() + ", higher than allowed, ignoring: " + video.getId());
			}
		}
		
		Log.debug(this, "Removed lengthy videos. Have " + videos.size() + " videos left.");
		
	}
	
	public void filterVideos(VideoFeed feed) {
		
		List<Video> videos = feed.getVideos();
		Log.debug(this, "Got " + videos.size() + " search results.");
		
		/*
		 * Filter the blacklist
		 */
		
		Iterator<Video> iterator = videos.iterator();
		while ( iterator.hasNext() ) {
			Video video = iterator.next();
			if ( this.blackList.contains(video.getId()) ) {
				Log.debugFinest(this,"Video in blacklist, ignoring: " + video.getId());
				iterator.remove();
			}
		}
		
		
		/*
		 * Filter the ones we already have
		 */
		ArrayList<Video> filtered = new ArrayList<Video>();
		for (Video video : videos) {
			
			boolean exists = false;
			for (Video inListVideo : this.videoList) {
				if (inListVideo.getId().equals(video.getId())) {
					exists = true;
					Log.debugFinest(this,"Video already in search list, ignoring: " + inListVideo.getId());
				}
			}
			if ( !exists ) {
				filtered.add(video);
			}
		}
		Log.debug(this, "Removed videos already in list. Have " + filtered.size() + " videos left.");
		
		/*
		 * Filter videos according to length
		 */
		ArrayList<Video> filteredByLength = new ArrayList<Video>();
		for ( Video video : filtered ) {
			if (video.getDuration() <= this.maxVideoDuration ) {
				filteredByLength.add(video);
			} else {
				Log.debugFinest(this,"Video length "+ video.getDuration() + ", higher than allowed, ignoring: " + video.getId());
			}
		}
		Log.debug(this, "Removed lengthy videos. Have " + filteredByLength.size() + " videos left.");
		
		/*
		 * Filter the videos according to whether they have been played or not
		 */
		ArrayList<Video> filteredByLengthAndPlayed = new ArrayList<Video>();
		for (Video video : filteredByLength) {
			if ( !this.playHistory.contains( video ) ) {
				filteredByLengthAndPlayed.add(video);
			} else {
				Log.debugFinest(this,"Video already played, ignoring: " + video.getId());
			}
		}
		Log.debug(this, "Removed played videos. Have " + filteredByLengthAndPlayed.size() + " videos left.");
		
		
		/*
		 * Fill in the originating tags property 
		 */
		for ( Video video : filteredByLengthAndPlayed ) {
			video.setOriginatingTags(this.lastSearchTag);
		}
		
		
		/*
		 * Add the filtered videos to our database
		 */
		this.videoList.addAll(filteredByLengthAndPlayed);
		
		
		/*
		 * Save to localStorage
		 */
		this.saveToLocalStorage();
		
		if ( MINIMUM_VIDEO_COUNT > this.videoList.size() ) {
			this.timer = new Timer() {

				@Override
				public void run() {
					searchVideos();
				}
				
			};
			this.timer.schedule(15000);
			
		}
	
	}
	
	/**
	 * Randomly chooses a tag from the tag cloud, giving more weight to more frequent tags.
	 * @return
	 */
	private String getSearchTag() {
		
		ArrayList<TagCloud.Tag> tags = this.tagCloud.getTagListSortedByFrequency();
		
		/*
		 * sum the normalized frequencies
		 */
		float frequencySum = 0;
		for ( TagCloud.Tag tag : tags ) {
			frequencySum += tag.getNormalizedFrequency();
		}
		
		
		double sum = 0;
		double r = Math.random();
		for ( TagCloud.Tag tag : tags ) {
			
			/*
			 * The normalized frequency is between 1 and 0, but we want the sum of all frequencies to add up to 1
			 * so we now divide the normalized tag frequency by the sum of all tag frequencies.
			 */
			sum += tag.getNormalizedFrequency()/frequencySum;
			
			if ( r < sum ) {
				return tag.getWord();
			}
		}
		/*
		 * We must have something. If the admin didn't set a place tags list just use the word 'cat'
		 */
		if ( tags.size() > 0 ) {
			return tags.get(tags.size()-1).getWord();
		} else {
			return "cat";
		}
	}



	public List<Video> getVideo(int n) {
		
		Log.debug(this, "Returning " + n + " videos from search list");
		List<Video> list = new ArrayList<Video>();
		
		
		int currentExternalVideos = this.videoList.size();
		int currentUserVideos = 0;
		for ( String user : this.userVideos.keySet() ) {
			currentUserVideos += this.userVideos.get(user).size();
		}
		
		int toDeliver = Math.min(n, currentExternalVideos+currentUserVideos);
		
		while ( toDeliver > 0 ) {
			double random = Math.random();
			if ( random < this.userVideoPercentage ) { // try to deliver a user video
				if ( currentUserVideos > 0 ) {
					list.add(getUserVideo());
				} else {
					list.add(getExternalVideo());
				}
			} else { // try to deliver an external video
				if ( this.videoList.size() > 0 ) {
					list.add(getExternalVideo());
				} else {
					list.add(getUserVideo());
				}
			}
			toDeliver--;
			
		}
		
		this.saveUserVideosToLocalStorage();
		this.saveToLocalStorage();
		return list;
		
	}
	
	
	private Video getUserVideo() {
		Log.debugFinest(this, "Delivering user video");
		// random user
		String user = this.users.get((int)Math.floor(Math.random()*this.users.size()));
		ArrayList<Video> userVideos = this.userVideos.get(user);
		
		if ( userVideos.size() > 0 ) {
			/*
			 * remove from the top and add to the end
			 */
			Video video = userVideos.remove(0);
			userVideos.add(video);
			return video;
		} else { // try sequentially
			for ( ArrayList<Video> videos : this.userVideos.values() ) {
				if ( videos.size() > 0 ) {
					Video video = videos.remove(0);
					videos.add(video);
					return video;
				}
			}
			
		}
		return null;
	}
	
	private Video getExternalVideo() {
		Log.debugFinest(this, "Delivering external video");
		int randomIndex =(int)(Math.random()*this.videoList.size()-1);
		return this.videoList.remove(randomIndex);
	}
	
	private void saveUserVideosToLocalStorage() {
		for ( String user : this.users ) {
			this.saveToLocalStorage("VideoSearcher-userVideos-"+user, this.userVideos.get(user));
		}
	}
	
	private void loadUserVideosFromLocalStorage() {
		for ( String user : this.users ) {
			this.userVideos.put(user, this.loadFromLocalStorage("VideoSearcher-userVideos-"+user) );
		}
	}
	
	private void saveToLocalStorage() {
		this.saveToLocalStorage("VideoSearcher-videos", this.videoList);
	}
	
	private void saveToLocalStorage(String storageName, ArrayList<Video> videoList) {
		ArrayList<String> videoSerialized = new ArrayList<String>();
		for ( Video video : videoList ) {
			videoSerialized.add(VideoAdapter.fromVideo(video).toJsonString());
		}
		Util.getPdApplication().getLocalStorage().saveList(storageName, videoSerialized);
	}
	
	private void loadFromLocalStorage() {
		this.videoList = this.loadFromLocalStorage("VideoSearcher-videos");
	}

	private ArrayList<Video> loadFromLocalStorage(String storageName) {
		ArrayList<String> videosSerialized = Util.getPdApplication().getLocalStorage().loadList(storageName);
		ArrayList<Video> videoList = new ArrayList<Video>();
		for ( String videoSerialized : videosSerialized ) {
			JsonVideoEntry jsonVideo = JsonVideoEntry.fromJson(videoSerialized);
			videoList.add(VideoAdapter.fromJSONVideoEntry(jsonVideo));
		}
		return videoList;
	}

	/**
	 * @return the tagCloud
	 */
	public PdTagCloud getTagCloud() {
		return tagCloud;
	}



	/**
	 * @param tagCloud the tagCloud to set
	 */
	public void setTagCloud(PdTagCloud tagCloud) {
		this.tagCloud = tagCloud;
	}

	public void addToBlackList(String id) {
		Log.debug(this, "Adding to black list: " + id);
		this.blackList.add(id);
		this.saveBlackListToServerStorage();
	}


}
