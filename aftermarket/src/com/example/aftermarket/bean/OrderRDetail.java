package com.example.aftermarket.bean;

import java.io.Serializable;
import java.util.List;

public class OrderRDetail implements Serializable {
	
	public int code;
	public String msg;
	public OrderItemDetailR data;
	@Override
	public String toString() {
		return "OrderRDetail [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
	
	
	

}
