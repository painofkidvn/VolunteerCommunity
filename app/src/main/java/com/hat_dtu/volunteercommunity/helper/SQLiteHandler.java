package com.hat_dtu.volunteercommunity.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hat_dtu.volunteercommunity.model.Place;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paino on 2/22/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "volunteer_community";

    /**
     * User session
     */
    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_CREATED_AT = "created_at";

    /**
     * Place session
     */

    //Place table name
    private static final String TABLE_PLACE = "place";

    //Chairty Table Columns names
    private static final String KEY_ID_C = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ACTIVITY = "activity";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_USER_ID = "user_id";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_API_KEY + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_CHARITY_TABLE = "CREATE TABLE " + TABLE_PLACE + "("
                + KEY_ID_C + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_PHONE + " TEXT,"
                + KEY_ACTIVITY + " TEXT," + KEY_LAT + " TEXT, " + KEY_LNG + " TEXT, "
                + KEY_USER_ID + " INTEGER)";
        db.execSQL(CREATE_CHARITY_TABLE);
//
//        String CREATE_USER_CHARITY_TABLE = "CREATE TABLE " + TABLE_USER_CHARITY + "("
//                + KEY_ID_UC + " INTEGER PRIMARY KEY,"
//                + KEY_USER_ID + " INTEGER,"
//                + KEY_CHARITY_ID + " INTEGER," + ")";
//        db.execSQL(CREATE_USER_CHARITY_TABLE);
        Log.d(TAG, "Database tables created");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_CHARITY);


        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String name, String email, String api_key, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_API_KEY, api_key); //
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("api_key", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /**
     * Storing charity detail into database
     */
    /*public void addPlace(String title, String address, String phone, String activity, String lat, String lng, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_ID_C, id);
        values.put(KEY_TITLE, title);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_PHONE, phone);
        values.put(KEY_ACTIVITY, activity);
        values.put(KEY_LAT, lat);
        values.put(KEY_LNG, lng);
        values.put(KEY_USER_ID, user_id);

        // Inserting Row
        long new_id = db.insert(TABLE_PLACE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Place inserted into sqlite: " + new_id);
    }*/

    /**
     * Getting charity data from database
     */
    public HashMap<String, String> getCharityDetails() {
        HashMap<String, String> charity = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLACE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            charity.put("title", cursor.getString(1));
            charity.put("address", cursor.getString(2));
            charity.put("phone", cursor.getString(3));
            charity.put("activity", cursor.getString(4));
            charity.put("lat", cursor.getString(5));
            charity.put("lng", cursor.getString(6));
            charity.put("user_id", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return charity
        Log.d(TAG, "Fetching charity from Sqlite: " + charity.toString());

        return charity;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteCharities() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PLACE, null, null);
        db.close();

        Log.d(TAG, "Deleted all charity info from sqlite");
    }


    public ArrayList<Place> getAllPlace(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Place> places = new ArrayList<>();

        Cursor cursor = db.query(TABLE_PLACE, null, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false){
                places.add(new Place(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getInt(7)));
                cursor.moveToNext();
            }

        }
        db.close();
        cursor.close();
        return places;
    }


}