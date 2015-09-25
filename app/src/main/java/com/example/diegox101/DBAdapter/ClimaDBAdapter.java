package com.example.diegox101.DBAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.diegox101.sqlitedb.DBHelper;

import java.sql.SQLException;

/**
 * Created by diegoxgarcia on 24/9/15.
 */
public class ClimaDBAdapter {

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public ClimaDBAdapter(Context context) {
        this.context = context;
    }

    public ClimaDBAdapter abrir() throws SQLException{
        dbHelper = new DBHelper(this.context);
        db = dbHelper.getWritableDatabase();
        return this;
    }
}
