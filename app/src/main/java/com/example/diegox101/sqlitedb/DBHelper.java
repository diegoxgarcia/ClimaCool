package com.example.diegox101.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by diegoxgarcia on 18/9/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static int version = 1;
    private static String name = "climaDb" ;
    private static SQLiteDatabase.CursorFactory factory = null;
    private static final String TABLE_CLIMAS_NAME = "climas";
    private static final String TABLE_CLIMALOCAL_NAME = "climalocal";


    public DBHelper(Context context) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CLIMAS_NAME +
                " (ID INTEGER PRIMARY KEY, NOMBRE TEXT, TEMP TEXT, TEMPMIN TEXT" +
                "TEMPMAX TEXT, HUMIDITY TEXT, PRESS TEXT, WIND TEXT, IMAGE_CODE TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CLIMALOCAL_NAME +
                " (ID INTEGER PRIMARY KEY, NOMBRE TEXT, TEMP TEXT, TEMPMIN TEXT" +
                "TEMPMAX TEXT, HUMIDITY TEXT, PRESS TEXT, WIND TEXT, IMAGE_CODE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
