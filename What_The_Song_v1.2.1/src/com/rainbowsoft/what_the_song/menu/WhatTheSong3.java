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

public class WhatTheSong3 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_3);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_3",
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

		WhatTheSongDBHelper3 wtsHelper3 = new WhatTheSongDBHelper3(this, "whatthesong3.db", null, DATABASE_VERSION);
		db = wtsHelper3.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable3 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable3 where id= '"
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
		new AlertDialog.Builder(WhatTheSong3.this)
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
											WhatTheSong3.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong3.this,
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
			Intent newActivity = new Intent(WhatTheSong3.this,
					WhatTheSongBookmark.class);
			WhatTheSong3.this.startActivity(newActivity);
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
					"m_pref_3", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper3 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper3(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // '헤어짐 테마' DB (130630 : 243개)
		db.execSQL("CREATE TABLE SongTable3 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable3 values(1,	'…사랑했잖아…',	'린',	'13049',	'64331');");
		db.execSQL("insert into SongTable3 values(2,	'365일',	'알리',	'31733',	'84507');");
		db.execSQL("insert into SongTable3 values(3,	'3분 55초 간의 고백',	'Subway',	'12469',	'68533');");
		db.execSQL("insert into SongTable3 values(4,	'Again',	'쥬얼리',	'6057',	'9028');");
		db.execSQL("insert into SongTable3 values(5,	'Because of you',	'Kelly Clarkson',	'21359',	'61917');");
		db.execSQL("insert into SongTable3 values(6,	'Girlfriend',	'원더걸스',	'35470',	'77292');");
		db.execSQL("insert into SongTable3 values(7,	'Goodbye',	'Jessica',	'7787',	'5740');");
		db.execSQL("insert into SongTable3 values(8,	'Promise U',	'바이브',	'5869',	'9047');");
		db.execSQL("insert into SongTable3 values(9,	'Promise U',	'씨야',	'16043',	'85055');");
		db.execSQL("insert into SongTable3 values(10,	'So Cool',	'씨스타',	'34260',	'58352');");
		db.execSQL("insert into SongTable3 values(11,	'Solo',	'다이나믹듀오',	'30067',	'85916');");
		db.execSQL("insert into SongTable3 values(12,	'Someday',	'아이유',	'33494',	'76797');");
		db.execSQL("insert into SongTable3 values(13,	'Tonight',	'쥬얼리',	'10082',	'9173');");
		db.execSQL("insert into SongTable3 values(14,	'When You Told Me You Loved Me',	'Jessica Simpson',	'20842',	'61645');");
		db.execSQL("insert into SongTable3 values(15,	'You & I',	'먼데이키즈',	'36341',	'87528');");
		db.execSQL("insert into SongTable3 values(16,	'가난한 사랑',	'버즈',	'13513',	'64339');");
		db.execSQL("insert into SongTable3 values(17,	'가수가 된 이유',	'신용재',	'35668',	'77358');");
		db.execSQL("insert into SongTable3 values(18,	'가슴 시린 이야기',	'휘성',	'33754',	'47318');");
		db.execSQL("insert into SongTable3 values(19,	'가슴아 그만해',	'M.C the Max',	'17773',	'45907');");
		db.execSQL("insert into SongTable3 values(20,	'가시',	'버즈',	'14684',	'69033');");
		db.execSQL("insert into SongTable3 values(21,	'거기서 거기',	'다이나믹듀오',	'34853',	'77146');");
		db.execSQL("insert into SongTable3 values(22,	'거리에서',	'성시경',	'16503',	'45722');");
		db.execSQL("insert into SongTable3 values(23,	'걸음이 느린 아이',	'고유진',	'13088',	'9746');");
		db.execSQL("insert into SongTable3 values(24,	'고칠게',	'진원',	'19520',	'46280');");
		db.execSQL("insert into SongTable3 values(25,	'그 때 그 사람',	'심수봉',	'374',	'176');");
		db.execSQL("insert into SongTable3 values(26,	'그 애 참 싫다',	'아이유',	'35356',	'87252');");
		db.execSQL("insert into SongTable3 values(27,	'그녀를 사랑해줘요',	'하동균',	'16133',	'85143');");
		db.execSQL("insert into SongTable3 values(28,	'그녀에게 전화오게 하는 방법',	'015B',	'16448',	'45697');");
		db.execSQL("insert into SongTable3 values(29,	'그대 그리고 나',	'소리새',	'1568',	'999');");
		db.execSQL("insert into SongTable3 values(30,	'그대 돌아오면',	'거미',	'10830',	'9278');");
		db.execSQL("insert into SongTable3 values(31,	'그대 한 사람',	'김수현',	'35119',	'47674');");
		db.execSQL("insert into SongTable3 values(32,	'그댄 행복에 살텐데',	'리즈',	'10339',	'9241');");
		db.execSQL("insert into SongTable3 values(33,	'그래서 그대는',	'얀',	'9986',	'9034');");
		db.execSQL("insert into SongTable3 values(34,	'그리움만 쌓이네',	'노영심',	'2618',	'3782');");
		db.execSQL("insert into SongTable3 values(35,	'그리워 그리워',	'노을',	'34575',	'47547');");
		db.execSQL("insert into SongTable3 values(36,	'그저 바라볼 수만 있어도',	'유익종',	'419',	'2267');");
		db.execSQL("insert into SongTable3 values(37,	'기다리는 이유',	'임창정',	'9593',	'6975');");
		db.execSQL("insert into SongTable3 values(38,	'기억상실',	'거미',	'13902',	'9888');");
		db.execSQL("insert into SongTable3 values(39,	'기억을 걷는 시간',	'넬',	'19390',	'46244');");
		db.execSQL("insert into SongTable3 values(40,	'깊은 밤 슬픈 노래',	'E2RE',	'36161',	'47936');");
		db.execSQL("insert into SongTable3 values(41,	'깊은 밤 슬픈 노래',	'DK(디셈버)',	'36154',	'47951');");
		db.execSQL("insert into SongTable3 values(42,	'끝사랑',	'김범수',	'34049',	'47408');");
		db.execSQL("insert into SongTable3 values(43,	'나 어떡해',	'샌드페블즈',	'414',	'1136');");
		db.execSQL("insert into SongTable3 values(44,	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable3 values(45,	'나만 몰랐던 이야기',	'아이유',	'33651',	'47295');");
		db.execSQL("insert into SongTable3 values(46,	'나만의 슬픔',	'김돈규',	'3504',	'4835');");
		db.execSQL("insert into SongTable3 values(47,	'나비야',	'하동균',	'19225',	'83407');");
		db.execSQL("insert into SongTable3 values(48,	'날 위한 이별',	'김혜림',	'2412',	'3622');");
		db.execSQL("insert into SongTable3 values(49,	'날 위한 이별',	'린',	'15792',	'45514');");
		db.execSQL("insert into SongTable3 values(50,	'남자 때문에',	'주',	'19112',	'83369');");
		db.execSQL("insert into SongTable3 values(51,	'남자도 우나요',	'다비치',	'35674',	'77345');");
		db.execSQL("insert into SongTable3 values(52,	'내 꺼였는데',	'2am',	'35114',	'87191');");
		db.execSQL("insert into SongTable3 values(53,	'내 남자친구를 부탁해',	'윤하',	'33394',	'76757');");
		db.execSQL("insert into SongTable3 values(54,	'내 사랑 내 곁에',	'김현식',	'272',	'231');");
		db.execSQL("insert into SongTable3 values(55,	'내겐 사랑하나…',	'타루',	'19073',	'85694');");
		db.execSQL("insert into SongTable3 values(56,	'너도 나처럼',	'2am',	'35113',	'77212');");
		db.execSQL("insert into SongTable3 values(57,	'너를 보내고',	'윤도현',	'8589',	'6181');");
		db.execSQL("insert into SongTable3 values(58,	'너를 사랑하고도',	'전유나',	'428',	'236');");
		db.execSQL("insert into SongTable3 values(59,	'너를 사랑하고도',	'서영은',	'13827',	'68433');");
		db.execSQL("insert into SongTable3 values(60,	'너야',	'신용재',	'35691',	'87349');");
		db.execSQL("insert into SongTable3 values(61,	'너의 뒤에서',	'박진영',	'2526',	'3607');");
		db.execSQL("insert into SongTable3 values(62,	'넌 이별 난 아직',	'스탠딩에그',	'33763',	'86795');");
		db.execSQL("insert into SongTable3 values(63,	'눈감아줄께',	'블락비',	'35317',	'77255');");
		db.execSQL("insert into SongTable3 values(64,	'눈물',	'리쌍',	'36371',	'48002');");
		db.execSQL("insert into SongTable3 values(65,	'눈물샤워(Feat.에일리)',	'배치기',	'36321',	'47978');");
		db.execSQL("insert into SongTable3 values(66,	'눈물이 많아서',	'수지',	'34676',	'47572');");
		db.execSQL("insert into SongTable3 values(67,	'눈물이 안 났어',	'임정희',	'15278',	'64819');");
		db.execSQL("insert into SongTable3 values(68,	'느리게 하는 일',	'아이유',	'33416',	'76758');");
		db.execSQL("insert into SongTable3 values(69,	'늦은 후회',	'보보',	'9732',	'7482');");
		db.execSQL("insert into SongTable3 values(70,	'다시 사랑한다 말할까',	'김동률',	'9691',	'7618');");
		db.execSQL("insert into SongTable3 values(71,	'다시 사랑할 수 있을까',	'포맨 & 박정은',	'15671',	'45494');");
		db.execSQL("insert into SongTable3 values(72,	'다시 와주라',	'바이브',	'32596',	'47019');");
		db.execSQL("insert into SongTable3 values(73,	'당신은 모르실거야',	'혜은이',	'477',	'273');");
		db.execSQL("insert into SongTable3 values(74,	'당신은 모르실거야',	'핑클',	'9466',	'6818');");
		db.execSQL("insert into SongTable3 values(75,	'돌아올순 없나요',	'디셈버',	'31953',	'84651');");
		db.execSQL("insert into SongTable3 values(76,	'되돌리다',	'이승기',	'36121',	'47938');");
		db.execSQL("insert into SongTable3 values(77,	'두번 헤어지는 일',	'다비치',	'33163',	'76703');");
		db.execSQL("insert into SongTable3 values(78,	'뒷모습',	'나윤권',	'18647',	'46062');");
		db.execSQL("insert into SongTable3 values(79,	'떠나는 사람, 남겨진 사람',	'어반자카파',	'정보없음',	'87025');");
		db.execSQL("insert into SongTable3 values(80,	'떠나지마',	'T',	'30702',	'46548');");
		db.execSQL("insert into SongTable3 values(81,	'또르르',	'지연',	'32101',	'84727');");
		db.execSQL("insert into SongTable3 values(82,	'립스틱 짙게 바르고',	'임주리',	'644',	'317');");
		db.execSQL("insert into SongTable3 values(83,	'립스틱 짙게 바르고',	'다비치',	'19262',	'83374');");
		db.execSQL("insert into SongTable3 values(84,	'마음으로 ',	'인피니트',	'33472',	'86781');");
		db.execSQL("insert into SongTable3 values(85,	'마음이 다쳐서',	'나비',	'31025',	'84201');");
		db.execSQL("insert into SongTable3 values(86,	'마지막사랑',	'박기영',	'8378',	'6011');");
		db.execSQL("insert into SongTable3 values(87,	'멀어져 간 사람아',	'박상민',	'2064',	'3401');");
		db.execSQL("insert into SongTable3 values(88,	'멍하니',	'MissA',	'33137',	'76680');");
		db.execSQL("insert into SongTable3 values(89,	'모노드라마',	'허각',	'36365',	'47995');");
		db.execSQL("insert into SongTable3 values(90,	'모르시나요',	'다비치',	'36443',	'48014');");
		db.execSQL("insert into SongTable3 values(91,	'모를까봐서',	'쥬얼리',	'19255',	'85728');");
		db.execSQL("insert into SongTable3 values(92,	'못해',	'포맨',	'32125',	'46912');");
		db.execSQL("insert into SongTable3 values(93,	'미아',	'아이유',	'30197',	'46483');");
		db.execSQL("insert into SongTable3 values(94,	'미인',	'이기찬',	'17135',	'81439');");
		db.execSQL("insert into SongTable3 values(95,	'미치게 보고 싶은 ',	'태연',	'35179',	'47697');");
		db.execSQL("insert into SongTable3 values(96,	'바람이 분다',	'이소라',	'14425',	'69034');");
		db.execSQL("insert into SongTable3 values(97,	'배반',	'빅마마',	'18715',	'46080');");
		db.execSQL("insert into SongTable3 values(98,	'버려야 할 것들',	'별',	'16343',	'85187');");
		db.execSQL("insert into SongTable3 values(99,	'버스정류장',	'모던쥬스',	'14223',	'68610');");
		db.execSQL("insert into SongTable3 values(100,	'벌써 일년',	'브라운 아이즈',	'9551',	'6923');");
		db.execSQL("insert into SongTable3 values(101,	'별',	'M.C the Max',	'12489',	'64226');");
		db.execSQL("insert into SongTable3 values(102,	'보고 싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable3 values(103,	'보여줄게',	'에일리',	'35970',	'47889');");
		db.execSQL("insert into SongTable3 values(104,	'비가 오는 날엔',	'비스트',	'33927',	'76916');");
		db.execSQL("insert into SongTable3 values(105,	'비와 당신',	'럼블피쉬',	'30450',	'46485');");
		db.execSQL("insert into SongTable3 values(106,	'비창',	'이상우',	'1850',	'3332');");
		db.execSQL("insert into SongTable3 values(107,	'비행소녀',	'마골피',	'17375',	'81537');");
		db.execSQL("insert into SongTable3 values(108,	'사랑 안해',	'백지영',	'15871',	'45528');");
		db.execSQL("insert into SongTable3 values(109,	'사랑.. 그게 뭔데',	'양파',	'17970',	'45943');");
		db.execSQL("insert into SongTable3 values(110,	'사랑..그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable3 values(111,	'사랑아',	'더원',	'17884',	'81766');");
		db.execSQL("insert into SongTable3 values(112,	'사랑은 아프려고 하는 거죠',	'M.C the Max',	'15460',	'45420');");
		db.execSQL("insert into SongTable3 values(113,	'사랑은 언제나 목마르다',	'유미',	'9835',	'7809');");
		db.execSQL("insert into SongTable3 values(114,	'사랑의 썰물',	'임지훈',	'1162',	'1673');");
		db.execSQL("insert into SongTable3 values(115,	'사랑이 서럽다',	'이정',	'35228',	'77235');");
		db.execSQL("insert into SongTable3 values(116,	'사랑이 술을 가르쳐',	'이승기',	'32129',	'46913');");
		db.execSQL("insert into SongTable3 values(117,	'사랑이라 쓰고 아픔이라 부른다',	'서인영',	'32653',	'47041');");
		db.execSQL("insert into SongTable3 values(118,	'사랑이여',	'유심초',	'507',	'462');");
		db.execSQL("insert into SongTable3 values(119,	'사랑한 후에',	'박효신',	'31630',	'46778');");
		db.execSQL("insert into SongTable3 values(120,	'사랑한다는 흔한 말',	'김연우',	'15624',	'45468');");
		db.execSQL("insert into SongTable3 values(121,	'사랑해 미워해',	'이진성(먼데이키즈)',	'36436',	'48005');");
		db.execSQL("insert into SongTable3 values(122,	'사랑했지만',	'김광석',	'1102',	'1479');");
		db.execSQL("insert into SongTable3 values(123,	'사랑했지만',	'김경호',	'4261',	'7401');");
		db.execSQL("insert into SongTable3 values(124,	'삭제',	'이승기',	'13588',	'68473');");
		db.execSQL("insert into SongTable3 values(125,	'산책',	'박기영',	'9712',	'7641');");
		db.execSQL("insert into SongTable3 values(126,	'살다가 한번쯤',	'포맨',	'34009',	'76933');");
		db.execSQL("insert into SongTable3 values(127,	'서쪽하늘 (청연 O.S.T)',	'이승철',	'15527',	'45430');");
		db.execSQL("insert into SongTable3 values(128,	'선물',	'T',	'10596',	'62839');");
		db.execSQL("insert into SongTable3 values(129,	'세월이 가면',	'최호섭',	'817',	'499');");
		db.execSQL("insert into SongTable3 values(130,	'세월이 가면',	'박보람',	'정보없음',	'86738');");
		db.execSQL("insert into SongTable3 values(131,	'소주 한 잔',	'임창정',	'11491',	'9431');");
		db.execSQL("insert into SongTable3 values(132,	'수취인불명',	'프리스타일',	'17308',	'45866');");
		db.execSQL("insert into SongTable3 values(133,	'슬픈 인연',	'나미',	'2219',	'4466');");
		db.execSQL("insert into SongTable3 values(134,	'슬픈 인연',	'먼데이키즈',	'30500',	'85973');");
		db.execSQL("insert into SongTable3 values(135,	'슬픈 인연',	'장혜진',	'34064',	'76957');");
		db.execSQL("insert into SongTable3 values(136,	'슬픈다짐(Remix Ver)',	'다비치',	'19465',	'83483');");
		db.execSQL("insert into SongTable3 values(137,	'슬픔을 참는 세가지 방법',	'혜령',	'10910',	'9398');");
		db.execSQL("insert into SongTable3 values(138,	'슬픔활용법',	'김범수',	'30050',	'46390');");
		db.execSQL("insert into SongTable3 values(139,	'습관',	'롤러코스터',	'9040',	'7941');");
		db.execSQL("insert into SongTable3 values(140,	'시작이 좋아',	'버벌진트',	'36294',	'47974');");
		db.execSQL("insert into SongTable3 values(141,	'싫다',	'백지영',	'36281',	'77491');");
		db.execSQL("insert into SongTable3 values(142,	'심장이 없어',	'에이트',	'30913',	'46591');");
		db.execSQL("insert into SongTable3 values(143,	'아름다운 이별',	'김건모',	'2451',	'3657');");
		db.execSQL("insert into SongTable3 values(144,	'아파',	'2ne1',	'33060',	'86657');");
		db.execSQL("insert into SongTable3 values(145,	'아파 아이야',	'양파',	'33805',	'47334');");
		db.execSQL("insert into SongTable3 values(146,	'안녕 나야',	'포맨',	'36377',	'48003');");
		db.execSQL("insert into SongTable3 values(147,	'안녕이라고 말하지마',	'다비치',	'34338',	'47493');");
		db.execSQL("insert into SongTable3 values(148,	'안부',	'별',	'14716',	'68987');");
		db.execSQL("insert into SongTable3 values(149,	'어떤가요',	'박화요비',	'10170',	'9126');");
		db.execSQL("insert into SongTable3 values(150,	'어쩌죠',	'우이경',	'31986',	'정보없음');");
		db.execSQL("insert into SongTable3 values(151,	'언제나 그 자리에',	'신효범',	'1500',	'1566');");
		db.execSQL("insert into SongTable3 values(152,	'언젠가는',	'이상은',	'1406',	'1812');");
		db.execSQL("insert into SongTable3 values(153,	'얼음꽃 (야왕 O.S.T)',	'에일리',	'36379',	'58870');");
		db.execSQL("insert into SongTable3 values(154,	'오늘 헤어졌어요',	'윤하',	'31980',	'84668');");
		db.execSQL("insert into SongTable3 values(155,	'오랜만이야',	'임창정',	'30921',	'84147');");
		db.execSQL("insert into SongTable3 values(156,	'왜 나만 아프죠',	'아이비',	'32036',	'86390');");
		db.execSQL("insert into SongTable3 values(157,	'우리 사랑했잖아',	'다비치, 티아라',	'34807',	'58485');");
		db.execSQL("insert into SongTable3 values(158,	'웃으며 안녕',	'소야앤썬',	'32545',	'47007');");
		db.execSQL("insert into SongTable3 values(159,	'원하고 원망하죠',	'애즈원',	'9716',	'7626');");
		db.execSQL("insert into SongTable3 values(160,	'응급실',	'Izi',	'14515',	'45117');");
		db.execSQL("insert into SongTable3 values(161,	'이 노래',	'2am',	'19848',	'83678');");
		db.execSQL("insert into SongTable3 values(162,	'이러지마 제발',	'케이윌',	'35952',	'77420');");
		db.execSQL("insert into SongTable3 values(163,	'이럴거면',	'아이비',	'17238',	'81468');");
		db.execSQL("insert into SongTable3 values(164,	'이별에게',	'보보',	'10435',	'9169');");
		db.execSQL("insert into SongTable3 values(165,	'이별택시',	'김연우',	'12637',	'66767');");
		db.execSQL("insert into SongTable3 values(166,	'이별후애',	'원써겐',	'18108',	'81963');");
		db.execSQL("insert into SongTable3 values(167,	'있다 없으니까',	'씨스타19',	'36397',	'58875');");
		db.execSQL("insert into SongTable3 values(168,	'잘 이별하기',	'2am',	'35139',	'87203');");
		db.execSQL("insert into SongTable3 values(169,	'잠 못 드는 밤에',	'문명진',	'33797',	'58551');");
		db.execSQL("insert into SongTable3 values(170,	'전부 너였다',	'노을',	'15695',	'45485');");
		db.execSQL("insert into SongTable3 values(171,	'전활 받지 않는 너에게',	'2am',	'33225',	'58062');");
		db.execSQL("insert into SongTable3 values(172,	'좋아보여',	'버벌진트',	'34349',	'77024');");
		db.execSQL("insert into SongTable3 values(173,	'좋은사람',	'박효신',	'10036',	'9060');");
		db.execSQL("insert into SongTable3 values(174,	'죽고 싶단 말밖에',	'허각',	'34620',	'77094');");
		db.execSQL("insert into SongTable3 values(175,	'죽어도 못보내',	'2am',	'32138',	'46919');");
		db.execSQL("insert into SongTable3 values(176,	'지독하게',	'FTISLAND',	'34948',	'47639');");
		db.execSQL("insert into SongTable3 values(177,	'지우개',	'알리',	'36390',	'77526');");
		db.execSQL("insert into SongTable3 values(178,	'진짜일 리 없어',	'임정희',	'33126',	'47160');");
		db.execSQL("insert into SongTable3 values(179,	'찢긴 가슴',	'아이비',	'35308',	'47729');");
		db.execSQL("insert into SongTable3 values(180,	'촌스럽게 굴지마',	'알리',	'34760',	'47587');");
		db.execSQL("insert into SongTable3 values(181,	'총 맞은 것처럼',	'백지영',	'30425',	'83914');");
		db.execSQL("insert into SongTable3 values(182,	'출국',	'하림',	'9783',	'62129');");
		db.execSQL("insert into SongTable3 values(183,	'카페인',	'양요섭',	'36132',	'58814');");
		db.execSQL("insert into SongTable3 values(184,	'커피를 마시고',	'어반자카파',	'35776',	'86708');");
		db.execSQL("insert into SongTable3 values(185,	'쿨하게 헤어지는 방법',	'김진표',	'31090',	'84237');");
		db.execSQL("insert into SongTable3 values(186,	'통화 연결음',	'써니힐',	'18691',	'83188');");
		db.execSQL("insert into SongTable3 values(187,	'편지',	'어니언스',	'106',	'751');");
		db.execSQL("insert into SongTable3 values(188,	'하루가 십년이 되는 날',	'M.C the Max',	'12556',	'64249');");
		db.execSQL("insert into SongTable3 values(189,	'하루하루',	'빅뱅',	'19974',	'83751');");
		db.execSQL("insert into SongTable3 values(190,	'한 사람',	'에이트',	'34732',	'77108');");
		db.execSQL("insert into SongTable3 values(191,	'행복하니',	'배치기',	'36329',	'58871');");
		db.execSQL("insert into SongTable3 values(192,	'헤어지는 중입니다',	'이은미',	'30925',	'84155');");
		db.execSQL("insert into SongTable3 values(193,	'헤어지지 못하는 여자, 떠나가지 못하는 남자',	'리쌍',	'31718',	'84505');");
		db.execSQL("insert into SongTable3 values(194,	'혼자 있는 방',	'아이유',	'33631',	'86754');");
		db.execSQL("insert into SongTable3 values(195,	'홀로 된다는 것',	'변진섭',	'168',	'781');");
		db.execSQL("insert into SongTable3 values(196,	'화장을 고치고',	'왁스',	'9610',	'7513');");
		db.execSQL("insert into SongTable3 values(197,	'후회',	'조성모',	'4942',	'5717');");
		db.execSQL("insert into SongTable3 values(198,	'후회는 없어',	'노블레스',	'19140',	'46184');");
		db.execSQL("insert into SongTable3 values(199,	'후회한다',	'포맨',	'33315',	'86710');");
		db.execSQL("insert into SongTable3 values(200,	'흉터',	'먼데이키즈',	'19438',	'83478');");
		db.execSQL("insert into SongTable3 values(201,	'희나리',	'구창모',	'178',	'796');");
		db.execSQL("insert into SongTable3 values(202,	'희나리',	'김범수',	'34213',	'76984');");
		db.execSQL("insert into SongTable3 values(203,	'희재',	'성시경',	'10842',	'9290');");
		db.execSQL("insert into SongTable3 values(204,	'술이야',	'바이브',	'15819',	'69848');");
		db.execSQL("insert into SongTable3 values(205,	'몽중인',	'박정현',	'8265',	'5904');");
		db.execSQL("insert into SongTable3 values(206,	'널 위한 거짓말',	'문명진',	'14193',	'68580');");
		db.execSQL("insert into SongTable3 values(207,	'상처',	'문명진',	'6045',	'7519');");
		db.execSQL("insert into SongTable3 values(208,	'하루하루',	'문명진',	'10787',	'68773');");
		db.execSQL("insert into SongTable3 values(209,	'가르쳐줘요',	'문명진',	'12933',	'66983');");
		db.execSQL("insert into SongTable3 values(210,	'편지',	'김광진',	'9505',	'7301');");
		db.execSQL("insert into SongTable3 values(211,	'기다릴게 (공주의 남자 O.S.T)',	'하동균(원티드), 이정',	'34257',	'58353');");
		db.execSQL("insert into SongTable3 values(212,	'내가 싫다',	'케이윌',	'34949',	'58527');");
		db.execSQL("insert into SongTable3 values(213,	'서랍정리',	'빅마마',	'36267',	'77490');");
		db.execSQL("insert into SongTable3 values(214,	'썸데이 (싱글파파는 열애 중 O.S.T)',	'김동희',	'19281',	'46216');");
		db.execSQL("insert into SongTable3 values(215,	'눈물이 말랐대 (with 윤하)',	'소울다이브',	'36014',	'87439');");
		db.execSQL("insert into SongTable3 values(216,	'우린 같은 꿈을 꾼 거야 (Feat. 조유진)',	'015B',	'16764',	'81316');");
		db.execSQL("insert into SongTable3 values(217,	'이별이 온다',	'에이트',	'32582',	'57918');");
		db.execSQL("insert into SongTable3 values(218,	'끝이란 말이죠',	'레디오(Ready＇O)',	'17465',	'81692');");
		db.execSQL("insert into SongTable3 values(219,	'나는 그 사람이 아프다 (Feat. 타루)',	'에피톤 프로젝트',	'32759',	'87275');");
		db.execSQL("insert into SongTable3 values(220,	'그런 사랑',	'란',	'15898',	'85207');");
		db.execSQL("insert into SongTable3 values(221,	'허수아비',	'이하이',	'36120',	'47939');");
		db.execSQL("insert into SongTable3 values(222,	'해요',	'정인호',	'9540',	'7393');");
		db.execSQL("insert into SongTable3 values(223,	'Tuesday',	'T',	'11808',	'65614');");
		db.execSQL("insert into SongTable3 values(224,	'보낸다..',	'엠투엠(M To M)',	'32396',	'57869');");
		db.execSQL("insert into SongTable3 values(225,	'순애보',	'유리상자',	'4042',	'5091');");
		db.execSQL("insert into SongTable3 values(226,	'머리를 자르고',	'데이라이트',	'18081',	'81840');");
		db.execSQL("insert into SongTable3 values(227,	'냄새',	'김건모',	'11022',	'62961');");
		db.execSQL("insert into SongTable3 values(228,	'그립고 그립고 그립다',	'케이윌',	'31834',	'46831');");
		db.execSQL("insert into SongTable3 values(229,	'당신과의 키스를 세어보아요',	'박화요비',	'13549',	'64398');");
		db.execSQL("insert into SongTable3 values(230,	'난(亂)',	'옥주현',	'11477',	'9423');");
		db.execSQL("insert into SongTable3 values(231,	'사랑 후에',	'신혜성, 린',	'16717',	'45789');");
		db.execSQL("insert into SongTable3 values(232,	'인형 (with 신혜성)',	'이지훈',	'9370',	'6710');");
		db.execSQL("insert into SongTable3 values(233,	'왜 하늘은',	'이지훈',	'3580',	'4857');");
		db.execSQL("insert into SongTable3 values(234,	'북극성',	'강타',	'9615',	'7503');");
		db.execSQL("insert into SongTable3 values(235,	'보이지 않는 사랑',	'신승훈',	'1295',	'1043');");
		db.execSQL("insert into SongTable3 values(236,	'그런가봐요',	'V.One',	'12480',	'66553');");
		db.execSQL("insert into SongTable3 values(237,	'녹는 중',	'다비치',	'36628',	'58942');");
		db.execSQL("insert into SongTable3 values(238,	'딱 한 잔만',	'윤건',	'36676',	'58952');");
		db.execSQL("insert into SongTable3 values(239,	'미칠 것 같아',	'길구봉구',	'36661',	'87590');");
		db.execSQL("insert into SongTable3 values(240,	'슬픔만은 아니겠죠',	'문명진',	'36656',	'48066');");
		db.execSQL("insert into SongTable3 values(241,	'신촌을 못 가 ',	'포스트맨',	'36370',	'77581');");
		db.execSQL("insert into SongTable3 values(242,	'웃다 울다',	'서인국',	'36671',	'48071');");
		db.execSQL("insert into SongTable3 values(243,	'사랑은 봄비처럼 이별은 겨울비처럼',	'임현정',	'11171',	'7112');");
	}			

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable3");
		onCreate(db);
	}
}