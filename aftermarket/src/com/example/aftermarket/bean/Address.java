package com.example.aftermarket.bean;

import java.util.List;

public class Address {
	
	public int code;
	public String msg;
	public List<AddressInfo> data;
	@Override
	public String toString() {
		return "Address [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	

}
