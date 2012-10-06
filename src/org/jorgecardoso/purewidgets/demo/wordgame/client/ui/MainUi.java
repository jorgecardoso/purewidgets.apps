package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import org.jorgecardoso.purewidgets.demo.wordgame.client.HighScores;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.client.widgets.PdButton;
import org.purewidgets.client.widgets.PdTextBox;
import org.purewidgets.shared.events.ActionListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class MainUi extends Composite {

	private static MainUiUiBinder uiBinder = GWT.create(MainUiUiBinder.class);

	interface MainUiUiBinder extends UiBinder<Widget, MainUi> {
	}

	
	HighScores highScores;
	 
	private ActionListener listener;
	public MainUi(ActionListener listener, HighScores highScores) {
		this.listener = listener;
		this.highScores = highScores;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	WordUi wordUi;

	@UiField
	HighScoresUi highScoresUi;
	
	public void setWord(String word) {
		
		this.wordUi.setText(word);
	}
	
	
	public void updateHighScores() {
		this.highScoresUi.update();
	}

	 @UiFactory 
	 HighScoresUi makeHighScores() {
		 HighScoresUi hs = new HighScoresUi(this.highScores);
		 return hs;
	 }
	 
	 @UiFactory 
	 PdButton makePdButton() { 
		 
		 PdButton tb = new PdButton("another", "Esta é muito difícil!");
			tb.addActionListener(this.listener);
//			tb.setOnScreenFeedbackInfo(msgs.userFeedback()+", "+MessagePattern.PATTERN_USER_NICKNAME);
//			tb.setOffScreenFeedbackTitle(MessagePattern.PATTERN_USER_NICKNAME);
//			tb.setOffScreenFeedbackInfo(msgs.userFeedback());
			//tb.setLongDescription(poll.getPollQuestion() );
			return tb;
   }
	
	 @UiFactory 
	 PdTextBox makePdTextbox() { 
		 
			PdTextBox tb = new PdTextBox("try", "Adivinha a palavra", null);
			tb.addActionListener(this.listener);
//			tb.setOnScreenFeedbackInfo(msgs.userFeedback()+", "+MessagePattern.PATTERN_USER_NICKNAME);
//			tb.setOffScreenFeedbackTitle(MessagePattern.PATTERN_USER_NICKNAME);
//			tb.setOffScreenFeedbackInfo(msgs.userFeedback());
			//tb.setLongDescription(poll.getPollQuestion() );
						return tb;
   }

}
