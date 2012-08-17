package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages.AlternateMessage;
import com.google.gwt.i18n.client.Messages.DefaultMessage;

@DefaultLocale("en")
public interface PollScreenMessages extends Messages {
	@DefaultMessage("{0,number} hours")
	@AlternateMessage({"one", "1 hour"})
	String closingPeriodHour(@PluralCount int value);

	@DefaultMessage("{0,number} days")
	@AlternateMessage({"one", "1 day"})	
	String closingPeriodDay(@PluralCount int value);
	
	@DefaultMessage("{0,number} weeks")
	@AlternateMessage({"one", "1 week"})
	String closingPeriodWeek(@PluralCount int value);


	@DefaultMessage("{0,number} minutes")
	@AlternateMessage({"one", "1 minute"})
	String closingPeriodMinute(@PluralCount int value);
}
	 