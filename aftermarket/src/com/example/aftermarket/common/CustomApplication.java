package com.example.aftermarket.common;


import android.app.Application;

public class CustomApplication extends Application{
	
	public static CustomApplication mInstance;
	private String token;
	
	 @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		init();
	}
	 public String getToken(){
		 return token;
	 }
	 public void setToken(){
		 this.token=token;
	 }

	private void init() {
		// TODO Auto-generated method stub
		
	}
	public static CustomApplication getInstance() {
		return mInstance;
	}

}
