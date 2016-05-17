package com.example.aftermarket.bean;

import java.io.Serializable;

public class CarInfoInput implements Serializable{

	public String car_id;
	public String user_id;
	public String category_id;
	public String carriage_number;
	public String engine_number;
	public String car_owner;
	public String car_number;
	public String buy_date;
	public String buy_money;
	public String total_mileage;
	public String category_name;
	public String img;
	public String completeness;
	@Override
	public String toString() {
		return "CarInfoInput [car_id=" + car_id + ", user_id=" + user_id
				+ ", category_id=" + category_id + ", carriage_number="
				+ carriage_number + ", engine_number=" + engine_number
				+ ", car_owner=" + car_owner + ", car_number=" + car_number
				+ ", buy_date=" + buy_date + ", buy_money=" + buy_money
				+ ", total_mileage=" + total_mileage + ", category_name="
				+ category_name + ", img=" + img + ", completeness="
				+ completeness + "]";
	}
	

}
