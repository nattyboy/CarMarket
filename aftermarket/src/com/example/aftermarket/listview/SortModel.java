package com.example.aftermarket.listview;

import android.graphics.drawable.Drawable;

public class SortModel {

	private String name;   
	private String sortLetters;  
	private Drawable draw;
	private String img;
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Drawable getDraw() {
		return draw;
	}
	public void setDraw(Drawable draw) {
		this.draw = draw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
