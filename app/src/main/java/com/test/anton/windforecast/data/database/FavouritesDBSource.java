package com.test.anton.windforecast.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.test.anton.windforecast.models.Favourite;

import java.util.ArrayList;
import java.util.List;

public class FavouritesDBSource implements FavouritesSource{

    private static FavouritesDBSource sDataSource;

    private FavouritesDBHelper mDbHelper;

    private SQLiteDatabase mDb;

    private FavouritesDBSource(@NonNull Context context) {
        mDbHelper = new FavouritesDBHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public static FavouritesDBSource getInstance(@NonNull Context context) {
        if (sDataSource == null) {
            sDataSource = new FavouritesDBSource(context);
        }
        return sDataSource;
    }

    @Override
    public List<Favourite> getFavourites() {
        List<Favourite> favourites = new ArrayList<Favourite>();
        try {

            String[] projection = {
                    FavouritesDBContract.FavouriteEntry._ID,
                    FavouritesDBContract.FavouriteEntry.COLUMN_COUNTRY_CODE,
                    FavouritesDBContract.FavouriteEntry.COLUMN_CITY
            };

            Cursor cursor = mDb.query(FavouritesDBContract.FavouriteEntry.TABLE_NAME, projection, null, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int id = cursor
                            .getInt(cursor.getColumnIndexOrThrow(FavouritesDBContract.FavouriteEntry._ID));
                    String country = cursor
                            .getString(cursor.getColumnIndexOrThrow(FavouritesDBContract.FavouriteEntry.COLUMN_COUNTRY_CODE));
                    String city =
                            cursor.getString(cursor.getColumnIndexOrThrow(FavouritesDBContract.FavouriteEntry.COLUMN_CITY));
                    Favourite favourite = new Favourite(String.valueOf(id), country, city);
                    favourites.add(favourite);
                }
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
        return favourites;
    }

    @Override
    public void saveFavourite(@NonNull Favourite favourite) {
        try {
            ContentValues values = new ContentValues();
            values.put(FavouritesDBContract.FavouriteEntry.COLUMN_COUNTRY_CODE, favourite.getCountryCode());
            values.put(FavouritesDBContract.FavouriteEntry.COLUMN_CITY, favourite.getCity());

            mDb.insert(FavouritesDBContract.FavouriteEntry.TABLE_NAME, null, values);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void updateFavourite(@NonNull Favourite favourite) {
        try {
            ContentValues values = new ContentValues();
            values.put(FavouritesDBContract.FavouriteEntry.COLUMN_COUNTRY_CODE, favourite.getCountryCode());

            String selection = FavouritesDBContract.FavouriteEntry._ID + " LIKE ?";
            String[] selectionArgs = {favourite.getId()};

            mDb.update(FavouritesDBContract.FavouriteEntry.TABLE_NAME, values, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void deleteAllFavourites() {
        try {
            mDb.delete(FavouritesDBContract.FavouriteEntry.TABLE_NAME, null, null);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void deleteFavourite(@NonNull String favouriteId) {
        try {
            String selection = FavouritesDBContract.FavouriteEntry._ID + " LIKE ?";
            String[] selectionArgs = {favouriteId};

            mDb.delete(FavouritesDBContract.FavouriteEntry.TABLE_NAME, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

}
