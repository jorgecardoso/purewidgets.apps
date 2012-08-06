package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;


import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;

import com.google.gwt.user.client.ui.Composite;

public interface PollScreenInterface  {
	EBVPollDao getPoll();
	
	Composite getUi();
}
