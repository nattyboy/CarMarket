package com.example.aftermarket.bean;

import java.io.Serializable;

public class AddressInfo implements Serializable{
	public String address_id;
	public String user_id;
	public String consignee;
	public String mobile;
	public String province;
	public String city;
	public String district;
	public String address;
	public String create_time;
	public String full_address;
	public int is_default;
	@Override
	public String toString() {
		return "AddressInfo [address_id=" + address_id + ", user_id=" + user_id + ", consignee=" + consignee
				+ ", mobile=" + mobile + ", province=" + province + ", city=" + city + ", district=" + district
				+ ", address=" + address + ", create_time=" + create_time + ", full_address=" + full_address
				+ ", is_default=" + is_default + "]";
	}
	
	
}
