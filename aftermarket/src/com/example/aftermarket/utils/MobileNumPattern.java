package com.example.aftermarket.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNumPattern {

	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		System.out.println(m.matches() + "---");

		return m.matches();
	}

	public static boolean isPassWordNO(String passWord) {

		Pattern p = Pattern.compile("^[^\u4e00-\u9fa5]{0,}$");

		Matcher m = p.matcher(passWord);

		if (m.matches()) {

			p = Pattern.compile("^[^\\s]{5,20}$");
			m = p.matcher(passWord);
		}

		return m.matches();

	}
}
