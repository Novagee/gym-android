package com.novagee.aidong.model;

import java.util.Date;

/**
 * 
 * @author seeyet
 * 活动列表的子项对�?
 */
public class Event {
	
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
	  * �?始时�?
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
	 

	public Event(){
		
	}
	
	public Event(String pic,String title,String description,String id){
		this.pic = pic;
		this.title = title;
		this.description = description;
		this.id = id;
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
