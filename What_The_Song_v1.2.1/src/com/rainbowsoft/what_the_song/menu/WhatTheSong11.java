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

public class WhatTheSong11 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_11);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_11",
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

		WhatTheSongDBHelper11 wtsHelper11 = new WhatTheSongDBHelper11(this, "whatthesong11.db", null, DATABASE_VERSION);
		db = wtsHelper11.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable11 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable11 where id= '"
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
		new AlertDialog.Builder(WhatTheSong11.this)
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
											WhatTheSong11.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong11.this,
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
			Intent newActivity = new Intent(WhatTheSong11.this,
					WhatTheSongBookmark.class);
			WhatTheSong11.this.startActivity(newActivity);
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
					"m_pref_11", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper11 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper11(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '끝판 테마' DB (130627 : 127개)
		db.execSQL("CREATE TABLE SongTable11 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable11 values(1,	'The Last Song',	'X-Japan',	'25825', '41755'	);");
		db.execSQL("insert into SongTable11 values(2,	'10Minutes',	'이효리',	'11833', '9504'	);");
		db.execSQL("insert into SongTable11 values(3,	'3!4!',	'룰라',	'3162', '4184'	);");
		db.execSQL("insert into SongTable11 values(4,	'Better Than Yesterday',	'MC스나이퍼',	'17485', '81598'	);");
		db.execSQL("insert into SongTable11 values(5,	'Bo Peep Bo Peep',	'티아라',	'31931', '86368'	);");
		db.execSQL("insert into SongTable11 values(6,	'Cutie Honey',	'아유미',	'16268', '45639'	);");
		db.execSQL("insert into SongTable11 values(7,	'Dash',	'백지영',	'8816', '6296'	);");
		db.execSQL("insert into SongTable11 values(8,	'DOC와 춤을',	'DJ DOC',	'3976', '5042'	);");
		db.execSQL("insert into SongTable11 values(9,	'Festival',	'엄정화',	'8342', '5978'	);");
		db.execSQL("insert into SongTable11 values(10,	'Gee',	'소녀시대',	'30627', '84011'	);");
		db.execSQL("insert into SongTable11 values(11,	'I Got A Boy',	'소녀시대',	'36269', '47973'	);");
		db.execSQL("insert into SongTable11 values(12,	'Loving U',	'씨스타',	'35535', '58670'	);");
		db.execSQL("insert into SongTable11 values(13,	'Madonna',	'시크릿',	'32937', '47106'	);");
		db.execSQL("insert into SongTable11 values(14,	'Magic',	'시크릿',	'32416', '76501'	);");
		db.execSQL("insert into SongTable11 values(15,	'No.1',	'보아',	'9858', '7868'	);");
		db.execSQL("insert into SongTable11 values(16,	'Nobody',	'원더걸스',	'30191', '46428'	);");
		db.execSQL("insert into SongTable11 values(17,	'Roly-Poly',	'티아라',	'34109', '47429'	);");
		db.execSQL("insert into SongTable11 values(18,	'Run To You',	'DJ DOC',	'8874', '6349'	);");
		db.execSQL("insert into SongTable11 values(19,	'She＇s Gone',	'Steelheart',	'7095', '1677'	);");
		db.execSQL("insert into SongTable11 values(20,	'Show',	'김원준',	'3272', '4737'	);");
		db.execSQL("insert into SongTable11 values(21,	'Tears',	'소찬휘',	'8797', '6286'	);");
		db.execSQL("insert into SongTable11 values(22,	'U-Go-Girl',	'이효리',	'19854', '83680'	);");
		db.execSQL("insert into SongTable11 values(23,	'Valenti',	'보아',	'10084', '9099'	);");
		db.execSQL("insert into SongTable11 values(24,	'강남스타일',	'싸이',	'35608', '47802'	);");
		db.execSQL("insert into SongTable11 values(25,	'거위의 꿈',	'인순이',	'16936', '81408'	);");
		db.execSQL("insert into SongTable11 values(26,	'고래사냥',	'송창식',	'789', '808'	);");
		db.execSQL("insert into SongTable11 values(27,	'고향역',	'나훈아',	'3457', '135'	);");
		db.execSQL("insert into SongTable11 values(28,	'그녀와의 이별',	'김현정',	'4635', '5511'	);");
		db.execSQL("insert into SongTable11 values(29,	'나 이런 사람이야',	'DJ DOC',	'32901', '86608'	);");
		db.execSQL("insert into SongTable11 values(30,	'난 괜찮아',	'진주',	'4263', '5188'	);");
		db.execSQL("insert into SongTable11 values(31,	'난 알아요',	'서태지와 아이들',	'1294', '1296'	);");
		db.execSQL("insert into SongTable11 values(32,	'날 봐, 귀순',	'대성',	'19749', '46331'	);");
		db.execSQL("insert into SongTable11 values(33,	'날개 잃은 천사',	'룰라',	'2546', '3735'	);");
		db.execSQL("insert into SongTable11 values(34,	'남행열차',	'김수희',	'911', '1367'	);");
		db.execSQL("insert into SongTable11 values(35,	'낭만고양이',	'체리필터',	'6093', '62666'	);");
		db.execSQL("insert into SongTable11 values(36,	'내 귀에 캔디',	'백지영',	'31525', '46746'	);");
		db.execSQL("insert into SongTable11 values(37,	'내 생에 봄날은',	'캔',	'9763', '7655'	);");
		db.execSQL("insert into SongTable11 values(38,	'내가 제일 잘 나가',	'2NE1',	'34084', '47425'	);");
		db.execSQL("insert into SongTable11 values(39,	'너',	'이정현',	'8906', '6351'	);");
		db.execSQL("insert into SongTable11 values(40,	'널 그리며',	'박남정',	'351', '241'	);");
		db.execSQL("insert into SongTable11 values(41,	'다시 만난 세계',	'소녀시대',	'18470', '46023'	);");
		db.execSQL("insert into SongTable11 values(42,	'다짐',	'조성모',	'9066', '6521'	);");
		db.execSQL("insert into SongTable11 values(43,	'동전한닢Remix',	'다이다믹듀오',	'정보없음', '84184'	);");
		db.execSQL("insert into SongTable11 values(44,	'둥지',	'남진',	'8693', '6235'	);");
		db.execSQL("insert into SongTable11 values(45,	'땡벌',	'강진',	'10136', '7463'	);");
		db.execSQL("insert into SongTable11 values(46,	'떳다!! 그녀!!',	'위치스',	'10541', '9199'	);");
		db.execSQL("insert into SongTable11 values(47,	'또 만나요',	'딕훼밀리',	'1251', '2869'	);");
		db.execSQL("insert into SongTable11 values(48,	'말달리자',	'크라잉넛',	'4751', '5603'	);");
		db.execSQL("insert into SongTable11 values(49,	'말해줘',	'지누션',	'3994', '5060'	);");
		db.execSQL("insert into SongTable11 values(50,	'매직카펫라이드',	'자우림',	'8940', '6404'	);");
		db.execSQL("insert into SongTable11 values(51,	'멍',	'김현정',	'8869', '6344'	);");
		db.execSQL("insert into SongTable11 values(52,	'무조건',	'박상철',	'14712', '45167'	);");
		db.execSQL("insert into SongTable11 values(53,	'미쳐',	'이정현',	'9682', '7620'	);");
		db.execSQL("insert into SongTable11 values(54,	'바꿔',	'이정현',	'8486', '6134'	);");
		db.execSQL("insert into SongTable11 values(55,	'바다에 누워',	'높은음자리',	'723', '379'	);");
		db.execSQL("insert into SongTable11 values(56,	'반',	'이정현',	'9770', '7727'	);");
		db.execSQL("insert into SongTable11 values(57,	'밤이 깊었네',	'크라잉넛',	'9550', '6943'	);");
		db.execSQL("insert into SongTable11 values(58,	'밤이면 밤마다',	'인순이',	'1996', '1542'	);");
		db.execSQL("insert into SongTable11 values(59,	'버스안에서',	'자자',	'3699', '4879'	);");
		db.execSQL("insert into SongTable11 values(60,	'부산 갈매기',	'문성재',	'492', '419'	);");
		db.execSQL("insert into SongTable11 values(61,	'불꽃',	'코요태',	'13043', '68003'	);");
		db.execSQL("insert into SongTable11 values(62,	'붉은 노을',	'이문세',	'1842', '1482'	);");
		db.execSQL("insert into SongTable11 values(63,	'붉은 노을',	'빅뱅',	'30399', '83886'	);");
		db.execSQL("insert into SongTable11 values(64,	'비상',	'코요태',	'11406', '9417'	);");
		db.execSQL("insert into SongTable11 values(65,	'빙고',	'거북이',	'14246', '64589'	);");
		db.execSQL("insert into SongTable11 values(66,	'빙글빙글',	'나미',	'139', '436'	);");
		db.execSQL("insert into SongTable11 values(67,	'뻐꾸기 둥지 위로 날아간 새',	'김건모',	'4273', '5221'	);");
		db.execSQL("insert into SongTable11 values(68,	'뿌요뿌요',	'UP',	'3839', '4968'	);");
		db.execSQL("insert into SongTable11 values(69,	'사노라면',	'크라잉넛',	'6076', '63318'	);");
		db.execSQL("insert into SongTable11 values(70,	'사랑으로',	'해바라기',	'234', '443'	);");
		db.execSQL("insert into SongTable11 values(71,	'사랑은 Move',	'시크릿',	'34546', '47542'	);");
		db.execSQL("insert into SongTable11 values(72,	'사랑해 그리고 기억해',	'god',	'8540', '6136'	);");
		db.execSQL("insert into SongTable11 values(73,	'새',	'싸이',	'9365', '6724'	);");
		db.execSQL("insert into SongTable11 values(74,	'샤방샤방',	'박현빈',	'19343', '46249'	);");
		db.execSQL("insert into SongTable11 values(75,	'소녀시대',	'소녀시대',	'18829', '46107'	);");
		db.execSQL("insert into SongTable11 values(76,	'순정',	'코요태',	'4959', '5767'	);");
		db.execSQL("insert into SongTable11 values(77,	'슈퍼맨',	'노라조',	'30449', '46489'	);");
		db.execSQL("insert into SongTable11 values(78,	'스피드',	'김건모',	'3112', '4145'	);");
		db.execSQL("insert into SongTable11 values(79,	'아잉♡',	'오렌지캬라멜',	'33306', '47203'	);");
		db.execSQL("insert into SongTable11 values(80,	'아침이슬',	'양희은',	'11372', '538'	);");
		db.execSQL("insert into SongTable11 values(81,	'아파트',	'윤수일',	'340', '539'	);");
		db.execSQL("insert into SongTable11 values(82,	'애국가 4절',	'의식곡',	'5019', '4221'	);");
		db.execSQL("insert into SongTable11 values(83,	'어젯밤이야기',	'소방차',	'2810', '567'	);");
		db.execSQL("insert into SongTable11 values(84,	'어쩌다 마주친 그대',	'송골매',	'1209', '1220'	);");
		db.execSQL("insert into SongTable11 values(85,	'얼쑤',	'윙크',	'32210', '84782'	);");
		db.execSQL("insert into SongTable11 values(86,	'여행을 떠나요',	'조용필',	'2056', '3493'	);");
		db.execSQL("insert into SongTable11 values(87,	'여행을 떠나요',	'이승기',	'19623', '46303'	);");
		db.execSQL("insert into SongTable11 values(88,	'열정',	'코요태',	'4609', '6769'	);");
		db.execSQL("insert into SongTable11 values(89,	'열정',	'혜은이',	'1542', '2475'	);");
		db.execSQL("insert into SongTable11 values(90,	'영원한 친구',	'나미',	'985', '939'	);");
		db.execSQL("insert into SongTable11 values(91,	'오리날다',	'체리필터',	'11932', '9526'	);");
		db.execSQL("insert into SongTable11 values(92,	'오빠만 믿어',	'박현빈',	'18469', '46022'	);");
		db.execSQL("insert into SongTable11 values(93,	'와',	'이정현',	'8463', '6054'	);");
		db.execSQL("insert into SongTable11 values(94,	'이젠 안녕',	'015B',	'866', '1386'	);");
		db.execSQL("insert into SongTable11 values(95,	'인디안 인형처럼',	'나미',	'14', '645'	);");
		db.execSQL("insert into SongTable11 values(96,	'일탈',	'자우림',	'4282', '5196'	);");
		db.execSQL("insert into SongTable11 values(97,	'있잖아',	'아이유',	'31387', '86220'	);");
		db.execSQL("insert into SongTable11 values(98,	'잘못된 만남',	'김건모',	'2454', '3658'	);");
		db.execSQL("insert into SongTable11 values(99,	'젊은 그대',	'김수철',	'1238', '1715'	);");
		db.execSQL("insert into SongTable11 values(100,	'제 3 한강교',	'혜은이',	'1696', '1250'	);");
		db.execSQL("insert into SongTable11 values(101,	'줄래',	'이정현',	'8953', '6447'	);");
		db.execSQL("insert into SongTable11 values(102,	'찬찬찬',	'편승엽',	'2148', '3524'	);");
		db.execSQL("insert into SongTable11 values(103,	'챔피언',	'싸이',	'10062', '9061'	);");
		db.execSQL("insert into SongTable11 values(104,	'초련',	'클론',	'8807', '6308'	);");
		db.execSQL("insert into SongTable11 values(105,	'콩가',	'컨츄리꼬꼬',	'9853', '7878'	);");
		db.execSQL("insert into SongTable11 values(106,	'킬리만자로의 표범',	'조용필',	'914', '3366'	);");
		db.execSQL("insert into SongTable11 values(107,	'파란',	'코요태',	'9304', '6691'	);");
		db.execSQL("insert into SongTable11 values(108,	'패션',	'코요태',	'9216', '6582'	);");
		db.execSQL("insert into SongTable11 values(109,	'하늘땅 별땅',	'티아라',	'33121', '58049'	);");
		db.execSQL("insert into SongTable11 values(110,	'하늘을 달리다',	'이적',	'11378', '63301'	);");
		db.execSQL("insert into SongTable11 values(111,	'한 남자를 사랑했습니다',	'카이',	'17750', '85458'	);");
		db.execSQL("insert into SongTable11 values(112,	'한 잔의 추억',	'이장희',	'609', '765'	);");
		db.execSQL("insert into SongTable11 values(113,	'한국을 빛낸 100명의 위인들',	'최영준',	'989', '6991'	);");
		db.execSQL("insert into SongTable11 values(114,	'한동안 뜸 했었지',	'사랑과 평화',	'2054', '2572'	);");
		db.execSQL("insert into SongTable11 values(115,	'혼자한 사랑',	'김현정',	'4706', '5588'	);");
		db.execSQL("insert into SongTable11 values(116,	'화려한 싱글',	'양혜승',	'11831', '9511'	);");
		db.execSQL("insert into SongTable11 values(117,	'환희',	'정수라',	'25', '784'	);");
		db.execSQL("insert into SongTable11 values(118,	'환희',	'싸이',	'15089', '69351'	);");
		db.execSQL("insert into SongTable11 values(119,	'환희',	'코요태',	'12507', '7969'	);");
		db.execSQL("insert into SongTable11 values(120,	'흐린 기억 속의 그대',	'현진영',	'1183', '1371'	);");
		db.execSQL("insert into SongTable11 values(121,	'Gimme! Gimme!',	'컨츄리꼬꼬',	'8336',	'5960');");
		db.execSQL("insert into SongTable11 values(122,	'Lucifer',	'샤이니',	'32848',	'47088');");
		db.execSQL("insert into SongTable11 values(123,	'Ring Ding Dong',	'샤이니',	'31754',	'46815');");
		db.execSQL("insert into SongTable11 values(124,	'캔디',	'H.O.T',	'3547',	'4843');");
		db.execSQL("insert into SongTable11 values(125,	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable11 values(126,	'착각의 늪',	'박경림',	'9807',	'7781');");
		db.execSQL("insert into SongTable11 values(127,	'Bad Girls',	'이효리',	'36844',	'48098');");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable11");
		onCreate(db);
	}
}