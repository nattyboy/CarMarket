/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.aftermarket;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.example.aftermarket.bean.UserInfo;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class DemoApplication extends Application {

	public static Context applicationContext;
	private static DemoApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	public static ArrayList<Activity> activities = new ArrayList<>();

	public static Object data = null;

	public static Object getData() {
		return data;
	}

	public static void setData(Object data) {
		DemoApplication.data = data;
	}

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	private static String token;
	private static String ease_user;
	private static String ease_pwd;
	private static UserInfo userInfo;
	private static String mCurrentLantitude;
	private static String mCurrentLongitude;

	public static String getmCurrentLantitude() {
		return mCurrentLantitude;
	}

	public static void setmCurrentLantitude(String mCurrentLantitude) {
		DemoApplication.mCurrentLantitude = mCurrentLantitude;
	}

	public static String getmCurrentLongitude() {
		return mCurrentLongitude;
	}

	public static void setmCurrentLongitude(String mCurrentLongitude) {
		DemoApplication.mCurrentLongitude = mCurrentLongitude;
	}

	public static UserInfo getUserInfo() {
		return userInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		DemoApplication.userInfo = userInfo;
	}

	public static String getEase_user() {
		return ease_user;
	}

	public static void setEase_user(String ease_user) {
		DemoApplication.ease_user = ease_user;
	}

	public static String getEase_pwd() {
		return ease_pwd;
	}

	public static void setEase_pwd(String ease_pwd) {
		DemoApplication.ease_pwd = ease_pwd;
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		DemoApplication.token = token;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;

		// TODO Auto-generated method stub
		// 给ImageLoder配置参数
		DisplayImageOptions options = new DisplayImageOptions.Builder()

		.cacheInMemory(true).cacheOnDisc(true)

		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)

		.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置

				// 如Bitmap.Config.ARGB_8888

				.showImageOnLoading(R.drawable.empty_photo) // 默认图片

				.showImageForEmptyUri(R.drawable.empty_photo) // url爲空會显示该图片，自己放在drawable里面的

				.showImageOnFail(R.drawable.empty_photo)// 加载失败显示的图片

				// .displayer(new RoundedBitmapDisplayer(5)) //圆角，不需要请删除

				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(

		this)

		.memoryCacheExtraOptions(480, 800)// 缓存在内存的图片的宽和高度

				.discCacheExtraOptions(480, 800, null) // CompressFormat.PNG类型，70质量（0-100）

				.memoryCache(new WeakMemoryCache())

				.memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据

				.discCacheSize(50 * 1024 * 1024). // 缓存到文件的最大数据

				discCacheFileCount(1000) // 文件数量

				.defaultDisplayImageOptions(options). // 上面的options对象，一些属性配置

				build();

		ImageLoader.getInstance().init(config); // 初始化

		/**
		 * this function will initialize the HuanXin SDK
		 * 
		 * @return boolean true if caller can continue to call HuanXin related
		 *         APIs after calling onInit, otherwise false.
		 * 
		 *         环信初始化SDK帮助函数
		 *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
		 * 
		 *         for example: 例子：
		 * 
		 *         public class DemoHXSDKHelper extends HXSDKHelper
		 * 
		 *         HXHelper = new DemoHXSDKHelper();
		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
		 */
		hxSDKHelper.onInit(applicationContext);
	}

	public static DemoApplication getInstance() {
		return instance;
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}

}
