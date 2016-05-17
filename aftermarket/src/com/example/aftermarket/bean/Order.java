package com.example.aftermarket.bean;

import java.util.List;

public class Order {
	public int code;
	public String msg;
	public List<OrderItem> data;
	@Override
	public String toString() {
		return "Order [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
