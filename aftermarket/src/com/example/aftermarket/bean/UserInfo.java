package com.example.aftermarket.bean;

public class UserInfo {
	
	public String user_id;
	public String mobile;
	public String user_name;
	public String sex;
	public String collect;
	public String user_img;
	public String ease_user;
	public String ease_pwd;
	public String address_id;
	public AddressItem address;
	public String token;
	public String balance;
	@Override
	public String toString() {
		return "UserInfo [user_id=" + user_id + ", mobile=" + mobile + ", user_name=" + user_name + ", sex=" + sex + ", collect=" + collect + ", user_img=" + user_img + ", ease_user=" + ease_user
				+ ", ease_pwd=" + ease_pwd + ", address_id=" + address_id + ", address=" + address + ", token=" + token + ", balance=" + balance + "]";
	}
	
	
}

