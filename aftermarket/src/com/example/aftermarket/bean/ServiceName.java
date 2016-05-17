package com.example.aftermarket.bean;

import java.util.List;

public class ServiceName {
	public int code;
	public String msg;
	public List<ServiceNameItem> data;
	public String desc;
	@Override
	public String toString() {
		return "ServiceName [code=" + code + ", msg=" + msg + ", data=" + data + ", desc=" + desc + "]";
	}

}
