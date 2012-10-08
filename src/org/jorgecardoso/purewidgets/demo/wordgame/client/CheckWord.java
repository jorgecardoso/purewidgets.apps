package org.jorgecardoso.purewidgets.demo.wordgame.client;

import java.util.Arrays;

import org.jorgecardoso.purewidgets.demo.wordgame.client.dictionary.WordService;
import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.CorrectPopupUi;
import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.IncorrectPopupUi;
import org.jorgecardoso.purewidgets.demo.wordgame.client.ui.ProcessingPopup;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

public class CheckWord {

	
	public static void check(final String nickname, final String word, final String toCheck, final AsyncCallback<Boolean> callback) {
		// show pop up with checking message
		
		final ProcessingPopup pp = new ProcessingPopup();
		pp.show();
		
		if ( isAnagram (word, toCheck) ) {
			final WordService searchWord = new WordService();
			
			
			searchWord.getDefinition(toCheck, new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					Log.warn(CheckWord.class, "Could not load the definition.", caught);
					pp.hide();
					if ( word.equalsIgnoreCase(toCheck) ) {
						CorrectPopupUi cpp = new CorrectPopupUi(nickname, toCheck+": Não foi possível carregar a definição da palavra");
						
						showTemporarily(cpp, 30000);
						callback.onSuccess(true);
					} else {
						IncorrectPopupUi cpp = new IncorrectPopupUi(nickname);
						showTemporarily(cpp, 10000);
						callback.onFailure(caught);
					}
				}
	
				@Override
				public void onSuccess(String result) {
					Log.debug(CheckWord.class, "Got definition: " + result);
					pp.hide();
					CorrectPopupUi cpp = new CorrectPopupUi(nickname, toCheck+": "+result);
				
					showTemporarily(cpp, 30000);
					callback.onSuccess(true);
				}
				
			});
		} else {
			// show popup with error
			Log.debug(CheckWord.class, "Sorry, words don't match");
			pp.hide();
			
			if ( word.equalsIgnoreCase(toCheck) ) {
				CorrectPopupUi cpp = new CorrectPopupUi(nickname, toCheck+": Não foi possível carregar a definição da palavra");
				
				showTemporarily(cpp, 30000);
				callback.onSuccess(true);
			} else {
				IncorrectPopupUi cpp = new IncorrectPopupUi(nickname);
				
				showTemporarily(cpp, 10000);
				callback.onSuccess(false);
			}	
		}
	}
	
	private static void showError() {
		
	}
	
	private static void showDefinition() {
		
	}
	
	private static void showTemporarily(final PopupPanel panel, int milliseconds) {
		//panel.setSize((Window.getClientWidth())+"px", (Window.getClientHeight())+"px");
		panel.show();
		panel.setPopupPosition(0, 0);
		panel.getElement().getStyle().setTop(15, Unit.PCT);
		panel.getElement().getStyle().setBottom(0, Unit.PX);
		panel.getElement().getStyle().setRight(0, Unit.PX);
		
		new Timer() {

			@Override
			public void run() {
				panel.hide();
				
			}
			
		}.schedule(milliseconds);
	}
	
	/**
	 * Adapted from http://www.daniweb.com/software-development/java/threads/324533/writing-code-to-see-whether-anagram#
	 * @param phrase1
	 * @param phrase2
	 * @return
	 */
	private static boolean isAnagram(String phrase1, String phrase2)
	{
		Log.debug(phrase1 + " " + phrase2);
		phrase1 = phrase1.toLowerCase();
		phrase2 = phrase2.toLowerCase();
		
		char[] p1array = phrase1.toCharArray();
		java.util.Arrays.sort(p1array);
		char[] p2array = phrase2.toCharArray();
		java.util.Arrays.sort(p2array);
		
		Log.debug(Arrays.toString(p1array));
		Log.debug(Arrays.toString(p2array));
		
		
		return Arrays.equals(p1array, p2array) ;
		
	}
}
