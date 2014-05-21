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

public class WhatTheSong13 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_13);

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

		SharedPreferences m_pref = getSharedPreferences("m_pref_13",
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
		cursor = db.rawQuery("SELECT * FROM SongTable13 WHERE song LIKE '"
				+ edit + "%' OR song LIKE '%" + edit + "%' OR song LIKE '%"
				+ edit + "' OR singer LIKE '" + edit + "%' OR singer LIKE '%"
				+ edit + "%' OR singer LIKE '%" + edit
				+ "' OR participant LIKE '" + edit
				+ "%' OR participant LIKE '%" + edit
				+ "%' OR participant LIKE '%" + edit + "' ORDER BY song ASC",
				null);
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

		WhatTheSongDBHelper13 wtsHelper13 = new WhatTheSongDBHelper13(this, "whatthesong13.db", null, DATABASE_VERSION);
		db = wtsHelper13.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable13 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable13 where id= '"
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
		new AlertDialog.Builder(WhatTheSong13.this)
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
											WhatTheSong13.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong13.this,
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
												+ song[p] + " - "
												+ participant[p]));
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
			Intent newActivity = new Intent(WhatTheSong13.this,
					WhatTheSongBookmark.class);
			WhatTheSong13.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
		} else if (v.getId() == R.id.search) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
			String edit = SearchText.getText().toString();
			cursor = db.rawQuery("SELECT * FROM SongTable13 WHERE song LIKE '"
					+ edit + "%' OR song LIKE '%" + edit + "%' OR song LIKE '%"
					+ edit + "' OR singer LIKE '" + edit
					+ "%' OR singer LIKE '%" + edit + "%' OR singer LIKE '%"
					+ edit + "' OR participant LIKE '" + edit
					+ "%' OR participant LIKE '%" + edit
					+ "%' OR participant LIKE '%" + edit + "'", null);
			if (cursor.getCount() == 0) {
				Toast.makeText(WhatTheSong13.this, "검색 결과가 없습니다.",
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
					"m_pref_13", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper13 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper13(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '나는 가수다 테마' DB (130627 : 454개)
		db.execSQL("CREATE TABLE SongTable13 ( id INTEGER ,"
				+ " participant TEXT ," + " song TEXT , " + " singer TEXT ,"
				+ " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable13 values(1,	'BMK',	'꽃피는 봄이 오면',	'BMK',	'14503',	'45112');");
		db.execSQL("insert into SongTable13 values(2,	'BMK',	'그대 내게 다시',	'변진섭',	'1511',	'1579');");
		db.execSQL("insert into SongTable13 values(3,	'BMK',	'아름다운 강산',	'이선희',	'870',	'923');");
		db.execSQL("insert into SongTable13 values(4,	'BMK',	'편지',	'김광진',	'9505',	'7301');");
		db.execSQL("insert into SongTable13 values(5,	'BMK',	'비와 당신의 이야기',	'부활',	'1742',	'1784');");
		db.execSQL("insert into SongTable13 values(6,	'BMK',	'삐에로는 우릴 보고 웃지',	'김완선',	'813',	'431');");
		db.execSQL("insert into SongTable13 values(7,	'BMK',	'사랑하기에',	'이정석',	'299',	'465');");
		db.execSQL("insert into SongTable13 values(8,	'JK 김동욱',	'미련한 사랑 (위기의 남자 O.S.T)',	'JK 김동욱',	'9952',	'7920');");
		db.execSQL("insert into SongTable13 values(9,	'JK 김동욱',	'비상',	'임재범',	'9429',	'7153');");
		db.execSQL("insert into SongTable13 values(10,	'JK 김동욱',	'조율',	'한영애',	'34080',	'1780');");
		db.execSQL("insert into SongTable13 values(11,	'JK 김동욱',	'상록수',	'양희은',	'4780',	'5673');");
		db.execSQL("insert into SongTable13 values(12,	'JK 김동욱',	'명태',	'강산에',	'10254',	'62924');");
		db.execSQL("insert into SongTable13 values(13,	'JK 김동욱',	'찔레꽃',	'이연실(원곡) / 이연실(TJ) / 임형주(KY)',	'35761',	'68675');");
		db.execSQL("insert into SongTable13 values(14,	'JK 김동욱',	'행복의 나라로',	'한대수',	'1506',	'2650');");
		db.execSQL("insert into SongTable13 values(15,	'JK 김동욱',	'담배가게 아가씨',	'송창식',	'2682',	'2234');");
		db.execSQL("insert into SongTable13 values(16,	'JK 김동욱',	'Higher',	'JK 김동욱',	'35615',	'정보없음');");
		db.execSQL("insert into SongTable13 values(17,	'JK 김동욱',	'옛 사랑',	'이문세',	'916',	'1364');");
		db.execSQL("insert into SongTable13 values(18,	'JK 김동욱',	'사랑 사랑 사랑',	'김현식',	'913',	'1501');");
		db.execSQL("insert into SongTable13 values(19,	'JK 김동욱',	'미인',	'신중현과 엽전들',	'1555',	'2372');");
		db.execSQL("insert into SongTable13 values(20,	'거미',	'난 행복해',	'이소라',	'2742',	'3924');");
		db.execSQL("insert into SongTable13 values(21,	'거미',	'또',	'인순이',	'3299',	'4720');");
		db.execSQL("insert into SongTable13 values(22,	'거미',	'비처럼 음악처럼',	'김현식',	'314',	'430');");
		db.execSQL("insert into SongTable13 values(23,	'거미',	'개구장이',	'산울림',	'2600',	'1642');");
		db.execSQL("insert into SongTable13 values(24,	'거미',	'날 떠나지마',	'박진영',	'2357',	'3569');");
		db.execSQL("insert into SongTable13 values(25,	'거미',	'P.S. I Love You',	'박정현',	'4714',	'5550');");
		db.execSQL("insert into SongTable13 values(26,	'거미',	'내 하나의 사람은 가고',	'임희숙',	'502',	'234');");
		db.execSQL("insert into SongTable13 values(27,	'거미',	'애인있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable13 values(28,	'거미',	'영원한 친구',	'나미',	'985',	'939');");
		db.execSQL("insert into SongTable13 values(29,	'거미',	'흐린 기억 속의 그대',	'현진영',	'1183',	'1371');");
		db.execSQL("insert into SongTable13 values(30,	'게이트 플라워즈',	'목포의 눈물',	'이난영',	'768',	'346');");
		db.execSQL("insert into SongTable13 values(31,	'고유진',	'마지막사랑',	'박기영',	'8378',	'6011');");
		db.execSQL("insert into SongTable13 values(32,	'국카스텐',	'한 잔의 추억',	'이장희',	'609',	'765');");
		db.execSQL("insert into SongTable13 values(33,	'국카스텐',	'거울',	'국카스텐',	'30978',	'84152');");
		db.execSQL("insert into SongTable13 values(34,	'국카스텐',	'가장무도회',	'김완선',	'1248',	'107');");
		db.execSQL("insert into SongTable13 values(35,	'국카스텐',	'The Saddest Thing',	'Melanie Safka',	'20587',	'2362');");
		db.execSQL("insert into SongTable13 values(36,	'국카스텐',	'어서 말을 해',	'해바라기',	'14998',	'2943');");
		db.execSQL("insert into SongTable13 values(37,	'국카스텐',	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable13 values(38,	'국카스텐',	'행진',	'들국화',	'1113',	'2577');");
		db.execSQL("insert into SongTable13 values(39,	'국카스텐',	'촛불 (축복 O.S.T)',	'조용필',	'652',	'2555');");
		db.execSQL("insert into SongTable13 values(40,	'국카스텐',	'잊혀진 계절',	'이용',	'512',	'659');");
		db.execSQL("insert into SongTable13 values(41,	'국카스텐',	'나 혼자 (Alone)',	'씨스타',	'35232',	'77243');");
		db.execSQL("insert into SongTable13 values(42,	'국카스텐',	'누구 없소',	'한영애',	'691',	'250');");
		db.execSQL("insert into SongTable13 values(43,	'국카스텐',	'매니큐어',	'국카스텐',	'35809',	'정보없음');");
		db.execSQL("insert into SongTable13 values(44,	'국카스텐',	'모나리자',	'조용필',	'1868',	'1665');");
		db.execSQL("insert into SongTable13 values(45,	'국카스텐',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable13 values(46,	'국카스텐',	'넋두리',	'김현식',	'1777',	'2302');");
		db.execSQL("insert into SongTable13 values(47,	'국카스텐',	'희야',	'부활',	'1824',	'2015');");
		db.execSQL("insert into SongTable13 values(48,	'국카스텐',	'해야',	'마그마',	'2145',	'4626');");
		db.execSQL("insert into SongTable13 values(49,	'국카스텐, 이은미',	'어른 아이',	'거미',	'15195',	'64851');");
		db.execSQL("insert into SongTable13 values(50,	'김건모',	'잠 못 드는 밤 비는 내리고',	'김건모',	'1117',	'1509');");
		db.execSQL("insert into SongTable13 values(51,	'김건모',	'립스틱 짙게 바르고',	'임주리',	'644',	'317');");
		db.execSQL("insert into SongTable13 values(52,	'김건모',	'You Are My Lady',	'정엽',	'30367',	'46473');");
		db.execSQL("insert into SongTable13 values(53,	'김건모',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable13 values(54,	'김건모',	'내 마음에 비친 내 모습',	'유재하',	'2948',	'4287');");
		db.execSQL("insert into SongTable13 values(55,	'김건모',	'시인의 마을',	'정태춘',	'1323',	'2034');");
		db.execSQL("insert into SongTable13 values(56,	'김건모',	'내 마음 당신 곁으로',	'김정수',	'268',	'230');");
		db.execSQL("insert into SongTable13 values(57,	'김건모',	'다행이다',	'이적',	'17772',	'45912');");
		db.execSQL("insert into SongTable13 values(58,	'김건모',	'I Can Wait Forever',	'Air Supply',	'21135',	'60456');");
		db.execSQL("insert into SongTable13 values(59,	'김건모',	'어떤 이의 꿈',	'봄 여름 가을 겨울',	'1175',	'562');");
		db.execSQL("insert into SongTable13 values(60,	'김건모',	'서른 즈음에',	'김광석',	'2337',	'4448');");
		db.execSQL("insert into SongTable13 values(61,	'김경호',	'모두 다 사랑하리',	'송골매',	'895',	'1411');");
		db.execSQL("insert into SongTable13 values(62,	'김경호',	'못 찾겠다 꾀꼬리',	'조용필',	'1930',	'1466');");
		db.execSQL("insert into SongTable13 values(63,	'김경호',	'암연 (여자 O.S.T)',	'고한우',	'3962',	'5046');");
		db.execSQL("insert into SongTable13 values(64,	'김경호',	'이유같지 않은 이유',	'박미경',	'2335',	'3578');");
		db.execSQL("insert into SongTable13 values(65,	'김경호',	'헤이 헤이 헤이 (Hey Hey Hey)',	'자우림',	'4043',	'5127');");
		db.execSQL("insert into SongTable13 values(66,	'김경호',	'내 눈물 모아',	'서지원',	'2936',	'4011');");
		db.execSQL("insert into SongTable13 values(67,	'김경호',	'찻잔',	'노고지리',	'39',	'701');");
		db.execSQL("insert into SongTable13 values(68,	'김경호',	'아직도 어두운 밤인가봐',	'전영록',	'1223',	'2462');");
		db.execSQL("insert into SongTable13 values(69,	'김경호',	'사랑 안해',	'백지영',	'15871',	'45528');");
		db.execSQL("insert into SongTable13 values(70,	'김경호',	'밤차',	'이은하',	'2695',	'1180');");
		db.execSQL("insert into SongTable13 values(71,	'김경호',	'걸어서 하늘까지 (걸어서 하늘까지 O.S.T)',	'장현철',	'1120',	'1575');");
		db.execSQL("insert into SongTable13 values(72,	'김경호',	'못 다 핀 꽃 한 송이',	'김수철',	'450',	'350');");
		db.execSQL("insert into SongTable13 values(73,	'김경호',	'그녀의 웃음소리뿐',	'이문세',	'1572',	'2247');");
		db.execSQL("insert into SongTable13 values(74,	'김경호, 김연우',	'사랑과 우정 사이',	'피노키오',	'1611',	'2797');");
		db.execSQL("insert into SongTable13 values(75,	'김범수',	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable13 values(76,	'김범수',	'그대 모습은 장미',	'민해경',	'388',	'1117');");
		db.execSQL("insert into SongTable13 values(77,	'김범수',	'제발',	'이소라',	'9276',	'6665');");
		db.execSQL("insert into SongTable13 values(78,	'김범수',	'그런 이유라는 걸',	'김범수',	'12299',	'66570');");
		db.execSQL("insert into SongTable13 values(79,	'김범수',	'그대의 향기',	'유영진',	'2011',	'3804');");
		db.execSQL("insert into SongTable13 values(80,	'김범수',	'늪',	'조관우',	'2299',	'3537');");
		db.execSQL("insert into SongTable13 values(81,	'김범수',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable13 values(82,	'김범수',	'님과 함께',	'남진',	'411',	'648');");
		db.execSQL("insert into SongTable13 values(83,	'김범수',	'여름 안에서',	'듀스',	'2246',	'3527');");
		db.execSQL("insert into SongTable13 values(84,	'김범수',	'사랑하오',	'김현철, 윤상(TJ) / 김범수(KY)',	'10904',	'86967');");
		db.execSQL("insert into SongTable13 values(85,	'김범수',	'외톨이야',	'씨엔블루',	'32118',	'46905');");
		db.execSQL("insert into SongTable13 values(86,	'김범수',	'희나리',	'구창모',	'178',	'796');");
		db.execSQL("insert into SongTable13 values(87,	'김범수',	'사랑으로',	'해바라기',	'234',	'443');");
		db.execSQL("insert into SongTable13 values(88,	'김범수',	'홀로 된다는 것',	'변진섭',	'168',	'781');");
		db.execSQL("insert into SongTable13 values(89,	'김범수',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable13 values(90,	'김범수, 박정현',	'사랑보다 깊은 상처',	'박정현, 임재범',	'4980',	'5810');");
		db.execSQL("insert into SongTable13 values(91,	'김연우',	'내가 너의 곁에 잠시 살았다는 걸',	'토이',	'3119',	'4095');");
		db.execSQL("insert into SongTable13 values(92,	'김연우',	'여전히 아름다운 지',	'토이',	'4975',	'5781');");
		db.execSQL("insert into SongTable13 values(93,	'김연우',	'미련',	'김건모',	'3136',	'4168');");
		db.execSQL("insert into SongTable13 values(94,	'김연우',	'나와 같다면',	'김장훈',	'4490',	'5414');");
		db.execSQL("insert into SongTable13 values(95,	'김연우',	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable13 values(96,	'김연우',	'가로수 그늘 아래 서면',	'이문세',	'3',	'1451');");
		db.execSQL("insert into SongTable13 values(97,	'김연우',	'미소를 띄우며 나를 보낸 그 모습처럼',	'이은하',	'1946',	'1419');");
		db.execSQL("insert into SongTable13 values(98,	'김연우',	'당신만이',	'이치현과 벗님들',	'2650',	'63686');");
		db.execSQL("insert into SongTable13 values(99,	'김연우',	'호랑나비',	'김흥국',	'381',	'778');");
		db.execSQL("insert into SongTable13 values(100,	'김연우',	'All By Myself',	'Eric Carmen',	'7229',	'2403');");
		db.execSQL("insert into SongTable13 values(101,	'김연우',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable13 values(102,	'김연우',	'Rain',	'이적',	'8333',	'5961');");
		db.execSQL("insert into SongTable13 values(103,	'김연우',	'사랑일뿐야',	'김민우',	'302',	'464');");
		db.execSQL("insert into SongTable13 values(104,	'김연우',	'그대 내 품에',	'유재하(TJ) / 김현식(KY)',	'12766',	'62251');");
		db.execSQL("insert into SongTable13 values(105,	'김연우',	'중독된 사랑',	'조장혁',	'8937',	'6431');");
		db.execSQL("insert into SongTable13 values(106,	'김연우',	'마지막 콘서트',	'이승철',	'1330',	'326');");
		db.execSQL("insert into SongTable13 values(107,	'김연우',	'1994년 어느 늦은 밤',	'장혜진',	'2432',	'3641');");
		db.execSQL("insert into SongTable13 values(108,	'김연우',	'그대여 변치 마오',	'남진',	'576',	'175');");
		db.execSQL("insert into SongTable13 values(109,	'김조한',	'I Believe (엽기적인 그녀 O.S.T)',	'신승훈',	'9603',	'6983');");
		db.execSQL("insert into SongTable13 values(110,	'김조한',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable13 values(111,	'김조한',	'취중진담',	'전람회',	'3134',	'4772');");
		db.execSQL("insert into SongTable13 values(112,	'김조한',	'세월이 가면',	'최호섭',	'817',	'499');");
		db.execSQL("insert into SongTable13 values(113,	'김조한',	'천생연분',	'솔리드',	'3122',	'4713');");
		db.execSQL("insert into SongTable13 values(114,	'김조한',	'사랑하기 때문에',	'유재하',	'241',	'1679');");
		db.execSQL("insert into SongTable13 values(115,	'김조한',	'아름다운 이별',	'김건모',	'2451',	'3657');");
		db.execSQL("insert into SongTable13 values(116,	'김조한',	'나는 문제 없어',	'황규영',	'1983',	'3346');");
		db.execSQL("insert into SongTable13 values(117,	'더 원',	'지나간다',	'김범수',	'33120',	'47159');");
		db.execSQL("insert into SongTable13 values(118,	'더 원',	'그 남자 (시크릿가든 O.S.T)',	'현빈',	'33519',	'47254');");
		db.execSQL("insert into SongTable13 values(119,	'더 원',	'아시나요',	'조성모',	'9054',	'6512');");
		db.execSQL("insert into SongTable13 values(120,	'더 원',	'지난 날',	'유재하',	'1927',	'2536');");
		db.execSQL("insert into SongTable13 values(121,	'더 원',	'사랑아(내 남자의 여자 O.S.T)',	'더 원',	'17884',	'81766');");
		db.execSQL("insert into SongTable13 values(122,	'더 원',	'비상',	'임재범',	'9429',	'7153');");
		db.execSQL("insert into SongTable13 values(123,	'더 원',	'그녀를 사랑해줘요',	'하동균',	'16133',	'85143');");
		db.execSQL("insert into SongTable13 values(124,	'더 원',	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable13 values(125,	'더 원',	'If I Leave (나 가거든)',	'조수미',	'9744',	'7679');");
		db.execSQL("insert into SongTable13 values(127,	'더 원',	'썸데이 (싱글파파는 열애 중 O.S.T)',	'김동희',	'19281',	'46216');");
		db.execSQL("insert into SongTable13 values(128,	'더 원',	'You Raise Me Up',	'Westlife',	'21357',	'61922');");
		db.execSQL("insert into SongTable13 values(129,	'더 원',	'그 사람 (제빵왕 김탁구 O.S.T)',	'이승철',	'32826',	'47075');");
		db.execSQL("insert into SongTable13 values(130,	'더 원, 소향',	'천일동안',	'이승환',	'2679',	'3853');");
		db.execSQL("insert into SongTable13 values(131,	'리사',	'그녀의 웃음소리뿐',	'이문세',	'1572',	'2247');");
		db.execSQL("insert into SongTable13 values(132,	'바비킴',	'사랑.. 그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable13 values(133,	'바비킴',	'태양을 피하는 방법',	'비',	'12094',	'9573');");
		db.execSQL("insert into SongTable13 values(134,	'바비킴',	'너의 결혼식',	'윤종신',	'613',	'1515');");
		db.execSQL("insert into SongTable13 values(135,	'바비킴',	'골목길',	'신촌블루스',	'1196',	'141');");
		db.execSQL("insert into SongTable13 values(136,	'바비킴',	'추억 속의 재회',	'조용필',	'550',	'724');");
		db.execSQL("insert into SongTable13 values(137,	'바비킴',	'사랑 사랑 사랑',	'김현식',	'913',	'1501');");
		db.execSQL("insert into SongTable13 values(138,	'바비킴',	'만남',	'노사연',	'615',	'328');");
		db.execSQL("insert into SongTable13 values(139,	'바비킴',	'미워도 다시 한번',	'바이브',	'9855',	'7804');");
		db.execSQL("insert into SongTable13 values(140,	'바비킴',	'한 동안 뜸했었지',	'사랑과 평화',	'2054',	'2572');");
		db.execSQL("insert into SongTable13 values(141,	'바비킴',	'회상',	'산울림',	'16941',	'4638');");
		db.execSQL("insert into SongTable13 values(142,	'바비킴',	'가을을 남기고 간 사랑',	'패티김',	'360',	'106');");
		db.execSQL("insert into SongTable13 values(143,	'바비킴',	'Double',	'김건모',	'9558',	'6937');");
		db.execSQL("insert into SongTable13 values(144,	'바비킴, 부가킹즈',	'물레방아 인생',	'조영남',	'1153',	'2868');");
		db.execSQL("insert into SongTable13 values(145,	'박미경',	'이유같지 않은 이유',	'박미경',	'2335',	'3578');");
		db.execSQL("insert into SongTable13 values(146,	'박미경',	'원하고 원망하죠',	'애즈원',	'9716',	'7626');");
		db.execSQL("insert into SongTable13 values(147,	'박미경',	'무인도',	'김추자',	'704',	'358');");
		db.execSQL("insert into SongTable13 values(148,	'박미경',	'이별',	'패티김',	'553',	'632');");
		db.execSQL("insert into SongTable13 values(149,	'박미경',	'주저하지 말아요',	'방미',	'3355',	'2533');");
		db.execSQL("insert into SongTable13 values(150,	'박상민',	'멀어져 간 사람아',	'박상민',	'2064',	'3401');");
		db.execSQL("insert into SongTable13 values(151,	'박상민',	'내꺼하자',	'인피니트',	'34203',	'47454');");
		db.execSQL("insert into SongTable13 values(152,	'박상민',	'여정',	'왁스',	'9981',	'9108');");
		db.execSQL("insert into SongTable13 values(153,	'박상민',	'불 꺼진 창',	'조영남',	'215',	'421');");
		db.execSQL("insert into SongTable13 values(154,	'박상민',	'With Me',	'휘성',	'11861',	'63962');");
		db.execSQL("insert into SongTable13 values(155,	'박상민',	'When A Man Loves A Woman',	'Michael Bolton',	'7023',	'1986');");
		db.execSQL("insert into SongTable13 values(156,	'박상민',	'회상',	'김현식',	'1755',	'4234');");
		db.execSQL("insert into SongTable13 values(157,	'박상민',	'MaMa',	'바비킴',	'30679',	'86057');");
		db.execSQL("insert into SongTable13 values(158,	'박상민',	'이 밤이 지나면',	'임재범',	'1254',	'1238');");
		db.execSQL("insert into SongTable13 values(159,	'박상민',	'가슴 아파도 (패션 70s O.S.T)',	'플라이 투 더 스카이',	'14928',	'45232');");
		db.execSQL("insert into SongTable13 values(160,	'박상민',	'이별의 끝은 어디인가요',	'양수경',	'142',	'636');");
		db.execSQL("insert into SongTable13 values(161,	'박상민',	'사랑.. 그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable13 values(162,	'박상민',	'나의 꿈을 찾아서',	'권인하',	'947',	'1304');");
		db.execSQL("insert into SongTable13 values(163,	'박상민',	'본능적으로 (Feat.스윙스)',	'윤종신',	'32649',	'57928');");
		db.execSQL("insert into SongTable13 values(164,	'박완규',	'천 년의 사랑',	'박완규',	'8447',	'6048');");
		db.execSQL("insert into SongTable13 values(165,	'박완규',	'사랑했어요',	'김현식',	'1362',	'1195');");
		db.execSQL("insert into SongTable13 values(166,	'박완규',	'고해',	'임재범',	'9033',	'7033');");
		db.execSQL("insert into SongTable13 values(167,	'박완규',	'내일을 향해',	'신성우',	'949',	'1374');");
		db.execSQL("insert into SongTable13 values(168,	'박완규',	'어느 60대 노부부의 이야기',	'김광석',	'10092',	'6370');");
		db.execSQL("insert into SongTable13 values(169,	'박완규',	'아버지',	'김경호',	'11826',	'9505');");
		db.execSQL("insert into SongTable13 values(170,	'박완규',	'봄비',	'이정화(원곡) / 김추자(TJ) / 박완규(KY)',	'625',	'47751');");
		db.execSQL("insert into SongTable13 values(171,	'박완규',	'부치지 않은 편지',	'김광석',	'3649',	'4866');");
		db.execSQL("insert into SongTable13 values(172,	'박완규',	'소금인형',	'안치환',	'1684',	'6655');");
		db.execSQL("insert into SongTable13 values(173,	'박완규',	'비밀',	'부활',	'33554',	'47265');");
		db.execSQL("insert into SongTable13 values(174,	'박완규',	'영원',	'Sky(최진영)',	'8528',	'6138');");
		db.execSQL("insert into SongTable13 values(175,	'박완규',	'비련',	'조용필',	'373',	'4405');");
		db.execSQL("insert into SongTable13 values(176,	'박완규',	'Hello',	'Lionel Richie',	'7103',	'1065');");
		db.execSQL("insert into SongTable13 values(177,	'박완규, 소향',	'Beauty and The Beast',	'Celine Dion, Peabo Bryson',	'7445',	'2447');");
		db.execSQL("insert into SongTable13 values(178,	'박정현',	'꿈에',	'박정현',	'9973',	'7955');");
		db.execSQL("insert into SongTable13 values(179,	'박정현',	'비오는 날의 수채화',	'강인원, 권인하, 김현식',	'212',	'894');");
		db.execSQL("insert into SongTable13 values(180,	'박정현',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable13 values(181,	'박정현',	'미아',	'박정현',	'14590',	'64680');");
		db.execSQL("insert into SongTable13 values(182,	'박정현',	'이젠 그랬으면 좋겠네',	'조용필',	'13271',	'86701');");
		db.execSQL("insert into SongTable13 values(183,	'박정현',	'소나기',	'부활',	'2216',	'3427');");
		db.execSQL("insert into SongTable13 values(184,	'박정현',	'그대 내 품에',	'유재하(TJ) / 김현식(KY)',	'12766',	'62251');");
		db.execSQL("insert into SongTable13 values(185,	'박정현',	'내 낡은 서랍 속의 바다',	'패닉',	'4636',	'5489');");
		db.execSQL("insert into SongTable13 values(186,	'박정현',	'바보',	'박효신',	'8868',	'6328');");
		db.execSQL("insert into SongTable13 values(187,	'박정현',	'겨울비',	'김종서',	'1539',	'2066');");
		db.execSQL("insert into SongTable13 values(188,	'박정현',	'이브의 경고',	'박미경',	'2699',	'3854');");
		db.execSQL("insert into SongTable13 values(189,	'박정현',	'If I Leave (나 가거든)',	'조수미',	'9744',	'7679');");
		db.execSQL("insert into SongTable13 values(190,	'박정현',	'우연히',	'이정선',	'3150',	'정보없음');");
		db.execSQL("insert into SongTable13 values(191,	'박정현',	'그것만이 내 세상',	'들국화',	'1780',	'1329');");
		db.execSQL("insert into SongTable13 values(192,	'박정현',	'널 붙잡을 노래',	'비',	'32403',	'46980');");
		db.execSQL("insert into SongTable13 values(193,	'박희수',	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable13 values(194,	'백두산',	'말달리자',	'크라잉넛',	'4751',	'5603');");
		db.execSQL("insert into SongTable13 values(195,	'백두산',	'꿍따리 샤바라',	'클론',	'3159',	'4152');");
		db.execSQL("insert into SongTable13 values(196,	'백지영',	'총 맞은 것처럼',	'백지영',	'30425',	'83914');");
		db.execSQL("insert into SongTable13 values(197,	'백지영',	'무시로',	'나훈아',	'370',	'357');");
		db.execSQL("insert into SongTable13 values(198,	'백지영',	'약속',	'김범수',	'8153',	'5841');");
		db.execSQL("insert into SongTable13 values(199,	'변진섭',	'비와 당신 (라디오스타 O.S.T)',	'박중훈',	'16563',	'45718');");
		db.execSQL("insert into SongTable13 values(200,	'변진섭',	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable13 values(201,	'변진섭',	'잊지 말아요 (아이리스 O.S.T)',	'백지영',	'31760',	'46813');");
		db.execSQL("insert into SongTable13 values(202,	'변진섭',	'세월이 가면',	'최호섭',	'817',	'499');");
		db.execSQL("insert into SongTable13 values(203,	'변진섭',	'사랑합니다',	'팀(Tim)',	'11271',	'62988');");
		db.execSQL("insert into SongTable13 values(204,	'변진섭',	'별리',	'김수철',	'1166',	'2253');");
		db.execSQL("insert into SongTable13 values(205,	'변진섭',	'사랑',	'나훈아',	'646',	'897');");
		db.execSQL("insert into SongTable13 values(206,	'빨간 우체통',	'일어나',	'김광석',	'2225',	'3455');");
		db.execSQL("insert into SongTable13 values(207,	'서문탁',	'Black Dog',	'Led Zeppelin',	'정보없음',	'61942');");
		db.execSQL("insert into SongTable13 values(208,	'서문탁',	'그게 나였어',	'이문세',	'1958',	'2584');");
		db.execSQL("insert into SongTable13 values(209,	'서문탁',	'사랑비',	'김태우',	'31588',	'46773');");
		db.execSQL("insert into SongTable13 values(210,	'서문탁',	'기억 속으로',	'이은미',	'1612',	'3343');");
		db.execSQL("insert into SongTable13 values(211,	'서문탁',	'Maria (미녀는 괴로워 O.S.T)',	'김아중',	'16712',	'45783');");
		db.execSQL("insert into SongTable13 values(212,	'서문탁',	'미안해요',	'김건모',	'9520',	'6853');");
		db.execSQL("insert into SongTable13 values(213,	'서문탁',	'마주치지 말자',	'장혜진',	'15668',	'45480');");
		db.execSQL("insert into SongTable13 values(214,	'서문탁',	'Butterfly (국가대표 O.S.T)',	'러브홀릭스',	'30528',	'46517');");
		db.execSQL("insert into SongTable13 values(215,	'서문탁',	'등대지기',	'동요 / 은희(TJ) / 동요(KY)',	'239',	'82168');");
		db.execSQL("insert into SongTable13 values(216,	'서문탁',	'거위의 꿈',	'카니발',	'4241',	'5186');");
		db.execSQL("insert into SongTable13 values(217,	'서문탁',	'사미인곡',	'서문탁',	'9660',	'7587');");
		db.execSQL("insert into SongTable13 values(218,	'서문탁',	'아리랑',	'경기도민요',	'1', '525');");
		db.execSQL("insert into SongTable13 values(219,	'소찬휘',	'불티',	'전영록',	'920',	'1671');");
		db.execSQL("insert into SongTable13 values(220,	'소찬휘',	'고래사냥',	'송창식',	'789',	'808');");
		db.execSQL("insert into SongTable13 values(221,	'소찬휘',	'어떤가요',	'이정봉',	'3289',	'4710');");
		db.execSQL("insert into SongTable13 values(222,	'소찬휘',	'성인식',	'박지윤',	'8999',	'6493');");
		db.execSQL("insert into SongTable13 values(223,	'소향',	'I Have Nothing',	'Whitney Houston',	'7627',	'8866');");
		db.execSQL("insert into SongTable13 values(224,	'소향',	'꽃밭에서',	'정훈희',	'2960',	'3010');");
		db.execSQL("insert into SongTable13 values(225,	'소향',	'하늘을 달리다',	'이적',	'11378',	'63301');");
		db.execSQL("insert into SongTable13 values(226,	'소향',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable13 values(227,	'소향',	'꿈',	'POS(원곡) / 소향(TJ, KY)',	'36353',	'77520');");
		db.execSQL("insert into SongTable13 values(228,	'소향',	'그대는 어디에',	'임재범',	'4041',	'5083');");
		db.execSQL("insert into SongTable13 values(229,	'소향',	'살다가',	'SG워너비',	'14612',	'68949');");
		db.execSQL("insert into SongTable13 values(230,	'소향',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable13 values(231,	'소향',	'인연 (동녘바람)',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable13 values(232,	'소향',	'O Holy Night',	'캐럴 / Mariah Carey(TJ, KY)',	'20929',	'60531');");
		db.execSQL("insert into SongTable13 values(233,	'소향',	'그것만이 내 세상',	'들국화',	'1780',	'1329');");
		db.execSQL("insert into SongTable13 values(234,	'시나위',	'그건 너 (세상 밖으로 O.S.T)',	'이장희',	'4005',	'164');");
		db.execSQL("insert into SongTable13 values(235,	'시나위',	'나 어떡해',	'샌드 페블즈',	'414',	'1136');");
		db.execSQL("insert into SongTable13 values(236,	'시나위',	'강남스타일',	'싸이',	'35608',	'47802');");
		db.execSQL("insert into SongTable13 values(237,	'시나위',	'제발',	'들국화',	'3414',	'65612');");
		db.execSQL("insert into SongTable13 values(238,	'시나위',	'세상만사',	'송골매',	'12286',	'63642');");
		db.execSQL("insert into SongTable13 values(239,	'시나위',	'내 마음에 주단을 깔고',	'산울림',	'4957',	'63777');");
		db.execSQL("insert into SongTable13 values(240,	'신효범',	'이별연습',	'인순이',	'3499',	'3054');");
		db.execSQL("insert into SongTable13 values(241,	'신효범',	'미련한 사랑 (위기의 남자 O.S.T)',	'JK 김동욱',	'9952',	'7920');");
		db.execSQL("insert into SongTable13 values(242,	'신효범',	'떠나야 할 그 사람',	'펄 시스터즈',	'17198',	'4346');");
		db.execSQL("insert into SongTable13 values(243,	'신효범',	'세월 가면',	'이광조(원곡) / 신효범(TJ) / 이광조(KY)',	'35006',	'4455');");
		db.execSQL("insert into SongTable13 values(244,	'어반 자카파',	'어제처럼',	'J',	'8822',	'6318');");
		db.execSQL("insert into SongTable13 values(245,	'옥주현',	'천일동안',	'이승환',	'2679',	'3853');");
		db.execSQL("insert into SongTable13 values(246,	'옥주현',	'사랑이 떠나가네',	'김건모',	'4300',	'5208');");
		db.execSQL("insert into SongTable13 values(247,	'옥주현',	'서시',	'신성우',	'2135',	'3446');");
		db.execSQL("insert into SongTable13 values(248,	'옥주현',	'LOVE',	'조장혁',	'9165',	'6540');");
		db.execSQL("insert into SongTable13 values(249,	'옥주현',	'U-Go-Girl (Feat.낯선)',	'이효리',	'19854',	'83680');");
		db.execSQL("insert into SongTable13 values(250,	'옥주현',	'남자는 배 여자는 항구',	'심수봉',	'4021',	'223');");
		db.execSQL("insert into SongTable13 values(251,	'유리',	'I Will Always Love You',	'Whitney Houston',	'7107',	'2045');");
		db.execSQL("insert into SongTable13 values(252,	'윤도현',	'It Burns',	'윤도현밴드',	'33984',	'58244');");
		db.execSQL("insert into SongTable13 values(253,	'윤도현',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable13 values(254,	'윤도현',	'Dash',	'백지영',	'8816',	'6296');");
		db.execSQL("insert into SongTable13 values(255,	'윤도현',	'나는 나비',	'윤도현밴드',	'16329',	'85183');");
		db.execSQL("insert into SongTable13 values(256,	'윤도현',	'마법의 성',	'더 클래식',	'2238',	'3514');");
		db.execSQL("insert into SongTable13 values(257,	'윤도현',	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable13 values(258,	'윤도현',	'해야',	'마그마',	'2145',	'4626');");
		db.execSQL("insert into SongTable13 values(259,	'윤도현',	'새벽기차',	'다섯손가락',	'94',	'913');");
		db.execSQL("insert into SongTable13 values(260,	'윤도현',	'커피 한 잔',	'펄 시스터즈',	'141',	'736');");
		db.execSQL("insert into SongTable13 values(261,	'윤도현',	'빙글빙글',	'나미',	'139',	'436');");
		db.execSQL("insert into SongTable13 values(262,	'윤도현',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable13 values(263,	'윤도현',	'크게 라디오를 켜고',	'시나위',	'9058',	'62163');");
		db.execSQL("insert into SongTable13 values(264,	'윤도현',	'삐딱하게',	'강산에(원곡) / 윤도현(TJ, KY)',	'34279',	'76998');");
		db.execSQL("insert into SongTable13 values(265,	'윤도현',	'내 사람이여',	'윤도현(TJ) / 이동원(KY)',	'34283',	'2332');");
		db.execSQL("insert into SongTable13 values(266,	'윤도현',	'붉은 노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable13 values(267,	'윤민수',	'그 남자 그 여자 (Feat.장혜진)',	'바이브',	'15757',	'45492');");
		db.execSQL("insert into SongTable13 values(268,	'윤민수',	'술이야',	'바이브',	'15819',	'69848');");
		db.execSQL("insert into SongTable13 values(269,	'윤민수',	'그리움만 쌓이네',	'노영심',	'2618',	'3782');");
		db.execSQL("insert into SongTable13 values(270,	'윤민수',	'사랑 그 쓸쓸함에 대하여',	'양희은',	'4226',	'5193');");
		db.execSQL("insert into SongTable13 values(271,	'윤민수',	'님은 먼 곳에',	'김추자',	'596',	'261');");
		db.execSQL("insert into SongTable13 values(272,	'윤민수',	'창 밖의 여자',	'조용필',	'567',	'1797');");
		db.execSQL("insert into SongTable13 values(273,	'윤민수',	'만약에',	'태연',	'19187',	'83377');");
		db.execSQL("insert into SongTable13 values(274,	'윤민수',	'기억상실',	'거미',	'13902',	'9888');");
		db.execSQL("insert into SongTable13 values(275,	'윤민수',	'빗속의 여인',	'김건모',	'9590',	'7127');");
		db.execSQL("insert into SongTable13 values(276,	'윤민수',	'나 어떡해',	'샌드 페블즈',	'414',	'1136');");
		db.execSQL("insert into SongTable13 values(277,	'윤민수',	'어머님께',	'god',	'4988',	'5783');");
		db.execSQL("insert into SongTable13 values(278,	'윤민수',	'꽃피는 봄이 오면',	'BMK',	'14503',	'45112');");
		db.execSQL("insert into SongTable13 values(279,	'윤민수',	'집시여인',	'이치현과 벗님들',	'20',	'696');");
		db.execSQL("insert into SongTable13 values(280,	'윤민수',	'잊지 말아요 (아이리스 O.S.T)',	'백지영',	'31760',	'46813');");
		db.execSQL("insert into SongTable13 values(281,	'윤민수, 이영현',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable13 values(282,	'윤하',	'먼 훗날에',	'박정운',	'1388',	'1493');");
		db.execSQL("insert into SongTable13 values(283,	'윤하',	'내 곁에서 떠나가지 말아요',	'빛과 소금',	'10458',	'63799');");
		db.execSQL("insert into SongTable13 values(284,	'윤하',	'서쪽하늘 (청연 O.S.T)',	'이승철',	'15527',	'45430');");
		db.execSQL("insert into SongTable13 values(285,	'윤하',	'날 울리지마',	'신승훈',	'715',	'1039');");
		db.execSQL("insert into SongTable13 values(286,	'윤하',	'I Don＇t Care',	'2NE1',	'31348',	'46712');");
		db.execSQL("insert into SongTable13 values(287,	'윤하',	'붉은 노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable13 values(288,	'윤하',	'일생을',	'김현철',	'3488',	'4814');");
		db.execSQL("insert into SongTable13 values(289,	'윤하',	'오직 너뿐인 나를',	'이승철',	'8100',	'5842');");
		db.execSQL("insert into SongTable13 values(290,	'윤하, 이정',	'손에 손 잡고',	'코리아나',	'1335',	'1694');");
		db.execSQL("insert into SongTable13 values(291,	'이소라',	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable13 values(292,	'이소라',	'너에게로 또 다시',	'변진섭',	'725',	'239');");
		db.execSQL("insert into SongTable13 values(293,	'이소라',	'나의 하루',	'박정현',	'4478',	'5402');");
		db.execSQL("insert into SongTable13 values(294,	'이소라',	'나를 사랑하지 않는 그대에게',	'이소라',	'13560',	'77353');");
		db.execSQL("insert into SongTable13 values(295,	'이소라',	'No.1',	'보아',	'9858',	'7868');");
		db.execSQL("insert into SongTable13 values(296,	'이소라',	'사랑이야',	'송창식',	'192',	'1678');");
		db.execSQL("insert into SongTable13 values(297,	'이소라',	'주먹이 운다 (Feat.임재범)',	'소울다이브',	'33899',	'76902');");
		db.execSQL("insert into SongTable13 values(298,	'이소라',	'행복을 주는 사람',	'해바라기',	'2554',	'3554');");
		db.execSQL("insert into SongTable13 values(299,	'이소라',	'슬픔 속에 그댈 지워야만 해',	'이현우',	'1108',	'2133');");
		db.execSQL("insert into SongTable13 values(300,	'이수영',	'휠릴리',	'이수영',	'13913',	'9886');");
		db.execSQL("insert into SongTable13 values(301,	'이수영',	'인연 (동녘바람)',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable13 values(302,	'이수영',	'인연 (불새 O.S.T)',	'이승철',	'13165',	'9775');");
		db.execSQL("insert into SongTable13 values(303,	'이수영',	'제 3한강교',	'혜은이',	'1696',	'1250');");
		db.execSQL("insert into SongTable13 values(304,	'이수영',	'옛 이야기',	'김규민',	'1164',	'1049');");
		db.execSQL("insert into SongTable13 values(305,	'이수영',	'Donde Voy',	'Tish Hinojosa',	'7204',	'8836');");
		db.execSQL("insert into SongTable13 values(306,	'이수영',	'눈의 꽃',	'박효신',	'14238',	'68590');");
		db.execSQL("insert into SongTable13 values(307,	'이영현',	'연',	'빅마마',	'16627',	'85250');");
		db.execSQL("insert into SongTable13 values(308,	'이영현',	'슬프도록 아름다운',	'K2',	'2482',	'3733');");
		db.execSQL("insert into SongTable13 values(309,	'이영현',	'천 년의 사랑',	'박완규',	'8447',	'6048');");
		db.execSQL("insert into SongTable13 values(310,	'이영현',	'바람의 노래',	'조용필',	'3880',	'4983');");
		db.execSQL("insert into SongTable13 values(311,	'이영현',	'뮤지컬',	'임상아',	'3643',	'4877');");
		db.execSQL("insert into SongTable13 values(312,	'이영현',	'칠갑산',	'주병선',	'777',	'733');");
		db.execSQL("insert into SongTable13 values(313,	'이영현',	'이미 슬픈 사랑',	'야다',	'8122',	'5830');");
		db.execSQL("insert into SongTable13 values(314,	'이영현',	'I Will Always Love You',	'Whitney Houston',	'7107',	'2045');");
		db.execSQL("insert into SongTable13 values(315,	'이영현',	'찰랑찰랑',	'이자연',	'2689',	'3916');");
		db.execSQL("insert into SongTable13 values(316,	'이영현',	'Tears',	'소찬휘',	'8797',	'6286');");
		db.execSQL("insert into SongTable13 values(317,	'이영현',	'기억 속의 먼 그대에게',	'박미경',	'3671',	'4891');");
		db.execSQL("insert into SongTable13 values(318,	'이영현',	'사랑아(내 남자의 여자 O.S.T)',	'더 원',	'17884',	'81766');");
		db.execSQL("insert into SongTable13 values(319,	'이영현',	'잠시만 안녕',	'M.C. The Max',	'10290',	'9140');");
		db.execSQL("insert into SongTable13 values(320,	'이영현',	'가시',	'버즈',	'14684',	'69033');");
		db.execSQL("insert into SongTable13 values(321,	'이영현',	'너를 위해 (동감 O.S.T)',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable13 values(322,	'이영현',	'사랑앓이',	'F.T Island',	'18104',	'45962');");
		db.execSQL("insert into SongTable13 values(323,	'이영현',	'The Water Is Wide',	'스코틀랜드 민요 / Karla Bonoff(TJ, KY)',	'7742',	'2387');");
		db.execSQL("insert into SongTable13 values(324,	'이은미',	'녹턴',	'이은미',	'32718',	'86559');");
		db.execSQL("insert into SongTable13 values(325,	'이은미',	'좋은 사람',	'박효신',	'10036',	'9060');");
		db.execSQL("insert into SongTable13 values(326,	'이은미',	'한계령',	'양희은',	'1932',	'2390');");
		db.execSQL("insert into SongTable13 values(327,	'이은미',	'얘기할 수 없어요',	'사랑과 평화',	'2077',	'4217');");
		db.execSQL("insert into SongTable13 values(328,	'이은미',	'한 동안 뜸했었지',	'사랑과 평화',	'2054',	'2572');");
		db.execSQL("insert into SongTable13 values(329,	'이은미',	'세상에서 가장 큰 피그미',	'더 리딩클럽(원곡) / 이은미(TJ, KY)',	'35417',	'47760');");
		db.execSQL("insert into SongTable13 values(330,	'이은미',	'하루',	'김범수',	'9337',	'6730');");
		db.execSQL("insert into SongTable13 values(331,	'이은미',	'Love Hurts',	'Nazareth',	'20000',	'8556');");
		db.execSQL("insert into SongTable13 values(332,	'이은미',	'아웃사이더',	'봄 여름 가을 겨울',	'372',	'1361');");
		db.execSQL("insert into SongTable13 values(333,	'이은미',	'너는 아름답다',	'이은미',	'35331',	'77268');");
		db.execSQL("insert into SongTable13 values(334,	'이은미',	'365일',	'알리',	'31733',	'84507');");
		db.execSQL("insert into SongTable13 values(335,	'이은미',	'Born To Be Wild',	'Steppenwolf',	'7070',	'8036');");
		db.execSQL("insert into SongTable13 values(336,	'이은미',	'너를 위해 (동감 O.S.T)',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable13 values(337,	'이은미',	'내 하나의 사람은 가고',	'임희숙',	'502',	'234');");
		db.execSQL("insert into SongTable13 values(338,	'이은미',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable13 values(339,	'이은미',	'깨어나',	'강산에(원곡) / 윤도현(TJ, KY)',	'5121',	'63507');");
		db.execSQL("insert into SongTable13 values(340,	'이은미',	'Both Sides Now',	'Joni Mitchell',	'7361',	'8351');");
		db.execSQL("insert into SongTable13 values(341,	'이은미',	'나만 몰랐던 이야기',	'아이유',	'33651',	'47295');");
		db.execSQL("insert into SongTable13 values(342,	'이정',	'말리꽃 (비천무 O.S.T)',	'이승철',	'8941',	'6438');");
		db.execSQL("insert into SongTable13 values(343,	'이정',	'그 날들',	'김광석',	'4248',	'7054');");
		db.execSQL("insert into SongTable13 values(344,	'이정',	'마도요',	'조용필',	'739',	'2342');");
		db.execSQL("insert into SongTable13 values(345,	'이정',	'그대는 모릅니다',	'이승환',	'8054',	'5804');");
		db.execSQL("insert into SongTable13 values(346,	'이현우',	'이 밤을 다시 한 번',	'조하문',	'481',	'631');");
		db.execSQL("insert into SongTable13 values(347,	'이현우',	'그냥 걸었어',	'임종환',	'1985',	'3337');");
		db.execSQL("insert into SongTable13 values(348,	'인순이',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable13 values(349,	'인순이',	'난 괜찮아',	'진주',	'4263',	'5188');");
		db.execSQL("insert into SongTable13 values(350,	'인순이',	'서른 즈음에',	'김광석',	'2337',	'4448');");
		db.execSQL("insert into SongTable13 values(351,	'인순이',	'오늘 같은 밤이면',	'박정운',	'1249',	'1292');");
		db.execSQL("insert into SongTable13 values(352,	'인순이',	'그 겨울의 찻집',	'조용필',	'1481',	'1454');");
		db.execSQL("insert into SongTable13 values(353,	'인순이',	'봄 여름 가을 겨울',	'김현식',	'4199',	'4396');");
		db.execSQL("insert into SongTable13 values(354,	'인순이',	'토요일은 밤이 좋아',	'김종찬',	'451',	'745');");
		db.execSQL("insert into SongTable13 values(355,	'인순이',	'금지된 사랑',	'김경호',	'4074',	'5114');");
		db.execSQL("insert into SongTable13 values(356,	'인순이',	'나만의 슬픔',	'김돈규',	'3504',	'4835');");
		db.execSQL("insert into SongTable13 values(357,	'인순이',	'청춘',	'산울림',	'438',	'2611');");
		db.execSQL("insert into SongTable13 values(358,	'인순이, 김도향',	'바보처럼 살았군요',	'김도향',	'292',	'392');");
		db.execSQL("insert into SongTable13 values(359,	'임재범',	'너를 위해 (동감 O.S.T)',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable13 values(360,	'임재범',	'빈 잔',	'남진',	'82',	'433');");
		db.execSQL("insert into SongTable13 values(361,	'임재범',	'여러분',	'윤복희',	'534',	'1310');");
		db.execSQL("insert into SongTable13 values(362,	'자우림',	'고래사냥',	'송창식',	'789',	'808');");
		db.execSQL("insert into SongTable13 values(363,	'자우림',	'뜨거운 안녕',	'쟈니 리',	'736',	'310');");
		db.execSQL("insert into SongTable13 values(364,	'자우림',	'매직카펫라이드',	'자우림',	'8940',	'6404');");
		db.execSQL("insert into SongTable13 values(365,	'자우림',	'왼손잡이',	'패닉',	'3070',	'4147');");
		db.execSQL("insert into SongTable13 values(366,	'자우림',	'재즈 카페',	'신해철',	'6',	'1315');");
		db.execSQL("insert into SongTable13 values(367,	'자우림',	'가시나무',	'시인과 촌장',	'3025',	'4204');");
		db.execSQL("insert into SongTable13 values(368,	'자우림',	'꿈',	'조용필',	'693',	'1036');");
		db.execSQL("insert into SongTable13 values(369,	'자우림',	'라구요',	'강산에',	'1507',	'1428');");
		db.execSQL("insert into SongTable13 values(370,	'자우림',	'Abracadabra (아브라카다브라)',	'브라운아이드걸스',	'31429',	'46732');");
		db.execSQL("insert into SongTable13 values(371,	'자우림',	'1994년 어느 늦은 밤',	'장혜진',	'2432',	'3641');");
		db.execSQL("insert into SongTable13 values(372,	'자우림',	'얘기할 수 없어요',	'사랑과 평화',	'2077',	'4217');");
		db.execSQL("insert into SongTable13 values(373,	'자우림',	'내 마음에 주단을 깔고',	'산울림',	'4957',	'63777');");
		db.execSQL("insert into SongTable13 values(374,	'자우림',	'정신차려',	'김수철',	'1331',	'684');");
		db.execSQL("insert into SongTable13 values(375,	'자우림',	'하루',	'김범수',	'9337',	'6730');");
		db.execSQL("insert into SongTable13 values(376,	'자우림, 백현진',	'사랑밖엔 난 몰라',	'심수봉',	'1486',	'1468');");
		db.execSQL("insert into SongTable13 values(377,	'장혜진',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable13 values(378,	'장혜진',	'가까이 하기엔 너무 먼 당신',	'이광조',	'1252',	'1103');");
		db.execSQL("insert into SongTable13 values(379,	'장혜진',	'미스터',	'카라',	'31473',	'84409');");
		db.execSQL("insert into SongTable13 values(380,	'장혜진',	'술이야',	'바이브',	'15819',	'69848');");
		db.execSQL("insert into SongTable13 values(381,	'장혜진',	'애모',	'김수희',	'1427',	'1983');");
		db.execSQL("insert into SongTable13 values(382,	'장혜진',	'누구 없소',	'한영애',	'691',	'250');");
		db.execSQL("insert into SongTable13 values(383,	'장혜진',	'아름다운 날들',	'장혜진',	'9524',	'6878');");
		db.execSQL("insert into SongTable13 values(384,	'장혜진',	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable13 values(385,	'장혜진',	'멀어져 간 사람아',	'박상민',	'2064',	'3401');");
		db.execSQL("insert into SongTable13 values(386,	'장혜진',	'그대와 영원히',	'이문세',	'2606',	'3656');");
		db.execSQL("insert into SongTable13 values(387,	'장혜진',	'모나리자',	'조용필',	'1868',	'1665');");
		db.execSQL("insert into SongTable13 values(388,	'장혜진',	'미소 속에 비친 그대',	'신승훈',	'182',	'370');");
		db.execSQL("insert into SongTable13 values(389,	'장혜진',	'분홍립스틱',	'강애리자',	'2175',	'1414');");
		db.execSQL("insert into SongTable13 values(390,	'장혜진',	'사랑.. 그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable13 values(391,	'장혜진, 김조한',	'이별 이야기',	'이문세, 고은희',	'1408',	'3335');");
		db.execSQL("insert into SongTable13 values(392,	'적우',	'열애',	'윤시내',	'387',	'586');");
		db.execSQL("insert into SongTable13 values(393,	'적우',	'나 홀로 뜰 앞에서',	'김완선',	'1288',	'2603');");
		db.execSQL("insert into SongTable13 values(394,	'적우',	'어떤 이의 꿈',	'봄 여름 가을 겨울',	'1175',	'562');");
		db.execSQL("insert into SongTable13 values(395,	'적우',	'처음 느낌 그대로',	'이소라',	'2917',	'4046');");
		db.execSQL("insert into SongTable13 values(396,	'적우',	'사람이 꽃보다 아름다워',	'안치환',	'4704',	'5516');");
		db.execSQL("insert into SongTable13 values(397,	'적우',	'이등병의 편지',	'김광석',	'1999',	'3425');");
		db.execSQL("insert into SongTable13 values(398,	'적우',	'어둠 그 별빛',	'김현식',	'2368',	'3633');");
		db.execSQL("insert into SongTable13 values(399,	'적우',	'저 꽃 속에 찬란한 빛이',	'박경희',	'111',	'969');");
		db.execSQL("insert into SongTable13 values(400,	'정엽',	'Nothing Better',	'정엽',	'33010',	'86615');");
		db.execSQL("insert into SongTable13 values(401,	'정엽',	'짝사랑',	'주현미',	'233',	'664');");
		db.execSQL("insert into SongTable13 values(402,	'정엽',	'잊을께',	'윤도현밴드',	'11761',	'63748');");
		db.execSQL("insert into SongTable13 values(403,	'정엽',	'잘 몰랐었다',	'정엽',	'34988',	'87273');");
		db.execSQL("insert into SongTable13 values(404,	'정엽',	'꿈에',	'조덕배',	'1515',	'2278');");
		db.execSQL("insert into SongTable13 values(405,	'정엽',	'나만의 것',	'김완선',	'940',	'208');");
		db.execSQL("insert into SongTable13 values(406,	'정엽',	'보이네',	'나미',	'666',	'1415');");
		db.execSQL("insert into SongTable13 values(407,	'정엽',	'난 행복해',	'이소라',	'2742',	'3924');");
		db.execSQL("insert into SongTable13 values(408,	'정엽',	'Hound Dog',	'Big Mama Thornton(원곡) / Elvis Presley(TJ, KY)',	'7135',	'8093');");
		db.execSQL("insert into SongTable13 values(409,	'정엽',	'사랑 사랑 사랑',	'김현식',	'913',	'1501');");
		db.execSQL("insert into SongTable13 values(410,	'정엽',	'총 맞은 것처럼',	'백지영',	'30425',	'83914');");
		db.execSQL("insert into SongTable13 values(411,	'정엽',	'제주도의 푸른 밤',	'최성원',	'2310',	'2633');");
		db.execSQL("insert into SongTable13 values(412,	'정엽',	'I Just Called To Say I Love You (Woman In Red O.S.T)',	'Stevie Wonder',	'7125',	'1068');");
		db.execSQL("insert into SongTable13 values(413,	'정엽',	'매일 그대와',	'최성원(원곡) / 들국화(TJ, KY)',	'1905',	'4360');");
		db.execSQL("insert into SongTable13 values(414,	'정인',	'미워요',	'정인',	'32340',	'46956');");
		db.execSQL("insert into SongTable13 values(415,	'정인',	'우리의 밤은 당신의 낮보다 아름답다',	'코나',	'3133',	'4161');");
		db.execSQL("insert into SongTable13 values(416,	'정인',	'사노라면',	'쟈니 리(원곡) / 들국화(TJ, KY)',	'1758',	'2411');");
		db.execSQL("insert into SongTable13 values(417,	'정인',	'불티',	'전영록',	'920',	'1671');");
		db.execSQL("insert into SongTable13 values(418,	'정인',	'내게로',	'장혜진',	'2413',	'3618');");
		db.execSQL("insert into SongTable13 values(419,	'정인',	'Calling You',	'Jevetta Steele',	'20047',	'60971');");
		db.execSQL("insert into SongTable13 values(420,	'정인',	'이별의 그늘',	'윤상',	'695',	'635');");
		db.execSQL("insert into SongTable13 values(421,	'조관우',	'이별여행',	'원미연',	'391',	'634');");
		db.execSQL("insert into SongTable13 values(422,	'조관우',	'하얀 나비',	'김정호',	'140',	'755');");
		db.execSQL("insert into SongTable13 values(423,	'조관우',	'남행열차',	'김수희',	'911',	'1367');");
		db.execSQL("insert into SongTable13 values(424,	'조관우',	'화요일에 비가 내리면',	'박미경',	'2916',	'1319');");
		db.execSQL("insert into SongTable13 values(425,	'조관우',	'고향역',	'나훈아',	'3457',	'135');");
		db.execSQL("insert into SongTable13 values(426,	'조관우',	'그대 내 맘에 들어오면은',	'조덕배',	'910',	'2249');");
		db.execSQL("insert into SongTable13 values(427,	'조관우',	'사랑했으므로',	'조관우',	'9364',	'6719');");
		db.execSQL("insert into SongTable13 values(428,	'조관우',	'그대는 어디에',	'임재범',	'4041',	'5083');");
		db.execSQL("insert into SongTable13 values(429,	'조관우',	'달의 몰락',	'김현철',	'1843',	'3310');");
		db.execSQL("insert into SongTable13 values(430,	'조관우',	'이름 모를 소녀',	'김정호',	'281',	'630');");
		db.execSQL("insert into SongTable13 values(431,	'조관우',	'단발머리',	'조용필',	'635',	'1461');");
		db.execSQL("insert into SongTable13 values(432,	'조규찬',	'이별이란 없는거야',	'최성원',	'1820',	'2186');");
		db.execSQL("insert into SongTable13 values(433,	'조규찬, 박기영',	'이 밤이 지나면',	'임재범',	'1254',	'1238');");
		db.execSQL("insert into SongTable13 values(434,	'조장혁',	'꿈',	'조용필',	'693',	'1036');");
		db.execSQL("insert into SongTable13 values(435,	'조장혁',	'소리쳐',	'이승철',	'16452',	'45713');");
		db.execSQL("insert into SongTable13 values(436,	'조장혁',	'내 아픔 아시는 당신께',	'조하문',	'121',	'840');");
		db.execSQL("insert into SongTable13 values(437,	'조장혁',	'거리에서',	'동물원',	'1332',	'1326');");
		db.execSQL("insert into SongTable13 values(438,	'지영선',	'Memory',	'김범수',	'14509',	'45119');");
		db.execSQL("insert into SongTable13 values(439,	'카이',	'대답 없는 너',	'김종서',	'1309',	'1300');");
		db.execSQL("insert into SongTable13 values(440,	'카이',	'가시나무',	'시인과 촌장',	'3025',	'4204');");
		db.execSQL("insert into SongTable13 values(441,	'타루',	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable13 values(442,	'테이',	'넌 할 수 있어',	'강산에',	'2110',	'3438');");
		db.execSQL("insert into SongTable13 values(443,	'테이',	'내 생에 봄날은',	'캔',	'9763',	'7655');");
		db.execSQL("insert into SongTable13 values(444,	'한영애',	'이별의 종착역',	'손시향',	'1246',	'950');");
		db.execSQL("insert into SongTable13 values(445,	'한영애',	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable13 values(446,	'한영애',	'Knockin＇ on Heaven＇s Door',	'Bob Dylan',	'7703',	'8302');");
		db.execSQL("insert into SongTable13 values(447,	'한영애',	'미지의 세계',	'조용필',	'837',	'1465');");
		db.execSQL("insert into SongTable13 values(448,	'한영애',	'옛 사랑',	'이문세',	'916',	'1364');");
		db.execSQL("insert into SongTable13 values(449,	'한영애',	'사랑한 후에',	'들국화',	'1844',	'3422');");
		db.execSQL("insert into SongTable13 values(450,	'한영애',	'Sunny (써니 O.S.T)',	'Boney M(원곡) / Bobby Hebb(TJ, KY)',	'7408',	'8688');");
		db.execSQL("insert into SongTable13 values(451,	'한영애',	'라구요',	'강산에',	'1507',	'1428');");
		db.execSQL("insert into SongTable13 values(452,	'한영애',	'바람기억',	'나얼',	'35884',	'47870');");
		db.execSQL("insert into SongTable13 values(453,	'한영애',	'사랑 그 쓸쓸함에 대하여',	'양희은',	'4226',	'5193');");
		db.execSQL("insert into SongTable13 values(454,	'한영애',	'회상 1',	'부활',	'8601',	'69673');");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable13");
		onCreate(db);
	}
}