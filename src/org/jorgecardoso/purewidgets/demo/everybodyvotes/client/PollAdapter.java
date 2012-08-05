/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import java.util.ArrayList;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin.ui.Poll;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.admin.ui.PollOption;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class PollAdapter {

	public static EBVPollDao getEBVPollDao(Poll poll, String place) {
		
		EBVPollDao pollDao = new EBVPollDao();
		
		if ( !poll.getPollIdText().isEmpty() ) {
			pollDao.setPollId(Long.parseLong(poll.getPollIdText()));
		}
		pollDao.setPollQuestion(poll.getQuestionText());
		pollDao.setPlaceId(place);
		
		ArrayList<EBVPollOptionDao> optionsDao = new ArrayList<EBVPollOptionDao>();
		
		//for ( String optionText : poll.getOptionsText() ) {
		for ( PollOption option : poll.getOptions() ) {
			
			EBVPollOptionDao optionDao = new EBVPollOptionDao(option.getText());
			optionDao.setVotes(option.getVotes());
			optionsDao.add(optionDao);
		}
		pollDao.setPollOptions(optionsDao);
		
		pollDao.setShowAfter(poll.getShowAfter().getTime());
		pollDao.setShowUntil(poll.getShowUntil().getTime());
		pollDao.setClosesOn(poll.getClosesOn().getTime());
		
		
		return pollDao;
	}
}
