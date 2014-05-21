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

public class WhatTheSong17 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_17);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_17",
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
		cursor = db.rawQuery("SELECT * FROM SongTable17 WHERE song LIKE '"
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

	public void callList(){
		int[] randid = new int[LISTSIZE];
		song = new String[LISTSIZE];
		singer = new String[LISTSIZE];
		tj = new String[LISTSIZE];
		ky = new String[LISTSIZE];
		participant = new String[LISTSIZE];
		
		int count = 0;
		myList = new ArrayList<Map<String, String>>();

		WhatTheSongDBHelper17 wtsHelper17 = new WhatTheSongDBHelper17(this);
		db = wtsHelper17.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable17 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable17 where id= '"
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
	
	@Override // 클릭 시
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final int p = position;
		new AlertDialog.Builder(WhatTheSong17.this)
				.setTitle(song[p]+ " - " + singer[p])
				.setIcon(R.drawable.icon_small)
				.setMessage("♪ 부른사람 : "+ participant[p] + "\n" + "♪ 태진 : " + tj[p] + ", ♪ 금영 : " + ky[p])
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
											WhatTheSong17.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(WhatTheSong17.this,
											"이미 즐겨찾기에 등록되어 있는 곡입니다.",
											Toast.LENGTH_LONG).show();
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
			Intent newActivity = new Intent(WhatTheSong17.this,
					WhatTheSongBookmark.class);
			WhatTheSong17.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
		}else if (v.getId() == R.id.search) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
			String edit = SearchText.getText().toString();
			cursor = db.rawQuery("SELECT * FROM SongTable17 WHERE song LIKE '"
					+ edit + "%' OR song LIKE '%" + edit +"%' OR song LIKE '%" + edit + "' OR singer LIKE '"
					+ edit + "%' OR singer LIKE '%" + edit +"%' OR singer LIKE '%" + edit + "' OR participant LIKE '"
					+ edit + "%' OR participant LIKE '%" + edit +"%' OR participant LIKE '%" + edit + "'", null);
			if (cursor.getCount() == 0) {
				Toast.makeText(WhatTheSong17.this, "검색 결과가 없습니다.",
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
					"m_pref_17", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper17 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper17(Context context) {
		super(context, "whatthesong17.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '슈퍼스타k 테마' DB (130627 : 441개)
		db.execSQL("CREATE TABLE SongTable17 ( id INTEGER ," + " participant TEXT ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable17 values(1,	'정준영',	'뭐라 할까!',	'브리즈(The Breeze)',	'13591',	'68379');");
		db.execSQL("insert into SongTable17 values(2,	'쾌남과 옥구슬',	'Funky Tonight',	'클론',	'8193',	'5847');");
		db.execSQL("insert into SongTable17 values(3,	'쾌남과 옥구슬',	'그 여자',	'백지영',	'33282',	'76732');");
		db.execSQL("insert into SongTable17 values(4,	'딕펑스',	'My Precious',	'딕펑스',	'정보없음',	'87381');");
		db.execSQL("insert into SongTable17 values(5,	'딕펑스',	'좋다 좋아',	'딕펑스',	'35891',	'47868');");
		db.execSQL("insert into SongTable17 values(6,	'안예슬',	'Why',	'Avril Lavigne',	'정보없음',	'60863');");
		db.execSQL("insert into SongTable17 values(7,	'안예슬',	'이런밤',	'화요비',	'10257',	'65886');");
		db.execSQL("insert into SongTable17 values(8,	'연규성 ',	'인연',	'이승철',	'13165',	'9775');");
		db.execSQL("insert into SongTable17 values(9,	'연규성 ',	'듣고 있나요',	'이승철',	'30445',	'46479');");
		db.execSQL("insert into SongTable17 values(10,	'로이킴',	'Volcano',	'Damien Rice',	'정보없음',	'79135');");
		db.execSQL("insert into SongTable17 values(11,	'박상보',	'행복하지 말아요',	'M.C. the Max',	'14260',	'45046');");
		db.execSQL("insert into SongTable17 values(12,	'유승우',	'석봉아',	'불나방 스타 쏘세지 클럽',	'31344',	'84358');");
		db.execSQL("insert into SongTable17 values(13,	'유승우',	'The Lazy Song',	'Bruno Mars',	'22216',	'79049');");
		db.execSQL("insert into SongTable17 values(14,	'육진수',	'해바라기',	'박상민',	'12653',	'9674');");
		db.execSQL("insert into SongTable17 values(15,	'김승재',	'Into The Rain',	'뮤즈그레인',	'16684',	'81261');");
		db.execSQL("insert into SongTable17 values(16,	'정희훈 ',	'Superstar',	'Carpenters',	'7038',	'8692');");
		db.execSQL("insert into SongTable17 values(17,	'주재우',	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable17 values(18,	'김민준',	' 편지',	'김광진',	'9505',	'7301');");
		db.execSQL("insert into SongTable17 values(19,	'계범주',	'누구없소?',	'한영애',	'691',	'250');");
		db.execSQL("insert into SongTable17 values(20,	'임병석',	'그땐 미처 알지 못했지',	'이적',	'11369',	'9405');");
		db.execSQL("insert into SongTable17 values(21,	'김승아',	'고백하기 좋은날',	'윤하',	'17837',	'81701');");
		db.execSQL("insert into SongTable17 values(22,	'홍대광',	'하늘을 날아',	'메이트',	'31808',	'84482');");
		db.execSQL("insert into SongTable17 values(23,	'홍대광',	'벚꽃 엔딩',	'버스커 버스커',	'35184',	'47700');");
		db.execSQL("insert into SongTable17 values(24,	'허니브라운',	'이노래',	'2AM',	'19848',	'83678');");
		db.execSQL("insert into SongTable17 values(25,	'김태민',	'외톨이야',	'CNBLUE',	'32118',	'46905');");
		db.execSQL("insert into SongTable17 values(26,	'장현규 ',	'다시와주라',	'바이브',	'32596',	'47019');");
		db.execSQL("insert into SongTable17 values(27,	'2Ms',	'Perhaps Love',	'J,하울',	'15637',	'45460');");
		db.execSQL("insert into SongTable17 values(28,	'김정환',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable17 values(29,	'이용혁',	'엄마',	'라디(Ra.D)',	'36202',	'87031');");
		db.execSQL("insert into SongTable17 values(30,	'라이브하이',	'Love On Top',	'Beyonce',	'22340',	'79016');");
		db.execSQL("insert into SongTable17 values(31,	'조앤',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable17 values(32,	'강용석',	'그대 내게 행복을 주는 사람',	'김현철',	'10565',	'7119');");
		db.execSQL("insert into SongTable17 values(33,	'오서정',	'Video Killed The Radio Star',	'Presidents of the United States of America',	'22181',	'정보없음');");
		db.execSQL("insert into SongTable17 values(34,	'박다빈',	'Lonely',	'2NE1',	'33904',	'76914');");
		db.execSQL("insert into SongTable17 values(35,	'임우진,쏘울라이츠',	'도시의 밤',	'쏘울라이츠',	'35803',	'77384');");
		db.execSQL("insert into SongTable17 values(36,	'김승한',	'Born To Rock',	'W&Whale',	'31597',	'84454');");
		db.execSQL("insert into SongTable17 values(37,	'420',	'어머님께',	'god',	'4988',	'5783');");
		db.execSQL("insert into SongTable17 values(38,	'소울라이츠',	'Gee',	'소녀시대',	'30627',	'84011');");
		db.execSQL("insert into SongTable17 values(39,	'푸트리노리자',	'상처',	'알리',	'35225',	'58571');");
		db.execSQL("insert into SongTable17 values(40,	'서창모',	'Come Back Home',	'서태지와 아이들',	'2751',	'3912');");
		db.execSQL("insert into SongTable17 values(41,	'서창모',	'환상속의 그대',	'서태지와 아이들',	'645',	'1352');");
		db.execSQL("insert into SongTable17 values(42,	'황설린',	'Forget You',	'Karmin',	'정보없음',	'79148');");
		db.execSQL("insert into SongTable17 values(43,	'가을',	'고백',	'4men',	'15865',	'85044');");
		db.execSQL("insert into SongTable17 values(44,	'이상아',	'2Hot',	'지나',	'35395',	'47754');");
		db.execSQL("insert into SongTable17 values(45,	'은종엽',	'10월의 어느 멋진날에',	'김동규',	'15576',	'68526');");
		db.execSQL("insert into SongTable17 values(46,	'김정현',	'천년의 사랑',	'박완규',	'8447',	'6048');");
		db.execSQL("insert into SongTable17 values(47,	'손범준',	'Mama Do',	'Pixie Lott',	'22320',	'63177');");
		db.execSQL("insert into SongTable17 values(48,	'이경비',	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable17 values(49,	'홍돈희',	'Live High',	'Jason Mraz',	'정보없음',	'79146');");
		db.execSQL("insert into SongTable17 values(50,	'이병호',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable17 values(51,	'이병호',	'거꾸로 강을 거슬러 오르는 저 힘찬 연어들처럼',	'강산에',	'4582',	'5443');");
		db.execSQL("insert into SongTable17 values(52,	'이종수',	'그대라면',	'알렉스(디어웨딩)',	'19729',	'83618');");
		db.execSQL("insert into SongTable17 values(53,	'트윙클 시스터즈,김정환',	'Twinkle',	'태티서',	'35315',	'47731');");
		db.execSQL("insert into SongTable17 values(54,	'최다언',	'헤어져야 사랑을 알죠',	'리사',	'16047',	'69986');");
		db.execSQL("insert into SongTable17 values(55,	'이보경',	'하모니',	'이영현,제아',	'32028',	'84690');");
		db.execSQL("insert into SongTable17 values(56,	'박다영',	'너도 나처럼',	'2AM',	'35113',	'77212');");
		db.execSQL("insert into SongTable17 values(57,	'김지섭',	'Mercy',	'Duffy',	'22324',	'60163');");
		db.execSQL("insert into SongTable17 values(58,	'제뉴',	'Officially Missing You',	'긱스',	'35828',	'86884');");
		db.execSQL("insert into SongTable17 values(59,	'팻듀오',	'난 여자가 있는데',	'박진영',	'9547',	'6881');");
		db.execSQL("insert into SongTable17 values(60,	'제니퍼 정',	'그리움만 쌓이네',	'노영심',	'2618',	'3782');");
		db.execSQL("insert into SongTable17 values(61,	'B#',	'Lovey-Dovey',	'티아라',	'34847',	'77144');");
		db.execSQL("insert into SongTable17 values(62,	'이지혜',	'나 돌아가',	'임정희(박진영)',	'19252',	'85061');");
		db.execSQL("insert into SongTable17 values(63,	'정소희',	'여러분',	'윤복희',	'534',	'1310');");
		db.execSQL("insert into SongTable17 values(64,	'이아름',	'별을 찾는 아이',	'아이유',	'34709',	'77106');");
		db.execSQL("insert into SongTable17 values(65,	'로이킴',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable17 values(66,	'연규성',	'슬픔활용법',	'김범수',	'30050',	'46390');");
		db.execSQL("insert into SongTable17 values(67,	'정준영',	'박하사탕',	'YB',	'9699',	'7640');");
		db.execSQL("insert into SongTable17 values(68,	'김정환/유승우/신창일/안예슬/김지섭',	'강남스타일',	'싸이',	'35608',	'47802');");
		db.execSQL("insert into SongTable17 values(69,	'쾌남과 옥구슬/딕펑스',	'Pretty Girl',	'카라',	'30505',	'83953');");
		db.execSQL("insert into SongTable17 values(70,	'정준영/로이킴/오서정/성우리/최다언',	'언젠가는',	'이상은',	'1406',	'1812');");
		db.execSQL("insert into SongTable17 values(71,	'계범주/박다빈/김진현/류다미/전유화',	'Twinkle',	'태티서',	'35315',	'47731');");
		db.execSQL("insert into SongTable17 values(72,	'이재연/진성호/강혜인/김우영/임우진',	'삐에로는 우릴보고 웃지',	'김완선',	'정보없음',	'431');");
		db.execSQL("insert into SongTable17 values(73,	'김승재/최민준/정희훈/김유희/김승아',	'미안해 널 미워해',	'자우림',	'4865',	'5710');");
		db.execSQL("insert into SongTable17 values(74,	'테이커스/허니브라운/박지용',	'너뿐이야',	'박진영',	'35314',	'77254');");
		db.execSQL("insert into SongTable17 values(75,	'이우상/김상진/김민지/김민성/차수경/이지혜',	'Hug',	'동방신기',	'12619',	'9645');");
		db.execSQL("insert into SongTable17 values(76,	'볼륨/라이브 하이',	'Sixth Sense',	'브라운 아이드 걸스',	'34445',	'47519');");
		db.execSQL("insert into SongTable17 values(77,	'이아름/연규성/홍대광/이보경',	'전활 받지 않는 너에게',	'2AM',	'33225',	'58062');");
		db.execSQL("insert into SongTable17 values(78,	'박력/이승철/오윤지/주재우/박다영',	'소원을 말해봐',	'소녀시대',	'31311',	'46694');");
		db.execSQL("insert into SongTable17 values(79,	'이범준/김재오/홍돈희/손범준',	'향수뿌리지마',	'틴탑',	'34219',	'47458');");
		db.execSQL("insert into SongTable17 values(80,	'정승수/쏠림/이정우/강민정/임병석',	'성인식',	'박지윤',	'8999',	'6493');");
		db.execSQL("insert into SongTable17 values(81,	'남기태/김현실/정석환/나찬영/푸트리/조나영',	'아파',	'2NE1',	'33060',	'86657');");
		db.execSQL("insert into SongTable17 values(82,	'가로등 라디오/마요네즈',	'Sherlock ',	'샤이니',	'35141',	'47685');");
		db.execSQL("insert into SongTable17 values(83,	'로이킴/정준영',	'먼지가 되어',	'김광석',	'10354',	'62285');");
		db.execSQL("insert into SongTable17 values(84,	'로이킴/정준영',	'먼지가 되어',	'로이킴/정준영',	'35965',	'47888');");
		db.execSQL("insert into SongTable17 values(85,	'계범주/푸트리노리자',	'Music Is My Life ',	'임정희',	'14915',	'45220');");
		db.execSQL("insert into SongTable17 values(86,	'소울라이츠/라이브하이',	'Be My Baby ',	'원더걸스',	'34613',	'77086');");
		db.execSQL("insert into SongTable17 values(87,	'최민준/박지용',	'니가 사는 그 집 ',	'박진영',	'18884',	'46116');");
		db.execSQL("insert into SongTable17 values(88,	'이지혜/박다영',	'인연 ',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable17 values(89,	'안예슬/오서정',	'Lonely ',	'2NE1',	'33904',	'76914');");
		db.execSQL("insert into SongTable17 values(90,	'딕펑스/김진현',	'금지된 사랑 ',	'김경호',	'4074',	'5114');");
		db.execSQL("insert into SongTable17 values(91,	'볼륨/쾌남과 옥구슬',	'Run Devil Run ',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable17 values(92,	'김승아/최다언',	'친구라도 될 걸 그랬어 ',	'거미',	'11238',	'63853');");
		db.execSQL("insert into SongTable17 values(93,	'마요네즈/진성호',	'그때 그 사람 ',	'심수봉',	'374',	'176');");
		db.execSQL("insert into SongTable17 values(94,	'이보경/정은우',	'Good Bye Sadness, Hello Happiness',	'윤미래',	'19379',	'85548');");
		db.execSQL("insert into SongTable17 values(95,	'강민정/손범준',	'Fantastic Baby',	'빅뱅',	'35073',	'77196');");
		db.execSQL("insert into SongTable17 values(96,	'허니브라운/테이커스',	'오늘 같은 밤',	'박정운',	'1249',	'1292');");
		db.execSQL("insert into SongTable17 values(97,	'이정우/김우영',	'사랑밖엔 난 몰라',	'심수봉',	'1486',	'1468');");
		db.execSQL("insert into SongTable17 values(98,	'홍대광/연규성',	'말리꽃 ',	'이승철',	'8941',	'6438');");
		db.execSQL("insert into SongTable17 values(99,	'홍대광/연규성',	'말리꽃 ',	'홍대광/연규성',	'정보없음',	'58825');");
		db.execSQL("insert into SongTable17 values(100,	'유승우/김정환',	'I Love You',	'2NE1',	'35561',	'47793');");
		db.execSQL("insert into SongTable17 values(101,	'계범주',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable17 values(102,	'이지혜',	'천일동안',	'이승환',	'2679',	'3853');");
		db.execSQL("insert into SongTable17 values(103,	'이지혜',	'천일동안',	'이지혜',	'정보없음',	'47948');");
		db.execSQL("insert into SongTable17 values(104,	'볼륨',	'Now',	'핑클',	'9121',	'6537');");
		db.execSQL("insert into SongTable17 values(105,	'김정환',	'버스 안에서',	'자자',	'3699',	'4879');");
		db.execSQL("insert into SongTable17 values(106,	'김정환',	'버스 안에서',	'김정환',	'36108',	'87412');");
		db.execSQL("insert into SongTable17 values(107,	'허니지',	'비켜줄께',	'브라운 아이즈',	'32385',	'46975');");
		db.execSQL("insert into SongTable17 values(108,	'허니지',	'비켜줄께',	'허니지',	'정보없음',	'58763');");
		db.execSQL("insert into SongTable17 values(109,	'연규성',	'예술이야',	'싸이',	'34174',	'58340');");
		db.execSQL("insert into SongTable17 values(110,	'연규성',	'예술이야',	'연규성',	'정보없음',	'47947');");
		db.execSQL("insert into SongTable17 values(111,	'홍대광',	'노래만 불렀지',	'김장훈',	'3597',	'6087');");
		db.execSQL("insert into SongTable17 values(112,	'홍대광',	'노래만 불렀지',	'홍대광',	'36023',	'47887');");
		db.execSQL("insert into SongTable17 values(113,	'유승우',	'My Son',	'김건모',	'11001',	'9330');");
		db.execSQL("insert into SongTable17 values(114,	'유승우',	'My Son',	'유승우',	'35972',	'47894');");
		db.execSQL("insert into SongTable17 values(115,	'로이킴',	'다시 사랑한다 말할까',	'김동률',	'9691',	'7618');");
		db.execSQL("insert into SongTable17 values(116,	'안예슬',	'전설속의 누군가처럼',	'신승훈',	'8793',	'6269');");
		db.execSQL("insert into SongTable17 values(117,	'정준영',	'매일매일 기다려',	'티삼스',	'2001',	'4361');");
		db.execSQL("insert into SongTable17 values(118,	'정준영',	'매일매일 기다려',	'정준영',	'36093',	'58770');");
		db.execSQL("insert into SongTable17 values(119,	'Top12',	'사랑만들기',	'파파야',	'9532',	'6855');");
		db.execSQL("insert into SongTable17 values(120,	'딕펑스',	'고추잠자리',	'조용필',	'929',	'1645');");
		db.execSQL("insert into SongTable17 values(121,	'딕펑스',	'고추잠자리',	'딕펑스',	'36233',	'58768');");
		db.execSQL("insert into SongTable17 values(122,	'연규성',	'어둠 그 별빛',	'김현식',	'2368',	'3633');");
		db.execSQL("insert into SongTable17 values(123,	'홍대광',	'이미 넌 고마운 사람',	'김연우',	'12668',	'66858');");
		db.execSQL("insert into SongTable17 values(124,	'홍대광',	'이미 넌 고마운 사람',	'홍대광',	'36095',	'87414');");
		db.execSQL("insert into SongTable17 values(125,	'허니지',	'왜 그래',	'김현철',	'2828',	'3988');");
		db.execSQL("insert into SongTable17 values(126,	'허니지',	'왜 그래',	'허니지',	'정보없음',	'58844');");
		db.execSQL("insert into SongTable17 values(127,	'로이킴',	'휘파람',	'이문세',	'2031',	'3294');");
		db.execSQL("insert into SongTable17 values(128,	'로이킴',	'휘파람',	'로이킴',	'36082',	'47897');");
		db.execSQL("insert into SongTable17 values(129,	'안예슬',	'Sk8Er Boi',	'Avril Lavigne',	'20384',	'61128');");
		db.execSQL("insert into SongTable17 values(130,	'정준영',	'Bed of Roses',	'Bon Jovi',	'7338',	'8335');");
		db.execSQL("insert into SongTable17 values(131,	'정준영',	'Bed of Roses',	'정준영',	'정보없음',	'87469');");
		db.execSQL("insert into SongTable17 values(132,	'김정환',	'Love Story',	'비',	'30254',	'83829');");
		db.execSQL("insert into SongTable17 values(133,	'유승우',	'열정',	'세븐',	'13546',	'68392');");
		db.execSQL("insert into SongTable17 values(134,	'유승우',	'열정',	'유승우',	'정보없음',	'47946');");
		db.execSQL("insert into SongTable17 values(135,	'Top9',	'마지막 승부',	'김민교',	'1781',	'3304');");
		db.execSQL("insert into SongTable17 values(136,	'딕펑스',	'같이걸을까 ',	'이적',	'33637',	'58178');");
		db.execSQL("insert into SongTable17 values(137,	'딕펑스',	'같이걸을까 ',	'딕펑스',	'36051',	'58781');");
		db.execSQL("insert into SongTable17 values(138,	'김정환',	'I`ll Be There ',	'김정환',	'정보없음',	'77457');");
		db.execSQL("insert into SongTable17 values(139,	'김정환',	'I`ll Be There ',	'Jackson 5',	'정보없음',	'60620');");
		db.execSQL("insert into SongTable17 values(140,	'홍대광',	'가족 ',	'이승환',	'3678',	'4888');");
		db.execSQL("insert into SongTable17 values(141,	'홍대광',	'가족 ',	'이승환',	'정보없음',	'58775');");
		db.execSQL("insert into SongTable17 values(142,	'허니지',	'오래된친구',	'빛과 소금',	'2065',	'3412');");
		db.execSQL("insert into SongTable17 values(143,	'허니지',	'오래된친구',	'허니지',	'정보없음',	'47913');");
		db.execSQL("insert into SongTable17 values(144,	'정준영',	'그것만이 내 세상',	'들국화',	'1780',	'1329');");
		db.execSQL("insert into SongTable17 values(145,	'정준영',	'그것만이 내 세상',	'정준영',	'정보없음',	'47945');");
		db.execSQL("insert into SongTable17 values(146,	'로이킴',	'청개구리',	'싸이',	'35613',	'58695');");
		db.execSQL("insert into SongTable17 values(147,	'로이킴',	'청개구리',	'로이킴',	'36072',	'77442');");
		db.execSQL("insert into SongTable17 values(148,	'유승우',	'말하는 대로',	'처진 달팽이',	'34139',	'76963');");
		db.execSQL("insert into SongTable17 values(149,	'Top7',	'고백',	'델리스파이스',	'10821',	'9276');");
		db.execSQL("insert into SongTable17 values(150,	'김정환',	'아름다운 강산',	'이선희(신중현)',	'870',	'923');");
		db.execSQL("insert into SongTable17 values(151,	'딕펑스',	'Muzik',	'포미닛',	'31576',	'46771');");
		db.execSQL("insert into SongTable17 values(152,	'딕펑스',	'Muzik',	'딕펑스',	'정보없음',	'77444');");
		db.execSQL("insert into SongTable17 values(153,	'로이킴',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable17 values(154,	'로이킴',	'서울의 달',	'로이킴',	'36119',	'87430');");
		db.execSQL("insert into SongTable17 values(155,	'유승우',	'Butterfly',	'Jason Mraz',	'22179',	'84831');");
		db.execSQL("insert into SongTable17 values(156,	'정준영',	'아웃사이더',	'봄여름가을겨울',	'372',	'1361');");
		db.execSQL("insert into SongTable17 values(157,	'정준영',	'아웃사이더',	'정준영',	'36494',	'77438');");
		db.execSQL("insert into SongTable17 values(158,	'홍대광',	'뜨거운 안녕',	'토이',	'18938',	'46135');");
		db.execSQL("insert into SongTable17 values(159,	'Top6',	'그대에게',	'무한궤도',	'786',	'3555');");
		db.execSQL("insert into SongTable17 values(160,	'정준영/윤하',	'기다리다',	'윤하',	'16677',	'85300');");
		db.execSQL("insert into SongTable17 values(161,	'딕펑스/김예림',	'Poker Face',	'Lady GaGa',	'20528',	'63150');");
		db.execSQL("insert into SongTable17 values(162,	'로이킴/호란',	'Romeo N Juliet',	'클래지콰이',	'18252',	'85500');");
		db.execSQL("insert into SongTable17 values(163,	'홍대광/김지수',	'밤이 깊었네',	'크라잉넛',	'9550',	'6943');");
		db.execSQL("insert into SongTable17 values(164,	'홍대광',	'내 낡은 서랍속의 바다',	'패닉',	'4636',	'5489');");
		db.execSQL("insert into SongTable17 values(165,	'홍대광',	'내 낡은 서랍속의 바다',	'홍대광',	'정보없음',	'87435');");
		db.execSQL("insert into SongTable17 values(166,	'딕펑스',	'연극이 끝난 후',	'샤프',	'3053',	'2243');");
		db.execSQL("insert into SongTable17 values(167,	'딕펑스',	'연극이 끝난 후',	'딕펑스',	'36089',	'87431');");
		db.execSQL("insert into SongTable17 values(168,	'정준영',	'응급실',	'이지(izi)',	'14515',	'45117');");
		db.execSQL("insert into SongTable17 values(169,	'정준영',	'응급실',	'정준영',	'36123',	'47920');");
		db.execSQL("insert into SongTable17 values(170,	'로이킴',	'한동안 뜸했었지',	'사랑과 평화',	'2054',	'2572');");
		db.execSQL("insert into SongTable17 values(171,	'로이킴',	'한동안 뜸했었지',	'로이킴',	'36234',	'87436');");
		db.execSQL("insert into SongTable17 values(172,	'로이킴/정준영',	'Creep',	'Radiohead',	'7740',	'8270');");
		db.execSQL("insert into SongTable17 values(173,	'로이킴/정준영',	'Creep',	'로이킴/정준영',	'정보없음',	'58822');");
		db.execSQL("insert into SongTable17 values(174,	'딕펑스/홍대광',	'아스피린',	'Girl',	'2931',	'4018');");
		db.execSQL("insert into SongTable17 values(175,	'딕펑스/홍대광',	'아스피린',	'딕펑스/홍대광',	'정보없음',	'77512');");
		db.execSQL("insert into SongTable17 values(176,	'로이킴',	'힐링이 필요해',	'윤건',	'35989',	'58790');");
		db.execSQL("insert into SongTable17 values(177,	'로이킴',	'힐링이 필요해',	'로이킴',	'36104',	'58802');");
		db.execSQL("insert into SongTable17 values(178,	'정준영',	'잊었니',	'이승철',	'35017',	'47662');");
		db.execSQL("insert into SongTable17 values(179,	'정준영',	'잊었니',	'정준영',	'36111',	'47935');");
		db.execSQL("insert into SongTable17 values(180,	'딕펑스',	'떠나지마…',	'윤미래',	'30702',	'46548');");
		db.execSQL("insert into SongTable17 values(181,	'딕펑스',	'떠나지마',	'딕펑스',	'36105',	'47927');");
		db.execSQL("insert into SongTable17 values(182,	'정준영',	'첫사랑',	'나비효과',	'10861',	'9287');");
		db.execSQL("insert into SongTable17 values(183,	'딕펑스',	'Big Girl',	'Mika',	'21789',	'60189');");
		db.execSQL("insert into SongTable17 values(184,	'로이킴',	'잊어야 한다는 마음으로',	'김광석',	'2141',	'2787');");
		db.execSQL("insert into SongTable17 values(185,	'쾌남과 옥구슬/박상보',	'돌아와',	'클론',	'8099',	'5822');");
		db.execSQL("insert into SongTable17 values(186,	'조앤/슈퍼스타걸스',	'Just Dance',	'Lady GaGa',	'21938',	'63142');");
		db.execSQL("insert into SongTable17 values(187,	'딕펑스',	'노는 게 남는 거야',	'딕펑스',	'36141',	'58810');");
		db.execSQL("insert into SongTable17 values(188,	'로이킴',	'누구를 위한 삶인가',	'리쌍',	'15928',	'45548');");
		db.execSQL("insert into SongTable17 values(189,	'딕펑스',	'나비',	'딕펑스',	'36140',	'58809');");
		db.execSQL("insert into SongTable17 values(190,	'로이킴',	'스쳐간다',	'로이킴',	'36139',	'47942');");
		db.execSQL("insert into SongTable17 values(191,	'Top12',	'붉은노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable17 values(192,	'Top12',	'아마추어',	'이승철',	'35924',	'47882');");
		db.execSQL("insert into SongTable17 values(193,	'김도현',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable17 values(194,	'김아란',	'When Will My Life Begin',	'Mandy Moore',	'22268',	'84843');");
		db.execSQL("insert into SongTable17 values(195,	'박필규',	'I Decide',	'헤리티지',	'34416',	'58364');");
		db.execSQL("insert into SongTable17 values(196,	'김민석',	'Baby',	'Justin Bieber ',	'22073',	'84915');");
		db.execSQL("insert into SongTable17 values(197,	'이광훈',	'중독',	'나윤권',	'13903',	'9887');");
		db.execSQL("insert into SongTable17 values(198,	'조은혜',	'Hey, Soul Sister',	'Train',	'22072',	'84969');");
		db.execSQL("insert into SongTable17 values(199,	'예리밴드',	'Shut Up And Let Me Go',	'The Ting Tings',	'22277',	'63167');");
		db.execSQL("insert into SongTable17 values(200,	'최준호',	'신부에게',	'유리상자',	'8426',	'6042');");
		db.execSQL("insert into SongTable17 values(201,	'손예림',	'이젠 그랬으면 좋겠네',	'조용필',	'13271',	'86701');");
		db.execSQL("insert into SongTable17 values(202,	'임보람',	'시계태엽',	'임정희',	'15138',	'64838');");
		db.execSQL("insert into SongTable17 values(203,	'김영준',	'내일 해',	'이정',	'11889',	'64064');");
		db.execSQL("insert into SongTable17 values(204,	'조철희',	'헤어지지말자',	'고현욱',	'10994',	'9385');");
		db.execSQL("insert into SongTable17 values(205,	'블랙퀸',	'Dr Feel Good',	'라니아',	'정보없음',	'47355');");
		db.execSQL("insert into SongTable17 values(206,	'박길수',	'애인 있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable17 values(207,	'허미영',	'거부',	'빅마마',	'11298',	'63335');");
		db.execSQL("insert into SongTable17 values(208,	'이성민',	'그땐 미처 알지 못했지',	'이적',	'11369',	'9405');");
		db.execSQL("insert into SongTable17 values(209,	'민훈기',	'그대의 향기',	'유영진',	'2011',	'3804');");
		db.execSQL("insert into SongTable17 values(210,	'윤빛나라',	'난 여자가 있는데',	'박진영',	'9547',	'6881');");
		db.execSQL("insert into SongTable17 values(211,	'신종국',	'청소',	'더 레이(The Ray)',	'16202',	'45642');");
		db.execSQL("insert into SongTable17 values(212,	'울랄라세션',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable17 values(213,	'울랄라세션',	'Kiss Me',	'박진영',	'4965',	'5750');");
		db.execSQL("insert into SongTable17 values(214,	'방희락',	'나쁜 버릇',	'지아(Zia)',	'32391',	'46978');");
		db.execSQL("insert into SongTable17 values(215,	'장세민',	'엄마',	'라디(Ra.D)',	'36202',	'87031');");
		db.execSQL("insert into SongTable17 values(216,	'박장현',	'후회한다',	'4men',	'33315',	'86710');");
		db.execSQL("insert into SongTable17 values(217,	'신지수',	'Rolling In The Deep',	'Adele',	'22213',	'79017');");
		db.execSQL("insert into SongTable17 values(218,	'이건율',	'Imagine',	'John Lennon',	'7267',	'2106');");
		db.execSQL("insert into SongTable17 values(219,	'Ambition',	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable17 values(220,	'전성진',	'하비샴의 왈츠',	'박정현',	'15197',	'69960');");
		db.execSQL("insert into SongTable17 values(221,	'경지애',	'Officially Missing You',	'Tamia',	'22270',	'60151');");
		db.execSQL("insert into SongTable17 values(222,	'유나킴',	'미친거니',	'송지은',	'33712',	'47317');");
		db.execSQL("insert into SongTable17 values(223,	'팻맨',	'Hello!',	'4men',	'33233',	'58075');");
		db.execSQL("insert into SongTable17 values(224,	'팻맨',	'Sunday Morning',	'Marron5',	'21232',	'84928');");
		db.execSQL("insert into SongTable17 values(225,	'최영진',	'그날들',	'김광석',	'4248',	'7054');");
		db.execSQL("insert into SongTable17 values(226,	'투개월',	'Virtual Insanity',	'Jamiroquai',	'20126',	'61994');");
		db.execSQL("insert into SongTable17 values(227,	'투개월',	'Romantico',	'테테',	'34469',	'77017');");
		db.execSQL("insert into SongTable17 values(228,	'투개월',	'Romantico',	'투개월',	'34689',	'87071');");
		db.execSQL("insert into SongTable17 values(229,	'김혜민',	'기억상실',	'거미',	'13902',	'9888');");
		db.execSQL("insert into SongTable17 values(230,	'임예주',	'있잖아',	'아이유',	'31387',	'86220');");
		db.execSQL("insert into SongTable17 values(231,	'유진킴',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable17 values(232,	'칸 아사두즈만',	'사랑아',	'더 원',	'17884',	'81766');");
		db.execSQL("insert into SongTable17 values(233,	'유승엽',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable17 values(234,	'김소영',	'Ring Ding Dong',	'샤이니',	'31754',	'46815');");
		db.execSQL("insert into SongTable17 values(235,	'정명수',	'끝사랑',	'김범수',	'34049',	'47408');");
		db.execSQL("insert into SongTable17 values(236,	'십키로',	'고백(Go Back)',	'다이나믹듀오',	'15388',	'69650');");
		db.execSQL("insert into SongTable17 values(237,	'하오준칭/츄이보 ',	'You Raise Me Up',	'Secret Garden(Westlife remake)',	'21357',	'61922');");
		db.execSQL("insert into SongTable17 values(238,	'하오준칭/츄이보 ',	'One Love',	'Blue',	'20409',	'61141');");
		db.execSQL("insert into SongTable17 values(239,	'하오준칭/츄이보 ',	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable17 values(240,	'강림호, 울랄라세션/크리스',	'Isn`t She Lovely',	'Stevie Wonder',	'20887',	'8512');");
		db.execSQL("insert into SongTable17 values(241,	'강림호',	'그리움을 사랑한 가시나무',	'테이',	'14581',	'68964');");
		db.execSQL("insert into SongTable17 values(242,	'최종수',	'슬픔보다 더 슬픈 이야기',	'김범수',	'30586',	'46531');");
		db.execSQL("insert into SongTable17 values(243,	'크리스',	'Grenade',	'Bruno Mars',	'22170',	'84884');");
		db.execSQL("insert into SongTable17 values(244,	'크리스',	'Hello',	'샤이니',	'33131',	'76696');");
		db.execSQL("insert into SongTable17 values(245,	'손예림',	'슬픈인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable17 values(246,	'이정아/크리스티나/크리스/박솔',	'Somebody to Love',	'Queen',	'20289',	'61150');");
		db.execSQL("insert into SongTable17 values(247,	'박필규/박다영/방희락/경지애/최종수',	'Ma boy',	'씨스타19',	'33886',	'76909');");
		db.execSQL("insert into SongTable17 values(248,	'신지수/이소리/이건율/손예림/박장현',	'Ma boy',	'씨스타19',	'33886',	'76909');");
		db.execSQL("insert into SongTable17 values(249,	'울랄라세션/예리밴드/팻듀오',	'흐린 기억 속의 그대',	'현진영',	'1183',	'1371');");
		db.execSQL("insert into SongTable17 values(250,	'민훈기/김지민/최동원/그레이스/이광훈',	'미워도 다시 한번',	'바이브',	'9855',	'7804');");
		db.execSQL("insert into SongTable17 values(251,	'윤빛나라/김도현/타미/임성현/강림호/황신원',	'날 떠나지마',	'박진영',	'2357',	'3569');");
		db.execSQL("insert into SongTable17 values(252,	'강진아/서동훈/길상준/유지',	'다시 만난 날',	'휘성',	'11871',	'9515');");
		db.execSQL("insert into SongTable17 values(253,	'정준일/조철희/고은선/이등록/김소영/신유정',	'With Coffee',	'브라운 아이즈',	'9599',	'7408');");
		db.execSQL("insert into SongTable17 values(254,	'볼륨/FM/싱어소울',	'왼손잡이',	'패닉',	'3070',	'4147');");
		db.execSQL("insert into SongTable17 values(255,	'최영태/김나연/윤빛나라/최정아/최현아',	'Ugly',	'2NE1',	'34228',	'47459');");
		db.execSQL("insert into SongTable17 values(256,	'버스커 버스커/투개월',	'줄리엣',	'샤이니',	'27224',	'43401');");
		db.execSQL("insert into SongTable17 values(257,	'버스커 버스커/투개월',	'줄리엣',	'버스커 버스커/투개월',	'정보없음',	'77107');");
		db.execSQL("insert into SongTable17 values(258,	'크리스티나/신유정',	'LOVE',	'브라운 아이드 걸스',	'19109',	'85697');");
		db.execSQL("insert into SongTable17 values(259,	'최영태/경지애',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable17 values(260,	'이정아/박필규',	'고백',	'뜨거운 감자',	'32409',	'76499');");
		db.execSQL("insert into SongTable17 values(261,	'타미/박솔',	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable17 values(262,	'길상준/이소리',	'Just A Feeling',	'S.E.S',	'9826',	'7795');");
		db.execSQL("insert into SongTable17 values(263,	'그레이스/강림호',	'좋은 사람',	'김형중',	'9526',	'6863');");
		db.execSQL("insert into SongTable17 values(264,	'유지광/최현아',	'사랑..그 놈',	'바비 킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable17 values(265,	'윤빛나라/윤빛나라',	'Tonight',	'빅뱅',	'33684',	'47312');");
		db.execSQL("insert into SongTable17 values(266,	'김지민/서동훈',	'촛불하나',	'god',	'9247',	'6596');");
		db.execSQL("insert into SongTable17 values(267,	'민훈기/최종수',	'그대 내게 다시',	'변진섭',	'1511',	'1579');");
		db.execSQL("insert into SongTable17 values(268,	'김도현/황신원',	'그땐 그랬지',	'카니발',	'4115',	'5131');");
		db.execSQL("insert into SongTable17 values(269,	'투개월/신지수',	'The Way U Are',	'동방신기',	'13480',	'68334');");
		db.execSQL("insert into SongTable17 values(270,	'손예림/이건율',	'Black & White',	'지나',	'33562',	'47276');");
		db.execSQL("insert into SongTable17 values(271,	'예리밴드/헤이즈',	'이브의 경고',	'박미경',	'2699',	'3854');");
		db.execSQL("insert into SongTable17 values(272,	'헤이즈',	'연애',	'김현철',	'8202',	'5911');");
		db.execSQL("insert into SongTable17 values(273,	'헤이즈',	'연애',	'헤이즈',	'정보없음',	'87096');");
		db.execSQL("insert into SongTable17 values(274,	'민훈기',	'그 아픔까지 사랑한 거야',	'조정현',	'1783',	'181');");
		db.execSQL("insert into SongTable17 values(275,	'민훈기',	'그 아픔까지 사랑한 거야',	'민훈기',	'정보없음',	'87068');");
		db.execSQL("insert into SongTable17 values(276,	'크리스티나',	'가지마 가지마',	'브라운 아이즈',	'19762',	'46333');");
		db.execSQL("insert into SongTable17 values(277,	'크리스티나',	'가지마 가지마',	'크리스티나',	'정보없음',	'87095');");
		db.execSQL("insert into SongTable17 values(278,	'이건율',	'나였으면',	'나윤권',	'13584',	'64428');");
		db.execSQL("insert into SongTable17 values(279,	'이건율',	'나였으면',	'이건율',	'정보없음',	'87089');");
		db.execSQL("insert into SongTable17 values(280,	'김도현',	'나는 나비',	'YB',	'16329',	'85183');");
		db.execSQL("insert into SongTable17 values(281,	'김도현',	'나는 나비',	'김도현',	'정보없음',	'87119');");
		db.execSQL("insert into SongTable17 values(282,	'이정아',	'편지',	'김광진',	'9505',	'7301');");
		db.execSQL("insert into SongTable17 values(283,	'이정아',	'편지',	'이정아',	'정보없음',	'87066');");
		db.execSQL("insert into SongTable17 values(284,	'크리스',	'진심',	'김광진',	'4679',	'5520');");
		db.execSQL("insert into SongTable17 values(285,	'크리스',	'진심',	'크리스',	'정보없음',	'77073');");
		db.execSQL("insert into SongTable17 values(286,	'버스커 버스커',	'동경소녀',	'김광진',	'6069',	'9039');");
		db.execSQL("insert into SongTable17 values(287,	'버스커 버스커',	'동경소녀',	'버스커 버스커',	'34489',	'77048');");
		db.execSQL("insert into SongTable17 values(288,	'신지수',	'나나나',	'유승준',	'4555',	'5460');");
		db.execSQL("insert into SongTable17 values(289,	'신지수',	'나나나',	'신지수',	'정보없음',	'87067');");
		db.execSQL("insert into SongTable17 values(290,	'울랄라세션',	'달의 몰락',	'김현철',	'1843',	'3310');");
		db.execSQL("insert into SongTable17 values(291,	'울랄라세션',	'달의 몰락',	'울랄라세션',	'34491',	'77053');");
		db.execSQL("insert into SongTable17 values(292,	'투개월',	'여우야',	'더클래식(김광진)',	'2847',	'3990');");
		db.execSQL("insert into SongTable17 values(293,	'투개월',	'여우야',	'투개월',	'34490',	'77046');");
		db.execSQL("insert into SongTable17 values(294,	'크리스티나',	'Can You Feel The Love Tonight',	'Elton John',	'7888',	'8043');");
		db.execSQL("insert into SongTable17 values(295,	'이건율',	'Part Time Lover',	'Stevie Wonder',	'7521',	'8164');");
		db.execSQL("insert into SongTable17 values(296,	'이정아',	'Desperado',	'Eagles',	'7344',	'61169');");
		db.execSQL("insert into SongTable17 values(297,	'이정아',	'Desperado',	'이정아',	'정보없음',	'87064');");
		db.execSQL("insert into SongTable17 values(298,	'크리스',	'Faith',	'George Michael',	'7904',	'8420');");
		db.execSQL("insert into SongTable17 values(299,	'김도현',	'My Heart Will Go On',	'Celine Dion',	'7736',	'5492');");
		db.execSQL("insert into SongTable17 values(300,	'신지수',	'If I Were A Boy',	'Beyonce',	'21924',	'61937');");
		db.execSQL("insert into SongTable17 values(301,	'투개월',	'Poker Face',	'Lady GaGa',	'20528',	'63150');");
		db.execSQL("insert into SongTable17 values(302,	'투개월',	'Poker Face',	'투개월',	'34523',	'87043');");
		db.execSQL("insert into SongTable17 values(303,	'버스커 버스커',	'Livin`La Vida Loca',	'Ricky Martin',	'7881',	'6381');");
		db.execSQL("insert into SongTable17 values(304,	'버스커 버스커',	'Livin`La Vida Loca',	'버스커 버스커',	'34558',	'87046');");
		db.execSQL("insert into SongTable17 values(305,	'울랄라세션',	'Open Arms',	'Journey',	'7396',	'8160');");
		db.execSQL("insert into SongTable17 values(306,	'울랄라세션',	'Open Arms',	'울랄라세션',	'34522',	'87047');");
		db.execSQL("insert into SongTable17 values(307,	'Top9',	'I Want To Hold Your Hand',	'Beatles',	'7056',	'8101');");
		db.execSQL("insert into SongTable17 values(308,	'크리스',	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable17 values(309,	'김도현',	'꿈을 꾸다',	'김태우',	'31865',	'84614');");
		db.execSQL("insert into SongTable17 values(310,	'버스커 버스커',	'정류장',	'패닉',	'15522',	'64957');");
		db.execSQL("insert into SongTable17 values(311,	'버스커 버스커',	'정류장',	'버스커 버스커',	'34548',	'47540');");
		db.execSQL("insert into SongTable17 values(312,	'신지수',	'길',	'god',	'9706',	'7632');");
		db.execSQL("insert into SongTable17 values(313,	'울랄라세션',	'미인',	'신중현과 엽전들',	'1555',	'2372');");
		db.execSQL("insert into SongTable17 values(314,	'울랄라세션',	'미인',	'울랄라세션',	'34550',	'47539');");
		db.execSQL("insert into SongTable17 values(315,	'크리스티나',	'개똥벌레',	'신형원',	'327',	'117');");
		db.execSQL("insert into SongTable17 values(316,	'크리스티나',	'개똥벌레',	'크리스티나',	'정보없음',	'87063');");
		db.execSQL("insert into SongTable17 values(317,	'투개월',	'Brown City',	'브라운 아이즈',	'12374',	'65606');");
		db.execSQL("insert into SongTable17 values(318,	'투개월',	'Brown City',	'투개월',	'34549',	'47541');");
		db.execSQL("insert into SongTable17 values(319,	'Top7',	'빛',	'H.O.T',	'4789',	'5619');");
		db.execSQL("insert into SongTable17 values(320,	'울랄라세션',	'나쁜 남자',	'비',	'9891',	'7897');");
		db.execSQL("insert into SongTable17 values(321,	'울랄라세션',	'나쁜 남자',	'울랄라세션',	'정보없음',	'87088');");
		db.execSQL("insert into SongTable17 values(322,	'버스커 버스커',	'어쩌다 마주친 그대',	'송골매',	'1209',	'1220');");
		db.execSQL("insert into SongTable17 values(323,	'버스커 버스커',	'어쩌다 마주친 그대',	'버스커 버스커',	'34591',	'47548');");
		db.execSQL("insert into SongTable17 values(324,	'크리스티나',	'Lonely',	'2NE1',	'33904',	'76914');");
		db.execSQL("insert into SongTable17 values(325,	'크리스티나',	'Lonely',	'크리스티나',	'정보없음',	'47550');");
		db.execSQL("insert into SongTable17 values(326,	'김도현',	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable17 values(327,	'김도현',	'가질 수 없는 너',	'김도현',	'정보없음',	'58424');");
		db.execSQL("insert into SongTable17 values(328,	'투개월',	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable17 values(329,	'투개월',	'달팽이',	'투개월',	'정보없음',	'58414');");
		db.execSQL("insert into SongTable17 values(330,	'Top5',	'풍선',	'다섯손가락',	'2080',	'2567');");
		db.execSQL("insert into SongTable17 values(331,	'투개월',	'니 생각',	'윤종신',	'34669',	'58437');");
		db.execSQL("insert into SongTable17 values(332,	'투개월',	'니 생각',	'투개월',	'정보없음',	'87120');");
		db.execSQL("insert into SongTable17 values(333,	'크리스티나',	'Pay Day',	'윤미래',	'17597',	'81606');");
		db.execSQL("insert into SongTable17 values(334,	'크리스티나',	'Pay Day',	'크리스티나',	'정보없음',	'87087');");
		db.execSQL("insert into SongTable17 values(335,	'버스커 버스커',	'막걸리나',	'윤종신',	'32404',	'57860');");
		db.execSQL("insert into SongTable17 values(336,	'버스커 버스커',	'막걸리나',	'버스커 버스커',	'34600',	'47553');");
		db.execSQL("insert into SongTable17 values(337,	'울랄라세션',	'서쪽 하늘',	'이승철',	'15527',	'45430');");
		db.execSQL("insert into SongTable17 values(338,	'울랄라세션',	'서쪽 하늘',	'울랄라세션',	'34595',	'47552');");
		db.execSQL("insert into SongTable17 values(339,	'버스커 버스커',	'그댄 달라요',	'한예슬',	'13327',	'68254');");
		db.execSQL("insert into SongTable17 values(340,	'버스커 버스커',	'그댄 달라요',	'버스커 버스커',	'34638',	'87069');");
		db.execSQL("insert into SongTable17 values(341,	'Top4',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable17 values(342,	'버스커 버스커',	'Valenti',	'보아',	'10084',	'9099');");
		db.execSQL("insert into SongTable17 values(343,	'투개월',	'예감 좋은 날',	'럼블피쉬',	'13548',	'64395');");
		db.execSQL("insert into SongTable17 values(344,	'최아란',	'흔들려',	'채연',	'31139',	'84250');");
		db.execSQL("insert into SongTable17 values(345,	'울랄라세션',	'Swing Baby',	'박진영',	'9564',	'6924');");
		db.execSQL("insert into SongTable17 values(346,	'울랄라세션',	'Swing Baby',	'울랄라세션',	'34622',	'77097');");
		db.execSQL("insert into SongTable17 values(347,	'슈퍼스타K F4',	'친구의 고백',	'2AM',	'30954',	'46600');");
		db.execSQL("insert into SongTable17 values(348,	'손예림',	'시간이 흐른뒤',	'윤미래',	'9652',	'7574');");
		db.execSQL("insert into SongTable17 values(349,	'버스커 버스커',	'I Believe',	'이수영',	'8541',	'6155');");
		db.execSQL("insert into SongTable17 values(350,	'버스커 버스커',	'I Believe',	'버스커 버스커',	'34648',	'77096');");
		db.execSQL("insert into SongTable17 values(351,	'울랄라세션',	'난 행복해',	'이소라',	'2742',	'3924');");
		db.execSQL("insert into SongTable17 values(352,	'울랄라세션',	'난 행복해',	'울랄라세션',	'35387',	'87261');");
		db.execSQL("insert into SongTable17 values(353,	'허각',	'죽고 싶단 말밖에',	'허각',	'34620',	'77094');");
		db.execSQL("insert into SongTable17 values(354,	'Top11',	'내일이 찾아오면',	'오,장,박',	'1716',	'3081');");
		db.execSQL("insert into SongTable17 values(355,	'버스커 버스커',	'서울사람들',	'버스커 버스커',	'34806',	'47579');");
		db.execSQL("insert into SongTable17 values(356,	'울랄라세션',	'너와 함께',	'울랄라세션',	'34695',	'58465');");
		db.execSQL("insert into SongTable17 values(357,	'김소정',	'바람아 멈추어다오',	'이지연',	'171',	'388');");
		db.execSQL("insert into SongTable17 values(358,	'허각',	'행복한 나를',	'에코',	'4108',	'5168');");
		db.execSQL("insert into SongTable17 values(359,	'허각',	'행복한 나를',	'허각',	'33297',	'47205');");
		db.execSQL("insert into SongTable17 values(360,	'존박',	'10 Minutes',	'이효리',	'11833',	'9504');");
		db.execSQL("insert into SongTable17 values(361,	'김은비',	'사랑밖엔 난 몰라',	'심수봉',	'1486',	'1468');");
		db.execSQL("insert into SongTable17 values(362,	'김지수',	'노란 샤쓰의 사나이',	'한명숙',	'240',	'243');");
		db.execSQL("insert into SongTable17 values(363,	'장재인',	'님과 함께',	'남진',	'411',	'648');");
		db.execSQL("insert into SongTable17 values(364,	'앤드류 넬슨',	'너를 사랑해',	'한동준',	'1512',	'2724');");
		db.execSQL("insert into SongTable17 values(365,	'김그림',	'하숙생',	'최희준',	'628',	'754');");
		db.execSQL("insert into SongTable17 values(366,	'이보람',	'Timeless',	'SG 워너비',	'12638',	'66685');");
		db.execSQL("insert into SongTable17 values(367,	'박보람',	'세월이 가면',	'최호섭',	'817',	'499');");
		db.execSQL("insert into SongTable17 values(368,	'강승윤',	'내 여자라니까',	'이승기',	'13483',	'9854');");
		db.execSQL("insert into SongTable17 values(369,	'Top11',	'세상에 뿌려진 사랑만큼',	'이승환',	'1324',	'1206');");
		db.execSQL("insert into SongTable17 values(370,	'강승윤',	'그녀의 웃음소리 뿐',	'이문세',	'1572',	'2247');");
		db.execSQL("insert into SongTable17 values(371,	'박보람',	'이별 이야기',	'이문세',	'1408',	'3335');");
		db.execSQL("insert into SongTable17 values(372,	'앤드류 넬슨',	'솔로예찬',	'이문세',	'4516',	'5435');");
		db.execSQL("insert into SongTable17 values(373,	'김지수',	'사랑이 지나가면',	'이문세',	'604',	'1190');");
		db.execSQL("insert into SongTable17 values(374,	'허각',	'조조할인',	'이문세',	'3371',	'4787');");
		db.execSQL("insert into SongTable17 values(375,	'김은비',	'알수없는 인생',	'이문세',	'16255',	'85170');");
		db.execSQL("insert into SongTable17 values(376,	'장재인',	'가로수 그늘 아래 서면',	'이문세',	'3',	'1451');");
		db.execSQL("insert into SongTable17 values(377,	'존박',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable17 values(378,	'존박',	'빗속에서',	'존박',	'33248',	'86707');");
		db.execSQL("insert into SongTable17 values(379,	'Top8',	'붉은노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable17 values(380,	'강승윤',	'Black Or White',	'Michael Jackson',	'7401',	'8030');");
		db.execSQL("insert into SongTable17 values(381,	'김지수',	'Ben',	'Michael Jackson',	'7739',	'2436');");
		db.execSQL("insert into SongTable17 values(382,	'장재인',	'The Way You Make Me Feel',	'Michael Jackson',	'21321',	'60739');");
		db.execSQL("insert into SongTable17 values(383,	'허각',	'I`ll Be There',	'Jackson 5',	'정보없음',	'60620');");
		db.execSQL("insert into SongTable17 values(384,	'존박',	'Man In The Mirror',	'Michael Jackson',	'22056',	'60740');");
		db.execSQL("insert into SongTable17 values(385,	'김은비',	'Heal The World',	'Michael Jackson',	'7387',	'2469');");
		db.execSQL("insert into SongTable17 values(386,	'Top6',	'I Want You Back',	'Jackson 5',	'22169',	'60833');");
		db.execSQL("insert into SongTable17 values(387,	'강승윤',	'본능적으로',	'윤종신',	'32649',	'57928');");
		db.execSQL("insert into SongTable17 values(388,	'강승윤',	'본능적으로',	'강승윤',	'정보없음',	'86706');");
		db.execSQL("insert into SongTable17 values(389,	'허각',	'안녕이라고 말하지마',	'이승철',	'471',	'547');");
		db.execSQL("insert into SongTable17 values(390,	'존박',	'잠도 오지 않는 밤에',	'이승철',	'3344',	'4562');");
		db.execSQL("insert into SongTable17 values(391,	'장재인',	'초대',	'엄정화',	'4777',	'5613');");
		db.execSQL("insert into SongTable17 values(392,	'장재인',	'Mercy',	'Duffy',	'22324',	'60163');");
		db.execSQL("insert into SongTable17 values(393,	'강승윤',	'Time Is Running Out',	'Muse',	'20891',	'61468');");
		db.execSQL("insert into SongTable17 values(394,	'허각',	'You Give Love A Bad Name',	'Bon Jovi',	'7790',	'8251');");
		db.execSQL("insert into SongTable17 values(395,	'존박',	'Sunday Morning',	'Marron5',	'21232',	'84928');");
		db.execSQL("insert into SongTable17 values(396,	'Top4',	'Festival',	'엄정화',	'8342',	'5978');");
		db.execSQL("insert into SongTable17 values(397,	'허각',	'하늘을 달리다',	'이적',	'11378',	'63301');");
		db.execSQL("insert into SongTable17 values(398,	'허각',	'하늘을 달리다',	'허각',	'정보없음',	'86723');");
		db.execSQL("insert into SongTable17 values(399,	'존박',	'니가 사는 그집',	'박진영',	'18884',	'46116');");
		db.execSQL("insert into SongTable17 values(400,	'장재인',	'Lemon Tree',	'박혜경',	'19564',	'83564');");
		db.execSQL("insert into SongTable17 values(401,	'김보경',	'Because of You',	'Kelly Clarkson',	'21359',	'61917');");
		db.execSQL("insert into SongTable17 values(402,	'김보경',	'Because of You',	'김보경',	'정보없음',	'86728');");
		db.execSQL("insert into SongTable17 values(403,	'존박',	'취중진담',	'전람회',	'3134',	'4772');");
		db.execSQL("insert into SongTable17 values(404,	'허각',	'사랑비',	'김태우',	'31588',	'46773');");
		db.execSQL("insert into SongTable17 values(405,	'허각',	'언제나',	'허각',	'33256',	'47193');");
		db.execSQL("insert into SongTable17 values(406,	'조문근',	'Love Like This',	'조문근',	'33349',	'58089');");
		db.execSQL("insert into SongTable17 values(407,	'Top11',	'마지막 축제',	'서태지와 아이들',	'1920',	'2799');");
		db.execSQL("insert into SongTable17 values(408,	'이진',	'마주치지 말자',	'장혜진',	'15668',	'45480');");
		db.execSQL("insert into SongTable17 values(409,	'박재은',	'삐에로는 우릴 보고 웃지',	'김완선',	'813',	'431');");
		db.execSQL("insert into SongTable17 values(410,	'박태진',	'누구라도 그러하듯이',	'배인숙',	'727',	'1152');");
		db.execSQL("insert into SongTable17 values(411,	'박나래',	'늪',	'조관우',	'2299',	'3537');");
		db.execSQL("insert into SongTable17 values(412,	'김주왕',	'I Do',	'비',	'14048',	'68570');");
		db.execSQL("insert into SongTable17 values(413,	'정선국',	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable17 values(414,	'서인국',	'아름다운 이별',	'김건모',	'2451',	'3657');");
		db.execSQL("insert into SongTable17 values(415,	'박세미',	'보라빛 향기',	'강수지',	'153',	'410');");
		db.execSQL("insert into SongTable17 values(416,	'길학미',	'경고',	'타샤니',	'8548',	'6194');");
		db.execSQL("insert into SongTable17 values(417,	'조문근',	'단발머리',	'조용필',	'635',	'1461');");
		db.execSQL("insert into SongTable17 values(418,	'조문근',	'Honey, Honey',	'ABBA',	'20001',	'60001');");
		db.execSQL("insert into SongTable17 values(419,	'박세미',	'Thank You For The Music',	'ABBA',	'20129',	'61830');");
		db.execSQL("insert into SongTable17 values(420,	'김주왕',	'Gimme! Gimme! Gimme!',	'ABBA',	'7320',	'8073');");
		db.execSQL("insert into SongTable17 values(421,	'정선국',	'Mamma Mia',	'ABBA',	'20938',	'60134');");
		db.execSQL("insert into SongTable17 values(422,	'서인국',	'Super Trouper',	'ABBA',	'21084',	'60413');");
		db.execSQL("insert into SongTable17 values(423,	'길학미',	'Waterloo',	'ABBA',	'20596',	'8952');");
		db.execSQL("insert into SongTable17 values(424,	'박나래',	'SOS',	'ABBA',	'20734',	'2692');");
		db.execSQL("insert into SongTable17 values(425,	'박태진',	'The Winner Takes It All',	'ABBA',	'20970',	'61678');");
		db.execSQL("insert into SongTable17 values(426,	'길학미',	'Blah Blah',	'길학미',	'정보없음',	'84496');");
		db.execSQL("insert into SongTable17 values(427,	'박태진',	'편지',	'박태진',	'정보없음',	'84499');");
		db.execSQL("insert into SongTable17 values(428,	'서인국',	'Young Love',	'서인국',	'31771',	'46794');");
		db.execSQL("insert into SongTable17 values(429,	'조문근',	'따뜻한 노래',	'조문근',	'31772',	'84494');");
		db.execSQL("insert into SongTable17 values(430,	'조문근',	'희야',	'이승철',	'1824',	'2015');");
		db.execSQL("insert into SongTable17 values(431,	'길학미',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable17 values(432,	'박태진',	'안녕이라고 말하지마',	'이승철',	'471',	'547');");
		db.execSQL("insert into SongTable17 values(433,	'서인국',	'오직 너뿐인 나를',	'이승철',	'8100',	'5842');");
		db.execSQL("insert into SongTable17 values(434,	'조문근',	'Ring My Bell',	'다이나믹듀오',	'13280',	'68194');");
		db.execSQL("insert into SongTable17 values(435,	'서인국',	'미워도 사랑하니까',	'다비치',	'19191',	'46198');");
		db.execSQL("insert into SongTable17 values(436,	'길학미',	'Lover Boy',	'클래지콰이',	'18096',	'81828');");
		db.execSQL("insert into SongTable17 values(437,	'Top3',	'깊은 밤을 날아서',	'이문세',	'1771',	'2259');");
		db.execSQL("insert into SongTable17 values(438,	'조문근',	'Hey Hey Hey',	'자우림',	'4043',	'5127');");
		db.execSQL("insert into SongTable17 values(439,	'서인국',	'나만 바라봐',	'태양',	'19645',	'46306');");
		db.execSQL("insert into SongTable17 values(440,	'서인국',	'부른다',	'서인국',	'31806',	'46828');");
		db.execSQL("insert into SongTable17 values(441,	'Top10',	'한걸음 더',	'윤상',	'1831',	'2173');");
	}				

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable17");
		onCreate(db);
	}
}