package com.victorguerrero.screeninteractioners;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Contact {
	private String name;
	private String jobTitle;
	private String thumbnailURL;
	private String phone;
	private String pictureURL;
	private String email;
	private String webpage;
	private Bitmap thumbnail;
	private Bitmap picture;
	private JSONObject json;
	
	public Contact() {
		name = "UNDEFINED";
		jobTitle = "UNDEFINED";
	}
	
	public Contact(JSONObject json) {
		try {
			this.json = json;
			name = json.getString("name");
			jobTitle = json.getString("job_title");
			thumbnailURL = json.getString("thumbnail-url");
			pictureURL = json.getString("picture-url");
			email = json.getString("email");
			webpage = json.getString("webpage");
			phone = json.getString("phone");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
	
	public String getJobTitle() {
		return jobTitle;
	}
	
	public String getWebpage() {
		return webpage;
	}
	
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	
	public Bitmap getThumbnail() {
		return thumbnail;
	}
	
	public void setThumbnail(Bitmap image) {
		thumbnail = image;
	}
	
	public String getPictureURL() {
		return pictureURL;
	}
	
	public Bitmap getPicture() {
		return picture;
	}
	
	public void setPicture(Bitmap image) {
		picture = image;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Boolean getStarred() {
		return false;
	}
	
	@Override
	public String toString() {
		return json.toString();
	}
}
