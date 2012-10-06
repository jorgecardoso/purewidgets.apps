package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import org.jorgecardoso.purewidgets.demo.wordgame.client.HighScores;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HighScoresUi extends Composite  {
	interface Style extends CssResource {
		
	    String left();
	    String right();
	
	  }
	
	@UiField Style style;
	
	private static HighScoresUiUiBinder uiBinder = GWT.create(HighScoresUiUiBinder.class);

	interface HighScoresUiUiBinder extends UiBinder<Widget, HighScoresUi> {
	}

	private HighScores highScores;

	@UiField 
	HTMLPanel verticalPanel;
	
	public HighScoresUi(HighScores highScores) {
		this.highScores = highScores;
		initWidget(uiBinder.createAndBindUi(this));
		this.update();
	}

	public void update() {
		verticalPanel.clear();
		
		int i = 0;
		for ( HighScores.Score s : this.highScores.getScores() ) {
			HorizontalPanel p = new HorizontalPanel();
			verticalPanel.add(p);
			
			p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			p.add(new Label( s.getNickname() ));
			
			
			p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			p.add(new Label( s.getScore()+"" ));
			
			i++;
			if ( i >= 10 ) {
				break;
			}
		}
		
	}
}
