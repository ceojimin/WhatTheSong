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

public class WhatTheSong4 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_4);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_4",
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

		WhatTheSongDBHelper4 wtsHelper4 = new WhatTheSongDBHelper4(this, "whatthesong4.db", null, DATABASE_VERSION);
		db = wtsHelper4.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable4 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable4 where id= '"
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
		new AlertDialog.Builder(WhatTheSong4.this)
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
											WhatTheSong4.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong4.this,
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
			Intent newActivity = new Intent(WhatTheSong4.this,
					WhatTheSongBookmark.class);
			WhatTheSong4.this.startActivity(newActivity);
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
					"m_pref_4", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper4 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper4(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '유혹 테마' DB (130630 : 47개)
		db.execSQL("CREATE TABLE SongTable4 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable4 values(1,	' 한 사람',	'채연',	'18261',	'81749');");
		db.execSQL("insert into SongTable4 values(2,	'10Minutes',	'이효리',	'11833',	'9504');");
		db.execSQL("insert into SongTable4 values(3,	'2HOT',	'지나',	'35395',	'47754');");
		db.execSQL("insert into SongTable4 values(4,	'A-Ha',	'아이비',	'15291',	'64880');");
		db.execSQL("insert into SongTable4 values(5,	'Apple Is A',	'티아라',	'31969',	'84667');");
		db.execSQL("insert into SongTable4 values(6,	'Call Me Maybe',	'Carly Rae Jepsen',	'22368',	'79101');");
		db.execSQL("insert into SongTable4 values(7,	'Call You Mind',	'Jeff Bernat',	'22443',	'정보없음');");
		db.execSQL("insert into SongTable4 values(8,	'Careless Whisper',	'Wham!',	'7154',	'3099');");
		db.execSQL("insert into SongTable4 values(9,	'Chantey Chantey(샨티샨티)',	'선하',	'19190',	'46201');");
		db.execSQL("insert into SongTable4 values(10,	'Come 2 Me',	'엄정화',	'16551',	'45740');");
		db.execSQL("insert into SongTable4 values(11,	'Cupido',	'아이비',	'17349',	'81624');");
		db.execSQL("insert into SongTable4 values(12,	'Honey',	'박진영',	'4326',	'5243');");
		db.execSQL("insert into SongTable4 values(13,	'I Need A Girl',	'태양',	'32772',	'47070');");
		db.execSQL("insert into SongTable4 values(14,	'Let Me Dance',	'렉시',	'12110',	'9561');");
		db.execSQL("insert into SongTable4 values(15,	'Question',	'하유선',	'14723',	'68992');");
		db.execSQL("insert into SongTable4 values(16,	'Shall We Dance',	'핸섬피플',	'33713',	'47326');");
		db.execSQL("insert into SongTable4 values(17,	'Spark',	'보아',	'13453',	'68306');");
		db.execSQL("insert into SongTable4 values(18,	'Stop',	'Sam Brown',	'20019',	'60004');");
		db.execSQL("insert into SongTable4 values(19,	'Swing Baby',	'박진영',	'9564',	'6924');");
		db.execSQL("insert into SongTable4 values(20,	'TV를 껐네',	'리쌍',	'34305',	'47481');");
		db.execSQL("insert into SongTable4 values(21,	'넌 내게 반했어',	'노브레인',	'14357',	'45070');");
		db.execSQL("insert into SongTable4 values(22,	'누나의 꿈',	'현영',	'15783',	'45509');");
		db.execSQL("insert into SongTable4 values(23,	'다가와',	'채연',	'14727',	'64699');");
		db.execSQL("insert into SongTable4 values(24,	'다가와',	'슬로우 잼',	'14829',	'45196');");
		db.execSQL("insert into SongTable4 values(25,	'당돌한 여자',	'서주경',	'3665',	'2887');");
		db.execSQL("insert into SongTable4 values(26,	'미스터',	'카라',	'31473',	'84409');");
		db.execSQL("insert into SongTable4 values(27,	'성인식',	'박지윤',	'8999',	'6493');");
		db.execSQL("insert into SongTable4 values(28,	'섹시한 남자',	'스페이스 A',	'8485',	'6068');");
		db.execSQL("insert into SongTable4 values(29,	'아이스크림',	'현아',	'35986',	'47898');");
		db.execSQL("insert into SongTable4 values(30,	'연애혁명',	'현영',	'17965',	'45947');");
		db.execSQL("insert into SongTable4 values(31,	'오늘밤은 어둠이 무서워요',	'10cm',	'33231',	'86529');");
		db.execSQL("insert into SongTable4 values(32,	'오빠',	'왁스',	'9300',	'6708');");
		db.execSQL("insert into SongTable4 values(33,	'유혹',	'이재영',	'966',	'627');");
		db.execSQL("insert into SongTable4 values(34,	'유혹',	'성은',	'14924',	'45223');");
		db.execSQL("insert into SongTable4 values(35,	'유혹의 소나타',	'아이비',	'17180',	'81467');");
		db.execSQL("insert into SongTable4 values(36,	'전화번호',	'지누션',	'14233',	'45045');");
		db.execSQL("insert into SongTable4 values(37,	'죽을래사귈래',	'센치한하하',	'34130',	'58316');");
		db.execSQL("insert into SongTable4 values(38,	'집에 가지마',	'GD&TOP',	'33460',	'58136');");
		db.execSQL("insert into SongTable4 values(39,	'초대',	'엄정화',	'4777',	'5613');");
		db.execSQL("insert into SongTable4 values(40,	'타인',	'영턱스클럽',	'3885',	'5014');");
		db.execSQL("insert into SongTable4 values(41,	'유혹',	'샤키',	'4977',	'5760');");
		db.execSQL("insert into SongTable4 values(42,	'Don＇t Cha',	'Pussycat Dolls',	'21405',	'62088');");
		db.execSQL("insert into SongTable4 values(43,	'Stuck',	'Stacie Orrico',	'20749',	'61247');");
		db.execSQL("insert into SongTable4 values(44,	'Lose My Breath',	'Destiny＇s Child',	'21167',	'61535');");
		db.execSQL("insert into SongTable4 values(45,	'Toxic',	'Britney Spears',	'20947',	'61323');");
		db.execSQL("insert into SongTable4 values(46,	'Womanizer',	'Britney Spears',	'21927',	'61913');");
		db.execSQL("insert into SongTable4 values(47,	'둘이서',	'채연',	'14373',	'45074');");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable4");
		onCreate(db);
	}
}