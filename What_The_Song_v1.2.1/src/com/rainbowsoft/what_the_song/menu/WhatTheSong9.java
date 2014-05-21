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

public class WhatTheSong9 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_9);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_9",
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

		WhatTheSongDBHelper9 wtsHelper9 = new WhatTheSongDBHelper9(this, "whatthesong9.db", null, DATABASE_VERSION);
		db = wtsHelper9.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable9 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable9 where id= '"
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
		new AlertDialog.Builder(WhatTheSong9.this)
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
											WhatTheSong9.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong9.this,
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
			Intent newActivity = new Intent(WhatTheSong9.this,
					WhatTheSongBookmark.class);
			WhatTheSong9.this.startActivity(newActivity);
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
					"m_pref_9", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper9 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper9(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '응원 테마' DB (130627 : 101개)
		db.execSQL("CREATE TABLE SongTable9 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");
		
		db.execSQL("insert into SongTable9 values(1,	'Better Than Yesterday',	'MC스나이퍼',	'17485',	'81598');");
		db.execSQL("insert into SongTable9 values(2,	'Bravo, My Life!',	'봄여름가을겨울',	'9768',	'7728');");
		db.execSQL("insert into SongTable9 values(3,	'Breakaway',	'Kelly Clarkson',	'21156',	'61626');");
		db.execSQL("insert into SongTable9 values(4,	'Do It Yourself',	'레이지본',	'11697',	'63706');");
		db.execSQL("insert into SongTable9 values(5,	'Dream High',	'수지',	'33488',	'47256');");
		db.execSQL("insert into SongTable9 values(6,	'Dreaming',	'김수현',	'33654',	'58190');");
		db.execSQL("insert into SongTable9 values(7,	'Fly',	'에픽하이',	'15308',	'69606');");
		db.execSQL("insert into SongTable9 values(8,	'Fly',	'윤하',	'17522',	'81738');");
		db.execSQL("insert into SongTable9 values(9,	'Fly Again',	'별',	'18402',	'46012');");
		db.execSQL("insert into SongTable9 values(10,	'Girls Girls',	'원더걸스',	'34614',	'87083');");
		db.execSQL("insert into SongTable9 values(11,	'Happy Day',	'체리필터',	'16318',	'45673');");
		db.execSQL("insert into SongTable9 values(12,	'Hero',	'Mariah Carey',	'7317',	'8086');");
		db.execSQL("insert into SongTable9 values(13,	'I Believe I Can Fly',	'R.Kelly',	'7763',	'61152');");
		db.execSQL("insert into SongTable9 values(14,	'I Go',	'럼블피쉬',	'16105',	'45607');");
		db.execSQL("insert into SongTable9 values(15,	'Life Is Cool',	'Sweetbox',	'21059',	'61427');");
		db.execSQL("insert into SongTable9 values(16,	'Never Give Up',	'방용국',	'34966',	'87165');");
		db.execSQL("insert into SongTable9 values(17,	'One Shot',	'B.A.P',	'36433',	'48009');");
		db.execSQL("insert into SongTable9 values(18,	'Run',	'에픽하이',	'32329',	'84826');");
		db.execSQL("insert into SongTable9 values(19,	'Run To The Sky',	'M.C the Max',	'15932',	'45552');");
		db.execSQL("insert into SongTable9 values(20,	'Rush',	'리쌍',	'6086',	'9009');");
		db.execSQL("insert into SongTable9 values(21,	'Show',	'김원준',	'3272',	'4737');");
		db.execSQL("insert into SongTable9 values(22,	'Smile Boy',	'김연아,이승기',	'32654',	'57957');");
		db.execSQL("insert into SongTable9 values(23,	'Song For Me',	'박정현',	'35545',	'58672');");
		db.execSQL("insert into SongTable9 values(24,	'Superstar',	'쥬얼리',	'14687',	'45168');");
		db.execSQL("insert into SongTable9 values(25,	'Tomorrow',	'백지영',	'16332',	'45682');");
		db.execSQL("insert into SongTable9 values(26,	'Twenty',	'쥬얼리',	'정보없음',	'69548');");
		db.execSQL("insert into SongTable9 values(27,	'We Are The One',	'싸이',	'15914',	'85073');");
		db.execSQL("insert into SongTable9 values(28,	'You Raise Me Up',	'Westlife',	'21357',	'61922');");
		db.execSQL("insert into SongTable9 values(29,	'같이 걸을까',	'이적',	'33637',	'58178');");
		db.execSQL("insert into SongTable9 values(30,	'거꾸로 강을 거슬러 오르는 저 힘찬 연어들처럼',	'강산에',	'4582',	'5443');");
		db.execSQL("insert into SongTable9 values(31,	'거위의 꿈',	'인순이',	'16936',	'81408');");
		db.execSQL("insert into SongTable9 values(32,	'길',	'god',	'9706',	'7632');");
		db.execSQL("insert into SongTable9 values(33,	'꿈을 향해',	'김종국',	'15902',	'69912');");
		db.execSQL("insert into SongTable9 values(34,	'나는 나비',	'윤도현',	'16329',	'85183');");
		db.execSQL("insert into SongTable9 values(35,	'나는 문제 없어',	'황규영',	'1983',	'3346');");
		db.execSQL("insert into SongTable9 values(36,	'나를 외치다',	'마야',	'16585',	'81271');");
		db.execSQL("insert into SongTable9 values(37,	'나에게로 떠나는 여행',	'버즈',	'14828',	'45229');");
		db.execSQL("insert into SongTable9 values(38,	'난 괜찮아',	'진주',	'4263',	'5188');");
		db.execSQL("insert into SongTable9 values(39,	'날개',	'대성',	'35078',	'87202');");
		db.execSQL("insert into SongTable9 values(40,	'날개를 펴고',	'루나',	'32157',	'84746');");
		db.execSQL("insert into SongTable9 values(41,	'낭만고양이',	'체리필터',	'6093',	'62666');");
		db.execSQL("insert into SongTable9 values(42,	'넌 할 수 있어',	'강산에',	'2110',	'3438');");
		db.execSQL("insert into SongTable9 values(43,	'네 꿈을 펼쳐라',	'양희은',	'597',	'2303');");
		db.execSQL("insert into SongTable9 values(44,	'달리기',	'S.E.S',	'9847',	'7266');");
		db.execSQL("insert into SongTable9 values(45,	'달팽이',	'패닉',	'2881',	'4028');");
		db.execSQL("insert into SongTable9 values(46,	'당신은 사랑받기 위해 태어난 사람',	'러브',	'17020',	'정보없음');");
		db.execSQL("insert into SongTable9 values(47,	'덤벼라 세상아',	'바스코',	'18649',	'83154');");
		db.execSQL("insert into SongTable9 values(48,	'망설이지 마요',	'몽니',	'정보없음',	'86881');");
		db.execSQL("insert into SongTable9 values(49,	'뮤지컬',	'임상아',	'3643',	'4877');");
		db.execSQL("insert into SongTable9 values(50,	'버스를 잡자',	'김범수',	'정보없음',	'87037');");
		db.execSQL("insert into SongTable9 values(51,	'붉은노을',	'빅뱅',	'30399',	'83886');");
		db.execSQL("insert into SongTable9 values(52,	'비망록',	'버즈',	'14820',	'69097');");
		db.execSQL("insert into SongTable9 values(53,	'비상',	'임재범',	'9429',	'7153');");
		db.execSQL("insert into SongTable9 values(54,	'비타민',	'박학기',	'30108',	'85911');");
		db.execSQL("insert into SongTable9 values(55,	'빛',	'H.O.T',	'4789',	'5619');");
		db.execSQL("insert into SongTable9 values(56,	'사노라면',	'크라잉넛',	'6076',	'63318');");
		db.execSQL("insert into SongTable9 values(57,	'산다는건 다 그런게 아니겠니',	'여행스케치',	'2417',	'3564');");
		db.execSQL("insert into SongTable9 values(58,	'상록수',	'양희은',	'4780',	'5673');");
		db.execSQL("insert into SongTable9 values(59,	'선인장',	'우현,심규선',	'36504',	'87565');");
		db.execSQL("insert into SongTable9 values(60,	'소원을 말해봐',	'소녀시대',	'31311',	'46694');");
		db.execSQL("insert into SongTable9 values(61,	'수고했어,오늘도',	'옥상달빛',	'34779',	'58483');");
		db.execSQL("insert into SongTable9 values(62,	'슈퍼히어로',	'이승환',	'18765',	'46096');");
		db.execSQL("insert into SongTable9 values(63,	'아름다운 세상',	'유리상자',	'10256',	'7037');");
		db.execSQL("insert into SongTable9 values(64,	'아마추어',	'이승철',	'35924',	'47882');");
		db.execSQL("insert into SongTable9 values(65,	'안녕',	'박혜경',	'11588',	'9445');");
		db.execSQL("insert into SongTable9 values(66,	'에너지',	'마이티마우스',	'19747',	'46334');");
		db.execSQL("insert into SongTable9 values(67,	'오리 날다',	'체리필터',	'11932',	'9526');");
		db.execSQL("insert into SongTable9 values(68,	'요즘 너 말야',	'제이레빗',	'35364',	'정보없음');");
		db.execSQL("insert into SongTable9 values(69,	'울지마',	'브로콜리너마저',	'33601',	'86764');");
		db.execSQL("insert into SongTable9 values(70,	'웃는거야',	'서영은',	'15791',	'69841');");
		db.execSQL("insert into SongTable9 values(71,	'웃어요',	'오석준',	'549',	'1590');");
		db.execSQL("insert into SongTable9 values(72,	'으라차차',	'럼블피쉬',	'15090',	'45286');");
		db.execSQL("insert into SongTable9 values(73,	'이 세상 살아가다 보면',	'이문세',	'2306',	'2979');");
		db.execSQL("insert into SongTable9 values(74,	'일어나',	'김광석',	'2225',	'3455');");
		db.execSQL("insert into SongTable9 values(75,	'일탈',	'자우림',	'4282',	'5196');");
		db.execSQL("insert into SongTable9 values(76,	'즐거운 생활',	'45rpm',	'12481',	'62867');");
		db.execSQL("insert into SongTable9 values(77,	'지지마',	'전혜선',	'13980',	'68494');");
		db.execSQL("insert into SongTable9 values(78,	'챔피언',	'싸이',	'10062',	'9061');");
		db.execSQL("insert into SongTable9 values(79,	'촛불 하나',	'god',	'9247',	'6596');");
		db.execSQL("insert into SongTable9 values(80,	'출발',	'김동률',	'19195',	'83411');");
		db.execSQL("insert into SongTable9 values(81,	'팬이야',	'자우림',	'9977',	'9063');");
		db.execSQL("insert into SongTable9 values(82,	'하늘을 날아',	'메이트',	'31808',	'84482');");
		db.execSQL("insert into SongTable9 values(83,	'하늘을 달리다',	'이적',	'11378',	'63301');");
		db.execSQL("insert into SongTable9 values(84,	'하하하쏭',	'자우림',	'14078',	'45017');");
		db.execSQL("insert into SongTable9 values(85,	'혼자가 아닌 나',	'서영은',	'10738',	'9256');");
		db.execSQL("insert into SongTable9 values(86,	'혼자라고 생각말기',	'김보경',	'36254',	'58838');");
		db.execSQL("insert into SongTable9 values(87,	'힘 내! (Way To Go)',	'소녀시대',	'30640',	'84026');");
		db.execSQL("insert into SongTable9 values(88,	'힘내!!!',	'박정현',	'13270',	'68276');");
		db.execSQL("insert into SongTable9 values(89,	'힘을 내요',	'JNC',	'14100',	'68569');");
		db.execSQL("insert into SongTable9 values(90,	'힘을 내요, 미스터 김',	'롤러코스터',	'9073',	'6536');");
		db.execSQL("insert into SongTable9 values(91,	'힘을 냅시다',	'리아',	'19403',	'46251');");
		db.execSQL("insert into SongTable9 values(92,	'Stronger (What Doesn＇t Kill You)',	'Kelly Clarkson',	'22310',	'79056');");
		db.execSQL("insert into SongTable9 values(93,	'&Design',	'문근영',	'16766',	'45800');");
		db.execSQL("insert into SongTable9 values(94,	'Hi-Five',	'고유진',	'19659',	'46316');");
		db.execSQL("insert into SongTable9 values(95,	'비전',	'유승준',	'8566',	'6154');");
		db.execSQL("insert into SongTable9 values(96,	'마지막 승부 (마지막 승부 O.S.T)',	'김민교',	'1781',	'3304');");
		db.execSQL("insert into SongTable9 values(97,	'내일을 향해',	'신성우',	'949',	'1374');");
		db.execSQL("insert into SongTable9 values(98,	'미스코리아',	'이효리',	'36771',	'58978');");
		db.execSQL("insert into SongTable9 values(99,	'Holly Jolly Bus (Feat. 순심이)',	'이효리',	'36915',	'87627');");
		db.execSQL("insert into SongTable9 values(100,	'아마도 (직장의 신 OST)',	'10cm',	'36630',	'48063');");
		db.execSQL("insert into SongTable9 values(101,	'맨발의 청춘 ',	'벅',	'3624',	'4876');");
	}				

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable9");
		onCreate(db);
	}
}