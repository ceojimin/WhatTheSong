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

public class WhatTheSong16 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_16);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_16",
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
		cursor = db.rawQuery("SELECT * FROM SongTable16 WHERE song LIKE '"
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

		WhatTheSongDBHelper16 wtsHelper16 = new WhatTheSongDBHelper16(this);
		db = wtsHelper16.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable16 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable16 where id= '"
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
		new AlertDialog.Builder(WhatTheSong16.this)
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
											WhatTheSong16.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong16.this,
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
			Intent newActivity = new Intent(WhatTheSong16.this,
					WhatTheSongBookmark.class);
			WhatTheSong16.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
		}else if (v.getId() == R.id.search) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
			String edit = SearchText.getText().toString();
			cursor = db.rawQuery("SELECT * FROM SongTable16 WHERE song LIKE '"
					+ edit + "%' OR song LIKE '%" + edit +"%' OR song LIKE '%" + edit + "' OR singer LIKE '"
					+ edit + "%' OR singer LIKE '%" + edit +"%' OR singer LIKE '%" + edit + "' OR participant LIKE '"
					+ edit + "%' OR participant LIKE '%" + edit +"%' OR participant LIKE '%" + edit + "'", null);
			if (cursor.getCount() == 0) {
				Toast.makeText(WhatTheSong16.this, "검색 결과가 없습니다.",
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
					"m_pref_16", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper16 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper16(Context context) {
		super(context, "whatthesong16.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {    // 'kpop 스타 테마' DB (130627 : 134개)
		db.execSQL("CREATE TABLE SongTable16 ( id INTEGER ," + " participant TEXT ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable16 values(1,	'YouU,앤드류 최',	'Only One',	'보아',	'35642',	'47820');");
		db.execSQL("insert into SongTable16 values(2,	'YouU',	'Ugly',	'2NE1',	'34228',	'47459');");
		db.execSQL("insert into SongTable16 values(3,	'YouU,이천원,최예근',	'난 여자가 있는데',	'박진영',	'9547',	'6881');");
		db.execSQL("insert into SongTable16 values(4,	'라쿤보이즈',	'So Hot',	'원더걸스',	'19644',	'46305');");
		db.execSQL("insert into SongTable16 values(5,	'라쿤보이즈',	'Now',	'핑클',	'9121',	'6537');");
		db.execSQL("insert into SongTable16 values(6,	'라쿤보이즈',	'Love On Top',	'Beyonce',	'22340',	'79016');");
		db.execSQL("insert into SongTable16 values(7,	'라쿤보이즈',	'I`ll Be Missing You',	'Puff Daddy',	'7751',	'8276');");
		db.execSQL("insert into SongTable16 values(8,	'라쿤보이즈',	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable16 values(9,	'라쿤보이즈',	'Like This ',	'원더걸스',	'35442',	'77282');");
		db.execSQL("insert into SongTable16 values(10,	'라쿤보이즈',	'Thriller',	'Michael Jackson ',	'7452',	'8219');");
		db.execSQL("insert into SongTable16 values(11,	'방예담,악동뮤지션',	'Officially Missing You',	'Tamia',	'22270',	'60151');");
		db.execSQL("insert into SongTable16 values(12,	'방예담',	'Karma Chameleon',	'Culture Club',	'20653',	'8119');");
		db.execSQL("insert into SongTable16 values(13,	'방예담',	'Where Is The Love?',	'Black Eyed Peas',	'20850',	'61728');");
		db.execSQL("insert into SongTable16 values(14,	'방예담,성수진',	'너뿐이야',	'박진영',	'35314',	'77254');");
		db.execSQL("insert into SongTable16 values(15,	'방예담',	'I Do',	'비',	'14048',	'68570');");
		db.execSQL("insert into SongTable16 values(16,	'방예담',	'Sir Duke',	'Stevie Wonder ',	'22420',	'2419');");
		db.execSQL("insert into SongTable16 values(17,	'방예담',	'Black Or White',	'Michael Jackson ',	'7401',	'8030');");
		db.execSQL("insert into SongTable16 values(18,	'방예담,앤드류 최',	'Baby',	'Justin Bieber',	'22073',	'84915');");
		db.execSQL("insert into SongTable16 values(19,	'방예담,악동뮤지션',	'Mmmbop',	'Hanson ',	'7752',	'61538');");
		db.execSQL("insert into SongTable16 values(20,	'방예담/악동뮤지션',	'I Want You Back',	'Jackson 5',	'22169',	'60833');");
		db.execSQL("insert into SongTable16 values(21,	'성수진',	'So Sick',	'Ne-Yo ',	'21403',	'61960');");
		db.execSQL("insert into SongTable16 values(22,	'성수진',	'초대',	'엄정화',	'4777',	'5613');");
		db.execSQL("insert into SongTable16 values(23,	'신지훈',	'You Raise Me Up',	'Westlife',	'21357',	'61922');");
		db.execSQL("insert into SongTable16 values(24,	'신지훈',	'편지',	'김광진',	'9505',	'7301');");
		db.execSQL("insert into SongTable16 values(25,	'신지훈',	'You Are Not Alone',	'Michael Jackson ',	'7829',	'8784');");
		db.execSQL("insert into SongTable16 values(26,	'신지훈',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable16 values(27,	'신지훈',	'너에게로 또 다시',	'변진섭',	'725',	'239');");
		db.execSQL("insert into SongTable16 values(28,	'신지훈/임경하',	'I`ll Be There',	'Jackson 5',	'정보없음',	'60620');");
		db.execSQL("insert into SongTable16 values(29,	'신지훈',	'Ben ',	'Michael Jackson ',	'7739',	'2436');");
		db.execSQL("insert into SongTable16 values(30,	'신지훈',	'Someone Like You',	'Adele',	'22204',	'79035');");
		db.execSQL("insert into SongTable16 values(31,	'신지훈',	'Toxic',	'Britney Spears',	'20947',	'61323');");
		db.execSQL("insert into SongTable16 values(32,	'악동뮤지션',	'뜨거운 안녕',	'토이',	'18938',	'46135');");
		db.execSQL("insert into SongTable16 values(33,	'악동뮤지션',	'외국인의 고백',	'악동뮤지션',	'36644',	'77594');");
		db.execSQL("insert into SongTable16 values(34,	'악동뮤지션',	'사랑은 은하수 다방에서',	'악동뮤지션',	'36599',	'58943');");
		db.execSQL("insert into SongTable16 values(35,	'악동뮤지션',	'사랑은 은하수 다방에서',	'10cm',	'33665',	'86807');");
		db.execSQL("insert into SongTable16 values(36,	'악동뮤지션',	'Crescendo',	'악동뮤지션',	'36542',	'48035');");
		db.execSQL("insert into SongTable16 values(37,	'악동뮤지션',	'Ring Ding Dong',	'샤이니',	'31754',	'46815');");
		db.execSQL("insert into SongTable16 values(38,	'악동뮤지션',	'매력있어',	'악동뮤지션',	'36208',	'47970');");
		db.execSQL("insert into SongTable16 values(39,	'악동뮤지션',	'다리꼬지마',	'악동뮤지션',	'36127',	'58811');");
		db.execSQL("insert into SongTable16 values(40,	'악동뮤지션',	'One Of A Kind',	'G-dragon',	'35857',	'87382');");
		db.execSQL("insert into SongTable16 values(41,	'악동뮤지션',	'라면인건가',	'악동뮤지션',	'36454',	'48019');");
		db.execSQL("insert into SongTable16 values(42,	'앤드류 최',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable16 values(43,	'앤드류 최',	'잠 못 드는 밤 비는 내리고',	'김건모',	'1117',	'1509');");
		db.execSQL("insert into SongTable16 values(44,	'앤드류 최',	'죽겠네',	'10cm',	'33659',	'47245');");
		db.execSQL("insert into SongTable16 values(45,	'앤드류 최',	'Dj Got Us Fallin` In Love',	'Usher',	'22124',	'84992');");
		db.execSQL("insert into SongTable16 values(46,	'앤드류 최',	'그xx',	'앤드류 최',	'36462',	'정보없음');");
		db.execSQL("insert into SongTable16 values(47,	'앤드류 최',	'그xx',	'G-Dragon',	'35792',	'47851');");
		db.execSQL("insert into SongTable16 values(48,	'앤드류 최',	'오직 너뿐인 나를',	'이승철',	'8100',	'5842');");
		db.execSQL("insert into SongTable16 values(49,	'앤드류 최',	'Lately',	'Stevie Wonder',	'7960',	'61149');");
		db.execSQL("insert into SongTable16 values(50,	'앤드류 최',	'My Everything',	'브라운 아이드',	'11978',	'64068');");
		db.execSQL("insert into SongTable16 values(51,	'앤드류 최',	'Sunday Morning',	'Marron5',	'21232',	'84928');");
		db.execSQL("insert into SongTable16 values(52,	'이진우',	'슬픔활용법',	'김범수',	'30050',	'46390');");
		db.execSQL("insert into SongTable16 values(53,	'소울퀸',	'Ain`t No Sunshine',	'Bill Withers',	'20793',	'8305');");
		db.execSQL("insert into SongTable16 values(54,	'이진우',	'Inside Your Heaven',	'Carrie Underwood',	'21320',	'정보없음');");
		db.execSQL("insert into SongTable16 values(55,	'이천원',	'백만송이 장미',	'심수봉',	'8093',	'5273');");
		db.execSQL("insert into SongTable16 values(56,	'이천원',	'Lonely',	'2NE1',	'33904',	'76914');");
		db.execSQL("insert into SongTable16 values(57,	'이천원',	'개구장이',	'산울림',	'2600',	'1642');");
		db.execSQL("insert into SongTable16 values(58,	'이천원',	'나혼자',	'씨스타',	'35232',	'77243');");
		db.execSQL("insert into SongTable16 values(59,	'이천원',	'Wild Wild West',	'Will Smith ',	'정보없음',	'60640');");
		db.execSQL("insert into SongTable16 values(60,	'이천원',	'Let`s Get It Started',	'Black Eyed Peas',	'21332',	'61944');");
		db.execSQL("insert into SongTable16 values(61,	'이천원',	'Because Of You(Just The Two Of Us)',	'Bill Withers',	'7907',	'60177');");
		db.execSQL("insert into SongTable16 values(62,	'이천원',	'엘리베이터',	'박진영',	'2781',	'3894');");
		db.execSQL("insert into SongTable16 values(63,	'최예근',	'챔피언',	'싸이',	'10062',	'9061');");
		db.execSQL("insert into SongTable16 values(64,	'최예근',	'Price Tag',	'Jessie J',	'22199',	'84892');");
		db.execSQL("insert into SongTable16 values(65,	'최예근',	'Girls On Top',	'보아',	'14984',	'45256');");
		db.execSQL("insert into SongTable16 values(66,	'최예근',	'Rolling In The Deep',	'Adele',	'22213',	'79017');");
		db.execSQL("insert into SongTable16 values(67,	'최예근',	'Fire',	'2NE1',	'31146',	'84248');");
		db.execSQL("insert into SongTable16 values(68,	'박지민',	'I Believe I Can Fly',	'R.Kelly',	'7763',	'61152');");
		db.execSQL("insert into SongTable16 values(69,	'박지민',	'Irreplaceable',	'Beyonce',	'21667',	'60323');");
		db.execSQL("insert into SongTable16 values(70,	'박지민',	'Over The Rainbow',	'박지민',	'정보없음',	'58583');");
		db.execSQL("insert into SongTable16 values(71,	'박지민',	'Over The Rainbow',	'Judy Garland',	'20296',	'60605');");
		db.execSQL("insert into SongTable16 values(72,	'박지민',	'I`ll Be There',	'Jackson 5',	'정보없음',	'60620');");
		db.execSQL("insert into SongTable16 values(73,	'박지민',	'Love On Top',	'Beyonce',	'22340',	'79016');");
		db.execSQL("insert into SongTable16 values(74,	'박지민',	'거위의 꿈',	'카니발',	'4241',	'5186');");
		db.execSQL("insert into SongTable16 values(75,	'이하이,박지민',	'Mercy',	'Duffy',	'22324',	'60163');");
		db.execSQL("insert into SongTable16 values(76,	'수펄스',	'The Boys',	'소녀시대',	'34551',	'47544');");
		db.execSQL("insert into SongTable16 values(77,	'박지민',	'Music Is My Life',	'임정희',	'14915',	'45220');");
		db.execSQL("insert into SongTable16 values(78,	'박지민',	'I Have Nothing',	'Whitney Houston',	'7627',	'8866');");
		db.execSQL("insert into SongTable16 values(79,	'박지민',	'Hey Hey Hey',	'자우림',	'4043',	'5127');");
		db.execSQL("insert into SongTable16 values(80,	'이하이',	'Bust Your Windows',	'Jazmine Sullivan',	'정보없음',	'79036');");
		db.execSQL("insert into SongTable16 values(81,	'이하이',	'미련한 사랑',	'JK김동욱',	'9952',	'7920');");
		db.execSQL("insert into SongTable16 values(82,	'이하이',	'Sway',	'Pussycat Dolls',	'21263',	'61736');");
		db.execSQL("insert into SongTable16 values(83,	'이하이',	'Killing Me Softly With His Song',	'Roberta Flack',	'7186',	'8121');");
		db.execSQL("insert into SongTable16 values(84,	'박지민,이하이',	'Rolling In The Deep',	'Adele',	'22213',	'79017');");
		db.execSQL("insert into SongTable16 values(85,	'이하이',	'U-Go-Girl',	'이효리',	'19854',	'83680');");
		db.execSQL("insert into SongTable16 values(86,	'이하이',	'시간이 흐른뒤',	'윤미래',	'9652',	'7574');");
		db.execSQL("insert into SongTable16 values(87,	'이하이',	'Love',	'Keyshia Cole',	'21776',	'60280');");
		db.execSQL("insert into SongTable16 values(88,	'이하이',	'Sweet Love',	'Anita Baker',	'정보없음',	'79088');");
		db.execSQL("insert into SongTable16 values(89,	'이하이',	'미련',	'김건모',	'3136',	'4168');");
		db.execSQL("insert into SongTable16 values(90,	'백아연',	'Pokerface',	'Lady GaGa',	'20528',	'63150');");
		db.execSQL("insert into SongTable16 values(91,	'백아연',	'Can`t Fight The Moonlight',	'Leann Rimes',	'7667',	'8970');");
		db.execSQL("insert into SongTable16 values(92,	'백아연',	'Saving All My Love For You',	'Whitney Houston ',	'20570',	'2413');");
		db.execSQL("insert into SongTable16 values(93,	'백아연',	'Lady Marmalade',	'Christina Aguilera',	'20038',	'60491');");
		db.execSQL("insert into SongTable16 values(94,	'백아연',	'잘못했어',	'2AM',	'32352',	'46960');");
		db.execSQL("insert into SongTable16 values(95,	'백아연',	'보고싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable16 values(96,	'백아연',	' Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable16 values(97,	'백아연',	'하루하루',	'빅뱅',	'19974',	'83751');");
		db.execSQL("insert into SongTable16 values(98,	'백아연',	'아시나요',	'조성모',	'9054',	'6512');");
		db.execSQL("insert into SongTable16 values(99,	'백아연',	'언젠가는',	'이상은',	'1406',	'1812');");
		db.execSQL("insert into SongTable16 values(100,	'이승훈',	'아버지',	'다이나믹듀오',	'30080',	'83774');");
		db.execSQL("insert into SongTable16 values(101,	'이승훈',	'어머니의 된장국',	'다이나믹듀오',	'30235',	'85931');");
		db.execSQL("insert into SongTable16 values(102,	'이승훈',	'챔피언',	'싸이',	'10062',	'9061');");
		db.execSQL("insert into SongTable16 values(103,	'이승훈',	'내가 노래를 못해도',	'세븐',	'34957',	'47642');");
		db.execSQL("insert into SongTable16 values(104,	'이승훈',	'내가 제일 잘 나가',	'2NE1',	'34084',	'47425');");
		db.execSQL("insert into SongTable16 values(105,	'이승훈',	'단발머리',	'조용필',	'635',	'1461');");
		db.execSQL("insert into SongTable16 values(106,	'이승훈',	'난 알아요',	'서태지와 아이들',	'1294',	'1296');");
		db.execSQL("insert into SongTable16 values(107,	'이미쉘',	'Chain of Fools',	'Aretha Franklin',	'20987',	'8826');");
		db.execSQL("insert into SongTable16 values(108,	'이미쉘',	'One Night Only',	'Jennifer Hudson',	'21814',	'60183');");
		db.execSQL("insert into SongTable16 values(109,	'이미쉘',	'사진을 보다가',	'바이브',	'12426',	'66586');");
		db.execSQL("insert into SongTable16 values(110,	'이미쉘',	'U Just',	'소울사이어티(박정은)',	'16549',	'86102');");
		db.execSQL("insert into SongTable16 values(111,	'이미쉘',	'그대의 향기',	'유영진',	'2011',	'3804');");
		db.execSQL("insert into SongTable16 values(112,	'이미쉘',	'Run to You',	'Whitney Houston ',	'7717',	'61042');");
		db.execSQL("insert into SongTable16 values(113,	'이미쉘',	'이별여행',	'원미연',	'391',	'634');");
		db.execSQL("insert into SongTable16 values(114,	'박제형',	'That Thing You Do',	'The Wonders ',	'20110',	'정보없음');");
		db.execSQL("insert into SongTable16 values(115,	'박제형',	'Can`t Take My Eyes Off You',	'Morten Harket',	'7737',	'5509');");
		db.execSQL("insert into SongTable16 values(116,	'박제형',	'This Love',	'Marron5',	'21040',	'61394');");
		db.execSQL("insert into SongTable16 values(117,	'박제형',	'Just The Way You Are',	'Bruno Mars',	'22134',	'84854');");
		db.execSQL("insert into SongTable16 values(118,	'박제형',	'Toxic',	'Britney Spears',	'20947',	'61323');");
		db.execSQL("insert into SongTable16 values(119,	'박제형',	'한 여름 밤의 꿈',	'권성연',	'1052',	'3501');");
		db.execSQL("insert into SongTable16 values(120,	'윤현상',	'그대 내 품에',	'유재하',	'12766',	'정보없음');");
		db.execSQL("insert into SongTable16 values(121,	'윤현상',	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable16 values(122,	'윤현상',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable16 values(123,	'윤현상',	'휘파람',	'이문세',	'2031',	'3294');");
		db.execSQL("insert into SongTable16 values(124,	'윤현상',	'1994년 어느 늦은 밤',	'장혜진',	'2432',	'3641');");
		db.execSQL("insert into SongTable16 values(125,	'백지웅',	'기억의 습작',	'김동률',	'14653',	'69504');");
		db.execSQL("insert into SongTable16 values(126,	'백지웅',	'사랑해요',	'김조한',	'10086',	'9101');");
		db.execSQL("insert into SongTable16 values(127,	'백지웅',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable16 values(128,	'백지웅',	'입영열차 안에서',	'김민우',	'122',	'651');");
		db.execSQL("insert into SongTable16 values(129,	'김나윤',	'Fallin`',	'Alicia Keys',	'20069',	'61072');");
		db.execSQL("insert into SongTable16 values(130,	'김나윤',	'Hit The Road Jack',	'Ray Charles ',	'정보없음',	'8458');");
		db.execSQL("insert into SongTable16 values(131,	'김나윤',	'Set Fire To The Rain',	'Adele',	'22299',	'79062');");
		db.execSQL("insert into SongTable16 values(132,	'김나윤',	'나는 문제없어',	'황규영',	'1983',	'3346');");
		db.execSQL("insert into SongTable16 values(133,	'이정미',	'나 돌아가',	'임정희(박진영)',	'19252',	'85061');");
		db.execSQL("insert into SongTable16 values(134,	'이정미',	'달팽이',	'패닉',	'2881',	'4028');");
	}				

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable16");
		onCreate(db);
	}
}