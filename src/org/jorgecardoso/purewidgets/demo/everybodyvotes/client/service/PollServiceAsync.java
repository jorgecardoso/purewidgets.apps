package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service;

import java.util.List;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PollServiceAsync {
	public void deletePoll(EBVPollDao poll, AsyncCallback<Void> callback);
	public void getActivePolls(String placeId, AsyncCallback<List<EBVPollDao>> callback);
	public void getClosedPolls(String placeId, AsyncCallback<List<EBVPollDao>> callback);
	public void getPoll(String pollId, AsyncCallback<EBVPollDao> callback);
	public void getPolls(String placeId, AsyncCallback<List<EBVPollDao>> callback);
	public void savePoll(EBVPollDao poll, AsyncCallback<Void> callback);
	public void updatePolls(String placeId, String applicationId, AsyncCallback<Void> callback);
}
