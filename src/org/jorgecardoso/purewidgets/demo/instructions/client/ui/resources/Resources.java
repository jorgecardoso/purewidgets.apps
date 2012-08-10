package org.jorgecardoso.purewidgets.demo.instructions.client.ui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
	  Resources INSTANCE = GWT.create(Resources.class);

	  @Source("web.svg")
	  TextResource webSvg();
	  
	  @Source("smstext.svg")
	  TextResource smsTextSvg();
	  
	  @Source("smsbutton.svg")
	  TextResource smsButtonSvg();
	  
	  @Source("emailtext.svg")
	  TextResource emailTextSvg();
	  
	  @Source("emailbutton.svg")
	  TextResource emailButtonSvg();
	  
	  @Source("qrcode.svg")
	  TextResource qrCodeSvg();	  
}