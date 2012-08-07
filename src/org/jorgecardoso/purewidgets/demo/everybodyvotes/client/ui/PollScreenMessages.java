package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import com.google.gwt.i18n.client.Messages;

public interface PollScreenMessages extends Messages {

	String closingPeriodHour(@PluralCount int value);

	String closingPeriodDay(@PluralCount int value);
	

	String closingPeriodWeek(@PluralCount int value);
}
	 