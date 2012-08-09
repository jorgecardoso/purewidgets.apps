package org.jorgecardoso.purewidgets.demo.instructions.client.ui;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;;

@DefaultLocale("en")
public interface ScreenMessages extends Messages{
	@DefaultMessage("How to interact with this screen")
	String title();
	
	@DefaultMessage("Web: {0}")
	String webAddress(String address);
	
	@DefaultMessage("SMS: {0}")
	String smsAddress(String address);	
	
	@DefaultMessage("Text me")
	String smsButtonLabel();	
	
	@DefaultMessage("3ax")
	String smsButtonReferenceCode();
	
	@DefaultMessage("97..")
	String smsPhoneNumber();	
	
	@DefaultMessage("{0}.3ax message")
	String smsText1(String place);		
	
	@DefaultMessage("xpto")
	String smsText2();		
}