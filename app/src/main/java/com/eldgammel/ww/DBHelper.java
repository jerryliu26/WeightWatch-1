package com.eldgammel.ww;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String name = "ww.sqlite3";
    private static final int version = 1;


    public DBHelper(Context ctx) {
        super(ctx, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE ww (" +
                "_id integer primary key autoincrement, " +
                "fName text not null, " +
                "cal int default 0, " +
                "amount int default 0);";

        db.execSQL(sql);
    }

    //SELECT SUM(credit) cr FROM course;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS ww;";
        db.execSQL(sql);
        this.onCreate(db);
    }
}
