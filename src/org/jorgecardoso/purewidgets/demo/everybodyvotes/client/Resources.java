package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE =  GWT.create(Resources.class);

	  @Source("css/pyp.css")
	  @CssResource.NotStrict
	  public CssResource css();

}
