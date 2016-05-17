package com.example.aftermarket.bean;

import java.util.List;

public class EaseMobData {
	
	public List<EasemobDataItem> data;
	public String code;
	public String msg;
	@Override
	public String toString() {
		return "EaseMobData [data=" + data + ", code=" + code + ", msg=" + msg + "]";
	}

}
