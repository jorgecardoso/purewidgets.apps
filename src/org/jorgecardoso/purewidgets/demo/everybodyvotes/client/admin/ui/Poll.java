package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class Poll extends Composite  {

	interface PollUiBinder extends UiBinder<Widget, Poll> {
	}

	private static PollUiBinder uiBinder = GWT.create(PollUiBinder.class);

	
	@UiField
	Button addPollButton;
	
	@UiField
	Button buttonAdd;
	
	@UiField
	DateBox closesOnDateBox;
	
	@UiField
	Button deleteButton;
	
	
	ArrayList<PollOption> options;
	
	
	@UiField
	VerticalPanel optionsPanel;
	
	@UiField
	Label pollIdLabel;
	
	
	@UiField
	TextBox questionTextBox;
	
	@UiField
	Button saveButton;
	
	@UiField
	DateBox showAfterDateBox;
	
	@UiField
	DateBox showUntilDateBox;
	
	private PollActionListener listener;

	public Poll() {
		initWidget(uiBinder.createAndBindUi(this));
		options = new ArrayList<PollOption>();
		
		showAfterDateBox.setValue(new Date());
		updateGui();
	}

	@UiHandler("buttonAdd")
	void onAddClick(ClickEvent e) {
		PollOption o = new PollOption();
		o.setPoll(this);
		options.add(o);
		updateGui();
	}
	
	@UiHandler("addPollButton")
	void onAddPollClick(ClickEvent e) {
		this.pollIdLabel.setText("");
		this.options.clear();
		this.questionTextBox.setText("");
		this.updateGui();
	}
	
	@UiHandler("deleteButton")
	void onDeleteClick(ClickEvent e) {
		if ( null != this.listener ) {
			this.listener.onDeletePoll(this);
		}
	}
	
	@UiHandler("saveButton")
	void onSaveClick(ClickEvent e) {
		//options.add(new PollOption());
		//updateGui();
		if ( null != this.listener ) {
			this.listener.onSavePoll(this);
		}
	}
	
	void updateGui() {
		
		
		this.optionsPanel.clear();
		for ( PollOption option : this.options ) {
			this.optionsPanel.add(option);
		}
	}
	
	public void addOption(String option, int votes) {
		this.options.add( new PollOption(this, option, votes) );
		this.updateGui();
	}

	public void clearOptions() {
		this.options.clear();
		this.updateGui();
	}

	public Date getClosesOn() {
		return this.closesOnDateBox.getValue();
	}
	
	/**
	 * @return the listener
	 */
	public PollActionListener getListener() {
		return listener;
	}
	public ArrayList<PollOption> getOptions() {
		return this.options;
	}
	
	public ArrayList<String> getOptionsText() {
		ArrayList<String> options = new ArrayList<String>();
		for ( PollOption option : this.options ) {
			options.add( option.getText() );
		}
		return options;
	}
	
	public String getPollIdText() {
		return this.pollIdLabel.getText();
	}
	
	
	public String getQuestionText() {
		return this.questionTextBox.getText();
	}
	
	public Date getShowAfter() {
		return this.showAfterDateBox.getValue();
	}
	
	public Date getShowUntil() {
		return this.showUntilDateBox.getValue();
	}
	public void onDeleteOption(PollOption optionDeleted) {
		//Window.alert("Deleting option: " + optionDeleted);
		this.options.remove(optionDeleted);
		this.updateGui();
	}
	
	public void setClosesOn(Date d) {
		this.closesOnDateBox.setValue(d);
	}
	/**
	 * @param listener the listener to set
	 */
	public void setListener(PollActionListener listener) {
		this.listener = listener;
	}
	
	public void setPollIdText(String pollId) {
		this.pollIdLabel.setText(pollId);
	}
	public void setQuestionText(String text) {
		this.questionTextBox.setText(text);
	}
	
	public void setShowAfter(Date d) {
		this.showAfterDateBox.setValue(d);
	}
	
	public void setShowUntil(Date d) {
		this.showUntilDateBox.setValue(d);
	}

}
