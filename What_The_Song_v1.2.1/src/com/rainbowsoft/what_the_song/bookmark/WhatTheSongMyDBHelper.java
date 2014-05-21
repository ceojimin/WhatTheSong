package com.rainbowsoft.what_the_song.bookmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WhatTheSongMyDBHelper extends SQLiteOpenHelper {
	public WhatTheSongMyDBHelper(Context context) {
		super(context, "mydb.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 즐겨찾기 DB 생성
		db.execSQL("CREATE TABLE mysongTable ( id INTEGER ," + " song TEXT ,"
				+ " singer TEXT ," + " taejin TEXT , " + " keumyoung TEXT);");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("DB OPEN", "DB OPEN OK");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {// db
																				// 업데이트
		db.execSQL("DROP TABLE IF EXISTS mysongTable");

		onCreate(db);
	}
}