package org.jorgecardoso.purewidgets.demo.wordgame.client;

import java.util.Arrays;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.EveryBodyVotes;
import org.jorgecardoso.purewidgets.demo.wordgame.client.dictionary.WordService;
import org.jorgecardoso.purewidgets.demo.wordgame.client.dictionary.WordListJson;
import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.MainUi;
import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.ProcessingPopup;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.application.PDApplicationLifeCycle;
import org.purewidgets.client.htmlwidgets.youtube.json.JsonVideoList;
import org.purewidgets.client.widgets.PdWidget;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class WordGame implements EntryPoint, PDApplicationLifeCycle, ActionListener{

	private static final String LS_CURRENT_WORD_KEY = "currentWord";
	

	
	PDApplication application;
	
	MainUi mainUi;
	
	HighScores highScores;
	
	String currentWord;
	
	@Override
	public void onModuleLoad() {
		PDApplication.load(this, "WordGame");		
		
	}

	@Override
	public void onPDApplicationLoaded(PDApplication pdApplication) {
		this.application = pdApplication;
		Log.debug(this, "Loaded");
		highScores = new HighScores();
		mainUi = new MainUi(this, highScores);
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
		RootPanel.get().add(mainUi);
		this.getWord();
	}
	
	private void getWord() {
		Log.debug(this, "getting word");
		String word = this.application.getLocalStorage().getString(LS_CURRENT_WORD_KEY);
		if ( null == word || word.length() < 1 ) {
			final WordService searchWord = new WordService();
			
			final ProcessingPopup pp = new ProcessingPopup();
			pp.setSize((Window.getClientWidth()-50)+"px", (Window.getClientHeight()-50)+"px");
			pp.show();
			pp.center();
			
			searchWord.getRandomWord(new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.warn(WordGame.this, "Could not get a new word from the dictionary.", caught);
					pp.hide();
				}

				@Override
				public void onSuccess(String result) {
					Log.debug(WordGame.this, "Got new word: " + result);
					pp.hide();
					WordGame.this.onNewWord(result);
					
				}
				
			});
			
		} else {
			this.onNewWord(word);
		}
		
	}
	
	private void onNewWord(String word) {
		this.currentWord = word;
		this.application.getLocalStorage().setString(LS_CURRENT_WORD_KEY, word);
		this.mainUi.setWord(word);
		
	}

	private void clearWord() {
		this.application.getLocalStorage().setString(LS_CURRENT_WORD_KEY, "");
	}
	
	@Override
	public void onAction(final ActionEvent<?> e) {
		PdWidget source = (PdWidget)e.getSource();
		
		if ( source.getWidgetId().equalsIgnoreCase("try") ) {
			final String word = (String)e.getParam();
		
			
			
			CheckWord.check(e.getNickname(), this.currentWord, word, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(Boolean result) {
					if ( result.booleanValue() ) {
						highScores.add(e.getUserId(), e.getNickname(), word.length());
						WordGame.this.mainUi.updateHighScores();
						WordGame.this.clearWord();
						WordGame.this.getWord();
						
					}
					
				}
			});
				
			
		} else if ( source.getWidgetId().equalsIgnoreCase("another") ) {
			WordGame.this.clearWord();
			WordGame.this.getWord();
		}
		
	}
	
	
	

}
