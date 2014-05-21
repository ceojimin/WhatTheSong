package com.rainbowsoft.what_the_song.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.rainbowsoft.what_the_song.R;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongBookmark;
import com.rainbowsoft.what_the_song.menu.WhatTheSong1;
import com.rainbowsoft.what_the_song.menu.WhatTheSong10;
import com.rainbowsoft.what_the_song.menu.WhatTheSong11;
import com.rainbowsoft.what_the_song.menu.WhatTheSong12;
import com.rainbowsoft.what_the_song.menu.WhatTheSong13;
import com.rainbowsoft.what_the_song.menu.WhatTheSong14;
import com.rainbowsoft.what_the_song.menu.WhatTheSong15;
import com.rainbowsoft.what_the_song.menu.WhatTheSong16;
import com.rainbowsoft.what_the_song.menu.WhatTheSong17;
import com.rainbowsoft.what_the_song.menu.WhatTheSong18;
import com.rainbowsoft.what_the_song.menu.WhatTheSong2;
import com.rainbowsoft.what_the_song.menu.WhatTheSong3;
import com.rainbowsoft.what_the_song.menu.WhatTheSong4;
import com.rainbowsoft.what_the_song.menu.WhatTheSong5;
import com.rainbowsoft.what_the_song.menu.WhatTheSong6;
import com.rainbowsoft.what_the_song.menu.WhatTheSong7;
import com.rainbowsoft.what_the_song.menu.WhatTheSong8;
import com.rainbowsoft.what_the_song.menu.WhatTheSong9;
import com.rainbowsoft.what_the_song.setting.WhatTheSongInformation;

public class Main extends Activity implements OnClickListener {
	//private AdView adView = null;
	private static final String LOGTAG = "BannerTypeJava";

	private ViewFlipper m_viewFlipper_1, m_viewFlipper_2;
	
	ImageView Song_1, Song_2, Song_3, Song_4, Song_5, Song_6, Song_7, Song_8,
			Song_9, Song_10, Song_11, Song_12, Song_13, Song_14, Song_15,
			Song_16, Song_17, Song_18;
	//ImageView Page_1, Page_2, Page_3, Page_4, Page_5; 
	ImageView Left, Right, My_list, setting, New, Ready, Ready_2;
	//FrameLayout fl;
	private boolean mFlag = false;
	private Handler mHandler;
	private int m_nPreTouchPosX = 0;

	Timer timer;
	TimerTask timertask;

	int page = 1;
	
	/* ADLink 기본설정 
	 * ADLink 의 광고 방식은 관리자 페이지에서 설정한 방식을 따른다.
	 * 
	 *광고종류
	 * 1. shortCut 광고(업데이트형, 상시광고)
	 * 2. install 광고
	 * 
	 */
	//private AdLinkAppInfo mAdLinkAppInfo;
	//private adLinkIntent mAdLinkIntent;
	private int appKey = 86;
	private int adLinkMode = 0;
	private int titleMode = -1;
	private int marketMode = -1;
	private int adAlarm = 1;
	
	
	private int adKey =0;
	private int adFlag = 0;	// 0: intro 1:menu 2:stop
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//initAdam();

		// ADLink appInfo 설정//		
		/*mAdLinkAppInfo = new AdLinkAppInfo();
		mAdLinkAppInfo.ctx = Main.this;
		mAdLinkAppInfo.appKey = appKey; // adLink에 등록한 매체사 appKey
		mAdLinkAppInfo.mode = adLinkMode; // 0: 상용 , 1:test

		mAdLinkAppInfo.titleMode = titleMode; // -1: 관리자페이지 설정에 따름(기본값),
										// 0: 관리자모드의 레벨값 적용,
										// 1: 광고타이틀 사용

		mAdLinkAppInfo.marketMode = marketMode; // -1: 관리자페이지 설정에 따름(기본값),
										// 0 : 호출광고 shortCut 설치
										// 1 : shortCut설치후 App 마켓이동
										// 노출되는 광고는 app은 1개,
										// 웹은 1개이상 노출 되며
										// app은 마켓으로 이동 된다
		
		mAdLinkAppInfo.adAlarm = adAlarm;			//0:사용안함 1:사용

		//광고 생성 알람 광고 자동 등록.//
		mAdLinkIntent = new adLinkIntent(mAdLinkAppInfo, this);*/

		Song_1 = (ImageView) findViewById(R.id.Song_1);
		Song_1.setOnClickListener(this);
		Song_2 = (ImageView) findViewById(R.id.Song_2);
		Song_2.setOnClickListener(this);
		Song_3 = (ImageView) findViewById(R.id.Song_3);
		Song_3.setOnClickListener(this);
		Song_4 = (ImageView) findViewById(R.id.Song_4);
		Song_4.setOnClickListener(this);
		Song_5 = (ImageView) findViewById(R.id.Song_5);
		Song_5.setOnClickListener(this);
		Song_6 = (ImageView) findViewById(R.id.Song_6);
		Song_6.setOnClickListener(this);
		Song_7 = (ImageView) findViewById(R.id.Song_7);
		Song_7.setOnClickListener(this);
		Song_8 = (ImageView) findViewById(R.id.Song_8);
		Song_8.setOnClickListener(this);
		Song_9 = (ImageView) findViewById(R.id.Song_9);
		Song_9.setOnClickListener(this);
		Song_10 = (ImageView) findViewById(R.id.Song_10);
		Song_10.setOnClickListener(this);
		Song_11 = (ImageView) findViewById(R.id.Song_11);
		Song_11.setOnClickListener(this);
		Song_12 = (ImageView) findViewById(R.id.Song_12);
		Song_12.setOnClickListener(this);
		Song_13 = (ImageView) findViewById(R.id.Song_13);
		Song_13.setOnClickListener(this);
		Song_14 = (ImageView) findViewById(R.id.Song_14);
		Song_14.setOnClickListener(this);
		Song_15 = (ImageView) findViewById(R.id.Song_15);
		Song_15.setOnClickListener(this);
		Song_16 = (ImageView) findViewById(R.id.Song_16);
		Song_16.setOnClickListener(this);
		Song_17 = (ImageView) findViewById(R.id.Song_17);
		Song_17.setOnClickListener(this);
		Song_18 = (ImageView) findViewById(R.id.Song_18);
		Song_18.setOnClickListener(this);
		/*Page_1 = (ImageView) findViewById(R.id.page1);
		Page_1.setOnClickListener(this);
		Page_2 = (ImageView) findViewById(R.id.page2);
		Page_2.setOnClickListener(this);
		Page_3 = (ImageView) findViewById(R.id.page3);
		Page_3.setOnClickListener(this);
		Page_4 = (ImageView) findViewById(R.id.page4);
		Page_4.setOnClickListener(this);
		Page_5 = (ImageView) findViewById(R.id.page5);
		Page_5.setOnClickListener(this);*/


		Left = (ImageView) findViewById(R.id.left);
		Left.setOnClickListener(this);
		Right = (ImageView) findViewById(R.id.right);
		Right.setOnClickListener(this);
		My_list = (ImageView) findViewById(R.id.my_list);
		My_list.setOnClickListener(this);
		setting = (ImageView) findViewById(R.id.setting);
		setting.setOnClickListener(this);
		Ready = (ImageView) findViewById(R.id.ready_btn);
		Ready.setOnClickListener(this);
		Ready_2 = (ImageView) findViewById(R.id.ready_btn_2);
		Ready_2.setOnClickListener(this);

		m_viewFlipper_1 = (ViewFlipper) findViewById(R.id.viewflipper_1);
		m_viewFlipper_2 = (ViewFlipper) findViewById(R.id.viewflipper_2);

		Left.setVisibility(View.INVISIBLE);
		Right.setVisibility(View.VISIBLE);

		String version;

		try {
			PackageInfo i = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = i.versionName;
		} catch (NameNotFoundException e) {
			version = "";
		}

		SharedPreferences pref = getSharedPreferences("pref",
				Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
		SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다.
		editor.putString("check_version", version); // 저장할 값들을 입력합니다.
		editor.commit(); // 저장합니다.

		String check_version = pref.getString("check_version", "");
		String check_status = pref.getString("check_status", "");

		if (!check_version.equals(check_status)) {
			AlertDialog alert = new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon_small)
					.setTitle("업데이트 내역")
					.setMessage(
							"version " + version + "\n\n"
									+ this.getString(R.string.app_update))
					.setPositiveButton("다시 보지 않기",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							String version;

							try {
								PackageInfo i = getPackageManager()
										.getPackageInfo(
												getPackageName(), 0);
								version = i.versionName;
							} catch (NameNotFoundException e) {
								version = "";
							}

							SharedPreferences pref = getSharedPreferences(
									"pref", Activity.MODE_PRIVATE);
							// UI 상태를 저장합니다.
							SharedPreferences.Editor editor = pref
									.edit(); // Editor를 불러옵니다.
							editor.putString("check_status", version);
							editor.commit(); // 저장합니다.
							dialog.cancel();
						}
					}).show();
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0) {
					mFlag = false;
					Log.d("", "handleMessage mFloag : " + mFlag);
				}
			}
		};

		startTimer();
	}

	public void onDestroy() {
		super.onDestroy();
		/*if (adView != null) {
			adView.destroy();
			adView = null;
		}*/
	}

	/*private void initAdam() {
		// Ad@m sdk 초기화 시작
		adView = (AdView) findViewById(R.id.adview);
		// 광고 리스너 설정
		// 1. 광고 클릭시 실행할 리스너
		adView.setOnAdClickedListener(new OnAdClickedListener() {
			public void OnAdClicked() {
				Log.i(LOGTAG, "광고를 클릭했습니다.");
			}
		});
		// 2. 광고 내려받기 실패했을 경우에 실행할 리스너
		adView.setOnAdFailedListener(new OnAdFailedListener() {
			public void OnAdFailed(AdError error, String message) {
				Log.w(LOGTAG, message);
			}
		});
		// 3. 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		adView.setOnAdLoadedListener(new OnAdLoadedListener() {
			public void OnAdLoaded() {
				Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
			}
		});
		// 4. 광고를 불러올때 실행할 리스너
		adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
			public void OnAdWillLoad(String url) {
				Log.i(LOGTAG, "광고를 불러옵니다. : " + url);
			}
		});

		// 광고 영역에 캐시 사용 여부 : 기본 값은 true
		adView.setAdCache(false);
		// Animation 효과 : 기본 값은 AnimationType.NONE
		adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
		adView.setVisibility(View.VISIBLE);
	}*/

	public void startTimer() {
		// 핸들러 만들기
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) { // 여기에서 메인 UI를 변화 시킨다.
				
				Right.setImageResource(R.drawable.change_right_btn);
				Left.setImageResource(R.drawable.change_left_btn);
				
				final Handler handler = new Handler() {
					public void handleMessage(Message msg) { // 여기에서 메인 UI를 변화 시킨다.
						
						Right.setImageResource(R.drawable.right_btn);
						Left.setImageResource(R.drawable.left_btn);
						startTimer();
					}
				};

				timertask = new TimerTask() {
					public void run() {
						Message msg = handler.obtainMessage();
						handler.sendMessage(msg);
						
					}
				};
				//timer = new Timer();
				timer.schedule(timertask,700); // 0.5초후에 Task를 실행하고 0.5초마다 반복 해라
				/*switch (page) {
				case 1: // 첫 페이지
					Left.setVisibility(View.INVISIBLE);
					if (Right.getVisibility() == View.VISIBLE) {
						Right.setVisibility(View.INVISIBLE);
					}else if(Right.getVisibility() == View.INVISIBLE){
						Right.setVisibility(View.VISIBLE);
					}
					//Right.setImageResource(R.drawable.change_right_btn);
					//Left.setImageResource(R.drawable.change_left_btn);
					break;
				case 5: // 마지막 페이지
					Right.setVisibility(View.INVISIBLE);
					if (Left.getVisibility() == View.VISIBLE) {
						Left.setVisibility(View.INVISIBLE);
					}else if(Left.getVisibility() == View.INVISIBLE){
						Left.setVisibility(View.VISIBLE);
					}
					//Right.setImageResource(R.drawable.change_right_btn);
					//Left.setImageResource(R.drawable.change_left_btn);
					break;
				default: // 그 외 페이지
					//Left.setVisibility(View.VISIBLE);
					//Right.setVisibility(View.VISIBLE);
					if (Left.getVisibility() == View.VISIBLE && Right.getVisibility() == View.VISIBLE) {
						Left.setVisibility(View.INVISIBLE);
						Right.setVisibility(View.INVISIBLE);
					}else if(Left.getVisibility() == View.INVISIBLE && Right.getVisibility() == View.INVISIBLE){
						Left.setVisibility(View.VISIBLE);
						Right.setVisibility(View.VISIBLE);
					}
				//	Right.setImageResource(R.drawable.change_right_btn);
					//Left.setImageResource(R.drawable.change_left_btn);
				}*/
			}
		};

		timertask = new TimerTask() {
			public void run() {
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
				
			}
		};
		timer = new Timer();
		timer.schedule(timertask,700); // 0.5초후에 Task를 실행하고 0.5초마다 반복 해라
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.Song_1) {
			Intent newActivity1 = new Intent(Main.this, WhatTheSong1.class);
			Main.this.startActivity(newActivity1);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_2) {
			Intent newActivity2 = new Intent(Main.this, WhatTheSong2.class);
			Main.this.startActivity(newActivity2);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_3) {
			Intent newActivity3 = new Intent(Main.this, WhatTheSong3.class);
			Main.this.startActivity(newActivity3);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_4) {
			Intent newActivity4 = new Intent(Main.this, WhatTheSong4.class);
			Main.this.startActivity(newActivity4);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_5) {
			Intent newActivity5 = new Intent(Main.this, WhatTheSong5.class);
			Main.this.startActivity(newActivity5);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_6) {
			Intent newActivity6 = new Intent(Main.this, WhatTheSong6.class);
			Main.this.startActivity(newActivity6);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_7) {
			Intent newActivity7 = new Intent(Main.this, WhatTheSong7.class);
			Main.this.startActivity(newActivity7);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_8) {
			Intent newActivity8 = new Intent(Main.this, WhatTheSong8.class);
			Main.this.startActivity(newActivity8);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_9) {
			Intent newActivity9 = new Intent(Main.this, WhatTheSong9.class);
			Main.this.startActivity(newActivity9);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_10) {
			Intent newActivity10 = new Intent(Main.this, WhatTheSong10.class);
			Main.this.startActivity(newActivity10);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_11) {
			Intent newActivity11 = new Intent(Main.this, WhatTheSong11.class);
			Main.this.startActivity(newActivity11);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_12) {
			Intent newActivity12 = new Intent(Main.this, WhatTheSong12.class);
			Main.this.startActivity(newActivity12);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_13) {
			Intent newActivity13 = new Intent(Main.this, WhatTheSong13.class);
			Main.this.startActivity(newActivity13);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_14) {
			Intent newActivity14 = new Intent(Main.this, WhatTheSong14.class);
			Main.this.startActivity(newActivity14);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_15) {
			Intent newActivity15 = new Intent(Main.this, WhatTheSong15.class);
			Main.this.startActivity(newActivity15);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_16) {
			Intent newActivity16 = new Intent(Main.this, WhatTheSong16.class);
			Main.this.startActivity(newActivity16);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_17) {
			Intent newActivity17 = new Intent(Main.this, WhatTheSong17.class);
			Main.this.startActivity(newActivity17);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.Song_18) {
			Intent newActivity18 = new Intent(Main.this, WhatTheSong18.class);
			Main.this.startActivity(newActivity18);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.my_list) {
			Intent bookmark = new Intent(Main.this, WhatTheSongBookmark.class);
			Main.this.startActivity(bookmark);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.setting) {
			Intent setting = new Intent(Main.this, WhatTheSongInformation.class);
			Main.this.startActivity(setting);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.left) {
			MovePreviousView();
			Left.setVisibility(View.VISIBLE);
			Right.setVisibility(View.VISIBLE);
			timer.cancel();
			startTimer();
			if (page == 1) {
				Left.setVisibility(View.INVISIBLE);
				Right.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.right) {
			MoveNextView();
			Left.setVisibility(View.VISIBLE);
			Right.setVisibility(View.VISIBLE);
			timer.cancel();
			startTimer();
			if (page == 5) {
				Right.setVisibility(View.INVISIBLE);
				Left.setVisibility(View.VISIBLE);
			}
		}else if (v.getId() == R.id.ready_btn || v.getId() == R.id.ready_btn_2) {
		
			Toast.makeText(Main.this, "새로운 테마 준비중입니다!!",
					Toast.LENGTH_SHORT).show();
		}
		/*else if (v.getId() == R.id.page1) {
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_right));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_in));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_left));
			//m_viewFlipper_2.setOutAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_out));
			m_viewFlipper_1.setDisplayedChild(0);
			//m_viewFlipper_2.setDisplayedChild(0);
			
		}
		else if (v.getId() == R.id.page2) {
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_right));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_out));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_left));
			//m_viewFlipper_2.setOutAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_in));			
			m_viewFlipper_1.setDisplayedChild(1);
			//m_viewFlipper_2.setDisplayedChild(1);
		}
		else if (v.getId() == R.id.page3) {
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_right));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_out));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_left));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_in));
			m_viewFlipper_1.setDisplayedChild(2);
			//m_viewFlipper_2.setDisplayedChild(2);
		}
		else if (v.getId() == R.id.page4) {
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_right));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_out));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_left));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_in));
			m_viewFlipper_1.setDisplayedChild(3);
			//m_viewFlipper_2.setDisplayedChild(3);
		}
		else if (v.getId() == R.id.page5) {
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_right));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_out));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_left));
			//m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			//		android.R.anim.fade_in));
			m_viewFlipper_1.setDisplayedChild(4);
			//m_viewFlipper_2.setDisplayedChild(4);
		}*/
	}

	private void MoveNextView() {
		if (m_viewFlipper_1.getDisplayedChild() != 4) {
			page++;
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_right));
			 m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			 android.R.anim.fade_in));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_left));
			 m_viewFlipper_2.setOutAnimation(AnimationUtils.loadAnimation(this,
			 android.R.anim.fade_out));
			m_viewFlipper_1.showNext();
			m_viewFlipper_2.showNext();
		} else if (m_viewFlipper_1.getDisplayedChild() == 4) {
			new AlertDialog.Builder(Main.this)
			.setMessage("마지막 페이지입니다.")
			.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
						}
					}).show();
			}
	}

	private void MovePreviousView() {
		if (m_viewFlipper_1.getDisplayedChild() != 0) {
			page--;
			m_viewFlipper_1.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.appear_from_left));
			 m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			 android.R.anim.fade_out));
			m_viewFlipper_1.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.disappear_to_right));
			 m_viewFlipper_2.setInAnimation(AnimationUtils.loadAnimation(this,
			 android.R.anim.fade_in));
			m_viewFlipper_1.showPrevious();
			m_viewFlipper_2.showPrevious();
		} else if (m_viewFlipper_1.getDisplayedChild() == 0) {
			new AlertDialog.Builder(Main.this)
			.setMessage("첫 번째 페이지입니다.")
			.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
						}
					}).show();
			}
	}
	
	View.OnTouchListener MyTouchListener = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				m_nPreTouchPosX = (int) event.getX();
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				int nTouchPosX = (int) event.getX();

				if (nTouchPosX < m_nPreTouchPosX) {
					MoveNextView();
				} else if (nTouchPosX > m_nPreTouchPosX) {
					MovePreviousView();
				}
				m_nPreTouchPosX = nTouchPosX;
			}
			return true;
		}
	};

	public void onBackPressed() {
		if (!mFlag) {
			Toast.makeText(Main.this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
					Toast.LENGTH_SHORT).show();
			mFlag = true;
			mHandler.sendEmptyMessageDelayed(0, 1000 * 2);
		} else {
			Log.i("Start", "adLinkStart");
			/* 광고 요청 */
			adFlag = 1;
			//mAdLinkIntent.adLinkStart();
		}
	}

	/*
	 * adLinkStart 후 광고 수신 결과를 return 해주는 메소드
	 * 
	 * result : 0 정상적으로 광고 수신 이외의 값은 통합연동가이드 참조
	 */
	/*@Override
	public void OnStartAdLinkResult(int result) {
		// TODO Auto-generated method stub
		Log.d("OnStrartAdLinkResult", "광고서버로부터의 응답 : " + result);
		if (result != 0) {
			// 작업을 수행할 코딩을 한다...
			// Toast.makeText(this, "ERROR : " + result,
			// Toast.LENGTH_SHORT).show();
			adFlag = 2;
			finish();
		}
	}*/
	
	/*
	 * 업데이트 광고, 상시광고만 해당.
	 * 
	 * shortCut 설치 후 설치 결과를 return 해주는 메소드
	 * 
	 * *상시광고의 경우 adLv 의미 -1 : 설치는 되었으나 서버에 전송실패 0 : 미설치 1~5: 해당광고의 Level 값
	 * -------------------------------- *업데이트광고의 경우 adLv 의미 -1 : 설치는 되었으나 서버에
	 * 전송실패 0 : 미설치 1 : 설치
	 */
	/*@Override
	public void onClickShortCutDialog_Ok(int[] adLv) {
		// TODO Auto-generated method stub
		// debug---------------------------------------------
				Log.d("OK", "확인버튼이 클릭 되었습니다.");				
				
				int successCount = 0;
				int unCheckCount = 0;
				int errCount = 0;
				String debugStr = "";
				adFlag = 2;
				
				for (int i=0; i<adLv.length; i++) {
					if(adLv[i] > 0)	successCount++;				
					else if(adLv[i] == 0)	unCheckCount++;
					else	errCount++;
					
					debugStr= debugStr+"adLv["+i+"] :" + adLv[i] + ", "; 
						
				}
				
				Log.d("adLv", "노출광고 수 : " + adLv.length);
				Log.d("adLv", "광고 설치 성공 수  : " + successCount);
				Log.d("adLv", "광고 미 설치  수 : " + unCheckCount);
				Log.d("adLv", "광고 설치 에러 수 : " + errCount);
				
				Log.d("adLv", "결과 : " + debugStr);
				//---------------------------------------------------------
				
				//작업을 수행할 코딩을 한다...-------
				if(adFlag == 2)
					finish();
				//--------------------------------
	}*/
	
	/*
	 * 업데이트 광고, 상시광고만 해당.
	 * 
	 * 취소버튼 클릭 이벤트를 return 해주는 메소드..
	 */
	/*@Override
	public void onClickShortCutDialog_Cancel() {
		// TODO Auto-generated method stub
		Log.d("cancel", "취소버튼이 클릭 되었습니다.");
		adFlag = 2;
		finish();
	}*/

	/*@Override
	public void onClickInstallDialog_download(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onInstallStateResult(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnIsAlarmActivated(boolean arg0) {
		// TODO Auto-generated method stub
		Log.d("OnIsAlarmActivated", "OnIsAlarmActivated : " + arg0);
	}*/
}