package com.example.aftermarket.ui;

import com.example.aftermarket.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShopSplashActivity extends Activity {
	private static final int sleepTime = 3000;

	@Override
	protected void onCreate(Bundle arg0) {
		final View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		super.onCreate(arg0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
				startActivity(new Intent(ShopSplashActivity.this, HomeActivity.class));
				finish();
			}
		}).start();

	}

}
