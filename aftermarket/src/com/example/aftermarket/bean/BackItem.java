package com.example.aftermarket.bean;

import java.util.List;

public class BackItem {
	
	public String back_type;
	public String back_money;
	public String back_desc;
	public String back_status;
	public String back_time;
	public List<String> back_img;
	@Override
	public String toString() {
		return "Back [back_type=" + back_type + ", back_money=" + back_money + ", back_desc=" + back_desc + ", back_status=" + back_status + ", back_time=" + back_time + ", back_img=" + back_img + "]";
	}

}
