package org.jorgecardoso.purewidgets.demo.wordgame.client.ui;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WordUi extends Composite implements HasText {

	private static WordUiUiBinder uiBinder = GWT.create(WordUiUiBinder.class);

	interface WordUiUiBinder extends UiBinder<Widget, WordUi> {
	}

	private static final int COLS = 9;
	private static final int ROWS = 4;
	
	ArrayList<Integer> indexes;
	
	public WordUi() {
		initWidget(uiBinder.createAndBindUi(this));
		indexes = new ArrayList<Integer>();
		for ( int i = 0; i < COLS*ROWS; i++ ) {
			indexes.add(new Integer(i));
		}
		initGrid();
	}

	@UiField
	Label labelWord;
	
	@UiField
	Grid gridWord;	

	public WordUi(String word) {
		super();
		
		setText(word);
	}

	private void initGrid() {
		gridWord.resize(ROWS, COLS);
	}

	public void setText(String text) {
		for ( int i = 0; i <ROWS; i++ ) {
			for ( int j = 0; j < COLS; j++ ) {
				gridWord.clearCell(i, j);
			}
		}
		
		this.shuffle(indexes);
		
		for ( int i = 0; i < text.length(); i++ ) {
				int index = indexes.get(i).intValue();
				int r = index/COLS;
				int c = index%COLS;
			
			//} while ( this.gridWord.getText(r, c).trim().length() >= 1);
			gridWord.setText(r, c, text.charAt(i)+"");
		}
	}

	private void shuffle(ArrayList<Integer> indexes) {
		for ( int i = 0; i < indexes.size(); i++ ) {
			int newIndex = (int)(Math.random()*indexes.size());
			Integer one = indexes.get(i);
			indexes.set(i, indexes.get(newIndex));
			indexes.set(newIndex, one);
		}
		
	}

	public String getText() {
		return labelWord.getText();
	}

}
