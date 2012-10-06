package org.jorgecardoso.purewidgets.demo.wordgame.client.dictionary;

import org.purewidgets.client.json.GenericJson;


public class WordDefinitionJson extends GenericJson  {

	protected WordDefinitionJson() {}
	
	
    public native final String getDefinition() /*-{
		if ( typeof(this.entry) == "undefined" ) {
			if ( typeof(this.superEntry) == "undefined" ) {
				return "";
			} else if ( typeof(this.superEntry[0].entry.sense) == "undefined" ) {
				return "";
			} else {
				return this.superEntry[0].entry.sense[0].def;
			}
		} else if ( typeof(this.entry.sense) == "undefined" ) {
			return "";
		} else {
			return this.entry.sense[0].def;
		}
	}-*/;
}
