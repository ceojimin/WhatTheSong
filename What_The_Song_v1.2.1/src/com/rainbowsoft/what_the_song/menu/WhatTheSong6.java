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

public class WhatTheSong6 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_6);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_6",
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

		WhatTheSongDBHelper6 wtsHelper6 = new WhatTheSongDBHelper6(this, "whatthesong6.db", null, DATABASE_VERSION);
		db = wtsHelper6.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable6 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable6 where id= '"
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
		new AlertDialog.Builder(WhatTheSong6.this)
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
											WhatTheSong6.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong6.this,
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
			Intent newActivity = new Intent(WhatTheSong6.this,
					WhatTheSongBookmark.class);
			WhatTheSong6.this.startActivity(newActivity);
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
					"m_pref_6", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper6 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper6(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '회식 테마' DB (130630 : 225개)
		db.execSQL("CREATE TABLE SongTable6 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable6 values(1,	'8282',	'다비치',	'30868',	'46582');");
		db.execSQL("insert into SongTable6 values(2,	'10 Minutes',	'이효리',	'11833',	'9504');");
		db.execSQL("insert into SongTable6 values(3,	'3!4!',	'룰라',	'3162',	'4184');");
		db.execSQL("insert into SongTable6 values(4,	'Bad Girl Good Girl',	'MissA',	'32773',	'47073');");
		db.execSQL("insert into SongTable6 values(5,	'Blue Sky',	'박기영',	'8795',	'6264');");
		db.execSQL("insert into SongTable6 values(6,	'Bo Peep Bo Peep',	'티아라',	'31931',	'86368');");
		db.execSQL("insert into SongTable6 values(7,	'Cutie Honey',	'아유미',	'16268',	'45639');");
		db.execSQL("insert into SongTable6 values(8,	'Dash',	'백지영',	'8816',	'6296');");
		db.execSQL("insert into SongTable6 values(9,	'DOC와 춤을',	'DJ DOC',	'3976',	'5042');");
		db.execSQL("insert into SongTable6 values(10,	'Festival',	'엄정화',	'8342',	'5978');");
		db.execSQL("insert into SongTable6 values(11,	'Gee',	'소녀시대',	'30627',	'84011');");
		db.execSQL("insert into SongTable6 values(12,	'Go칼로리',	'뚱＇s',	'32657',	'47042');");
		db.execSQL("insert into SongTable6 values(13,	'Hot Issue',	'포미닛',	'31287',	'46703');");
		db.execSQL("insert into SongTable6 values(14,	'I Got A Boy',	'소녀시대',	'36269',	'47973');");
		db.execSQL("insert into SongTable6 values(15,	'Loving U',	'씨스타',	'35535',	'58670');");
		db.execSQL("insert into SongTable6 values(16,	'Ma boy',	'씨스타19',	'33886',	'76909');");
		db.execSQL("insert into SongTable6 values(17,	'Madonna',	'시크릿',	'32937',	'47106');");
		db.execSQL("insert into SongTable6 values(18,	'Magic',	'시크릿',	'32416',	'76501');");
		db.execSQL("insert into SongTable6 values(19,	'Maria',	'김아중',	'16712',	'45783');");
		db.execSQL("insert into SongTable6 values(20,	'Missing You',	'플라이투더스카이',	'11662',	'9466');");
		db.execSQL("insert into SongTable6 values(21,	'No.1',	'보아',	'9858',	'7868');");
		db.execSQL("insert into SongTable6 values(22,	'Nobody',	'원더걸스',	'30191',	'46428');");
		db.execSQL("insert into SongTable6 values(23,	'OH!',	'소녀시대',	'32153',	'46920');");
		db.execSQL("insert into SongTable6 values(24,	'Oh, My Julia',	'컨츄리꼬꼬',	'9249',	'6593');");
		db.execSQL("insert into SongTable6 values(25,	'One More Time',	'쥬얼리',	'19243',	'46212');");
		db.execSQL("insert into SongTable6 values(26,	'Pretty Girl',	'카라',	'30505',	'83953');");
		db.execSQL("insert into SongTable6 values(27,	'Roly-Poly',	'티아라',	'34109',	'47429');");
		db.execSQL("insert into SongTable6 values(28,	'Run To You',	'DJ DOC',	'8874',	'6349');");
		db.execSQL("insert into SongTable6 values(29,	'Show',	'김원준',	'3272',	'4737');");
		db.execSQL("insert into SongTable6 values(30,	'So Cool',	'씨스타',	'34260',	'58352');");
		db.execSQL("insert into SongTable6 values(31,	'Sorry Sorry',	'슈퍼주니어',	'30919',	'84145');");
		db.execSQL("insert into SongTable6 values(32,	'Tears',	'소찬휘',	'8797',	'6286');");
		db.execSQL("insert into SongTable6 values(33,	'Tell Me',	'원더걸스',	'18619',	'83136');");
		db.execSQL("insert into SongTable6 values(34,	'Twinkle',	'태티서',	'35315',	'47731');");
		db.execSQL("insert into SongTable6 values(35,	'U-Go-Girl',	'이효리',	'19854',	'83680');");
		db.execSQL("insert into SongTable6 values(36,	'Valenti',	'보아',	'10084',	'9099');");
		db.execSQL("insert into SongTable6 values(37,	'가는 세월',	'서유석',	'663',	'103');");
		db.execSQL("insert into SongTable6 values(38,	'강남스타일',	'싸이',	'35608',	'47802');");
		db.execSQL("insert into SongTable6 values(39,	'강북멋쟁이',	'정형돈',	'36287',	'77499');");
		db.execSQL("insert into SongTable6 values(40,	'개똥벌레',	'신형원',	'327',	'117');");
		db.execSQL("insert into SongTable6 values(41,	'검은 고양이',	'터보',	'2838',	'3947');");
		db.execSQL("insert into SongTable6 values(42,	'고등어',	'노라조',	'31376',	'84361');");
		db.execSQL("insert into SongTable6 values(43,	'고래사냥',	'송창식',	'789',	'808');");
		db.execSQL("insert into SongTable6 values(44,	'고향역',	'나훈아',	'3457',	'135');");
		db.execSQL("insert into SongTable6 values(45,	'곤드레 만드레',	'박현빈',	'16296',	'89095');");
		db.execSQL("insert into SongTable6 values(46,	'그 땐 그랬지',	'카니발',	'4115',	'5131');");
		db.execSQL("insert into SongTable6 values(47,	'그녀와의 이별',	'김현정',	'4635',	'5511');");
		db.execSQL("insert into SongTable6 values(48,	'그리움만 쌓이네',	'윤민수',	'34336',	'77035');");
		db.execSQL("insert into SongTable6 values(49,	'꿈의 대화',	'이범용',	'2006',	'1650');");
		db.execSQL("insert into SongTable6 values(50,	'나 이런 사람이야',	'DJ DOC',	'32901',	'86608');");
		db.execSQL("insert into SongTable6 values(51,	'나 항상 그대를',	'이선희',	'841',	'822');");
		db.execSQL("insert into SongTable6 values(52,	'낙원',	'싸이',	'10126',	'9177');");
		db.execSQL("insert into SongTable6 values(53,	'난 널 사랑해',	'신효범',	'1792',	'3326');");
		db.execSQL("insert into SongTable6 values(54,	'난 아직 사랑을 몰라',	'문근영',	'12998',	'64322');");
		db.execSQL("insert into SongTable6 values(55,	'난 알아요',	'서태지와 아이들',	'1294',	'1296');");
		db.execSQL("insert into SongTable6 values(56,	'날 봐, 귀순',	'대성',	'19749',	'46331');");
		db.execSQL("insert into SongTable6 values(57,	'날개 잃은 천사',	'룰라',	'2546',	'3735');");
		db.execSQL("insert into SongTable6 values(58,	'남자는 배 여자는 항구',	'심수봉',	'4021',	'223');");
		db.execSQL("insert into SongTable6 values(59,	'남행열차',	'김수희',	'911',	'1367');");
		db.execSQL("insert into SongTable6 values(60,	'낭만고양이',	'체리필터',	'6093',	'62666');");
		db.execSQL("insert into SongTable6 values(61,	'낭만에 대하여',	'최백호',	'2487',	'3792');");
		db.execSQL("insert into SongTable6 values(62,	'내 귀에 캔디',	'백지영',	'31525',	'46746');");
		db.execSQL("insert into SongTable6 values(63,	'내 생에 봄날은',	'캔',	'9763',	'7655');");
		db.execSQL("insert into SongTable6 values(64,	'내가 제일 잘 나가',	'2ne1',	'34084',	'47425');");
		db.execSQL("insert into SongTable6 values(65,	'너',	'이정현',	'8906',	'6351');");
		db.execSQL("insert into SongTable6 values(66,	'너는 왜',	'철이와 미애',	'1063',	'1498');");
		db.execSQL("insert into SongTable6 values(67,	'너랑 나',	'아이유',	'34700',	'47581');");
		db.execSQL("insert into SongTable6 values(68,	'널 그리며',	'박남정',	'351',	'241');");
		db.execSQL("insert into SongTable6 values(69,	'누나의 꿈',	'현영',	'15783',	'45509');");
		db.execSQL("insert into SongTable6 values(70,	'다시 만난 세계',	'소녀시대',	'18470',	'46023');");
		db.execSQL("insert into SongTable6 values(71,	'다짐',	'조성모',	'9066',	'6521');");
		db.execSQL("insert into SongTable6 values(72,	'다행이다',	'이적',	'17772',	'45912');");
		db.execSQL("insert into SongTable6 values(73,	'담배가게 아가씨',	'송창식',	'2682',	'2234');");
		db.execSQL("insert into SongTable6 values(74,	'담배가게 아가씨',	'윤도현밴드',	'4546',	'7212');");
		db.execSQL("insert into SongTable6 values(75,	'당돌한 여자',	'서주경',	'3665',	'2887');");
		db.execSQL("insert into SongTable6 values(76,	'동반자',	'태진아',	'13079',	'9744');");
		db.execSQL("insert into SongTable6 values(77,	'둥지',	'남진',	'8693',	'6235');");
		db.execSQL("insert into SongTable6 values(78,	'땡벌',	'강진',	'10136',	'7463');");
		db.execSQL("insert into SongTable6 values(79,	'라라라',	'SG워너비',	'19510',	'46283');");
		db.execSQL("insert into SongTable6 values(80,	'만남',	'노사연',	'615',	'328');");
		db.execSQL("insert into SongTable6 values(81,	'말해줘',	'지누션',	'3994',	'5060');");
		db.execSQL("insert into SongTable6 values(82,	'매직카펫라이드',	'자우림',	'8940',	'6404');");
		db.execSQL("insert into SongTable6 values(83,	'맨발의 청춘 ',	'벅',	'3624',	'4876');");
		db.execSQL("insert into SongTable6 values(84,	'먼지가 되어',	'로이킴&정준영',	'35965',	'47888');");
		db.execSQL("insert into SongTable6 values(85,	'멍',	'김현정',	'8869',	'6344');");
		db.execSQL("insert into SongTable6 values(86,	'몰라',	'엄정화',	'8290',	'5932');");
		db.execSQL("insert into SongTable6 values(87,	'무조건',	'박상철',	'14712',	'45167');");
		db.execSQL("insert into SongTable6 values(88,	'물방울 넥타이',	'현숙',	'30924',	'84131');");
		db.execSQL("insert into SongTable6 values(89,	'뮤지컬',	'임상아',	'3643',	'4877');");
		db.execSQL("insert into SongTable6 values(90,	'미쳐',	'이정현',	'9682',	'7620');");
		db.execSQL("insert into SongTable6 values(91,	'바다에 누워',	'높은음자리',	'723',	'379');");
		db.execSQL("insert into SongTable6 values(92,	'바람났어',	'GG',	'34122',	'58312');");
		db.execSQL("insert into SongTable6 values(93,	'반',	'이정현',	'9770',	'7727');");
		db.execSQL("insert into SongTable6 values(94,	'밤이 깊었네',	'크라잉넛',	'9550',	'6943');");
		db.execSQL("insert into SongTable6 values(95,	'밤이면 밤마다',	'인순이',	'1996',	'1542');");
		db.execSQL("insert into SongTable6 values(96,	'버스안에서',	'자자',	'3699',	'4879');");
		db.execSQL("insert into SongTable6 values(97,	'보고싶다',	'김범수',	'11095',	'62858');");
		db.execSQL("insert into SongTable6 values(98,	'부산 갈매기',	'문성재',	'492',	'419');");
		db.execSQL("insert into SongTable6 values(99,	'분홍립스틱',	'송윤아',	'10317',	'62758');");
		db.execSQL("insert into SongTable6 values(100,	'불꽃',	'코요태',	'13043',	'68003');");
		db.execSQL("insert into SongTable6 values(101,	'불타는 금요일',	'다이나믹듀오',	'34686',	'47582');");
		db.execSQL("insert into SongTable6 values(102,	'붉은 노을',	'빅뱅',	'30399',	'83886');");
		db.execSQL("insert into SongTable6 values(103,	'붉은 노을',	'이문세',	'1842',	'1482');");
		db.execSQL("insert into SongTable6 values(104,	'비상',	'코요태',	'11406',	'9417');");
		db.execSQL("insert into SongTable6 values(105,	'빈잔',	'남진',	'82',	'433');");
		db.execSQL("insert into SongTable6 values(106,	'빙고',	'거북이',	'14246',	'64589');");
		db.execSQL("insert into SongTable6 values(107,	'빙글빙글',	'나미',	'139',	'436');");
		db.execSQL("insert into SongTable6 values(108,	'뻐꾸기 둥지 위로 날아간 새',	'김건모',	'4273',	'5221');");
		db.execSQL("insert into SongTable6 values(109,	'뿌요뿌요',	'UP',	'3839',	'4968');");
		db.execSQL("insert into SongTable6 values(110,	'사랑…그 놈',	'바비킴',	'30661',	'84029');");
		db.execSQL("insert into SongTable6 values(111,	'사랑으로',	'해바라기',	'234',	'443');");
		db.execSQL("insert into SongTable6 values(112,	'사랑은 Move',	'시크릿',	'34546',	'47542');");
		db.execSQL("insert into SongTable6 values(113,	'사랑의 배터리',	'홍진영',	'31308',	'46697');");
		db.execSQL("insert into SongTable6 values(114,	'사랑이 다른 사랑으로 잊혀지네',	'하림',	'13234',	'68107');");
		db.execSQL("insert into SongTable6 values(115,	'상하이로맨스',	'오렌지캬라멜',	'34530',	'47531');");
		db.execSQL("insert into SongTable6 values(116,	'새',	'싸이',	'9365',	'6724');");
		db.execSQL("insert into SongTable6 values(117,	'샤방샤방',	'박현빈',	'19343',	'46249');");
		db.execSQL("insert into SongTable6 values(118,	'소녀시대',	'소녀시대',	'18829',	'46107');");
		db.execSQL("insert into SongTable6 values(119,	'순정',	'코요태',	'4959',	'5767');");
		db.execSQL("insert into SongTable6 values(120,	'슈퍼맨',	'노라조',	'30449',	'46489');");
		db.execSQL("insert into SongTable6 values(121,	'스피드',	'김건모',	'3112',	'4145');");
		db.execSQL("insert into SongTable6 values(122,	'쌈바의 여인',	'설운도',	'2854',	'3918');");
		db.execSQL("insert into SongTable6 values(123,	'아름다운 구속',	'김종서',	'3611',	'4906');");
		db.execSQL("insert into SongTable6 values(124,	'아름다운 밤',	'울랄라세션',	'35349',	'77275');");
		db.execSQL("insert into SongTable6 values(125,	'아잉♡',	'오렌지캬라멜',	'33306',	'47203');");
		db.execSQL("insert into SongTable6 values(126,	'아침이슬',	'양희은',	'11372',	'538');");
		db.execSQL("insert into SongTable6 values(127,	'아파트',	'윤수일',	'340',	'539');");
		db.execSQL("insert into SongTable6 values(128,	'안녕이라고 말하지마',	'다비치',	'34338',	'47493');");
		db.execSQL("insert into SongTable6 values(129,	'알 수 없는 인생',	'이문세',	'16255',	'85170');");
		db.execSQL("insert into SongTable6 values(130,	'압구정날라리',	'처진달팽이',	'34123',	'58314');");
		db.execSQL("insert into SongTable6 values(131,	'애인 있어요',	'이은미',	'15435',	'45387');");
		db.execSQL("insert into SongTable6 values(132,	'어머나',	'장윤정',	'12667',	'9647');");
		db.execSQL("insert into SongTable6 values(133,	'어젯밤이야기',	'소방차',	'2810',	'567');");
		db.execSQL("insert into SongTable6 values(134,	'어쩌다 마주친 그대',	'송골매',	'1209',	'1220');");
		db.execSQL("insert into SongTable6 values(135,	'얼쑤',	'윙크',	'32210',	'84782');");
		db.execSQL("insert into SongTable6 values(136,	'여행을 떠나요',	'이승기',	'19623',	'46303');");
		db.execSQL("insert into SongTable6 values(137,	'여행을 떠나요',	'조용필',	'2056',	'3493');");
		db.execSQL("insert into SongTable6 values(138,	'연예인',	'싸이',	'16256',	'45651');");
		db.execSQL("insert into SongTable6 values(139,	'열정',	'코요태',	'4609',	'6769');");
		db.execSQL("insert into SongTable6 values(140,	'열정',	'혜은이',	'1542',	'2475');");
		db.execSQL("insert into SongTable6 values(141,	'영원한 친구',	'나미',	'985',	'939');");
		db.execSQL("insert into SongTable6 values(142,	'오리날다',	'체리필터',	'11932',	'9526');");
		db.execSQL("insert into SongTable6 values(143,	'오빠만 믿어',	'박현빈',	'18469',	'46022');");
		db.execSQL("insert into SongTable6 values(144,	'요즘여자 요즘남자',	'현숙',	'3921',	'4936');");
		db.execSQL("insert into SongTable6 values(145,	'유행가',	'송대관',	'11526',	'63383');");
		db.execSQL("insert into SongTable6 values(146,	'이별 공식',	'R.ef',	'2647',	'3742');");
		db.execSQL("insert into SongTable6 values(147,	'이브의 경고',	'박미경',	'2699',	'3854');");
		db.execSQL("insert into SongTable6 values(148,	'이태원 프리덤',	'UV',	'38864',	'76861');");
		db.execSQL("insert into SongTable6 values(149,	'인디안 인형처럼',	'나미',	'14',	'645');");
		db.execSQL("insert into SongTable6 values(150,	'일과 이분의 일',	'투투',	'2133',	'3436');");
		db.execSQL("insert into SongTable6 values(151,	'일탈',	'자우림',	'4282',	'5196');");
		db.execSQL("insert into SongTable6 values(152,	'있잖아',	'아이유',	'31387',	'86220');");
		db.execSQL("insert into SongTable6 values(153,	'잘못된 만남',	'김건모',	'2454',	'3658');");
		db.execSQL("insert into SongTable6 values(154,	'잠시만 안녕',	'M.C. The Max',	'10290',	'9140');");
		db.execSQL("insert into SongTable6 values(155,	'젊은 그대',	'김수철',	'1238',	'1715');");
		db.execSQL("insert into SongTable6 values(156,	'제 3 한강교',	'혜은이',	'1696',	'1250');");
		db.execSQL("insert into SongTable6 values(157,	'좋은 날',	'아이유',	'33393',	'76754');");
		db.execSQL("insert into SongTable6 values(158,	'좋을텐데',	'성시경',	'10079',	'9704');");
		db.execSQL("insert into SongTable6 values(159,	'줄래',	'이정현',	'8953',	'6447');");
		db.execSQL("insert into SongTable6 values(160,	'진달래꽃',	'마야',	'11178',	'9382');");
		db.execSQL("insert into SongTable6 values(161,	'짠자라',	'장윤정',	'14868',	'69242');");
		db.execSQL("insert into SongTable6 values(162,	'쩔어',	'뚱＇s',	'36133',	'58796');");
		db.execSQL("insert into SongTable6 values(163,	'찬찬찬',	'편승엽',	'2148',	'3524');");
		db.execSQL("insert into SongTable6 values(164,	'찰랑찰랑',	'이자연',	'2689',	'3916');");
		db.execSQL("insert into SongTable6 values(165,	'챔피언',	'싸이',	'10062',	'9061');");
		db.execSQL("insert into SongTable6 values(166,	'천생연분',	'솔리드',	'3122',	'4713');");
		db.execSQL("insert into SongTable6 values(167,	'체념',	'빅마마',	'11019',	'63269');");
		db.execSQL("insert into SongTable6 values(168,	'초련',	'클론',	'8807',	'6308');");
		db.execSQL("insert into SongTable6 values(169,	'춤추는 탬버린',	'현숙',	'13924',	'45009');");
		db.execSQL("insert into SongTable6 values(170,	'콩가',	'컨츄리꼬꼬',	'9853',	'7878');");
		db.execSQL("insert into SongTable6 values(171,	'크레용',	'G-dragon',	'35854',	'47863');");
		db.execSQL("insert into SongTable6 values(172,	'킬리만자로의 표범',	'조용필',	'914',	'3366');");
		db.execSQL("insert into SongTable6 values(173,	'파란',	'코요태',	'9304',	'6691');");
		db.execSQL("insert into SongTable6 values(174,	'패션',	'코요태',	'9216',	'6582');");
		db.execSQL("insert into SongTable6 values(175,	'하늘땅 별땅',	'비비',	'4105',	'5141');");
		db.execSQL("insert into SongTable6 values(176,	'하늘땅 별땅',	'티아라',	'33121',	'58049');");
		db.execSQL("insert into SongTable6 values(177,	'하늘을 달리다',	'이적',	'11378',	'63301');");
		db.execSQL("insert into SongTable6 values(178,	'한 남자',	'김종국',	'13448',	'9829');");
		db.execSQL("insert into SongTable6 values(179,	'한 잔의 추억',	'이장희',	'609',	'765');");
		db.execSQL("insert into SongTable6 values(180,	'한동안 뜸 했었지',	'사랑과 평화',	'2054',	'2572');");
		db.execSQL("insert into SongTable6 values(181,	'행복한 나를',	'에코',	'4108',	'5168');");
		db.execSQL("insert into SongTable6 values(182,	'혼자한 사랑',	'김현정',	'4706',	'5588');");
		db.execSQL("insert into SongTable6 values(183,	'화려한 싱글',	'양혜승',	'11831',	'9511');");
		db.execSQL("insert into SongTable6 values(184,	'환희',	'정수라',	'25',	'784');");
		db.execSQL("insert into SongTable6 values(185,	'환희',	'싸이',	'15089',	'69351');");
		db.execSQL("insert into SongTable6 values(186,	'환희',	'코요태',	'12507',	'7969');");
		db.execSQL("insert into SongTable6 values(187,	'황진이',	'박상철',	'17554',	'45889');");
		db.execSQL("insert into SongTable6 values(188,	'황홀한 고백',	'윤수일',	'435',	'998');");
		db.execSQL("insert into SongTable6 values(189,	'훗',	'소녀시대',	'33222',	'47182');");
		db.execSQL("insert into SongTable6 values(190,	'흐린 기억 속의 그대',	'현진영',	'1183',	'1371');");
		db.execSQL("insert into SongTable6 values(191,	'흔들어 주세요',	'철싸',	'34131',	'58317');");
		db.execSQL("insert into SongTable6 values(192,	'어부바',	'장윤정',	'16515',	'81216');");
		db.execSQL("insert into SongTable6 values(193,	'자기야',	'박주희',	'15008',	'69323');");
		db.execSQL("insert into SongTable6 values(194,	'Poison',	'엄정화',	'4703',	'5549');");
		db.execSQL("insert into SongTable6 values(195,	'만남',	'코요태',	'8069',	'5824');");
		db.execSQL("insert into SongTable6 values(196,	'하늘에서 남자들이 비처럼 내려와',	'버블 시스터즈',	'10847',	'9288');");
		db.execSQL("insert into SongTable6 values(197,	'당신의 의미',	'이자연',	'472',	'860');");
		db.execSQL("insert into SongTable6 values(198,	'뿐이고',	'박구윤',	'31933',	'86342');");
		db.execSQL("insert into SongTable6 values(199,	'나 혼자 (Alone)',	'씨스타',	'35232',	'77243');");
		db.execSQL("insert into SongTable6 values(200,	'우연히',	'우연이',	'14662',	'68889');");
		db.execSQL("insert into SongTable6 values(201,	'진이',	'하이디',	'3135',	'4163');");
		db.execSQL("insert into SongTable6 values(202,	'자~엉덩이',	'원투',	'11531',	'9462');");
		db.execSQL("insert into SongTable6 values(203,	'그대에게',	'무한궤도',	'786',	'3555');");
		db.execSQL("insert into SongTable6 values(204,	'착각의 늪',	'박경림',	'9807',	'7781');");
		db.execSQL("insert into SongTable6 values(205,	'GENTLEMAN(젠틀맨)',	'싸이',	'36670',	'77604');");
		db.execSQL("insert into SongTable6 values(206,	'강원도 아리랑',	'민요(조용필)',	'5200',	'114');");
		db.execSQL("insert into SongTable6 values(207,	'얘기할 수 없어요',	'사랑과 평화',	'2077',	'4217');");
		db.execSQL("insert into SongTable6 values(208,	'삐에로는 우릴 보고 웃지',	'김완선',	'813',	'431');");
		db.execSQL("insert into SongTable6 values(209,	'디디디',	'김혜림',	'325',	'311');");
		db.execSQL("insert into SongTable6 values(210,	'Gimme! Gimme!',	'컨츄리꼬꼬',	'8336',	'5960');");
		db.execSQL("insert into SongTable6 values(211,	'Lucifer',	'샤이니',	'32848',	'47088');");
		db.execSQL("insert into SongTable6 values(212,	'Ring Ding Dong',	'샤이니',	'31754',	'46815');");
		db.execSQL("insert into SongTable6 values(213,	'캔디',	'H.O.T',	'3547',	'4843');");
		db.execSQL("insert into SongTable6 values(214,	'Run Devil Run',	'소녀시대',	'32358',	'46965');");
		db.execSQL("insert into SongTable6 values(215,	'열정',	'유승준',	'8144',	'5850');");
		db.execSQL("insert into SongTable6 values(216,	'찾길 바래',	'유승준',	'9255',	'6602');");
		db.execSQL("insert into SongTable6 values(217,	'부담',	'백지영',	'8453',	'6040');");
		db.execSQL("insert into SongTable6 values(218,	'비전',	'유승준',	'8566',	'6154');");
		db.execSQL("insert into SongTable6 values(219,	'Mad (Feat. 언터쳐블)',	'바다',	'31489',	'84426');");
		db.execSQL("insert into SongTable6 values(220,	'비행기',	'거북이',	'16223',	'45653');");
		db.execSQL("insert into SongTable6 values(221,	'기다려 늑대',	'줄리엣',	'4068',	'5132');");
		db.execSQL("insert into SongTable6 values(222,	'기다려 늑대',	'민효린',	'17926',	'85449');");
		db.execSQL("insert into SongTable6 values(223,	'떳다!! 그녀!!',	'위치스',	'10541',	'9199');");
		db.execSQL("insert into SongTable6 values(224,	'Chitty Chitty Bang Bang',	'이효리',	'32460',	'57878');");
		db.execSQL("insert into SongTable6 values(225,	'할 수 있어',	'NRG',	'4231',	'5199');");
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable6");
		onCreate(db);
	}
}