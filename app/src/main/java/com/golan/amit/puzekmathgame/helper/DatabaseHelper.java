package com.golan.amit.puzekmathgame.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.golan.amit.puzekmathgame.MainActivity;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String MATHGAME_SCORE_TABLE = "MATHGAME";
    public static final String COL_SCORE = "high_score";

    public DatabaseHelper(Context context) {
        super(context, "puzekmathgame.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INTEGER NOT NULL)",
                MATHGAME_SCORE_TABLE, COL_SCORE);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertHighScore(int aScore) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MATHGAME_SCORE_TABLE, null, null);

        ContentValues values = new ContentValues();
        values.put(COL_SCORE, aScore);
        db.insert(MATHGAME_SCORE_TABLE, null, values);

        db.close();
    }

    public int getHighScore() {
        int returnVal = -1;

        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT %s FROM %s", COL_SCORE, MATHGAME_SCORE_TABLE);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        try {
            returnVal = cursor.getInt(0);
            Log.d(MainActivity.DEBUGTAG, String.format("Got %d from db", returnVal));
        } catch (Exception ex) {
            Log.d(MainActivity.DEBUGTAG, "Could not get from db, exception occures");
        }

        db.close();
        return returnVal;
    }
}
