package com.rainbowsoft.what_the_song.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rainbowsoft.what_the_song.R;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongBookmark;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongMyDBHelper;

public class WhatTheSong18 extends ListActivity implements OnClickListener {
	private static final int DATABASE_VERSION = 2;
	private static final int LISTSIZE = 30;
	//private AdView adView = null;
	private static final String LOGTAG = "BannerTypeJava";
	ImageView My_list, Refresh, Search, manual;
	EditText SearchText;
	SQLiteDatabase db, mdb;
	Map<String, String> data;
	List<Map<String, String>> myList;
	SimpleAdapter adapter;
	Random rand = new Random();
	int count = 0;
	int[] randid = new int[LISTSIZE];

	Cursor cursor;
	public static String searchUrl = "http://m.youtube.com/#/results?q=";

	String[] song;
	String[] singer;
	String[] tj;
	String[] ky;
	String[] participant;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.what_the_song_18);

		//initAdam();

		My_list = (ImageView) findViewById(R.id.my_list);
		My_list.setOnClickListener(this);
		Refresh = (ImageView) findViewById(R.id.refresh);
		Refresh.setOnClickListener(this);
		Search = (ImageView) findViewById(R.id.search);
		Search.setOnClickListener(this);
		SearchText = (EditText) findViewById(R.id.searchText);
		manual = (ImageView) findViewById(R.id.manual);
		manual.setOnClickListener(this);

		callList();
		
		String version;

		try {
			PackageInfo i = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = i.versionName;
		} catch (NameNotFoundException e) {
			version = "";
		}

		SharedPreferences m_pref = getSharedPreferences("m_pref_18",
				Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
		SharedPreferences.Editor editor = m_pref.edit(); // Editor를 불러옵니다.
		editor.putString("check_version", version); // 저장할 값들을 입력합니다.
		editor.commit(); // 저장합니다.

		String check_version = m_pref.getString("check_version", "");
		String check_status = m_pref.getString("check_status", "");

		if (!check_version.equals(check_status)) {
			manual.setVisibility(View.VISIBLE);
		} else{
			manual.setVisibility(View.INVISIBLE);
		}
		
		SearchText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						Search.performClick();
					}
					return true;
				}
				return false;
			}
		});
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
	
	public void callSearchList() {
		song = new String[cursor.getCount()];
		singer = new String[cursor.getCount()];
		tj = new String[cursor.getCount()];
		ky = new String[cursor.getCount()];
		participant = new String[cursor.getCount()];

		myList = new ArrayList<Map<String, String>>();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		String edit = SearchText.getText().toString();
		cursor = db.rawQuery("SELECT * FROM SongTable18 WHERE song LIKE '"
				+ edit + "%' OR song LIKE '%" + edit +"%' OR song LIKE '%" + edit + "' OR singer LIKE '"
				+ edit + "%' OR singer LIKE '%" + edit +"%' OR singer LIKE '%" + edit + "' OR participant LIKE '"
				+ edit + "%' OR participant LIKE '%" + edit +"%' OR participant LIKE '%" + edit + "' ORDER BY song ASC", null);
		cursor.moveToFirst();

		participant = new String[cursor.getCount()];
		song = new String[cursor.getCount()];
		singer = new String[cursor.getCount()];
		tj = new String[cursor.getCount()];
		ky = new String[cursor.getCount()];

		for (int i = 0; i < cursor.getCount(); i++) {
			participant[i] = cursor.getString(1);
			song[i] = cursor.getString(2);
			singer[i] = cursor.getString(3);
			tj[i] = cursor.getString(4);
			ky[i] = cursor.getString(5);

			data = new HashMap<String, String>();
			data.put("song", song[i]);
			data.put("singer", singer[i]);
			myList.add(data);
			Log.i("db", cursor.getString(2) + " : " + cursor.getString(3));
			cursor.moveToNext();
		}
		Toast.makeText(this, cursor.getCount() + "개의 검색 결과가 있습니다.",
				Toast.LENGTH_SHORT).show();
	}

	public void callList() {
		int[] randid = new int[LISTSIZE];
		song = new String[LISTSIZE];
		singer = new String[LISTSIZE];
		tj = new String[LISTSIZE];
		ky = new String[LISTSIZE];
		participant = new String[LISTSIZE];
		
		int count = 0;
		myList = new ArrayList<Map<String, String>>();

		WhatTheSongDBHelper18 wtsHelper18 = new WhatTheSongDBHelper18(this, "whatthesong18.db", null, DATABASE_VERSION);
		db = wtsHelper18.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable18 ", null);
			int r = rand.nextInt(cursor.getCount()) + 1;
			for (int i = 0; i < randid.length; i++) {
				if (randid[i] == r) {
					tf = false;
					break;
				}
			}
			if (tf) {
				randid[count++] = r;
			}
		}

		for (int i = 0; i < randid.length; i++) {
			cursor = db.rawQuery("SELECT * FROM SongTable18 where id= '"
					+ randid[i] + "'", null);
			cursor.moveToFirst();

			participant[i] = cursor.getString(1);
			song[i] = cursor.getString(2);
			singer[i] = cursor.getString(3);
			tj[i] = cursor.getString(4);
			ky[i] = cursor.getString(5);

			data = new HashMap<String, String>();
			data.put("song", song[i]);
			data.put("singer", singer[i]);
			myList.add(data);
			Log.i("db", song[i] + " : " + singer[i]);
		}
	}

	@Override
	// 클릭 시
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final int p = position;
		new AlertDialog.Builder(WhatTheSong18.this)
				.setTitle(song[p] + " - " + singer[p])
				.setIcon(R.drawable.icon_small)
				.setMessage(
						"♪ 부른사람 : " + participant[p] + "\n" + "♪ 태진 : " + tj[p]
								+ ", ♪ 금영 : " + ky[p])
				.setPositiveButton("즐겨찾기 추가",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								cursor = mdb.rawQuery(
										"SELECT * FROM mysongTable", null);
								int n = cursor.getCount();
								cursor = mdb.rawQuery(
										"SELECT * FROM mysongTable WHERE song = '"
												+ song[p] + "' AND singer = '"
												+ singer[p] + "'", null);
								if (cursor.getCount() == 0) {
									mdb.execSQL("insert into mysongTable values('"
											+ n
											+ 1
											+ "', '"
											+ song[p]
											+ "', '"
											+ singer[p]
											+ "', '"
											+ tj[p]
											+ "', '" + ky[p] + "' );");
									Toast.makeText(
											WhatTheSong18.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong18.this,
											"이미 즐겨찾기에 등록되어 있는 곡입니다.",
											Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNeutralButton("들어보기",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								final Intent search = new Intent(
										Intent.ACTION_VIEW, Uri.parse(searchUrl
												+ song[p] + " - " + participant[p]));
								startActivity(search);
							}
						})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		db.close();
		mdb.close();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.my_list) {
			Intent newActivity = new Intent(WhatTheSong18.this,
					WhatTheSongBookmark.class);
			WhatTheSong18.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
		}else if (v.getId() == R.id.search) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
			String edit = SearchText.getText().toString();
			cursor = db.rawQuery("SELECT * FROM SongTable18 WHERE song LIKE '"
					+ edit + "%' OR song LIKE '%" + edit +"%' OR song LIKE '%" + edit + "' OR singer LIKE '"
					+ edit + "%' OR singer LIKE '%" + edit +"%' OR singer LIKE '%" + edit + "' OR participant LIKE '"
					+ edit + "%' OR participant LIKE '%" + edit +"%' OR participant LIKE '%" + edit + "'", null);
			if (cursor.getCount() == 0) {
				Toast.makeText(WhatTheSong18.this, "검색 결과가 없습니다.",
						Toast.LENGTH_SHORT).show();
			} else {
				callSearchList();
			}
		} else if (v.getId() == R.id.manual) {
			String version;

			try {
				PackageInfo i = getPackageManager()
						.getPackageInfo(
								getPackageName(), 0);
				version = i.versionName;
			} catch (NameNotFoundException e) {
				version = "";
			}

			SharedPreferences m_pref = getSharedPreferences(
					"m_pref_18", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}	
	}
}

class WhatTheSongDBHelper18 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper18(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '보이스코리아 테마' DB (130627 : 231개)
		db.execSQL("CREATE TABLE SongTable18 ( id INTEGER ," + " participant TEXT ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable18 values(1,	'사필성',	'비가오나 눈이 오나',	'하동균',	'19290',	'85735');");
		db.execSQL("insert into SongTable18 values(2,	'이시몬',	'이별',	'패티김',	'553',	'632');");
		db.execSQL("insert into SongTable18 values(3,	'김민석',	'눈물이 뚝뚝',	'케이윌',	'30994',	'46621');");
		db.execSQL("insert into SongTable18 values(4,	'박영섭',	'소나무',	'바비 킴',	'17178',	'85369');");
		db.execSQL("insert into SongTable18 values(5,	'김우현',	'뻐꾸기 둥지위로 날아간 새',	'김건모',	'4273',	'5221');");
		db.execSQL("insert into SongTable18 values(6,	'이예준',	'가수가 된 이유',	'신용재',	'35668',	'77358');");
		db.execSQL("insert into SongTable18 values(7,	'박의성',	'씨스루',	'프라이머리',	'35302',	'77251');");
		db.execSQL("insert into SongTable18 values(8,	'신유미',	'제발',	'들국화',	'3414',	'65612');");
		db.execSQL("insert into SongTable18 values(9,	'윤성기',	'날 울리지마',	'신승훈',	'715',	'1039');");
		db.execSQL("insert into SongTable18 values(10,	'송수빈',	'푸른 칵테일의 향기',	'한영애',	'3619',	'7666');");
		db.execSQL("insert into SongTable18 values(11,	'이정석',	'1178',	'YB',	'16200',	'45635');");
		db.execSQL("insert into SongTable18 values(12,	'김대건',	'난치병',	'하림',	'5865',	'9018');");
		db.execSQL("insert into SongTable18 values(13,	'김은지',	'주문',	'동방신기',	'30189',	'46439');");
		db.execSQL("insert into SongTable18 values(14,	'유다은',	'짝사랑',	'Gigs',	'9269',	'6578');");
		db.execSQL("insert into SongTable18 values(15,	'박철수',	'불놀이야',	'옥슨 80',	'1298',	'2393');");
		db.execSQL("insert into SongTable18 values(16,	'배두훈',	'피우든 마시든',	'김범수',	'36618',	'77600');");
		db.execSQL("insert into SongTable18 values(17,	'김현지',	'Rush',	'리쌍',	'6086',	'9009');");
		db.execSQL("insert into SongTable18 values(18,	'이병현',	'싸구려커피',	'장기하와 얼굴들',	'30296',	'83864');");
		db.execSQL("insert into SongTable18 values(19,	'김현수',	'전부 너였다',	'노을',	'15695',	'45485');");
		db.execSQL("insert into SongTable18 values(20,	'황성재',	'죽고 싶단 말 밖에',	'허각',	'34620',	'77094');");
		db.execSQL("insert into SongTable18 values(21,	'조재일',	'편지',	'어니언스',	'106',	'751');");
		db.execSQL("insert into SongTable18 values(22,	'박수경',	'핑핑글',	'알리',	'정보없음',	'87110');");
		db.execSQL("insert into SongTable18 values(23,	'김남훈',	'Roly-Poly',	'티아라',	'34109',	'47429');");
		db.execSQL("insert into SongTable18 values(24,	'장준수',	'Elnino Prodigo',	'윈디시티',	'15236',	'정보없음');");
		db.execSQL("insert into SongTable18 values(25,	'김은혜',	'Hey Guyz',	'자우림',	'10058',	'9048');");
		db.execSQL("insert into SongTable18 values(26,	'이시내',	'Inside My Heart',	'김정은',	'정보없음',	'64782');");
		db.execSQL("insert into SongTable18 values(27,	'장우수',	'사랑한다는 흔한 말',	'김연우',	'15624',	'45468');");
		db.execSQL("insert into SongTable18 values(28,	'송푸름',	'어떤가요',	'화요비',	'10170',	'9126');");
		db.execSQL("insert into SongTable18 values(29,	'박전구',	'우연히',	'이정선',	'3150',	'정보없음');");
		db.execSQL("insert into SongTable18 values(30,	'장규희',	'Good-bye Baby',	'Miss A',	'34182',	'47443');");
		db.execSQL("insert into SongTable18 values(31,	'안병재',	'흐린 기억속의 그대',	'현진영',	'1183',	'1371');");
		db.execSQL("insert into SongTable18 values(32,	'임보배',	'가자!',	'Gigs',	'5791',	'63442');");
		db.execSQL("insert into SongTable18 values(33,	'정광일',	'눈물 편지',	'성시경',	'14844',	'69807');");
		db.execSQL("insert into SongTable18 values(34,	'신윤수',	'열애',	'윤시내',	'387',	'586');");
		db.execSQL("insert into SongTable18 values(35,	'권태원',	'그대의 향기',	'유영진',	'2011',	'3804');");
		db.execSQL("insert into SongTable18 values(36,	'박수민',	'약속',	'김범수',	'8153',	'5841');");
		db.execSQL("insert into SongTable18 values(37,	'나아람',	'말없이 바라만 봐',	'40',	'35304',	'87253');");
		db.execSQL("insert into SongTable18 values(38,	'오상아',	'나에게로의 초대',	'정경화',	'3294',	'4139');");
		db.execSQL("insert into SongTable18 values(39,	'이진실',	'그대 돌아오면',	'거미',	'10830',	'9278');");
		db.execSQL("insert into SongTable18 values(40,	'양은진',	'몽중인',	'박정현',	'8265',	'5904');");
		db.execSQL("insert into SongTable18 values(41,	'이예준/이현주',	'날 닮은 너',	'임창정',	'9252',	'6571');");
		db.execSQL("insert into SongTable18 values(42,	'이시몬/유다은',	'봄비',	'신중현',	'1890',	'415');");
		db.execSQL("insert into SongTable18 values(43,	'김민지/박의성',	'I`m In Love',	'라디',	'34106',	'86461');");
		db.execSQL("insert into SongTable18 values(44,	'김우현/김은지',	'Sherlock',	'샤이니',	'35141',	'47685');");
		db.execSQL("insert into SongTable18 values(45,	'김현지/윤성호',	'피리부는 사나이',	'송창식',	'639',	'985');");
		db.execSQL("insert into SongTable18 values(46,	'송푸름/김인형/이진실',	'새',	'싸이',	'9365',	'6724');");
		db.execSQL("insert into SongTable18 values(47,	'함성훈/김민석',	'점점',	'브라운 아이즈',	'10429',	'9170');");
		db.execSQL("insert into SongTable18 values(48,	'이병현/정진하',	'난 아직도 널',	'작품하나',	'1764',	'2291');");
		db.execSQL("insert into SongTable18 values(49,	'이정석/장치은',	'흩어진 나날들',	'강수지',	'1259',	'795');");
		db.execSQL("insert into SongTable18 values(50,	'장준수/권태원/박전구',	'호랑나비',	'김흥국',	'381',	'778');");
		db.execSQL("insert into SongTable18 values(51,	'이나겸/이소리',	'보여줄게',	'에일리',	'35970',	'47889');");
		db.execSQL("insert into SongTable18 values(52,	'윤성기/조재일',	'말하는 대로',	'처진 달팽이',	'34139',	'76963');");
		db.execSQL("insert into SongTable18 values(53,	'신유미/이재원',	'빗속의 여인',	'김건모',	'9590',	'7127');");
		db.execSQL("insert into SongTable18 values(54,	'배두훈/김현수',	'어떤이의 꿈',	'봄여름가을겨울',	'1175',	'562');");
		db.execSQL("insert into SongTable18 values(55,	'김남훈/오상아',	'Hotel California',	'The Eagles',	'7119',	'2559');");
		db.execSQL("insert into SongTable18 values(56,	'나아람/한고은',	'Be My Baby',	'원더걸스',	'34613',	'77086');");
		db.execSQL("insert into SongTable18 values(57,	'남유희/장규희',	'있다 없으니까',	'씨스타19',	'36397',	'58875');");
		db.execSQL("insert into SongTable18 values(58,	'최성은/박수민',	'I Got A Boy',	'소녀시대',	'36269',	'47973');");
		db.execSQL("insert into SongTable18 values(59,	'송수빈/심창현',	'기억해줘',	'이소라',	'3579',	'4863');");
		db.execSQL("insert into SongTable18 values(60,	'서해인/양은진',	'8282',	'다비치',	'30868',	'46582');");
		db.execSQL("insert into SongTable18 values(61,	'이시몬',	'Sad Salsa',	'백지영',	'8879',	'6315');");
		db.execSQL("insert into SongTable18 values(62,	'남유희',	'애송이',	'렉시',	'12059',	'65779');");
		db.execSQL("insert into SongTable18 values(63,	'이예준',	'그대가 그대를',	'이승환',	'8886',	'6326');");
		db.execSQL("insert into SongTable18 values(64,	'서해인',	'난 괜찮아',	'진주',	'4263',	'5188');");
		db.execSQL("insert into SongTable18 values(65,	'박수민',	'Valenti',	'보아',	'10084',	'9099');");
		db.execSQL("insert into SongTable18 values(66,	'유다은',	'어쩌다 마주친 그대',	'송골매',	'1209',	'1220');");
		db.execSQL("insert into SongTable18 values(67,	'이소리',	'난 널 사랑해',	'신효범',	'1792',	'3326');");
		db.execSQL("insert into SongTable18 values(68,	'김현수',	'세상이 그대를 속일지라도',	'김장훈',	'4682',	'5453');");
		db.execSQL("insert into SongTable18 values(69,	'송푸름',	'그런 일은',	'화요비',	'9318',	'6695');");
		db.execSQL("insert into SongTable18 values(70,	'윤성호',	'먼 훗날에',	'박정운',	'1388',	'1493');");
		db.execSQL("insert into SongTable18 values(71,	'윤성기',	'그녀의 웃음소리뿐',	'이문세',	'1572',	'2247');");
		db.execSQL("insert into SongTable18 values(72,	'신유미',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable18 values(73,	'한고은',	'Twinkle',	'태티서',	'35315',	'47731');");
		db.execSQL("insert into SongTable18 values(74,	'송수빈',	'바다에 누워',	'높은음자리',	'723',	'379');");
		db.execSQL("insert into SongTable18 values(75,	'김남훈',	'생각이나',	'부활',	'31528',	'46749');");
		db.execSQL("insert into SongTable18 values(76,	'이병현',	'짱가',	'김건모',	'9546',	'6876');");
		db.execSQL("insert into SongTable18 values(77,	'이나겸',	'바보',	'박효신',	'8868',	'6328');");
		db.execSQL("insert into SongTable18 values(78,	'김현지',	'바보처럼 살았군요',	'김도향',	'292',	'392');");
		db.execSQL("insert into SongTable18 values(79,	'장치은',	'이노래',	'2AM',	'19848',	'83678');");
		db.execSQL("insert into SongTable18 values(80,	'함성훈',	'울고 싶어라',	'이남이(사랑과 평화)',	'468',	'614');");
		db.execSQL("insert into SongTable18 values(81,	'이정석',	'정류장',	'패닉',	'15522',	'64957');");
		db.execSQL("insert into SongTable18 values(82,	'장준수',	'물레방아 인생',	'조영남',	'1153',	'2868');");
		db.execSQL("insert into SongTable18 values(83,	'김우현',	'포기하지 마',	'성진우',	'2500',	'3714');");
		db.execSQL("insert into SongTable18 values(84,	'이재원',	'어른 아이',	'거미',	'15195',	'64851');");
		db.execSQL("insert into SongTable18 values(85,	'박의성',	'죽겠네',	'10cm',	'33659',	'47245');");
		db.execSQL("insert into SongTable18 values(86,	'나아람',	'나비효과',	'신승훈',	'30246',	'85980');");
		db.execSQL("insert into SongTable18 values(87,	'최성은',	'구두',	'씨야',	'15756',	'69831');");
		db.execSQL("insert into SongTable18 values(88,	'배두훈',	'그남자',	'현빈',	'33519',	'47254');");
		db.execSQL("insert into SongTable18 values(89,	'박의성',	'흐린 가을 하늘에 편지를 써',	'김광석(동물원)',	'1854',	'1516');");
		db.execSQL("insert into SongTable18 values(90,	'윤성기',	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable18 values(91,	'배두훈',	'엄마야',	'신승훈',	'8902',	'6335');");
		db.execSQL("insert into SongTable18 values(92,	'이시몬',	'소녀',	'이문세',	'332',	'3674');");
		db.execSQL("insert into SongTable18 values(93,	'함성훈',	'우리의 밤은 당신의 낮보다 아름답다',	'코나',	'3133',	'4161');");
		db.execSQL("insert into SongTable18 values(94,	'송푸름',	'잃어버린 우산',	'우순실',	'348',	'961');");
		db.execSQL("insert into SongTable18 values(95,	'박의성/윤성기/배두훈',	'거위의 꿈',	'인순이',	'16936',	'81408');");
		db.execSQL("insert into SongTable18 values(96,	'이시몬/함성훈/송푸름',	'행복을 주는 사람',	'해바라기',	'2554',	'3554');");
		db.execSQL("insert into SongTable18 values(97,	'신유미',	'바람기억',	'나얼',	'35884',	'47870');");
		db.execSQL("insert into SongTable18 values(98,	'이예준',	'열맞춰!',	'H.O.T',	'4788',	'5617');");
		db.execSQL("insert into SongTable18 values(99,	'이정석',	'서시',	'신성우',	'2135',	'3446');");
		db.execSQL("insert into SongTable18 values(100,	'유다은',	'연(捐)',	'빅마마',	'16627',	'85250');");
		db.execSQL("insert into SongTable18 values(101,	'김현지',	'나는 나비',	'YB',	'16329',	'85183');");
		db.execSQL("insert into SongTable18 values(102,	'송수빈',	'사랑은 연필로 쓰세요',	'전영록',	'4016',	'450');");
		db.execSQL("insert into SongTable18 values(103,	'신유미/이예준/이정석',	'풍문으로 들었소',	'장기하와 얼굴들',	'34894',	'47630');");
		db.execSQL("insert into SongTable18 values(104,	'유다은/김현지/송수빈',	'비상',	'임재범',	'9429',	'7153');");
		db.execSQL("insert into SongTable18 values(105,	'장재호',	'이별택시',	'김연우',	'12637',	'66767');");
		db.execSQL("insert into SongTable18 values(106,	'샘구',	'…사랑했자나…',	'린',	'13049',	'64331');");
		db.execSQL("insert into SongTable18 values(107,	'김지은',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable18 values(108,	'정승원,이신성',	'이 밤이 지나면',	'임재범',	'1254',	'1238');");
		db.execSQL("insert into SongTable18 values(109,	'이헌승',	'그날들',	'김광석',	'4248',	'7054');");
		db.execSQL("insert into SongTable18 values(110,	'하예나',	'청혼',	'노을',	'13319',	'68248');");
		db.execSQL("insert into SongTable18 values(111,	'지세희,오슬기',	'이유같지 않은 이유',	'박미경',	'2335',	'3578');");
		db.execSQL("insert into SongTable18 values(112,	'우혜미',	'나의 노래',	'김광석',	'1581',	'1380');");
		db.execSQL("insert into SongTable18 values(113,	'신지현',	'서른 즈음에',	'김광석',	'2337',	'4448');");
		db.execSQL("insert into SongTable18 values(114,	'배근석',	'신데렐라',	'서인영',	'19903',	'46361');");
		db.execSQL("insert into SongTable18 values(115,	'나들이',	'님은 먼 곳에',	'김추자',	'596',	'261');");
		db.execSQL("insert into SongTable18 values(116,	'홍혁수',	'With Me',	'휘성',	'11861',	'63962');");
		db.execSQL("insert into SongTable18 values(117,	'강미진',	'미아',	'아이유',	'30197',	'46438');");
		db.execSQL("insert into SongTable18 values(118,	'이찬미,이윤경',	'꿈에',	'박정현',	'9973',	'7955');");
		db.execSQL("insert into SongTable18 values(119,	'유성은,황예린',	'10 Minutes',	'이효리',	'11833',	'9504');");
		db.execSQL("insert into SongTable18 values(120,	'강보리,한경수',	'사랑..그 놈',	'바비 킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable18 values(121,	'장은아',	'별짓 다 해봤는데',	'알리',	'38882',	'47349');");
		db.execSQL("insert into SongTable18 values(122,	'이웅희',	'어른 아이',	'거미',	'15195',	'64851');");
		db.execSQL("insert into SongTable18 values(123,	'허공',	'여전히 아름다운지',	'김연우',	'4975',	'5781');");
		db.execSQL("insert into SongTable18 values(124,	'손승연',	'Go Away',	'2NE1',	'33063',	'58029');");
		db.execSQL("insert into SongTable18 values(125,	'서혁신',	'거울',	'윤도현 밴드',	'5847',	'63326');");
		db.execSQL("insert into SongTable18 values(126,	'장정우',	'그때로 돌아가는 게',	'김조한',	'8394',	'6007');");
		db.execSQL("insert into SongTable18 values(127,	'권순재',	'미소속에 비친 그대',	'신승훈',	'182',	'370');");
		db.execSQL("insert into SongTable18 values(128,	'이소정',	'내가 웃는게 아니야',	'리쌍',	'15330',	'45369');");
		db.execSQL("insert into SongTable18 values(129,	'강태우',	'이태원 프리덤',	'Uv',	'38864',	'76861');");
		db.execSQL("insert into SongTable18 values(130,	'임진호,지세희',	'끝사랑',	'김범수',	'34049',	'47408');");
		db.execSQL("insert into SongTable18 values(131,	'김지훈',	'재즈 카페',	'신해철',	'6',	'1315');");
		db.execSQL("insert into SongTable18 values(132,	'전은철',	'후회한다',	'4men',	'33315',	'86710');");
		db.execSQL("insert into SongTable18 values(133,	'김은아',	'헤어지는 중입니다',	'이은미',	'30925',	'84155');");
		db.execSQL("insert into SongTable18 values(134,	'오경석',	'기억의 습작',	'전람회',	'2200',	'3462');");
		db.execSQL("insert into SongTable18 values(135,	'이한얼',	'왜 눈물만 나는지',	'박효신',	'13786',	'58550');");
		db.execSQL("insert into SongTable18 values(136,	'김민정,박태영',	'태양을 피하는 방법',	'비',	'12094',	'9573');");
		db.execSQL("insert into SongTable18 values(137,	'장재호/황예린',	'안부',	'별,나윤권',	'14716',	'68987');");
		db.execSQL("insert into SongTable18 values(138,	'지세희/오경석',	'맨발의 청춘',	'벅',	'3624',	'4876');");
		db.execSQL("insert into SongTable18 values(139,	'유성은/임진호',	'미소를 띄우며 나를 보낸 그 모습처럼',	'이은하',	'1946',	'1419');");
		db.execSQL("insert into SongTable18 values(140,	'우혜미/정소연',	'아쉬움',	'신촌블루스',	'1729',	'2608');");
		db.execSQL("insert into SongTable18 values(141,	'강미진/이찬미',	'마리아',	'김아중',	'16712',	'45783');");
		db.execSQL("insert into SongTable18 values(142,	'정승원',	'You Are My Lady',	'정엽',	'30367',	'46473');");
		db.execSQL("insert into SongTable18 values(143,	'이은아',	'믿음',	'이소라',	'4568',	'5493');");
		db.execSQL("insert into SongTable18 values(144,	'장정우',	'담배가게 아가씨',	'송창식',	'2682',	'2234');");
		db.execSQL("insert into SongTable18 values(145,	'정나현',	'봄 여름 가을 겨울',	'김현식',	'4199',	'4396');");
		db.execSQL("insert into SongTable18 values(146,	'샘구',	' 나만 바라봐',	'태양',	'19645',	'46306');");
		db.execSQL("insert into SongTable18 values(147,	'손승연',	'물들어',	'BMK',	'17427',	'85390');");
		db.execSQL("insert into SongTable18 values(148,	'홍혁수',	'사랑합니다',	'팀(Tim)',	'11271',	'62988');");
		db.execSQL("insert into SongTable18 values(149,	'김현민',	'내 눈물 모아',	'서지원',	'2936',	'4011');");
		db.execSQL("insert into SongTable18 values(150,	'이소정',	'2 Different Tears',	'원더걸스',	'32605',	'47023');");
		db.execSQL("insert into SongTable18 values(151,	'장재호',	'내 아픔 아시는 당신께',	'조하문',	'121',	'840');");
		db.execSQL("insert into SongTable18 values(152,	'배근석',	'아틀란티스 소녀',	'보아',	'11466',	'9429');");
		db.execSQL("insert into SongTable18 values(153,	'우혜미',	'한잔 더',	'바비 킴',	'13957',	'64591');");
		db.execSQL("insert into SongTable18 values(154,	'장은아',	'아름다운 날들',	'장혜진',	'9524',	'6878');");
		db.execSQL("insert into SongTable18 values(155,	'최준영',	'Sea of Love',	'플라이 투 더 스카이',	'9874',	'7887');");
		db.execSQL("insert into SongTable18 values(156,	'하예나',	'희야',	'부활',	'1824',	'2015');");
		db.execSQL("insert into SongTable18 values(157,	'박태영',	'미련한 사랑',	'JK 김동욱',	'9952',	'7920');");
		db.execSQL("insert into SongTable18 values(158,	'강미진',	'Ugly',	'2Ne1',	'34228',	'47459');");
		db.execSQL("insert into SongTable18 values(159,	'허공',	'다 줄꺼야',	'조규만',	'8614',	'96539');");
		db.execSQL("insert into SongTable18 values(160,	'유성은',	'비나리',	'심수봉',	'2927',	'3778');");
		db.execSQL("insert into SongTable18 values(161,	'손승연/오슬기',	'하늘에서 남자들이 비처럼 내려와',	'Bubble Sisters',	'10847',	'9288');");
		db.execSQL("insert into SongTable18 values(162,	'지세희',	'사랑비',	'김태우',	'31588',	'46773');");
		db.execSQL("insert into SongTable18 values(163,	'정승원',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable18 values(164,	'배근석',	'리듬 속의 그 춤을',	'김완선',	'3634',	'4350');");
		db.execSQL("insert into SongTable18 values(165,	'이소정',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable18 values(166,	'홍혁수',	'The Way U Are',	'동방신기',	'13480',	'68334');");
		db.execSQL("insert into SongTable18 values(167,	'장재호',	'편지',	'김광진',	'9505',	'7301');");
		db.execSQL("insert into SongTable18 values(168,	'정나현',	'여자',	'빅마마',	'14901',	'45225');");
		db.execSQL("insert into SongTable18 values(169,	'손승연',	'비와 당신의 이야기',	'부활',	'1742',	'1784');");
		db.execSQL("insert into SongTable18 values(170,	'우혜미/하예나/강미진/유성은',	'밤이면 밤마다',	'인순이',	'1996',	'1542');");
		db.execSQL("insert into SongTable18 values(171,	'우혜미/하예나/강미진/유성은',	'영원한 친구',	'나미',	'985',	'939');");
		db.execSQL("insert into SongTable18 values(172,	'우혜미',	'당신과의 키스를 세어보아요',	'화요비',	'13549',	'64398');");
		db.execSQL("insert into SongTable18 values(173,	'하예나',	'나에게로의 초대',	'정경화',	'3294',	'4139');");
		db.execSQL("insert into SongTable18 values(174,	'지세희',	'동경',	'박효신',	'9375',	'7378');");
		db.execSQL("insert into SongTable18 values(175,	'정나현',	'귀로',	'박선주',	'749',	'2245');");
		db.execSQL("insert into SongTable18 values(176,	'유성은',	'Just A Feeling',	'S.E.S',	'9826',	'7795');");
		db.execSQL("insert into SongTable18 values(177,	'강미진',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable18 values(178,	'이소정',	'기억상실',	'거미',	'13902',	'9888');");
		db.execSQL("insert into SongTable18 values(179,	'손승연',	'안녕',	'김태화',	'2384',	'545');");
		db.execSQL("insert into SongTable18 values(180,	'유성은',	'잠시 길을 잃다',	'015B',	'19300',	'86315');");
		db.execSQL("insert into SongTable18 values(181,	'하예나',	'안되나요',	'휘성',	'9851',	'7859');");
		db.execSQL("insert into SongTable18 values(182,	'최준영',	'희나리',	'구창모',	'178',	'796');");
		db.execSQL("insert into SongTable18 values(183,	'신지현',	'그리움만 쌓이네',	'노영심',	'2618',	'3782');");
		db.execSQL("insert into SongTable18 values(184,	'강미진',	'유혹의 소나타',	'아이비',	'17180',	'81467');");
		db.execSQL("insert into SongTable18 values(185,	'우혜미',	'Maria',	'윤시내',	'정보없음',	'87242');");
		db.execSQL("insert into SongTable18 values(186,	'신초이',	'아웃사이더',	'봄여름가을겨울',	'372',	'1361');");
		db.execSQL("insert into SongTable18 values(187,	'장은아',	'다가와서',	'브라운 아이드 걸스',	'15741',	'45496');");
		db.execSQL("insert into SongTable18 values(188,	'허공',	'우울한 편지',	'유재하',	'8970',	'63230');");
		db.execSQL("insert into SongTable18 values(189,	'박태영',	'좋은 날',	'아이유',	'33393',	'76754');");
		db.execSQL("insert into SongTable18 values(190,	'인지윤',	'U-Go-Girl',	'이효리',	'19854',	'83680');");
		db.execSQL("insert into SongTable18 values(191,	'남일',	'음음음',	'박진영',	'4392',	'7453');");
		db.execSQL("insert into SongTable18 values(192,	'정승원/이웅희',	'밥만 잘 먹더라',	'Homme',	'32897',	'47097');");
		db.execSQL("insert into SongTable18 values(193,	'남일',	'Bad Girl Good Girl',	'Miss A',	'32773',	'47073');");
		db.execSQL("insert into SongTable18 values(194,	'선지혜',	'애모',	'김수희',	'1427',	'1983');");
		db.execSQL("insert into SongTable18 values(195,	'함성훈',	'잠 못 드는 밤 비는 내리고',	'김건모',	'1117',	'1509');");
		db.execSQL("insert into SongTable18 values(196,	'인지윤',	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable18 values(197,	'최성수',	'서시',	'신성우',	'2135',	'3446');");
		db.execSQL("insert into SongTable18 values(198,	'전초아',	'제발',	'이소라',	'9276',	'6665');");
		db.execSQL("insert into SongTable18 values(199,	'노영호',	'사랑일뿐야',	'김민우',	'302',	'464');");
		db.execSQL("insert into SongTable18 values(200,	'인지윤/함성훈',	'Trouble Maker',	'장현승,현아',	'34717',	'77110');");
		db.execSQL("insert into SongTable18 values(201,	'편선희',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable18 values(202,	'이한올/홍혁수',	'니가 사는 그 집',	'박진영',	'18884',	'46116');");
		db.execSQL("insert into SongTable18 values(203,	'샘구/권순재',	'가지마 가지마',	'브라운 아이즈',	'19762',	'46333');");
		db.execSQL("insert into SongTable18 values(204,	'김현민',	'바라본다',	'윤상현',	'33300',	'86727');");
		db.execSQL("insert into SongTable18 values(205,	'최준영',	'너를 위해',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable18 values(206,	'장정우/노영호',	'사랑은 향기를 남기고',	'테이',	'12560',	'9651');");
		db.execSQL("insert into SongTable18 values(207,	'서혁신/남일',	'Heartbeat',	'2PM',	'31851',	'84613');");
		db.execSQL("insert into SongTable18 values(208,	'이소정/나들이',	'코뿔소',	'한영애',	'정보없음',	'87239');");
		db.execSQL("insert into SongTable18 values(209,	'김지훈/정나현',	'따뜻했던 커피조차도',	'조규찬',	'3277',	'4344');");
		db.execSQL("insert into SongTable18 values(210,	'허공/김민정',	'니가 있어야 할 곳',	'god',	'9769',	'7722');");
		db.execSQL("insert into SongTable18 values(211,	'최준영/허규',	'Music Is My Life',	'임정희',	'14915',	'45220');");
		db.execSQL("insert into SongTable18 values(212,	'신지현/조지은',	'경고',	'타샤니',	'8548',	'6194');");
		db.execSQL("insert into SongTable18 values(213,	'이은아/선지혜',	'잊지말아요',	'백지영',	'31760',	'46813');");
		db.execSQL("insert into SongTable18 values(214,	'장은아/이윤경',	'훗',	'소녀시대',	'33222',	'47182');");
		db.execSQL("insert into SongTable18 values(215,	'박태영/임병석',	'누난 너무 예뻐',	'샤이니',	'19646',	'46317');");
		db.execSQL("insert into SongTable18 values(216,	'하예나/편선희',	'저 바다가 날 막겠어',	'임상아',	'4002',	'5081');");
		db.execSQL("insert into SongTable18 values(217,	'김현민/김지훈',	'중독된 사랑',	'조장혁',	'8937',	'6431');");
		db.execSQL("insert into SongTable18 values(218,	'강태우/배근석',	'거짓말',	'빅뱅',	'18553',	'46034');");
		db.execSQL("insert into SongTable18 values(219,	'신초이/김채린',	'사랑 사랑 사랑',	'김현식',	'913',	'1501');");
		db.execSQL("insert into SongTable18 values(220,	'지세희/정나현',	'I Will Survive',	'Gloria Gaynor',	'20155',	'8490');");
		db.execSQL("insert into SongTable18 values(221,	'유성은/강미진',	'Rolling In The Deep',	'Adele',	'22213',	'79017');");
		db.execSQL("insert into SongTable18 values(222,	'이소정/손승연',	'If I Ain`t Got You',	'Alicia Keys ',	'21045',	'61469');");
		db.execSQL("insert into SongTable18 values(223,	'지세희',	'그것만이 내 세상',	'들국화',	'1780',	'1329');");
		db.execSQL("insert into SongTable18 values(224,	'우혜미',	'필승',	'서태지와 아이들',	'2754',	'3911');");
		db.execSQL("insert into SongTable18 values(225,	'손승연',	'여러분',	'윤복희',	'534',	'1310');");
		db.execSQL("insert into SongTable18 values(226,	'우혜미/하예나',	'Stop',	'Sam Brown',	'20019',	'60004');");
		db.execSQL("insert into SongTable18 values(227,	'유성은',	'Game Over',	'유성은',	'정보없음',	'58630');");
		db.execSQL("insert into SongTable18 values(228,	'우혜미',	'Lovely',	'우혜미',	'정보없음',	'58631');");
		db.execSQL("insert into SongTable18 values(229,	'손승연',	'미운오리새끼',	'손승연',	'35874',	'47741');");
		db.execSQL("insert into SongTable18 values(230,	'유성은',	'창밖의 여자',	'조용필',	'567',	'1797');");
		db.execSQL("insert into SongTable18 values(231,	'손승연/유성은/우혜미/지세희',	'Stand Up For You',	'손승연,유성은,우혜미,지세희',	'정보없음',	'77305');");
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable18");
		onCreate(db);
	}
}