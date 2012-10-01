package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.ui;

import org.purewidgets.client.htmlwidgets.youtube.Video;
import org.purewidgets.shared.events.ActionEvent;

public interface VideoActionListener {
	public void onVideoAction(ActionEvent<?> event, Video video, String action);
}
