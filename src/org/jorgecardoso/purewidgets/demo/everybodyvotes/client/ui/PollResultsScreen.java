package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;
import org.purewidgets.client.widgets.PdListBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;

public class PollResultsScreen extends Composite implements PollScreenInterface {

	private static PollResultsScreenUiBinder uiBinder = GWT.create(PollResultsScreenUiBinder.class);

	interface PollResultsScreenUiBinder extends UiBinder<Widget, PollResultsScreen> {
	}

	@UiField
	InlineLabel labelQuestion;

	private EBVPollDao poll;

	public PollResultsScreen(EBVPollDao poll) {
		this.poll = poll;
		initWidget(uiBinder.createAndBindUi(this));
		this.labelQuestion.setText(poll.getPollQuestion());
	}



	 @UiFactory 
	SimplePanel getPollResultPanel() {
		DataTable dt = DataTable.create();
		dt.addColumn(ColumnType.STRING, "Option");
		dt.addColumn(ColumnType.NUMBER, "Votes"); 
		for ( EBVPollOptionDao pollOption : this.poll.getPollOptions() ) {
			int i = dt.addRow();
			dt.setValue(i, 0, pollOption.getOption() + ": " + pollOption.getVotes()+" voto(s)");
			dt.setValue(i, 1, pollOption.getVotes());
		}
		
	    ChartArea ca = ChartArea.create();
	    ca.setTop("20%");
	    ca.setLeft("30%");
	    ca.setWidth("70%");
	    ca.setHeight("70%");
	    
	    
		PieChart.PieOptions options = PieChart.PieOptions.create();
		//Options options = Options.create();
		
		options.setSliceVisibilityThreshold(0);
		
	    options.setFontSize(23);
	    
	    options.set3D(true);
	    
	    options.setChartArea(ca);
	    options.set("forceIFrame", "false");
	    options.setFontName("'Nunito', sans-serif");
	    
	    options.setTitle("");//"Results for: " + poll.getPollQuestion());
	    

	    AxisOptions ao = AxisOptions.create();
//	    ao.setTextPosition("in");
//	    options.setVAxisOptions(ao);
//	    
//	    ao = AxisOptions.create();
//	    ao.setTextPosition("none");
//	    ao.setMinValue(0);
//	    options.setHAxisOptions(ao);

		PieChart pie = new PieChart(dt, options);
		
		
		//pie.setWidth("500");
		//pie.setHeight("400");
		
		SimplePanel p = new SimplePanel();
		
		p.add(pie);
		
		
		pie.setWidth(Window.getClientWidth()+"px");
		pie.setHeight(Window.getClientHeight()+"px");
		
		return p;
	}



	@Override
	public EBVPollDao getPoll() {
		
		return this.poll;
	}



	@Override
	public Composite getUi() {
		
		return this;
	}
	

}
