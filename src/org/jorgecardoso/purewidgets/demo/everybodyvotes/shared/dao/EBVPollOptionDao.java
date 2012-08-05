/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao;

import java.io.Serializable;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EBVPollOptionDao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private String option;
	
	private int votes;

	public EBVPollOptionDao(String option) {
		this.option = option;
		this.votes = 0;
	}
	
	@SuppressWarnings("unused")
	private EBVPollOptionDao() {}

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @return the votes
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @param votes the votes to set
	 */
	public void setVotes(int votes) {
		this.votes = votes;
	}
}
