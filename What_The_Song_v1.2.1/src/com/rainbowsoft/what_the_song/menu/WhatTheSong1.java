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

public class WhatTheSong1 extends ListActivity implements OnClickListener {
	private static final int DATABASE_VERSION = 2;
	private static final int LISTSIZE = 30;
	//private AdView adView = null;
	private static final String LOGTAG = "BannerTypeJava";
	ImageView My_list, Refresh, manual;
	//TextView tv, Singer;

	SQLiteDatabase db, mdb;
	Map<String, String> data;
	List<Map<String, String>> myList;
	SimpleAdapter adapter;
	Random rand = new Random();

	Cursor cursor;
	public static String searchUrl = "http://m.youtube.com/#/results?q=";

	String[] song = new String[LISTSIZE];
	String[] singer = new String[LISTSIZE];
	String[] tj = new String[LISTSIZE];
	String[] ky = new String[LISTSIZE];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.what_the_song_1);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref",
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

		WhatTheSongDBHelper1 wtsHelper1 = new WhatTheSongDBHelper1(this, "whatthesong1.db", null, DATABASE_VERSION);
		db = wtsHelper1.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable1 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable1 where id= '"
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
		new AlertDialog.Builder(WhatTheSong1.this)
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
											WhatTheSong1.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong1.this,
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.my_list) {
			Intent newActivity = new Intent(WhatTheSong1.this,
					WhatTheSongBookmark.class);
			WhatTheSong1.this.startActivity(newActivity);
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
					"m_pref", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper1 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper1(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '짝사랑 테마' DB (130630 : 160개)
		db.execSQL("CREATE TABLE SongTable1 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable1 values(1,	'1440',	'허각',	'36415',	'77548');");
		db.execSQL("insert into SongTable1 values(2,	'동경',	'박효신',	'9375',	'7378');");
		db.execSQL("insert into SongTable1 values(3,	'?(물음표)',	'프라이머리',	'36030',	'47911');");
		db.execSQL("insert into SongTable1 values(4,	'Breathe',	'Miss A',	'33108',	'47155');");
		db.execSQL("insert into SongTable1 values(5,	'Good bye',	'라디',	'36336',	'86855');");
		db.execSQL("insert into SongTable1 values(6,	'Here I am',	'윤상현',	'33527',	'76796');");
		db.execSQL("insert into SongTable1 values(7,	'illa illa(일라 일라)',	'주니엘',	'35460',	'77304');");
		db.execSQL("insert into SongTable1 values(8,	'Loving U',	'씨스타',	'35535',	'58670');");
		db.execSQL("insert into SongTable1 values(9,	'officially missing you',	'긱스',	'35828',	'86884');");
		db.execSQL("insert into SongTable1 values(10,	'Stop',	'Sam Brown',	'20019',	'60004');");
		db.execSQL("insert into SongTable1 values(11,	'가슴이 시린 게',	'이현',	'35496',	'47774');");
		db.execSQL("insert into SongTable1 values(12,	'가지마 가지마',	'브라운 아이즈',	'19762',	'46333');");
		db.execSQL("insert into SongTable1 values(13,	'가질 수 없는 너',	'뱅크',	'2730',	'3890');");
		db.execSQL("insert into SongTable1 values(14,	'겨울사랑',	'더원',	'36475',	'48026');");
		db.execSQL("insert into SongTable1 values(15,	'고해',	'임재범',	'9033',	'7033');");
		db.execSQL("insert into SongTable1 values(16,	'Goodbye To Romance',	'써니힐',	'36221',	'47960');");
		db.execSQL("insert into SongTable1 values(17,	'그 아픔까지 사랑한거야',	'조정현',	'1783',	'181');");
		db.execSQL("insert into SongTable1 values(18,	'그 애 참 싫다',	'아이유',	'35356',	'87252');");
		db.execSQL("insert into SongTable1 values(19,	'그XX',	'G-드래곤',	'35792',	'47851');");
		db.execSQL("insert into SongTable1 values(20,	'그남자',	'현빈',	'33519',	'47254');");
		db.execSQL("insert into SongTable1 values(21,	'그녀를 사랑해줘요',	'하동균',	'16133',	'85143');");
		db.execSQL("insert into SongTable1 values(22,	'그대라서',	'거미',	'32004',	'46876');");
		db.execSQL("insert into SongTable1 values(23,	'그대만 모르죠',	'나르샤',	'18401',	'83033');");
		db.execSQL("insert into SongTable1 values(24,	'그댄 행복에 살텐데',	'리즈',	'10339',	'9241');");
		db.execSQL("insert into SongTable1 values(25,	'그런 사람 또 없습니다',	'이승철',	'30751',	'46557');");
		db.execSQL("insert into SongTable1 values(26,	'그리워운다',	'신보라',	'35489',	'47770');");
		db.execSQL("insert into SongTable1 values(27,	'그여자',	'백지영',	'33282',	'76732');");
		db.execSQL("insert into SongTable1 values(28,	'기다리다',	'윤하',	'16677',	'85300');");
		db.execSQL("insert into SongTable1 values(29,	'긴 생머리 그녀',	'틴탑',	'36489',	'48033');");
		db.execSQL("insert into SongTable1 values(30,	'꿈에',	'박정현',	'9973',	'7955');");
		db.execSQL("insert into SongTable1 values(31,	'나 혼자서',	'티파니',	'30980',	'84193');");
		db.execSQL("insert into SongTable1 values(32,	'나무',	'보아',	'11479',	'63340');");
		db.execSQL("insert into SongTable1 values(33,	'나쁜놈(Feat.소야)',	'마이티마우스',	'35348',	'47747');");
		db.execSQL("insert into SongTable1 values(34,	'나였으면',	'나윤권',	'13584',	'64428');");
		db.execSQL("insert into SongTable1 values(35,	'날모르죠',	'린',	'19874',	'69506');");
		db.execSQL("insert into SongTable1 values(36,	'남자를 몰라',	'버즈',	'15978',	'85068');");
		db.execSQL("insert into SongTable1 values(37,	'너랑 나',	'아이유',	'34700',	'47581');");
		db.execSQL("insert into SongTable1 values(38,	'너의 뒤에서',	'박진영',	'2526',	'3607');");
		db.execSQL("insert into SongTable1 values(39,	'넌 감동이었어 ',	'성시경',	'5768',	'9006');");
		db.execSQL("insert into SongTable1 values(40,	'눈의 꽃',	'박효신',	'14238',	'68590');");
		db.execSQL("insert into SongTable1 values(41,	'단 한 사람',	'고유진',	'16284',	'81139');");
		db.execSQL("insert into SongTable1 values(42,	'들리나요',	'태연',	'30177',	'85940');");
		db.execSQL("insert into SongTable1 values(43,	'만약에',	'태연',	'19187',	'83377');");
		db.execSQL("insert into SongTable1 values(44,	'매력있어',	'악동뮤지션',	'36208',	'47970');");
		db.execSQL("insert into SongTable1 values(45,	'못해',	'포맨',	'32125',	'46912');");
		db.execSQL("insert into SongTable1 values(46,	'물고기자리',	'장나라',	'9811',	'60079');");
		db.execSQL("insert into SongTable1 values(47,	'미워도 다시 한번',	'바이브',	'9855',	'7804');");
		db.execSQL("insert into SongTable1 values(48,	'믿어요',	'동방신기',	'14056',	'45013');");
		db.execSQL("insert into SongTable1 values(49,	'바라만 봐도 좋은데',	'노리플라이',	'34144',	'76967');");
		db.execSQL("insert into SongTable1 values(50,	'별',	'유미',	'16790',	'85322');");
		db.execSQL("insert into SongTable1 values(51,	'별별별',	'소녀시대',	'32191',	'84753');");
		db.execSQL("insert into SongTable1 values(52,	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable1 values(53,	'비가 오는 날엔',	'비스트',	'33927',	'76916');");
		db.execSQL("insert into SongTable1 values(54,	'사랑 빛',	'CNBLUE',	'32583',	'57917');");
		db.execSQL("insert into SongTable1 values(55,	'사랑과 우정 사이',	'피노키오',	'1611',	'2797');");
		db.execSQL("insert into SongTable1 values(56,	'사랑이라면 (빅 O.S.T)',	'노을',	'35495',	'47783');");
		db.execSQL("insert into SongTable1 values(57,	'사랑하고 있네요',	'M.C. The Max',	'15823',	'45516');");
		db.execSQL("insert into SongTable1 values(58,	'사랑합니다',	'팀',	'11271',	'62988');");
		db.execSQL("insert into SongTable1 values(59,	'산골 소년의 사랑이야기',	'예민',	'1673',	'1779');");
		db.execSQL("insert into SongTable1 values(60,	'살다가 한번쯤',	'포맨',	'34009',	'76933');");
		db.execSQL("insert into SongTable1 values(61,	'상사병',	'박정현',	'10044',	'62699');");
		db.execSQL("insert into SongTable1 values(62,	'상상',	'씨엔블루',	'33787',	'58203');");
		db.execSQL("insert into SongTable1 values(63,	'선배',	'쥬얼리',	'10420',	'63643');");
		db.execSQL("insert into SongTable1 values(64,	'선생님 사랑해요',	'한스밴드',	'4821',	'5631');");
		db.execSQL("insert into SongTable1 values(65,	'소녀시대',	'소녀시대',	'18829',	'46107');");
		db.execSQL("insert into SongTable1 values(66,	'술 한 잔 해요',	'지아',	'31981',	'46868');");
		db.execSQL("insert into SongTable1 values(67,	'시작',	'박기영',	'8157',	'5861');");
		db.execSQL("insert into SongTable1 values(68,	'심장이 없어',	'에이트',	'30913',	'46591');");
		db.execSQL("insert into SongTable1 values(69,	'아는 오빠',	'미',	'36478',	'48027');");
		db.execSQL("insert into SongTable1 values(70,	'안되나요',	'휘성',	'9851',	'7859');");
		db.execSQL("insert into SongTable1 values(71,	'애인있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable1 values(72,	'여가',	'장연주',	'14511',	'68897');");
		db.execSQL("insert into SongTable1 values(73,	'여전히 아름다운지',	'김연우',	'4975',	'5781');");
		db.execSQL("insert into SongTable1 values(74,	'연애소설',	'가비앤제이',	'30620',	'84008');");
		db.execSQL("insert into SongTable1 values(75,	'웨딩드레스',	'태양',	'31870',	'84630');");
		db.execSQL("insert into SongTable1 values(76,	'인형의 꿈',	'러브홀릭',	'11994',	'64083');");
		db.execSQL("insert into SongTable1 values(77,	'잊을께',	'윤도현',	'11761',	'63748');");
		db.execSQL("insert into SongTable1 values(78,	'잘가요 내사랑',	'에이트',	'31257',	'46682');");
		db.execSQL("insert into SongTable1 values(79,	'잘해주지마요',	'김종국',	'32119',	'46908');");
		db.execSQL("insert into SongTable1 values(80,	'저녁하늘',	'에일리',	'35974',	'87409');");
		db.execSQL("insert into SongTable1 values(81,	'전할 수 없는 이야기',	'휘성',	'5732',	'9030');");
		db.execSQL("insert into SongTable1 values(82,	'점점',	'지연',	'33270',	'86722');");
		db.execSQL("insert into SongTable1 values(83,	'좋겠어',	'FTISLAND',	'35830',	'77391');");
		db.execSQL("insert into SongTable1 values(84,	'좋아보여',	'버벌진트',	'34349',	'77024');");
		db.execSQL("insert into SongTable1 values(85,	'좋은 사람 (Feat. 김형중)',	'토이',	'9526',	'6863');");
		db.execSQL("insert into SongTable1 values(86,	'좋은오빠동생으로만',	'바이브',	'32670',	'76562');");
		db.execSQL("insert into SongTable1 values(87,	'좋을텐데',	'성시경',	'10079',	'9704');");
		db.execSQL("insert into SongTable1 values(88,	'짝사랑',	'긱스',	'9269',	'6578');");
		db.execSQL("insert into SongTable1 values(89,	'짝사랑',	'산들',	'35540',	'58598');");
		db.execSQL("insert into SongTable1 values(90,	'짝사랑',	'포맨',	'34011',	'76947');");
		db.execSQL("insert into SongTable1 values(91,	'첫사랑',	'버스커버스커',	'35223',	'58564');");
		db.execSQL("insert into SongTable1 values(92,	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable1 values(93,	'취중진담',	'전람회',	'3134',	'4772');");
		db.execSQL("insert into SongTable1 values(94,	'친구의 고백',	'2AM',	'30954',	'46600');");
		db.execSQL("insert into SongTable1 values(95,	'큰일이다',	'V.O.S',	'31221',	'46675');");
		db.execSQL("insert into SongTable1 values(96,	'한남자',	'김종국',	'13448',	'9829');");
		db.execSQL("insert into SongTable1 values(97,	'한숨만',	'이정',	'15046',	'45273');");
		db.execSQL("insert into SongTable1 values(98,	'해바라기도 가끔 목이 아프죠',	'M.C. The Max',	'14292',	'68692');");
		db.execSQL("insert into SongTable1 values(99,	'혼자만 하는 사랑',	'거미',	'15258',	'69677');");
		db.execSQL("insert into SongTable1 values(100,	'화분',	'알렉스',	'19580',	'85816');");
		db.execSQL("insert into SongTable1 values(101,	'그랬나봐',	'김형중',	'10817',	'9279');");
		db.execSQL("insert into SongTable1 values(102,	'말도 안 돼',	'윤하',	'32414',	'46981');");
		db.execSQL("insert into SongTable1 values(103,	'세 글자 (루루공주 O.S.T)',	'엠투엠(M To M)',	'15204',	'45329');");
		db.execSQL("insert into SongTable1 values(104,	'넌 친구? 난 연인',	'UN',	'정보없음',	'69348');");
		db.execSQL("insert into SongTable1 values(105,	'그댄 달라요',	'버스커버스커',	'34638',	'87069');");
		db.execSQL("insert into SongTable1 values(106,	'옆 사람',	'보아',	'32899',	'76621');");
		db.execSQL("insert into SongTable1 values(107,	'사랑하고 싶었어',	'M.C. The Max',	'11312',	'63203');");
		db.execSQL("insert into SongTable1 values(108,	'사랑해도 괜찮니 (가문의 영광 O.S.T)',	'포맨',	'30629',	'84037');");
		db.execSQL("insert into SongTable1 values(109,	'짝사랑',	'정엽',	'33831',	'58209');");
		db.execSQL("insert into SongTable1 values(110,	'사랑하면 안되나요 (아이리스 O.S.T)',	'서인영',	'31966',	'46866');");
		db.execSQL("insert into SongTable1 values(111,	'YooHoo',	'시크릿',	'36753',	'58971');");
		db.execSQL("insert into SongTable1 values(112,	'방백(Aside)',	'샤이니',	'36467',	'77554');");
		db.execSQL("insert into SongTable1 values(113,	'바보가슴 (천명 O.S.T)',	'시아준수',	'36827',	'48096');");
		db.execSQL("insert into SongTable1 values(114,	'Bounce',	'조용필',	'36688',	'48072');");
		db.execSQL("insert into SongTable1 values(115,	'겁쟁이',	'버즈',	'14657',	'45151');");
		db.execSQL("insert into SongTable1 values(116,	'사랑에 빠진 딸기',	'타루',	'32311',	'86483');");
		db.execSQL("insert into SongTable1 values(117,	'짝사랑',	'딕펑스',	'36238',	'87451');");
		db.execSQL("insert into SongTable1 values(118,	'좋아해도 되나요 (…Is It OK?)',	'f(x)',	'33600',	'86831');");
		db.execSQL("insert into SongTable1 values(119,	'아는 여자 (아는 여자 O.S.T)',	'데이라이트',	'13543',	'68356');");
		db.execSQL("insert into SongTable1 values(120,	'사랑인걸요 (맨땅에 헤딩 O.S.T)',	'태연, 써니',	'31678',	'46795');");
		db.execSQL("insert into SongTable1 values(121,	'그래도 사랑해 (빅 O.S.T)',	'수지',	'35610',	'47803');");
		db.execSQL("insert into SongTable1 values(122,	'첫눈에',	'윤하',	'18802',	'83205');");
		db.execSQL("insert into SongTable1 values(123,	'사고쳤어요',	'다비치',	'30883',	'84150');");
		db.execSQL("insert into SongTable1 values(124,	'짝사랑',	'이하이',	'36556',	'58917');");
		db.execSQL("insert into SongTable1 values(125,	'Lovesick',	'산이',	'33124',	'86702');");
		db.execSQL("insert into SongTable1 values(126,	'Friend',	'원더걸스',	'정보없음',	'85692');");
		db.execSQL("insert into SongTable1 values(127,	'Chocolate (트리플 O.S.T)',	'타루',	'정보없음',	'84354');");
		db.execSQL("insert into SongTable1 values(128,	'오늘도 사랑해 (공주의 남자 O.S.T)',	'백지영',	'34178',	'58345');");
		db.execSQL("insert into SongTable1 values(129,	'아파도 괜찮아요 (김수로 O.S.T)',	'서현',	'32744',	'47064');");
		db.execSQL("insert into SongTable1 values(130,	'사랑한다 말할까 (장난스런 키스 O.S.T)',	'소유',	'33123',	'58056');");
		db.execSQL("insert into SongTable1 values(131,	'비밀',	'아이유',	'34701',	'58466');");
		db.execSQL("insert into SongTable1 values(132,	'처음 보는 나 (논스톱4 O.S.T)',	'봉태규',	'12542',	'9643');");
		db.execSQL("insert into SongTable1 values(133,	'Hush',	'Apink',	'35342',	'47745');");
		db.execSQL("insert into SongTable1 values(134,	'몰라요',	'Apink',	'38898',	'47366');");
		db.execSQL("insert into SongTable1 values(135,	'너라서 (빅 O.S.T)',	'다비치',	'35446',	'77287');");
		db.execSQL("insert into SongTable1 values(136,	'너라서 (빅 O.S.T)',	'공유',	'35647',	'87342');");
		db.execSQL("insert into SongTable1 values(137,	'그녀처럼',	'지아',	'32049',	'46892');");
		db.execSQL("insert into SongTable1 values(138,	'짝사랑',	'주현미',	'233',	'664');");
		db.execSQL("insert into SongTable1 values(139,	'아저씨 (Feat. 제이레빗)',	'김진표',	'35513',	'77312');");
		db.execSQL("insert into SongTable1 values(140,	'복숭아',	'아이유',	'35324',	'77261');");
		db.execSQL("insert into SongTable1 values(141,	'나를 사랑하지 않는 그대에게',	'이소라',	'13560',	'77353');");
		db.execSQL("insert into SongTable1 values(142,	'Creep',	'Radiohead',	'7740',	'8270');");
		db.execSQL("insert into SongTable1 values(143,	'고백',	'박혜경',	'8599',	'6195');");
		db.execSQL("insert into SongTable1 values(144,	'오빠 나빠',	'제시카, 티파니, 서현',	'19443',	'83477');");
		db.execSQL("insert into SongTable1 values(145,	'로맨틱하게',	'블락비',	'35977',	'58785');");
		db.execSQL("insert into SongTable1 values(146,	'널 사랑하겠어 (사랑을 보다2 O.S.T)',	'효린',	'34921',	'87148');");
		db.execSQL("insert into SongTable1 values(147,	'그리움을 사랑한 가시나무',	'테이',	'14581',	'68964');");
		db.execSQL("insert into SongTable1 values(148,	'혼자 하는 사랑',	'앤',	'12951',	'66937');");
		db.execSQL("insert into SongTable1 values(149,	'바보 (Feat. Bumkey)',	'에픽하이',	'32367',	'86474');");
		db.execSQL("insert into SongTable1 values(150,	'아시나요',	'조성모',	'9054',	'6512');");
		db.execSQL("insert into SongTable1 values(151,	'여우비 (내 여자친구는 구미호 O.S.T)',	'이선희',	'32933',	'47119');");
		db.execSQL("insert into SongTable1 values(152,	'눈치채줄래요',	'로맨틱펀치',	'36311',	'77546');");
		db.execSQL("insert into SongTable1 values(153,	'똑똑똑',	'포맨',	'31832',	'84604');");
		db.execSQL("insert into SongTable1 values(154,	'Baby Baby',	'소녀시대',	'19079',	'85698');");
		db.execSQL("insert into SongTable1 values(155,	'들었다 놨다',	'써니힐, 데이브레이크',	'36652',	'77593');");
		db.execSQL("insert into SongTable1 values(156,	'사랑은 은하수 다방에서 ',	'악동뮤지션',	'36599',	'58943');");
		db.execSQL("insert into SongTable1 values(157,	'Mad (Feat. 언터쳐블)',	'바다',	'31489',	'84426');");
		db.execSQL("insert into SongTable1 values(158,	'꿈에',	'조덕배',	'1515',	'2278');");
		db.execSQL("insert into SongTable1 values(159,	'나의 옛날 이야기',	'조덕배',	'1728',	'1492');");
		db.execSQL("insert into SongTable1 values(160,	'그대 내 맘에 들어오면은',	'조덕배',	'910',	'2249');");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable1");
		onCreate(db);
	}
}
