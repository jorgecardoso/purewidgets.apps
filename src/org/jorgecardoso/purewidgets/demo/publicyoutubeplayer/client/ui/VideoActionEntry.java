/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui;

import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Log;
import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.client.widgets.PdButton;
import org.purewidgets.client.widgets.PdDownload;
import org.purewidgets.client.widgets.youtube.Video;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;

import com.google.gwt.core.client.GWT;

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
	

		
	public VideoActionEntry(Video video, String actionLabel, String actionId, boolean createDownloadButton, boolean createReportButton) {
		initWidget(uiBinder.createAndBindUi(this));
		this.actionId = actionId;
		this.video = video;
		
		title.setText( video.getTitle() );
		
		uiLabelUser.setText( video.getAuthor() );
		
		image.setUrl( video.getThumbnail() );
		
		likeGuiButton = createButton(video.getId(), actionLabel);
		
		likeGuiButton.setLongDescription(video.getTitle());
		likeGuiButton.getWidgetOptions().get(0).setIconUrl(video.getThumbnail());
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
	
	/**
	 * 
	 */
	private PdButton createButton(String videoId, final String label) {
		PdButton btn = new PdButton(this.encodeLabel(label)+"-"+videoId, label);
		
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
		PdButton btn = new PdButton(this.encodeLabel("Report")+"-"+video.getId(), constants.reportAsInnapropriate());
		btn.setLongDescription(video.getTitle());
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
		PdDownload btn = new PdDownload(this.encodeLabel("Download")+"-"+video.getId(), constants.getLink(), video.getDefaultPlayer());
		btn.setLongDescription(video.getTitle());
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
	
	private String encodeLabel(String label) {
		return label.replace(' ', '_');
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
}