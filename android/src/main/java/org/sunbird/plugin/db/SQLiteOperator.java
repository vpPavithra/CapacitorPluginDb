package org.sunbird.plugin.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.database.CursorWindow;


import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import android.util.Log;

public class SQLiteOperator {

    private static final String LOG_TAG = SQLiteOperator.class.getSimpleName();
    private SQLiteDatabase database;
    private String dbName;
    private int dbVersion;

    SQLiteOperator(SQLiteDatabase database, String dbName, int dbVersion) {
        this.database = database;
        this.dbName = dbName;
        this.dbVersion = dbVersion;
    }

    public String getDBName() {
        return this.dbName;
    }

    public int getDBVersion() {
        return dbVersion;
    }

    public JSONArray execute(String query) throws JSONException {
        Cursor cursor = database.rawQuery(query, null);
        JSONArray jsonArray = new JSONArray();

        if (cursor != null && cursor.moveToFirst()) {
            int size = cursor.getCount();
            for (int index = 0; index < size; index++) {
                JSONObject jsonObject = new JSONObject();
                int columnCount = cursor.getColumnCount();

                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    switch (cursor.getType(columnIndex)) {
                        case Cursor.FIELD_TYPE_STRING:
                            jsonObject.put(cursor.getColumnName(columnIndex), cursor.getString(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            jsonObject.put(cursor.getColumnName(columnIndex), cursor.getFloat(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            jsonObject.put(cursor.getColumnName(columnIndex), cursor.getLong(columnIndex));
                            break;
                    }
                }

                jsonArray.put(jsonObject);
                cursor.moveToNext();
            }
        }

        return jsonArray;

    }



    public void beginTransaction() {
        database.beginTransaction();
    }

    public void endTransaction(boolean isOperationSuccessful) {
        if (isOperationSuccessful) {
            database.setTransactionSuccessful();
        }
        database.endTransaction();
    }

    public JSONArray read(boolean distinct,
                          String table,
                          String[] columns,
                          String selection,
                          String[] selectionArgs,
                          String groupBy,
                          String having,
                          String orderBy,
                          String limit) throws JSONException {

        Cursor cursor = database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if ((cursor instanceof SQLiteCursor) && Build.VERSION.SDK_INT >= 28) {
            ((SQLiteCursor) cursor).setWindow(new CursorWindow(null, 1024*1024*7));
        }

        Log.d("Cursor on read ", String.valueOf(cursor));
        JSONArray jsonArray = new JSONArray();

        if (cursor != null && cursor.moveToFirst()) {
            int size = cursor.getCount();
            for (int index = 0; index < size; index++) {
                JSONObject jsonObject = new JSONObject();

                int columnCount = cursor.getColumnCount();

                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    switch (cursor.getType(columnIndex)) {
                        case Cursor.FIELD_TYPE_BLOB:
                            jsonObject.put(cursor.getColumnName(columnIndex), new String(cursor.getBlob(2), StandardCharsets.ISO_8859_1));
                        break;
                        case Cursor.FIELD_TYPE_STRING:
                            jsonObject.put(cursor.getColumnName(columnIndex), cursor.getString(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            jsonObject.put(cursor.getColumnName(columnIndex), cursor.getFloat(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            jsonObject.put(cursor.getColumnName(columnIndex), cursor.getLong(columnIndex));
                            break;
                    }
                }

                jsonArray.put(jsonObject);

                cursor.moveToNext();
            }
        }

        return jsonArray;
    }


    public long insert(String table, JSONObject jsonObject) throws JSONException {

        ContentValues values = new ContentValues();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof Integer) {
                values.put(key, (Integer) value);
            } else if (value instanceof Long) {
                values.put(key, (Long) value);
            } else if (value instanceof String) {
                values.put(key, (String) value);
            } else if (value instanceof Float) {
                values.put(key, (Float) value);
            } else if (value instanceof Double) {
                values.put(key, (Double) value);
            }

        }

        return database.insert(table, null, values);
    }

    public int update(String table, String whereClause, String[] whereArgs, JSONObject jsonObject) throws JSONException {

        ContentValues values = new ContentValues();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof Integer) {
                values.put(key, (Integer) value);
            } else if (value instanceof Long) {
                values.put(key, (Long) value);
            } else if (value instanceof String) {
                values.put(key, (String) value);
            } else if (value instanceof Float) {
                values.put(key, (Float) value);
            } else if (value instanceof Double) {
                values.put(key, (Double) value);
            }

        }
        Log.d("Update values ", String.valueOf(values));

        return database.update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) throws JSONException {
        return database.delete(table, whereClause, whereArgs);
    }

}
