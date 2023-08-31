package org.sunbird.plugin.db;

import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

@CapacitorPlugin(name = "SunbirdDB")
public class SunbirdDBPlugin extends Plugin {

    private SunbirdDB implementation = new SunbirdDB();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }


    @PluginMethod
    public void init(PluginCall call) {
        String dbName = call.getString("dbName");
        Integer ver = call.getInt("dbVersion");
        JSArray arr = call.getArray("migrations");
        try {
            List<Migration> migrationList = prepareMigrations(arr);
            Log.d("migration list ", migrationList.toString());
            SunbirdDBContext sunbirdDBContext = new SunbirdDBContext();
            sunbirdDBContext.setContext(getContext())
                    .setDbName(dbName)
                    .setDbVersion(ver)
                    .setMigrationList(migrationList);
            Log.d("***** context ", sunbirdDBContext.toString());
            SunbirdDBHelper.init(sunbirdDBContext, call);
            getOperator(false);
        } catch (JSONException e) {
            // implementation.init(e.getMessage());
            call.reject("Error", e);
        }
    }

    @PluginMethod
    public void open(PluginCall call) {
        String filePath = call.getString("filePath");
        Log.d("********** filepath ", filePath);
        try {
            SunbirdDBHelper.initExternalDatabase(new SunbirdDBContext(getContext(),filePath.replace("file://", "")),call);
            Log.d("******** success", " on open db");
            JSObject ret = new JSObject();
            ret.put("value", implementation.open(filePath));
            call.success(ret);
        } catch (Exception e) {
            Log.d("******* error", " on open");
            e.printStackTrace();
            implementation.open(e.getMessage());
            call.error("Error ", e);
        }
    }

    @PluginMethod
    public void close(PluginCall call) {
        Log.d("Close db", "call close db");
        try {
            SunbirdDBHelper.getInstance().close();
            JSObject ret = new JSObject();
            ret.put("value", "true");
            implementation.close("true");
            call.resolve(ret);
        } catch (Exception e) {
            e.printStackTrace();
            implementation.close(e.getMessage());
            call.reject("Error ", e.getMessage());
        }
    }

    @PluginMethod
    public void read(PluginCall call) {
        Log.d("********* read ", call.getString("table"));
        try {
            JSONArray resultArray = getOperator(call.getBoolean("useExternalDb")).read(
                    call.getBoolean("distinct"),
                    call.getString("table"),
                    toStringArray(call.getArray("columns")),
                    call.getString("selection"),
                    toStringArray(call.getArray("selectionArgs")),
                    call.getString("groupBy"),
                    call.getString("having"),
                    call.getString("orderBy"),
                    call.getString("limit")
            );
            Log.d("********** read ", String.valueOf(resultArray));
            implementation.read(resultArray);
            JSObject ret = new JSObject();
            ret.put("res", "success");
            ret.put("res", resultArray);
            call.resolve(ret);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    @PluginMethod
    public void insert(PluginCall call) {
        Log.d("******** insert ", call.getString("table"));
        // String readTableSql = "SELECT * FROM telemetry";
        try {
            SQLiteOperator db = getOperator(call.getBoolean("useExternalDb"));
            long number = db.insert(call.getString("table"), call.getObject("model"));
            // JSONArray readRes = db.execute(readTableSql);
            Log.d("********* execute insert ", String.valueOf(number));
            // Log.d("********* execute read", String.valueOf(readRes));
            JSObject ret = new JSObject();
            ret.put("success", "insert");
            ret.put("res", number);
            implementation.insert(number);
            call.resolve(ret);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    @PluginMethod
    public void update(PluginCall call) {
        Log.d("********** update ", call.getString("table"));
        try {
            int count = getOperator(call.getBoolean("useExternalDb")).update(
                    call.getString("table"),
                    call.getString("whereClause"),
                    toStringArray(call.getArray("whereArgs")),
                    call.getObject("model")
            );
            JSObject ret = new JSObject();
            ret.put("success ", "update");
            ret.put("result", count);
            implementation.update(count);
            call.resolve(ret);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    @PluginMethod
    public void delete(PluginCall call) {
        Log.d("******** delete ", call.getString("table"));
        try {
            int count = getOperator(call.getBoolean("useExternalDb")).delete(
                    call.getString("table"),
                    call.getString("whereClause"),
                    toStringArray(call.getArray("whereArgs"))
            );
            implementation.delete(count);
            JSObject ret = new JSObject();
            ret.put("success ", "delete");
            ret.put("result", count);
            call.resolve(ret);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    @PluginMethod
    public void execute(PluginCall call) {
        Log.d("********* execute ", call.getString("query"));
        String sqlQuery = call.getString("query");
        String readTableSql = "SELECT * FROM telemetry";
        try {
            SQLiteOperator db = getOperator(call.getBoolean("useExternalDb"));
            JSONArray resultArray = db.execute(sqlQuery);
            JSONArray readRes = db.execute(readTableSql);
            JSObject ret = new JSObject();
            ret.put("result", resultArray);
            ret.put("success", "execute");
            Log.d("********* execute create ", String.valueOf(resultArray));
            Log.d("********* execute read", String.valueOf(readRes));
            implementation.execute(resultArray);
            call.resolve(ret);
        } catch (Exception e) {
            Log.d("****** error ", e.getMessage());
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    // Called before the db operation start
    @PluginMethod
    public void beginTransaction(PluginCall call) {
        getOperator(false).beginTransaction();
        JSObject ret = new JSObject();
        implementation.beginTransaction("beginTransaction");
        ret.put("res", "beginTransaction");
        call.resolve(ret);
    }

    // Called after the db operation end
    @PluginMethod
    public void endTransaction(PluginCall call) {
        getOperator(call.getBoolean("useExternalDb")).endTransaction(call.getBoolean("isOperationSuccessful"));
        JSObject ret = new JSObject();
        implementation.endTransaction("endTransaction");
        ret.put("res", "endTransaction");
        call.resolve(ret);
    }

    @PluginMethod
    public void copyDatabase(PluginCall call) {
        String destinationFilePath = call.getString("filePath");
        Log.d("****** copy database ", destinationFilePath);
        try {
            String destination = destinationFilePath.replace("file://", "");
            String dbName = getOperator(false).getDBName();
            FileUtil.cp(getContext().getDatabasePath(dbName).getPath(), destination);
            implementation.copyDatabase("success");
            Log.d("****** copy database ", "success");
            JSObject ret = new JSObject();
            ret.put("res", "success");
            call.resolve(ret);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("****** copy database ", "error "+e.getMessage());
            implementation.copyDatabase("error" + e.getMessage());
            call.reject(e.getMessage());
        }
    }

    private List<Migration> prepareMigrations(JSONArray args) throws JSONException {
        List<Migration> migrationList = new ArrayList<>();
        JSONArray migrationArray = args;
        int size = migrationArray.length();
        for (int i = 0; i < size; i++) {
            migrationList.add(createMigration(migrationArray.getJSONObject(i)));
        }
        return migrationList;
    }

    private Migration createMigration(JSONObject migrationObject) throws JSONException {
        List<String> queryList = new ArrayList<>();
        JSONArray queryArray = migrationObject.getJSONArray("queryList");
        int querySize = queryArray.length();
        for (int j = 0; j < querySize; j++) {
            queryList.add(queryArray.getString(j));
        }

        Migration migration = new Migration();
        migration.setQueryList(queryList)
                .setTargetDbVersion(migrationObject.getInt("targetDbVersion"));
        return migration;
    }

    private String[] toStringArray(JSONArray array) throws JSONException {
        int length = array.length();
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            values[i] = array.getString(i);
        }
        return values;
    }

    private SQLiteOperator getOperator(boolean useexternalDbOperator) {
        return SunbirdDBHelper.getInstance().operator(useexternalDbOperator);
    }
}
