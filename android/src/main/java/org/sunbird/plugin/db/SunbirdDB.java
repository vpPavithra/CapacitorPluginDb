package org.sunbird.plugin.db;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import com.getcapacitor.JSObject;

public class SunbirdDB {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public String init(String callback) {
        Log.i("init", callback);
        return callback;
    }

    public String open(String filePath) {
        Log.i("open", filePath);
        return filePath;
    }

    public String close(String res) {
        Log.i("close", res);
        return res;
    }

    public String copyDatabase(String response) {
        Log.i("copyDatabase", response);
        return response;
    }

    public JSONArray execute(JSONArray array) {
        Log.i("execute", array.toString());
        return array;
    }

    public JSONArray read(JSONArray array) {
        Log.i("read", array.toString());
        return array;
    }

    public long insert(long num) {
        Log.i("insert ", "num");
        return num;
    }

    public int update(int cnt) {
        Log.i("update ", "num");
        return cnt;
    }

    public int delete(int cnt) {
        Log.i("delete ", "num");
        return cnt;
    }

    public String beginTransaction(String bt) {
        Log.i("beginTransaction ", bt);
        return bt;
    }

    public String endTransaction(String bt) {
        Log.i("endTransaction ", bt);
        return bt;
    }

}
