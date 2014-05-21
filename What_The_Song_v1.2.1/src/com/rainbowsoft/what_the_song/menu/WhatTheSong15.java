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

public class WhatTheSong15 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_15);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_15",
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
		cursor = db.rawQuery("SELECT * FROM SongTable15 WHERE song LIKE '"
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

		WhatTheSongDBHelper15 wtsHelper15 = new WhatTheSongDBHelper15(this);
		db = wtsHelper15.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable15 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable15 where id= '"
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
		new AlertDialog.Builder(WhatTheSong15.this)
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
											WhatTheSong15.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong15.this,
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
			Intent newActivity = new Intent(WhatTheSong15.this,
					WhatTheSongBookmark.class);
			WhatTheSong15.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
		}else if (v.getId() == R.id.search) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
			String edit = SearchText.getText().toString();
			cursor = db.rawQuery("SELECT * FROM SongTable15 WHERE song LIKE '"
					+ edit + "%' OR song LIKE '%" + edit +"%' OR song LIKE '%" + edit + "' OR singer LIKE '"
					+ edit + "%' OR singer LIKE '%" + edit +"%' OR singer LIKE '%" + edit + "' OR participant LIKE '"
					+ edit + "%' OR participant LIKE '%" + edit +"%' OR participant LIKE '%" + edit + "'", null);
			if (cursor.getCount() == 0) {
				Toast.makeText(WhatTheSong15.this, "검색 결과가 없습니다.",
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
					"m_pref_15", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper15 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper15(Context context) {
		super(context, "whatthesong15.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '위대한 탄생 테마' DB (130627 : 679개)
		db.execSQL("CREATE TABLE SongTable15 ( id INTEGER ," + " participant TEXT ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable15 values(1,	'G7',	'새들처럼',	'변진섭',	'151',	'912');");
		db.execSQL("insert into SongTable15 values(2,	'Top 12',	'Show',	'김원준',	'3272',	'4737');");
		db.execSQL("insert into SongTable15 values(3,	'Top 12, 김정인, 김태원',	'Over The Rainbow',	'Judy Garland',	'20296',	'60605');");
		db.execSQL("insert into SongTable15 values(4,	'강병진',	'청소',	'더 레이(The Ray)',	'16202',	'45642');");
		db.execSQL("insert into SongTable15 values(5,	'권리세',	'Hey Hey Hey',	'자우림',	'4043',	'5127');");
		db.execSQL("insert into SongTable15 values(6,	'권리세',	'Like A Virgin',	'Madonna',	'7035',	'3273');");
		db.execSQL("insert into SongTable15 values(7,	'권리세',	'기다리다',	'윤하',	'16677',	'85300');");
		db.execSQL("insert into SongTable15 values(8,	'권리세',	'나 혼자서',	'티파니',	'30980',	'84193');");
		db.execSQL("insert into SongTable15 values(9,	'권리세',	'No.1',	'보아',	'9858',	'7868');");
		db.execSQL("insert into SongTable15 values(10,	'권리세',	'애인있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable15 values(11,	'권리세',	'마법의 성',	'더 클래식',	'2238',	'3514');");
		db.execSQL("insert into SongTable15 values(12,	'권리세, 노지훈',	'U-Go-Girl (With 낯선)',	'이효리',	'19854',	'83680');");
		db.execSQL("insert into SongTable15 values(13,	'권리세, 린라다',	'Bad Girl Good Girl',	'miss A',	'32773',	'47073');");
		db.execSQL("insert into SongTable15 values(14,	'권영기',	'잔소리',	'김건모',	'13871',	'64481');");
		db.execSQL("insert into SongTable15 values(15,	'권영기',	'땡벌',	'강진',	'10136',	'7463');");
		db.execSQL("insert into SongTable15 values(16,	'김도엽',	'사랑해도 괜찮니',	'포맨',	'30629',	'84037');");
		db.execSQL("insert into SongTable15 values(17,	'김도엽, 정희주',	'Billie Jean',	'Michael Jackson',	'7013',	'8027');");
		db.execSQL("insert into SongTable15 values(18,	'김우현',	'I Will Be Back',	'2PM',	'33171',	'58053');");
		db.execSQL("insert into SongTable15 values(19,	'김정인',	'You Raise Me Up',	'Westlife',	'21357',	'61922');");
		db.execSQL("insert into SongTable15 values(20,	'김정인',	'One Summer Night',	'진추하, 아비',	'7117',	'2672');");
		db.execSQL("insert into SongTable15 values(21,	'김정인',	'아이처럼',	'김동률',	'19166',	'83375');");
		db.execSQL("insert into SongTable15 values(22,	'김정인',	'If I Leave (나 가거든)',	'조수미',	'9744',	'7679');");
		db.execSQL("insert into SongTable15 values(23,	'김정인, 이유나',	'Dancing Queen',	'ABBA',	'7166',	'8048');");
		db.execSQL("insert into SongTable15 values(24,	'김한준',	'Geek In The Pink',	'Jason Mraz',	'21829',	'60155');");
		db.execSQL("insert into SongTable15 values(25,	'김한준',	'죽겠네',	'10cm',	'33659',	'47245');");
		db.execSQL("insert into SongTable15 values(26,	'김한준',	'You And Me',	'자우림',	'16536',	'45729');");
		db.execSQL("insert into SongTable15 values(27,	'김한준',	'제발',	'이소라',	'9276',	'6665');");
		db.execSQL("insert into SongTable15 values(28,	'김한준, 백새은',	'붉은 노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable15 values(29,	'김혜리',	'2 Different Tears',	'원더걸스',	'32605',	'47023');");
		db.execSQL("insert into SongTable15 values(30,	'김혜리',	'너에게로 또 다시',	'변진섭',	'725',	'239');");
		db.execSQL("insert into SongTable15 values(31,	'김혜리',	'Open Arms',	'Journey',	'7396',	'8160');");
		db.execSQL("insert into SongTable15 values(32,	'김혜리',	'독설.. 이 지독한 사랑',	'니모',	'18886',	'85645');");
		db.execSQL("insert into SongTable15 values(33,	'김혜리',	'우리 사랑 여기까지죠',	'혜령',	'14633',	'69159');");
		db.execSQL("insert into SongTable15 values(34,	'김혜리',	'애인있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable15 values(35,	'김혜리',	'너를 위해 (동감 O.S.T)',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable15 values(36,	'김혜리, 이태권',	'그 남자 그 여자 (Feat.장혜진)',	'바이브',	'15757',	'45492');");
		db.execSQL("insert into SongTable15 values(37,	'김휘',	'전활 받지 않는 너에게',	'2am',	'33225',	'58062');");
		db.execSQL("insert into SongTable15 values(38,	'김휘',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(39,	'나탈리 화이트',	'Can＇t You See',	'손담비',	'32779',	'57971');");
		db.execSQL("insert into SongTable15 values(40,	'노지훈',	'와줘',	'세븐',	'10988',	'9318');");
		db.execSQL("insert into SongTable15 values(41,	'노지훈',	'이브의 경고',	'박미경',	'2699',	'3854');");
		db.execSQL("insert into SongTable15 values(42,	'노지훈',	'Goodbye',	'Air Supply',	'7788',	'60448');");
		db.execSQL("insert into SongTable15 values(43,	'노지훈',	'Love Story',	'비',	'30254',	'83829');");
		db.execSQL("insert into SongTable15 values(44,	'노지훈',	'죽어도 못 보내',	'2AM',	'32138',	'46919');");
		db.execSQL("insert into SongTable15 values(45,	'노지훈',	'Hug',	'동방신기',	'12619',	'9645');");
		db.execSQL("insert into SongTable15 values(46,	'노지훈, 황지환, miss A',	'Bad Girl Good Girl',	'miss A',	'32773',	'47073');");
		db.execSQL("insert into SongTable15 values(47,	'데이비드 오',	'No.1',	'보아',	'9858',	'7868');");
		db.execSQL("insert into SongTable15 values(48,	'데이비드 오',	'여행을 떠나요',	'조용필',	'2056',	'3493');");
		db.execSQL("insert into SongTable15 values(49,	'데이비드 오',	'연극이 끝난 후',	'샤프',	'3053',	'2243');");
		db.execSQL("insert into SongTable15 values(50,	'데이비드 오',	'내게 남은 사랑을 드릴게요',	'장혜리',	'62',	'842');");
		db.execSQL("insert into SongTable15 values(51,	'데이비드 오',	'Beat It',	'Michael Jackson',	'7437',	'2509');");
		db.execSQL("insert into SongTable15 values(52,	'데이비드 오',	'You & I',	'박봄',	'31811',	'46829');");
		db.execSQL("insert into SongTable15 values(53,	'데이비드 오',	'너 그럴 때면',	'이브',	'4715',	'5580');");
		db.execSQL("insert into SongTable15 values(54,	'데이비드 오',	'Sunday Morning',	'Maroon 5',	'21232',	'84928');");
		db.execSQL("insert into SongTable15 values(55,	'데이비드 오',	'Down (Feat.Lil Wayne)',	'Jay Sean',	'22391',	'84856');");
		db.execSQL("insert into SongTable15 values(56,	'데이비드 오',	'나만 바라봐',	'태양',	'19645',	'46306');");
		db.execSQL("insert into SongTable15 values(57,	'데이비드 오, 백새은, 손진영, 양정모, 이태권, 정희주, 조형우',	'붉은 노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable15 values(58,	'데이비드 오, 조형우',	'샤이보이 (Shy Boy)',	'시크릿',	'33509',	'58142');");
		db.execSQL("insert into SongTable15 values(59,	'데이비드 오, 조형우',	'I＇m Yours',	'Jason Mraz',	'21945',	'60993');");
		db.execSQL("insert into SongTable15 values(60,	'데이비드 오, 조형우',	'I Don＇t Care',	'2NE1',	'31348',	'46712');");
		db.execSQL("insert into SongTable15 values(61,	'리진펑',	'까만안경',	'이루',	'16468',	'45717');");
		db.execSQL("insert into SongTable15 values(62,	'린라다',	'꺼져줄게 잘 살아',	'지나',	'32828',	'57982');");
		db.execSQL("insert into SongTable15 values(63,	'린라다',	'Breathe',	'miss A',	'33108',	'47155');");
		db.execSQL("insert into SongTable15 values(64,	'맹세창',	'이 노래',	'2AM',	'19848',	'83678');");
		db.execSQL("insert into SongTable15 values(65,	'메간 리',	'시계태엽',	'임정희',	'15138',	'64838');");
		db.execSQL("insert into SongTable15 values(66,	'메간 리, 이미소',	'I Don＇t Care',	'2NE1',	'31348',	'46712');");
		db.execSQL("insert into SongTable15 values(67,	'믹키',	'Again & Again',	'2PM',	'31059',	'46637');");
		db.execSQL("insert into SongTable15 values(68,	'믹키',	'One Moment In Time',	'Whitney Houston',	'정보없음',	'60655');");
		db.execSQL("insert into SongTable15 values(69,	'박소담',	'세월이 가면',	'최호섭',	'817',	'499');");
		db.execSQL("insert into SongTable15 values(70,	'박원미',	'Woman In Love',	'Barbra Streisand',	'7114',	'2514');");
		db.execSQL("insert into SongTable15 values(71,	'박원미',	'봄 여름 가을 겨울',	'김현식',	'4199',	'4396');");
		db.execSQL("insert into SongTable15 values(72,	'박원미',	'1994년 어느 늦은 밤',	'장혜진',	'2432',	'3641');");
		db.execSQL("insert into SongTable15 values(73,	'박원미, 안아리',	'거부',	'빅마마',	'11298',	'63335');");
		db.execSQL("insert into SongTable15 values(74,	'박채린',	'BEN',	'Michael Jackson',	'7739',	'2436');");
		db.execSQL("insert into SongTable15 values(75,	'백새은',	'Beautiful',	'Christina Aguilera',	'20448',	'61161');");
		db.execSQL("insert into SongTable15 values(76,	'백새은',	'나는 나',	'주주클럽',	'3673',	'4892');");
		db.execSQL("insert into SongTable15 values(77,	'백새은',	'팬이야',	'자우림',	'9977',	'9063');");
		db.execSQL("insert into SongTable15 values(78,	'백새은',	'You＇ve Got A Friend',	'James Taylor',	'20103',	'정보없음');");
		db.execSQL("insert into SongTable15 values(79,	'백새은',	'Dont Speak',	'No Doubt',	'7753',	'61283');");
		db.execSQL("insert into SongTable15 values(80,	'백새은',	'Nothing Better',	'정엽',	'33010',	'86615');");
		db.execSQL("insert into SongTable15 values(81,	'백새은',	'Something Good',	'자우림',	'30149',	'85883');");
		db.execSQL("insert into SongTable15 values(82,	'백청강',	'미지의 세계',	'조용필',	'837',	'1465');");
		db.execSQL("insert into SongTable15 values(83,	'백청강',	'J에게',	'이선희',	'539',	'688');");
		db.execSQL("insert into SongTable15 values(84,	'백청강',	'인연 (동녘바람)',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable15 values(85,	'백청강',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable15 values(86,	'백청강',	'이별(離別)이 별이 되나봐',	'백청강',	'33979',	'47400');");
		db.execSQL("insert into SongTable15 values(87,	'백청강',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable15 values(88,	'백청강',	'Without You',	'Badfinger(원곡) / Harry Nilsson',	'7096',	'2105');");
		db.execSQL("insert into SongTable15 values(89,	'백청강',	'Heartbreaker',	'G-Dragon',	'31541',	'46752');");
		db.execSQL("insert into SongTable15 values(90,	'백청강',	'We Are The Future',	'H.O.T',	'3995',	'5051');");
		db.execSQL("insert into SongTable15 values(91,	'백청강',	'사랑 그 시린 아픔으로',	'김경호',	'15730',	'45491');");
		db.execSQL("insert into SongTable15 values(92,	'백청강',	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable15 values(93,	'백청강',	'생각이 나',	'부활',	'31528',	'46749');");
		db.execSQL("insert into SongTable15 values(94,	'백청강',	'희야',	'부활',	'1824',	'2015');");
		db.execSQL("insert into SongTable15 values(95,	'백청강, 김경호',	'아버지',	'김경호',	'11826',	'9505');");
		db.execSQL("insert into SongTable15 values(96,	'백청강, 손진영, 이태권, 부활',	'1970',	'부활',	'13141',	'68280');");
		db.execSQL("insert into SongTable15 values(97,	'백청강, 손진영, 이태권, 부활',	'Lonely Night',	'부활',	'4001',	'5087');");
		db.execSQL("insert into SongTable15 values(98,	'백청강, 양정모',	'사랑보다 깊은 상처',	'박정현, 임재범',	'4980',	'5810');");
		db.execSQL("insert into SongTable15 values(99,	'서의환',	'전부 너였다',	'노을',	'15695',	'45485');");
		db.execSQL("insert into SongTable15 values(100,	'서의환, 오영근',	'그대 내게 다시',	'럼블피쉬',	'18367',	'46014');");
		db.execSQL("insert into SongTable15 values(101,	'서형주',	'If I Ain＇t Got You',	'Alicia Keys',	'21045',	'61469');");
		db.execSQL("insert into SongTable15 values(102,	'서형주',	'Everywhere',	'Michelle Branch',	'20720',	'61258');");
		db.execSQL("insert into SongTable15 values(103,	'셰인',	'Don＇t Know Why',	'Norah Jones',	'20608',	'61137');");
		db.execSQL("insert into SongTable15 values(104,	'셰인',	'태양을 피하는 방법',	'비',	'12094',	'9573');");
		db.execSQL("insert into SongTable15 values(105,	'셰인',	'단발머리',	'조용필',	'635',	'1461');");
		db.execSQL("insert into SongTable15 values(106,	'셰인',	'그 때 그 사람',	'심수봉',	'374',	'176');");
		db.execSQL("insert into SongTable15 values(107,	'셰인',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(108,	'셰인',	'그대 내 맘에 들어오면은',	'조덕배',	'910',	'2249');");
		db.execSQL("insert into SongTable15 values(109,	'셰인',	'왜 그래',	'김현철',	'2828',	'3988');");
		db.execSQL("insert into SongTable15 values(110,	'셰인',	'Baby Baby',	'포맨',	'30354',	'83866');");
		db.execSQL("insert into SongTable15 values(111,	'셰인',	'If I Ain＇t Got You',	'Alicia Keys',	'21045',	'61469');");
		db.execSQL("insert into SongTable15 values(112,	'셰인',	'I＇m In Love',	'라디',	'34106',	'86461');");
		db.execSQL("insert into SongTable15 values(113,	'셰인',	'소녀에게',	'신승훈',	'1404',	'2099');");
		db.execSQL("insert into SongTable15 values(114,	'셰인, 신승훈',	'I Believe (엽기적인 그녀 O.S.T)',	'신승훈',	'9603',	'6983');");
		db.execSQL("insert into SongTable15 values(115,	'셰인, 신승훈',	'나비효과',	'신승훈',	'30246',	'85980');");
		db.execSQL("insert into SongTable15 values(116,	'셰인, 윤건희, 조형우, 황지환',	'I＇m Your Friend',	'프로젝트 프렌즈',	'33775',	'47333');");
		db.execSQL("insert into SongTable15 values(117,	'셰인, 정엽',	'Nothing Better',	'정엽',	'33010',	'86615');");
		db.execSQL("insert into SongTable15 values(118,	'셰인, 한승구',	'Again & Again',	'2PM',	'31059',	'46637');");
		db.execSQL("insert into SongTable15 values(119,	'손진영',	'사랑해 그리고 기억해',	'god',	'8540',	'6136');");
		db.execSQL("insert into SongTable15 values(120,	'손진영',	'바람의 노래',	'조용필',	'3880',	'4983');");
		db.execSQL("insert into SongTable15 values(121,	'손진영',	'첫 눈이 온다구요',	'이정석',	'2757',	'1711');");
		db.execSQL("insert into SongTable15 values(122,	'손진영',	'거꾸로 강을 거슬러 오르는 저 힘찬 연어들처럼',	'강산에',	'4582',	'5443');");
		db.execSQL("insert into SongTable15 values(123,	'손진영',	'이 밤이 지나면',	'임재범',	'1254',	'1238');");
		db.execSQL("insert into SongTable15 values(124,	'손진영',	'She＇s Gone',	'Steelheart',	'7095',	'1677');");
		db.execSQL("insert into SongTable15 values(125,	'손진영',	'Lonely Night',	'부활',	'4001',	'5087');");
		db.execSQL("insert into SongTable15 values(126,	'손진영',	'사랑할수록',	'부활',	'1917',	'3356');");
		db.execSQL("insert into SongTable15 values(127,	'손진영',	'나와 같다면',	'김장훈',	'4490',	'5414');");
		db.execSQL("insert into SongTable15 values(128,	'손진영, 이진선',	'사랑보다 깊은 상처',	'박정현, 임재범',	'4980',	'5810');");
		db.execSQL("insert into SongTable15 values(129,	'신샤론',	'난 괜찮아',	'진주',	'4263',	'5188');");
		db.execSQL("insert into SongTable15 values(130,	'신샤론',	'Tell Me',	'S.E.S',	'8613',	'69208');");
		db.execSQL("insert into SongTable15 values(131,	'안아리',	'하하하쏭',	'자우림',	'14078',	'45017');");
		db.execSQL("insert into SongTable15 values(132,	'안아리',	'You & I',	'박봄',	'31811',	'46829');");
		db.execSQL("insert into SongTable15 values(133,	'안아리',	'미안해 널 미워해',	'자우림',	'4865',	'5710');");
		db.execSQL("insert into SongTable15 values(134,	'안아리',	'좋은 날',	'아이유',	'33393',	'76754');");
		db.execSQL("insert into SongTable15 values(135,	'양정모',	'아름다운 사실',	'부활',	'12491',	'9627');");
		db.execSQL("insert into SongTable15 values(136,	'양정모',	'생각이 나',	'부활',	'31528',	'46749');");
		db.execSQL("insert into SongTable15 values(137,	'양정모',	'비밀',	'부활',	'33554',	'47265');");
		db.execSQL("insert into SongTable15 values(138,	'양정모',	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable15 values(139,	'유솔아, 윤건희',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable15 values(140,	'유수미',	'사랑 참 밉다',	'이영현',	'33188',	'47175');");
		db.execSQL("insert into SongTable15 values(141,	'윤건희',	'I Believe (엽기적인 그녀 O.S.T)',	'신승훈',	'9603',	'6983');");
		db.execSQL("insert into SongTable15 values(142,	'윤건희',	'마법의 성',	'더 클래식',	'2238',	'3514');");
		db.execSQL("insert into SongTable15 values(143,	'윤건희',	'오랜 이별 뒤에',	'신승훈',	'2446',	'3706');");
		db.execSQL("insert into SongTable15 values(144,	'윤건희',	'나보다 조금 더 높은 곳에 니가 있을 뿐',	'신승훈',	'3091',	'4110');");
		db.execSQL("insert into SongTable15 values(145,	'윤건희',	'니가 밉다',	'2PM',	'31103',	'86159');");
		db.execSQL("insert into SongTable15 values(146,	'이동미',	'What＇s Up',	'4 Non Blondes',	'7180',	'2459');");
		db.execSQL("insert into SongTable15 values(147,	'이미소',	'어제처럼',	'J',	'8822',	'6318');");
		db.execSQL("insert into SongTable15 values(148,	'이미소',	'못해',	'포맨',	'32125',	'46912');");
		db.execSQL("insert into SongTable15 values(149,	'이아람',	'눈물이 안 났어',	'임정희',	'15278',	'64819');");
		db.execSQL("insert into SongTable15 values(150,	'이아람',	'Stand Up For Love',	'Destiny＇s Child',	'21362',	'61934');");
		db.execSQL("insert into SongTable15 values(151,	'이은비, 홍윤지',	'Gee',	'소녀시대',	'30627',	'84011');");
		db.execSQL("insert into SongTable15 values(152,	'이주형, 장유란',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable15 values(153,	'이진선',	'365일',	'알리',	'31733',	'84507');");
		db.execSQL("insert into SongTable15 values(154,	'이진선',	'녹턴',	'이은미',	'32718',	'86559');");
		db.execSQL("insert into SongTable15 values(155,	'이진선',	'혼자만 하는 사랑',	'거미',	'15258',	'69677');");
		db.execSQL("insert into SongTable15 values(156,	'이태권',	'Bad Case Of Loving You',	'Robert Palmer',	'7236',	'1056');");
		db.execSQL("insert into SongTable15 values(157,	'이태권',	'꿈',	'조용필',	'693',	'1036');");
		db.execSQL("insert into SongTable15 values(158,	'이태권',	'슬픈 그림같은 사랑',	'이상우',	'1301',	'1309');");
		db.execSQL("insert into SongTable15 values(159,	'이태권',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable15 values(160,	'이태권',	'Love Potion No.9',	'The Searchers',	'7621',	'4664');");
		db.execSQL("insert into SongTable15 values(161,	'이태권',	'박하사탕',	'윤도현밴드(YB)',	'9699',	'7640');");
		db.execSQL("insert into SongTable15 values(162,	'이태권',	'흑백사진',	'이태권',	'33978',	'47402');");
		db.execSQL("insert into SongTable15 values(163,	'이태권',	'오늘 같은 밤이면',	'박정운',	'1249',	'1292');");
		db.execSQL("insert into SongTable15 values(164,	'이태권',	'Love',	'씨엔블루',	'32612',	'47026');");
		db.execSQL("insert into SongTable15 values(165,	'이태권',	'What＇s Up',	'4 Non Blondes',	'7180',	'2459');");
		db.execSQL("insert into SongTable15 values(166,	'이태권',	'사랑',	'부활',	'16654',	'45773');");
		db.execSQL("insert into SongTable15 values(167,	'이태권',	'Stayin＇ Alive',	'Bee Gees',	'7014',	'2821');");
		db.execSQL("insert into SongTable15 values(168,	'이태권',	'여전히 아름다운 지',	'토이',	'4975',	'5781');");
		db.execSQL("insert into SongTable15 values(169,	'이태권',	'추억이면',	'부활',	'15004',	'45262');");
		db.execSQL("insert into SongTable15 values(170,	'이태권',	'Lonely Night',	'부활',	'4001',	'5087');");
		db.execSQL("insert into SongTable15 values(171,	'이태권, 양희은',	'사랑 그 쓸쓸함에 대하여',	'양희은',	'4226',	'5193');");
		db.execSQL("insert into SongTable15 values(172,	'임우태',	'Cracks Of My Broken Heart',	'Eric Benet',	'21892',	'84983');");
		db.execSQL("insert into SongTable15 values(173,	'임우태, 한지선',	'그대 내게 다시',	'럼블피쉬',	'18367',	'46014');");
		db.execSQL("insert into SongTable15 values(174,	'정희주',	'이젠 그랬으면 좋겠네',	'조용필',	'13271',	'86701');");
		db.execSQL("insert into SongTable15 values(175,	'정희주',	'하나의 사랑',	'박상민',	'4461',	'5256');");
		db.execSQL("insert into SongTable15 values(176,	'정희주',	'Don＇t Stop Me Now',	'Queen',	'20246',	'8397');");
		db.execSQL("insert into SongTable15 values(177,	'정희주',	'T.O.P (Twinkling Of Paradise)',	'신화',	'8151',	'5866');");
		db.execSQL("insert into SongTable15 values(178,	'정희주',	'헤어지는 중입니다',	'이은미',	'30925',	'84155');");
		db.execSQL("insert into SongTable15 values(179,	'정희주',	'꿈을 꾸다 (아이리스 O.S.T)',	'김태우',	'31865',	'84614');");
		db.execSQL("insert into SongTable15 values(180,	'정희주',	'사랑.. 그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable15 values(181,	'정희주',	'봄날은 간다',	'김윤아',	'5838',	'7642');");
		db.execSQL("insert into SongTable15 values(182,	'정희주',	'Sunday Morning',	'Maroon 5',	'21232',	'84928');");
		db.execSQL("insert into SongTable15 values(183,	'조형우',	'Can＇t Take My Eyes Off You',	'Morten Harket',	'7737',	'5509');");
		db.execSQL("insert into SongTable15 values(184,	'조형우',	'아름다운 구속',	'김종서',	'3611',	'4906');");
		db.execSQL("insert into SongTable15 values(185,	'조형우',	'I＇m Yours',	'Jason Mraz',	'21945',	'60993');");
		db.execSQL("insert into SongTable15 values(186,	'조형우',	'고백',	'뜨거운감자',	'32409',	'76499');");
		db.execSQL("insert into SongTable15 values(187,	'조형우',	'라디오를 켜봐요',	'신승훈',	'30245',	'83822');");
		db.execSQL("insert into SongTable15 values(188,	'조형우',	'로미오와 줄리엣',	'신승훈',	'1385',	'2080');");
		db.execSQL("insert into SongTable15 values(189,	'조형우',	'가시나무',	'시인과 촌장',	'3025',	'4204');");
		db.execSQL("insert into SongTable15 values(190,	'최환준',	'나였으면',	'나윤권',	'13584',	'64428');");
		db.execSQL("insert into SongTable15 values(191,	'한호',	'Insomnia',	'휘성',	'30831',	'84119');");
		db.execSQL("insert into SongTable15 values(192,	'허지애',	'난 알아요',	'서태지와 아이들',	'1294',	'1296');");
		db.execSQL("insert into SongTable15 values(193,	'허지애',	'Put Your Records On',	'Corinne Bailey Rae',	'21559',	'정보없음');");
		db.execSQL("insert into SongTable15 values(194,	'황지환',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable15 values(195,	'황지환',	'내가 너의 곁에 잠시 살았다는 걸',	'토이',	'3119',	'4095');");
		db.execSQL("insert into SongTable15 values(196,	'황지환',	'A Song For U',	'이지영 (빅마마)',	'정보없음',	'58027');");
		db.execSQL("insert into SongTable15 values(197,	'황지환',	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable15 values(198,	'황지환',	'Round & Round',	'브라운아이드소울',	'19342',	'정보없음');");
		db.execSQL("insert into SongTable15 values(199,	'황지환',	'엄마야',	'신승훈',	'8902',	'6335');");
		db.execSQL("insert into SongTable15 values(200,	'황지환',	'로미오와 줄리엣',	'신승훈',	'1385',	'2080');");
		db.execSQL("insert into SongTable15 values(201,	'황지환',	'If I Ain＇t Got You',	'Alicia Keys',	'21045',	'61469');");
		db.execSQL("insert into SongTable15 values(202,	'강민지',	'내 손을 잡아 (최고의 사랑 O.S.T)',	'아이유',	'33962',	'58259');");
		db.execSQL("insert into SongTable15 values(203,	'강병진',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable15 values(204,	'강병진',	'I Do',	'비',	'14048',	'68570');");
		db.execSQL("insert into SongTable15 values(205,	'강지안',	'Ugly',	'2NE1',	'34228',	'47459');");
		db.execSQL("insert into SongTable15 values(206,	'강희정',	'하루종일 비가 내렸어',	'디아',	'33319',	'47198');");
		db.execSQL("insert into SongTable15 values(207,	'강희정',	'별 짓 다 해봤는데',	'알리',	'38882',	'47349');");
		db.execSQL("insert into SongTable15 values(208,	'고필준',	'I＇m in love',	'라디',	'34106',	'86461');");
		db.execSQL("insert into SongTable15 values(209,	'고필준',	'Put Your Records On',	'Corinne Bailey Rae',	'21559',	'정보없음');");
		db.execSQL("insert into SongTable15 values(210,	'구자명',	'비밀',	'부활',	'33554',	'47265');");
		db.execSQL("insert into SongTable15 values(211,	'구자명',	'내 여자',	'더 원',	'30158',	'83778');");
		db.execSQL("insert into SongTable15 values(212,	'구자명',	'그렇게 겪고도 모릅니까',	'태원',	'17182',	'81443');");
		db.execSQL("insert into SongTable15 values(213,	'구자명',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable15 values(214,	'구자명',	'하고 싶은 말',	'김태우',	'16552',	'45742');");
		db.execSQL("insert into SongTable15 values(215,	'구자명',	'그 땐 미처 알지 못했지',	'이적',	'11369',	'9405');");
		db.execSQL("insert into SongTable15 values(216,	'구자명',	'그것만이 내 세상',	'들국화',	'1780',	'1329');");
		db.execSQL("insert into SongTable15 values(217,	'구자명',	'사랑스러워',	'김종국',	'15128',	'64833');");
		db.execSQL("insert into SongTable15 values(218,	'구자명',	'Roly-Poly',	'티아라',	'34109',	'47429');");
		db.execSQL("insert into SongTable15 values(219,	'구자명',	'가시',	'버즈',	'14684',	'69033');");
		db.execSQL("insert into SongTable15 values(220,	'구자명',	'붉은 낙타',	'이승환',	'3704',	'7154');");
		db.execSQL("insert into SongTable15 values(221,	'구자명',	'사람이 꽃보다 아름다워',	'안치환',	'4704',	'5516');");
		db.execSQL("insert into SongTable15 values(222,	'구자명',	'미안해요',	'김건모',	'9520',	'6853');");
		db.execSQL("insert into SongTable15 values(223,	'구자명',	'질주',	'구자명',	'정보없음',	'58579');");
		db.execSQL("insert into SongTable15 values(224,	'구자명, 김태우',	'하고 싶은 말',	'김태우',	'16552',	'45742');");
		db.execSQL("insert into SongTable15 values(225,	'구자명, 배수정, 이선희',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(226,	'구자명, 애슐리 윤',	'미워도 다시 한번',	'바이브',	'9855',	'7804');");
		db.execSQL("insert into SongTable15 values(227,	'구자명, 최태완',	'단념',	'이승기',	'31856',	'정보없음');");
		db.execSQL("insert into SongTable15 values(228,	'그레이스 유',	'Save Me From Myself',	'Christina Aguilera',	'정보없음',	'60152');");
		db.execSQL("insert into SongTable15 values(229,	'그레이스 유, 현진주',	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable15 values(230,	'김경주',	'혜성',	'윤하',	'18793',	'46097');");
		db.execSQL("insert into SongTable15 values(231,	'김경주',	'낭만고양이',	'체리필터',	'6093',	'62666');");
		db.execSQL("insert into SongTable15 values(232,	'김경주',	'Boom Boom Pow',	'Black Eyed Peas',	'20898',	'63151');");
		db.execSQL("insert into SongTable15 values(233,	'김경주',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable15 values(234,	'김경주',	'비밀번호 486',	'윤하',	'17489',	'45877');");
		db.execSQL("insert into SongTable15 values(235,	'김경주',	'엄마의 일기',	'왁스',	'9260',	'6657');");
		db.execSQL("insert into SongTable15 values(236,	'김경주',	'눈물',	'리아',	'4791',	'5626');");
		db.execSQL("insert into SongTable15 values(237,	'김경주, 신예림',	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable15 values(238,	'김경주, 정유정',	'8282',	'다비치',	'30868',	'46582');");
		db.execSQL("insert into SongTable15 values(239,	'김경주, 최태완',	'내 안의 그대',	'서영은',	'11834',	'9502');");
		db.execSQL("insert into SongTable15 values(240,	'김도엽',	'잠시 길을 잃다',	'015B',	'19300',	'86315');");
		db.execSQL("insert into SongTable15 values(241,	'김도엽',	'Listen',	'Beyonce',	'21531',	'60490');");
		db.execSQL("insert into SongTable15 values(242,	'김동욱',	'이유 (시크릿가든 O.S.T)',	'신용재',	'33344',	'58088');");
		db.execSQL("insert into SongTable15 values(243,	'김동욱',	'She＇s Gone',	'Steelheart',	'7095',	'1677');");
		db.execSQL("insert into SongTable15 values(244,	'김동현, 이병호',	'I Believe I Can Fly',	'R.Kelly',	'7763',	'61152');");
		db.execSQL("insert into SongTable15 values(245,	'김민선',	'I Love You',	'나비',	'19442',	'83491');");
		db.execSQL("insert into SongTable15 values(246,	'김민수',	'여전히 아름다운 지',	'토이',	'4975',	'5781');");
		db.execSQL("insert into SongTable15 values(247,	'김민수',	'Rush',	'리쌍',	'6086',	'9009');");
		db.execSQL("insert into SongTable15 values(248,	'김민정',	'Love You I Do',	'Jennifer Hudson',	'정보없음',	'60214');");
		db.execSQL("insert into SongTable15 values(249,	'김민정',	'Mercy',	'Duffy',	'22324',	'60163');");
		db.execSQL("insert into SongTable15 values(250,	'김민정, 김현승',	'미워도 다시 한번',	'바이브',	'9855',	'7804');");
		db.execSQL("insert into SongTable15 values(251,	'김성진',	'꼬마 I Cry',	'윤하',	'17580',	'81596');");
		db.execSQL("insert into SongTable15 values(252,	'김성진',	'별',	'유미',	'16790',	'85322');");
		db.execSQL("insert into SongTable15 values(253,	'김성진',	'Lovin＇ You',	'Minnie Riperton',	'20139',	'8139');");
		db.execSQL("insert into SongTable15 values(254,	'김성진',	'Put Your Records On',	'Corinne Bailey Rae',	'21559',	'정보없음');");
		db.execSQL("insert into SongTable15 values(255,	'김성진, 애슐리 윤',	'다이어리',	'나비',	'34117',	'76951');");
		db.execSQL("insert into SongTable15 values(256,	'김세경',	'I Will',	'The Beatles',	'7880',	'7880');");
		db.execSQL("insert into SongTable15 values(257,	'김시은',	'나와 같다면',	'김장훈',	'4490',	'5414');");
		db.execSQL("insert into SongTable15 values(258,	'김시은, 반규남',	'왼손잡이',	'패닉',	'3070',	'4147');");
		db.execSQL("insert into SongTable15 values(259,	'김시은, 서혜인',	'Price Tag',	'Jessie J',	'22199',	'84892');");
		db.execSQL("insert into SongTable15 values(260,	'김신혜',	'난 널 사랑해',	'신효범',	'1792',	'3326');");
		db.execSQL("insert into SongTable15 values(261,	'김예훈',	'기다리다',	'패닉',	'8707',	'7098');");
		db.execSQL("insert into SongTable15 values(262,	'김우영',	'Dr Feel Good',	'라니아',	'정보없음',	'47355');");
		db.execSQL("insert into SongTable15 values(263,	'김우영',	'기억만이라도',	'앤',	'11445',	'9422');");
		db.execSQL("insert into SongTable15 values(264,	'김종민',	'Creep',	'Radio Head',	'7740',	'8270');");
		db.execSQL("insert into SongTable15 values(265,	'김종민',	'타잔',	'윤도현밴드(YB)',	'4912',	'4131');");
		db.execSQL("insert into SongTable15 values(266,	'김주현',	'가로수 그늘 아래 서면',	'이문세',	'3',	'1451');");
		db.execSQL("insert into SongTable15 values(267,	'김지은',	'다이어리',	'나비',	'34117',	'76951');");
		db.execSQL("insert into SongTable15 values(268,	'김지은',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(269,	'김진영',	'Hip Song',	'비',	'32440',	'57876');");
		db.execSQL("insert into SongTable15 values(270,	'김태극',	'첫인상',	'김건모',	'1513',	'2162');");
		db.execSQL("insert into SongTable15 values(271,	'김태극',	'Chitty Chitty Bang Bang',	'이효리',	'32460',	'57878');");
		db.execSQL("insert into SongTable15 values(272,	'김태극',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable15 values(273,	'김태극',	'엄마',	'라디',	'36202',	'87031');");
		db.execSQL("insert into SongTable15 values(274,	'김태극',	'추억 속의 그대',	'황치훈',	'4',	'722');");
		db.execSQL("insert into SongTable15 values(275,	'김태극',	'그대 내 품에',	'유재하(TJ) / 김현식(KY)',	'12766',	'62251');");
		db.execSQL("insert into SongTable15 values(276,	'김태극',	'날 위한 이별',	'김혜림',	'2412',	'3622');");
		db.execSQL("insert into SongTable15 values(277,	'김태극',	'너의 뒤에서',	'박진영',	'2526',	'3607');");
		db.execSQL("insert into SongTable15 values(278,	'김태극, 샘 카터, 최정훈, 홍동균',	'불티',	'전영록',	'920',	'1671');");
		db.execSQL("insert into SongTable15 values(279,	'김태극, 양민우',	'Swing Baby',	'박진영',	'9564',	'6924');");
		db.execSQL("insert into SongTable15 values(280,	'김태극, 저스틴 김, 전은진',	'이젠 안녕',	'015B',	'866',	'1386');");
		db.execSQL("insert into SongTable15 values(281,	'김태극, 홍동균',	'Chitty Chitty Bang Bang',	'이효리',	'32460',	'57878');");
		db.execSQL("insert into SongTable15 values(282,	'김태극, 홍동균',	'The Way U Are',	'동방신기',	'13480',	'68334');");
		db.execSQL("insert into SongTable15 values(283,	'김태극, 홍동균',	'Rock U',	'카라',	'19935',	'46374');");
		db.execSQL("insert into SongTable15 values(284,	'김태양',	'추억 속의 그대',	'이승기',	'19396',	'85776');");
		db.execSQL("insert into SongTable15 values(285,	'김태양',	'감기',	'이기찬',	'10335',	'9145');");
		db.execSQL("insert into SongTable15 values(286,	'김현승',	'가슴 아파도 (패션 70s O.S.T)',	'플라이 투 더 스카이',	'14928',	'45232');");
		db.execSQL("insert into SongTable15 values(287,	'김현중',	'365일',	'알리',	'31733',	'84507');");
		db.execSQL("insert into SongTable15 values(288,	'김혜랑',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable15 values(289,	'김혜랑',	'The Time(Dirty Bit)',	'Black Eyed Peas',	'정보없음',	'84883');");
		db.execSQL("insert into SongTable15 values(290,	'김혜랑',	'Bye Bye Bye',	'화요비',	'32638',	'47040');");
		db.execSQL("insert into SongTable15 values(291,	'닉 조쉬',	'나무',	'보아',	'11479',	'63340');");
		db.execSQL("insert into SongTable15 values(292,	'닉 조쉬',	'A',	'레인보우',	'32938',	'47105');");
		db.execSQL("insert into SongTable15 values(293,	'디디무',	'Bubble Pop',	'현아',	'34128',	'76958');");
		db.execSQL("insert into SongTable15 values(294,	'레이 홍',	'J에게',	'이선희',	'539',	'688');");
		db.execSQL("insert into SongTable15 values(295,	'루이스초이',	'꽃밭에서',	'정훈희',	'2960',	'3010');");
		db.execSQL("insert into SongTable15 values(296,	'루이스초이',	'Nella Fantasia',	'Sarah Brightman',	'22146',	'84993');");
		db.execSQL("insert into SongTable15 values(297,	'메간 리',	'별',	'유미',	'16790',	'85322');");
		db.execSQL("insert into SongTable15 values(298,	'메간 리',	'이젠 그랬으면 좋겠네',	'조용필',	'13271',	'86701');");
		db.execSQL("insert into SongTable15 values(299,	'메간 리',	'마법의 성',	'더 클래식',	'2238',	'3514');");
		db.execSQL("insert into SongTable15 values(300,	'메간 리',	'365일',	'알리',	'31733',	'84507');");
		db.execSQL("insert into SongTable15 values(301,	'메간 리',	'어떤가요',	'이정봉',	'3289',	'4710');");
		db.execSQL("insert into SongTable15 values(302,	'메간 리',	'You Mean Everything To Me',	'박정현',	'9253',	'6594');");
		db.execSQL("insert into SongTable15 values(303,	'메간 리, 장이정',	'2 Different Tears',	'원더걸스',	'32605',	'47023');");
		db.execSQL("insert into SongTable15 values(304,	'메간 리, 장이정',	'Couple Song',	'라디',	'30529',	'46520');");
		db.execSQL("insert into SongTable15 values(305,	'메간 리, 한다성',	'Way Back Into Love (그 여자 작사 그 남자 작곡 O.S.T)',	'Hugh Grant, Haley Bennett',	'21573',	'60487');");
		db.execSQL("insert into SongTable15 values(306,	'박민영, 장성재, 정서경, 홍동균',	'사랑했나봐',	'윤도현밴드(YB)',	'14832',	'45202');");
		db.execSQL("insert into SongTable15 values(307,	'박상미',	'Going Home',	'김윤아',	'32517',	'47008');");
		db.execSQL("insert into SongTable15 values(308,	'박영삼',	'별 짓 다 해봤는데',	'알리',	'38882',	'47349');");
		db.execSQL("insert into SongTable15 values(309,	'박영삼',	'난치병',	'하림',	'5865',	'9018');");
		db.execSQL("insert into SongTable15 values(310,	'박영삼',	'가로수 그늘 아래 서면',	'이문세',	'3',	'1451');");
		db.execSQL("insert into SongTable15 values(311,	'박지혜',	'나비',	'박기영',	'14091',	'45031');");
		db.execSQL("insert into SongTable15 values(312,	'박지혜',	'Mercy',	'Duffy',	'22324',	'60163');");
		db.execSQL("insert into SongTable15 values(313,	'박지혜',	'Rolling In The Deep',	'Adele',	'22213',	'79017');");
		db.execSQL("insert into SongTable15 values(314,	'박지혜',	'그녀는 예뻤다',	'박진영',	'3848',	'4979');");
		db.execSQL("insert into SongTable15 values(315,	'반규남',	'삐딱하게',	'강산에(원곡) / 윤도현(TJ, KY)',	'34279',	'76998');");
		db.execSQL("insert into SongTable15 values(316,	'반규남',	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable15 values(317,	'배수정',	'Bust Your Windows',	'Jazmin Sullivan',	'정보없음',	'79036');");
		db.execSQL("insert into SongTable15 values(318,	'배수정',	'예감 좋은 날',	'럼블피쉬',	'13548',	'64395');");
		db.execSQL("insert into SongTable15 values(319,	'배수정',	'편지할게요',	'박정현',	'8124',	'5837');");
		db.execSQL("insert into SongTable15 values(320,	'배수정',	'봄날은 간다',	'김윤아',	'5838',	'7642');");
		db.execSQL("insert into SongTable15 values(321,	'배수정',	'실화',	'린',	'31815',	'84580');");
		db.execSQL("insert into SongTable15 values(322,	'배수정',	'미소를 띄우며 나를 보낸 그 모습처럼',	'이은하',	'1946',	'1419');");
		db.execSQL("insert into SongTable15 values(323,	'배수정',	'L-O-V-E',	'Nat King Cole(Natalie Cole)',	'21216',	'정보없음');");
		db.execSQL("insert into SongTable15 values(324,	'배수정',	'직감',	'씨엔블루',	'33783',	'47325');");
		db.execSQL("insert into SongTable15 values(325,	'배수정',	'거리에서',	'동물원',	'1332',	'1326');");
		db.execSQL("insert into SongTable15 values(326,	'배수정',	'괜찮아',	'박효신',	'11363',	'62701');");
		db.execSQL("insert into SongTable15 values(327,	'배수정',	'Music Is My Life',	'임정희',	'14915',	'45220');");
		db.execSQL("insert into SongTable15 values(328,	'배수정',	'Hero',	'Mariah Carey',	'7317',	'8086');");
		db.execSQL("insert into SongTable15 values(329,	'배수정',	'칠갑산',	'주병선',	'777',	'733');");
		db.execSQL("insert into SongTable15 values(330,	'배수정',	'두근두근 콩닥콩닥',	'배수정',	'정보없음',	'87220');");
		db.execSQL("insert into SongTable15 values(331,	'배수정, 김조한',	'Fields Of Gold',	'Sting',	'정보없음',	'8843');");
		db.execSQL("insert into SongTable15 values(332,	'배수정, 에릭 남',	'2 Different Tears',	'원더걸스',	'32605',	'47023');");
		db.execSQL("insert into SongTable15 values(333,	'배수정, 이태윤',	'Break Away',	'빅마마',	'10834',	'9282');");
		db.execSQL("insert into SongTable15 values(334,	'배수정, 정서경',	'당신은 모르실거야',	'혜은이',	'477',	'273');");
		db.execSQL("insert into SongTable15 values(335,	'백청강',	'그리워져',	'백청강',	'35266',	'47717');");
		db.execSQL("insert into SongTable15 values(336,	'백청강, 이태권',	'Bad Case Of Loving You',	'Robert Palmer',	'7236',	'1056');");
		db.execSQL("insert into SongTable15 values(337,	'백청강, 황지환',	'나만 바라봐',	'태양',	'19645',	'46306');");
		db.execSQL("insert into SongTable15 values(338,	'샘 카터',	'Someone Like You',	'Adele',	'22204',	'79035');");
		db.execSQL("insert into SongTable15 values(339,	'샘 카터',	'아이처럼',	'김동률',	'19166',	'83375');");
		db.execSQL("insert into SongTable15 values(340,	'샘 카터',	'I Do',	'비',	'14048',	'68570');");
		db.execSQL("insert into SongTable15 values(341,	'샘 카터',	'이별의 그늘',	'윤상',	'695',	'635');");
		db.execSQL("insert into SongTable15 values(342,	'샘 카터',	'Wherever You Will Go',	'The Calling',	'20177',	'61088');");
		db.execSQL("insert into SongTable15 values(343,	'샘 카터',	'잊을께',	'윤도현',	'11761',	'63748');");
		db.execSQL("insert into SongTable15 values(344,	'샘 카터',	'애인있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable15 values(345,	'샘 카터',	'미련',	'김건모',	'3136',	'4168');");
		db.execSQL("insert into SongTable15 values(346,	'샘 카터',	'한 남자',	'김종국',	'13448',	'9829');");
		db.execSQL("insert into SongTable15 values(347,	'샘 카터',	'Right Here Waiting',	'Richard Marx',	'7273',	'2443');");
		db.execSQL("insert into SongTable15 values(348,	'샘 카터, 정서경',	'Way Back Into Love (그 여자 작사 그 남자 작곡 O.S.T)',	'Hugh Grant, Haley Bennett',	'21573',	'60487');");
		db.execSQL("insert into SongTable15 values(349,	'서준교',	'물들어',	'BMK',	'17427',	'85390');");
		db.execSQL("insert into SongTable15 values(350,	'서준교',	'사랑은 이별보다 빨라서',	'BMK',	'33230',	'76710');");
		db.execSQL("insert into SongTable15 values(351,	'서준교',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable15 values(352,	'서준교, 한다성',	'청혼',	'노을',	'13319',	'68248');");
		db.execSQL("insert into SongTable15 values(353,	'서혜인',	'Hoedown Throwdown',	'Miley Cyrus',	'정보없음',	'84970');");
		db.execSQL("insert into SongTable15 values(354,	'서혜인',	'만약에',	'태연',	'19187',	'83377');");
		db.execSQL("insert into SongTable15 values(355,	'세리나',	'I＇m Yours',	'Jason Mraz',	'21945',	'60993');");
		db.execSQL("insert into SongTable15 values(356,	'세리나',	'You & I',	'박봄',	'31811',	'46829');");
		db.execSQL("insert into SongTable15 values(357,	'셰인',	'너를 본다',	'셰인',	'35120',	'47686');");
		db.execSQL("insert into SongTable15 values(358,	'셰인, 에릭 남',	'Just The Way You Are',	'Bruno Mars',	'22134',	'84854');");
		db.execSQL("insert into SongTable15 values(359,	'송창석',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(360,	'송한희',	'길에서',	'나비',	'31675',	'86263');");
		db.execSQL("insert into SongTable15 values(361,	'시봉균',	'Love The Way You Lie (Feat. Rihanna)',	'Eminem',	'22105',	'84951');");
		db.execSQL("insert into SongTable15 values(362,	'신예림',	'보고 싶은 얼굴',	'민해경',	'421',	'408');");
		db.execSQL("insert into SongTable15 values(363,	'신예림',	'Fallin＇',	'Alicia Keys',	'20069',	'61072');");
		db.execSQL("insert into SongTable15 values(364,	'신예림',	'Just Dance',	'Lady GaGa',	'21938',	'63142');");
		db.execSQL("insert into SongTable15 values(365,	'신예림',	'Reflection (Mulan O.S.T)',	'Christina Aguilera',	'7796',	'61040');");
		db.execSQL("insert into SongTable15 values(366,	'신예림',	'낭만고양이',	'체리필터',	'6093',	'62666');");
		db.execSQL("insert into SongTable15 values(367,	'신예림',	'비밀번호 486',	'윤하',	'17489',	'45877');");
		db.execSQL("insert into SongTable15 values(368,	'신예림',	'미쳐',	'이정현',	'9682',	'7620');");
		db.execSQL("insert into SongTable15 values(369,	'신예림',	'저기요',	'요아리',	'32733',	'47067');");
		db.execSQL("insert into SongTable15 values(370,	'신용수',	'넌 감동이었어',	'성시경',	'5768',	'9006');");
		db.execSQL("insert into SongTable15 values(371,	'신용수',	'Good Old Fashioned Lover Boy',	'Queen',	'21947',	'정보없음');");
		db.execSQL("insert into SongTable15 values(372,	'신용수',	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable15 values(373,	'쌀리타',	'모나리자',	'엠블랙',	'34159',	'47438');");
		db.execSQL("insert into SongTable15 values(374,	'쌀리타',	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable15 values(375,	'쓰리포인트',	'Rush',	'리쌍',	'6086',	'9009');");
		db.execSQL("insert into SongTable15 values(376,	'안희준',	'난 아직 모르잖아요',	'이문세',	'631',	'828');");
		db.execSQL("insert into SongTable15 values(377,	'안희준',	'전부 너였다',	'노을',	'15695',	'45485');");
		db.execSQL("insert into SongTable15 values(378,	'안희준',	'친구의 고백',	'2AM',	'30954',	'46600');");
		db.execSQL("insert into SongTable15 values(379,	'양민우',	'나만 바라봐',	'태양',	'19645',	'46306');");
		db.execSQL("insert into SongTable15 values(380,	'양승우',	'Still Loving You',	'Scorpions',	'7909',	'2458');");
		db.execSQL("insert into SongTable15 values(381,	'앙토니 맥펄슨',	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable15 values(382,	'애슐리 윤',	'A Moment Like This',	'Kelly Clarkson',	'20361',	'61881');");
		db.execSQL("insert into SongTable15 values(383,	'애슐리 윤',	'잘 된 일이야',	'나비',	'33561',	'47268');");
		db.execSQL("insert into SongTable15 values(384,	'애슐리 윤',	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable15 values(385,	'애슐리 윤',	'어른 아이',	'거미',	'15195',	'64851');");
		db.execSQL("insert into SongTable15 values(386,	'애슐리 윤',	'못해',	'포맨',	'32125',	'46912');");
		db.execSQL("insert into SongTable15 values(387,	'에릭 남',	'Set Fire To The Rain',	'Adele',	'22299',	'79062');");
		db.execSQL("insert into SongTable15 values(388,	'에릭 남',	'Ordinary People',	'John Legend',	'21388',	'61976');");
		db.execSQL("insert into SongTable15 values(389,	'에릭 남',	'행복한 나를',	'에코',	'4108',	'5168');");
		db.execSQL("insert into SongTable15 values(390,	'에릭 남',	'안되나요',	'휘성',	'9851',	'7859');");
		db.execSQL("insert into SongTable15 values(391,	'에릭 남',	'울다',	'이승환',	'16746',	'81327');");
		db.execSQL("insert into SongTable15 values(392,	'에릭 남',	'Ugly',	'2NE1',	'34228',	'47459');");
		db.execSQL("insert into SongTable15 values(393,	'에릭 남',	'봄 여름 가을 겨울',	'봄 여름 가을 겨울',	'1802',	'정보없음');");
		db.execSQL("insert into SongTable15 values(394,	'에릭 남',	'If I Ain＇t Got You',	'Alicia Keys',	'21045',	'61469');");
		db.execSQL("insert into SongTable15 values(395,	'에릭 남',	'길',	'god',	'9706',	'7632');");
		db.execSQL("insert into SongTable15 values(396,	'에릭 남',	'Love',	'씨엔블루',	'32612',	'47026');");
		db.execSQL("insert into SongTable15 values(397,	'에릭 남',	'Sunday Morning',	'Maroon 5',	'21232',	'84928');");
		db.execSQL("insert into SongTable15 values(398,	'우인익',	'뭐라할까',	'브리즈',	'13591',	'68379');");
		db.execSQL("insert into SongTable15 values(399,	'윤정',	'인연 (동녘바람)',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable15 values(400,	'윤정',	'너는 내 남자',	'한혜진',	'11896',	'9514');");
		db.execSQL("insert into SongTable15 values(401,	'윤정',	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable15 values(402,	'이나래',	'비와 당신',	'럼블피쉬',	'30450',	'46485');");
		db.execSQL("insert into SongTable15 values(403,	'이병옥',	'Live High',	'Jason Mraz',	'정보없음',	'79146');");
		db.execSQL("insert into SongTable15 values(404,	'이병옥',	'단발머리',	'조용필',	'635',	'1461');");
		db.execSQL("insert into SongTable15 values(405,	'이병호',	'Run It!',	'Chris Brown',	'21377',	'60922');");
		db.execSQL("insert into SongTable15 values(406,	'이서연',	'Oh!',	'소녀시대',	'32153',	'46920');");
		db.execSQL("insert into SongTable15 values(407,	'이서연',	'Don＇t Cry',	'박봄',	'38912',	'47368');");
		db.execSQL("insert into SongTable15 values(408,	'이서연',	'Goodbye Baby',	'miss A',	'34182',	'47443');");
		db.execSQL("insert into SongTable15 values(409,	'이서연',	'You & I',	'박봄',	'31811',	'46829');");
		db.execSQL("insert into SongTable15 values(410,	'이서연',	'낭만고양이',	'체리필터',	'6093',	'62666');");
		db.execSQL("insert into SongTable15 values(411,	'이성현',	'다시 사랑한다 말할까',	'김동률',	'9691',	'7618');");
		db.execSQL("insert into SongTable15 values(412,	'이성현',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(413,	'이소영',	'Five',	'체리필터',	'9084',	'6449');");
		db.execSQL("insert into SongTable15 values(414,	'이소영',	'천일동안',	'이승환',	'2679',	'3853');");
		db.execSQL("insert into SongTable15 values(415,	'이소영, 정지원',	'난 알아요',	'서태지와 아이들',	'1294',	'1296');");
		db.execSQL("insert into SongTable15 values(416,	'이수연',	'사랑은 언제나 목마르다',	'유미',	'9835',	'7809');");
		db.execSQL("insert into SongTable15 values(417,	'이수연',	'피아노의 숲',	'버블시스터즈',	'34007',	'47418');");
		db.execSQL("insert into SongTable15 values(418,	'이수연',	'아파 아이야',	'양파',	'33805',	'47334');");
		db.execSQL("insert into SongTable15 values(419,	'이수연',	'Girls On Top',	'보아',	'14984',	'45256');");
		db.execSQL("insert into SongTable15 values(420,	'이연희',	'강원도 아리랑',	'민요(조용필)',	'5200',	'114');");
		db.execSQL("insert into SongTable15 values(421,	'이은별',	'담',	'김윤아',	'9717',	'7651');");
		db.execSQL("insert into SongTable15 values(422,	'이찬',	'Beat It',	'Michael Jackson',	'7437',	'2509');");
		db.execSQL("insert into SongTable15 values(423,	'이태권',	'사랑에 떨어지다',	'이태권',	'35115',	'87201');");
		db.execSQL("insert into SongTable15 values(424,	'이하나',	'Dear. Mom',	'소녀시대',	'30778',	'84101');");
		db.execSQL("insert into SongTable15 values(425,	'이하나',	'Huh',	'포미닛',	'32613',	'47032');");
		db.execSQL("insert into SongTable15 values(426,	'이하나',	'비밀번호 486',	'윤하',	'17489',	'45877');");
		db.execSQL("insert into SongTable15 values(427,	'이홍규',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable15 values(428,	'이환희',	'죽일 놈',	'다이나믹듀오',	'31729',	'84559');");
		db.execSQL("insert into SongTable15 values(429,	'임랜스',	'거위의 꿈',	'카니발',	'4241',	'5186');");
		db.execSQL("insert into SongTable15 values(430,	'임랜스',	'진달래꽃',	'마야',	'11178',	'9382');");
		db.execSQL("insert into SongTable15 values(431,	'임랜스',	'The Climb',	'Miley Cyrus',	'20902',	'84963');");
		db.execSQL("insert into SongTable15 values(432,	'임랜스',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(433,	'임지연',	'선남선녀',	'유지나',	'32909',	'86616');");
		db.execSQL("insert into SongTable15 values(434,	'장성재',	'나와 같다면',	'김장훈',	'4490',	'5414');");
		db.execSQL("insert into SongTable15 values(435,	'장성재',	'Cracks Of My Broken Heart',	'Eric Benet',	'21892',	'84983');");
		db.execSQL("insert into SongTable15 values(436,	'장성재',	'한 번 더 이별',	'성시경',	'18751',	'46087');");
		db.execSQL("insert into SongTable15 values(437,	'장성재',	'사랑합니다',	'팀(Tim)',	'11271',	'62988');");
		db.execSQL("insert into SongTable15 values(438,	'장성재',	'With Me',	'휘성',	'11861',	'63962');");
		db.execSQL("insert into SongTable15 values(439,	'장성재',	'그 때로 돌아가는 게',	'김조한',	'8394',	'6007');");
		db.execSQL("insert into SongTable15 values(440,	'장성재',	'세월이 가면',	'최호섭',	'817',	'499');");
		db.execSQL("insert into SongTable15 values(441,	'장성재',	'좋을텐데',	'성시경',	'10079',	'9704');");
		db.execSQL("insert into SongTable15 values(442,	'장성재',	'Fiction',	'비스트',	'33928',	'47385');");
		db.execSQL("insert into SongTable15 values(443,	'장성재',	'비와 당신',	'노브레인',	'16491',	'85239');");
		db.execSQL("insert into SongTable15 values(444,	'장성재',	'미소 속에 비친 그대',	'신승훈',	'182',	'370');");
		db.execSQL("insert into SongTable15 values(445,	'장성재, 정희주',	'사랑.. 그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable15 values(446,	'장성재, 최형석',	'청혼',	'노을',	'13319',	'68248');");
		db.execSQL("insert into SongTable15 values(447,	'장솔',	'통화연결음',	'린',	'32032',	'58018');");
		db.execSQL("insert into SongTable15 values(448,	'장솔',	'나만 바라봐',	'태양',	'19645',	'46306');");
		db.execSQL("insert into SongTable15 values(449,	'장솔',	'흩어진 나날들',	'강수지',	'1259',	'795');");
		db.execSQL("insert into SongTable15 values(450,	'장솔, 한예슬',	'Lady Marmalade',	'Moulin Rouge O.S.T',	'20038',	'60491');");
		db.execSQL("insert into SongTable15 values(451,	'장은정',	'그녀는 예뻤다',	'박진영',	'3848',	'4979');");
		db.execSQL("insert into SongTable15 values(452,	'장이정',	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable15 values(453,	'장이정',	'Down (Feat.Lil Wayne)',	'Jay Sean',	'22391',	'84856');");
		db.execSQL("insert into SongTable15 values(454,	'장이정',	'그녀는 예뻤다',	'박진영',	'3848',	'4979');");
		db.execSQL("insert into SongTable15 values(455,	'장이정',	'와줘',	'세븐',	'10988',	'9318');");
		db.execSQL("insert into SongTable15 values(456,	'장이정',	'이 노래',	'2AM',	'19848',	'83678');");
		db.execSQL("insert into SongTable15 values(457,	'장이정',	'같이 걸을까',	'이적',	'33637',	'58178');");
		db.execSQL("insert into SongTable15 values(458,	'장이정, 함춘호',	'죽겠네',	'10cm',	'33659',	'47245');");
		db.execSQL("insert into SongTable15 values(459,	'저스틴 김',	'그녀가 나를 보네',	'브라운아이즈',	'9579',	'6992');");
		db.execSQL("insert into SongTable15 values(460,	'저스틴 김',	'끝사랑',	'김범수',	'34049',	'47408');");
		db.execSQL("insert into SongTable15 values(461,	'저스틴 김',	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable15 values(462,	'저스틴 김',	'On Bended Knee',	'Boyz Ⅱ Men',	'20726',	'61871');");
		db.execSQL("insert into SongTable15 values(463,	'저스틴 김',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(464,	'저스틴 김',	'한 걸음 더',	'윤상',	'1831',	'2173');");
		db.execSQL("insert into SongTable15 values(465,	'저스틴 김',	'기억의 습작',	'전람회',	'2200',	'3462');");
		db.execSQL("insert into SongTable15 values(466,	'저스틴 김',	'Lately',	'Stevie Wonder',	'7960',	'61149');");
		db.execSQL("insert into SongTable15 values(467,	'저스틴 김, 홍동균',	'난 알아요',	'서태지와 아이들',	'1294',	'1296');");
		db.execSQL("insert into SongTable15 values(468,	'전영선',	'When You Told Me You Loved Me',	'Jessica Simpson',	'20842',	'61645');");
		db.execSQL("insert into SongTable15 values(469,	'전영선',	'Nella Fantasia',	'Sarah Brightman',	'22146',	'84993');");
		db.execSQL("insert into SongTable15 values(470,	'전영선',	'친구라도 될 걸 그랬어',	'거미',	'11238',	'63853');");
		db.execSQL("insert into SongTable15 values(471,	'전영선',	'인연 (불새 O.S.T)',	'이승철',	'13165',	'9775');");
		db.execSQL("insert into SongTable15 values(472,	'전은진',	'가리워진 길',	'유재하',	'3028',	'4009');");
		db.execSQL("insert into SongTable15 values(473,	'전은진',	'용서',	'이희진',	'2340',	'3538');");
		db.execSQL("insert into SongTable15 values(474,	'전은진',	'거리에서',	'성시경',	'16503',	'45722');");
		db.execSQL("insert into SongTable15 values(475,	'전은진',	'Stop',	'Sam Brown',	'20019',	'60004');");
		db.execSQL("insert into SongTable15 values(476,	'전은진',	'그 때 그 사람',	'심수봉',	'374',	'176');");
		db.execSQL("insert into SongTable15 values(477,	'전은진',	'좋아좋아',	'일기예보',	'3271',	'4756');");
		db.execSQL("insert into SongTable15 values(478,	'전은진',	'My Prayer',	'보아',	'13416',	'68364');");
		db.execSQL("insert into SongTable15 values(479,	'전은진',	'샤이닝',	'자우림',	'16954',	'85354');");
		db.execSQL("insert into SongTable15 values(480,	'전은진',	'심장병',	'이승환',	'14032',	'45014');");
		db.execSQL("insert into SongTable15 values(481,	'전은진',	'When You Told Me You Loved Me',	'Jessica Simpson',	'20842',	'61645');");
		db.execSQL("insert into SongTable15 values(482,	'전은진',	'Adia',	'Sarah Mclachlan',	'20174',	'61625');");
		db.execSQL("insert into SongTable15 values(483,	'전은진, 이현',	'그대 안의 블루',	'김현철, 이소라',	'1350',	'1962');");
		db.execSQL("insert into SongTable15 values(484,	'전은진, 정서경, 푸니타',	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable15 values(485,	'전은진, 차여울',	'너에게 난, 나에게 넌',	'자전거탄풍경',	'3777',	'62426');");
		db.execSQL("insert into SongTable15 values(486,	'전은진, 푸니타',	'빙글빙글',	'나미',	'139',	'436');");
		db.execSQL("insert into SongTable15 values(487,	'전 출연진',	'Thank For The Music',	'ABBA',	'20129',	'61830');");
		db.execSQL("insert into SongTable15 values(488,	'정민',	'기대',	'나윤권',	'15357',	'45376');");
		db.execSQL("insert into SongTable15 values(489,	'정서경',	'누구 없소',	'한영애',	'691',	'250');");
		db.execSQL("insert into SongTable15 values(490,	'정서경',	'팬이야',	'자우림',	'9977',	'9063');");
		db.execSQL("insert into SongTable15 values(491,	'정서경',	'Desperado',	'Eagles',	'7344',	'61169');");
		db.execSQL("insert into SongTable15 values(492,	'정서경',	'Calling You',	'Jevetta Steele',	'20047',	'60971');");
		db.execSQL("insert into SongTable15 values(493,	'정서경',	'Hey Jude',	'The Beatles',	'7018',	'2529');");
		db.execSQL("insert into SongTable15 values(494,	'정서경',	'Love Song',	'Sara Bareilles',	'21818',	'정보없음');");
		db.execSQL("insert into SongTable15 values(495,	'정서경',	'죄인',	'이은미',	'32532',	'57907');");
		db.execSQL("insert into SongTable15 values(496,	'정서경',	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable15 values(497,	'정서경',	'Dear',	'매드소울차일드',	'32970',	'47115');");
		db.execSQL("insert into SongTable15 values(498,	'정서경',	'빗속에서',	'이문세',	'8187',	'4649');");
		db.execSQL("insert into SongTable15 values(499,	'정서경',	'You Mean Everything To Me',	'Neil Sedaka',	'7146',	'1100');");
		db.execSQL("insert into SongTable15 values(500,	'정서경',	'Bad Girl Good Girl',	'miss A',	'32773',	'47073');");
		db.execSQL("insert into SongTable15 values(501,	'정서경',	'Sweet Dreams',	'Eurythmics',	'21107',	'8696');");
		db.execSQL("insert into SongTable15 values(502,	'정서경, 김혜리',	'Open Arms',	'Journey',	'7396',	'8160');");
		db.execSQL("insert into SongTable15 values(503,	'정성은',	'Saving All My Love For You',	'Whitney Houston',	'20570',	'2413');");
		db.execSQL("insert into SongTable15 values(504,	'정성은',	'널 붙잡을 노래',	'비',	'32403',	'46980');");
		db.execSQL("insert into SongTable15 values(505,	'정유정',	'1, 2, 3',	'윤하',	'31050',	'46624');");
		db.execSQL("insert into SongTable15 values(506,	'정종한',	'슬픈 부탁',	'먼데이키즈',	'19905',	'83633');");
		db.execSQL("insert into SongTable15 values(507,	'정지원',	'Isn＇t She Lovely',	'Stevie Wonder',	'20887',	'8512');");
		db.execSQL("insert into SongTable15 values(508,	'정지원',	'성인식',	'박지윤',	'8999',	'6493');");
		db.execSQL("insert into SongTable15 values(509,	'정지원',	'꽃밭에서',	'정훈희',	'2960',	'3010');");
		db.execSQL("insert into SongTable15 values(510,	'제네타 김',	'Run The World',	'Beyonce',	'22224',	'84848');");
		db.execSQL("insert into SongTable15 values(511,	'제네타 김',	'위태로운 이야기',	'박정현',	'15970',	'69925');");
		db.execSQL("insert into SongTable15 values(512,	'제네타 김',	'시계태엽',	'임정희',	'15138',	'64838');");
		db.execSQL("insert into SongTable15 values(513,	'제이미 조',	'나는 위험한 사랑을 상상한다',	'김윤아',	'13026',	'68062');");
		db.execSQL("insert into SongTable15 values(514,	'제이미 조',	'야상곡',	'김윤아',	'12902',	'64286');");
		db.execSQL("insert into SongTable15 values(515,	'조경원',	'Breathe',	'G-Dragon',	'31548',	'84431');");
		db.execSQL("insert into SongTable15 values(516,	'조경원',	'응급실',	'Izi',	'14515',	'45117');");
		db.execSQL("insert into SongTable15 values(517,	'조경주',	'A Song For U',	'이지영 (빅마마)',	'정보없음',	'58027');");
		db.execSQL("insert into SongTable15 values(518,	'조경주',	'시계태엽',	'임정희',	'15138',	'64838');");
		db.execSQL("insert into SongTable15 values(519,	'조재우',	'인연 (동녘바람)',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable15 values(520,	'조재우',	'세상 끝에서',	'팀(Tim)',	'정보없음',	'83745');");
		db.execSQL("insert into SongTable15 values(521,	'조재우',	'사랑합니다',	'팀(Tim)',	'11271',	'62988');");
		db.execSQL("insert into SongTable15 values(522,	'차겨울',	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable15 values(523,	'차겨울',	'남자답지 못한 말',	'팀(Tim)',	'33338',	'58103');");
		db.execSQL("insert into SongTable15 values(524,	'차겨울',	'애송이의 사랑',	'양파',	'3675',	'4881');");
		db.execSQL("insert into SongTable15 values(525,	'차겨울',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable15 values(526,	'차여울',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable15 values(527,	'차여울',	'편지할게요',	'박정현',	'8124',	'5837');");
		db.execSQL("insert into SongTable15 values(528,	'최정훈',	'If You',	'앤',	'정보없음',	'68028');");
		db.execSQL("insert into SongTable15 values(529,	'최정훈',	'To Me',	'김범수',	'13447',	'64453');");
		db.execSQL("insert into SongTable15 values(530,	'최정훈',	'마법의 성',	'더 클래식',	'2238',	'3514');");
		db.execSQL("insert into SongTable15 values(531,	'최정훈',	'세 가지 소원',	'이승환',	'8149',	'5924');");
		db.execSQL("insert into SongTable15 values(532,	'최정훈',	'사랑해요',	'김조한',	'10086',	'9101');");
		db.execSQL("insert into SongTable15 values(533,	'최정훈',	'The Rose',	'Bette Midler',	'7100',	'8215');");
		db.execSQL("insert into SongTable15 values(534,	'최정훈',	'What A Wonderful World',	'Louis Armstrong',	'7131',	'1097');");
		db.execSQL("insert into SongTable15 values(535,	'최정훈, 셰인',	'Georgia On My Mind',	'Ray Charles',	'7020',	'8070');");
		db.execSQL("insert into SongTable15 values(536,	'최정훈, 푸니타',	'It＇s Gonna Be Rolling (Feat. 박효신)',	'이소라',	'5737',	'62378');");
		db.execSQL("insert into SongTable15 values(537,	'최형석',	'아프고 아픈 이름',	'앤',	'5895',	'7874');");
		db.execSQL("insert into SongTable15 values(538,	'최형석',	'사랑합니다',	'팀(Tim)',	'11271',	'62988');");
		db.execSQL("insert into SongTable15 values(539,	'최환준',	'너의 뒤에서',	'박진영',	'2526',	'3607');");
		db.execSQL("insert into SongTable15 values(540,	'최환준',	'미인',	'이기찬',	'17135',	'81439');");
		db.execSQL("insert into SongTable15 values(541,	'크리스틴',	'이태원 프리덤',	'UV',	'38864',	'76861');");
		db.execSQL("insert into SongTable15 values(542,	'크리스틴',	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable15 values(543,	'티타라우',	'Impossible',	'Christina Aguilera',	'20739',	'61115');");
		db.execSQL("insert into SongTable15 values(544,	'티타라우',	'Lonely',	'2NE1',	'33904',	'76914');");
		db.execSQL("insert into SongTable15 values(545,	'티타라우',	'Don＇t Cry',	'박봄',	'38912',	'47368');");
		db.execSQL("insert into SongTable15 values(546,	'편난이',	'기억 속으로',	'이은미',	'1612',	'3343');");
		db.execSQL("insert into SongTable15 values(547,	'푸니타',	'Need You Now',	'Lady Antebellum',	'22089',	'84837');");
		db.execSQL("insert into SongTable15 values(548,	'푸니타',	'듣고 있나요',	'이승철',	'30445',	'46479');");
		db.execSQL("insert into SongTable15 values(549,	'푸니타',	'가로수 그늘 아래 서면',	'이문세',	'3',	'1451');");
		db.execSQL("insert into SongTable15 values(550,	'푸니타',	'초대',	'엄정화',	'4777',	'5613');");
		db.execSQL("insert into SongTable15 values(551,	'푸니타',	'I＇m in love',	'라디',	'34106',	'86461');");
		db.execSQL("insert into SongTable15 values(552,	'푸니타',	'인디안 인형처럼',	'나미',	'14',	'645');");
		db.execSQL("insert into SongTable15 values(553,	'푸니타',	'The Power Of Love',	'Jennifer Rush',	'정보없음',	'2055');");
		db.execSQL("insert into SongTable15 values(554,	'푸니타',	'아틀란티스 소녀',	'보아',	'11466',	'9429');");
		db.execSQL("insert into SongTable15 values(555,	'푸니타, 이태권',	'I Will Survive',	'Gloria Gaynor',	'20155',	'8490');");
		db.execSQL("insert into SongTable15 values(556,	'한다성',	'녹턴',	'이은미',	'32718',	'86559');");
		db.execSQL("insert into SongTable15 values(557,	'한다성',	'미인',	'이기찬',	'17135',	'81439');");
		db.execSQL("insert into SongTable15 values(558,	'한다성',	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable15 values(559,	'한다성',	'내 맘이 안 그래',	'이승환',	'18831',	'83219');");
		db.execSQL("insert into SongTable15 values(560,	'한다성',	'기억상실',	'거미',	'13902',	'9888');");
		db.execSQL("insert into SongTable15 values(561,	'한다성',	'믿어지지 않는 얘기',	'조규찬',	'4054',	'5137');");
		db.execSQL("insert into SongTable15 values(562,	'한성욱',	'서울의 달',	'김건모',	'14980',	'45246');");
		db.execSQL("insert into SongTable15 values(563,	'한예슬',	'골목길',	'신촌블루스',	'1196',	'141');");
		db.execSQL("insert into SongTable15 values(564,	'합동무대',	'Trouble Maker',	'트러블메이커(현아, 장현승)',	'34717',	'77110');");
		db.execSQL("insert into SongTable15 values(565,	'허선화',	'바보',	'박효신',	'8868',	'6328');");
		db.execSQL("insert into SongTable15 values(566,	'허윤영',	'소원',	'지영선',	'6050',	'6926');");
		db.execSQL("insert into SongTable15 values(567,	'현진주',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable15 values(568,	'현진주',	'엄마',	'라디',	'36202',	'87031');");
		db.execSQL("insert into SongTable15 values(569,	'현진주',	'점점',	'브라운아이즈',	'10429',	'9170');");
		db.execSQL("insert into SongTable15 values(570,	'홍동균',	'Fly Me To The Moon',	'Frank Sinatra',	'21420',	'정보없음');");
		db.execSQL("insert into SongTable15 values(571,	'홍동균',	'Put Your Records On',	'Corinne Bailey Rae',	'21559',	'정보없음');");
		db.execSQL("insert into SongTable15 values(572,	'홍동균',	'사랑하나요',	'이승환',	'4191',	'7810');");
		db.execSQL("insert into SongTable15 values(573,	'홍동균',	'하늘을 날아',	'메이트',	'31808',	'84482');");
		db.execSQL("insert into SongTable15 values(574,	'홍동균',	'Rock U',	'카라',	'19935',	'46374');");
		db.execSQL("insert into SongTable15 values(575,	'홍동균',	'영일만 친구',	'최백호',	'1269',	'940');");
		db.execSQL("insert into SongTable15 values(576,	'홍동균',	'My Way',	'Frank Sinatra',	'7004',	'1080');");
		db.execSQL("insert into SongTable15 values(577,	'50kg',	'삐에로는 우릴 보고 웃지',	'김완선',	'813',	'431');");
		db.execSQL("insert into SongTable15 values(578,	'50kg',	'Goodbye Baby',	'miss A',	'34182',	'47443');");
		db.execSQL("insert into SongTable15 values(579,	'50kg',	'Friday Night',	'god',	'8784',	'6272');");
		db.execSQL("insert into SongTable15 values(580,	'50kg',	'점점',	'브라운아이즈',	'10429',	'9170');");
		db.execSQL("insert into SongTable15 values(581,	'50kg',	'Let＇s Get It Started',	'Black Eyed Peas',	'21332',	'61944');");
		db.execSQL("insert into SongTable15 values(582,	'50kg',	'청혼',	'노을',	'13319',	'68248');");
		db.execSQL("insert into SongTable15 values(583,	'50kg',	'사랑에 빠지고 싶다',	'김조한',	'34630',	'47568');");
		db.execSQL("insert into SongTable15 values(584,	'50kg',	'노란 샤쓰의 사나이',	'한명숙',	'240',	'243');");
		db.execSQL("insert into SongTable15 values(585,	'50kg',	'지금 이 순간 (지킬 앤 하이드 O.S.T)',	'조승우',	'16364',	'64801');");
		db.execSQL("insert into SongTable15 values(586,	'50kg',	'사랑 사랑 사랑',	'FT아일랜드',	'32987',	'47128');");
		db.execSQL("insert into SongTable15 values(587,	'50kg',	'아니 벌써',	'산울림',	'1991',	'3105');");
		db.execSQL("insert into SongTable15 values(588,	'50kg',	'세상에 뿌려진 사랑만큼',	'이승환',	'1324',	'1206');");
		db.execSQL("insert into SongTable15 values(589,	'50kg',	'오늘도 난',	'이승철',	'3495',	'4813');");
		db.execSQL("insert into SongTable15 values(590,	'50kg, 윤일상',	'오늘 그댈 사랑합니다',	'JK김동욱',	'32711',	'86596');");
		db.execSQL("insert into SongTable15 values(591,	'50kg, 장은정',	'Swing Baby',	'박진영',	'9564',	'6924');");
		db.execSQL("insert into SongTable15 values(592,	'50kg, 구자명, 에릭 남, 장성재',	'님과 함께',	'남진',	'411',	'648');");
		db.execSQL("insert into SongTable15 values(593,	'50kg, 김태극, 에릭 남, 최정훈',	'Blue',	'빅뱅',	'35035',	'47665');");
		db.execSQL("insert into SongTable15 values(594,	'Top 10',	'Butterfly (국가대표 O.S.T)',	'러브홀릭스',	'30528',	'46517');");
		db.execSQL("insert into SongTable15 values(595,	'Top 12',	'행복',	'H.O.T',	'3950',	'5037');");
		db.execSQL("insert into SongTable15 values(596,	'구현모',	'The Last Time',	'Eric Benet',	'21747',	'61229');");
		db.execSQL("insert into SongTable15 values(597,	'김예은',	'헤어져야 사랑을 알죠',	'리사',	'16047',	'69986');");
		db.execSQL("insert into SongTable15 values(598,	'나경원',	'주문',	'동방신기',	'30189',	'46439');");
		db.execSQL("insert into SongTable15 values(599,	'나경원',	'Hit The Road Jack',	'Ray Charles',	'정보없음',	'8458');");
		db.execSQL("insert into SongTable15 values(600,	'나경원',	'소녀시대',	'이승철',	'1300',	'501');");
		db.execSQL("insert into SongTable15 values(601,	'남주희',	'박하사탕',	'윤도현밴드(YB)',	'9699',	'7640');");
		db.execSQL("insert into SongTable15 values(602,	'남주희',	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable15 values(603,	'레빗츄',	'Bounce',	'JJ프로젝트',	'35389',	'77296');");
		db.execSQL("insert into SongTable15 values(604,	'레빗츄',	'헤어지지 못하는 여자, 떠나가지 못하는 남자',	'리쌍',	'31718',	'84505');");
		db.execSQL("insert into SongTable15 values(605,	'미키 림',	'Get It In',	'T',	'34582',	'58427');");
		db.execSQL("insert into SongTable15 values(606,	'미키 림',	'남자라서',	'거미',	'32546',	'47009');");
		db.execSQL("insert into SongTable15 values(607,	'박동진',	'Toxic',	'Britney Spears',	'20947',	'61323');");
		db.execSQL("insert into SongTable15 values(608,	'박동진',	'점핑',	'카라',	'33276',	'76737');");
		db.execSQL("insert into SongTable15 values(609,	'박수진',	'이별여행',	'원미연',	'391',	'634');");
		db.execSQL("insert into SongTable15 values(610,	'박수진',	'Twinkle',	'태티서',	'35315',	'47731');");
		db.execSQL("insert into SongTable15 values(611,	'박수진',	'총 맞은 것처럼',	'백지영',	'30425',	'83914');");
		db.execSQL("insert into SongTable15 values(612,	'박수진',	'Halo',	'Beyonce',	'20899',	'63158');");
		db.execSQL("insert into SongTable15 values(613,	'박수진',	'기억 속의 먼 그대에게',	'박미경',	'3671',	'4891');");
		db.execSQL("insert into SongTable15 values(614,	'박수진',	'Empire State Of Mind',	'Jay-Z',	'22024',	'84949');");
		db.execSQL("insert into SongTable15 values(615,	'박수진',	'있다 없으니까',	'씨스타19',	'36397',	'58875');");
		db.execSQL("insert into SongTable15 values(616,	'박수진, 빅스타',	'말해줘',	'지누션',	'3994',	'5060');");
		db.execSQL("insert into SongTable15 values(617,	'박우철',	'거리에서',	'동물원',	'1332',	'1326');");
		db.execSQL("insert into SongTable15 values(618,	'박우철',	'This Love',	'Maroon 5',	'21040',	'61394');");
		db.execSQL("insert into SongTable15 values(619,	'박우철',	'누난 너무 예뻐',	'샤이니',	'19646',	'46317');");
		db.execSQL("insert into SongTable15 values(620,	'박하늘',	'Fantastic Baby',	'빅뱅',	'35073',	'77196');");
		db.execSQL("insert into SongTable15 values(621,	'박하늘',	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable15 values(622,	'서지은',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable15 values(623,	'성현주',	'Don＇t Know Why',	'Norah Jones',	'20608',	'61137');");
		db.execSQL("insert into SongTable15 values(624,	'성현주',	'아시나요',	'조성모',	'9054',	'6512');");
		db.execSQL("insert into SongTable15 values(625,	'소울슈프림',	'First Kiss',	'포맨',	'30250',	'83831');");
		db.execSQL("insert into SongTable15 values(626,	'소울슈프림',	'니가 있어야 할 곳',	'god',	'9769',	'7722');");
		db.execSQL("insert into SongTable15 values(627,	'소울슈프림',	'안녕 사랑아',	'박효신',	'33075',	'47142');");
		db.execSQL("insert into SongTable15 values(628,	'손미래',	'Stand Up For Love',	'Destiny＇s Child',	'21362',	'61934');");
		db.execSQL("insert into SongTable15 values(629,	'신미애',	'내가 고백을 하면 깜짝 놀랄거야',	'산울림',	'8163',	'63779');");
		db.execSQL("insert into SongTable15 values(630,	'신우영',	'Rolling In The Deep',	'Adele',	'22213',	'79017');");
		db.execSQL("insert into SongTable15 values(631,	'신우영',	'Super Star',	'효린, 지연, 에일리',	'35028',	'정보없음');");
		db.execSQL("insert into SongTable15 values(632,	'신진수',	'죽을 것만 같아',	'환희',	'34233',	'47461');");
		db.execSQL("insert into SongTable15 values(633,	'안재만',	'Billie Jean',	'Michael Jackson',	'7013',	'8027');");
		db.execSQL("insert into SongTable15 values(634,	'양동선',	'좋아보여',	'버벌진트',	'34349',	'77024');");
		db.execSQL("insert into SongTable15 values(635,	'양동선',	'난치병',	'하림',	'5865',	'9018');");
		db.execSQL("insert into SongTable15 values(636,	'양성애',	'난 행복해',	'이소라',	'2742',	'3924');");
		db.execSQL("insert into SongTable15 values(637,	'양성애',	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable15 values(638,	'여일밴드',	'옛 사랑',	'이문세',	'916',	'1364');");
		db.execSQL("insert into SongTable15 values(639,	'오병길',	'1994년 어느 늦은 밤',	'장혜진',	'2432',	'3641');");
		db.execSQL("insert into SongTable15 values(640,	'오병길',	'담배가게 아가씨',	'송창식',	'2682',	'2234');");
		db.execSQL("insert into SongTable15 values(641,	'오병길',	'미인',	'신중현과 엽전들',	'1555',	'2372');");
		db.execSQL("insert into SongTable15 values(642,	'오병길',	'Closer',	'Ne-Yo',	'22189',	'60165');");
		db.execSQL("insert into SongTable15 values(643,	'오병길',	'그리움만 쌓이네',	'노영심',	'2618',	'3782');");
		db.execSQL("insert into SongTable15 values(644,	'오병길',	'미안해요',	'김건모',	'9520',	'6853');");
		db.execSQL("insert into SongTable15 values(645,	'오병길, 김연우',	'사랑과 우정 사이',	'피노키오',	'1611',	'2797');");
		db.execSQL("insert into SongTable15 values(646,	'오병길, 한영애',	'누구 없소',	'한영애',	'691',	'250');");
		db.execSQL("insert into SongTable15 values(647,	'이지혜',	'악몽',	'버블시스터즈',	'10930',	'62991');");
		db.execSQL("insert into SongTable15 values(648,	'이형은',	'I Want You Back',	'The Jackson 5',	'22169',	'60833');");
		db.execSQL("insert into SongTable15 values(649,	'이형은',	'잠 못 드는 밤 비는 내리고',	'김건모',	'1117',	'1509');");
		db.execSQL("insert into SongTable15 values(650,	'이형은',	'Someone Like You',	'Adele',	'22204',	'79035');");
		db.execSQL("insert into SongTable15 values(651,	'이형은',	'단발머리',	'조용필',	'635',	'1461');");
		db.execSQL("insert into SongTable15 values(652,	'이형은, 김소현',	'Over The Rainbow',	'Judy Garland',	'20296',	'60605');");
		db.execSQL("insert into SongTable15 values(653,	'임철',	'경고',	'타샤니',	'8548',	'6194');");
		db.execSQL("insert into SongTable15 values(654,	'장원석',	'청소',	'더 레이(The Ray)',	'16202',	'45642');");
		db.execSQL("insert into SongTable15 values(655,	'장원석',	'사랑합니다',	'팀(Tim)',	'11271',	'62988');");
		db.execSQL("insert into SongTable15 values(656,	'정영윤',	'Big Girl',	'MIKA',	'21789',	'60189');");
		db.execSQL("insert into SongTable15 values(657,	'정진철',	'그대의 향기',	'유영진',	'2011',	'3804');");
		db.execSQL("insert into SongTable15 values(658,	'정진철',	'For Once In My Life',	'Stevie Wonder',	'정보없음',	'8424');");
		db.execSQL("insert into SongTable15 values(659,	'정진철',	'아버지',	'인순이',	'31200',	'86308');");
		db.execSQL("insert into SongTable15 values(660,	'조선영',	'And I Am Telling You I＇m Not Going',	'Jennifer Hudson',	'21742',	'60416');");
		db.execSQL("insert into SongTable15 values(661,	'최윤호',	'Over You',	'Daughtry',	'21896',	'60170');");
		db.execSQL("insert into SongTable15 values(662,	'추상길',	'기억상실',	'거미',	'13902',	'9888');");
		db.execSQL("insert into SongTable15 values(663,	'한기란',	'마론인형',	'자우림',	'4537',	'62823');");
		db.execSQL("insert into SongTable15 values(664,	'한기란',	'Run To You',	'Whitney Houston',	'7717',	'61042');");
		db.execSQL("insert into SongTable15 values(665,	'한기란',	'시간을 거슬러',	'린',	'34911',	'58512');");
		db.execSQL("insert into SongTable15 values(666,	'한동근',	'사랑.. 그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable15 values(667,	'한동근',	'Desperado',	'Eagles',	'7344',	'61169');");
		db.execSQL("insert into SongTable15 values(668,	'한동근',	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable15 values(669,	'한동근',	'Autumn Leaves',	'Eric Clapton',	'22148',	'정보없음');");
		db.execSQL("insert into SongTable15 values(670,	'한동근',	'기다리다',	'패닉',	'8707',	'7098');");
		db.execSQL("insert into SongTable15 values(671,	'한동근',	'Let It Be',	'The Beatles',	'7049',	'1075');");
		db.execSQL("insert into SongTable15 values(672,	'한동근',	'비상',	'임재범',	'9429',	'7153');");
		db.execSQL("insert into SongTable15 values(673,	'한동근',	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable15 values(674,	'한동근',	'You Give Love A Bad Name',	'Bon Jovi',	'7790',	'8251');");
		db.execSQL("insert into SongTable15 values(675,	'한동근, 김태원',	'Lonely Night',	'부활',	'4001',	'5087');");
		db.execSQL("insert into SongTable15 values(676,	'한동근, 소향',	'My Heart Will Go On',	'Celine Dion',	'7736',	'5492');");
		db.execSQL("insert into SongTable15 values(677,	'황샛별',	'Sweet Dreams',	'Beyonce',	'정보없음',	'84905');");
		db.execSQL("insert into SongTable15 values(678,	'황샛별',	'Can＇t Nobody',	'2NE1',	'33058',	'47146');");
		db.execSQL("insert into SongTable15 values(679,	'황선화',	'잠시 길을 잃다',	'015B',	'19300',	'86315');");
	}				

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable15");
		onCreate(db);
	}
}