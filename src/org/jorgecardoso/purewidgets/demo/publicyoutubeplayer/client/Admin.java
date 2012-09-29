package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import java.util.ArrayList;

import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.storage.ServerStorage;
import org.purewidgets.shared.storage.KeyValue;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Admin {
	
	private String OPTIONS[] = {PublicYoutubePlayer.URL_PARAMETER_MAX_VIDEO_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_MAX_FEATURED_VIDEO_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_ACTIVITY_SCREEN_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_TAGCLOUD_SCREEN_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_PLACE_TAGS,
								PublicYoutubePlayer.URL_PARAMETER_FEATURED_USERS,
								PublicYoutubePlayer.URL_PARAMETER_FEATURED_VIDEOS_PERCENTAGE,
								PublicYoutubePlayer.URL_PARAMETER_MAX_FEATURED_VIDEOS,
								PublicYoutubePlayer.URL_PARAMETER_RESET,
								"videoBlackList",
								"font-size"};
	
	private String DESCRIPTIONS[] = {"(seconds) The maximum video duration for played videos",
									"(seconds) The maximum video duration for videos from featured users",
									"(seconds) How much time to stay at the activity screen",
									"(seconds) How much time to stay at the tagcloud screen",
									"(seconds) How much time to stay at the results screen",
									"(comma separated list) base place tags that are always present in the tag cloud",
									"(comma separated list) featured youtube usernames.",
									"(float) percentage of videos from featured users to provide",
									"(int) load at most this number of latest videos from each featured user",
									"(true/false) Reset the tag cloud in the nex run?",
									"List of blacklisted video ids",
									"Body font size"
		
	};
	
	private ArrayList<TextBox> values;
	
	private ServerStorage rs;

	
	private ArrayList<String> parameters;
	private ArrayList<String> descriptions;
	
	public void  run(PDApplication app) {
		parameters = new ArrayList<String>();
		descriptions = new ArrayList<String>();
		for (String p : OPTIONS) {
			parameters.add(p);
		}
		for (String d : DESCRIPTIONS) {
			descriptions.add(d);
		}
		for (String p : VideoSearcher.YOUTUBE_SEARCH_PARAMETERS) {
			parameters.add("youtube-"+p);
			descriptions.add(p);
		}
		

		
		rs = app.getRemoteStorage();
		
		
		values = new ArrayList<TextBox>();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		
		
		for (int i = 0; i < parameters.size(); i++) {
			String option = parameters.get(i);
		
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			Label l = new Label(option);
			TextBox textbox = new TextBox();
			values.add(textbox);
			horizontalPanel.add(l);
			horizontalPanel.add(textbox);
			horizontalPanel.add(new Label(descriptions.get(i)));
			
			verticalPanel.add(horizontalPanel);
		}
		
		
		
		RootPanel.get().add(verticalPanel);
		
		Button save = new Button("Save");
		
		RootPanel.get().add(save);
		
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveOptions();
			}
		});
		loadOptions();
	}
	
	public  void loadOptions() {
		
		for (int i = 0; i < parameters.size();  i++) {
			rs.getString(parameters.get(i),  new AsyncCallback<KeyValue>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.debug("Failed");
					Log.debug(caught.getMessage());
					RootPanel.get().add(new Label("Failed"));
				}

				@Override
				public void onSuccess(KeyValue result) {
					if ( null == result ) return;
					
					for (int j = 0; j < parameters.size(); j++) {
						if ( parameters.get(j).equals(result.getKey()) ) {
							TextBox t = values.get(j);
							t.setText(result.getValue());
									
						}
					}
					Log.debug("ok:" + result.getValue());
					//RootPanel.get().add(new Label("ok: " + result));
				}
				
			});			
			
		}		
	}
	
	
	public  void saveOptions() {
		
		
		for (int i = 0; i < parameters.size();  i++) {
			String text = values.get(i).getText();
			rs.setString(parameters.get(i), text, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.debug("Failed");
					Log.debug(caught.getMessage());

				}

				@Override
				public void onSuccess(Void result) {
					Log.debug("ok");
					
				}
				
			});
		}
		
		/*
		
		rs.getString("teste",  new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.debug("Failed");
				Log.debug(caught.getMessage());
				RootPanel.get().add(new Label("Failed"));
				
			}

			@Override
			public void onSuccess(String result) {
				Log.debug("ok:" + result);
				RootPanel.get().add(new Label("ok: " + result));
			}
			
		});
		*/
	
	}
}
