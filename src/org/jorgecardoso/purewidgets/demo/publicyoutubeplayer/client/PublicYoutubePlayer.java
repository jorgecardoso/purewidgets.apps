package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui.VideoActionListener;
import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui.VideoScreen;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.client.widgets.youtube.EmbeddedPlayer;
import org.purewidgets.client.widgets.youtube.PlayerError;
import org.purewidgets.client.widgets.youtube.PlayerListener;
import org.purewidgets.client.widgets.youtube.PlayerState;
import org.purewidgets.client.widgets.youtube.VideoFeed;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.application.PDApplicationLifeCycle;
import org.purewidgets.client.feedback.FeedbackSequencer;


import org.purewidgets.client.widgets.PdWidget;
import org.purewidgets.client.widgets.youtube.Video;

import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;
import org.purewidgets.shared.widgets.TagCloud;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PublicYoutubePlayer implements PDApplicationLifeCycle, EntryPoint, VideoActionListener, PlayerListener{

	
	private static  enum State {PLAYING, ACTIVITY, TAGCLOUD, NEXT};//, HIGLIGHT};
	
	/**
	 * The Url parameter name for setting the maximum video duration
	 */
	public static final String URL_PARAMETER_MAX_VIDEO_DURATION = "maxvideotime";
	
	/**
	 * The Url parameter name for setting the maximum video duration
	 */
	public static final String URL_PARAMETER_MAX_FEATURED_VIDEO_DURATION = "maxfeaturedvideotime";
	
	/**
	 * The default maximum video duration allowed.
	 */
	private static final int DEFAULT_MAX_VIDEO_DURATION = 10*60; // seconds
	
	/**
	 * The Url parameter name for setting the duration of the 'Activity' screen
	 */
	public static final String URL_PARAMETER_ACTIVITY_SCREEN_DURATION = "activityscreentime";
	
	/**
	 * The default value for the duration of the 'Activity' screen
	 */
	private static int DEFAULT_ACTIVITY_SCREEN_DURATION	=	10; // seconds
	
	/**
	 * The Url parameter name for setting the duration of the 'To play next' screen
	 */
	public static final String URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION = "toplaynextscreentime";
	
	/**
	 * The default value for the duration of the 'To play next' screen
	 */
	private static int DEFAULT_TOPLAYNEXT_SCREEN_DURATION = 60; // seconds	
	
	/**
	 * The Url parameter name for setting the duration of the 'Tagcloud' screen
	 */
	public static final String URL_PARAMETER_TAGCLOUD_SCREEN_DURATION = "tagcloudscreentime";
	
	/**
	 * The default value for the duration of the 'To play next' screen
	 */
	private static int DEFAULT_TAGCLOUD_SCREEN_DURATION = 10; // seconds
	
	/**
	 * The Url parameter name for setting whether users can suggest tags
	 */
	public static final String URL_PARAMETER_ALLOW_USER_TAGS = "allowusertags";
	
	/**
	 * The Url parameter name for the place tags
	 */
	public static final String URL_PARAMETER_PLACE_TAGS = "placetags";
	
	/**
	 * The Url parameter name featured youtube users
	 */
	public static final String URL_PARAMETER_FEATURED_USERS = "featuredusers";	
	
	public static final String URL_PARAMETER_FEATURED_VIDEOS_PERCENTAGE = "featurevideopercentage";
	
	public static final String URL_PARAMETER_MAX_FEATURED_VIDEOS = "maxfeaturedvideos";
	
	
	public static final String URL_PARAMETER_RESET = "reset";
	
	
	/**
	 * The duration of the 'Activity' screen
	 */
	private int activityScreenDurationParameter;
	
	/**
	 * The duration of the 'To play next' screen
	 */
	private int toPlayNextScreenDurationParameter;
	
	/**
	 * The duration of the 'To play next' screen
	 */
	private int tagCloudScreenDurationParameter;	
	
	/**
	 * The maximum duration of a video
	 */
	private int maxVideoDurationParameter;
	
	/**
	 * Whether users can suggest tags, or not.
	 */
	private boolean allowUserTagsParameter;
	
	
	/**
	 * The user defined place tags
	 */
	private String placeTagsParameter;
	
	private boolean resetParameter;
	
	
	/**
	 * The duration of the confirmation screen before starting to play the video
	 */
	//private int toPlayNextConfirmationDuration;
	
	/**
	 * If a video stalls for some reason we wait this period of time before searching another one.
	 */
	private static final int STALLED_WAIT_PERIOD = 20*1000; //miliseconds


	private static final int MAX_TAG_CLOUD_SIZE = 25;


	
	
	ArrayList<String> recentlyPlayedVideos;
	
	
	
	EmbeddedPlayer youtube;
	
	ArrayList<String> placeTags;
	
	
	VideoScreen screen;
	
	Video toPlay;

	Timer stalledTimer;
	
	

	Timer stateTimer;
	
	State currentState;

	private PDApplication pdApplication;
	
	private VideoSearcher videoSearcher;
	
	private PlayHistory playHistory;

	private Timer queueTimer;
	
	
	@Override
	public void onError(PlayerError state) {
		Log.debug(this, "Player Error: " + state.name());
		
		if ( this.currentState == State.PLAYING ) {
			// If we get an error during playback, set the stalled timer so that the app
			// does not get stuck.
			this.stalledTimer.schedule(3000);
		}
		//this.searchVideos();
	}
	
	@Override
	public void onLoading(int percent) {
	}

	@Override
	public void onPDApplicationLoaded(PDApplication app) {
		this.pdApplication = app;
		Util.setPdApplication(app);
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			(new Admin()).run(app);
			return;
		}
		//Logger.getLogger("PuReWidgets").setLevel(Level.OFF);
		//org.purewidgets.shared.logging.Log.setLevel(Level.WARNING);
		
		/*
		 * Read the application parameters
		 * These can be set in the URL or via the admin console
		 */
		this.maxVideoDurationParameter = app.getParameterInt(URL_PARAMETER_MAX_VIDEO_DURATION, DEFAULT_MAX_VIDEO_DURATION);
		Log.info(this, "Loaded '" + URL_PARAMETER_MAX_VIDEO_DURATION + "' parameter with value: " + this.maxVideoDurationParameter);
		
		int maxFeaturedVideoDurationParameter = app.getParameterInt(URL_PARAMETER_MAX_FEATURED_VIDEO_DURATION, DEFAULT_MAX_VIDEO_DURATION);
		Log.info(this, "Loaded '" + URL_PARAMETER_MAX_FEATURED_VIDEO_DURATION + "' parameter with value: " + maxFeaturedVideoDurationParameter);
		
		
		this.activityScreenDurationParameter = app.getParameterInt(URL_PARAMETER_ACTIVITY_SCREEN_DURATION, DEFAULT_ACTIVITY_SCREEN_DURATION);
		Log.info(this, "Loaded '" + URL_PARAMETER_ACTIVITY_SCREEN_DURATION + "' parameter with value: " + this.activityScreenDurationParameter);
		
		this.toPlayNextScreenDurationParameter = app.getParameterInt(URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION, DEFAULT_TOPLAYNEXT_SCREEN_DURATION);
		Log.info(this, "Loaded '" + URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION + "' parameter with value: " + this.toPlayNextScreenDurationParameter);
	
		this.tagCloudScreenDurationParameter = app.getParameterInt(URL_PARAMETER_TAGCLOUD_SCREEN_DURATION, DEFAULT_TAGCLOUD_SCREEN_DURATION);
		Log.info(this, "Loaded '" + URL_PARAMETER_TAGCLOUD_SCREEN_DURATION + "' parameter with value: " + this.tagCloudScreenDurationParameter);
		
		
		this.allowUserTagsParameter =  app.getParameterBoolean(URL_PARAMETER_ALLOW_USER_TAGS, false); //com.google.gwt.user.client.Window.Location.getParameter(URL_PARAMETER_ALLOW_USER_TAGS);
		Log.info(this, "Loaded '" + URL_PARAMETER_ALLOW_USER_TAGS + "' parameter with value: " + this.allowUserTagsParameter);
		
		this.placeTagsParameter = app.getParameterString(URL_PARAMETER_PLACE_TAGS, "public,youtube,player");
		Log.info(this, "Loaded '" + URL_PARAMETER_PLACE_TAGS + "' parameter with value: " + this.placeTagsParameter);
	

		String featuredUsers = app.getParameterString(URL_PARAMETER_FEATURED_USERS, "");
		Log.info(this, "Loaded '" + URL_PARAMETER_FEATURED_USERS + "' parameter with value: " + featuredUsers);
	
		
		this.resetParameter = app.getParameterBoolean(URL_PARAMETER_RESET, false);
		Log.info(this, "Loaded '" + URL_PARAMETER_RESET + "' parameter with value: " + this.resetParameter);
		
		float featuredVideoPercentage = app.getParameterFloat(URL_PARAMETER_FEATURED_VIDEOS_PERCENTAGE, 0.5f);
		Log.info(this, "Loaded '" + URL_PARAMETER_FEATURED_VIDEOS_PERCENTAGE + "' parameter with value: " + featuredVideoPercentage);
		
		int maxFeaturedVideos = app.getParameterInt(URL_PARAMETER_MAX_FEATURED_VIDEOS, 10);
		Log.info(this, "Loaded '" + URL_PARAMETER_MAX_FEATURED_VIDEOS + "' parameter with value: " + maxFeaturedVideos);
	
		
		app.setParameterString(URL_PARAMETER_RESET, "false");
		
		if ( this.resetParameter ) {
			/*
			 * clear remote widgets
			 */
			Log.warn(this,"Reset parameter set to true. Deleting all widgets on server!");
			this.pdApplication.getInteractionManager().deleteAllWidgets(this.pdApplication.getPlaceId(), this.pdApplication.getApplicationId(), this.pdApplication.getApplicationId(), null);
		}
		
		screen = new VideoScreen();
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
		RootPanel.get().add(screen);
		
		this.screen.showActivity();
		
		stalledTimer = new Timer() {
			@Override
			public void run() {
				timerStalledElapsed();
			}
		};
		
		stateTimer = new Timer() {
			@Override
			public void run() {
				timerStateElapsed();
			}
		};
	    queueTimer = new Timer() {

			@Override
			public void run() {
				PublicYoutubePlayer.this.screen.queue.hide();
			}
		};
		
		
		
		this.initGui();


		this.playHistory = new PlayHistory();
		this.videoSearcher = new VideoSearcher(this.maxVideoDurationParameter, featuredUsers.split(","), featuredVideoPercentage, maxFeaturedVideos, maxFeaturedVideoDurationParameter, this.playHistory, this.screen.tagCloud);
		
		
		this.gotoState(State.ACTIVITY);
	}
	
	
	private void initGui() {
		Log.debug(this, "initGui");

		this.screen.recentlyPlayed.setVideoEventListener(this);
		this.screen.toPlayNext.setVideoEventListener(this);
		
		youtube = new EmbeddedPlayer("youtubeplayer", "100%", "100%", false, this);
		//RootPanel.get("youtube").add(youtube);
		screen.setYoutubePlayer(youtube);
		
		this.loadTagCloud();
	}

	
	@Override
	public void onModuleLoad() {
		Logger.getLogger("").setLevel(Level.ALL);
		Logger.getLogger("PuReWidgets").setLevel(Level.FINE);
		Log.get().setLevel(Level.ALL);
		
		PDApplication.load(this, "PublicYoutubePlayer");
	}
	
	
	@Override
	public void onReady() {
		Log.debug(this, "Youtube player ready");
		
		if ( this.toPlay != null ) {
				this.youtube.loadVideoById(this.toPlay.getId());
				this.youtube.play();
		}
		
	}



	@Override
	public void onStateChange(PlayerState state) {
		Log.debug(this, "Player State: " +  state.name());
		
		if ( state == PlayerState.PLAYING ) {
			stalledTimer.cancel();
		} else {
			stalledTimer.cancel();
			stalledTimer.schedule(STALLED_WAIT_PERIOD);
		}
		
		if ( state == PlayerState.ENDED ) {
			this.videoEnded();	
		} 
	}


	
	@Override
	public void onVideoAction(ActionEvent<?> e, Video video, String action) {
		Log.warn(this, "User Clicked youtube" + video);
			//label.setText(label.getText() + e.toDebugString());
			
			//String videoId = source.getWidgetId().substring("btn_like_".length());
			
			//Video video = this.allPlayedVideos.get(videoId);
			
		if ( null != video ) {
			if ( action.equalsIgnoreCase("like") ) {
				Log.debug(this, e.getNickname() + " liked video " + video.getId() );
				
				this.updateTagCloud( video );
			} else if ( action.equalsIgnoreCase("play") ) {
				Log.debug(this, e.getNickname() + " wants to play " + video.getId());
				
				this.screen.queue.addQueueEntry(video);
				
				if ( !this.screen.queue.isShowing() ) {
					this.screen.queue.center();
					this.screen.queue.show();
					this.queueTimer.schedule(FeedbackSequencer.DEFAULT_FEEDBACK_DURATION+FeedbackSequencer.DEFAULT_FINAL_DELAY);
				}
				
			} else if ( action.equalsIgnoreCase("report") ) {
				Log.debug(this, "User reported video: " + video.getId() );
				this.videoSearcher.addToBlackList(video.getId());
			}
		}
			
		
	}



	/**
	 * 
	 */
	private void decreaseTagCloudFrequency() {
		ArrayList<TagCloud.Tag> tags = this.screen.tagCloud.getTagList();
		
		
		for ( TagCloud.Tag tag : tags ) {
			
			int freq = tag.getFrequency();
			
			freq = (int)(freq*0.9);
			if ( freq < 1 ) {
				freq = 1;
			}
			
			tag.setFrequency(freq);
			
		}
		
		this.screen.tagCloud.setTagList(tags);
		this.screen.tagCloud.updateGui();
		this.saveTagCloud();
	}

	

	private void gotoState( State state ) {
		if ( State.PLAYING == state ) { // video
			
			/*
			 * Remove videos that were put on queue from the toplaynext list
			 */
			for ( Video video : this.screen.queue.getVideos() ) {
				Log.debug(this, "Removing video from toPlayNext list: " + video.getId());
				this.screen.toPlayNext.remove(video);
			}
			
			
			/*
			 * Get a new video to play from the queue, or if the queue is empty, get it from the toplaynext list
			 */
			Video topFromQueue = this.screen.queue.getNextVideoFromQueue();
			if ( null == topFromQueue ) {
				topFromQueue = this.screen.toPlayNext.removeOldest();
				
			} 
			this.toPlay = topFromQueue;
			
			if ( null == this.toPlay ) {
				this.gotoState(State.ACTIVITY);
				return;
			}
			
			Log.info(this, "Going to Play state (video: " + this.toPlay.getTitle() + ")");
			this.currentState = State.PLAYING;
			this.screen.showVideo();
	
			/*
			 * Update the play history, recentlyplayed list and tagcloud
			 */
			if ( null != this.toPlay ) {
				
				this.playHistory.add( this.toPlay );
				
				this.screen.recentlyPlayed.addEntry(this.toPlay);
			
				this.updateTagCloud(this.toPlay);
			}

			
		} else if ( State.ACTIVITY == state ) { // recently played
			Log.info(this, "Going to Activity state");
			this.currentState = State.ACTIVITY;
			
			this.screen.showActivity();
			stateTimer.schedule(this.activityScreenDurationParameter*1000);
			
			
		} else if ( State.NEXT == state ) { // to play next
			try {
				Log.info(this, "Going to Next state");
				this.currentState = State.NEXT;
				
				/*
				 * Get videos from the search results and put them in the gui list
				 */
				while ( !this.screen.toPlayNext.isFull() ) {
					List<Video> videos = this.videoSearcher.getVideo(1);
					
					for (Video v : videos) { 
						this.screen.toPlayNext.addEntry(v);
					}
				}
				
				
				this.screen.showNext();

				stateTimer.schedule(this.toPlayNextScreenDurationParameter*1000);
			} catch (Exception e) {
				Log.error(this,  "Error", e);
			}
			
		} else if ( State.TAGCLOUD == state ) { //show tag cloud
			Log.info(this, "Going to TagCloud state");
			this.currentState = State.TAGCLOUD;
			this.screen.showTagCloud();
			Timer timer = new Timer() {

				@Override
				public void run() {
					PublicYoutubePlayer.this.screen.tagCloud.updateGui();
					
				}
				
			};
			timer.schedule(500);
			
			stateTimer.schedule(this.tagCloudScreenDurationParameter*1000);
		}
	}
	

	

	private void loadTagCloud() {
		ArrayList<String> keywords = this.pdApplication.getLocalStorage().loadList("TagCloudKeywords");
		
		
		int [] frequencies = this.pdApplication.getLocalStorage().loadIntList("TagCloudFrequencies");
		
		if ( this.resetParameter ) {
			
			keywords = new ArrayList<String>();
			frequencies = new int[0];
		}
		
		if ( keywords.size() == 0 || frequencies.length == 0 ) {
			//gtc = new PdTagCloud("tagcloud", false);
			
		} else {
			for ( int i = 0; i < keywords.size(); i++) {
				this.screen.tagCloud.addTag(keywords.get(i), frequencies[i]);
			}
			//gtc = new PdTagCloud("tagcloud", keywords.toArray( new String[]{}), frequencies, allowUserTagsParameter);
		}
		
		
		//this.gtc.addActionListener(this);
		

		
		
		/*
		 * Make sure the tagcloud has the placetags
		 */
		this.placeTags = new ArrayList<String>();
		if ( null != this.placeTagsParameter && this.placeTagsParameter.length() > 0 ) {
			String tags[] = this.placeTagsParameter.split(",");
			
			for ( String tag : tags ) {
				this.placeTags.add(tag);
				if ( !this.screen.tagCloud.contains(tag) ) {
					this.screen.tagCloud.addTag(tag, 1);
				}
			}
		}
		
		
	}
	
//	
//	
//	
//	private void saveRecentlyPlayedVideos() {
//		this.pdApplication.getLocalStorage().saveList("LastPlayedVideos", this.recentlyPlayedVideos);
//	}
	
	private void saveTagCloud() {
		ArrayList<TagCloud.Tag> tags = this.screen.tagCloud.getTagList();
		
		ArrayList<String> keywords = new ArrayList<String>();
		int[] frequencies = new int[tags.size()];
		
		int i = 0;
		for ( TagCloud.Tag t : tags ) {
			keywords.add(t.getWord());
			frequencies[i++] = t.getFrequency();
		}
		
		this.pdApplication.getLocalStorage().saveList("TagCloudKeywords", keywords);
		this.pdApplication.getLocalStorage().saveIntList("TagCloudFrequencies", frequencies);
	}
	
	
	
	private void timerStalledElapsed() {
		Log.warn(this, "Video stalled, stopping it.");
		this.youtube.stop();
		this.videoEnded();
	}
	
	private void timerStateElapsed() {
		if ( State.ACTIVITY == this.currentState ) {
			this.gotoState(State.TAGCLOUD);
		} else if ( State.TAGCLOUD == this.currentState ) {
			this.gotoState(State.NEXT);
		} else if ( State.NEXT == this.currentState ) {
			this.gotoState(State.PLAYING);
		}
	}
	
	

	private void updateTagCloud( Video video ) {
		
		TagCloud.Tag originTag = this.screen.tagCloud.getTag( video.getOriginatingTags() );
		Log.debug(this, "Updating tag cloud with tag '" +originTag + "' from video '" + video.getId() + "'");
		if ( null != originTag ) {
		
			int f = originTag.getFrequency();
			f = f + 3;
			if ( f > 50 ) {
				f = 50;
			}
			originTag.setFrequency(f);
		}
		//
		
		/*
		 * Add two keywords of the video to the tag cloud, that aren't already on the tag cloud
		 */
		String[] keywords = video.getKeywords();
		
		
		
		int count = 0;
		for ( String keyword: keywords ) {
			if ( !this.screen.tagCloud.contains(keyword) ) {
				this.screen.tagCloud.addTag(keyword, 6);
				count ++;
				if ( count >= 2 ) {
					break;
				}
			}
		}
		
		/*
		 * Limit tag cloud size.
		 * 
		 * TODO: make this more efficient
		 */
		while ( this.placeTags.size() <= MAX_TAG_CLOUD_SIZE && this.screen.tagCloud.getKeywords().size() > MAX_TAG_CLOUD_SIZE ) {
			ArrayList<TagCloud.Tag> tags = this.screen.tagCloud.getTagListSortedByFrequency(false);
			Iterator<TagCloud.Tag> it = tags.iterator();
			
			while( it.hasNext() ) {
				
				TagCloud.Tag tag = it.next();
				
				if ( !this.placeTags.contains( tag.getWord() ) ) {
					//remove
					it.remove();
					break;
				}
			}
			
			this.screen.tagCloud.setTagList(tags);
		}
		this.screen.tagCloud.updateGui();
		this.saveTagCloud();
	}

	private void videoEnded() {
		//this.youtube.clear();
		this.stalledTimer.cancel();
		
		this.gotoState(State.ACTIVITY);
		decreaseTagCloudFrequency();
	}

	
	
}
