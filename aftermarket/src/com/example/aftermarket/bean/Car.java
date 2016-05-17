package com.example.aftermarket.bean;

import java.util.List;

public class Car {
	public int code;
	public String msg;
	public List<CarHeader> data;
	@Override
	public String toString() {
		return "Car [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}

	
/**
 * 首先遍历字母A的，取得其size,然后再将其Ascii码的值加1，继续遍历。添加到hashMap的数组中。然后按照字母排序。
 */
