package org.jorgecardoso.purewidgets.demo.wordgame.client.dictionary;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;

import com.google.gwt.core.client.JsArrayString;

public class WordListJson extends GenericJson {
	
	protected WordListJson() {}
	
    private native final JsArrayString getListAsJsArray() /*-{
		if ( typeof(this.list) == "undefined" ) {
			return Array();
		} else {
			return this.list;
		}
	}-*/;
    
    public final String[] getList() {
    	JsArrayString jsArrayString = this.getListAsJsArray();
    	
    	
    	if ( jsArrayString.length() == 0 ) {
    		
    		
    		return new String[0];
    		
    		
    	} else {
    		String []list = new String[jsArrayString.length()];
    	
    		for (int i = 0; i < jsArrayString.length(); i++ ) {
    			list[i] = jsArrayString.get(i);
    		}
    		return list;
    	}
    	
    }
        
}
