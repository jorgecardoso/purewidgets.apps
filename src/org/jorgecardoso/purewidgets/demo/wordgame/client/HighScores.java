package org.jorgecardoso.purewidgets.demo.wordgame.client;

import java.util.ArrayList;
import java.util.Collections;

import org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client.Util;
import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.htmlwidgets.youtube.Video;
import org.purewidgets.client.htmlwidgets.youtube.VideoAdapter;
import org.purewidgets.client.htmlwidgets.youtube.json.JsonVideoEntry;

public class HighScores {
	public class Score implements Comparable<Score> {
		private String userId;
		private String nickname;
		private int score;
		
		public Score (String id, String nick, int score) {
			this.userId = id;
			this.nickname = nick;
			this.score = score;
		}

		public void addToScore(int toAdd) {
			this.score += toAdd;
		}
		
		/**
		 * @return the score
		 */
		public int getScore() {
			return score;
		}

		/**
		 * @param score the score to set
		 */
		public void setScore(int score) {
			this.score = score;
		}

		/**
		 * @return the userId
		 */
		public String getUserId() {
			return userId;
		}

		/**
		 * @param userId the userId to set
		 */
		public void setUserId(String userId) {
			this.userId = userId;
		}

		/**
		 * @return the nickname
		 */
		public String getNickname() {
			return nickname;
		}

		/**
		 * @param nickname the nickname to set
		 */
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		@Override
		public int compareTo(Score arg0) {
			
			return arg0.getScore()-this.score;
		}
	}
	
	
	ArrayList<Score> scores;
	
	
	public HighScores() {
		scores = new ArrayList<Score>();
		this.loadFromLocalStorage();
	}
	
	public ArrayList<Score> getScores() {
		return this.scores;
		
	}
	
	public void add(String userId, String nickname, int scoreToAdd) {
		int index = this.contains(userId);
		
		if ( index > -1 ) {
			Log.debug(this, "User exists");
			this.scores.get(index).addToScore(scoreToAdd);
		} else {
			Log.debug(this, "User does not exist");
			Score s = new Score(userId, nickname, scoreToAdd);
			this.scores.add(s);
		}
		Collections.sort(this.scores);
		this.saveToLocalStorage();
	}
	
	private int contains(String userId) {
		for (int i = 0; i < this.scores.size(); i++ ) {
			if ( this.scores.get(i).getUserId().equals(userId) ) {
				return i;
			}
		}
		return -1;
		
	}
	
	

	
	private void saveToLocalStorage() {
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> nicks = new ArrayList<String>();
		ArrayList<String> scores = new ArrayList<String>();
		for ( Score s : this.scores ) {
			ids.add(s.getUserId());
			nicks.add(s.getNickname());
			scores.add(s.getScore()+"");
		}
		PDApplication.getCurrent().getLocalStorage().saveList("highScores-Ids", ids);
		PDApplication.getCurrent().getLocalStorage().saveList("highScores-Nicks", nicks);
		PDApplication.getCurrent().getLocalStorage().saveList("highScores-Scores", scores);
	}
	
	private void loadFromLocalStorage() {
		ArrayList<String> ids = PDApplication.getCurrent().getLocalStorage().loadList("highScores-Ids");
		ArrayList<String> nicks = PDApplication.getCurrent().getLocalStorage().loadList("highScores-Nicks");
		ArrayList<String> scores = PDApplication.getCurrent().getLocalStorage().loadList("highScores-Scores");
		
		for ( int i = 0; i < ids.size(); i++) {
			Score s = new Score(ids.get(i), nicks.get(i), Integer.parseInt(scores.get(i)) );
			
			this.scores.add(s);
		}
		Collections.sort(this.scores);
	}
	
	
}
