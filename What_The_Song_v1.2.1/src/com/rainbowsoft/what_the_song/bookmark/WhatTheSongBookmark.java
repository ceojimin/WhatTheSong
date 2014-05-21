package com.rainbowsoft.what_the_song.bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class WhatTheSongBookmark extends ListActivity implements
		OnClickListener {
	SQLiteDatabase mdb;
	Map<String, String> data;
	List<Map<String, String>> myList;
	SimpleAdapter adapter;
	Cursor cursor;
	ImageView removall;

	public static String searchUrl = "http://m.youtube.com/#/results?q=";

	String[] song;
	String[] singer;
	String[] tj;
	String[] ky;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whatthesongbookmark);

		removall = (ImageView) findViewById(R.id.removall);
		removall.setOnClickListener(this);

		myList = new ArrayList<Map<String, String>>();

		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		cursor = mdb.rawQuery("SELECT * FROM mysongTable ORDER BY song ASC",
				null);
		cursor.moveToFirst();

		if (cursor.getCount() != 0) {
			Toast.makeText(WhatTheSongBookmark.this,
					"즐겨찾기에 총 " + cursor.getCount() + "개의 곡이 저장되어 있습니다.",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(WhatTheSongBookmark.this, "즐겨찾기에 저장된 곡이 없습니다.",
					Toast.LENGTH_SHORT).show();
		}
		
		callList();
	}
	
	public void callList(){
		myList = new ArrayList<Map<String, String>>();

		WhatTheSongMyDBHelper wtsmHelper = new WhatTheSongMyDBHelper(this);
		mdb = wtsmHelper.getReadableDatabase();

		cursor = mdb.rawQuery("SELECT * FROM mysongTable ORDER BY song ASC",
				null);
		cursor.moveToFirst();

		song = new String[cursor.getCount()];
		singer = new String[cursor.getCount()];
		tj = new String[cursor.getCount()];
		ky = new String[cursor.getCount()];
		
		for (int i = 0; i < cursor.getCount(); i++) {
			song[i] = cursor.getString(1);
			singer[i] = cursor.getString(2);
			tj[i] = cursor.getString(3);
			ky[i] = cursor.getString(4);

			data = new HashMap<String, String>();
			data.put("song", song[i]);
			data.put("singer", singer[i]);
			myList.add(data);
			Log.i("db", song[i] + " : " + singer[i]);
			cursor.moveToNext();
		}

		adapter = new SimpleAdapter(this, myList, R.layout.list, new String[] {
				"song", "singer" }, new int[] { R.id.song, R.id.singer });

		setListAdapter(adapter);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final int p = position;
		new AlertDialog.Builder(WhatTheSongBookmark.this)
				.setTitle(song[p] + " - " + singer[p])
				.setIcon(R.drawable.icon_small)
				.setMessage("♪ 태진 : " + tj[p] + ", ♪ 금영 : " + ky[p])
				.setPositiveButton("즐겨찾기 삭제",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								mdb.execSQL("DELETE FROM mysongTable WHERE song = '"
										+ song[p]
										+ "' AND singer = '"
										+ singer[p] + "'");
								Toast.makeText(
										WhatTheSongBookmark.this,
										"' " + song[p] + " - " + singer[p]
												+ " ' 즐겨찾기에서 삭제 완료!",
										Toast.LENGTH_SHORT).show();								
								
								callList();
								
								if (myList.size() == 0) {
									Toast.makeText(WhatTheSongBookmark.this,
											"즐겨찾기에 저장된 곡이 없습니다.",
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
		mdb.close();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.removall) {
			new AlertDialog.Builder(WhatTheSongBookmark.this)
					.setTitle("즐겨찾기 전체 삭제")
					.setIcon(R.drawable.icon_small)
					.setMessage("즐겨찾기에 저장된 모든 곡을 삭제하시겠습니까?")
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (myList.size() == 0) {
										Toast.makeText(
												WhatTheSongBookmark.this,
												"삭제할 곡이 없습니다.",
												Toast.LENGTH_SHORT).show();
									} else {
										mdb.execSQL("DELETE FROM mysongTable ");
										// 리스트 삭제
										myList.removeAll(myList);
										adapter.notifyDataSetChanged();
										Toast.makeText(
												WhatTheSongBookmark.this,
												"모든 곡이 삭제되었습니다.",
												Toast.LENGTH_SHORT).show();
										if (myList.size() == 0) {
											Toast.makeText(WhatTheSongBookmark.this,
													"즐겨찾기에 저장된 곡이 없습니다.",
													Toast.LENGTH_SHORT).show();
										}
									}

								}
							})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}
	}
}