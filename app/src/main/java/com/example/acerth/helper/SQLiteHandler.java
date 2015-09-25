package com.example.acerth.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by ACERTH on 9/23/2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

        private static final String TAG = SQLiteHandler.class.getSimpleName();

        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 3;

        // Database Name
        private static final String DATABASE_NAME = "iearthinth_safe";

        // Login table name
        private static final String TABLE_USER = "login";

        // Login Table Columns names
        private static final String KEY_ID = "user_id";
        private static final String KEY_NAME = "user_name";
        private static final String KEY_PASS = "user_password";
        private static final String KEY_EMAIL = "user_email";
        private static final String KEY_GAME_NAME = "user_game_name";
        private static final String KEY_LEVEL = "level";
        private static final String KEY_LEAGUE = "league";
        private static final String KEY_SCORE = "score";
        private static final String KEY_DISTANCE = "distance";
        private static final String KEY_CALORIES = "calories";
        private static final String KEY_ADMIN_ID = "admin_id";


        public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT UNIQUE,"
                + KEY_PASS + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_GAME_NAME + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_LEAGUE + " TEXT,"
                + KEY_SCORE + " NUMERIC,"
                + KEY_DISTANCE + " NUMERIC,"
                + KEY_CALORIES + " NUMERIC,"
                + KEY_ADMIN_ID + " TEXT"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

        /**
         * Storing user details in database
         * */
    public void addUser(int user_id, String user_name, String user_password ,String user_email, String user_game_name, String level,
                        String league, double score, double distance, double calories, String admin_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, user_id); //user_id
        values.put(KEY_NAME, user_name); // Name
        values.put(KEY_PASS, user_password); // password
        values.put(KEY_EMAIL, user_email); // Email
        values.put(KEY_GAME_NAME, user_game_name); // user_game_name
        values.put(KEY_LEVEL, level); // level
        values.put(KEY_LEAGUE, league); // league
        values.put(KEY_SCORE, score); // score
        values.put(KEY_DISTANCE, distance); // distance
        values.put(KEY_CALORIES, calories); // calories
        values.put(KEY_ADMIN_ID, admin_id); // admin_id

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void updateUser(int user_id, String user_name, String user_password , String user_email, String user_game_name, String level,
                           String league, double score, double distance, double calories, String admin_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user_name); // Name
        values.put(KEY_PASS, user_password); // password
        values.put(KEY_EMAIL, user_email); // Email
        values.put(KEY_GAME_NAME, user_game_name); // user_game_name
        values.put(KEY_LEVEL, level); // level
        values.put(KEY_LEAGUE, league); // league
        values.put(KEY_SCORE, score); // score
        values.put(KEY_DISTANCE, distance); // distance
        values.put(KEY_CALORIES, calories); // calories
        values.put(KEY_ADMIN_ID, admin_id); // admin_id

        // Inserting Row
        long id = db.update(TABLE_USER, values, KEY_ID + " = " + user_id, null);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("user_id", cursor.getString(0));
            user.put("user_name", cursor.getString(1));
            user.put("user_password", cursor.getString(2));
            user.put("user_email", cursor.getString(3));
            user.put("user_game_name", cursor.getString(4));
            user.put("level", cursor.getString(5));
            user.put("league", cursor.getString(6));
            user.put("score", cursor.getString(7));
            user.put("distance", cursor.getString(8));
            user.put("calories", cursor.getString(9));
            user.put("admin_id", cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user login status return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
