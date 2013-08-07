package com.sto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sto.utils.StoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 2/27/13
 */
public class DBAdapter extends SQLiteOpenHelper {

    private InputStream insertStatementStream;

    //DataBase property
    private static final String DATABASE_NAME = "sto";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_NAME_STO = "sto_post";
    //columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SHORT_HISTORY = "short_story";
    public static final String COLUMN_TELEPHONE = "telephone";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_SITE = "site";
    public static final String COLUMN_WASH_TYPE = "wash_type";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "descr";
    public static final String COLUMN_CATEGORY = "category";


    //create scripts
    private String CREATE_TABLE_STO = "CREATE TABLE " + TABLE_NAME_STO + "("
            + COLUMN_ID + " INTEGER , "
            + COLUMN_SHORT_HISTORY + "  TEXT NOT NULL, "
            + COLUMN_TELEPHONE + " TEXT NOT NULL, "
            + COLUMN_LONGITUDE + " TEXT NOT NULL, "
            + COLUMN_LATITUDE + " TEXT NOT NULL, "
            + COLUMN_TIME + " TEXT NOT NULL, "
            + COLUMN_SITE + " TEXT NOT NULL, "
            + COLUMN_WASH_TYPE + " TEXT NOT NULL, "
            + COLUMN_TITLE + "  TEXT, "
            + COLUMN_DESCRIPTION + "  TEXT, "
            + COLUMN_CATEGORY + "  TEXT NOT NULL  DEFAULT '0' "
            + ");";


    public DBAdapter(Context context, InputStream insertStatementStream) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.insertStatementStream = insertStatementStream;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STO);
        List<String> insertStatements = StoUtils.getAllInsertStatements(insertStatementStream);
        for (String statement : insertStatements) {
            try {
                db.execSQL(statement);
            } catch (Exception e) {
                Log.e("ESTEO", e.getMessage());
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBAdapter.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STO);
        onCreate(db);
    }
}
