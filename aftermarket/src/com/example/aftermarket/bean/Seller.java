package com.example.aftermarket.bean;

import java.util.List;

public class Seller {
	public int code;
	public String msg;
	public List<SellerInfo> data;
	@Override
	public String toString() {
		return "Seller [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
