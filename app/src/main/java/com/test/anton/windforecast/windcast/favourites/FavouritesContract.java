package com.test.anton.windforecast.windcast.favourites;

import android.support.annotation.NonNull;

import com.test.anton.windforecast.models.Favourite;
import com.test.anton.windforecast.models.ForecastedFavourite;

import java.util.List;

public interface FavouritesContract {

    interface View {
        void showFavourites(@NonNull List<ForecastedFavourite> favourites);

        void showAlreadyAdded();

        void showLoadingError();

        void onItemsLoadComplete();

        void setLoadingIndicator(boolean active);
    }

    interface Presenter {
        void start();

        void loadFavourites();

        void saveFavourite(@NonNull Favourite favourite);
    }
}
