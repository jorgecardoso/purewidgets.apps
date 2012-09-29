package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import java.util.ArrayList;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.EveryBodyVotes;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.client.widgets.PdListBox;
import org.purewidgets.client.widgets.PdTextBox;
import org.purewidgets.shared.events.ActionEvent;
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
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SuggestScreen extends Composite  {

	private static PollScreenUiBinder uiBinder = GWT.create(PollScreenUiBinder.class);

	interface PollScreenUiBinder extends UiBinder<Widget, SuggestScreen> {
	}
	
	SuggestScreenMessages msgs = GWT.create(SuggestScreenMessages.class);
			

	public SuggestScreen() {
		initWidget(uiBinder.createAndBindUi(this));

	}


	 @UiFactory 
	 PdTextBox makePdTextbox() { 
		 
			PdTextBox tb = new PdTextBox("suggest", msgs.askForPolls(), null);
			tb.setOnScreenFeedbackInfo(msgs.userFeedback()+", "+MessagePattern.PATTERN_USER_NICKNAME);
			tb.setOffScreenFeedbackTitle(MessagePattern.PATTERN_USER_NICKNAME);
			tb.setOffScreenFeedbackInfo(msgs.userFeedback());
			//tb.setLongDescription(poll.getPollQuestion() );
						return tb;
   }

}
