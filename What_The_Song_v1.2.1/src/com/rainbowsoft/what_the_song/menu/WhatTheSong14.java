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

public class WhatTheSong14 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_14);

		//initAdam();

		My_list = (ImageView) findViewById(R.id.my_list);
		My_list.setOnClickListener(this);
		Refresh = (ImageView) findViewById(R.id.refresh);
		Refresh.setOnClickListener(this);
		SearchText = (EditText) findViewById(R.id.searchText);
		Search = (ImageView) findViewById(R.id.search);
		Search.setOnClickListener(this);
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_14",
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
		cursor = db.rawQuery("SELECT * FROM SongTable14 WHERE song LIKE '"
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

		WhatTheSongDBHelper14 wtsHelper14 = new WhatTheSongDBHelper14(this, "whatthesong14.db", null, DATABASE_VERSION);
		db = wtsHelper14.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable14 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable14 where id= '"
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
		new AlertDialog.Builder(WhatTheSong14.this)
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
											WhatTheSong14.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong14.this,
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
			Intent newActivity = new Intent(WhatTheSong14.this,
					WhatTheSongBookmark.class);
			WhatTheSong14.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
		} else if (v.getId() == R.id.search) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
			String edit = SearchText.getText().toString();
			cursor = db.rawQuery("SELECT * FROM SongTable14 WHERE song LIKE '"
					+ edit + "%' OR song LIKE '%" + edit +"%' OR song LIKE '%" + edit + "' OR singer LIKE '"
					+ edit + "%' OR singer LIKE '%" + edit +"%' OR singer LIKE '%" + edit + "' OR participant LIKE '"
					+ edit + "%' OR participant LIKE '%" + edit +"%' OR participant LIKE '%" + edit + "'", null);
			if (cursor.getCount() == 0) {
				Toast.makeText(WhatTheSong14.this, "검색 결과가 없습니다.",
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
					"m_pref_14", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper14 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper14(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '불후의 명곡 테마' DB (130627 : 647개)
		db.execSQL("CREATE TABLE SongTable14 ( id INTEGER ,"
				+ " participant TEXT ," + " song TEXT ," + " singer TEXT ,"
				+ " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable14 values(1,	'B1A4',	'차표 한 장',	'송대관',	'956',	'1297');");
		db.execSQL("insert into SongTable14 values(2,	'B1A4',	'무조건',	'박상철',	'14712',	'45167');");
		db.execSQL("insert into SongTable14 values(3,	'B1A4',	'아리랑 목동',	'하춘화(원곡) / 김치켓(TJ, KY)',	'1177',	'924');");
		db.execSQL("insert into SongTable14 values(4,	'B1A4',	'잊혀진 계절',	'이용',	'512',	'659');");
		db.execSQL("insert into SongTable14 values(5,	'B1A4',	'비 내리는 명동 거리',	'배호',	'210',	'425');");
		db.execSQL("insert into SongTable14 values(6,	'B1A4',	'남자답게 사는 법',	'김영배',	'2404',	'3625');");
		db.execSQL("insert into SongTable14 values(7,	'B1A4, 김유정',	'White Love (스키장에서)',	'터보',	'4923',	'4854');");
		db.execSQL("insert into SongTable14 values(8,	'DK(디셈버)',	'미안해요',	'김건모',	'9520',	'6853');");
		db.execSQL("insert into SongTable14 values(9,	'DK(디셈버)',	'종이학',	'전영록',	'1179',	'2528');");
		db.execSQL("insert into SongTable14 values(10,	'DK(디셈버)',	'사랑하고 싶어',	'소방차',	'18',	'907');");
		db.execSQL("insert into SongTable14 values(11,	'DK(디셈버)',	'가슴이 떨려',	'김정수',	'1943',	'2260');");
		db.execSQL("insert into SongTable14 values(12,	'DK(디셈버)',	'내 님의 사랑은',	'양희은',	'529',	'838');");
		db.execSQL("insert into SongTable14 values(13,	'JK김동욱',	'백만송이 장미',	'심수봉',	'8093',	'5273');");
		db.execSQL("insert into SongTable14 values(14,	'JK김동욱',	'그대 나를 보면',	'이문세',	'2147',	'3996');");
		db.execSQL("insert into SongTable14 values(15,	'Lucia(심규선)',	'미련',	'김건모',	'3136',	'4168');");
		db.execSQL("insert into SongTable14 values(16,	'miss A',	'마지막잎새',	'배호',	'728',	'325');");
		db.execSQL("insert into SongTable14 values(17,	'UV',	'눈동자',	'엄정화',	'1423',	'2725');");
		db.execSQL("insert into SongTable14 values(18,	'강민경',	'타타타',	'김국환',	'1168',	'1102');");
		db.execSQL("insert into SongTable14 values(19,	'강민경',	'이 밤의 끝을 잡고',	'솔리드',	'2589',	'3779');");
		db.execSQL("insert into SongTable14 values(20,	'강민경',	'잊어야 한다는 마음으로',	'김광석',	'2141',	'2787');");
		db.execSQL("insert into SongTable14 values(21,	'강민경',	'사랑은 연필로 쓰세요',	'전영록',	'4016',	'450');");
		db.execSQL("insert into SongTable14 values(22,	'강민경',	'처음 본 순간',	'송골매',	'3441',	'4590');");
		db.execSQL("insert into SongTable14 values(23,	'강민경',	'당신은 모르실거야',	'혜은이',	'477',	'273');");
		db.execSQL("insert into SongTable14 values(24,	'강민경',	'사랑 사랑 사랑',	'김현식',	'913',	'1501');");
		db.execSQL("insert into SongTable14 values(25,	'강민경',	'물레방아 인생',	'조영남',	'1153',	'2868');");
		db.execSQL("insert into SongTable14 values(26,	'강민경',	'둘이서',	'채연',	'14373',	'45074');");
		db.execSQL("insert into SongTable14 values(27,	'강민경',	'시청 앞 지하철 역에서',	'동물원',	'1809',	'1503');");
		db.execSQL("insert into SongTable14 values(28,	'강민경',	'공항의 이별',	'문주란',	'785',	'142');");
		db.execSQL("insert into SongTable14 values(29,	'강민경',	'사랑이 떠나가네',	'김건모',	'4300',	'5208');");
		db.execSQL("insert into SongTable14 values(30,	'강민경',	'이별',	'패티김',	'553',	'632');");
		db.execSQL("insert into SongTable14 values(31,	'강민경',	'널 그리며',	'박남정',	'351',	'241');");
		db.execSQL("insert into SongTable14 values(32,	'고유진',	'무인도',	'김추자',	'704',	'358');");
		db.execSQL("insert into SongTable14 values(33,	'규현(슈퍼주니어)',	'너무합니다',	'김수희',	'3415',	'238');");
		db.execSQL("insert into SongTable14 values(34,	'규현(슈퍼주니어)',	'가장무도회',	'김완선',	'1248',	'107');");
		db.execSQL("insert into SongTable14 values(35,	'규현(슈퍼주니어)',	'기억의 습작',	'전람회',	'2200',	'3462');");
		db.execSQL("insert into SongTable14 values(36,	'규현(슈퍼주니어)',	'님과 함께',	'남진',	'411',	'648');");
		db.execSQL("insert into SongTable14 values(37,	'규현(슈퍼주니어), 김민종',	'그대와 함께 (느낌 O.S.T)',	'더 블루',	'31180',	'86184');");
		db.execSQL("insert into SongTable14 values(38,	'규현(슈퍼주니어), 손호영',	'거짓말',	'god',	'9213',	'6567');");
		db.execSQL("insert into SongTable14 values(39,	'김다현',	'이름 모를 소녀',	'김정호',	'281',	'630');");
		db.execSQL("insert into SongTable14 values(40,	'김다현',	'희망사항',	'변진섭',	'829',	'798');");
		db.execSQL("insert into SongTable14 values(41,	'김다현',	'미소를 띄우며 나를 보낸 그 모습처럼',	'이은하',	'1946',	'1419');");
		db.execSQL("insert into SongTable14 values(42,	'김다현',	'커피 한 잔',	'펄 시스터즈',	'141',	'736');");
		db.execSQL("insert into SongTable14 values(43,	'김태우',	'빨간 구두 아가씨',	'남일해',	'323',	'397');");
		db.execSQL("insert into SongTable14 values(44,	'김태우',	'그대 없이는 못 살아',	'패티김',	'1091',	'1118');");
		db.execSQL("insert into SongTable14 values(45,	'김태우',	'아름다운 강산',	'이선희',	'870',	'923');");
		db.execSQL("insert into SongTable14 values(46,	'김태우',	'고해',	'임재범',	'9033',	'7033');");
		db.execSQL("insert into SongTable14 values(47,	'김태우',	'솔로예찬',	'이문세'	,'4516',	'5435');");
		db.execSQL("insert into SongTable14 values(48,	'나래(아이 투 아이)',	'난 아직 모르잖아요',	'이문세',	'631',	'828');");
		db.execSQL("insert into SongTable14 values(49,	'나르샤(브라운아이드걸스)',	'친구여 (Feat.인순이)',	'조PD',	'12986',	'9729');");
		db.execSQL("insert into SongTable14 values(50,	'나르샤(브라운아이드걸스)',	'대전 블루스',	'안정애',	'401',	'281');");
		db.execSQL("insert into SongTable14 values(51,	'나르샤(브라운아이드걸스)',	'착한 사랑',	'김민종',	'4573',	'5467');");
		db.execSQL("insert into SongTable14 values(52,	'나르샤(브라운아이드걸스)',	'동숙의 노래',	'문주란',	'290',	'295');");
		db.execSQL("insert into SongTable14 values(53,	'나르샤(브라운아이드걸스)',	'홀로 된다는 것',	'변진섭'	,'168',	'781');");
		db.execSQL("insert into SongTable14 values(54,	'나르샤(브라운아이드걸스)',	'사랑하지 않을래',	'장덕',	'18506',	'5397');");
		db.execSQL("insert into SongTable14 values(55,	'나르샤(브라운아이드걸스)',	'싫어',	'펄 시스터즈'	,'1187',	'정보없음');");
		db.execSQL("insert into SongTable14 values(56,	'나르샤(브라운아이드걸스)',	'찰랑찰랑',	'이자연',	'2689',	'3916');");
		db.execSQL("insert into SongTable14 values(57,	'나비',	'옥경이',	'태진아',	'63',	'599');");
		db.execSQL("insert into SongTable14 values(58,	'나비',	'불 꺼진 창',	'조영남',	'215',	'421');");
		db.execSQL("insert into SongTable14 values(59,	'나비',	'나비소녀',	'김세화',	'1635',	'2510');");
		db.execSQL("insert into SongTable14 values(60,	'나윤권',	'혼자만의 겨울',	'강수지',	'2869',	'3979');");
		db.execSQL("insert into SongTable14 values(61,	'남우현(인피니트)',	'남자는 여자를 귀찮게 해',	'문주란',	'118',	'1354');");
		db.execSQL("insert into SongTable14 values(62,	'남우현(인피니트)',	'편지할게요',	'박정현',	'8124',	'5837');");
		db.execSQL("insert into SongTable14 values(63,	'남우현(인피니트)',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable14 values(64,	'남우현(인피니트)',	'내 사랑 울보',	'전영록',	'1264',	'1332');");
		db.execSQL("insert into SongTable14 values(65,	'남우현(인피니트)',	'어쩌다 마주친 그대',	'송골매',	'1209',	'1220');");
		db.execSQL("insert into SongTable14 values(66,	'남우현(인피니트)',	'열정',	'혜은이',	'1542',	'2475');");
		db.execSQL("insert into SongTable14 values(67,	'남우현(인피니트)',	'추억 만들기',	'김현식',	'1182',	'1316');");
		db.execSQL("insert into SongTable14 values(68,	'노라조',	'돌아가는 삼각지',	'배호',	'80',	'288');");
		db.execSQL("insert into SongTable14 values(69,	'노브레인',	'섬마을 선생님',	'이미자',	'657',	'497');");
		db.execSQL("insert into SongTable14 values(70,	'노브레인',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable14 values(71,	'노브레인',	'럭키서울',	'현인(TJ)/최희준(KY)',	'622',	'314');");
		db.execSQL("insert into SongTable14 values(72,	'노브레인',	'사랑만은 않겠어요',	'윤수일',	'35',	'441');");
		db.execSQL("insert into SongTable14 values(73,	'노브레인',	'선녀와 나무꾼',	'김창남',	'2105',	'3734');");
		db.execSQL("insert into SongTable14 values(74,	'노브레인',	'연',	'라이너스',	'1341',	'1709');");
		db.execSQL("insert into SongTable14 values(75,	'노브레인',	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable14 values(76,	'노브레인',	'친구야 친구',	'윤항기',	'1031',	'2290');");
		db.execSQL("insert into SongTable14 values(77,	'노브레인',	'월남에서 돌아온 김상사',	'김추자',	'1137',	'2495');");
		db.execSQL("insert into SongTable14 values(78,	'노브레인',	'소녀와 가로등',	'진미령',	'112',	'502');");
		db.execSQL("insert into SongTable14 values(79,	'노을',	'세노야 세노야',	'양희은',	'1094',	'1205');");
		db.execSQL("insert into SongTable14 values(80,	'노을',	'그대에게서 벗어나고파',	'윤시내',	'1192',	'2255');");
		db.execSQL("insert into SongTable14 values(81,	'노을',	'사랑하는 마음',	'김세환',	'1173',	'1680');");
		db.execSQL("insert into SongTable14 values(82,	'노을',	'해야',	'마그마',	'2145',	'4626');");
		db.execSQL("insert into SongTable14 values(83,	'다나',	'날 울리지마',	'신승훈',	'715',	'1039');");
		db.execSQL("insert into SongTable14 values(84,	'다비치',	'사랑의 미로',	'최진희',	'83',	'456');");
		db.execSQL("insert into SongTable14 values(85,	'더 문샤이너스',	'기쁜 우리 사랑은',	'최성수',	'209',	'190');");
		db.execSQL("insert into SongTable14 values(86,	'더 씨야',	'구름, 들꽃, 돌, 여인',	'해바라기',	'1936',	'2322');");
		db.execSQL("insert into SongTable14 values(87,	'더 포지션',	'너',	'해바라기',	'1344',	'1459');");
		db.execSQL("insert into SongTable14 values(88,	'데이브레이크',	'실버들',	'희자매',	'559',	'513');");
		db.execSQL("insert into SongTable14 values(89,	'데이브레이크',	'그대와 함께 (느낌 O.S.T)',	'더 블루',	'31180',	'86184');");
		db.execSQL("insert into SongTable14 values(90,	'데이브레이크',	'이 밤이 지나면',	'임재범',	'1254',	'1238');");
		db.execSQL("insert into SongTable14 values(91,	'데이브레이크',	'보슬비 오는 거리',	'문주란(원곡) / 성재희(TJ, KY)',	'738',	'412');");
		db.execSQL("insert into SongTable14 values(92,	'데이브레이크',	'숙녀에게',	'변진섭',	'753',	'1697');");
		db.execSQL("insert into SongTable14 values(93,	'레드애플',	'안녕',	'배호',	'709',	'544');");
		db.execSQL("insert into SongTable14 values(94,	'레이나(애프터스쿨)',	'알고 싶어요',	'이선희',	'1273',	'929');");
		db.execSQL("insert into SongTable14 values(95,	'려욱(슈퍼주니어)',	'하얀 목련',	'양희은',	'1327',	'1273');");
		db.execSQL("insert into SongTable14 values(96,	'려욱(슈퍼주니어)',	'오늘도 난',	'이승철',	'3495',	'4813');");
		db.execSQL("insert into SongTable14 values(97,	'려욱(슈퍼주니어)',	'봉선화 연정',	'현철',	'185',	'890');");
		db.execSQL("insert into SongTable14 values(98,	'려욱(슈퍼주니어)',	'바다에 누워',	'높은음자리',	'723',	'379');");
		db.execSQL("insert into SongTable14 values(99,	'려욱(슈퍼주니어)',	'그녀를 만나는 곳 100m 전',	'이상우',	'33',	'167');");
		db.execSQL("insert into SongTable14 values(100,	'려욱(슈퍼주니어)',	'휘파람을 부세요',	'정미조',	'861',	'791');");
		db.execSQL("insert into SongTable14 values(101,	'려욱(슈퍼주니어)',	'나는 너를',	'장현',	'74',	'199');");
		db.execSQL("insert into SongTable14 values(102,	'루나(f(x))',	'천일동안',	'이승환',	'2679',	'3853');");
		db.execSQL("insert into SongTable14 values(103,	'루나(f(x))',	'동행',	'최성수',	'203',	'862');");
		db.execSQL("insert into SongTable14 values(104,	'루나(f(x))',	'바보',	'윤형주',	'1492',	'1668');");
		db.execSQL("insert into SongTable14 values(105,	'루나(f(x))',	'해 뜰 날',	'송대관',	'379',	'766');");
		db.execSQL("insert into SongTable14 values(106,	'린',	'엄마야',	'신승훈',	'8902',	'6335');");
		db.execSQL("insert into SongTable14 values(107,	'린',	'눈이 내리네',	'이숙(TJ)/김추자(KY)',	'562',	'4650');");
		db.execSQL("insert into SongTable14 values(108,	'린',	'담배가게 아가씨',	'송창식',	'2682',	'2234');");
		db.execSQL("insert into SongTable14 values(109,	'린',	'혼자만의 사랑',	'김건모',	'1740',	'3266');");
		db.execSQL("insert into SongTable14 values(110,	'린',	'사랑은 영원히',	'패티김',	'680',	'451');");
		db.execSQL("insert into SongTable14 values(111,	'린',	'겨울 장미',	'이은하',	'346',	'123');");
		db.execSQL("insert into SongTable14 values(112,	'린',	'서울 야곡',	'현인',	'169',	'488');");
		db.execSQL("insert into SongTable14 values(113,	'린',	'추억',	'윤수일',	'3328',	'9817');");
		db.execSQL("insert into SongTable14 values(114,	'린',	'디디디',	'김혜림',	'325',	'311');");
		db.execSQL("insert into SongTable14 values(115,	'린',	'탈춤',	'활주로',	'1307',	'2561');");
		db.execSQL("insert into SongTable14 values(116,	'린',	'Bad Girl Good Girl',	'miss A',	'32773',	'47073');");
		db.execSQL("insert into SongTable14 values(117,	'린',	'간다고 하지 마오 (님은 먼 곳에 O.S.T)',	'김정미(원곡) / 수애(KY)',	'정보없음',	'46383');");
		db.execSQL("insert into SongTable14 values(118,	'문명진',	'슬픔만은 아니겠죠',	'해바라기',	'정보없음',	'4467');");
		db.execSQL("insert into SongTable14 values(119,	'문명진',	'가로수 그늘 아래 서면',	'이문세',	'3',	'1451');");
		db.execSQL("insert into SongTable14 values(120,	'민경훈',	'바람이 전하는 말',	'조용필',	'764',	'390');");
		db.execSQL("insert into SongTable14 values(121,	'바다',	'사랑밖엔 난 몰라',	'심수봉',	'1486',	'1468');");
		db.execSQL("insert into SongTable14 values(122,	'바다',	'옛 사랑',	'이문세',	'916',	'1364');");
		db.execSQL("insert into SongTable14 values(123,	'박재범',	'애모',	'김수희',	'1427',	'1983');");
		db.execSQL("insert into SongTable14 values(124,	'박재범',	'기분 좋은 날',	'김완선',	'671',	'188');");
		db.execSQL("insert into SongTable14 values(125,	'박재범',	'나를 돌아봐',	'듀스',	'1547',	'2160');");
		db.execSQL("insert into SongTable14 values(126,	'박재범',	'그대여 변치 마오',	'남진',	'576',	'175');");
		db.execSQL("insert into SongTable14 values(127,	'박재범',	'널 사랑하겠어',	'동물원',	'2853',	'3985');");
		db.execSQL("insert into SongTable14 values(128,	'박재범',	'빈 잔',	'남진',	'82',	'433');");
		db.execSQL("insert into SongTable14 values(129,	'박재범',	'잠 못 드는 밤 비는 내리고',	'김건모',	'1117',	'1509');");
		db.execSQL("insert into SongTable14 values(130,	'박재범',	'사랑이란 두 글자',	'패티김',	'2315',	'1191');");
		db.execSQL("insert into SongTable14 values(131,	'박재범',	'아리송해',	'이은하',	'1959',	'2049');");
		db.execSQL("insert into SongTable14 values(132,	'박재범',	'신라의 달밤',	'현인',	'136',	'512');");
		db.execSQL("insert into SongTable14 values(133,	'박재범',	'빗속의 여인',	'김건모',	'9590',	'7127');");
		db.execSQL("insert into SongTable14 values(134,	'박재범',	'남자는 배 여자는 항구',	'심수봉',	'4021',	'223');");
		db.execSQL("insert into SongTable14 values(135,	'박재범',	'붉은 노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable14 values(136,	'박재범, 김수로, 솔비, 장혁',	'말해줘',	'지누션',	'3994',	'5060');");
		db.execSQL("insert into SongTable14 values(137,	'박재범, 문희준',	'캔디',	'H.O.T',	'3547',	'4843');");
		db.execSQL("insert into SongTable14 values(138,	'박현빈',	'당신',	'배호',	'3141',	'269');");
		db.execSQL("insert into SongTable14 values(139,	'박현빈',	'네박자',	'송대관',	'4650',	'5327');");
		db.execSQL("insert into SongTable14 values(140,	'박현빈',	'Festival',	'엄정화',	'8342',	'5978');");
		db.execSQL("insert into SongTable14 values(141,	'박현빈',	'경아',	'박혜성',	'713',	'1522');");
		db.execSQL("insert into SongTable14 values(142,	'박현빈, 허경환',	'걸어서 하늘까지 (걸어서 하늘까지 O.S.T)'	,'장현철',	'1120',	'1575');");
		db.execSQL("insert into SongTable14 values(143,	'별',	'립스틱 짙게 바르고',	'임주리',	'644',	'317');");
		db.execSQL("insert into SongTable14 values(144,	'부가킹즈',	'화가 났을까',	'김세환',	'927',	'1728');");
		db.execSQL("insert into SongTable14 values(145,	'브라이언',	'오늘 같은 밤',	'이광조',	'1815',	'3494');");
		db.execSQL("insert into SongTable14 values(146,	'브라이언',	'영일만 친구',	'최백호',	'1269',	'940');");
		db.execSQL("insert into SongTable14 values(147,	'브라이언',	'난 너에게',	'정수라',	'467',	'215');");
		db.execSQL("insert into SongTable14 values(148,	'브라이언',	'그 사람 바보야',	'정훈희',	'521',	'179');");
		db.execSQL("insert into SongTable14 values(149,	'브라이언',	'미안 미안해',	'태진아',	'254',	'372');");
		db.execSQL("insert into SongTable14 values(150,	'브라이언, 김완선',	'나만의 것',	'김완선',	'940',	'208');");
		db.execSQL("insert into SongTable14 values(151,	'사이먼 디',	'풀잎사랑',	'최성수',	'1215',	'1353');");
		db.execSQL("insert into SongTable14 values(152,	'사이먼 디',	'사랑의 이름표',	'현철',	'8266',	'5997');");
		db.execSQL("insert into SongTable14 values(153,	'산체스(팬텀)',	'이 밤을 다시 한 번',	'조하문',	'481',	'631');");
		db.execSQL("insert into SongTable14 values(154,	'서인영',	'처음 그 느낌처럼',	'신승훈',	'1465',	'2025');");
		db.execSQL("insert into SongTable14 values(155,	'서인영',	'밤차',	'이은하',	'2695',	'1180');");
		db.execSQL("insert into SongTable14 values(156,	'성규(인피니트)',	'이루어질 수 없는 사랑',	'양희은',	'400',	'948');");
		db.execSQL("insert into SongTable14 values(157,	'성규(인피니트)',	'파도',	'UN',	'9588',	'6959');");
		db.execSQL("insert into SongTable14 values(158,	'성규(인피니트)',	'내 마음 별과 같이',	'현철',	'432',	'839');");
		db.execSQL("insert into SongTable14 values(159,	'성규(인피니트)',	'해변의 여인',	'쿨',	'3956',	'5036');");
		db.execSQL("insert into SongTable14 values(160,	'성규(인피니트)',	'담다디',	'이상은',	'1163',	'857');");
		db.execSQL("insert into SongTable14 values(161,	'성규(인피니트)',	'나 그대에게 모두 드리리',	'이장희',	'230',	'197');");
		db.execSQL("insert into SongTable14 values(162,	'성규(인피니트), 남우현(인피니트)',	'해 뜰 날',	'송대관',	'379',	'766');");
		db.execSQL("insert into SongTable14 values(163,	'성훈(브라운아이드소울)',	'맨 처음 고백',	'송창식',	'532',	'1410');");
		db.execSQL("insert into SongTable14 values(164,	'성훈(브라운아이드소울)',	'딜라일라',	'조영남',	'4812',	'2337');");
		db.execSQL("insert into SongTable14 values(165,	'성훈(브라운아이드소울)',	'흔들린 우정',	'홍경민',	'8929',	'6411');");
		db.execSQL("insert into SongTable14 values(166,	'성훈(브라운아이드소울)',	'거리에서',	'동물원',	'1332',	'1326');");
		db.execSQL("insert into SongTable14 values(167,	'성훈(브라운아이드소울)',	'물레방아 도는데',	'나훈아',	'505',	'362');");
		db.execSQL("insert into SongTable14 values(168,	'성훈(브라운아이드소울)',	'뻐꾸기 둥지 위로 날아간 새',	'김건모',	'4273',	'5221');");
		db.execSQL("insert into SongTable14 values(169,	'성훈(브라운아이드소울)',	'사랑이여 다시 한 번',	'패티김',	'2576',	'4427');");
		db.execSQL("insert into SongTable14 values(170,	'성훈(브라운아이드소울)',	'미소를 띄우며 나를 보낸 그 모습처럼',	'이은하',	'1946',	'1419');");
		db.execSQL("insert into SongTable14 values(171,	'성훈(브라운아이드소울)',	'꿈 속의 사랑',	'현인',	'606',	'158');");
		db.execSQL("insert into SongTable14 values(172,	'성훈(브라운아이드소울)',	'아름다워',	'윤수일',	'3286',	'4475');");
		db.execSQL("insert into SongTable14 values(173,	'성훈(브라운아이드소울)',	'불놀이야',	'옥슨80',	'1298',	'2393');");
		db.execSQL("insert into SongTable14 values(174,	'성훈(브라운아이드소울)',	'그녀는 예뻤다',	'박진영',	'3848',	'4979');");
		db.execSQL("insert into SongTable14 values(175,	'성훈(브라운아이드소울)',	'그런 거지 뭐',	'윤항기',	'282',	'177');");
		db.execSQL("insert into SongTable14 values(176,	'성훈(브라운아이드소울)',	'님아',	'펄 시스터즈(원곡) / 신효범(TJ) / 펄 시스터즈(KY)',	'2661',	'4315');");
		db.execSQL("insert into SongTable14 values(177,	'소냐',	'상아의 노래',	'송창식',	'1009',	'911');");
		db.execSQL("insert into SongTable14 values(178,	'소냐',	'스피드',	'김건모',	'3112',	'4145');");
		db.execSQL("insert into SongTable14 values(179,	'소냐',	'사랑의 맹세',	'패티김',	'819',	'455');");
		db.execSQL("insert into SongTable14 values(180,	'소냐',	'파초',	'수와 진',	'702',	'1425');");
		db.execSQL("insert into SongTable14 values(181,	'소냐',	'초대',	'엄정화',	'4777',	'5613');");
		db.execSQL("insert into SongTable14 values(182,	'소냐',	'어젯밤 이야기',	'소방차',	'2810',	'567');");
		db.execSQL("insert into SongTable14 values(183,	'소냐',	'문 밖에 있는 그대',	'박강성',	'3533',	'4372');");
		db.execSQL("insert into SongTable14 values(184,	'소냐',	'한계령',	'양희은',	'1932',	'2390');");
		db.execSQL("insert into SongTable14 values(185,	'소냐',	'정',	'영턱스클럽',	'3340',	'4755');");
		db.execSQL("insert into SongTable14 values(186,	'소냐',	'앉으나 서나 당신 생각',	'현철',	'103',	'928');");
		db.execSQL("insert into SongTable14 values(187,	'소냐',	'한 여름밤의 꿈',	'권성연',	'1052',	'3501');");
		db.execSQL("insert into SongTable14 values(188,	'소냐',	'슬픈 그림같은 사랑',	'이상우',	'1301',	'1309');");
		db.execSQL("insert into SongTable14 values(189,	'소냐',	'사랑으로',	'해바라기',	'234',	'443');");
		db.execSQL("insert into SongTable14 values(190,	'손호영',	'하얀 그리움',	'김민종',	'9714',	'7667');");
		db.execSQL("insert into SongTable14 values(191,	'손호영',	'밤이면 밤마다',	'인순이',	'1996',	'1542');");
		db.execSQL("insert into SongTable14 values(192,	'손호영',	'사랑과 행복 그리고 이별',	'이용',	'3388',	'4416');");
		db.execSQL("insert into SongTable14 values(193,	'손호영',	'안개 낀 장충단공원',	'배호',	'326',	'542');");
		db.execSQL("insert into SongTable14 values(194,	'손호영',	'현아',	'김범룡',	'8000',	'4680');");
		db.execSQL("insert into SongTable14 values(195,	'손호영',	'사모곡',	'태진아',	'1859',	'3353');");
		db.execSQL("insert into SongTable14 values(196,	'손호영',	'숨은 그림찾기',	'엄정화',	'4858',	'5705');");
		db.execSQL("insert into SongTable14 values(197,	'손호영',	'새들처럼',	'변진섭',	'151',	'912');");
		db.execSQL("insert into SongTable14 values(198,	'손호영, 김태우',	'겨울 이야기',	'DJ DOC',	'2890',	'4010');");
		db.execSQL("insert into SongTable14 values(199,	'송지은(시크릿)',	'회상3',	'부활',	'1899',	'69962');");
		db.execSQL("insert into SongTable14 values(200,	'송지은(시크릿)',	'친구라는 건 (with 김범수)',	'박효신',	'13250',	'9818');");
		db.execSQL("insert into SongTable14 values(201,	'송지은(시크릿)',	'꿈',	'이현우',	'1319',	'1305');");
		db.execSQL("insert into SongTable14 values(202,	'송지은(시크릿)',	'내 인생은 나의 것',	'민해경',	'543',	'233');");
		db.execSQL("insert into SongTable14 values(203,	'스윗소로우',	'젊은 태양',	'심수봉',	'183',	'972');");
		db.execSQL("insert into SongTable14 values(204,	'스윗소로우',	'좋은 날',	'이승환',	'1938',	'2531');");
		db.execSQL("insert into SongTable14 values(205,	'스윗소로우',	'공부합시다',	'윤시내',	'12200',	'4222');");
		db.execSQL("insert into SongTable14 values(206,	'스윗소로우',	'하얀 손수건',	'트윈폴리오',	'131',	'759');");
		db.execSQL("insert into SongTable14 values(207,	'스윗소로우',	'사랑하는 우리',	'조하문',	'1819',	'1681');");
		db.execSQL("insert into SongTable14 values(208,	'스윗소로우',	'유행가',	'송대관',	'11526',	'63383');");
		db.execSQL("insert into SongTable14 values(209,	'스윗소로우',	'거짓말이야',	'김추자',	'1219',	'2228');");
		db.execSQL("insert into SongTable14 values(210,	'스윗소로우',	'호반에서 만난 사람',	'하춘화(원곡) / 하춘화(TJ) / 최양숙(KY)',	'3728',	'1670');");
		db.execSQL("insert into SongTable14 values(211,	'스윗소로우',	'서울',	'장재남(TJ) / 이용(KY)',	'1234',	'1688');");
		db.execSQL("insert into SongTable14 values(212,	'스윗소로우',	'두메산골',	'배호',	'165',	'299');");
		db.execSQL("insert into SongTable14 values(213,	'스윗소로우',	'노란 손수건',	'태진아',	'1426',	'1437');");
		db.execSQL("insert into SongTable14 values(214,	'스윗소로우',	'다 가라',	'엄정화',	'9690',	'7617');");
		db.execSQL("insert into SongTable14 values(215,	'스윗소로우, 정재형',	'썸머 징글벨',	'박진영',	'3875',	'4999');");
		db.execSQL("insert into SongTable14 values(216,	'스피카',	'깊은 밤을 날아서',	'이문세',	'1771',	'2259');");
		db.execSQL("insert into SongTable14 values(217,	'신용재',	'잊혀진 계절',	'이용',	'512',	'659');");
		db.execSQL("insert into SongTable14 values(218,	'신용재',	'그 겨울의 찻집',	'조용필',	'1481',	'1454');");
		db.execSQL("insert into SongTable14 values(219,	'신용재',	'사랑이라는 이유로',	'김광석',	'1835',	'2732');");
		db.execSQL("insert into SongTable14 values(220,	'신용재',	'그날들',	'김광석',	'4248',	'7054');");
		db.execSQL("insert into SongTable14 values(221,	'신용재',	'불티',	'전영록',	'920',	'1671');");
		db.execSQL("insert into SongTable14 values(222,	'신용재',	'아가에게',	'송골매',	'34963',	'58495');");
		db.execSQL("insert into SongTable14 values(223,	'신용재',	'감수광',	'혜은이',	'449',	'112');");
		db.execSQL("insert into SongTable14 values(224,	'신용재',	'비처럼 음악처럼',	'김현식',	'314',	'430');");
		db.execSQL("insert into SongTable14 values(225,	'신용재',	'그쟈',	'최백호',	'1800',	'2165');");
		db.execSQL("insert into SongTable14 values(226,	'신용재',	'고래사냥',	'송창식',	'789',	'808');");
		db.execSQL("insert into SongTable14 values(227,	'신용재',	'꽃밭에서',	'정훈희',	'2960',	'3010');");
		db.execSQL("insert into SongTable14 values(228,	'신용재',	'거울도 안 보는 여자',	'태진아',	'551',	'120');");
		db.execSQL("insert into SongTable14 values(229,	'신용재',	'왜 불러',	'송창식',	'4032',	'942');");
		db.execSQL("insert into SongTable14 values(230,	'신용재',	'사랑 없인 못 살아요',	'조영남',	'12199',	'65810');");
		db.execSQL("insert into SongTable14 values(231,	'신용재',	'겨울이 오면',	'김건모',	'3349',	'3730');");
		db.execSQL("insert into SongTable14 values(232,	'신용재',	'기다려줘',	'김광석',	'1038',	'4244');");
		db.execSQL("insert into SongTable14 values(233,	'신용재',	'이 노래',	'2AM',	'19848',	'83678');");
		db.execSQL("insert into SongTable14 values(234,	'신용재',	'해후',	'최성수',	'237',	'771');");
		db.execSQL("insert into SongTable14 values(235,	'신용재',	'DJ에게',	'윤시내',	'864',	'1663');");
		db.execSQL("insert into SongTable14 values(236,	'신용재',	'웨딩케익',	'트윈폴리오',	'1253',	'3964');");
		db.execSQL("insert into SongTable14 values(237,	'신용재',	'내 아픔 아시는 당신께',	'조하문',	'121',	'840');");
		db.execSQL("insert into SongTable14 values(238,	'신용재',	'우리 순이',	'송대관',	'719',	'605');");
		db.execSQL("insert into SongTable14 values(239,	'신용재',	'봄비',	'박인수',	'1890',	'415');");
		db.execSQL("insert into SongTable14 values(240,	'신용재',	'너를 위해 (동감 O.S.T)',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable14 values(241,	'신용재, 조장혁',	'중독된 사랑',	'조장혁',	'8937',	'6431');");
		db.execSQL("insert into SongTable14 values(242,	'신혜성',	'준비 없는 이별',	'녹색지대',	'2827',	'3977');");
		db.execSQL("insert into SongTable14 values(243,	'아이비',	'초대',	'엄정화',	'4777',	'5613');");
		db.execSQL("insert into SongTable14 values(244,	'아이비',	'사랑은 유리 같은 것',	'원준희',	'1668',	'1998');");
		db.execSQL("insert into SongTable14 values(245,	'아이비',	'하얀 나비',	'김정호',	'140',	'755');");
		db.execSQL("insert into SongTable14 values(246,	'아이비',	'또',	'인순이'	,'3299',	'4720');");
		db.execSQL("insert into SongTable14 values(247,	'아이비',	'소양강 처녀',	'김태희',	'158',	'504');");
		db.execSQL("insert into SongTable14 values(248,	'아이비',	'아름다운 아픔',	'김민종'	,'8836',	'6332');");
		db.execSQL("insert into SongTable14 values(249,	'아이비',	'돌지 않는 풍차'	,'문주란',	'3480',	'292');");
		db.execSQL("insert into SongTable14 values(250,	'아이유',	'남자는 배 여자는 항구',	'심수봉',	'4021',	'223');");
		db.execSQL("insert into SongTable14 values(251,	'아이유',	'좋은 사람',	'토이',	'9526',	'6863');");
		db.execSQL("insert into SongTable14 values(252,	'아이 투 아이',	'길가에 앉아서',	'김세환',	'595',	'194');");
		db.execSQL("insert into SongTable14 values(253,	'알렉스',	'너만을 느끼며',	'더 블루',	'31160',	'1426');");
		db.execSQL("insert into SongTable14 values(254,	'알렉스',	'행복을 주는 사람',	'해바라기',	'2554',	'3554');");
		db.execSQL("insert into SongTable14 values(255,	'알리',	'고추잠자리',	'조용필',	'929',	'1645');");
		db.execSQL("insert into SongTable14 values(256,	'알리',	'킬리만자로의 표범',	'조용필',	'914',	'3366');");
		db.execSQL("insert into SongTable14 values(257,	'알리',	'나나나',	'유승준',	'4555',	'5460');");
		db.execSQL("insert into SongTable14 values(258,	'알리',	'바람이 불어오는 곳',	'김광석',	'10418',	'63868');");
		db.execSQL("insert into SongTable14 values(259,	'알리',	'얄미운 사람',	'김지애',	'11410',	'560');");
		db.execSQL("insert into SongTable14 values(260,	'알리',	'세상 모르고 살았노라',	'활주로(TJ)/송골매(KY)',	'1590',	'1691');");
		db.execSQL("insert into SongTable14 values(261,	'알리',	'새벽비',	'혜은이',	'890',	'1686');");
		db.execSQL("insert into SongTable14 values(262,	'알리',	'골목길',	'김현식',	'1196',	'141');");
		db.execSQL("insert into SongTable14 values(263,	'알리',	'가까이 하기엔 너무 먼 당신',	'이광조',	'1252',	'1103');");
		db.execSQL("insert into SongTable14 values(264,	'알리',	'내 마음 갈 곳을 잃어',	'최백호',	'582',	'229');");
		db.execSQL("insert into SongTable14 values(265,	'알리',	'젊은 그대',	'김수철',	'1238',	'1715');");
		db.execSQL("insert into SongTable14 values(266,	'알리',	'안녕',	'김태화',	'2384',	'545');");
		db.execSQL("insert into SongTable14 values(267,	'알리',	'사랑은 아무나 하나',	'태진아',	'8834',	'6314');");
		db.execSQL("insert into SongTable14 values(268,	'알리',	'피리 부는 사나이',	'송창식',	'639',	'985');");
		db.execSQL("insert into SongTable14 values(269,	'알리',	'화개장터',	'조영남',	'5055',	'996');");
		db.execSQL("insert into SongTable14 values(270,	'알리',	'오랜 방황의 끝',	'김태영',	'8748',	'6281');");
		db.execSQL("insert into SongTable14 values(271,	'알리',	'변해가네',	'동물원',	'2046',	'2227');");
		db.execSQL("insert into SongTable14 values(272,	'알리',	'가시나무새',	'오청수(TJ)/패티김(KY)',	'965',	'1321');");
		db.execSQL("insert into SongTable14 values(273,	'알리',	'넌 친구 난 연인',	'김건모',	'4152',	'3729');");
		db.execSQL("insert into SongTable14 values(274,	'알리',	'초우',	'패티김',	'752',	'719');");
		db.execSQL("insert into SongTable14 values(275,	'알리',	'돌이키지마',	'이은하',	'148',	'291');");
		db.execSQL("insert into SongTable14 values(276,	'알리',	'비 내리는 고모령',	'현인',	'755',	'424');");
		db.execSQL("insert into SongTable14 values(277,	'알리',	'떠나지마',	'윤수일',	'4762',	'63857');");
		db.execSQL("insert into SongTable14 values(278,	'알리',	'사랑은 차가운 유혹',	'양수경',	'181',	'1044');");
		db.execSQL("insert into SongTable14 values(279,	'알리',	'연극이 끝난 후',	'샤프',	'3053',	'2243');");
		db.execSQL("insert into SongTable14 values(280,	'알리',	'난 여자가 있는데',	'박진영',	'9547',	'6881');");
		db.execSQL("insert into SongTable14 values(281,	'알리',	'여러분',	'윤복희',	'534',	'1310');");
		db.execSQL("insert into SongTable14 values(282,	'알리',	'한 잔의 추억',	'이장희',	'609',	'765');");
		db.execSQL("insert into SongTable14 values(283,	'알리',	'미인',	'신중현과 엽전들',	'1555',	'2372');");
		db.execSQL("insert into SongTable14 values(284,	'알리',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable14 values(285,	'알리, 이예린',	'늘 지금처럼',	'이예린',	'3012',	'4084');");
		db.execSQL("insert into SongTable14 values(286,	'애즈원, 버벌진트',	'사랑하는 영자씨',	'현숙',	'2778',	'4048');");
		db.execSQL("insert into SongTable14 values(287,	'양요섭(비스트)',	'미워요',	'심수봉',	'1316',	'1176');");
		db.execSQL("insert into SongTable14 values(288,	'양요섭(비스트)',	'희야',	'부활',	'1824',	'2015');");
		db.execSQL("insert into SongTable14 values(289,	'양요섭(비스트)',	'그대와 영원히',	'이문세',	'2606',	'3656');");
		db.execSQL("insert into SongTable14 values(290,	'양요섭(비스트)',	'엄마',	'라디',	'36202',	'87031');");
		db.execSQL("insert into SongTable14 values(291,	'에이티',	'바다새',	'바다새',	'1935',	'2374');");
		db.execSQL("insert into SongTable14 values(292,	'에일리',	'빛과 그림자',	'패티김(TJ)/최희준(KY)',	'34',	'437');");
		db.execSQL("insert into SongTable14 values(293,	'에일리',	'봄비',	'이은하',	'460',	'416');");
		db.execSQL("insert into SongTable14 values(294,	'에일리',	'베사메무쵸',	'현인',	'113',	'404');");
		db.execSQL("insert into SongTable14 values(295,	'에일리',	'제 2의 고향',	'윤수일',	'1043',	'975');");
		db.execSQL("insert into SongTable14 values(296,	'에일리',	'고독한 연인',	'김수희',	'310',	'807');");
		db.execSQL("insert into SongTable14 values(297,	'에일리',	'나 어떡해',	'샌드 페블즈',	'414',	'1136');");
		db.execSQL("insert into SongTable14 values(298,	'에일리',	'날 떠나지마',	'박진영',	'2357',	'3569');");
		db.execSQL("insert into SongTable14 values(299,	'에일리',	'나는 어떡하라구',	'윤항기',	'600',	'201');");
		db.execSQL("insert into SongTable14 values(300,	'에일리',	'통화중',	'소방차',	'333',	'2563');");
		db.execSQL("insert into SongTable14 values(301,	'에일리',	'토요일은 밤이 좋아',	'김종찬',	'451',	'745');");
		db.execSQL("insert into SongTable14 values(302,	'에일리',	'아침이슬',	'양희은',	'11372',	'538');");
		db.execSQL("insert into SongTable14 values(303,	'에일리',	'인연 (불새 O.S.T)',	'이승철',	'13165',	'9775');");
		db.execSQL("insert into SongTable14 values(304,	'에일리',	'싫다 싫어',	'현철',	'422',	'516');");
		db.execSQL("insert into SongTable14 values(305,	'에일리',	'3!4!',	'룰라',	'3162',	'4184');");
		db.execSQL("insert into SongTable14 values(306,	'에일리',	'하룻밤의 꿈',	'이상우',	'1321',	'1291');");
		db.execSQL("insert into SongTable14 values(307,	'에일리',	'장미',	'사랑과 평화',	'2048',	'4566');");
		db.execSQL("insert into SongTable14 values(308,	'에일리',	'너를 향한 마음',	'이승환',	'273',	'1150');");
		db.execSQL("insert into SongTable14 values(309,	'에일리',	'리듬 속의 그 춤을',	'김완선',	'3634',	'4350');");
		db.execSQL("insert into SongTable14 values(310,	'에일리, 배치기',	'님아',	'펄 시스터즈(원곡) / 신효범(TJ) / 펄 시스터즈(KY)',	'2661',	'4315');");
		db.execSQL("insert into SongTable14 values(311,	'엠블랙',	'남자는 여자를 귀찮게 해',	'문주란',	'118',	'1354');");
		db.execSQL("insert into SongTable14 values(312,	'엠블랙',	'너 나 좋아해 나 너 좋아해',	'현이와 덕이',	'1577',	'1455');");
		db.execSQL("insert into SongTable14 values(313,	'예성(슈퍼주니어)',	'사랑밖엔 난 몰라',	'심수봉',	'1486',	'1468');");
		db.execSQL("insert into SongTable14 values(314,	'예성(슈퍼주니어)',	'사랑할수록',	'부활',	'1917',	'3356');");
		db.execSQL("insert into SongTable14 values(315,	'예성(슈퍼주니어)',	'한 남자',	'김종국',	'13448',	'9829');");
		db.execSQL("insert into SongTable14 values(316,	'예성(슈퍼주니어)',	'서시',	'신성우',	'2135',	'3446');");
		db.execSQL("insert into SongTable14 values(317,	'왁스',	'백치 아다다',	'나애심(TJ) / 문주란(KY)',	'1440',	'1469');");
		db.execSQL("insert into SongTable14 values(318,	'왁스',	'그대 내게 다시',	'변진섭',	'1511',	'1579');");
		db.execSQL("insert into SongTable14 values(319,	'왁스',	'님 떠난 후',	'장덕',	'280',	'852');");
		db.execSQL("insert into SongTable14 values(320,	'왁스',	'누구라도 그러하듯이',	'배인숙',	'727',	'1152');");
		db.execSQL("insert into SongTable14 values(321,	'왁스',	'샤방샤방',	'박현빈',	'19343',	'46249');");
		db.execSQL("insert into SongTable14 values(322,	'왁스',	'모두가 사랑이예요',	'해바라기',	'523',	'342');");
		db.execSQL("insert into SongTable14 values(323,	'왁스',	'미워요',	'심수봉',	'1316',	'1176');");
		db.execSQL("insert into SongTable14 values(324,	'울랄라세션',	'성인식',	'박지윤',	'8999',	'6493');");
		db.execSQL("insert into SongTable14 values(325,	'울랄라세션',	'G.Cafe',	'소방차',	'2408',	'3606');");
		db.execSQL("insert into SongTable14 values(326,	'울랄라세션',	'작은 연못',	'양희은',	'22',	'2183');");
		db.execSQL("insert into SongTable14 values(327,	'울랄라세션',	'사랑해 누나',	'유승준',	'3998',	'5078');");
		db.execSQL("insert into SongTable14 values(328,	'울랄라세션',	'사랑의 이름표',	'현철',	'8266',	'5997');");
		db.execSQL("insert into SongTable14 values(329,	'울랄라세션',	'제주도의 푸른 밤',	'최성원',	'2310',	'2633');");
		db.execSQL("insert into SongTable14 values(330,	'울랄라세션',	'비창',	'이상우',	'1850',	'3332');");
		db.execSQL("insert into SongTable14 values(331,	'울랄라세션',	'그 애와 나랑은',	'이장희',	'2400',	'1125');");
		db.execSQL("insert into SongTable14 values(332,	'울랄라세션',	'덩크슛',	'이승환',	'1685',	'3160');");
		db.execSQL("insert into SongTable14 values(333,	'울랄라세션',	'남남',	'최성수',	'488',	'220');");
		db.execSQL("insert into SongTable14 values(334,	'울랄라세션(박광선)',	'화요일에 비가 내리면',	'박미경',	'2916',	'1319');");
		db.execSQL("insert into SongTable14 values(335,	'윈디시티',	'그건 너',	'이장희',	'4005',	'164');");
		db.execSQL("insert into SongTable14 values(336,	'윈디시티',	'혼자랍니다',	'송대관',	'1213',	'994');");
		db.execSQL("insert into SongTable14 values(337,	'유리상자',	'아내에게 바치는 노래',	'하수영',	'3140',	'520');");
		db.execSQL("insert into SongTable14 values(338,	'유리상자',	'사랑은 언제나 그 자리에',	'해바라기',	'3014',	'66838');");
		db.execSQL("insert into SongTable14 values(339,	'유미',	'배반의 장미',	'엄정화',	'3783',	'4920');");
		db.execSQL("insert into SongTable14 values(340,	'유미',	'하늘 아래서',	'김민종',	'1566',	'2735');");
		db.execSQL("insert into SongTable14 values(341,	'유미',	'사랑보다 깊은 상처',	'박정현, 임재범',	'4980',	'5810');");
		db.execSQL("insert into SongTable14 values(342,	'유키스',	'땡벌',	'강진',	'10136',	'7463');");
		db.execSQL("insert into SongTable14 values(343,	'윤하',	'네 꿈을 펼쳐라',	'양희은',	'597',	'2303');");
		db.execSQL("insert into SongTable14 values(344,	'윤하',	'Summer Time',	'포지션',	'3911',	'5021');");
		db.execSQL("insert into SongTable14 values(345,	'윤형렬',	'하늘만 허락한 사랑',	'엄정화',	'3138',	'4728');");
		db.execSQL("insert into SongTable14 values(346,	'윤형렬',	'이별 아닌 이별',	'이범학',	'271',	'949');");
		db.execSQL("insert into SongTable14 values(347,	'윤형렬',	'사랑',	'나훈아',	'646',	'897');");
		db.execSQL("insert into SongTable14 values(348,	'이기찬',	'님은 먼 곳에',	'김추자',	'596',	'261');");
		db.execSQL("insert into SongTable14 values(349,	'이기찬',	'물새 한 마리',	'하춘화',	'1222',	'875');");
		db.execSQL("insert into SongTable14 values(350,	'이기찬',	'오늘은 고백한다',	'배호',	'4467',	'6371');");
		db.execSQL("insert into SongTable14 values(351,	'이석훈',	'봄날은 간다',	'백설희',	'732',	'413');");
		db.execSQL("insert into SongTable14 values(352,	'이석훈',	'이 밤을 다시 한 번',	'조하문',	'481',	'631');");
		db.execSQL("insert into SongTable14 values(353,	'이석훈',	'사랑을 잃어버린 나',	'이광조',	'1753',	'1429');");
		db.execSQL("insert into SongTable14 values(354,	'이석훈',	'낭만에 대하여',	'최백호',	'2487',	'3792');");
		db.execSQL("insert into SongTable14 values(355,	'이석훈',	'눈물로 쓴 편지',	'김세화',	'544',	'252');");
		db.execSQL("insert into SongTable14 values(356,	'이석훈',	'바보처럼 살았군요',	'김도향',	'292',	'392');");
		db.execSQL("insert into SongTable14 values(357,	'이석훈, 조정현',	'그 아픔까지 사랑한 거야',	'조정현',	'1783',	'181');");
		db.execSQL("insert into SongTable14 values(358,	'이정',	'청포도 사랑',	'도미',	'291',	'718');");
		db.execSQL("insert into SongTable14 values(359,	'이정',	'빗속의 여인',	'김건모',	'9590',	'7127');");
		db.execSQL("insert into SongTable14 values(360,	'이정',	'우리는',	'송창식',	'1810',	'2486');");
		db.execSQL("insert into SongTable14 values(361,	'이정',	'내 고향 충청도',	'조영남',	'87',	'1148');");
		db.execSQL("insert into SongTable14 values(362,	'이정',	'흐린 가을 하늘에 편지를 써',	'동물원',	'1854',	'1516');");
		db.execSQL("insert into SongTable14 values(363,	'이정',	'나 홀로 춤을 추긴 너무 외로워',	'김완선',	'1281',	'2597');");
		db.execSQL("insert into SongTable14 values(364,	'이정',	'그녀의 웃음소리뿐',	'이문세',	'1572',	'2247');");
		db.execSQL("insert into SongTable14 values(365,	'이지형',	'예정된 시간을 위해',	'장덕',	'1671',	'2773');");
		db.execSQL("insert into SongTable14 values(366,	'이진성(먼데이키즈)',	'9월의 노래',	'패티김',	'653',	'154');");
		db.execSQL("insert into SongTable14 values(367,	'이혁',	'신라의 달밤',	'현인',	'136',	'512');");
		db.execSQL("insert into SongTable14 values(368,	'이혁',	'열애',	'윤시내',	'387',	'586');");
		db.execSQL("insert into SongTable14 values(369,	'이현',	'청춘을 돌려다오',	'나훈아',	'3458',	'716');");
		db.execSQL("insert into SongTable14 values(370,	'이현',	'여름날의 추억',	'이정석',	'1133',	'2195');");
		db.execSQL("insert into SongTable14 values(371,	'이현',	'언젠가는',	'이상은',	'1406',	'1812');");
		db.execSQL("insert into SongTable14 values(372,	'이현',	'한 동안 뜸했었지',	'사랑과 평화',	'2054',	'2572');");
		db.execSQL("insert into SongTable14 values(373,	'이해리',	'나보다 조금 더 높은 곳에 니가 있을 뿐',	'신승훈',	'3091',	'4110');");
		db.execSQL("insert into SongTable14 values(374,	'이해리',	'못 다 핀 꽃 한 송이',	'김수철',	'450',	'350');");
		db.execSQL("insert into SongTable14 values(375,	'이해리',	'그 누구인가',	'이광조',	'3174',	'정보없음');");
		db.execSQL("insert into SongTable14 values(376,	'이해리',	'빗 속을 거닐며',	'김추자',	'1284',	'2873');");
		db.execSQL("insert into SongTable14 values(377,	'이해리',	'사랑의 트위스트',	'설운도',	'3801',	'4921');");
		db.execSQL("insert into SongTable14 values(378,	'이해리',	'너의 뒤에서',	'박진영',	'2526',	'3607');");
		db.execSQL("insert into SongTable14 values(379,	'이해리',	'하얀 바람',	'소방차',	'626',	'758');");
		db.execSQL("insert into SongTable14 values(380,	'이해리',	'정말로',	'현숙',	'180',	'2523');");
		db.execSQL("insert into SongTable14 values(381,	'이해리, 유열',	'사랑의 찬가 (불꽃 O.S.T)',	'서영은, 유열',	'정보없음',	'7031');");
		db.execSQL("insert into SongTable14 values(382,	'이홍기(FT아일랜드)',	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable14 values(383,	'이홍기(FT아일랜드)',	'그대는 인형처럼 웃고 있지만',	'민해경',	'11',	'834');");
		db.execSQL("insert into SongTable14 values(384,	'이홍기(FT아일랜드)',	'신사동 그 사람',	'주현미',	'902',	'1471');");
		db.execSQL("insert into SongTable14 values(385,	'이홍기(FT아일랜드)',	'모두 다 사랑하리',	'송골매',	'895',	'1411');");
		db.execSQL("insert into SongTable14 values(386,	'이홍기(FT아일랜드), 김희철',	'조조할인',	'이문세, 이적',	'3371',	'4787');");
		db.execSQL("insert into SongTable14 values(387,	'인피니트H',	'오직 하나뿐인 그대',	'심신',	'30',	'598');");
		db.execSQL("insert into SongTable14 values(388,	'인피니트H',	'빗속을 둘이서',	'김정호(원곡) / 투 에이스(금과 은)(TJ, KY)',	'124',	'435');");
		db.execSQL("insert into SongTable14 values(389,	'인피니트H',	'밤이면 밤마다',	'인순이',	'1996',	'1542');");
		db.execSQL("insert into SongTable14 values(390,	'인피니트H',	'부산 갈매기',	'문성재',	'492',	'419');");
		db.execSQL("insert into SongTable14 values(391,	'일락',	'크게 라디오를 켜고',	'시나위'	,'9058',	'62163');");
		db.execSQL("insert into SongTable14 values(392,	'임정희',	'그 후로 오랫동안',	'신승훈',	'2288',	'3539');");
		db.execSQL("insert into SongTable14 values(393,	'임정희',	'나 어떡해',	'샌드 페블즈',	'414',	'1136');");
		db.execSQL("insert into SongTable14 values(394,	'임정희',	'그 때 또 다시',	'임창정',	'3889',	'4978');");
		db.execSQL("insert into SongTable14 values(395,	'임정희',	'이등병의 편지',	'김광석',	'1999',	'3425');");
		db.execSQL("insert into SongTable14 values(396,	'임정희',	'아직도 어두운 밤인가봐',	'전영록',	'1223',	'2462');");
		db.execSQL("insert into SongTable14 values(397,	'임정희',	'모여라',	'송골매',	'2961',	'4367');");
		db.execSQL("insert into SongTable14 values(398,	'임정희',	'진짜 진짜 좋아해',	'혜은이',	'246',	'693');");
		db.execSQL("insert into SongTable14 values(399,	'임정희',	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable14 values(400,	'임태경',	'동백아가씨',	'이미자',	'1229',	'863');");
		db.execSQL("insert into SongTable14 values(401,	'임태경',	'열애',	'윤시내',	'387',	'586');");
		db.execSQL("insert into SongTable14 values(402,	'임태경',	'첫 눈이 온다구요',	'이정석',	'2757',	'1711');");
		db.execSQL("insert into SongTable14 values(403,	'임태경',	'푸르른 날',	'송창식',	'2970',	'2565');");
		db.execSQL("insert into SongTable14 values(404,	'임태경',	'지금',	'조영남',	'730',	'1717');");
		db.execSQL("insert into SongTable14 values(405,	'임태경',	'초련',	'클론',	'8807',	'6308');");
		db.execSQL("insert into SongTable14 values(406,	'임태경',	'잊혀지는 것',	'동물원',	'3745',	'63876');");
		db.execSQL("insert into SongTable14 values(407,	'임태경',	'바닷가에서',	'안다성',	'745',	'382');");
		db.execSQL("insert into SongTable14 values(408,	'임태경',	'아름다운 이별',	'김건모',	'2451',	'3657');");
		db.execSQL("insert into SongTable14 values(409,	'임태경',	'사랑은 생명의 꽃',	'패티김',	'845',	'447');");
		db.execSQL("insert into SongTable14 values(410,	'임태경',	'아직도 그대는 내 사랑',	'이은하',	'514',	'537');");
		db.execSQL("insert into SongTable14 values(411,	'임태경',	'아파트',	'윤수일',	'340',	'539');");
		db.execSQL("insert into SongTable14 values(412,	'임태경',	'그대로 그렇게',	'휘버스',	'688',	'2250');");
		db.execSQL("insert into SongTable14 values(413,	'임태경',	'누가 울어',	'배호',	'3471',	'248');");
		db.execSQL("insert into SongTable14 values(414,	'장미여관',	'올 가을엔 사랑할거야',	'방미',	'214',	'600');");
		db.execSQL("insert into SongTable14 values(415,	'장희영',	'로미오와 줄리엣',	'신승훈',	'1385',	'2080');");
		db.execSQL("insert into SongTable14 values(416,	'장희영',	'모두 다 사랑하리',	'송골매',	'895',	'1411');");
		db.execSQL("insert into SongTable14 values(417,	'전지윤(포미닛)',	'또 만났네요',	'주현미',	'1048',	'1337');");
		db.execSQL("insert into SongTable14 values(418,	'전지윤(포미닛)',	'남행열차',	'김수희',	'911',	'1367');");
		db.execSQL("insert into SongTable14 values(419,	'전지윤(포미닛)',	'삐에로는 우릴 보고 웃지',	'김완선',	'813',	'431');");
		db.execSQL("insert into SongTable14 values(420,	'전지윤(포미닛)',	'성인식',	'박지윤',	'8999',	'6493');");
		db.execSQL("insert into SongTable14 values(421,	'전지윤(포미닛)',	'미워도 다시 한번',	'남진',	'685',	'374');");
		db.execSQL("insert into SongTable14 values(422,	'전지윤(포미닛), 공형진',	'이 세상 살아가다 보면',	'이문세',	'2306',	'2979');");
		db.execSQL("insert into SongTable14 values(423,	'전지윤(포미닛), 구준엽',	'난',	'클론',	'3164',	'4182');");
		db.execSQL("insert into SongTable14 values(424,	'전지윤(포미닛), 이세준',	'이별 이야기',	'이문세, 고은희',	'1408',	'3335');");
		db.execSQL("insert into SongTable14 values(425,	'정동하',	'그럴 수가 있나요',	'김세환(TJ) / 김추자(KY)',	'4173',	'2583');");
		db.execSQL("insert into SongTable14 values(426,	'정동하',	'정 때문에',	'송대관',	'349',	'683');");
		db.execSQL("insert into SongTable14 values(427,	'정동하',	'무정블루스',	'강승모',	'163',	'359');");
		db.execSQL("insert into SongTable14 values(428,	'정동하',	'날 버린 남자',	'하춘화',	'1266',	'2208');");
		db.execSQL("insert into SongTable14 values(429,	'정동하',	'바람이려오',	'이용',	'2557',	'1423');");
		db.execSQL("insert into SongTable14 values(430,	'정동하',	'안개 속으로 가 버린 사랑',	'배호',	'486',	'543');");
		db.execSQL("insert into SongTable14 values(431,	'정동하',	'바람 바람 바람',	'김범룡',	'498',	'386');");
		db.execSQL("insert into SongTable14 values(432,	'정동하',	'동반자',	'태진아',	'13079',	'9744');");
		db.execSQL("insert into SongTable14 values(433,	'정동하',	'Poison',	'엄정화',	'4703',	'5549');");
		db.execSQL("insert into SongTable14 values(434,	'정동하',	'이별여행',	'원미연'	,'391',	'634');");
		db.execSQL("insert into SongTable14 values(435,	'정동하',	'보고 싶은 마음',	'김정호'	,'1068',	'2230');");
		db.execSQL("insert into SongTable14 values(436,	'정동하',	'거위의 꿈',	'인순이'	,'16936',	'81408');");
		db.execSQL("insert into SongTable14 values(437,	'정동하',	'서울의 모정',	'패티김',	'1131',	'2654');");
		db.execSQL("insert into SongTable14 values(438,	'정동하',	'세상 끝에서의 시작 (미스터Q O.S.T)',	'김민종'	,'4665',	'5480');");
		db.execSQL("insert into SongTable14 values(439,	'정동하',	'비상'	,'임재범'	,'9429',	'7153');");
		db.execSQL("insert into SongTable14 values(440,	'정동하, 알리',	'광화문연가',	'이문세',	'1624',	'3557');");
		db.execSQL("insert into SongTable14 values(441,	'정성화',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable14 values(442,	'제아(브라운아이드걸스)',	'사랑이야',	'송창식',	'192',	'1678');");
		db.execSQL("insert into SongTable14 values(443,	'제아(브라운아이드걸스)',	'겨울비는 내리고',	'김범룡',	'410',	'1452');");
		db.execSQL("insert into SongTable14 values(444,	'제아(브라운아이드걸스)',	'고향이 남쪽이랬지',	'송대관',	'2659',	'3827');");
		db.execSQL("insert into SongTable14 values(445,	'제아(브라운아이드걸스), 나르샤',	'3자대면',	'엄정화',	'3946',	'5044');");
		db.execSQL("insert into SongTable14 values(446,	'제아(브라운아이드걸스), 조관우',	'겨울 이야기',	'조관우',	'2855',	'3968');");
		db.execSQL("insert into SongTable14 values(447,	'존박',	'못 잊어',	'패티김',	'588',	'353');");
		db.execSQL("insert into SongTable14 values(448,	'종현(샤이니)',	'백만송이 장미',	'심수봉',	'8093',	'5273');");
		db.execSQL("insert into SongTable14 values(449,	'종현(샤이니)',	'Lonely Night',	'부활',	'4001',	'5087');");
		db.execSQL("insert into SongTable14 values(450,	'종현(샤이니)',	'왼손잡이',	'패닉',	'3070',	'4147');");
		db.execSQL("insert into SongTable14 values(451,	'종현(샤이니)',	'귀로',	'박선주',	'749',	'2245');");
		db.execSQL("insert into SongTable14 values(452,	'주희',	'미인',	'신중현과 엽전들',	'1555',	'2372');");
		db.execSQL("insert into SongTable14 values(453,	'준수(2PM)',	'흐린 기억 속의 그대',	'현진영',	'1183',	'1371');");
		db.execSQL("insert into SongTable14 values(454,	'준수(2PM)',	'보고 싶은 얼굴',	'민해경',	'421',	'408');");
		db.execSQL("insert into SongTable14 values(455,	'준수(2PM)',	'잠깐만',	'주현미',	'405',	'668');");
		db.execSQL("insert into SongTable14 values(456,	'준수(2PM)',	'이별공식',	'R.ef',	'2647',	'3742');");
		db.execSQL("insert into SongTable14 values(457,	'준수(2PM), 이영현',	'우리 사랑 이대로 (연풍연가 O.S.T)',	'주영훈, 이혜진',	'8013',	'5795');");
		db.execSQL("insert into SongTable14 values(458,	'지오(엠블랙)',	'사랑은 차가운 유혹',	'양수경',	'181',	'1044');");
		db.execSQL("insert into SongTable14 values(459,	'지오(엠블랙)',	'사랑은 이제 그만',	'민해경',	'144',	'452');");
		db.execSQL("insert into SongTable14 values(460,	'지오(엠블랙)',	'추억으로 가는 당신',	'주현미',	'190',	'917');");
		db.execSQL("insert into SongTable14 values(461,	'지오(엠블랙)',	'못 잊겠어요',	'김수희',	'208',	'351');");
		db.execSQL("insert into SongTable14 values(462,	'지오(엠블랙)',	'오늘밤',	'김완선',	'3104',	'3074');");
		db.execSQL("insert into SongTable14 values(463,	'지오(엠블랙)',	'너에게로 또 다시',	'변진섭',	'725',	'239');");
		db.execSQL("insert into SongTable14 values(464,	'지오(엠블랙)',	'가슴 아프게',	'남진',	'3463',	'1289');");
		db.execSQL("insert into SongTable14 values(465,	'지오(엠블랙)',	'후인',	'최성수',	'1241',	'2169');");
		db.execSQL("insert into SongTable14 values(466,	'지오(엠블랙)',	'사랑의 시',	'윤시내',	'3620',	'정보없음');");
		db.execSQL("insert into SongTable14 values(467,	'지오(엠블랙)',	'사랑스런 그대',	'윤형주',	'3213',	'4418');");
		db.execSQL("insert into SongTable14 values(468,	'지오(엠블랙)',	'왜 나만',	'이선희',	'4603',	'3046');");
		db.execSQL("insert into SongTable14 values(469,	'지오(엠블랙), 간미연',	'Killer',	'베이비복스',	'8410',	'6035');");
		db.execSQL("insert into SongTable14 values(470,	'지오(엠블랙), 임정희',	'사랑보다 깊은 상처',	'박정현, 임재범',	'4980',	'5810');");
		db.execSQL("insert into SongTable14 values(471,	'지오(엠블랙), 조여정',	'여행을 떠나요',	'조용필',	'2056',	'3493');");
		db.execSQL("insert into SongTable14 values(472,	'진주',	'요즘여자 요즘남자',	'현숙',	'3921',	'4936');");
		db.execSQL("insert into SongTable14 values(473,	'차지연',	'별이 진다네',	'여행스케치',	'1757',	'1571');");
		db.execSQL("insert into SongTable14 values(474,	'차지연',	'이젠 잊기로 해요',	'김완선',	'1687',	'정보없음');");
		db.execSQL("insert into SongTable14 values(475,	'차지연',	'세상에 뿌려진 사랑만큼',	'이승환',	'1324',	'1206');");
		db.execSQL("insert into SongTable14 values(476,	'차지연',	'애수',	'최성수',	'1561',	'2198');");
		db.execSQL("insert into SongTable14 values(477,	'차지연',	'열애',	'윤시내',	'387',	'586');");
		db.execSQL("insert into SongTable14 values(478,	'차지연',	'좋은 걸 어떡해',	'김세환',	'831',	'4575');");
		db.execSQL("insert into SongTable14 values(479,	'차지연',	'눈 오는 밤',	'조하문',	'1317',	'3954');");
		db.execSQL("insert into SongTable14 values(480,	'차지연',	'네박자',	'송대관',	'4650',	'5327');");
		db.execSQL("insert into SongTable14 values(481,	'차지연',	'잡초',	'나훈아',	'603',	'670');");
		db.execSQL("insert into SongTable14 values(482,	'창민(2AM)',	'여자이니까',	'심수봉',	'341',	'577');");
		db.execSQL("insert into SongTable14 values(483,	'창민(2AM)',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable14 values(484,	'창민(2AM)',	'둥지',	'남진',	'8693',	'6235');");
		db.execSQL("insert into SongTable14 values(485,	'창민(2AM)',	'질투',	'유승범',	'1287',	'1298');");
		db.execSQL("insert into SongTable14 values(486,	'창민(2AM)',	'그대는 인형처럼 웃고 있지만',	'민해경',	'11',	'834');");
		db.execSQL("insert into SongTable14 values(487,	'창민(2AM)',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable14 values(488,	'창민(2AM)',	'눈물의 부르스',	'주현미',	'2252',	'2300');");
		db.execSQL("insert into SongTable14 values(489,	'창민(2AM), 이현',	'바다에 누워',	'높은음자리',	'723',	'379');");
		db.execSQL("insert into SongTable14 values(490,	'체리필터',	'굿바이',	'배호',	'13253',	'7784');");
		db.execSQL("insert into SongTable14 values(491,	'체리필터',	'말해줘',	'지누션',	'3994',	'5060');");
		db.execSQL("insert into SongTable14 values(492,	'최진이',	'미소 속에 비친 그대',	'신승훈',	'182',	'370');");
		db.execSQL("insert into SongTable14 values(493,	'최진이',	'토요일 밤에',	'김세환',	'96',	'744');");
		db.execSQL("insert into SongTable14 values(494,	'최코디',	'엉뚱한 상상',	'지누',	'3167',	'4175');");
		db.execSQL("insert into SongTable14 values(495,	'카라',	'하얀 겨울',	'Mr.2',	'1739',	'3216');");
		db.execSQL("insert into SongTable14 values(496,	'케이윌',	'목포의 눈물',	'이난영',	'768',	'346');");
		db.execSQL("insert into SongTable14 values(497,	'케이윌',	'세월 가면',	'신효범(TJ)/이광조(KY)',	'35006',	'4455');");
		db.execSQL("insert into SongTable14 values(498,	'케이윌',	'뛰어',	'최백호',	'2809',	'63844');");
		db.execSQL("insert into SongTable14 values(499,	'케이윌',	'좋아서 만났지요',	'정훈희',	'2807',	'정보없음');");
		db.execSQL("insert into SongTable14 values(500,	'케이윌',	'쌈바의 여인',	'설운도',	'2854',	'3918');");
		db.execSQL("insert into SongTable14 values(501,	'케이윌',	'가을을 남기고 간 사랑',	'패티김',	'360',	'106');");
		db.execSQL("insert into SongTable14 values(502,	'케이윌',	'그녀에게 전해주오',	'소방차',	'1989',	'1646');");
		db.execSQL("insert into SongTable14 values(503,	'케이윌',	'그대 발길 머무는 곳에',	'조용필',	'843',	'171');");
		db.execSQL("insert into SongTable14 values(504,	'케이윌',	'사랑 그 쓸쓸함에 대하여',	'양희은',	'4226',	'5193');");
		db.execSQL("insert into SongTable14 values(505,	'케이윌',	'하루',	'김범수',	'9337',	'6730');");
		db.execSQL("insert into SongTable14 values(506,	'케이윌',	'사랑할거야',	'이상은',	'934',	'466');");
		db.execSQL("insert into SongTable14 values(507,	'케이윌',	'슬픔이여 안녕',	'이숙',	'3330',	'2646');");
		db.execSQL("insert into SongTable14 values(508,	'케이윌',	'한 사람을 위한 마음',	'이승환(TJ)/럼블피쉬(KY)',	'1114',	'86062');");
		db.execSQL("insert into SongTable14 values(509,	'케이윌, 뱅크',	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable14 values(510,	'클로버',	'회상',	'터보',	'4162',	'5169');");
		db.execSQL("insert into SongTable14 values(511,	'킹스턴루디스카',	'아름다운 것들',	'양희은',	'856',	'2037');");
		db.execSQL("insert into SongTable14 values(512,	'태민(샤이니)',	'한 번쯤',	'송창식',	'1322',	'991');");
		db.execSQL("insert into SongTable14 values(513,	'태민(샤이니)',	'도시여 안녕',	'조영남',	'1304',	'1041');");
		db.execSQL("insert into SongTable14 values(514,	'태민(샤이니)',	'몰라',	'엄정화',	'8290',	'5932');");
		db.execSQL("insert into SongTable14 values(515,	'태민(샤이니)',	'마포종점',	'은방울자매',	'824',	'327');");
		db.execSQL("insert into SongTable14 values(516,	'태민(샤이니)',	'잘못된 만남',	'김건모',	'2454',	'3658');");
		db.execSQL("insert into SongTable14 values(517,	'태민(샤이니)',	'4월이 가면',	'패티김',	'1827',	'3876');");
		db.execSQL("insert into SongTable14 values(518,	'태민(샤이니)',	'밤차',	'이은하',	'2695',	'1180');");
		db.execSQL("insert into SongTable14 values(519,	'태민(샤이니)',	'굳세어라 금순아',	'현인',	'574',	'156');");
		db.execSQL("insert into SongTable14 values(520,	'태민(샤이니)',	'황홀한 고백',	'윤수일',	'435',	'998');");
		db.execSQL("insert into SongTable14 values(521,	'투빅',	'후애',	'엄정화',	'3909',	'5112');");
		db.execSQL("insert into SongTable14 values(522,	'투빅',	'너에게로 또 다시',	'변진섭',	'725',	'239');");
		db.execSQL("insert into SongTable14 values(523,	'틴탑',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable14 values(524,	'틴탑',	'꽃집아가씨',	'윤형주(원곡) / 봉봉사중창단(TJ, KY)',	'4037',	'821');");
		db.execSQL("insert into SongTable14 values(525,	'틴탑',	'몰라',	'엄정화',	'8290',	'5932');");
		db.execSQL("insert into SongTable14 values(526,	'틴탑',	'내 마음의 보석상자',	'해바라기',	'767',	'1457');");
		db.execSQL("insert into SongTable14 values(527,	'팀(Tim)',	'나침반',	'설운도',	'394',	'210');");
		db.execSQL("insert into SongTable14 values(528,	'팀(Tim)',	'가나다라',	'송창식',	'2188',	'4201');");
		db.execSQL("insert into SongTable14 values(529,	'팀(Tim)',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable14 values(530,	'팝핀현준, 박애리',	'내 마음 별과 같이',	'현철',	'432',	'839');");
		db.execSQL("insert into SongTable14 values(531,	'팝핀현준, 박애리',	'날이 갈수록',	'김정호',	'835',	'219');");
		db.execSQL("insert into SongTable14 values(532,	'팝핀현준, 박애리',	'목포는 항구다',	'이난영',	'206',	'345');");
		db.execSQL("insert into SongTable14 values(533,	'팝핀현준, 박애리',	'공항의 이별',	'문주란',	'785',	'142');");
		db.execSQL("insert into SongTable14 values(534,	'포맨',	'떠나야 할 그 사람',	'펄 시스터즈',	'17198',	'4346');");
		db.execSQL("insert into SongTable14 values(535,	'포맨',	'사랑의 시',	'해바라기',	'1733',	'1674');");
		db.execSQL("insert into SongTable14 values(536,	'포맨',	'비나리',	'심수봉',	'2927',	'3778');");
		db.execSQL("insert into SongTable14 values(537,	'포맨',	'조조할인',	'이문세, 이적',	'3371',	'4787');");
		db.execSQL("insert into SongTable14 values(538,	'피아',	'붉은 낙타',	'이승환',	'3704',	'7154');");
		db.execSQL("insert into SongTable14 values(539,	'하우스룰즈',	'잘했군 잘했어',	'하춘화',	'5202',	'4561');");
		db.execSQL("insert into SongTable14 values(540,	'허각',	'멍에',	'김수희',	'318',	'338');");
		db.execSQL("insert into SongTable14 values(541,	'허각',	'리듬 속의 그 춤을',	'김완선',	'3634',	'4350');");
		db.execSQL("insert into SongTable14 values(542,	'허각',	'오직 너뿐인 나를',	'이승철',	'8100',	'5842');");
		db.execSQL("insert into SongTable14 values(543,	'허각',	'나에게 애인이 있다면',	'남진',	'1143',	'3899');");
		db.execSQL("insert into SongTable14 values(544,	'허각',	'Q',	'조용필',	'1201',	'1576');");
		db.execSQL("insert into SongTable14 values(545,	'허각',	'내게 오는 길',	'성시경',	'9256',	'6679');");
		db.execSQL("insert into SongTable14 values(546,	'허각',	'서른 즈음에',	'김광석',	'2337',	'4448');");
		db.execSQL("insert into SongTable14 values(547,	'허각',	'바람아 멈추어다오',	'이지연',	'171',	'388');");
		db.execSQL("insert into SongTable14 values(548,	'허각',	'세상만사',	'송골매',	'12286',	'63642');");
		db.execSQL("insert into SongTable14 values(549,	'허각',	'제 3한강교',	'혜은이',	'1696',	'1250');");
		db.execSQL("insert into SongTable14 values(550,	'허각',	'사랑했어요',	'김현식',	'1362',	'1195');");
		db.execSQL("insert into SongTable14 values(551,	'허각',	'즐거운 인생',	'이광조',	'3152',	'3115');");
		db.execSQL("insert into SongTable14 values(552,	'허각',	'입영전야',	'최백호',	'1180',	'963');");
		db.execSQL("insert into SongTable14 values(553,	'허각',	'안개',	'정훈희',	'338',	'541');");
		db.execSQL("insert into SongTable14 values(554,	'허각',	'또 한 번 사랑은 가고',	'이기찬',	'9643',	'7568');");
		db.execSQL("insert into SongTable14 values(555,	'허각',	'인디안 인형처럼',	'나미',	'14',	'645');");
		db.execSQL("insert into SongTable14 values(556,	'허각',	'상록수',	'양희은',	'4780',	'5673');");
		db.execSQL("insert into SongTable14 values(557,	'허각',	'작은 새',	'어니언스',	'9',	'661');");
		db.execSQL("insert into SongTable14 values(558,	'허각',	'눈물 젖은 두만강',	'김정구',	'748',	'255');");
		db.execSQL("insert into SongTable14 values(559,	'허각',	'사랑이 지나가면',	'이문세',	'604',	'1190');");
		db.execSQL("insert into SongTable14 values(560,	'허각, 이시영',	'일과 이분의 일',	'투투',	'2133',	'3436');");
		db.execSQL("insert into SongTable14 values(561,	'허각, 임병수',	'사랑이란 말은 너무 너무 흔해',	'임병수',	'637',	'1675');");
		db.execSQL("insert into SongTable14 values(562,	'허각, 홍종구',	'너에게 원한 건',	'노이즈',	'882',	'1585');");
		db.execSQL("insert into SongTable14 values(563,	'허영생(SS501)',	'빨간 우산',	'김건모',	'3137',	'4143');");
		db.execSQL("insert into SongTable14 values(564,	'홍경민',	'해야',	'마그마',	'2145',	'4626');");
		db.execSQL("insert into SongTable14 values(565,	'홍경민',	'작은 연인들',	'권태수, 김세화',	'444',	'968');");
		db.execSQL("insert into SongTable14 values(566,	'홍경민',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable14 values(567,	'홍경민',	'너무 아픈 사랑은 사랑이 아니었음을',	'김광석',	'13100',	'63677');");
		db.execSQL("insert into SongTable14 values(568,	'홍경민',	'하얀 밤에',	'전영록',	'8304',	'2351');");
		db.execSQL("insert into SongTable14 values(569,	'홍경민',	'비가',	'혜은이',	'569',	'3481');");
		db.execSQL("insert into SongTable14 values(570,	'홍경민',	'언제나 그대 내 곁에',	'김현식',	'1883',	'3213');");
		db.execSQL("insert into SongTable14 values(571,	'홍경민',	'내 나라 내 겨레',	'송창식',	'정보없음',	'4285');");
		db.execSQL("insert into SongTable14 values(572,	'홍경민',	'집착',	'박미경',	'8334',	'5973');");
		db.execSQL("insert into SongTable14 values(573,	'홍경민',	'핑계',	'김건모',	'1738',	'3302');");
		db.execSQL("insert into SongTable14 values(574,	'홍경민',	'미녀와 야수',	'DJ DOC',	'2891',	'4008');");
		db.execSQL("insert into SongTable14 values(575,	'홍경민',	'가버린 친구에게 바침',	'휘버스',	'1296',	'2215');");
		db.execSQL("insert into SongTable14 values(576,	'홍경민',	'사랑해 그리고 기억해',	'god',	'8540',	'6136');");
		db.execSQL("insert into SongTable14 values(577,	'홍경민',	'장미빛 스카프',	'윤항기',	'28',	'673');");
		db.execSQL("insert into SongTable14 values(578,	'홍경민',	'일급비밀',	'소방차',	'440',	'959');");
		db.execSQL("insert into SongTable14 values(579,	'홍경민',	'친구여',	'조용필',	'779',	'732');");
		db.execSQL("insert into SongTable14 values(580,	'홍경민',	'늙은 군인의 노래',	'양희은',	'2002',	'1656');");
		db.execSQL("insert into SongTable14 values(581,	'홍경민',	'애상',	'쿨',	'4509',	'5422');");
		db.execSQL("insert into SongTable14 values(582,	'홍경민',	'사랑은 나비인가봐',	'현철',	'4011',	'445');");
		db.execSQL("insert into SongTable14 values(583,	'홍경민',	'순정',	'코요태',	'4959',	'5767');");
		db.execSQL("insert into SongTable14 values(584,	'홍경민',	'좋은 걸 어떡해',	'김세환',	'831',	'4575');");
		db.execSQL("insert into SongTable14 values(585,	'홍경민',	'청춘을 돌려다오',	'나훈아',	'3458',	'716');");
		db.execSQL("insert into SongTable14 values(586,	'홍경민',	'어서 말을 해',	'해바라기',	'14998',	'2943');");
		db.execSQL("insert into SongTable14 values(587,	'화요비',	'어제 내린 비',	'윤형주',	'470',	'566');");
		db.execSQL("insert into SongTable14 values(588,	'화요비',	'마음 약해서',	'들고양이들',	'79',	'319');");
		db.execSQL("insert into SongTable14 values(589,	'화요비',	'영암아리랑',	'하춘화',	'5222',	'4515');");
		db.execSQL("insert into SongTable14 values(590,	'화요비',	'몰래 한 사랑',	'김지애',	'48',	'348');");
		db.execSQL("insert into SongTable14 values(591,	'화요비',	'영시의 이별',	'배호',	'1769',	'937');");
		db.execSQL("insert into SongTable14 values(592,	'환희',	'갈대의 순정',	'박일남',	'330',	'109');");
		db.execSQL("insert into SongTable14 values(593,	'효린(씨스타)',	'그 때 그 사람',	'심수봉',	'374',	'176');");
		db.execSQL("insert into SongTable14 values(594,	'효린(씨스타)',	'희야',	'부활',	'1824',	'2015');");
		db.execSQL("insert into SongTable14 values(595,	'효린(씨스타)',	'Memories…(Smiling Tears)',	'T(윤미래)',	'9890',	'7883');");
		db.execSQL("insert into SongTable14 values(596,	'효린(씨스타)',	'미니스커트',	'민해경',	'946',	'365');");
		db.execSQL("insert into SongTable14 values(597,	'효린(씨스타)',	'오늘 같은 밤이면',	'박정운',	'1249',	'1292');");
		db.execSQL("insert into SongTable14 values(598,	'효린(씨스타)',	'거위의 꿈',	'카니발',	'4241',	'5186');");
		db.execSQL("insert into SongTable14 values(599,	'효린(씨스타)',	'비 내리는 영동교',	'주현미',	'815',	'1290');");
		db.execSQL("insert into SongTable14 values(600,	'효린(씨스타)',	'서울여자',	'김수희',	'809',	'489');");
		db.execSQL("insert into SongTable14 values(601,	'효린(씨스타)',	'나홀로 춤을 추긴 너무 외로워',	'김완선',	'1281',	'2597');");
		db.execSQL("insert into SongTable14 values(602,	'효린(씨스타)',	'My Name',	'보아',	'13386',	'9801');");
		db.execSQL("insert into SongTable14 values(603,	'효린(씨스타)',	'마음이 고와야지',	'남진',	'4023',	'322');");
		db.execSQL("insert into SongTable14 values(604,	'효린(씨스타)',	'커피 한 잔',	'펄 시스터즈',	'141',	'736');");
		db.execSQL("insert into SongTable14 values(605,	'효린(씨스타), 신용재',	'이제는',	'서울패밀리',	'1956',	'3498');");
		db.execSQL("insert into SongTable14 values(606,	'효린(씨스타), 이현우',	'날 떠나지마',	'박진영',	'2357',	'3569');");
		db.execSQL("insert into SongTable14 values(607,	'효린(씨스타), DJ DOC',	'사랑을 아직도 난',	'DJ DOC',	'9111',	'6556');");
		db.execSQL("insert into SongTable14 values(608,	'휘성',	'노란 샤쓰의 사나이',	'한명숙',	'240',	'243');");
		db.execSQL("insert into SongTable14 values(609,	'하동균',	'사랑한 후에',	'들국화',	'1844',	'3422');");
		db.execSQL("insert into SongTable14 values(610,	'JK김동욱',	'그것만이 내 세상',	'들국화',	'1780',	'1329');");
		db.execSQL("insert into SongTable14 values(611,	'박재범',	'매일 그대와',	'들국화',	'1905',	'4360');");
		db.execSQL("insert into SongTable14 values(612,	'유미',	'세계로 가는 기차',	'들국화',	'1743',	'1344');");
		db.execSQL("insert into SongTable14 values(613,	'정동하',	'제발',	'들국화',	'3414',	'65612');");
		db.execSQL("insert into SongTable14 values(614,	'포맨',	'사랑일 뿐이야',	'들국화',	'3313',	'정보없음');");
		db.execSQL("insert into SongTable14 values(615,	'더 원',	'이별이란 없는 거야',	'최성원(들국화)',	'1820',	'2186');");
		db.execSQL("insert into SongTable14 values(616,	'스윗소로우',	'돌고 돌고 돌고',	'들국화',	'1900',	'4334');");
		db.execSQL("insert into SongTable14 values(617,	'이정',	'행진',	'들국화',	'1113',	'2577');");
		db.execSQL("insert into SongTable14 values(618,	'알리',	'걱정 말아요 그대',	'들국화',	'14251',	'45040');");
		db.execSQL("insert into SongTable14 values(619,	'문명진',	'아침이 밝아올 때까지',	'들국화',	'1718',	'2334');");
		db.execSQL("insert into SongTable14 values(620,	'부가킹즈',	'사노라면',	'들국화',	'1758',	'2411');");
		db.execSQL("insert into SongTable14 values(621,	'문명진',	'그런 사람 또 없습니다 (슬픔보다 더 슬픈 이야기 O.S.T)',	'이승철',	'30751',	'46557');");
		db.execSQL("insert into SongTable14 values(622,	'허각',	'서쪽하늘 (청연 O.S.T)',	'이승철',	'15527',	'45430');");
		db.execSQL("insert into SongTable14 values(623,	'에일리',	'희야',	'부활',	'1824',	'2015');");
		db.execSQL("insert into SongTable14 values(624,	'영지',	'마지막 콘서트',	'이승철',	'1330',	'326');");
		db.execSQL("insert into SongTable14 values(625,	'FT아일랜드',	'말리꽃 (비천무 O.S.T)',	'이승철',	'8941',	'6438');");
		db.execSQL("insert into SongTable14 values(626,	'울랄라세션',	'방황',	'이승철',	'1198',	'1548');");
		db.execSQL("insert into SongTable14 values(627,	'케이윌',	'오직 너뿐인 나를',	'이승철',	'8100',	'5842');");
		db.execSQL("insert into SongTable14 values(628,	'정인',	'친구의 친구를 사랑했네',	'이승철',	'2017',	'2765');");
		db.execSQL("insert into SongTable14 values(629,	'나인',	'안녕이라고 말하지 마',	'이승철',	'471',	'547');");
		db.execSQL("insert into SongTable14 values(630,	'바다',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable14 values(631,	'신용재',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable14 values(632,	'이정',	'인연 (불새 O.S.T)',	'이승철',	'13165',	'9775');");
		db.execSQL("insert into SongTable14 values(633,	'팝핀현준, 박애리',	'아리랑',	'경기도민요',	'1',	'525');");
		db.execSQL("insert into SongTable14 values(634,	'정동하',	'쾌지나 칭칭 나네',	'경상도민요',	'정보없음',	'4600');");
		db.execSQL("insert into SongTable14 values(635,	'포미닛',	'늴리리야',	'민요',	'5542',	'63120');");
		db.execSQL("insert into SongTable14 values(636,	'문명진',	'군밤타령',	'경기도민요',	'5203',	'155');");
		db.execSQL("insert into SongTable14 values(637,	'임태경',	'새타령',	'김세레나(한국민요)',	'5211',	'482');");
		db.execSQL("insert into SongTable14 values(638,	'바다',	'한 오백년',	'조용필(강원도민요)',	'5209',	'764');");
		db.execSQL("insert into SongTable14 values(639,	'더 포지션',	'꿈에',	'조덕배',	'1515',	'2278');");
		db.execSQL("insert into SongTable14 values(640,	'문명진',	'슬픈 노래는 부르지 않을 거야',	'조덕배',	'2089',	'1699');");
		db.execSQL("insert into SongTable14 values(641,	'바다',	'나의 옛날 이야기',	'조덕배',	'1728',	'1492');");
		db.execSQL("insert into SongTable14 values(642,	'엠블랙',	'그대 내 맘에 들어오면은',	'조덕배',	'910',	'2249');");
		db.execSQL("insert into SongTable14 values(643,	'팀(Tim)',	'여인이여',	'박남정',	'964',	'2268');");
		db.execSQL("insert into SongTable14 values(644,	'핫젝갓알지',	'비에 스친 날들',	'박남정',	'17254',	'1370');");
		db.execSQL("insert into SongTable14 values(645,	'바다',	'사랑의 불시착',	'박남정',	'123',	'457');");
		db.execSQL("insert into SongTable14 values(646,	'플라워',	'안녕 내 사랑',	'박남정',	'3111',	'546');");
		db.execSQL("insert into SongTable14 values(647,	'틴탑, 100%',	'널 그리며',	'박남정',	'351',	'241');");
		db.execSQL("insert into SongTable14 values(648,	'유미',	'잃어버린 30년',	'설운도',	'770',	'647');");
		db.execSQL("insert into SongTable14 values(649,	'커먼 그라운드, 애쉬그레이',	'마음이 울적해서',	'설운도',	'300',	'323');");
		db.execSQL("insert into SongTable14 values(650,	'B1A4',	'누이',	'설운도',	'8335',	'5994');");
		db.execSQL("insert into SongTable14 values(651,	'주석, 홍진영',	'쌈바의 여인',	'설운도',	'2854',	'3918');");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable14");
		onCreate(db);
	}
}