package org.jorgecardoso.purewidgets.demo.instructions.client.ui;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;;

@DefaultLocale("en")
public interface ScreenMessages extends Messages{
	@DefaultMessage("How to interact with this display?")
	String title();
	
	@DefaultMessage("{0}")
	String webAddressTitle(String address);
	
	@DefaultMessage("Send an SMS to: {0}")
	String smsAddressTitle(String address);	
	
	@DefaultMessage("Text me")
	String textLabel();
	
	@DefaultMessage("Click me")
	String buttonLabel();	
	
	@DefaultMessage("(3ax)")
	String buttonReferenceCode();
	
	@DefaultMessage("97..")
	String smsPhoneNumber();	
	
	@DefaultMessage("{0}.3ax")
	String smsText1(String place);		
	
	@DefaultMessage("message")
	String smsText2();	
	
	
	@DefaultMessage("Send an Email to: {0}")
	String emailTitle(String emailAddres);		
	
	@DefaultMessage("3ax Message")
	String emailSubject();	
	
	
	@DefaultMessage("3ax")
	String emailButtonSubject();	
	
	@DefaultMessage("QR Codes")
	String qrCodeTitle();		
}