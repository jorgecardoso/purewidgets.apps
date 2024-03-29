package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import java.util.ArrayList;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.EveryBodyVotes;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

import org.purewidgets.client.widgets.PdListBox;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;
import org.purewidgets.shared.logging.Log;

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

public class PollScreen extends Composite  implements PollScreenInterface {

	private static PollScreenUiBinder uiBinder = GWT.create(PollScreenUiBinder.class);

	interface PollScreenUiBinder extends UiBinder<Widget, PollScreen> {
	}
	
	PollScreenMessages msgs = GWT.create(PollScreenMessages.class);
			
	@UiField
	InlineLabel labelQuestion;
	
	@UiField
	Label labelCloseTime;

	private EBVPollDao poll;
	
	public PollScreen(EBVPollDao poll) {
		this.poll = poll;
		initWidget(uiBinder.createAndBindUi(this));
		this.labelQuestion.setText(poll.getPollQuestion());
		
		this.displayTimeLeft();
	}
	
	private void displayTimeLeft() {
		float timeLeftMinutes = ( poll.getClosesOn() - System.currentTimeMillis() )/(1000*60); //minutes
		float timeLeftHours = timeLeftMinutes/60;
		float timeLeftDays = timeLeftHours/24;
		float timeLeftWeeks = timeLeftDays/7;
		
		
		Log.debug(this, "Timeleft: " + timeLeftMinutes + " minutes,  "+ timeLeftHours + " hours");
		String timeLeft = "";
		if ( timeLeftWeeks >= 1 ) {
			timeLeft =  msgs.closingPeriodWeek( ((int)timeLeftWeeks) ); 
		} else if ( timeLeftDays >= 1 ){
			timeLeft =  msgs.closingPeriodDay( ((int)timeLeftDays) );
		} else if ( timeLeftHours >= 1) {
			timeLeft = msgs.closingPeriodHour( ((int)timeLeftHours) );
		} else {
			timeLeft = msgs.closingPeriodMinute( ((int)timeLeftMinutes) );
		}
		
		this.labelCloseTime.setText(timeLeft);
	}
	
	
	 @UiFactory 
	 PdListBox makePdListbox() { 
		 ArrayList<String> l = new ArrayList<String>();
			for ( EBVPollOptionDao pollOption : this.poll.getPollOptions() ) {
				l.add(pollOption.getOption());
			}
			
			PdListBox tb = new PdListBox("poll " + this.poll.getPollId(), this.poll.getPollQuestion(), l);
			tb.setOffScreenFeedbackInfo("%WL%: %WOS%");
			tb.setShortDescription("Vote");
			tb.setLongDescription(poll.getPollQuestion() );
			tb.addActionListener(new ActionListener() {

				@Override
				public void onAction(ActionEvent<?> e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			return tb;
    }

	/**
	 * @return the poll
	 */
	public EBVPollDao getPoll() {
		return poll;
	}

	@Override
	public Composite getUi() {
		return this;
	}



}
