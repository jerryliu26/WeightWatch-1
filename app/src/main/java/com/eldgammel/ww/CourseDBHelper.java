package com.eldgammel.ww;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDBHelper extends SQLiteOpenHelper {

    private static final String name = "courses.sqlite3";
    private static final int version = 1;


    public CourseDBHelper(Context ctx) {
        super(ctx, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE course (" +
                "_id integer primary key autoincrement, " +
                "fName text not null, " +             // course code
                "credit int default 0, " +
                "amount int default 0);";

        db.execSQL(sql);
    }

    //SELECT SUM(credit) cr FROM course;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS course;";
        db.execSQL(sql);
        this.onCreate(db);
    }
}
