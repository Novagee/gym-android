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
	 private String starttime;
	 
	 /**
	  * 结束时间
	  */
	 private String endtime;
	 
	 /**
	  * ？不知道
	  */
	 private boolean isActive;
	 
	 /**
	  * 活动图片
	  */
	 private String pic;
	 
	 /**
	  * 活动地点
	  */
	 private String address;
	 
	 /**
	  * 费嗯
	  */
	 private int fee;
	 

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

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}
	 
	 
	 
}
