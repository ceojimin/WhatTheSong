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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rainbowsoft.what_the_song.R;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongBookmark;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongMyDBHelper;

public class WhatTheSong10 extends ListActivity implements OnClickListener {
	private static final int DATABASE_VERSION = 2;
	private static final int LISTSIZE = 30;
	//private AdView adView = null;
	private static final String LOGTAG = "BannerTypeJava";
	ImageView My_list, Refresh, manual;
	SQLiteDatabase db, mdb;
	Map<String, String> data;
	List<Map<String, String>> myList;
	SimpleAdapter adapter;
	Random rand = new Random();
	int count = 0;
	int[] randid = new int[LISTSIZE];

	Cursor cursor;
	public static String searchUrl = "http://m.youtube.com/#/results?q=";

	String[] song = new String[LISTSIZE];
	String[] singer = new String[LISTSIZE];
	String[] tj = new String[LISTSIZE];
	String[] ky = new String[LISTSIZE];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.what_the_song_10);

		//initAdam();

		My_list = (ImageView) findViewById(R.id.my_list);
		My_list.setOnClickListener(this);
		Refresh = (ImageView) findViewById(R.id.refresh);
		Refresh.setOnClickListener(this);
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_10",
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

	public void callList() {
		int[] randid = new int[LISTSIZE];
		int count = 0;
		myList = new ArrayList<Map<String, String>>();

		WhatTheSongDBHelper10 wtsHelper10 = new WhatTheSongDBHelper10(this, "whatthesong10.db", null, DATABASE_VERSION);
		db = wtsHelper10.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable10 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable10 where id= '"
					+ randid[i] + "'", null);
			cursor.moveToFirst();

			song[i] = cursor.getString(1);
			singer[i] = cursor.getString(2);
			tj[i] = cursor.getString(3);
			ky[i] = cursor.getString(4);

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
		new AlertDialog.Builder(WhatTheSong10.this)
				.setTitle(song[p]+ " - " + singer[p])
				.setIcon(R.drawable.icon_small)
				.setMessage("♪ 태진 : " + tj[p] + ", ♪ 금영 : " + ky[p])
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
											WhatTheSong10.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong10.this,
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
												+ song[p] + " - " + singer[p]));
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
			Intent newActivity = new Intent(WhatTheSong10.this,
					WhatTheSongBookmark.class);
			WhatTheSong10.this.startActivity(newActivity);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (v.getId() == R.id.refresh) {
			db.close();
			callList();
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
					"m_pref_10", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper10 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper10(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '축하, 감사, 기념 테마' DB (130627 : 46개)
		db.execSQL("CREATE TABLE SongTable10 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable10 values(1,	'1991年, 찬 바람이 불던 밤…',	'박효신',	'17734', '81654'	);");
		db.execSQL("insert into SongTable10 values(2,	'23번째 생일',	'김연우',	'8627', '정보없음'	);");
		db.execSQL("insert into SongTable10 values(3,	'Congratulations',	'Cliff Richard',	'7301', '61345'	);");
		db.execSQL("insert into SongTable10 values(4,	'Dear. Mom',	'소녀시대',	'30778', '84101'	);");
		db.execSQL("insert into SongTable10 values(5,	'Happy Birthday',	'Stevie Wonder',	'정보없음', '8443'	);");
		db.execSQL("insert into SongTable10 values(6,	'Happy Birthday',	'코요태',	'정보없음', '62441'	);");
		db.execSQL("insert into SongTable10 values(7,	'Happy Birthday To Me',	'딜라이트',	'31568', '정보없음'	);");
		db.execSQL("insert into SongTable10 values(8,	'Happy Birthday To You',	'권진원',	'8225', '5929'	);");
		db.execSQL("insert into SongTable10 values(9,	'Happy Birthday To You',	'터보',	'5114', '62838'	);");
		db.execSQL("insert into SongTable10 values(10,	'Mother',	'플라워',	'4316', '7421'	);");
		db.execSQL("insert into SongTable10 values(11,	'My World',	'다이나믹듀오',	'14528', '정보없음'	);");
		db.execSQL("insert into SongTable10 values(12,	'Sorry (Dear. Daddy)',	'f(x)',	'32753', '정보없음'	);");
		db.execSQL("insert into SongTable10 values(13,	'To Sir With Love',	'Lulu',	'7040', '2421'	);");
		db.execSQL("insert into SongTable10 values(14,	'가족',	'이승환',	'3678', '4888'	);");
		db.execSQL("insert into SongTable10 values(15,	'겨울아이',	'수지',	'33565', '86787'	);");
		db.execSQL("insert into SongTable10 values(16,	'그녀의 생일',	'뱅크',	'정보없음', '5659'	);");
		db.execSQL("insert into SongTable10 values(17,	'너의 생일',	'듀스',	'8765', '66590'	);");
		db.execSQL("insert into SongTable10 values(18,	'널 위한 멜로디 (결혼 축가)',	'M4',	'32294', '84821'	);");
		db.execSQL("insert into SongTable10 values(19,	'독도는 우리 땅',	'정광태',	'516', '286'	);");
		db.execSQL("insert into SongTable10 values(20,	'미역국',	'팬텀',	'35687', '87380'	);");
		db.execSQL("insert into SongTable10 values(21,	'생일',	'하이봐',	'15924', '45556'	);");
		db.execSQL("insert into SongTable10 values(22,	'생일 축하합니다',	'축하곡',	'5111', '2487'	);");
		db.execSQL("insert into SongTable10 values(23,	'스승의 은혜',	'의식곡',	'정보없음', '5330'	);");
		db.execSQL("insert into SongTable10 values(24,	'아버지',	'인순이',	'31200', '86308'	);");
		db.execSQL("insert into SongTable10 values(25,	'아버지',	'데프콘',	'18610', '83158'	);");
		db.execSQL("insert into SongTable10 values(26,	'아버지',	'김경호',	'11826', '9505'	);");
		db.execSQL("insert into SongTable10 values(27,	'아버지',	'싸이',	'15174', '69562'	);");
		db.execSQL("insert into SongTable10 values(28,	'아버지',	'김종서',	'19899', '46377'	);");
		db.execSQL("insert into SongTable10 values(29,	'아버지 구두',	'SG워너비',	'17903', '81781'	);");
		db.execSQL("insert into SongTable10 values(30,	'아빠! 힘내세요',	'동요',	'정보없음', '68587'	);");
		db.execSQL("insert into SongTable10 values(31,	'아빠의 청춘',	'오기택',	'599', '533'	);");
		db.execSQL("insert into SongTable10 values(32,	'어머님께',	'god',	'4988', '5783'	);");
		db.execSQL("insert into SongTable10 values(33,	'엄마의 일기',	'왁스',	'9260', '6657'	);");
		db.execSQL("insert into SongTable10 values(34,	'오락실',	'한스밴드',	'4939', '5757'	);");
		db.execSQL("insert into SongTable10 values(35,	'우리집',	'MC 스나이퍼',	'정보없음', '81921'	);");
		db.execSQL("insert into SongTable10 values(36,	'청혼 (결혼 축가)',	'노을',	'13319', '68248'	);");
		db.execSQL("insert into SongTable10 values(37,	'축복합니다',	'들국화',	'2884', '2781'	);");
		db.execSQL("insert into SongTable10 values(38,	'축하해',	'프라이머리',	'36032', '87434'	);");
		db.execSQL("insert into SongTable10 values(39,	'축하해 생일',	'버벌진트',	'35637', '77331'	);");
		db.execSQL("insert into SongTable10 values(40,	'축하해요',	'푸른하늘',	'8186', '정보없음'	);");
		db.execSQL("insert into SongTable10 values(41,	'현관을 열면',	'배치기',	'16736', '81278'	);");
		db.execSQL("insert into SongTable10 values(42,	'MaMa',	'바비킴',	'30679', '86057'	);");
		db.execSQL("insert into SongTable10 values(43,	'아내에게 바치는 노래',	'하수영',	'3140', '520'	);");
		db.execSQL("insert into SongTable10 values(44,	'친구',	'안재욱',	'11030', '9321'	);");
		db.execSQL("insert into SongTable10 values(45,	'친구야 친구',	'박상규',	'1157',	'983');");
		db.execSQL("insert into SongTable10 values(46,	'홍시',	'나훈아',	'15263',	'69605');");
	}			
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable10");
		onCreate(db);
	}
}