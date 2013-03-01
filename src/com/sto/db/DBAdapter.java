package com.sto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String COLUMN_AUTHOR = "autor";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SHORT_HISTORY = "short_story";
    public static final String COLUMN_FULL_HISTORY = "full_story";
    public static final String COLUMN_XFIELDS = "xfields";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "descr";
    public static final String COLUMN_KEYWORDS = "keywords";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ALT_NAME = "alt_name";
    public static final String COLUMN_COMM_NUM = "comm_num";
    public static final String COLUMN_ALLOW_COMM = "allow_comm";
    public static final String COLUMN_ALLOW_MAIN = "allow_main";
    public static final String COLUMN_APPROVE = "approve";
    public static final String COLUMN_FIXED = "fixed";
    public static final String COLUMN_ALLOW_BR = "allow_br";
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_MATA_TITLE = "metatitle";


    //create scripts
    private String CREATE_TABLE_STO = "CREATE TABLE " + TABLE_NAME_STO + "("
            + COLUMN_ID + " INTEGER , "
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_DATE + " DATE, "
            + COLUMN_SHORT_HISTORY + "  TEXT NOT NULL, "
            + COLUMN_FULL_HISTORY + "  TEXT NOT NULL, "
            + COLUMN_XFIELDS + " TEXT NOT NULL, "
            + COLUMN_TITLE + "  TEXT, "
            + COLUMN_DESCRIPTION + "  TEXT, "
            + COLUMN_KEYWORDS + "  TEXT NOT NULL,"
            + COLUMN_CATEGORY + "  TEXT NOT NULL  DEFAULT '0', "
            + COLUMN_ALT_NAME + "  TEXT, "
            + COLUMN_COMM_NUM + "  INTEGER NOT NULL DEFAULT '0', "
            + COLUMN_ALLOW_COMM + "  INTEGER NOT NULL DEFAULT '1', "
            + COLUMN_ALLOW_MAIN + "  INTEGER NOT NULL DEFAULT '1', "
            + COLUMN_APPROVE + "  INTEGER NOT NULL DEFAULT '0', "
            + COLUMN_FIXED + "  INTEGER NOT NULL DEFAULT '0', "
            + COLUMN_ALLOW_BR + "  INTEGER NOT NULL DEFAULT '1', "
            + COLUMN_SYMBOL + "  TEXT NOT NULL DEFAULT '', "
            + COLUMN_TAGS + "  TEXT NOT NULL DEFAULT '', "
            + COLUMN_MATA_TITLE + "  TEXT NOT NULL DEFAULT '' "
            +");";


    public DBAdapter(Context context, InputStream insertStatementStream) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.insertStatementStream = insertStatementStream;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STO);
        List<String> insertStatements = DBUtility.getAllInsertStatements(insertStatementStream);
        for( String statement : insertStatements){
            db.execSQL(statement);
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
