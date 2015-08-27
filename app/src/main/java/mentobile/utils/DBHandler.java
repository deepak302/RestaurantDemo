package mentobile.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Deepak Sharma on 7/28/2015.
 */

public class DBHandler extends SQLiteOpenHelper {

    String TAG = "DBHandler";

    public static final String DATABASE_NAME = "RestauraneDemo.db";
    public static final String TABLE_USER_PROFILE = "tbl_user_profile";
    public static final String TABLE_DELIVERY_ADDRESS = "tbl_del_address";
    public static final String CRT_UPLOAD = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER_PROFILE
            + "(FNAME VARCHAR , " +
            "   LNAME VARCHAR," +
            "   FULLNAME VARCHAR, " +
            "   EMAIL VARCHAR PRIMARY KEY NOT NULL UNIQUE, " +
            "   MOBILE VARCHAR, " +
            "   CITY VARCHAR, " +
            "   LOCATION VARCHAR , " +
            "   COMPANY VARCHAR, " +
            "   FLATNO VARCHAR, " +
            "   APARTMENT VARCHAR, " +
            "   POSTCODE VARCHAR, " +
            "   OTHERADDRESS VARCHAR, " +
            "   DELINS VARCHAR )";

    public static final String CRT_DELVRY_ADDRESS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DELIVERY_ADDRESS
            + "( EMAIL VARCHAR PRIMARY KEY NOT NULL UNIQUE, " +
            "   FULLNAME VARCHAR, " +
            "   PHONE VARCHAR, " +
            "   PINCODE VARCHAR, " +
            "   DELIVERY_ADDRESS VARCHAR , " +
            "   CITY VARCHAR, " +
            "   STATE VARCHAR, " +
            "   LANDMARK VARCHAR )";

    public DBHandler(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
        Log.d(TAG, "::::::DBHandler");
        onUpgrade(getWritableDatabase(), 1, 1);
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRT_UPLOAD);
        db.execSQL(CRT_DELVRY_ADDRESS);
        Log.d(TAG, "::::::OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "::::::OnUpgrade");
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
    }

    public void insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
    }

    public Cursor getPRofileDataFromDB(String emailID) {
        SQLiteDatabase db = getReadableDatabase();
        String selectData = "SELECT * FROM " + TABLE_USER_PROFILE + " WHERE email=" + "'" + emailID + "'";
        Cursor cursor = db.rawQuery(selectData, null);
        return cursor;
    }

    public Cursor getAllDeliveryAddress() {
        SQLiteDatabase db = getReadableDatabase();
        String selectData = "SELECT * FROM " + TABLE_DELIVERY_ADDRESS;
        Cursor cursor = db.rawQuery(selectData, null);
        return cursor;
    }

    public boolean isTableEmplty(String tablename) {
        SQLiteDatabase db = getReadableDatabase();
        String selectData = "SELECT *FROM " + tablename;
        Cursor cursor = db.rawQuery(selectData, null);
        if (cursor.getCount() < 1) {
            return true;
        }
        return false;
    }
}
