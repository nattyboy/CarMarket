package com.example.aftermarket.bean;

import java.io.Serializable;

public class BusinessItem implements Serializable {
	public String dim_id;
	public String dim_name;
	public String dim_desc;
	public String cost;

	@Override
	public String toString() {
		return "BuynessItem [dim_id=" + dim_id + ", dim_name=" + dim_name + ", dim_desc=" + dim_desc + ", cost=" + cost
				+ "]";
	}

}