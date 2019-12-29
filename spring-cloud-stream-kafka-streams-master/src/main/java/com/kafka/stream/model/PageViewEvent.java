package com.kafka.stream.model;

public class PageViewEvent {
		
	private String userId;
	private String page;
	private long duration;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	@Override
	public String toString() {
		return "PageViewEvent [userId=" + userId + ", page=" + page + ", duration=" + duration + "]";
	}
	

}
