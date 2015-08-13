package com.jianyue.utils;

import java.util.Date;

/**
 * 
 * @author seeyet
 * 活动列表的子项对象
 */
public class ClassEvent {
	
	 /**
	  * 活动id
	  */
	 private String id;
	 
	 /**
	  * 活动标题
	  */
	 private String title;
	 
	 /**
	  * 描述
	  */
	 private String description;
	 
	 /**
	  * 开始时间
	  */
	 private Date starttime;
	 
	 /**
	  * 结束时间
	  */
	 private Date endtime;
	 
	 /**
	  * ？不知道
	  */
	 private boolean isActive;
	 
	 /**
	  * 活动图片
	  */
	 private String pic;
	 
     /**
      * 活动图片
      */
	 private String imageUrl; 
	 
	 /**
	  * 活动标题
	  */
	 private String eventTitle;
	 
	 /**
	  * 活动描述
	  */
	 private String eventContent;
	 
	 /**
	  * 活动的id
	  */
	 private String eventId;
	 
	public ClassEvent(){
		
	}
	
	public ClassEvent(String imageUrl,String eventTitle,String eventContent,String eventId){
		this.imageUrl = imageUrl;
		this.eventTitle = eventTitle;
		this.eventContent = eventContent;
		this.eventId = eventId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	 
	 
	 
}
