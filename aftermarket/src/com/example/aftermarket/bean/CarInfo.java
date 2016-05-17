package com.example.aftermarket.bean;

import java.util.List;

public class CarInfo {
	public int code;
	public String msg;
	public List <CarInfoInput> data;
	@Override
	public String toString() {
		return "CarInfo [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
	

}
