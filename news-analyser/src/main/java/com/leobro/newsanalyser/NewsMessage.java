package com.leobro.newsanalyser;

/**
 * Data class to hold information on one news message.
 */
public class NewsMessage {

	private String headline;
	private int priority = -1;

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
