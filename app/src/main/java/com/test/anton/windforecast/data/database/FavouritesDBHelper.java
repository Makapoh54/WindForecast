package com.test.anton.windforecast.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouritesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "favourite_database";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            FavouritesDBContract.FavouriteEntry.TABLE_NAME + " (" +
            FavouritesDBContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FavouritesDBContract.FavouriteEntry.COLUMN_COUNTRY_CODE + " TEXT, " +
            FavouritesDBContract.FavouriteEntry.COLUMN_CITY + " TEXT" + ")";

    public FavouritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FavouritesDBContract.FavouriteEntry.TABLE_NAME);
            onCreate(db);
        }
    }
}
