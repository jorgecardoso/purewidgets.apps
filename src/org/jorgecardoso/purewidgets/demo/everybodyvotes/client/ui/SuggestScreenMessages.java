package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages.AlternateMessage;
import com.google.gwt.i18n.client.Messages.DefaultMessage;

@DefaultLocale("en")
public interface SuggestScreenMessages extends Messages {
	
	@DefaultMessage("Suggest your own poll!")
	String askForPolls();
}
	 