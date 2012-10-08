package org.jorgecardoso.purewidgets.demo.wordgame.client.dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.jorgecardoso.purewidgets.demo.wordgame.client.Log;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.http.HttpService;
import org.purewidgets.client.http.HttpServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class WordService {
	String chars = "abcdefghijklmnopqrstuvwxyz";

	//private AsyncCallback<String> callback;
	
	public WordService() {
		//this.callback = callback;
	}
	
	
	public void getDefinition(String word, AsyncCallback<String> callback) {
		this.requestDefinition(word, callback);
		
	}
	

	private void requestDefinition(final String word, final AsyncCallback<String> callback) {
		HttpServiceAsync interactionService;
		interactionService = GWT.create(HttpService.class);
	
		
		//JsonpRequestBuilder builder = new JsonpRequestBuilder();//RequestBuilder.GET, url);
		
	    
	    //builder.requestString("http://www.dicionario-aberto.net/search-json/"+word, new AsyncCallback<String>() {
		interactionService.get("http://www.dicionario-aberto.net/search-json/"+word, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Log.warn(WordService.this, "Could not get definition. ", caught);
				callback.onFailure(caught);
//				new Timer() {
//					@Override
//					public void run() {
//						requestDefinition(word, callback);
//					}
//					
//				}.schedule(1500);
				
			}

			@Override
			public void onSuccess(String result) {
				Log.debug(result);
				result = result.replace("{,", "{");
				Log.debug(result);
				WordDefinitionJson definition = WordDefinitionJson.fromJson(result);
				callback.onSuccess(definition.getDefinition());
				
			}
	    });
	}
	
	
	
	public void getRandomWord(AsyncCallback<String> callback) {
		this.requestRandomWord(callback);
	}
	
	
	
	private void requestRandomWord(final AsyncCallback<String> callback) {
	
		
		
		JsonpRequestBuilder builder = new JsonpRequestBuilder();//RequestBuilder.GET, url);
		
	    
	      //Request request = 
	    builder.requestObject("http://www.dicionario-aberto.net/search-json?random=1", new AsyncCallback<WordDefinitionJson>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(WordService.this, "Could not get word list. ", caught);
				Log.warn(WordService.this, "Retrying in 1500 milliseconds.");
				new Timer() {
					@Override
					public void run() {
						requestRandomWord(callback);
					}
				
				}.schedule(1500);
			}

			@Override
			public void onSuccess(WordDefinitionJson result) {
				String word = result.getWord();
				Log.debug(WordService.this,  word);
				
				boolean noGood = false;
				for ( int i = 0; i < word.length(); i++ ) {
					if ( chars.indexOf(word.charAt(i)) < 0 ) {
						
						noGood = true;
						break;
					}
				}
				if ( word.length() < 4 || word.length() > 12) {
					noGood = true;
				}
		
					
				if ( noGood ) {
					Log.warn(WordService.this, "Got an empty word. Retrying in 1500 milliseconds.");
					new Timer() {
						@Override
						public void run() {
							requestRandomWord(callback);
						}
					
					}.schedule(1500);
				} else {

					callback.onSuccess(word);
				} 
			}
	    });
	}
	
//
//	private void requestRandomWord(final AsyncCallback<String> callback) {
//		final Random random = new Random(System.currentTimeMillis());
//		int maxPrefixChars = random.nextInt(3)+1;
//		String prefix = "";
//		for (int i = 0; i < maxPrefixChars; i++ ) {
//			int r = random.nextInt(chars.length());
//			prefix += chars.charAt(r);
//		}
//		
//		
//		JsonpRequestBuilder builder = new JsonpRequestBuilder();//RequestBuilder.GET, url);
//		
//	    
//	      //Request request = 
//	    builder.requestObject("http://www.dicionario-aberto.net/search-json?prefix="+prefix, new AsyncCallback<WordListJson>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				Log.warn(WordService.this, "Could not get word list. ", caught);
//				Log.warn(WordService.this, "Retrying in 1500 milliseconds.");
//				new Timer() {
//					@Override
//					public void run() {
//						requestRandomWord(callback);
//					}
//				
//				}.schedule(1500);
//			}
//
//			@Override
//			public void onSuccess(WordListJson result) {
//				String []words = result.getList();
//				Log.debug(WordService.this, Arrays.toString( words));
//				
//				ArrayList<String> filteredWords = new ArrayList<String>();
//				
//				for ( String word : words ) {
//					boolean remove = false;
//					for ( int i = 0; i < word.length(); i++ ) {
//						if ( chars.indexOf(word.charAt(i)) < 0 ) {
//							
//							remove = true;
//							break;
//						}
//					}
//					if ( word.length() < 4 || word.length() > 8) {
//						remove = true;
//					}
//					if ( !remove ) {
//						filteredWords.add(word);
//					}
//					
//				}
//				
//				words = filteredWords.toArray(new String[]{});
//				
//				Log.debug(WordService.this, "Filtered:" +  Arrays.toString( words));
//				
//				if ( words.length > 0 ) {
//					int r = random.nextInt(words.length );
//					callback.onSuccess(words[r]);
//				} else {
//					Log.warn(WordService.this, "Got an empty set of words. Retrying in 1500 milliseconds.");
//					new Timer() {
//						@Override
//						public void run() {
//							requestRandomWord(callback);
//						}
//					
//					}.schedule(1500);
//				
//				
//					
//				}
//			}
//	    });
//	}
}
