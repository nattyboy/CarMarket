package com.example.aftermarket.bean;

import java.util.List;

public class CarHeader {
	public String header;
	public List <CarCategory> children;
	@Override
	public String toString() {
		return "CarHeader [header=" + header + ", children=" + children + "]";
	}
	

}
