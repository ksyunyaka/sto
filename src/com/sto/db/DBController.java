package com.sto.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.sto.entity.STO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 2/27/13
 */
public class DBController {

    // Database fields
    private SQLiteDatabase database;
    private DBAdapter dbAdapter;
    private String[] allColumns = { DBAdapter.COLUMN_ID,
            DBAdapter.COLUMN_AUTHOR };

    public DBController(Context context) {
        dbAdapter = new DBAdapter(context);
    }

    public void open() throws SQLException {
        database = dbAdapter.getWritableDatabase();
    }

    public void close() {
        dbAdapter.close();
    }

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
        System.out.println("Comment deleted with id: " + id);
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
        return entity;
    }

}
