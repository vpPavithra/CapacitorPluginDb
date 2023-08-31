package org.sunbird.plugin.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.getcapacitor.PluginCall;
import com.getcapacitor.JSObject;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import java.util.List;

public class SunbirdDBHelper extends SQLiteOpenHelper {

    private static SunbirdDBHelper instance;
    private static SQLiteOperator externalDbOperator;

    public static void init(SunbirdDBContext sunbirdDBContext, PluginCall call) {
        Log.d("db init ", sunbirdDBContext.toString());
        if (instance == null) {
            instance = new SunbirdDBHelper(sunbirdDBContext, call);
            Log.d("instance ", instance.toString());
        }
    }

    public static void initExternalDatabase(SunbirdDBContext sunbirdDBContext, PluginCall call) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(sunbirdDBContext.getFilePath(), null, SQLiteDatabase.OPEN_READWRITE);
        externalDbOperator = new SQLiteOperator(database, null, 0);
    }

    private SQLiteDatabase externalDatabase;

    public static SunbirdDBHelper getInstance() {
        return instance;
    }

    private SunbirdDBContext sunbirdDBContext;
    private SQLiteOperator sqLiteOperator;
    private List<Migration> migrationList;
    private PluginCall call;


    private SunbirdDBHelper(SunbirdDBContext sunbirdDBContext, PluginCall call) {
        super(sunbirdDBContext.getContext(), sunbirdDBContext.getDbName(),
                null, sunbirdDBContext.getDbVersion());
        this.sunbirdDBContext = sunbirdDBContext;
        this.migrationList = sunbirdDBContext.getMigrationList();
        this.call = call;
    }

    private void publishEvent(JSONObject object){
        JSObject ret = new JSObject();
        ret.put("status", "OK");
        ret.put("res", object.toString());
        Log.d("Publish event ", ret.toString());
        this.call.setKeepAlive(true);
        this.call.resolve(ret);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("on create ", "db");
        publishEvent(createJsonForOncreate());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("on upgrade ", "db");
        publishEvent(createJsonForOnupgrade(oldVersion, newVersion));
    }

    public static void openDataBase(String filePath) throws SQLException {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READWRITE);
        externalDbOperator =  new SQLiteOperator(database, null, 0);
    }

    @Override
    public synchronized void close() {
        if(externalDatabase != null){
            externalDatabase.close();
        }
        super.close();
    }

    private JSONObject createJsonForOncreate() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "onCreate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForOnupgrade(int oldVersion, int newVersion) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "onUpgrade");
            jsonObject.put("oldVersion", oldVersion);
            jsonObject.put("newVersion", newVersion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public SQLiteOperator operator(boolean useExternalDbSession) {
        if(useExternalDbSession){
            return externalDbOperator;
        }
        if (sqLiteOperator == null) {
            SQLiteDatabase database = getWritableDatabase();
            sqLiteOperator = new SQLiteOperator(database, sunbirdDBContext.getDbName(), sunbirdDBContext.getDbVersion());
        }
        return sqLiteOperator;
    }

}