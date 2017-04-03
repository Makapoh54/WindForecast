package com.test.anton.windforecast.windcast.favouritesdetails;


import android.support.annotation.NonNull;

import java.util.List;

public interface FavouriteDetailsContract {

    interface View {
        void showDetails(@NonNull List<Float> forecasts,@NonNull List<String> legend);

        void showLoadingError();

        void setLoadingIndicator(boolean active);
    }

    interface Presenter {
        void loadForecast();

        void cancelRequest();
    }
}
