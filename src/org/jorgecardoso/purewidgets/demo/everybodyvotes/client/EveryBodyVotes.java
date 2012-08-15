/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.application.PDApplicationLifeCycle;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.client.widgets.PdListBox;

import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;
import org.purewidgets.shared.logging.Log;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin.Admin;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollService;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollServiceAsync;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui.MainScreen;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;



/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EveryBodyVotes implements ActionListener, PDApplicationLifeCycle, EntryPoint{
	
	private static final String LS_CURRENT_POLL_INDEX = "currentPollIndex";
	
	private static final int POLL_DISPLAY_INTERVAL = 1500000; 
	
	private static final float SUGGEST_PROBABILITY = 0.2f;
	//private static final int POLL_RESULT_DISPLAY_INTERVAL = 15000; 
	
	
	private PDApplication pdApplication;
	
	private int currentPollIndex;
	
	private LocalStorage localStorage;
	
	private List<EBVPollDao> polls;
	
	private PollServiceAsync pollService;
	
	/*
	 * Indicates how many service responses we got. Used to trigger the gui when we have received both
	 * the closed polls and the active polls. 
	 */
	private int receivedCount = 0;
	
	/*
	 * Indicates if we are showing the poll or the poll result due to user interaction
	 */
	private boolean showingPollResult = false;
	
	private Timer timer;
	private long timerRegularStart;
	private long timerResultStart;

	private MainScreen mainScreen;
	
	
	
	@Override
	public void onAction(ActionEvent<?> e) {
//		this.onTimerElapsed();
//		GuiWidget widget = (GuiWidget)e.getSource();
//		String pollId = widget.getWidgetId().substring(5).trim();
//		Log.debug(this, "Current poll: " + this.polls.get( this.currentPollIndex ).getPollId());
//		Log.debug(this, "Received poll interaction: " + pollId);
//		Log.debug(this, "Equal: " + this.polls.get( this.currentPollIndex ).getPollId().toString().equals(pollId) );
//		if ( this.polls.get( this.currentPollIndex ).getPollId().toString().equals(pollId) && !this.showingPollResult) {
//			Log.debug(this, "Showing result");
//			//this.showPollResult(pollId);
//		}

		
	}

	
	@Override
	public void onPDApplicationLoaded(PDApplication application) {
		this.pdApplication = application;
		
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			new Admin().run();
			return;
		} 
		
				
		
		/*
		 * Get the index of the last poll displayed
		 */
		this.localStorage = application.getLocalStorage();
		
		Integer currentPoll =  this.localStorage.getInteger(LS_CURRENT_POLL_INDEX);
	
		
		if ( null == currentPoll ) {
			this.currentPollIndex = -1;
		} else {
			this.currentPollIndex = currentPoll.intValue();
		}
		
		/*
		 * Create the service to get polls 
		 */
		pollService = GWT.create(PollService.class);
		((ServiceDefTarget)pollService).setServiceEntryPoint("/pollservice"); 
		
		
		/*
		 * This makes sure that the application is created on the server side
		 * 
		 */
		pollService.updatePolls(application.getPlaceId(), 
				application.getApplicationName(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(EveryBodyVotes.class.getName(), "Could not update polls!");
					}

					@Override
					public void onSuccess(Void result) {
						Log.warn(EveryBodyVotes.class.getName(), "Polls updated successfully");
					}
		});

		
		this.askForPollList();
		
		mainScreen = new MainScreen();
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));

		RootPanel.get().add(mainScreen);
		
	}
	
	@Override
	public void onModuleLoad() {
		/*
		 * Load the Google visualization API and then the PublicDisplayApplication
		 */
		VisualizationUtils.loadVisualizationApi("1.1", new Runnable() {
			@Override
			public void run() {
				PDApplication.load(EveryBodyVotes.this, "EveryBodyVotes");
			}
		} , CoreChart.PACKAGE);

	}
	
//	private void startResultTimer(){
//		if ( null == timer ) {
//			timer = new Timer() {
//				@Override
//				public void run() {
//					EveryBodyVotes.this.onTimerElapsed();
//				}
//			};
//		}
//		timer.schedule(POLL_RESULT_DISPLAY_INTERVAL);
//		this.timerResultStart = System.currentTimeMillis();
//		this.showingPollResult = true;
//	}

	private void advancePoll() {
		Log.debug(this, "Advancing poll");
		if ( null == this.polls || this.polls.size() < 1 ) {
			return;
		}
		
		this.currentPollIndex++;
		
		if ( this.currentPollIndex > this.polls.size()-1 ) {
			this.currentPollIndex = 0;
		}
		
		this.localStorage.setInt(LS_CURRENT_POLL_INDEX, this.currentPollIndex);
		
		EBVPollDao nextPoll = this.polls.get(this.currentPollIndex);


		this.mainScreen.show( nextPoll ); //, nextPoll.getClosesOn() >= System.currentTimeMillis(), timeLeft );


	}
	
	
	private void askForPollList() {
		
		this.receivedCount = 0;
		
		if ( null != this.polls ) {
			this.polls.clear();
		} else {
			this.polls = new ArrayList<EBVPollDao>();
		}
		
		pollService.getActivePolls(this.pdApplication.getPlaceId(), new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(EveryBodyVotes.class.getName(), "Oops! " + caught.getMessage() );
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				if ( null == result || result.size() == 0 ) {
					//Window.alert("no polls found");
					Log.warn(EveryBodyVotes.class.getName(), "No active polls found");
				}
				Log.debug(EveryBodyVotes.class.getName(), "Received " + result.size() + " open polls");
				/*
				 * The active polls are the first in the list
				 */
				EveryBodyVotes.this.polls.addAll(0, result);
				
//				EveryBodyVotes.this.widgets = new HashMap<String, PdListBox>();
//				for ( EBVPollDao poll : result ) {
//					ArrayList<String> l = new ArrayList<String>();
//					for ( EBVPollOptionDao pollOption : poll.getPollOptions() ) {
//						l.add(pollOption.getOption());
//					}
//					
//					PdListBox tb = new PdListBox("poll " + poll.getPollId(), poll.getPollQuestion(), l);
//					tb.setShortDescription("Vote");
//					tb.setLongDescription(poll.getPollQuestion() );
//					tb.addActionListener(EveryBodyVotes.this);
//					EveryBodyVotes.this.widgets.put(poll.getPollId().toString(), tb);
//					//EveryBodyVotes.this.slidingPanel.add(tb);
//				}
				
				
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
			}
		});
		
		pollService.getClosedPolls(this.pdApplication.getPlaceId(), new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(EveryBodyVotes.class.getName(), "Oops! " + caught.getMessage() );
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				if ( null == result || result.size() == 0 ) {
					//Window.alert("no polls found");
					Log.warn(EveryBodyVotes.class.getName(), "No closed polls found");
				}
				Log.debug(EveryBodyVotes.class.getName(), "Received " + result.size() + " closed polls");
				/*
				 * The closed polls are the last in the list
				 */
				EveryBodyVotes.this.polls.addAll(result);
//				for (EBVPollDao p: result) {
//					slidingPanel.add(EveryBodyVotes.this.getPollResult(p));
//				}
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
				
			}
		});
	}
	
	private void checkAdvancePoll(int count) {
		Log.debug(this, count+"");
		if ( count >= 2 ) {
			//this.slidingPanel.setWidget(this.slidingPanel.getWidgets().get(0));
			//this.slidingPanel.setWidget(this.slidingPanel.getWidgets().get(1));
			//this.slidingPanel.setWidget(this.slidingPanel.getWidgets().get(0));
			this.advancePoll();
			startRegularTimer();
		}
	}
	
//	private  SimplePanel getPollListBox(EBVPollDao poll) {
//		
//		PdListBox tb = EveryBodyVotes.this.widgets.get(poll.getPollId().toString());
//		
//			SimplePanel panel = new SimplePanel();
//			panel.setStyleName("listboxPanel");
//			panel.add(tb);
//			return panel;
//	
//	}
	
//	private SimplePanel getPollWidget(EBVPollDao poll) {
//		if ( EveryBodyVotes.this.widgets.containsKey(poll.getPollId().toString()) ) {
//			return getPollListBox(poll);
//		} else {
//			return getPollResult(poll);
//		}
//	}
	
	private void onTimerElapsed() {
		Log.debug(this, "Timer elapsed");
		if ( Math.random() < SUGGEST_PROBABILITY ) {
			this.mainScreen.showSuggestScreen();
		} else {
			this.advancePoll();
		}
		this.startRegularTimer();

		
	}
	
	
	private void startRegularTimer() {
		this.startRegularTimer(POLL_DISPLAY_INTERVAL);
	}
	
//	private void showClosedPoll(EBVPollDao poll) {
//		PieChart pie = getPollResult(poll);
//		
//		RootPanel.get("content").clear();
//		Label title = new Label("Results for \"" + poll.getPollQuestion() + "\"");
//		
//		RootPanel.get("content").add(title);
//		RootPanel.get("content").add(pie);
//		
//		GuiTextBox suggest = new GuiTextBox("suggest", "Suggest a poll");
//		suggest.setWidth("500px");
//		RootPanel.get("content").add(suggest);
//	}
//	
//	private void showOpenPoll(EBVPollDao poll) {
//		
//		GuiListBox tb = EveryBodyVotes.this.widgets.get(poll.getPollId().toString());
//		RootPanel.get("content").clear();
//		RootPanel.get("content").add(tb);
//		
//	}
	
	
//	private void showPoll(EBVPollDao poll) {
//		long today = new Date().getTime();
//		
//		if ( poll.getClosesOn() < today ) { // closed poll
//			showClosedPoll(poll);
//		} else {
//			showOpenPoll(poll);
//		}	
//	}

//	private void showPollResult(final String pollId) {
//		Log.debug(this, "Showing poll result for poll: " + pollId);
//		
//		pollService.updatePolls(PublicDisplayApplication.getPlaceName(), PublicDisplayApplication.getApplicationName(), new AsyncCallback<Void>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("Oops. Could not update poll" + caught.getMessage());
//				
//			}
//
//			@Override
//			public void onSuccess(Void result) {
//				
//				pollService.getPoll(pollId, new AsyncCallback<EBVPollDao>(){
//
//					@Override
//					public void onFailure(Throwable caught) {
//						Window.alert(caught.getMessage());
//						
//					}
//
//					@Override
//					public void onSuccess(EBVPollDao result) {
//						EveryBodyVotes.this.startResultTimer();
//						EveryBodyVotes.this.showClosedPoll(result);
//					}
//					
//				});
//				
//			}
//			
//		});
//	}

	private void startRegularTimer(int delay) {
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					EveryBodyVotes.this.onTimerElapsed();
				}
			};
		}
		timer.schedule(delay);
		this.timerRegularStart = System.currentTimeMillis();
		this.showingPollResult = false;
	}
}
