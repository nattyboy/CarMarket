package com.example.aftermarket.bean;

import java.util.List;

public class OrderEvaluteData {
	
	public String msg;
	public int code;
	public List<EvaluteDataItem> data;
	@Override
	public String toString() {
		return "OrderEvaluteData [msg=" + msg + ", code=" + code + ", data=" + data + "]";
	}
	

}
