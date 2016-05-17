package com.example.aftermarket.bean;

import java.util.List;

public class EvaluteDataItem {
	
	public String  score_id;
	public String  merchant_id;
	public String  score_content;
	public String  score;
	public String  attach_file;
	public String create_user;
	public String  create_date;
	public String  parent_id;
	public String  order_id;
	public String  mobile;
	public String  user_name;
	public List<String> img;
	@Override
	public String toString() {
		return "EvaluteDataItem [score_id=" + score_id + ", merchant_id=" + merchant_id + ", score_content=" + score_content + ", score=" + score + ", attach_file=" + attach_file + ", create_user=" + create_user + ", create_date=" + create_date + ", parent_id=" + parent_id + ", order_id=" + order_id + ", mobile=" + mobile + ", user_name=" + user_name + ", img=" + img + "]";
	}
	

}
