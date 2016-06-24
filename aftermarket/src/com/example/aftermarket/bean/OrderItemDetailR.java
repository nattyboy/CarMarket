package com.example.aftermarket.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItemDetailR  implements Serializable{

	
	public String car_name;
	public String order_id;
	public String order_amount;
	public String pay_id;
	public String pay_status;
	public String order_status;
	public String order_content;
	public String dim_name;
	public String order_date;
	public String order_address;
	public String mobile;
	public String consignee;
	public String user_name;
	public String user_img;
	public String company_name;
	public String company_logo;
	public String order_sn;
	public String create_date;
	//public String merchant_date;
	private String evaluation_date;
	private ArrayList<Object> evaluation;
	
	//public List<Object> back;
	public BackbackR back;

	@Override
	public String toString() {
		return "OrderItemDetailR [car_name=" + car_name + ", order_id=" + order_id + ", order_amount=" + order_amount + ", pay_id=" + pay_id + ", pay_status=" + pay_status + ", order_status="
				+ order_status + ", order_content=" + order_content + ", dim_name=" + dim_name + ", order_date=" + order_date + ", order_address=" + order_address + ", mobile=" + mobile
				+ ", consignee=" + consignee + ", user_name=" + user_name + ", user_img=" + user_img + ", company_name=" + company_name + ", company_logo=" + company_logo + ", order_sn=" + order_sn
				+ ", create_date=" + create_date + ", evaluation_date=" + evaluation_date + ", evaluation=" + evaluation + ", back=" + back + "]";
	}

	
}

