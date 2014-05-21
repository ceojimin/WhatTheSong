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

public class WhatTheSong5 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_5);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_5",
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

		WhatTheSongDBHelper5 wtsHelper5 = new WhatTheSongDBHelper5(this, "whatthesong5.db", null, DATABASE_VERSION);
		db = wtsHelper5.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable5 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable5 where id= '"
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
		new AlertDialog.Builder(WhatTheSong5.this)
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
											WhatTheSong5.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong5.this,
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
			Intent newActivity = new Intent(WhatTheSong5.this,
					WhatTheSongBookmark.class);
			WhatTheSong5.this.startActivity(newActivity);
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
					"m_pref_5", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper5 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper5(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '고음 테마' DB (130627 : 163개)
		db.execSQL("CREATE TABLE SongTable5 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable5 values(1,	'8282',	'다비치',	'30868',	'46582');");
		db.execSQL("insert into SongTable5 values(2,	'1, 2, 3, 4',	'이하이',	'36019',	'47909');");
		db.execSQL("insert into SongTable5 values(3,	'365일',	'알리',	'31733',	'84507');");
		db.execSQL("insert into SongTable5 values(4,	'After',	'얀',	'9306',	'6687');");
		db.execSQL("insert into SongTable5 values(5,	'Angel',	'박완규',	'9351',	'7551');");
		db.execSQL("insert into SongTable5 values(6,	'Don＇t Cry',	'더 크로스',	'11789',	'9486');");
		db.execSQL("insert into SongTable5 values(7,	'Emotions',	'Mariah Carey',	'7458',	'8061');");
		db.execSQL("insert into SongTable5 values(8,	'Endless',	'플라워',	'9281',	'6658');");
		db.execSQL("insert into SongTable5 values(9,	'Fine',	'소찬휘',	'9715',	'7683');");
		db.execSQL("insert into SongTable5 values(10,	'Heaven',	'에일리',	'34991',	'47649');");
		db.execSQL("insert into SongTable5 values(11,	'I Love You',	'포지션',	'9308',	'6666');");
		db.execSQL("insert into SongTable5 values(12,	'If I Leave (나 가거든)',	'조수미',	'9744',	'7679');");
		db.execSQL("insert into SongTable5 values(13,	'illa illa(일라 일라)',	'주니엘',	'35460',	'77304');");
		db.execSQL("insert into SongTable5 values(14,	'Lie',	'박화요비',	'8996',	'6506');");
		db.execSQL("insert into SongTable5 values(15,	'Listen',	'Beyonce',	'21531',	'60490');");
		db.execSQL("insert into SongTable5 values(16,	'Lonely Night',	'부활',	'4001',	'5087');");
		db.execSQL("insert into SongTable5 values(17,	'Maria',	'김아중',	'16712',	'45783');");
		db.execSQL("insert into SongTable5 values(18,	'Mermaid',	'소향,박정현,이영현',	'34317',	'47489');");
		db.execSQL("insert into SongTable5 values(19,	'Never Ending Story',	'부활',	'10031',	'9037');");
		db.execSQL("insert into SongTable5 values(20,	'O Sole Mio',	'마리아',	'정보없음',	'6986');");
		db.execSQL("insert into SongTable5 values(21,	'One Love',	'M.C. The Max',	'10594',	'62836');");
		db.execSQL("insert into SongTable5 values(22,	'P.S.l love you',	'박정현',	'4714',	'5550');");
		db.execSQL("insert into SongTable5 values(23,	'Red rose',	'The Cross',	'18129',	'64981');");
		db.execSQL("insert into SongTable5 values(24,	'Say Yes',	'활(김명기)',	'11009',	'9316');");
		db.execSQL("insert into SongTable5 values(25,	'She＇s Gone',	'Steelheart',	'7095',	'1677');");
		db.execSQL("insert into SongTable5 values(26,	'Shiny Day',	'마리아',	'12569',	'66679');");
		db.execSQL("insert into SongTable5 values(27,	'Shout',	'김경호',	'8259',	'6471');");
		db.execSQL("insert into SongTable5 values(28,	'Tears',	'소찬휘',	'8797',	'6286');");
		db.execSQL("insert into SongTable5 values(29,	'The show must go on',	'Queen',	'20093',	'61836');");
		db.execSQL("insert into SongTable5 values(30,	'To Heaven',	'조성모',	'4783',	'5596');");
		db.execSQL("insert into SongTable5 values(31,	'You',	'김상민',	'9685',	'7525');");
		db.execSQL("insert into SongTable5 values(32,	'가난한 사랑',	'버즈',	'13513',	'64339');");
		db.execSQL("insert into SongTable5 values(33,	'가슴아 그만해',	'M.C. The Max',	'17773',	'45907');");
		db.execSQL("insert into SongTable5 values(34,	'가시',	'버즈',	'14684',	'69033');");
		db.execSQL("insert into SongTable5 values(35,	'가시나무',	'조성모',	'8691',	'6212');");
		db.execSQL("insert into SongTable5 values(36,	'가지마가지마',	'브라운아이드소울',	'19762',	'46333');");
		db.execSQL("insert into SongTable5 values(37,	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable5 values(38,	'거짓말',	'버즈',	'14736',	'68976');");
		db.execSQL("insert into SongTable5 values(39,	'겁쟁이',	'버즈',	'14657',	'45151');");
		db.execSQL("insert into SongTable5 values(40,	'겨울비',	'김종서',	'1539',	'2066');");
		db.execSQL("insert into SongTable5 values(41,	'귀로',	'박선주',	'749',	'2245');");
		db.execSQL("insert into SongTable5 values(42,	'귀로',	'나얼',	'14469',	'68797');");
		db.execSQL("insert into SongTable5 values(43,	'귀천',	'문차일드',	'9001',	'6519');");
		db.execSQL("insert into SongTable5 values(44,	'그녀의 연인에게',	'K2',	'8203',	'5918');");
		db.execSQL("insert into SongTable5 values(45,	'그래서 그대는',	'얀',	'9986',	'9034');");
		db.execSQL("insert into SongTable5 values(46,	'금지된 사랑',	'김경호',	'4074',	'5114');");
		db.execSQL("insert into SongTable5 values(47,	'꽃피는 봄이 오면',	'BMK',	'14503',	'45112');");
		db.execSQL("insert into SongTable5 values(48,	'꿈에',	'박정현',	'9973',	'7955');");
		db.execSQL("insert into SongTable5 values(49,	'나 돌아가',	'임정희',	'정보없음',	'85061');");
		db.execSQL("insert into SongTable5 values(50,	'나를 슬프게 하는 사람들',	'김경호',	'3935',	'5015');");
		db.execSQL("insert into SongTable5 values(51,	'나만의 그대',	'김혁건',	'19005',	'83308');");
		db.execSQL("insert into SongTable5 values(52,	'나만의 슬픔',	'김돈규',	'3504',	'4835');");
		db.execSQL("insert into SongTable5 values(53,	'나에게로 떠나는 여행',	'버즈',	'14828',	'45229');");
		db.execSQL("insert into SongTable5 values(54,	'나에게로의 초대',	'정경화',	'3294',	'4139');");
		db.execSQL("insert into SongTable5 values(55,	'낙인',	'임재범',	'32096',	'84726');");
		db.execSQL("insert into SongTable5 values(56,	'난 괜찮아',	'진주',	'4263',	'5188');");
		db.execSQL("insert into SongTable5 values(57,	'난 널 사랑해',	'신효범',	'1792',	'3326');");
		db.execSQL("insert into SongTable5 values(58,	'난장이가 쏘아올린 작은공',	'The Cross',	'12367',	'66599');");
		db.execSQL("insert into SongTable5 values(59,	'내 삶의 반',	'한경일',	'10876',	'9291');");
		db.execSQL("insert into SongTable5 values(60,	'내겐 너니까',	'효린',	'34554',	'47545');");
		db.execSQL("insert into SongTable5 values(61,	'너랑 나',	'아이유',	'34700',	'47581');");
		db.execSQL("insert into SongTable5 values(62,	'너를 보내고',	'윤도현',	'8589',	'6181');");
		db.execSQL("insert into SongTable5 values(63,	'너를 위해',	'임재범',	'8890',	'6348');");
		db.execSQL("insert into SongTable5 values(64,	'눈꽃송이',	'투로맨스',	'30576',	'46510');");
		db.execSQL("insert into SongTable5 values(65,	'눈물',	'리아',	'4791',	'5626');");
		db.execSQL("insert into SongTable5 values(66,	'눈부신 눈물',	'디셈버',	'33242',	'47189');");
		db.execSQL("insert into SongTable5 values(67,	'다신 찾지마',	'다비치',	'34365',	'77032');");
		db.execSQL("insert into SongTable5 values(68,	'다이어리',	'나비',	'34117',	'76951');");
		db.execSQL("insert into SongTable5 values(69,	'달',	'박정현',	'14576',	'45125');");
		db.execSQL("insert into SongTable5 values(70,	'떠나는 사람을 위해',	'최재훈',	'4269',	'5190');");
		db.execSQL("insert into SongTable5 values(71,	'마지막사랑',	'박기영',	'8378',	'6011');");
		db.execSQL("insert into SongTable5 values(72,	'말리꽃',	'이승철',	'8941',	'6438');");
		db.execSQL("insert into SongTable5 values(73,	'모르시나요',	'다비치',	'36443',	'48014');");
		db.execSQL("insert into SongTable5 values(74,	'몹쓸사랑',	'나오미',	'19236',	'46206');");
		db.execSQL("insert into SongTable5 values(75,	'못해',	'포맨',	'32125',	'46912');");
		db.execSQL("insert into SongTable5 values(76,	'미아',	'아이유',	'30197',	'46438');");
		db.execSQL("insert into SongTable5 values(77,	'미워요',	'정인',	'32340',	'46956');");
		db.execSQL("insert into SongTable5 values(78,	'바람기억',	'나얼',	'35884',	'47870');");
		db.execSQL("insert into SongTable5 values(79,	'발걸음',	'에메랄드 캐슬',	'3800',	'4984');");
		db.execSQL("insert into SongTable5 values(80,	'베르사이유의 장미',	'네미시스',	'12503',	'64148');");
		db.execSQL("insert into SongTable5 values(81,	'별',	'M.C. The Max',	'12489',	'64226');");
		db.execSQL("insert into SongTable5 values(82,	'별이될게',	'디셈버',	'32601',	'86537');");
		db.execSQL("insert into SongTable5 values(83,	'보고싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable5 values(84,	'비가와',	'락밴드크로스',	'15483',	'45414');");
		db.execSQL("insert into SongTable5 values(85,	'비와 외로움(락버전)',	'바람꽃',	'5774',	'65746');");
		db.execSQL("insert into SongTable5 values(86,	'비의 랩소디',	'최재훈',	'8787',	'6279');");
		db.execSQL("insert into SongTable5 values(87,	'비정',	'김경호',	'8161',	'5871');");
		db.execSQL("insert into SongTable5 values(88,	'사고쳤어요',	'다비치',	'30883',	'84150');");
		db.execSQL("insert into SongTable5 values(89,	'사랑Two',	'윤도현',	'8730',	'7086');");
		db.execSQL("insert into SongTable5 values(90,	'사랑보다 깊은 상처',	'박정현',	'4980',	'5810');");
		db.execSQL("insert into SongTable5 values(91,	'사랑아',	'더원',	'17884',	'81766');");
		db.execSQL("insert into SongTable5 values(92,	'사랑은 아프려고 하는 거죠',	'M.C. The Max',	'15460',	'45420');");
		db.execSQL("insert into SongTable5 values(93,	'사랑은 언제나 목마르다',	'유미',	'9835',	'7809');");
		db.execSQL("insert into SongTable5 values(94,	'사랑의 시',	'M.C. The Max',	'12437',	'66565');");
		db.execSQL("insert into SongTable5 values(95,	'사랑이 슬픔에게',	'야다',	'9385',	'6759');");
		db.execSQL("insert into SongTable5 values(96,	'사랑이 올까요',	'박정현',	'8236',	'9056');");
		db.execSQL("insert into SongTable5 values(97,	'사랑이라 쓰고 아픔이라 부른다',	'서인영',	'32653',	'47041');");
		db.execSQL("insert into SongTable5 values(98,	'사랑한 후에',	'박효신',	'31630',	'46778');");
		db.execSQL("insert into SongTable5 values(99,	'사랑할수록',	'부활',	'1917',	'3356');");
		db.execSQL("insert into SongTable5 values(100,	'사미인곡',	'서문탁',	'9660',	'7587');");
		db.execSQL("insert into SongTable5 values(101,	'사슬',	'서문탁',	'9050',	'6528');");
		db.execSQL("insert into SongTable5 values(102,	'살다가 한번쯤',	'포맨',	'34009',	'76933');");
		db.execSQL("insert into SongTable5 values(103,	'살만해',	'브랜뉴데이',	'30636',	'84017');");
		db.execSQL("insert into SongTable5 values(104,	'세상의 중심에서 사랑을 외치다(Feat. 소찬휘)',	'더 크로스',	'17343',	'81521');");
		db.execSQL("insert into SongTable5 values(105,	'소년의 꿈',	'활',	'9100',	'7425');");
		db.execSQL("insert into SongTable5 values(106,	'슬픈 언약식',	'김정민',	'2816',	'3951');");
		db.execSQL("insert into SongTable5 values(107,	'시간아 멈춰라',	'다비치',	'32562',	'57844');");
		db.execSQL("insert into SongTable5 values(108,	'심',	'얀',	'10052',	'64011');");
		db.execSQL("insert into SongTable5 values(109,	'아름다운 구속',	'김종서',	'3611',	'4906');");
		db.execSQL("insert into SongTable5 values(110,	'아무말도,아무것도',	'박정현',	'9218',	'6637');");
		db.execSQL("insert into SongTable5 values(111,	'아시나요',	'조성모',	'9054',	'6512');");
		db.execSQL("insert into SongTable5 values(112,	'안녕이라고 말하지마',	'다비치',	'34338',	'47493');");
		db.execSQL("insert into SongTable5 values(113,	'애모',	'김수희',	'1427',	'1983');");
		db.execSQL("insert into SongTable5 values(114,	'애인있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable5 values(115,	'언제나 사랑해',	'제이세라',	'33724',	'58187');");
		db.execSQL("insert into SongTable5 values(116,	'얼음꽃',	'아이유',	'34071',	'58299');");
		db.execSQL("insert into SongTable5 values(117,	'여자라서 하지 못한 말',	'유미',	'30491',	'46499');");
		db.execSQL("insert into SongTable5 values(118,	'여전히 아름다운 지',	'토이',	'4975',	'5781');");
		db.execSQL("insert into SongTable5 values(119,	'연',	'빅마마',	'16627',	'85250');");
		db.execSQL("insert into SongTable5 values(120,	'와인',	'김경호',	'8945',	'6429');");
		db.execSQL("insert into SongTable5 values(121,	'유리의성',	'K2',	'8349',	'6156');");
		db.execSQL("insert into SongTable5 values(122,	'이곳에서',	'플라워',	'14057',	'68559');");
		db.execSQL("insert into SongTable5 values(123,	'이럴거면',	'아이비',	'17238',	'81468');");
		db.execSQL("insert into SongTable5 values(124,	'이미 슬픈 사랑',	'야다',	'8122',	'5830');");
		db.execSQL("insert into SongTable5 values(125,	'이별의 간주곡',	'더크로스',	'12333',	'66546');");
		db.execSQL("insert into SongTable5 values(126,	'이유같지 않은 이유',	'박미경',	'2335',	'3578');");
		db.execSQL("insert into SongTable5 values(127,	'이젠 그랬으면 좋겠네',	'박정현',	'33901',	'76912');");
		db.execSQL("insert into SongTable5 values(128,	'인연',	'이선희',	'14814',	'45195');");
		db.execSQL("insert into SongTable5 values(129,	'자유인',	'김경호',	'5884',	'63972');");
		db.execSQL("insert into SongTable5 values(130,	'잘 된 일이야',	'나비',	'33561',	'47268');");
		db.execSQL("insert into SongTable5 values(131,	'잘가요 로맨스',	'서인영',	'32590',	'86539');");
		db.execSQL("insert into SongTable5 values(132,	'잘가요 로맨스',	'박선주',	'31712',	'84552');");
		db.execSQL("insert into SongTable5 values(133,	'잠시만 안녕',	'M.C. The Max',	'10290',	'9140');");
		db.execSQL("insert into SongTable5 values(134,	'좋은 날',	'아이유',	'33393',	'76754');");
		db.execSQL("insert into SongTable5 values(135,	'중독된 사랑',	'조장혁',	'8937',	'6431');");
		db.execSQL("insert into SongTable5 values(136,	'진달래꽃',	'마야',	'11178',	'9382');");
		db.execSQL("insert into SongTable5 values(137,	'진짜일 리 없어',	'임정희',	'33126',	'47160');");
		db.execSQL("insert into SongTable5 values(138,	'진혼',	'야다',	'9315',	'6711');");
		db.execSQL("insert into SongTable5 values(139,	'천년의 사랑',	'박완규',	'8447',	'6048');");
		db.execSQL("insert into SongTable5 values(140,	'천년의 사랑',	'이영현',	'34995',	'47647');");
		db.execSQL("insert into SongTable5 values(141,	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable5 values(142,	'피아노',	'조성모',	'11000',	'9307');");
		db.execSQL("insert into SongTable5 values(143,	'하늘 끝에서 흘린 눈물',	'주니퍼',	'9376',	'6731');");
		db.execSQL("insert into SongTable5 values(144,	'하루',	'김범수',	'9337',	'6730');");
		db.execSQL("insert into SongTable5 values(145,	'하루 끝',	'아이유',	'35353',	'47737');");
		db.execSQL("insert into SongTable5 values(146,	'하루가 십년이 되는 날',	'M.C. The Max',	'12556',	'64249');");
		db.execSQL("insert into SongTable5 values(147,	'한 번쯤',	'송창식',	'1322',	'991');");
		db.execSQL("insert into SongTable5 values(148,	'해바라기도 가끔 목이 아프죠',	'M.C. The Max',	'14292',	'68692');");
		db.execSQL("insert into SongTable5 values(149,	'행복하지 말아요',	'M.C. The Max',	'14260',	'45046');");
		db.execSQL("insert into SongTable5 values(150,	'혜야',	'종현',	'30184',	'83801');");
		db.execSQL("insert into SongTable5 values(151,	'후회가 싫다',	'김범수',	'13206',	'68117');");
		db.execSQL("insert into SongTable5 values(152,	'흉터',	'먼데이키즈',	'19438',	'83478');");
		db.execSQL("insert into SongTable5 values(153,	'보여줄게',	'에일리',	'35970',	'47889');");
		db.execSQL("insert into SongTable5 values(154,	'이기적이야',	'알리',	'36316',	'77516');");
		db.execSQL("insert into SongTable5 values(155,	'서시',	'신성우',	'2135',	'3446');");
		db.execSQL("insert into SongTable5 values(156,	'서랍정리',	'빅마마',	'36267',	'77490');");
		db.execSQL("insert into SongTable5 values(157,	'Endless Rain',	'X Japan',	'6773',	'40993');");
		db.execSQL("insert into SongTable5 values(158,	'얼음꽃 (야왕 O.S.T)',	'에일리',	'36379',	'58870');");
		db.execSQL("insert into SongTable5 values(159,	'내 손을 잡아 (최고의 사랑 O.S.T)',	'아이유',	'33962',	'58259');");
		db.execSQL("insert into SongTable5 values(160,	'눈물이 나',	'소냐',	'9892',	'7914');");
		db.execSQL("insert into SongTable5 values(161,	'내일을 향해',	'신성우',	'949',	'1374');");
		db.execSQL("insert into SongTable5 values(162,	'사랑, 결코 시들지 않는…',	'서문탁',	'8471',	'6135');");
		db.execSQL("insert into SongTable5 values(163,	'진화론',	'예레미',	'2720',	'64087');");
	}			

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable5");
		onCreate(db);
	}
}