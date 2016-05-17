package com.example.aftermarket.bean;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLoginInfo {

	private String USER_INFO = "userInfo";

	private Context context;
	
	public UserLoginInfo() {
	}

	public UserLoginInfo(Context context) {

		this.context = context;
	}
	// ����ַ����͵�ֵ
	public void setUserInfo(String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.putString(key, value);
		editor.commit();
	}

	// ������ε�ֵ
	public void setUserInfo(String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.putInt(key, value);
		editor.commit();
	}

	// ��ų�����ֵ
	public void setUserInfo(String key, Long value) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.putLong(key, value);
		editor.commit();
	}

	// ��Ų�����ֵ
	public void setUserInfo(String key, Boolean value) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.putBoolean(key, value);
		editor.commit();
	}

	// ��ռ�¼
	public void clear() {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	// ע���û�ʱ����û���������
	// public void logOut() {
	// SharedPreferences sp = context.getSharedPreferences(USER_INFO,
	// Context.MODE_PRIVATE);
	// SharedPreferences.Editor editor = sp.edit();
	// editor.remove(ACCOUNT);
	// editor.remove(PASSWORD);
	// editor.commit();
	// }

	// ����û���Ϣ��ĳ���ַ����͵�ֵ
	public String getStringInfo(String key) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	// ����û�Ϣ��ĳ�����β�����ֵ
	public int getIntInfo(String key) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		return sp.getInt(key, -1);
	}

	// ����û���Ϣ��ĳ����β�����ֵ
	public Long getLongInfo(String key) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		return sp.getLong(key, -1);
	}

	// ����û���Ϣ��ĳ����Ͳ�����ֵ
	public boolean getBooleanInfo(String key) {
		SharedPreferences sp = context.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

}
