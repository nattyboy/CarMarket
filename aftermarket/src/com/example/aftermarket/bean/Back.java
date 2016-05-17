package com.example.aftermarket.bean;

import java.util.List;

public class Back {

	public int code ;
	public String msg;
	public List<BackData> data;
	@Override
	public String toString() {
		return "Back [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
}
