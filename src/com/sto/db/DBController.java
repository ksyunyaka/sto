package com.sto.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.sto.entity.STO;
import com.sto.utils.StoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sto.db.DBAdapter.*;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 2/27/13
 */
public class DBController {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private InputStream insertStatementStream;
    // Database fields
    private SQLiteDatabase database;
    private DBAdapter dbAdapter;

    private String[] allColumns = {COLUMN_ID, COLUMN_SHORT_HISTORY, COLUMN_TELEPHONE, COLUMN_LONGITUDE, COLUMN_LATITUDE,
            COLUMN_TIME, COLUMN_SITE, COLUMN_WASH_TYPE, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_CATEGORY};

    public DBController(Context context, InputStream insertStatementStream) {
        dbAdapter = new DBAdapter(context, insertStatementStream);
        this.insertStatementStream = insertStatementStream;
    }

    public void open() throws SQLException {
        database = dbAdapter.getWritableDatabase();
        if (insertStatementStream != null) {
            try {
                insertStatementStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void close() {
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }

    public List<STO> getAllSTOEntities() {
        List<STO> entityList = new ArrayList<STO>();

        Cursor cursor = database.query(DBAdapter.TABLE_NAME_STO,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            STO entity = cursorToSTO(cursor);
            entityList.add(entity);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entityList;
    }

    private STO cursorToSTO(Cursor cursor) {
        STO entity = new STO();
        entity.setId(cursor.getLong(0));
        entity.setShortHistory(cursor.getString(1));
        entity.setTelephone(cursor.getString(2));
        String longtitude = cursor.getString(3);
        if(longtitude.length()>0){
            entity.setLongitude(Float.parseFloat(longtitude));
        }
        String latitude = cursor.getString(4);
        if( latitude.length()>0){
            entity.setLatitude(Float.parseFloat(latitude));
        }
        entity.setTime(cursor.getString(5));
        entity.setSite(cursor.getString(6));
        entity.setWashType(cursor.getString(7));
        entity.setTitle(cursor.getString(8));
        entity.setDescription(cursor.getString(9));
        entity.setCategory(StoUtils.parseCategory(cursor.getString(10)));
        return entity;
    }

}
