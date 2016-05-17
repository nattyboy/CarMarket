package com.example.aftermarket.bean;

import java.util.List;

public class HomePageInfo {
	public int code;
	public String msg;
	@Override
	public String toString() {
		return "HomePageInfo [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
	public  InfoData data;
	public static class InfoData{
		public HomeImageinfo start;
		public List<HomeImageinfo> top;
		public List<HomeImageinfo> footer;
		public List<HomeImageinfo> question;
		public List<HomeImageinfo> notice;
		public List<HomeImageinfo> modules;
		@Override
		public String toString() {
			return "InfoData [start=" + start + ", top=" + top + ", footer="
					+ footer + ", question=" + question + ", notice=" + notice
					+ ", modules=" + modules + ", tel=" + tel + "]";
		}
		public String tel;
	}
	public static class HomeImageinfo{
		public String ad_id;
		public String ad_url;
		public String x_img;
		@Override
		public String toString() {
			return "HomeImageinfo [ad_id=" + ad_id + ", ad_url=" + ad_url
					+ ", x_img=" + x_img + ", xx_img=" + xx_img + "]";
		}
		public String xx_img;
	}

}
