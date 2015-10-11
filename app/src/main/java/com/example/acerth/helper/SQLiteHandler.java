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
        private static final String TABLE_USERPLAYMAP = "tab_Map";

        // Login Table Columns names
        private static final String KEY_ID = "user_id";
        private static final String KEY_NAME = "user_name";
        private static final String KEY_PASS = "user_password";
        private static final String KEY_EMAIL = "user_email";
        private static final String KEY_GAME_NAME = "user_game_name";
        private static final String KEY_IMAGE_NAME = "user_image_name";
        private static final String KEY_IMAGE_PATH = "user_image_path";
        private static final String KEY_LEVEL = "level";
        private static final String KEY_LEAGUE = "league";
        private static final String KEY_SCORE = "score";
        private static final String KEY_DISTANCE = "distance";
        private static final String KEY_CALORIES = "calories";
        private static final String KEY_ADMIN_ID = "admin_id";

        private static final String KEY_STEP = "step";
        private static final String KEY_ELAPSEDTIME = "elapsedTime";
        private static final String KEY_TIMESTART = "time_start";
        private static final String KEY_TIMESTOP = "time_stop";
        private String val;

        public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

        // Creating Tablesh
        @Override
        public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT UNIQUE,"
                + KEY_PASS + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_GAME_NAME + " TEXT,"
                + KEY_IMAGE_NAME + " TEXT,"
                + KEY_IMAGE_PATH  + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_LEAGUE + " TEXT,"
                + KEY_SCORE + " REAL,"
                + KEY_DISTANCE + " REAL,"
                + KEY_CALORIES + " REAL,"
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
    public void addUser(int user_id, String user_name, String user_password ,String user_email, String user_game_name, String user_image_name,
                        String user_image_path, String level, String league, double score, double distance, double calories, String admin_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, user_id); //user_id
        values.put(KEY_NAME, user_name); // Name
        values.put(KEY_PASS, user_password); // password
        values.put(KEY_EMAIL, user_email); // Email
        values.put(KEY_GAME_NAME, user_game_name); // user_game_name
        values.put(KEY_IMAGE_NAME, user_image_name); // user_image_name
        values.put(KEY_IMAGE_PATH, user_image_path); // user_image_path
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

    public void updateUser(int user_id, String user_name, String user_password , String user_email, String user_game_name, String user_image_name,
                           String user_image_path, String level, String league, double score, double distance, double calories, String admin_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user_name); // Name
        values.put(KEY_PASS, user_password); // password
        values.put(KEY_EMAIL, user_email); // Email
        values.put(KEY_GAME_NAME, user_game_name); // user_game_name
        values.put(KEY_IMAGE_NAME, user_image_name); // user_image_name
        values.put(KEY_IMAGE_PATH, user_image_path); // user_image_path
        values.put(KEY_LEVEL, level); // level
        values.put(KEY_LEAGUE, league); // league
        values.put(KEY_SCORE, score); // score
        values.put(KEY_DISTANCE, distance); // distance
        values.put(KEY_CALORIES, calories); // calories
        values.put(KEY_ADMIN_ID, admin_id); // admin_id

        // Inserting Row
        long id = db.update(TABLE_USER, values, KEY_ID + " = " + user_id, new String[]{String.valueOf(user_game_name)});
        db.close(); // Closing database connection

        Log.d(TAG, "Old user update into sqlite: " + id);
    }

    public void updateGamename(int user_id, String oldName, String newName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GAME_NAME, newName); // user_game_name

        long id = db.update(TABLE_USER, values, KEY_ID + " = " + user_id, new String[]{String.valueOf(oldName)});
        db.close(); // Closing database connection

        Log.d(TAG, "New game name of user update into sqlite: " + id);
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
            user.put("user_id", cursor.getString(cursor.getColumnIndex(KEY_ID)));
            user.put("user_name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("user_password", cursor.getString(cursor.getColumnIndex(KEY_PASS)));
            user.put("user_email", cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.put("user_game_name", cursor.getString(cursor.getColumnIndex(KEY_GAME_NAME)));
            user.put("user_image_name", cursor.getString(cursor.getColumnIndex(KEY_IMAGE_NAME)));
            user.put("user_image_path", cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
            user.put("level", cursor.getString(cursor.getColumnIndex(KEY_LEVEL)));
            user.put("league", cursor.getString(cursor.getColumnIndex(KEY_LEAGUE)));
            user.put("score", cursor.getString(cursor.getColumnIndex(KEY_SCORE)));
            user.put("distance", cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
            user.put("calories", cursor.getString(cursor.getColumnIndex(KEY_CALORIES)));
            user.put("admin_id", cursor.getString(cursor.getColumnIndex(KEY_ADMIN_ID)));
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

    public void addplayData(int user_id,float distance,int calories,int step,String elapsedTime,String time_start,String time_stop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, user_id); //user_id
        values.put(KEY_DISTANCE, distance); // distance
        values.put(KEY_CALORIES, calories); // calories
        values.put(KEY_STEP, step); // step
        values.put(KEY_ELAPSEDTIME, elapsedTime); // elapsedTime
        values.put(KEY_TIMESTART, time_start); // time_start
        values.put(KEY_TIMESTOP, time_stop); // time_stop


        // Inserting Row
        long id = db.insert(TABLE_USERPLAYMAP, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

}
