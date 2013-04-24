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

    private String[] allColumns = {DBAdapter.COLUMN_ID, DBAdapter.COLUMN_AUTHOR, DBAdapter.COLUMN_DATE, DBAdapter.COLUMN_SHORT_HISTORY,
            DBAdapter.COLUMN_FULL_HISTORY, DBAdapter.COLUMN_XFIELDS, DBAdapter.COLUMN_TITLE, DBAdapter.COLUMN_DESCRIPTION, DBAdapter.COLUMN_CATEGORY};

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

    //it will be good if we won't create new entity.
    public STO createSTO(String author) {
        ContentValues values = new ContentValues();
        values.put(DBAdapter.COLUMN_AUTHOR, author);
        long insertId = database.insert(DBAdapter.TABLE_NAME_STO, null,
                values);
        Cursor cursor = database.query(DBAdapter.TABLE_NAME_STO,
                allColumns, DBAdapter.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        STO newEntity = cursorToSTO(cursor);
        cursor.close();
        return newEntity;
    }

    public void deleteSTOEtity(STO entity) {
        long id = entity.getId();
        System.out.println("STO deleted with id: " + id);
        database.delete(DBAdapter.TABLE_NAME_STO, DBAdapter.COLUMN_ID
                + " = " + id, null);
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
        entity.setAuthor(cursor.getString(1));
        String dateStr = cursor.getString(2);
        Date date = null;
        if (!dateStr.equals("0000-00-00 00:00:00")) {
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                //nothing to do here))
            }
        }
        entity.setDate(date);
        entity.setShortHistory(cursor.getString(3));
        entity.setFullHistory(cursor.getString(4));
        entity.setxFields(cursor.getString(5));
        entity.setTitle(cursor.getString(6));
        entity.setDescription(cursor.getString(7));
        entity.setCategory(StoUtils.parseCategory(cursor.getString(8)));
        return entity;
    }

}
