package com.example.aftermarket.bean;

import java.util.List;

public class SellerInfo {
	public String merchant_id;
	public String company_name;
	public String company_desc;
	public String address;
	public String contact_user;
	public String contact_mobile;
	public String user_id;
	public String company_logo;
	public String store_img;
	public String on_time;
	public String tel;
	public String balance;
	public String margin;
	public String enable;
	public float score;
	public String evaluation_num;
	public String volume;
	public String is_approve;
	public String merchant_type;
	public String create_time;
	public String update_time;
	public String business_approve;
	public String lat;
	public String lng;
	public String distance;
	public int is_collect;
	public String easemob_user;
	public List<ServiceItem> door;
	public List<ServiceItem> shop;
	@Override
	public String toString() {
		return "SellerInfo [merchant_id=" + merchant_id + ", company_name=" + company_name + ", company_desc="
				+ company_desc + ", address=" + address + ", contact_user=" + contact_user + ", contact_mobile="
				+ contact_mobile + ", user_id=" + user_id + ", company_logo=" + company_logo + ", store_img="
				+ store_img + ", on_time=" + on_time + ", tel=" + tel + ", balance=" + balance + ", margin=" + margin
				+ ", enable=" + enable + ", score=" + score + ", evaluation_num=" + evaluation_num + ", volume="
				+ volume + ", is_approve=" + is_approve + ", merchant_type=" + merchant_type + ", create_time="
				+ create_time + ", update_time=" + update_time + ", business_approve=" + business_approve + ", lat="
				+ lat + ", lng=" + lng + ", distance=" + distance + ", is_collect=" + is_collect + ", easemob_user="
				+ easemob_user + ", door=" + door + ", shop=" + shop + "]";
	}
	
}
