package com.rainbowsoft.what_the_song.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rainbowsoft.what_the_song.R;

public class WhatTheSongInformation extends Activity {
	public final static String INFORMATION_TITLE = "title";
	public final static String INFORMATION_DETAIL = "detail";
	String version = "";

	// Recommened Charset UTF-8
	private String encoding = "UTF-8";

	// SectionHeaders
	private final static String[] header = new String[] { "기본정보", "친구추천",
			"노래, 테마, 기타 제안 및 건의", "레인보우소프트의 다른 앱" };

	// Section Contents
	private String[] basis;
	private String[] recommendation;
	private String[] proposal;
	private String[] rainbowsoftapp;

	// Adapter for ListView Contents
	private SeparatedListAdapter adapter;

	// ListView Contentsprivate
	ListView information_listview;

	public void getVersion() {
		try {
			final PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = info.versionName;
		} catch (NameNotFoundException e) {
		}
	}

	public Map<String, ?> createItem(String title, String detail) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(INFORMATION_TITLE, title);
		item.put(INFORMATION_DETAIL, detail);
		return item;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Sets the View Layer
		setContentView(R.layout.information);

		getVersion();

		basis = new String[] { "어플리케이션 소개", "현재 버전 :		" + version };
		recommendation = new String[] { "카카오톡" };
		proposal = new String[] { "홈페이지", "이메일" };
		rainbowsoftapp = new String[] { "보이스매치" };

		// Create the ListView Adapter
		adapter = new SeparatedListAdapter(this);

		// ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,
		// R.layout.list_information, notes);

		adapter.addSection(header[0], new ArrayAdapter<String>(this,
				R.layout.list_information, basis));
		adapter.addSection(header[1], new ArrayAdapter<String>(this,
				R.layout.list_information, recommendation));
		adapter.addSection(header[2], new ArrayAdapter<String>(this,
				R.layout.list_information, proposal));
		adapter.addSection(header[3], new ArrayAdapter<String>(this,
				R.layout.list_information, rainbowsoftapp));

		// Add Sections
		/*
		 * for (int i = 0; i < header.length; i++) {
		 * adapter.addSection(header[i], listadapter); }
		 */

		// Get a reference to the ListView holder
		information_listview = (ListView) this
				.findViewById(R.id.information_list);

		// Set the adapter on the ListView holder
		information_listview.setAdapter(adapter);

		// Listen for Click events
		information_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				String packageName = "project.voicematch";
				final Intent homepage = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.facebook.com/rainbowsoft.co"));
				Intent mail = new Intent(Intent.ACTION_SEND);
				mail.setType("text/email");
				mail.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { "ceojimin@rainbowsoft.net" });
				final Intent app = new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=project.voicematch"));
				Intent appintent = WhatTheSongInformation.this
						.getPackageManager().getLaunchIntentForPackage(
								packageName);

				if (position == 1) { // 어플리케이션 소개
					Intent newActivity = new Intent(
							WhatTheSongInformation.this,
							WhatTheSongInformation_intro.class);
					WhatTheSongInformation.this.startActivity(newActivity);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);

				} else if (position == 2) { // 버전정보

				} else if (position == 4) { // 카카오톡 친구추천
					try {
						sendAppData(view);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (position == 6) { // 홈페이지 이동
					startActivity(homepage);
				} else if (position == 7) { // 이메일 전송
					startActivity(mail);
				} else if (position == 9) { // 보이스매치 다운
					// List<PackageInfo> appinfo = getPackageManager()
					// .getInstalledPackages(PackageManager.GET_ACTIVITIES);
					Boolean tf = false;

					try {
						PackageInfo pi = getPackageManager().getPackageInfo(
								packageName, 0);
						if (pi != null) {
							tf = true;
							startActivity(appintent);
						}
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					if (tf == false) {
						startActivity(app);
					}
				}
			}
		});
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	public void sendAppData(View v) throws NameNotFoundException {
		ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();

		// If application is support Android platform.
		Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
		metaInfoAndroid.put("os", "android");
		metaInfoAndroid.put("devicetype", "phone");
		metaInfoAndroid.put("installurl",
				"market://details?id=com.rainbowsoft.what_the_song");
		metaInfoAndroid.put("executeurl", "WhatTheSong://starActivity");

		// If application is support ios platform.
		Map<String, String> metaInfoIOS = new Hashtable<String, String>(1);
		metaInfoIOS.put("os", "ios");
		metaInfoIOS.put("devicetype", "phone");
		metaInfoIOS.put("installurl", "your iOS app install url");
		metaInfoIOS.put("executeurl", "kakaoLinkTest://starActivity");

		// add to array
		metaInfoArray.add(metaInfoAndroid);
		metaInfoArray.add(metaInfoIOS);

		// Recommended: Use application context for parameter.
		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!kakaoLink.isAvailableIntent()) {
			alert("Not installed KakaoTalk.");
			return;
		}

		/**
		 * @param activity
		 * @param url
		 * @param message
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 * @param metaInfoArray
		 */
		kakaoLink
				.openKakaoAppLink(
						this,
						"http://link.kakao.com/?test-android-app",
						"노래방에서 무슨 노래를 불러야 할 지 생각이 안 날 때, 지금 상황에 딱 어울리는 노래가 떠오르지 않을 때 찾아주세요!",
						getPackageName(),
						getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
						"What The Song (음표)", encoding, metaInfoArray);
	}

	private void alert(String message) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.app_name).setMessage(message)
				.setPositiveButton(android.R.string.ok, null).create().show();
	}
}