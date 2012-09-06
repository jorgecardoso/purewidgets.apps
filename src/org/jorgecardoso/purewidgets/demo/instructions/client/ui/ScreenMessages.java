package org.jorgecardoso.purewidgets.demo.instructions.client.ui;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;;

@DefaultLocale("en")
public interface ScreenMessages extends Messages{
	@DefaultMessage("How to interact with this display?")
	String title();
	
	@DefaultMessage("{0}")
	String webAddressTitle(String address);
	
	@DefaultMessage("SMS: {0}")
	String smsAddressTitle(String address);	
	
	@DefaultMessage("Texxt me")
	String textLabel();
	
	@DefaultMessage("Click me")
	String buttonLabel();	
	
	@DefaultMessage("3ax")
	String buttonReferenceCode();
	
	@DefaultMessage("97..")
	String smsPhoneNumber();	
	
	@DefaultMessage("{0}.3ax message")
	String smsText1(String place);		
	
	@DefaultMessage("xpto")
	String smsText2();	
	
	
	@DefaultMessage("Email: {0}")
	String emailTitle(String emailAddres);		
	
	@DefaultMessage("{0}.ref")
	String emailSubject(String placeReferenceCode);	
	
	
	@DefaultMessage("QR Code")
	String qrCodeTitle();		
}