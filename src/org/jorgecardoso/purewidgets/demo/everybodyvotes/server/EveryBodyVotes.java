package org.jorgecardoso.purewidgets.demo.everybodyvotes.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.purewidgets.server.application.PDApplication;
import org.purewidgets.server.application.PDApplicationLifeCycle;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;

import org.purewidgets.shared.widgets.ListBox;
import org.purewidgets.shared.widgets.TextBox;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.server.dao.Dao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

public class EveryBodyVotes extends HttpServlet implements PDApplicationLifeCycle, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	PDApplication app;
	
	String message;
	
	ArrayList<Widget> oldWidgetList;
	
	List<EBVPollDao> polls = null;
	
	HttpServletRequest req;
	
	HttpServletResponse resp;
	
	@Override
	public synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		
		this.resp = resp;
		
		PDApplication.load(req, this, "EveryBodyVotes");
	}
	
	
	@Override
	public void onPDApplicationEnded() {
		Log.debug(this, "EveryBodyVote: Finish");
		
		if ( null != this.resp ) {
			try {
				resp.setContentType("text/html");
				resp.getWriter().write("<html><body>" + message + "</body></html>");
			} catch (IOException e) {
				Log.error(this, "Could not write the Http response.");
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void onPDApplicationLoaded(PDApplication application) {
		Log.debug(this, "EveryBodyVote: Loaded");
		this.app = application;
		message = "";

		ArrayList<Widget> widgetList = app.getServerCommunicator().getWidgetsList(this.app.getPlaceId(), this.app.getApplicationId());
		this.onWidgetsList(widgetList);

		
		this.polls = Dao.getActivePolls(this.app.getPlaceId());
		
		/*
		 * Create the list widgets
		 */
		for ( EBVPollDao poll : polls ) {
			ArrayList<String> listOptions = new ArrayList<String>();
			message += poll.getPollQuestion() + "<br>";
			
			for ( EBVPollOptionDao pollOption : poll.getPollOptions() ) {
				listOptions.add(pollOption.getOption());
				message += pollOption.getOption() + ": " + pollOption.getVotes() + "<br>";
			}
			message += "<br>";
			
			String widgetId = "poll " + poll.getPollId();
			
			
			
			ListBox listBox = new ListBox(widgetId, poll.getPollQuestion(), listOptions);
			listBox.setShortDescription("Vote");
			listBox.setLongDescription( poll.getPollQuestion() );
			listBox.addActionListener( this );
			
			if ( !existsWidget(widgetId) ) {
				this.app.addWidget(listBox, true);
			} else {
				this.app.addWidget(listBox, false);
			}
		}
		
		
		
		Log.debug(this, "Creating suggest widget");
		TextBox suggest = new TextBox("suggest", "Suggest a poll");
		suggest.setShortDescription("Suggest a poll");
		//suggest.setLongDescription("Suggest a poll");
		suggest.addActionListener(this);
		if ( !existsWidget(suggest.getWidgetId()) ) {
			this.app.addWidget(suggest, true);
		} else {
			this.app.addWidget(suggest, false);
		}
		
		
		deleteUnusedWidgets();
	}

	@Override
	public void onAction(ActionEvent<?> ae) {		
		Widget source = (Widget)ae.getSource();
		
		if ( source.getWidgetId().startsWith("poll") ) {
			String pollIdString = source.getWidgetId().substring(5);
			long pollId = Long.parseLong(pollIdString);
			for ( EBVPollDao poll : this.polls ) {
				if ( poll.getPollId() == pollId ) {
					Log.info(this, "Voting on " + poll.getPollQuestion());
					Dao.beginTransaction();
					poll.vote(ae.getUserId(), ae.getParam().toString());
					Dao.put(poll);
					Dao.commitOrRollbackTransaction();
				}
			}
		} else if ( source.getWidgetId().equals("suggest") ) {
			this.sendMail(ae.getNickname(), ae.getParam().toString());
		}
		

		
	}

	
	private void deleteUnusedWidgets() {
		Log.debug(EveryBodyVotes.this, "Deleting unused widgets...");
		/*
		 * Go through our widgets and delete the ones that refer to closed polls
		 */
		for ( Widget widget : this.oldWidgetList ) {
			if (widget.getWidgetId().equals("suggest")) continue;
			
			boolean exists = false;
			for ( EBVPollDao poll : this.polls ) {
				if ( widget.getWidgetId().substring(5).equals( String.valueOf(poll.getPollId()) ) ) {
					exists = true;
				}
			}
			/*for ( Widget oldWidget : WidgetManager.get().getWidgetList() ) {
				if ( widget.getWidgetId().equals( oldWidget.getWidgetId() ) ) {
					exists = true;
				}
			}*/
			if (!exists) {
				Log.debug(EveryBodyVotes.this, "Deleting widget " + widget.getWidgetId() );
				this.app.removeWidget(widget, true);
			}
		}
	}

	private boolean existsWidget(String widgetId) {
		for ( Widget widget : this.oldWidgetList ) {
			if ( widget.getWidgetId().equals(widgetId) ) {
				return true;
			}
		}
		return false;
	}

	private void onWidgetsList(ArrayList<Widget> widgetList) {
		this.oldWidgetList = widgetList;
	}
	
	private void sendMail(String persona, String suggestion) {
		Log.debug(this, "Sending email: " + suggestion);
		
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = persona + " suggested: \n " + suggestion;

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("jorgediablu@gmail.com", "Jorge Cardoso"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("jorgecardoso@ieee.org", "Mr. User"));
            msg.setSubject("Poll suggestion");
            msg.setText(msgBody);
            Transport.send(msg);
    
        } catch (AddressException e) {
            Log.warn(this, e.getMessage());
        } catch (MessagingException e) {
            Log.warn(this, e.getMessage());
        } catch (UnsupportedEncodingException e) {
			Log.warn(this, e.getMessage());
			e.printStackTrace();
		}
		
	}

}
