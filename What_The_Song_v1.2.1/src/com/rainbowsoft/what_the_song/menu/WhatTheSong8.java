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

public class WhatTheSong8 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_8);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_8",
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

		WhatTheSongDBHelper8 wtsHelper8 = new WhatTheSongDBHelper8(this, "whatthesong8.db", null, DATABASE_VERSION);
		db = wtsHelper8.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable8 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable8 where id= '"
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
		new AlertDialog.Builder(WhatTheSong8.this)
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
											WhatTheSong8.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong8.this,
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
			Intent newActivity = new Intent(WhatTheSong8.this,
					WhatTheSongBookmark.class);
			WhatTheSong8.this.startActivity(newActivity);
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
					"m_pref_8", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper8 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper8(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '시즌 테마' DB (130627 : 126개)
		db.execSQL("CREATE TABLE SongTable8 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable8 values(1,	'10월의 어느 멋진 날에',	'김동규',	'15576',	'68526');");
		db.execSQL("insert into SongTable8 values(2,	'12월 32일',	'별',	'10120',	'9123');");
		db.execSQL("insert into SongTable8 values(3,	'12월의 로망스',	'유리상자',	'18919',	'83300');");
		db.execSQL("insert into SongTable8 values(4,	'12월의 이야기',	'스윗소로우',	'36186',	'47950');");
		db.execSQL("insert into SongTable8 values(5,	'3!4!',	'룰라',	'3162',	'4184');");
		db.execSQL("insert into SongTable8 values(6,	'All I Want For Chrismas Is You',	'Mariah Carey',	'20422',	'8008');");
		db.execSQL("insert into SongTable8 values(7,	'All You Need Is Love',	'무한도전',	'16758',	'85313');");
		db.execSQL("insert into SongTable8 values(8,	'Festival',	'엄정화',	'8342',	'5978');");
		db.execSQL("insert into SongTable8 values(9,	'Holiday',	'씨스타',	'35554',	'58668');");
		db.execSQL("insert into SongTable8 values(10,	'Hot Summer',	'f(x)',	'34039',	'47417');");
		db.execSQL("insert into SongTable8 values(11,	'How Are You?',	'쥬얼리',	'11450',	'63282');");
		db.execSQL("insert into SongTable8 values(12,	'Love In The Ice',	'동방신기',	'30226',	'83807');");
		db.execSQL("insert into SongTable8 values(13,	'Merry-Chri',	'보아',	'14312',	'45057');");
		db.execSQL("insert into SongTable8 values(14,	'One Summer Night',	'진추하, 아비',	'7117',	'2672');");
		db.execSQL("insert into SongTable8 values(15,	'Sea Of Love',	'플라이투더스카이',	'9874',	'7887');");
		db.execSQL("insert into SongTable8 values(16,	'Snow Prince ',	'SS501',	'15495',	'45421');");
		db.execSQL("insert into SongTable8 values(17,	'Summer Couple',	'문차일드',	'8903',	'6353');");
		db.execSQL("insert into SongTable8 values(18,	'Summer Dance',	'이정현',	'11676',	'9475');");
		db.execSQL("insert into SongTable8 values(19,	'Summer Dream',	'씨야',	'18063',	'81785');");
		db.execSQL("insert into SongTable8 values(20,	'Summer Holiday',	'Cliff Richard',	'7165',	'61179');");
		db.execSQL("insert into SongTable8 values(21,	'Summer Time',	'포지션',	'3911',	'5021');");
		db.execSQL("insert into SongTable8 values(22,	'Summer Vacation',	'SMTOWN',	'5806',	'7959');");
		db.execSQL("insert into SongTable8 values(23,	'Twist King',	'터보',	'3275',	'4743');");
		db.execSQL("insert into SongTable8 values(24,	'White',	'핑클',	'8578',	'6190');");
		db.execSQL("insert into SongTable8 values(25,	'Winter Kiss',	'박정현',	'30501',	'83950');");
		db.execSQL("insert into SongTable8 values(26,	'가을 우체국 앞에서',	'윤도현',	'4545',	'3204');");
		db.execSQL("insert into SongTable8 values(27,	'겨울 애상',	'이선희',	'717',	'806');");
		db.execSQL("insert into SongTable8 values(28,	'겨울동화',	'보이스원',	'19044',	'83347');");
		db.execSQL("insert into SongTable8 values(29,	'겨울바다',	'푸른하늘',	'527',	'1480');");
		db.execSQL("insert into SongTable8 values(30,	'겨울밤',	'장재인',	'34742',	'77127');");
		db.execSQL("insert into SongTable8 values(31,	'겨울비',	'김종서',	'1539',	'2066');");
		db.execSQL("insert into SongTable8 values(32,	'겨울사랑',	'가비엔제이',	'16708',	'45788');");
		db.execSQL("insert into SongTable8 values(33,	'겨울아이',	'이종용',	'160',	'1111');");
		db.execSQL("insert into SongTable8 values(34,	'겨울애',	'김연우',	'34799',	'58453');");
		db.execSQL("insert into SongTable8 values(35,	'겨울이 오면',	'김건모',	'3349',	'3730');");
		db.execSQL("insert into SongTable8 values(36,	'겨울이 오면',	'임재범, 테이',	'16664',	'45777');");
		db.execSQL("insert into SongTable8 values(37,	'겨울이야기',	'DJ DOC ',	'2890',	'4010');");
		db.execSQL("insert into SongTable8 values(38,	'겨울이야기',	'조관우',	'2855',	'3968');");
		db.execSQL("insert into SongTable8 values(39,	'겨울일기',	'장나라',	'14395',	'45079');");
		db.execSQL("insert into SongTable8 values(40,	'고래',	'명콜드라이브',	'32847',	'47086');");
		db.execSQL("insert into SongTable8 values(41,	'고속도로 로망스',	'김장훈',	'13708',	'64421');");
		db.execSQL("insert into SongTable8 values(42,	'그 겨울의 찻집',	'조용필',	'1481',	'1454');");
		db.execSQL("insert into SongTable8 values(43,	'그 어느 겨울',	'박희수',	'4958',	'5721');");
		db.execSQL("insert into SongTable8 values(44,	'그 해 여름',	'인피니트',	'35550',	'58664');");
		db.execSQL("insert into SongTable8 values(45,	'꽃송이가',	'버스커버스커',	'35227',	'87227');");
		db.execSQL("insert into SongTable8 values(46,	'꿍따리 샤바라',	'클론',	'3159',	'4152');");
		db.execSQL("insert into SongTable8 values(47,	'나에게로 떠나는 여행',	'버즈',	'14828',	'45229');");
		db.execSQL("insert into SongTable8 values(48,	'내 입술…따뜻한 커피처럼',	'샾',	'9698',	'7631');");
		db.execSQL("insert into SongTable8 values(49,	'냉면',	'명카드라이브',	'31390',	'84372');");
		db.execSQL("insert into SongTable8 values(50,	'너에게 난, 나에게 넌',	'자전거탄풍경',	'3777',	'62426');");
		db.execSQL("insert into SongTable8 values(51,	'눈의 꽃',	'박효신',	'14238',	'68590');");
		db.execSQL("insert into SongTable8 values(52,	'니가 참 좋아',	'쥬얼리',	'11657',	'63608');");
		db.execSQL("insert into SongTable8 values(53,	'달빛바다',	'아이유, 피에스타',	'35660',	'77339');");
		db.execSQL("insert into SongTable8 values(54,	'더위 먹은 갈매기',	'돌브레인',	'31413',	'84373');");
		db.execSQL("insert into SongTable8 values(55,	'더위 먹은 갈매기',	'유재석',	'35018',	'87146');");
		db.execSQL("insert into SongTable8 values(56,	'도시 탈출',	'클론',	'3926',	'4994');");
		db.execSQL("insert into SongTable8 values(57,	'로맨틱 겨울',	'김진표',	'32015',	'46881');");
		db.execSQL("insert into SongTable8 values(58,	'맥주와 땅콩',	'쿨',	'8848',	'6327');");
		db.execSQL("insert into SongTable8 values(59,	'미리 메리 크리스마스',	'아이유',	'33403',	'76766');");
		db.execSQL("insert into SongTable8 values(60,	'바다',	'UP',	'3933',	'5010');");
		db.execSQL("insert into SongTable8 values(61,	'바다의 공주',	'LPG',	'18335',	'45994');");
		db.execSQL("insert into SongTable8 values(62,	'바다의 왕자',	'박명수',	'8990',	'6491');");
		db.execSQL("insert into SongTable8 values(63,	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable8 values(64,	'벚꽃 엔딩',	'버스커버스커',	'35184',	'47700');");
		db.execSQL("insert into SongTable8 values(65,	'봄 여름 여름 여름',	'용감한녀석들',	'35645',	'77347');");
		db.execSQL("insert into SongTable8 values(66,	'부산 바캉스',	'스컬,하하',	'35675',	'77349');");
		db.execSQL("insert into SongTable8 values(67,	'비행기',	'거북이',	'16223',	'45653');");
		db.execSQL("insert into SongTable8 values(68,	'뿌요뿌요',	'UP',	'3839',	'4968');");
		db.execSQL("insert into SongTable8 values(69,	'사랑과 전쟁',	'다비치',	'19814',	'46352');");
		db.execSQL("insert into SongTable8 values(70,	'사랑의 눈보라',	'린',	'33352',	'정보없음');");
		db.execSQL("insert into SongTable8 values(71,	'스키장에서',	'코요태',	'33396',	'정보없음');");
		db.execSQL("insert into SongTable8 values(72,	'스키장에서',	'터보',	'4923',	'4854');");
		db.execSQL("insert into SongTable8 values(73,	'썸머 징글벨',	'박진영',	'3875',	'4999');");
		db.execSQL("insert into SongTable8 values(74,	'아이스크림',	'MC몽',	'16463',	'45710');");
		db.execSQL("insert into SongTable8 values(75,	'아틀란티스 소녀',	'보아',	'11466',	'9429');");
		db.execSQL("insert into SongTable8 values(76,	'엉뚱한 상상',	'지누',	'3167',	'4175');");
		db.execSQL("insert into SongTable8 values(77,	'여름',	'징검다리',	'970',	'1708');");
		db.execSQL("insert into SongTable8 values(78,	'여름 안에서',	'듀스',	'2246',	'3527');");
		db.execSQL("insert into SongTable8 values(79,	'여름날',	'유희열',	'19945',	'46380');");
		db.execSQL("insert into SongTable8 values(80,	'여름아! 부탁해',	'인디고',	'9974',	'7980');");
		db.execSQL("insert into SongTable8 values(81,	'여름안에서',	'서연',	'11464',	'63311');");
		db.execSQL("insert into SongTable8 values(82,	'여름이 좋아',	'김동완',	'18241',	'81910');");
		db.execSQL("insert into SongTable8 values(83,	'여름이야기',	'DJ DOC ',	'3198',	'4188');");
		db.execSQL("insert into SongTable8 values(84,	'여행길',	'부가킹즈',	'15216',	'64861');");
		db.execSQL("insert into SongTable8 values(85,	'여행을 떠나요',	'조용필',	'2056',	'3493');");
		db.execSQL("insert into SongTable8 values(86,	'여행을 떠나요',	'이승기',	'19623',	'46303');");
		db.execSQL("insert into SongTable8 values(87,	'오동잎',	'최헌',	'476',	'596');");
		db.execSQL("insert into SongTable8 values(88,	'우리 기쁜 날',	'김장훈',	'9409',	'65723');");
		db.execSQL("insert into SongTable8 values(89,	'으샤! 으샤!',	'신화',	'4653',	'5507');");
		db.execSQL("insert into SongTable8 values(90,	'이렇게 눈이 와요',	'김형중',	'15509',	'45422');");
		db.execSQL("insert into SongTable8 values(91,	'일년전에',	'현승, 정은지(에이핑크), 김남주(에이핑크)',	'36282',	'77517');");
		db.execSQL("insert into SongTable8 values(92,	'잊혀진 계절',	'이용',	'512',	'659');");
		db.execSQL("insert into SongTable8 values(93,	'제주도의 푸른밤',	'성시경',	'13300',	'64367');");
		db.execSQL("insert into SongTable8 values(94,	'첫 눈이 온다구요',	'이정석',	'2757',	'1711');");
		db.execSQL("insert into SongTable8 values(95,	'춥다',	'에픽하이',	'35943',	'77415');");
		db.execSQL("insert into SongTable8 values(96,	'크리스마스니까',	'성시경, 박효신, 이석훈, 서인국, VIXX',	'36180',	'47944');");
		db.execSQL("insert into SongTable8 values(97,	'태양은 가득히',	'문차일드',	'8903',	'6353');");
		db.execSQL("insert into SongTable8 values(98,	'파도',	'UN',	'9588',	'6959');");
		db.execSQL("insert into SongTable8 values(99,	'팥빙수',	'윤종신',	'4243',	'60035');");
		db.execSQL("insert into SongTable8 values(100,	'하얀 겨울',	'Mr.2',	'1739',	'3216');");
		db.execSQL("insert into SongTable8 values(101,	'하얀 겨울',	'김범수,박정현',	'36159',	'77466');");
		db.execSQL("insert into SongTable8 values(102,	'하얀 설레임',	'케이윌,소유,정민',	'36150',	'47956');");
		db.execSQL("insert into SongTable8 values(103,	'하얀 전쟁',	'영턱스클럽',	'4275',	'5198');");
		db.execSQL("insert into SongTable8 values(104,	'하얀고백',	'인피니트',	'34730',	'87116');");
		db.execSQL("insert into SongTable8 values(105,	'한 여름날의 꿈',	'SG워너비',	'17639',	'81659');");
		db.execSQL("insert into SongTable8 values(106,	'해변으로 가요',	'키보이스',	'4020',	'767');");
		db.execSQL("insert into SongTable8 values(107,	'해변으로 가요',	'DJ DOC',	'8526',	'68838');");
		db.execSQL("insert into SongTable8 values(108,	'해변의 여인',	'쿨',	'3956',	'5036');");
		db.execSQL("insert into SongTable8 values(109,	'해변의 여인',	'나훈아',	'636',	'768');");
		db.execSQL("insert into SongTable8 values(110,	'허니문',	'터보',	'8381',	'6031');");
		db.execSQL("insert into SongTable8 values(111,	'혼자 맞는 겨울',	'이수영',	'10561',	'62740');");
		db.execSQL("insert into SongTable8 values(112,	'회상',	'터보',	'4162',	'5169');");
		db.execSQL("insert into SongTable8 values(113,	'흐린 가을 하늘에 편지를 써',	'이정',	'35059',	'58552');");
		db.execSQL("insert into SongTable8 values(114,	'흰 눈',	'이루',	'16786',	'45790');");
		db.execSQL("insert into SongTable8 values(115,	'Love Blossom (러브블러썸)',	'케이윌',	'36642',	'77582');");
		db.execSQL("insert into SongTable8 values(116,	'Hi Ya Ya 여름날',	'동방신기',	'14987',	'45257');");
		db.execSQL("insert into SongTable8 values(117,	'봄봄봄',	'로이킴',	'36707',	'48076');");
		db.execSQL("insert into SongTable8 values(118,	'콩떡빙수',	'악동뮤지션',	'36885',	'48117');");
		db.execSQL("insert into SongTable8 values(119,	'봄에게 바라는 것',	'포지션',	'36617',	'77580');");
		db.execSQL("insert into SongTable8 values(120,	'Happy Christmas',	'컨츄리꼬꼬',	'8565',	'6635');");
		db.execSQL("insert into SongTable8 values(121,	'봄비',	'바이브',	'36838',	'58991');");
		db.execSQL("insert into SongTable8 values(122,	'봄을 노래하다',	'40',	'36679',	'58963');");
		db.execSQL("insert into SongTable8 values(123,	'봄은 있었다',	'윤하',	'36764',	'87610');");
		db.execSQL("insert into SongTable8 values(124,	'내 봄으로',	'피아',	'36621',	'58945');");
		db.execSQL("insert into SongTable8 values(125,	'봄에게 바라는 것 ',	'더 포지션',	'36617',	'77580');");
		db.execSQL("insert into SongTable8 values(126,	'봄비 (구가의서 O.S.T)',	'백지영',	'36708',	'48081');");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable8");
		onCreate(db);
	}
}