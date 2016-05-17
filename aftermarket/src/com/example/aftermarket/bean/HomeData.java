package com.example.aftermarket.bean;

import java.util.List;

public class HomeData {
	
	public StartItem start;
	public List<TopItem> top;
	public List<FooterItem> footer;
	public List<QuestionItem> question;
	public List<NoticeItem> notice;
	public List<ModulesItem> modules;
	public String tel;
	@Override
	public String toString() {
		return "HomeData [start=" + start + ", top=" + top + ", footer=" + footer + ", question=" + question
				+ ", notice=" + notice + ", modules=" + modules + ", tel=" + tel + "]";
	}
	

}
