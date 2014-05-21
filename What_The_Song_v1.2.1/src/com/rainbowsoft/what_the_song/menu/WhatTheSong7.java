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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rainbowsoft.what_the_song.R;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongBookmark;
import com.rainbowsoft.what_the_song.bookmark.WhatTheSongMyDBHelper;

public class WhatTheSong7 extends ListActivity implements OnClickListener {
	private static final int LISTSIZE = 29;
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
		setContentView(R.layout.what_the_song_7);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_7",
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
	
	public void callList(){
		int[] randid = new int[LISTSIZE];
		int count = 0;
		myList = new ArrayList<Map<String, String>>();

		WhatTheSongDBHelper7 wtsHelper7 = new WhatTheSongDBHelper7(this);
		db = wtsHelper7.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable7 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable7 where id= '"
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
		new AlertDialog.Builder(WhatTheSong7.this)
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
											WhatTheSong7.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong7.this,
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
			Intent newActivity = new Intent(WhatTheSong7.this,
					WhatTheSongBookmark.class);
			WhatTheSong7.this.startActivity(newActivity);
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
					"m_pref_7", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper7 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper7(Context context) {
		super(context, "whatthesong7.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '클럽 테마' DB (130627 : 29개)
		db.execSQL("CREATE TABLE SongTable7 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable7 values(1,	'Birthday Sex',	'Jeremih',	'21973', '정보없음'	);");
		db.execSQL("insert into SongTable7 values(2,	'Boom Boom Pow',	'Black Eyed Peas',	'20898', '63151'	);");
		db.execSQL("insert into SongTable7 values(3,	'Cupid Suffle',	'Cupid',	'정보없음', '60149'	);");
		db.execSQL("insert into SongTable7 values(4,	'Don＇t Stop The Party',	'Pitbull',	'22433', '정보없음'	);");
		db.execSQL("insert into SongTable7 values(5,	'Empire State Of Mind',	'Jay-Z',	'22024', '정보없음'	);");
		db.execSQL("insert into SongTable7 values(6,	'Give Me Everything',	'Pitbull',	'22226', '79003'	);");
		db.execSQL("insert into SongTable7 values(7,	'Hangover',	'Taio Cruz',	'22364', '정보없음'	);");
		db.execSQL("insert into SongTable7 values(8,	'Hotel Room Service',	'Pitbull',	'21998', '정보없음'	);");
		db.execSQL("insert into SongTable7 values(9,	'In Da Club',	'50 Cent',	'20677', '61147'	);");
		db.execSQL("insert into SongTable7 values(10,	'International Love',	'Pitbull',	'22271', '79021'	);");
		db.execSQL("insert into SongTable7 values(11,	'La La La',	'Da＇ Zoo',	'22386', '79119'	);");
		db.execSQL("insert into SongTable7 values(12,	'Like A G6',	'Far East Movement',	'22140', '84855'	);");
		db.execSQL("insert into SongTable7 values(13,	'Livin＇ My Love',	'LMFAO, Steve Aoki',	'22309', '79080'	);");
		db.execSQL("insert into SongTable7 values(14,	'Low',	'Flo Rida',	'21835', '60991'	);");
		db.execSQL("insert into SongTable7 values(15,	'My First Kiss (Feat. Ke$ha)',	'3OH!3',	'22108', '84954'	);");
		db.execSQL("insert into SongTable7 values(16,	'One More Night',	'Maroon 5',	'22374', '79115'	);");
		db.execSQL("insert into SongTable7 values(17,	'Party Rock Anthem (Feat. Lauren Bennett & Goon Rock)',	'LMFAO',	'22232', '79013'	);");
		db.execSQL("insert into SongTable7 values(18,	'Put Your Hands Up',	'Benny Benassi',	'21547', '정보없음'	);");
		db.execSQL("insert into SongTable7 values(19,	'Rocketeer',	'Far East Movement',	'22190', '84863'	);");
		db.execSQL("insert into SongTable7 values(20,	'S & M',	'Rihanna',	'22197', '84846'	);");
		db.execSQL("insert into SongTable7 values(21,	'Sex Appeal',	'Bueno Clinic',	'22359', '79082'	);");
		db.execSQL("insert into SongTable7 values(22,	'Sexy And I Know It',	'LMFAO',	'22274', '79032'	);");
		db.execSQL("insert into SongTable7 values(23,	'Shots',	'LMFAO',	'22252', '84834'	);");
		db.execSQL("insert into SongTable7 values(24,	'Shut Up And Let me Go',	'The Ting Tings',	'22277', '63167'	);");
		db.execSQL("insert into SongTable7 values(25,	'Sorry For Party Rocking',	'LMFAO',	'22314', '79024'	);");
		db.execSQL("insert into SongTable7 values(26,	'Tchu Tchu Tcha',	'Pitbull',	'정보없음', '79188'	);");
		db.execSQL("insert into SongTable7 values(27,	'The Time (Dirty Bit)',	'Black Eyed Peas',	'정보없음', '84883'	);");
		db.execSQL("insert into SongTable7 values(28,	'Tik Tok',	'Ke$ha',	'22047', '63691'	);");
		db.execSQL("insert into SongTable7 values(29,	'We No Speak Americano',	'Yolanda Be Cool, DCUP',	'22254', '84850'	);");		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable7");
		onCreate(db);
	}
}