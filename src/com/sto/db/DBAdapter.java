package com.sto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 2/27/13
 */
public class DBAdapter extends SQLiteOpenHelper {

    //DataBase property
    private static final String DATABASE_NAME = "sto";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_NAME_STO = "sto_post";
    //columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SHORT_HISTORY = "short_story";
    public static final String COLUMN_FULL_HISTORY = "full_story";
    public static final String COLUMN_XFIELDS = "xfields";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "descr";
    public static final String COLUMN_KEYWORDS= "keywords";
    public static final String COLUMN_CATEGORY = "category";


    //create scripts
    private String CREATE_TABLE_STO = "CREATE TABLE " + TABLE_NAME_STO + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_AUTHOR + " TEXT NOT NULL "
         //   + COLUMN_DATE + " DATE NOT NULL , "
          //  + COLUMN_SHORT_HISTORY + "  TEXT NOT NULL, "
           // + COLUMN_FULL_HISTORY + "  TEXT NOT NULL, "
           // + COLUMN_XFIELDS + " INTEGER NOT NULL , "
           // + COLUMN_TITLE + "  TEXT NOT NULL, "
           // + COLUMN_DESCRIPTION + "  TEXT NOT NULL, "
           // + COLUMN_KEYWORDS + "  TEXT NOT NULL, "
            //+ COLUMN_CATEGORY + "  TEXT NOT NULL " +
           + ");";


    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STO);
        db.execSQL("insert into " + TABLE_NAME_STO + " (" + COLUMN_AUTHOR + ") values('This is from sql lite')");
        db.execSQL("insert into " + TABLE_NAME_STO+ " (" + COLUMN_AUTHOR + ") values('Egor')");
        db.execSQL("insert into " + TABLE_NAME_STO+ " (" + COLUMN_AUTHOR + ") values('Ksyunayka')");
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
