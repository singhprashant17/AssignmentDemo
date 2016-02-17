package com.demo.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.demo.assignment.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database_name";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "table_name";
    private static final String KEY_ID = "id";
    private static final String KEY_URI = "uri";
    private static final String KEY_TITLE = "title";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " TEXT, " + KEY_URI + " TEXT, "
                + KEY_TITLE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<DataModel> getAllItems() {
        Cursor cursor = getWritableDatabase().rawQuery("select * from " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            List<DataModel> dataModelList = new ArrayList<>();
            do {
                String id = cursor.getString(0);
                String uri = cursor.getString(1);
                String title = cursor.getString(2);
                dataModelList.add(new DataModel(id, uri, title));
            } while (cursor.moveToNext());
            return dataModelList;
        }
        return null;
    }

    public void saveToDatabase(final List<DataModel> dataModelList) {
        final SQLiteDatabase db = getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DataModel> allItems = getAllItems();
                if (allItems != null && !allItems.isEmpty()) {
                    clearData();
                }
                for (DataModel dataModel : dataModelList) {
                    ContentValues data = new ContentValues();
                    data.put(KEY_ID, dataModel.getId());
                    data.put(KEY_URI, dataModel.getUri());
                    data.put(KEY_TITLE, dataModel.getTitle());
                    db.insert(TABLE_NAME, null, data);
                }
            }
        }).start();
    }

    private void clearData() {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME);
    }
}
