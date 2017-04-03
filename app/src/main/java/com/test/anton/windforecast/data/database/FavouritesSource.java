package com.test.anton.windforecast.data.database;


import android.support.annotation.NonNull;

import com.test.anton.windforecast.models.Favourite;

import java.util.List;

public interface FavouritesSource {

    List<Favourite> getFavourites();

    void saveFavourite(@NonNull Favourite favourite);

    void updateFavourite(@NonNull Favourite favourite);

    void deleteAllFavourites();

    void deleteFavourite(@NonNull String favouriteId);
}