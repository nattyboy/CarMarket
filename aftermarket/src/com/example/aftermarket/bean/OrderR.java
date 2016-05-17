package com.example.aftermarket.bean;

import java.io.Serializable;
import java.util.List;

public class OrderR implements Serializable{
	public int code;
	public String msg;
	public List<OrderItemR> data;
	@Override
	public String toString() {
		return "OrderR [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
