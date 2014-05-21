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

public class WhatTheSong12 extends ListActivity implements OnClickListener {
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
		setContentView(R.layout.what_the_song_12);
		
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

		SharedPreferences m_pref = getSharedPreferences("m_pref_12",
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

		WhatTheSongDBHelper12 wtsHelper12 = new WhatTheSongDBHelper12(this, "whatthesong12.db", null, DATABASE_VERSION);
		db = wtsHelper12.getReadableDatabase();
		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);

		while (count != LISTSIZE) {
			boolean tf = true;
			cursor = db.rawQuery("SELECT * FROM SongTable12 ", null);
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
			cursor = db.rawQuery("SELECT * FROM SongTable12 where id= '"
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
		new AlertDialog.Builder(WhatTheSong12.this)
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
											WhatTheSong12.this,
											"' " + song[p] + " - " + singer[p]
													+ " ' 즐겨찾기에 추가 완료!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(WhatTheSong12.this,
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
			Intent newActivity = new Intent(WhatTheSong12.this,
					WhatTheSongBookmark.class);
			WhatTheSong12.this.startActivity(newActivity);
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
					"m_pref_12", Activity.MODE_PRIVATE);
			// UI 상태를 저장합니다.
			SharedPreferences.Editor editor = m_pref
					.edit(); // Editor를 불러옵니다.
			editor.putString("check_status", version);
			editor.commit(); // 저장합니다.
			manual.setVisibility(View.INVISIBLE);
		}
	}
}

class WhatTheSongDBHelper12 extends SQLiteOpenHelper {
	public WhatTheSongDBHelper12(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {   // '특이 테마' DB (130627 : 54개)
		db.execSQL("CREATE TABLE SongTable12 ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");

		db.execSQL("insert into SongTable12 values(1,	'1분만 닥쳐줄래요',	'넬',	'19774', '83610'	);");
		db.execSQL("insert into SongTable12 values(2,	'Ghost School',	'이브',	'12254', '9583'	);");
		db.execSQL("insert into SongTable12 values(3,	'Vampire',	'이브',	'12340', '64205'	);");
		db.execSQL("insert into SongTable12 values(4,	'강북멋쟁이',	'정형돈',	'36287', '77499'	);");
		db.execSQL("insert into SongTable12 values(5,	'귀요미송',	'하리',	'정보없음', '58908'	);");
		db.execSQL("insert into SongTable12 values(6,	'그 어릿광대의 세 아들들에 대하여',	'패닉',	'3430', '4834'	);");
		db.execSQL("insert into SongTable12 values(7,	'기억을 지워주는 병원',	'팻두',	'정보없음', '86860'	);");
		db.execSQL("insert into SongTable12 values(8,	'꺼져',	'형돈이와 대준이',	'36483', '58909'	);");
		db.execSQL("insert into SongTable12 values(9,	'나 좀 만나줘',	'형돈이와 대준이',	'36434', '정보없음'	);");
		db.execSQL("insert into SongTable12 values(10,	'나이가 나를 먹다',	'닥터코어 911',	'19732', '46325'	);");
		db.execSQL("insert into SongTable12 values(11,	'내.도.소 (내 도망간 여자친구를 소개합니다)',	'노라조',	'30466', '86014'	);");
		db.execSQL("insert into SongTable12 values(12,	'내가 너의 오아시스가 되어줄께',	'팻두',	'정보없음', '86775'	);");
		db.execSQL("insert into SongTable12 values(13,	'니가 진짜로 원하는 게 뭐야',	'크래쉬',	'4098', '9394'	);");
		db.execSQL("insert into SongTable12 values(14,	'다리꼬지마',	'악동뮤지션',	'36127', '58811'	);");
		db.execSQL("insert into SongTable12 values(15,	'달이 차오른다, 가자',	'장기하와 얼굴들',	'30880', '46583'	);");
		db.execSQL("insert into SongTable12 values(16,	'라면인건가',	'악동뮤지션',	'36454', '48019'	);");
		db.execSQL("insert into SongTable12 values(17,	'빙',	'거리의 시인들',	'9042', '9352'	);");
		db.execSQL("insert into SongTable12 values(18,	'사랑의 병원으로 놀러오세요',	'자우림',	'14101', '64544'	);");
		db.execSQL("insert into SongTable12 values(19,	'석봉아',	'불나방 스타 쏘세지 클럽',	'31344', '84358'	);");
		db.execSQL("insert into SongTable12 values(20,	'싸구려 커피',	'장기하와 얼굴들',	'30296', '83864'	);");
		db.execSQL("insert into SongTable12 values(21,	'안 좋을 때 들으면 더 안 좋은 노래',	'형돈이와 대준이',	'35449', '77288'	);");
		db.execSQL("insert into SongTable12 values(22,	'안양1번가',	'MC스나이퍼',	'정보없음', '85634'	);");
		db.execSQL("insert into SongTable12 values(23,	'앵콜요청금지',	'브로콜리너마저',	'19588', '46295'	);");
		db.execSQL("insert into SongTable12 values(24,	'영계백숙',	'애프터쉐이빙',	'31408', '86240'	);");
		db.execSQL("insert into SongTable12 values(25,	'영맨',	'이박사',	'정보없음', '7186'	);");
		db.execSQL("insert into SongTable12 values(26,	'오빠 나 추워',	'리미와 감자',	'86790', '정보없음'	);");
		db.execSQL("insert into SongTable12 values(27,	'이태원프리덤',	'UV',	'38864', '76861'	);");
		db.execSQL("insert into SongTable12 values(28,	'인생극장 A형',	'싸이',	'정보없음', '85128'	);");
		db.execSQL("insert into SongTable12 values(29,	'인생극장 B형',	'싸이',	'정보없음', '85141'	);");
		db.execSQL("insert into SongTable12 values(30,	'인생의 참된 것',	'안재환',	'14622', '45137'	);");
		db.execSQL("insert into SongTable12 values(31,	'장가갈 수 있을까',	'커피소년',	'35216', '87234'	);");
		db.execSQL("insert into SongTable12 values(32,	'좋아해줘',	'검정치마',	'32371', '46525'	);");
		db.execSQL("insert into SongTable12 values(33,	'죽은 친구와의 전화',	'팻두',	'정보없음', '86776'	);");
		db.execSQL("insert into SongTable12 values(34,	'착한 늑대와 나쁜 돼지새끼 3마리',	'거리의 시인들',	'9109', '6906'	);");
		db.execSQL("insert into SongTable12 values(35,	'착한 아이',	'크라잉넛',	'31523', '84427'	);");
		db.execSQL("insert into SongTable12 values(36,	'참아주세요',	'김혜연',	'18724', '83198'	);");
		db.execSQL("insert into SongTable12 values(37,	'춤이 뭐길래',	'량현량하',	'8679', '6209'	);");
		db.execSQL("insert into SongTable12 values(38,	'쿨하지 못해 미안해',	'UV',	'32512', '46990'	);");
		db.execSQL("insert into SongTable12 values(39,	'킬리만자로의 표범',	'조용필',	'914', '3366'	);");
		db.execSQL("insert into SongTable12 values(40,	'홍콩반점',	'리미와 감자',	'86468', '정보없음'	);");
		db.execSQL("insert into SongTable12 values(41,	'흥보가 기가 막혀',	'육각수',	'2715', '3869'	);");
		db.execSQL("insert into SongTable12 values(42,	'희맨사항',	'형돈이와 대준이',	'36492', '58907'	);");
		db.execSQL("insert into SongTable12 values(43,	'오빠는 풍각쟁이',	'박향림',	'13168', '68131'	);");
		db.execSQL("insert into SongTable12 values(44,	'룩셈부르크',	'크라잉넛',	'16217',	'85171');");
		db.execSQL("insert into SongTable12 values(45,	'아메리카노',	'10cm',	'32985',	'86618');");
		db.execSQL("insert into SongTable12 values(46,	'자~엉덩이',	'원투',	'11531',	'9462');");
		db.execSQL("insert into SongTable12 values(47,	'오빠들은 못 생겨서 싫어요',	'장미여관',	'36703',	'58967');");
		db.execSQL("insert into SongTable12 values(48,	'아리조나 카우보이',	'명국환',	'38',	'527');");
		db.execSQL("insert into SongTable12 values(49,	'세상은 요지경',	'신신애',	'1433',	'2167');");
		db.execSQL("insert into SongTable12 values(50,	'랩 운동',	'더블K',	'36675',	'87583');");
		db.execSQL("insert into SongTable12 values(51,	'외국인의 고백',	'악동뮤지션',	'36644',	'77594');");
		db.execSQL("insert into SongTable12 values(52,	'지구인?',	'10cm',	'36624',	'87586');");
		db.execSQL("insert into SongTable12 values(53,	'공주는 외로워',	'김자옥',	'3549',	'4819');");
		db.execSQL("insert into SongTable12 values(54,	'진화론',	'예레미',	'2720',	'64087');");
	}				

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SongTable12");
		onCreate(db);
	}
}