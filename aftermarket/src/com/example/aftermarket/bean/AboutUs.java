package com.example.aftermarket.bean;

public class AboutUs {
	
	public int code;
	public String msg;
	public Data data;
	
	public class Data{
		public String logo;
		public String content;
		public String footer;
		@Override
		public String toString() {
			return "Data [logo=" + logo + ", content=" + content + ", footer=" + footer + "]";
		}
		
	}

	@Override
	public String toString() {
		return "AboutUs [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}


