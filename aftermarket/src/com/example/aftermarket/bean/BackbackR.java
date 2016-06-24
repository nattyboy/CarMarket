package com.example.aftermarket.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackbackR extends Object implements Serializable {
	
	public String back_type;
	public String back_money;
	public String back_desc;
	public String back_status;
	public String back_time;
	public ArrayList<String> back_img;
	public String merchant_reason;
	public ArrayList<String> merchant_img;
	public String merchant_time;
	@Override
	public String toString() {
		return "BackbackR [back_type=" + back_type + ", back_money=" + back_money + ", back_desc=" + back_desc + ", back_status=" + back_status + ", back_time=" + back_time + ", back_img=" + back_img
				+ ", merchant_reason=" + merchant_reason + ", merchant_img=" + merchant_img + ", merchant_time=" + merchant_time + "]";
	}
	
	
	
	
}


