package com.rainbowsoft.what_the_song.setting;

import com.rainbowsoft.what_the_song.R;

import android.app.Activity;
import android.os.Bundle;

public class WhatTheSongInformation_intro extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_intro);
	}
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}
}
