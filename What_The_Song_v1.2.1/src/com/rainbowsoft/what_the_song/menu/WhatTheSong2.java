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

public class WhatTheSong2 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_2);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_2",
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

		WhatTheSongDBHelper2 wtsHelper2 = new WhatTheSongDBHelper2(this, "whatthesong2.db", null, DATABASE_VERSION);
		db = wtsHelper2.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable2 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable2 where id= '"
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
		new AlertDialog.Builder(WhatTheSong2.this)
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
											WhatTheSong2.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong2.this,
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
			Intent newActivity = new Intent(WhatTheSong2.this,
					WhatTheSongBookmark.class);
			WhatTheSong2.this.startActivity(newActivity);
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
					"m_pref_2", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper2 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper2(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '고백 테마' DB (130630 : 136개)
		db.execSQL("CREATE TABLE SongTable2 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable2 values(1,	'10월의 어느 멋진 날에',	'김동규',	'15576',	'68526');");
		db.execSQL("insert into SongTable2 values(2,	'all for you',	'서인국',	'35774',	'77372');");
		db.execSQL("insert into SongTable2 values(3,	'baby',	'Justin bieber',	'22073',	'84915');");
		db.execSQL("insert into SongTable2 values(4,	'Baby Baby',	'포맨',	'30354',	'83866');");
		db.execSQL("insert into SongTable2 values(5,	'Cosmic Girl',	'김태우',	'36471',	'48025');");
		db.execSQL("insert into SongTable2 values(6,	'I Think I',	'별',	'13612',	'68426');");
		db.execSQL("insert into SongTable2 values(7,	'I＇m in love',	'라디',	'34106',	'86461');");
		db.execSQL("insert into SongTable2 values(8,	'Love',	'조장혁',	'9165',	'6540');");
		db.execSQL("insert into SongTable2 values(9,	'marry you',	'Bruno mars',	'22329',	'79064');");
		db.execSQL("insert into SongTable2 values(10,	'maybe',	'선예',	'33518',	'86774');");
		db.execSQL("insert into SongTable2 values(11,	'Nothing Better',	'브라운아이드소울',	'18981',	'85825');");
		db.execSQL("insert into SongTable2 values(12,	'nothing better',	'정엽',	'33010',	'86615');");
		db.execSQL("insert into SongTable2 values(13,	'Oh!',	'소녀시대',	'32153',	'46920');");
		db.execSQL("insert into SongTable2 values(14,	'Paradise',	'T-max',	'30645',	'84041');");
		db.execSQL("insert into SongTable2 values(15,	'payphone',	'Marron5',	'22348',	'79100');");
		db.execSQL("insert into SongTable2 values(16,	'Shock',	'비스트',	'32290',	'46953');");
		db.execSQL("insert into SongTable2 values(17,	'Twinkle',	'태티서',	'35315',	'47731');");
		db.execSQL("insert into SongTable2 values(18,	'가슴이 뛴다',	'케이윌',	'33740',	'76850');");
		db.execSQL("insert into SongTable2 values(19,	'겁쟁이',	'버즈',	'14657',	'45151');");
		db.execSQL("insert into SongTable2 values(20,	'결혼해줄래',	'이승기',	'31298',	'46693');");
		db.execSQL("insert into SongTable2 values(21,	'고백',	'다이나믹듀오',	'15388',	'69650');");
		db.execSQL("insert into SongTable2 values(22,	'고백',	'델리스파이스',	'10821',	'9276');");
		db.execSQL("insert into SongTable2 values(23,	'고백',	'뜨거운감자',	'32409',	'76499');");
		db.execSQL("insert into SongTable2 values(24,	'고백',	'박혜경',	'8599',	'6195');");
		db.execSQL("insert into SongTable2 values(25,	'고백',	'포맨',	'15865',	'85044');");
		db.execSQL("insert into SongTable2 values(26,	'고백을 앞두고',	'윤종신',	'13410',	'68381');");
		db.execSQL("insert into SongTable2 values(27,	'고백하기 좋은 날',	'윤하',	'17837',	'81701');");
		db.execSQL("insert into SongTable2 values(28,	'고백하던 날',	'조권',	'32771',	'47071');");
		db.execSQL("insert into SongTable2 values(29,	'고해',	'임재범',	'9033',	'7033');");
		db.execSQL("insert into SongTable2 values(30,	'곰인형',	'린',	'35138',	'47683');");
		db.execSQL("insert into SongTable2 values(31,	'귀여워(with 권정열 of 10cm)',	'별',	'36002',	'77435');");
		db.execSQL("insert into SongTable2 values(32,	'그녀를 만나는 곳 100m 전',	'이상우',	'33',	'167');");
		db.execSQL("insert into SongTable2 values(33,	'그대는 눈물겹다',	'M.C. The Max',	'12639',	'64227');");
		db.execSQL("insert into SongTable2 values(34,	'그대를 사랑하는 10가지 이유',	'이석훈',	'32505',	'47003');");
		db.execSQL("insert into SongTable2 values(35,	'그댄 행복에 살텐데',	'리즈',	'10339',	'9241');");
		db.execSQL("insert into SongTable2 values(36,	'까만안경',	'이루',	'16468',	'45717');");
		db.execSQL("insert into SongTable2 values(37,	'꿈을 꾸다',	'김태우',	'31865',	'84614');");
		db.execSQL("insert into SongTable2 values(38,	'나 그대에게 모두 드리리',	'이장희',	'230',	'197');");
		db.execSQL("insert into SongTable2 values(39,	'나도 여자랍니다',	'장나라',	'12397',	'64207');");
		db.execSQL("insert into SongTable2 values(40,	'나를 사랑했던 사람아',	'허각',	'35198',	'47703');");
		db.execSQL("insert into SongTable2 values(41,	'나만 부를 수 있는 노래',	'바닷길',	'34124',	'58311');");
		db.execSQL("insert into SongTable2 values(42,	'난 사랑에 빠졌죠',	'박지윤',	'9819',	'7786');");
		db.execSQL("insert into SongTable2 values(43,	'내 사랑아',	'이종현',	'35573',	'47804');");
		db.execSQL("insert into SongTable2 values(44,	'내 손을 잡아 (최고의 사랑 O.S.T)',	'아이유',	'33962',	'58259');");
		db.execSQL("insert into SongTable2 values(45,	'내 안의 그대',	'서영은',	'11834',	'9502');");
		db.execSQL("insert into SongTable2 values(46,	'내 여자라니까',	'이승기',	'13483',	'9854');");
		db.execSQL("insert into SongTable2 values(47,	'내가 노래를 못해도',	'세븐',	'34957',	'47642');");
		db.execSQL("insert into SongTable2 values(48,	'내게 오는 길',	'성시경',	'9256',	'6679');");
		db.execSQL("insert into SongTable2 values(49,	'내꺼중에 최고',	'이현',	'33642',	'47294');");
		db.execSQL("insert into SongTable2 values(50,	'내꺼하자',	'인피니트',	'34203',	'47454');");
		db.execSQL("insert into SongTable2 values(51,	'너 땜에 못살아',	'서인국',	'36410',	'77544');");
		db.execSQL("insert into SongTable2 values(52,	'너를 사랑해',	'한동준',	'1512',	'2724');");
		db.execSQL("insert into SongTable2 values(53,	'너를 사랑해',	'S.E.S',	'4934',	'5756');");
		db.execSQL("insert into SongTable2 values(54,	'너를 위해',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable2 values(55,	'넌 내게 반했어',	'정용화',	'34110',	'47431');");
		db.execSQL("insert into SongTable2 values(56,	'널 사랑하겠어',	'동물원',	'2853',	'3985');");
		db.execSQL("insert into SongTable2 values(57,	'니가 참 좋아',	'쥬얼리',	'11657',	'63608');");
		db.execSQL("insert into SongTable2 values(58,	'니가 필요해',	'케이윌',	'34999',	'47655');");
		db.execSQL("insert into SongTable2 values(59,	'다 줄꺼야',	'조규만',	'8614',	'96538');");
		db.execSQL("insert into SongTable2 values(60,	'다른 누구도 아닌 너에게',	'장재인',	'34980',	'77174');");
		db.execSQL("insert into SongTable2 values(61,	'다행이다',	'이적',	'17772',	'45912');");
		db.execSQL("insert into SongTable2 values(62,	'단 한사람',	'고유진',	'16284',	'81139');");
		db.execSQL("insert into SongTable2 values(63,	'달콤한 상상',	'쥬얼리',	'11875',	'65669');");
		db.execSQL("insert into SongTable2 values(64,	'러브 레시피',	'클래지콰이',	'36417',	'58883');");
		db.execSQL("insert into SongTable2 values(65,	'몰라요',	'Apink',	'38898',	'47366');");
		db.execSQL("insert into SongTable2 values(66,	'못된 것만 배워서',	'B1A4',	'35375',	'87233');");
		db.execSQL("insert into SongTable2 values(67,	'바보야',	'쥬얼리',	'12003',	'63609');");
		db.execSQL("insert into SongTable2 values(68,	'바보에게… 바보가',	'박명수',	'19440',	'83482');");
		db.execSQL("insert into SongTable2 values(69,	'반쪽',	'V.O.S',	'19795',	'85830');");
		db.execSQL("insert into SongTable2 values(70,	'벚꽃 엔딩',	'버스커버스커',	'35184',	'47700');");
		db.execSQL("insert into SongTable2 values(71,	'비밀번호 486',	'윤하',	'17489',	'45877');");
		db.execSQL("insert into SongTable2 values(72,	'사랑',	'노사연',	'19142',	'46148');");
		db.execSQL("insert into SongTable2 values(73,	'사랑 빛',	'CNBLUE',	'32583',	'57917');");
		db.execSQL("insert into SongTable2 values(74,	'사랑시 고백구 행복동',	'제이세라',	'34908',	'47622');");
		db.execSQL("insert into SongTable2 values(75,	'사랑의 바보',	'더넛츠',	'13721',	'64438');");
		db.execSQL("insert into SongTable2 values(76,	'사랑의 시작은 고백에서부터',	'소울크라이',	'34086',	'86942');");
		db.execSQL("insert into SongTable2 values(77,	'사랑하기 때문에',	'유재하',	'241',	'1679');");
		db.execSQL("insert into SongTable2 values(78,	'사랑하오',	'알렉스',	'30102',	'46412');");
		db.execSQL("insert into SongTable2 values(79,	'사랑합니다',	'M.C. The Max ',	'33324',	'47212');");
		db.execSQL("insert into SongTable2 values(80,	'사랑합니다',	'이재훈',	'13297',	'9740');");
		db.execSQL("insert into SongTable2 values(81,	'사랑해',	'쥬얼리',	'4371',	'7518');");
		db.execSQL("insert into SongTable2 values(82,	'사랑해',	'스윗소로우',	'19279',	'85739');");
		db.execSQL("insert into SongTable2 values(83,	'사랑해도 될까요',	'유리상자',	'9724',	'7661');");
		db.execSQL("insert into SongTable2 values(84,	'설레임',	'별',	'정보없음',	'45307');");
		db.execSQL("insert into SongTable2 values(85,	'솜사탕',	'네미시스',	'13525',	'64231');");
		db.execSQL("insert into SongTable2 values(86,	'시작',	'박기영',	'8157',	'5861');");
		db.execSQL("insert into SongTable2 values(87,	'신부에게',	'유리상자',	'8426',	'6042');");
		db.execSQL("insert into SongTable2 values(88,	'아이처럼',	'김동률',	'19166',	'83375');");
		db.execSQL("insert into SongTable2 values(89,	'여가',	'장연주',	'14511',	'68897');");
		db.execSQL("insert into SongTable2 values(90,	'연인',	'김연우',	'12558',	'9650');");
		db.execSQL("insert into SongTable2 values(91,	'오! 나의 여신님',	'트랙스',	'33017',	'58025');");
		db.execSQL("insert into SongTable2 values(92,	'왜 모르니',	'별',	'10428',	'9251');");
		db.execSQL("insert into SongTable2 values(93,	'원더보이',	'애프터스쿨BLUE',	'34193',	'58321');");
		db.execSQL("insert into SongTable2 values(94,	'이 노래',	'2AM',	'19848',	'83678');");
		db.execSQL("insert into SongTable2 values(95,	'이게 사랑이 아니면',	'버벌진트',	'36479',	'48031');");
		db.execSQL("insert into SongTable2 values(96,	'이런 남자',	'먼데이키즈',	'15473',	'69694');");
		db.execSQL("insert into SongTable2 values(97,	'이상형',	'버스커버스커',	'35166',	'47689');");
		db.execSQL("insert into SongTable2 values(98,	'이젠',	'쥬얼리',	'9525',	'7385');");
		db.execSQL("insert into SongTable2 values(99,	'잔소리',	'아이유',	'32663',	'76568');");
		db.execSQL("insert into SongTable2 values(100,	'좋아해',	'김진표',	'31183',	'86179');");
		db.execSQL("insert into SongTable2 values(101,	'좋은 걸 어떡해',	'블랙펄',	'18373',	'46016');");
		db.execSQL("insert into SongTable2 values(102,	'좋은 날',	'아이유',	'33393',	'76754');");
		db.execSQL("insert into SongTable2 values(103,	'죽겠네',	'10cm',	'33658',	'47245');");
		db.execSQL("insert into SongTable2 values(104,	'죽을 만큼 아파서',	'MC몽',	'32736',	'47068');");
		db.execSQL("insert into SongTable2 values(105,	'천생연분',	'솔리드',	'3122',	'4713');");
		db.execSQL("insert into SongTable2 values(106,	'청혼',	'노을',	'13319',	'68248');");
		db.execSQL("insert into SongTable2 values(107,	'청혼',	'이소라',	'3612',	'4885');");
		db.execSQL("insert into SongTable2 values(108,	'취중고백',	'필',	'15430',	'69615');");
		db.execSQL("insert into SongTable2 values(109,	'취중진담',	'전람회',	'3134',	'4772');");
		db.execSQL("insert into SongTable2 values(110,	'키친',	'이소은',	'10574',	'62855');");
		db.execSQL("insert into SongTable2 values(111,	'통화 연결음',	'써니힐',	'18691',	'83188');");
		db.execSQL("insert into SongTable2 values(112,	'행복한 나를',	'에코',	'4108',	'5168');");
		db.execSQL("insert into SongTable2 values(113,	'흰눈',	'이루',	'16786',	'45790');");
		db.execSQL("insert into SongTable2 values(114,	'Julian',	'스쿨',	'9491',	'6737');");
		db.execSQL("insert into SongTable2 values(115,	'사랑스러워',	'김종국',	'15128',	'64833');");
		db.execSQL("insert into SongTable2 values(116,	'사랑',	'나훈아',	'646',	'897');");
		db.execSQL("insert into SongTable2 values(117,	'말도 안 돼',	'윤하',	'32414',	'46981');");
		db.execSQL("insert into SongTable2 values(118,	'YooHoo',	'시크릿',	'36753',	'58971');");
		db.execSQL("insert into SongTable2 values(119,	'너는 내 운명',	'하하',	'18948',	'83310');");
		db.execSQL("insert into SongTable2 values(120,	'별빛달빛',	'시크릿',	'33992',	'76928');");
		db.execSQL("insert into SongTable2 values(121,	'초콜릿',	'케이윌, 마리오',	'31562',	'46758');");
		db.execSQL("insert into SongTable2 values(122,	'Stand By Me (꽃보다 남자 O.S.T)',	'샤이니',	'30672',	'84049');");
		db.execSQL("insert into SongTable2 values(123,	'The Romantic',	'투개월',	'34997',	'47659');");
		db.execSQL("insert into SongTable2 values(124,	'Number 1',	'투개월',	'36891',	'48123');");
		db.execSQL("insert into SongTable2 values(125,	'청혼하는 거에요',	'포맨',	'36783',	'77612');");
		db.execSQL("insert into SongTable2 values(126,	'연가',	'유승준',	'8593',	'6184');");
		db.execSQL("insert into SongTable2 values(127,	'Hello (Feat. 버벌진트)',	'조용필',	'36712',	'48080');");
		db.execSQL("insert into SongTable2 values(128,	'그대 인형 (오! 마이 레이디 O.S.T)',	'써니',	'32376',	'76489');");
		db.execSQL("insert into SongTable2 values(129,	'그대와 나, 설레임',	'어쿠스틱 콜라보',	'35791',	'58694');");
		db.execSQL("insert into SongTable2 values(130,	'연애시대 (Feat. Ra.D, Narr. 한효주)',	'이승기',	'34537',	'87074');");
		db.execSQL("insert into SongTable2 values(131,	'I Love You',	'나비',	'19442',	'83491');");
		db.execSQL("insert into SongTable2 values(132,	'Propose',	'강타',	'10103',	'9119');");
		db.execSQL("insert into SongTable2 values(133,	'몸만 와',	'팬텀',	'36607',	'77579');");
		db.execSQL("insert into SongTable2 values(134,	'평생',	'UN',	'9217',	'6601');");
		db.execSQL("insert into SongTable2 values(135,	'사랑의 향기는 설레임을 타고 온다',	'임현정',	'15775',	'45507');");
		db.execSQL("insert into SongTable2 values(136,	'결혼해줘',	'임창정',	'3990',	'5070');");
	}			

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable2");
		onCreate(db);
	}
}
