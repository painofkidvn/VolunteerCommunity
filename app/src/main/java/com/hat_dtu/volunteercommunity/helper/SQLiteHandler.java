package com.hat_dtu.volunteercommunity.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String TABLE_CHARITY = "charity";

    //Chairty Table Columns names
    private static final String KEY_ID_C = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ACTIVITY = "activity";
    private static final String KEY_RATING = "rating";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";

    /**
     * user_charity session
     */

    //user_charity table name
    private static final String TABLE_USER_CHARITY = "user_charity";
    //user_charity Table Columns names
    private static final String KEY_ID_UC = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_CHARITY_ID = "charity_id";

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

//        String CREATE_CHARITY_TABLE = "CREATE TABLE " + TABLE_CHARITY + "("
//                + KEY_ID_C + " INTEGER PRIMARY KEY,"
//                + KEY_TITLE + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_PHONE + " TEXT,"
//                + KEY_ACTIVITY + " TEXT," + KEY_RATING + " TEXT, "
//                + KEY_LAT + " TEXT, " + KEY_LNG + " TEXT" + ")";
//        db.execSQL(CREATE_CHARITY_TABLE);
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
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARITY);
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
    public void addCharity(Integer user_id, String title, String address, String phone, String activity, String rating, String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_PHONE, phone);
        values.put(KEY_ACTIVITY, activity);
        values.put(KEY_RATING, rating);
        values.put(KEY_LAT, lat);
        values.put(KEY_LNG, lng);

        // Inserting Row

        long id = db.insert(TABLE_CHARITY, null, values);
        addUserCharity(user_id, id);
        db.close(); // Closing database connection

        Log.d(TAG, "New Place Location inserted into sqlite: " + id);
    }

    /**
     * Getting charity data from database
     */
    public HashMap<String, String> getCharityDetails() {
        HashMap<String, String> charity = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_CHARITY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            charity.put("title", cursor.getString(1));
            charity.put("address", cursor.getString(2));
            charity.put("phone", cursor.getString(3));
            charity.put("activity", cursor.getString(4));
            charity.put("rating", cursor.getString(5));
            charity.put("lat", cursor.getString(6));
            charity.put("lng", cursor.getString(7));
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
        db.delete(TABLE_CHARITY, null, null);
        db.close();

        Log.d(TAG, "Deleted all charity info from sqlite");
    }

    /**
     * Delete a Place
     */
    public void deleteCharity(long user_id, long charity_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_CHARITY + " c, " + TABLE_USER_CHARITY + " uc WHERE "
                + "c.id = " + charity_id + " AND uc.charity_id = c.id AND uc.user_id = " + user_id);
        db.close();

        Log.d(TAG, "Deleted " + charity_id + " charity info from sqlite");
    }

    /**
     * Update a charity
     */
    public void updateCharity(long user_id, long charity_id, String title, String address,
                              String phone, String activity, String rating, String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_CHARITY + " c, " + TABLE_USER_CHARITY + " uc SET "
                + "c.title = " + title + ", c.address = " + address + ", c.phone = " + phone
                + ", c.activity = " + activity + ", c.rating = " + rating + ", c.lat = " + lat + ", c.lng = " + lng
                + " WHERE c.id = " + charity_id + " AND c.id = uc.charity_id AND uc.user_id = " + user_id);
        db.close();

        Log.d(TAG, "Updated " + charity_id + " charity info from sqlite");
    }


    /**
     * Storing user_charity detail into database
     */
    public void addUserCharity(long user_id, long charity_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_CHARITY_ID, charity_id);

        // Inserting Row
        long id = db.insert(TABLE_USER_CHARITY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New User_Charity inserted into sqlite: " + id);
    }


}