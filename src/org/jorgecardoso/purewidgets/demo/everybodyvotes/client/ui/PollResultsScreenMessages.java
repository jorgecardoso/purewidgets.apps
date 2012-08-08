package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;;


@DefaultLocale("en")
public interface PollResultsScreenMessages extends Messages {
	@DefaultMessage("{0,number} votes")
	@AlternateMessage({"one", "1 vote"})
	String votes(@PluralCount int value);

}
	 