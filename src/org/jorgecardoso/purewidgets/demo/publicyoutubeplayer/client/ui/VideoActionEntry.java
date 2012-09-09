/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui;

import java.util.ArrayList;

import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Log;
import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.client.widgets.PdButton;
import org.purewidgets.client.widgets.PdDownload;
import org.purewidgets.client.widgets.youtube.Video;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;
import org.purewidgets.shared.im.WidgetParameter;

import com.gargoylesoftware.htmlunit.html.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class VideoActionEntry extends Composite {

	private static VideoActionEntryUiBinder uiBinder = GWT
			.create(VideoActionEntryUiBinder.class);

	interface VideoActionEntryUiBinder extends
			UiBinder<Widget, VideoActionEntry> {
	}
	
	private VideoActionEntryConstants constants = GWT.create(VideoActionEntryConstants.class);
	
	@UiField
    Label title;
	
	@UiField
	HorizontalPanel outerPanel;
	
	@UiField
	HTMLPanel buttonPanel;

	@UiField
	Image image;
	
	@UiField
	Label uiLabelUser;
	
	private Video video;
	
	private PdButton likeGuiButton;
	private PdDownload downloadGuiButton;
	private PdButton reportButton;
	private VideoActionListener videoEventListener;
	private String actionId;
	
	private int order;
		
	public VideoActionEntry(Video video, int order, String actionLabel, String actionId, boolean createDownloadButton, boolean createReportButton) {
		initWidget(uiBinder.createAndBindUi(this));
		this.actionId = actionId;
		this.video = video;
		this.order = order;
		
		String title = video.getTitle();
		title = title.replaceAll("_", " ");
		title = title.replaceAll("\"", " ");
		title = title.toLowerCase();
		
		if ( null != title && title.length() > 30 ) {
			title = title.substring(0, 30) + "...";
		}
		this.title.setText( title );
		
		uiLabelUser.setText( video.getAuthor() );
		
		image.setUrl( video.getThumbnail() );
		
//		lastVideoOrderTag = org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Util.getPdApplication().getLocalStorage().getInteger("lastVideoOrderTag");
//		if ( null != lastVideoOrderTag) {
//			lastVideoOrderTag++;
//			if ( lastVideoOrderTag > 100 ) {
//				lastVideoOrderTag = 0;
//			}
//			
//			
//		} else {
//			lastVideoOrderTag = 0;
//		}
//		org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Util.getPdApplication().getLocalStorage().setInt("lastVideoOrderTag", lastVideoOrderTag);

		
		likeGuiButton = createButton(video, actionLabel);
		

		buttonPanel.add( likeGuiButton );
		if ( createDownloadButton ) {
			downloadGuiButton =  createDownloadButton(video);
		}
		
		if ( createReportButton ) {
			reportButton = this.createReportButton(video);
		}
    }
	
	public void highlight(boolean h) {
		if ( h ) { 
			this.outerPanel.addStyleName("highlight");
		} else {
			this.outerPanel.removeStyleName("highlight");
		}
	}
	
	public void dispose() {
		Log.debug(this, "Disposing");
		if ( null != likeGuiButton ) {
			Log.debugFinest(this, "Disposing of button");
			likeGuiButton.removeFromServer();
		}
		if ( null != downloadGuiButton ) {
			Log.debugFinest(this, "Disposing of download button");
			downloadGuiButton.removeFromServer();
		}
		
		if ( null != this.reportButton ) {
			Log.debugFinest(this, "Disposing of report button");
			this.reportButton.removeFromServer();
		}
	}
	
	private  ArrayList<WidgetParameter> createSortParameter(String prefix, int order) {
		ArrayList<WidgetParameter> parameters = new ArrayList<WidgetParameter>();
		parameters.add(new WidgetParameter(org.purewidgets.shared.im.Widget.SORT_ORDER_PARAMETER_NAME, prefix + NumberFormat.getFormat("000").format(order)));
		return parameters;
	}
	/**
	 * 
	 */
	private PdButton createButton(Video video, final String label) {
		PdButton btn = new PdButton(this.actionId + " " + video.getId(), label, null, video.getTitle(), createSortParameter("d", this.order));
		
		btn.getWidgetOptions().get(0).setIconUrl(video.getThumbnail());
		
		btn.setUserSharedInfoInputFeedbackPattern(MessagePattern.PATTERN_WIDGET_SHORT_DESCRIPTION + ": " + MessagePattern.PATTERN_WIDGET_LONG_DESCRIPTION+"(10)");
		btn.getFeedbackSequencer().setFeedbackFinalDelay(5000);
		btn.addActionListener(new ActionListener() {

			@Override
			public void onAction(ActionEvent<?> e) {
				if ( null != VideoActionEntry.this.videoEventListener ) {
					Log.warn(VideoActionEntry.this, "Triggering video event.");
					VideoActionEntry.this.videoEventListener.onVideoAction(e, VideoActionEntry.this.video, VideoActionEntry.this.actionId);
				} else {
					Log.warn(VideoActionEntry.this, "No video event listener defined...");
				}
				
			}
			
		});
		return btn;
	}
	/**
	 * 
	 */
	private PdButton createReportButton(Video video) {
		PdButton btn = new PdButton("Report " + video.getId(), constants.reportAsInnapropriate(), null, video.getTitle(), createSortParameter("a", this.order) );
		
		btn.getWidgetOptions().get(0).setIconUrl(video.getThumbnail());
		//btn.getFeedbackSequencer().setFeedbackFinalDelay(5000);
		btn.addActionListener(new ActionListener() {

			@Override
			public void onAction(ActionEvent<?> e) {
				if ( null != VideoActionEntry.this.videoEventListener ) {
					VideoActionEntry.this.videoEventListener.onVideoAction(e, VideoActionEntry.this.video, "Report");
				}
				
			}
			
		});
		return btn;
	}
	
	/**
	 * 
	 */
	private PdDownload createDownloadButton(Video video) {
		
		
		PdDownload btn = new PdDownload("Download " + video.getId(), constants.getLink(), video.getDefaultPlayer(), null, video.getTitle(), createSortParameter("b", this.order));

		btn.getWidgetOptions().get(0).setIconUrl(video.getThumbnail());
		//btn.getFeedbackSequencer().setFeedbackFinalDelay(5000);
		btn.addActionListener(new ActionListener() {

			@Override
			public void onAction(ActionEvent<?> e) {
				if ( null != VideoActionEntry.this.videoEventListener ) {
					VideoActionEntry.this.videoEventListener.onVideoAction(e, VideoActionEntry.this.video, "Download");
				}
				
			}
			
		});
		return btn;
	}
	

	/**
	 * @return the videoEventListener
	 */
	public VideoActionListener getVideoEventListener() {
		return videoEventListener;
	}


	/**
	 * @param videoEventListener the videoEventListener to set
	 */
	public void setVideoEventListener(VideoActionListener videoEventListener) {
		this.videoEventListener = videoEventListener;
	}

	/**
	 * @return the video
	 */
	public Video getVideo() {
		return video;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}