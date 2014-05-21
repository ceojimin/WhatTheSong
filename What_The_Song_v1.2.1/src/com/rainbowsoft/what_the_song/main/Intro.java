package com.rainbowsoft.what_the_song.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.rainbowsoft.what_the_song.R;

public class Intro extends Activity {
	private boolean mFlag = false;
	private Handler mHandler;

	/*
	 * ADLink 기본설정 ADLink 의 광고 방식은 관리자 페이지에서 설정한 방식을 따른다.
	 * 
	 * 광고종류 1. shortCut 광고(업데이트형, 상시광고) 2. install 광고
	 */
	//private AdLinkAppInfo mAdLinkAppInfo;
	//private adLinkIntent mAdLinkIntent;
	private int appKey = 86;
	private int adLinkMode = 0;
	private int titleMode = -1;
	private int marketMode = -1;
	private int adAlarm = 1;

	private int adKey = 0;
	private int adFlag = 0; // 0: intro 1:menu 2:stop

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
/*
		// ADLink appInfo 설정 //
		mAdLinkAppInfo = new AdLinkAppInfo();
		mAdLinkAppInfo.ctx = Intro.this;
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

		mAdLinkAppInfo.adAlarm = adAlarm; // 0:사용안함 1:사용

		//광고 생성 알람 광고 자동 등록.
		mAdLinkIntent = new adLinkIntent(mAdLinkAppInfo, this);*/

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
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Intent intent = new Intent(Intro.this, Main.class);
			startActivity(intent);

			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			finish();
			return true;
		}
		return false;
	}

	public void onBackPressed() {
		if (!mFlag) {
			Toast.makeText(Intro.this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
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
