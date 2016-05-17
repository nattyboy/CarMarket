package com.example.aftermarket.bean;

import java.util.List;

public class MyQuestion {
	
	public int code;
	public String msg;
	public List<MyQuestionData> data;
	@Override
	public String toString() {
		return "MyQuestion [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	

}
