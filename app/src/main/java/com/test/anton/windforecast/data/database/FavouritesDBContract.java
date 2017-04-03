package com.test.anton.windforecast.data.database;


import android.provider.BaseColumns;

public class FavouritesDBContract {

    private FavouritesDBContract() {
    }

    public static class FavouriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_COUNTRY_CODE = "country";
        public static final String COLUMN_CITY = "city";
    }
}
