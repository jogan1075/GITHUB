package com.webcontrol.captura;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "WC4Acceso.sqlite";
	private static final int DB_SCHEME_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_SCHEME_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DataBaseManager.CREATE_TABLE);
		db.execSQL(DataBaseManager.CREATE_TABLE2);
		db.execSQL(DataBaseManager.CREATE_TABLE3);
		db.execSQL(DataBaseManager.CREATE_TABLE4);
		db.execSQL(DataBaseManager.CREATE_TABLE5);
		db.execSQL(DataBaseManager.CREATE_TABLE6);
		db.execSQL(DataBaseManager.CREATE_TABLE7);
		db.execSQL(DataBaseManager.CREATE_TABLE8);
		db.execSQL(DataBaseManager.CREATE_TABLE9);
		db.execSQL(DataBaseManager.CREATE_TABLE10);
		db.execSQL(DataBaseManager.CREATE_TABLE11);
		db.execSQL(DataBaseManager.CREATE_TABLE12);
		db.execSQL(DataBaseManager.CREATE_TABLE13);
		db.execSQL(DataBaseManager.CREATE_TABLE14);
		db.execSQL(DataBaseManager.CREATE_TABLE15);
	//	db.execSQL(DataBaseManager.CREATE_TABLE16);
		db.execSQL(DataBaseManager.CREATE_TABLE17);
		db.execSQL(DataBaseManager.CREATE_TABLE18);
		db.execSQL(DataBaseManager.CREATE_TABLE19);
		db.execSQL(DataBaseManager.CREATE_TABLE20);
		db.execSQL(DataBaseManager.CREATE_TABLE21);
		//db.execSQL(DataBaseManager.CREATE_TABLE22);
		//db.execSQL(DataBaseManager.CREATE_TABLE23);
		db.execSQL(DataBaseManager.CREATE_TABLE24);
		db.execSQL(DataBaseManager.CREATE_TABLE25);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
