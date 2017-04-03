package com.test.anton.windforecast.data;

import android.support.annotation.NonNull;


import com.test.anton.windforecast.Settings;
import com.test.anton.windforecast.data.database.FavouritesDBSource;
import com.test.anton.windforecast.data.database.FavouritesSource;
import com.test.anton.windforecast.data.remote.WeatherService;
import com.test.anton.windforecast.models.Favourite;
import com.test.anton.windforecast.models.WeatherForecast;
import com.test.anton.windforecast.models.WeatherForecasts;
import com.test.anton.windforecast.models.WindForecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavouritesRepository implements FavouritesSource{

    private static FavouritesRepository sFavouritesRepository = null;

    private final FavouritesDBSource mFavouriteDBSource;
    private final WeatherService mWeatherService;

    private Call<WeatherForecasts> mAsyncForecastCall;

    private List<FavouritesRepositoryObserver> mObservers = new ArrayList<FavouritesRepositoryObserver>();


    public static FavouritesRepository getInstance(FavouritesDBSource favouriteDBSource,
                                                   WeatherService weatherService) {
        if (sFavouritesRepository == null) {
            sFavouritesRepository = new FavouritesRepository(favouriteDBSource, weatherService);
        }
        return sFavouritesRepository;
    }


    public static void destroyInstance() {
        sFavouritesRepository = null;
    }

    private FavouritesRepository(@NonNull FavouritesDBSource favouriteDBSource,
                                 @NonNull WeatherService weatherService) {
        mWeatherService = weatherService;
        mFavouriteDBSource = favouriteDBSource;
    }

    public void addContentObserver(FavouritesRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(FavouritesRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (FavouritesRepositoryObserver observer : mObservers) {
            observer.onFavouriteChanged();
        }
    }

    @Override
    public List<Favourite> getFavourites() {
        return mFavouriteDBSource.getFavourites();
    }

    @Override
    public void saveFavourite(@NonNull Favourite favourite) {
        mFavouriteDBSource.saveFavourite(favourite);
        notifyContentObserver();
    }

    @Override
    public void updateFavourite(@NonNull Favourite favourite) {
        mFavouriteDBSource.updateFavourite(favourite);
        notifyContentObserver();
    }

    @Override
    public void deleteAllFavourites() {
        mFavouriteDBSource.deleteAllFavourites();
        notifyContentObserver();
    }

    @Override
    public void deleteFavourite(@NonNull String favouriteId) {
        mFavouriteDBSource.deleteFavourite(favouriteId);
        notifyContentObserver();
    }

    public WindForecast getCurrentWindForecast(String countryCode, String city) {
        WindForecast windForecast = null;
        Map<String, String> options = new HashMap<>();

        options.put("q", city + "," + countryCode.toLowerCase());
        options.put("units", "metric");
        options.put("APPID", Settings.API_KEY);
        Call<WeatherForecast> call = mWeatherService.getCurrentWindForecast(options);
        try {
            windForecast = call.execute().body().getWindForecast();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return windForecast;
    }

    public WeatherForecasts getFiveDaysForecastAsync(String countryCode, String city, Callback<WeatherForecasts> callback) {
        WeatherForecasts weatherForecasts = new WeatherForecasts(null);
        Map<String, String> options = new HashMap<>();

        options.put("q", city + "," + countryCode.toLowerCase());
        options.put("units", "metric");
        options.put("APPID", Settings.API_KEY);
        mAsyncForecastCall = mWeatherService.getFiveDaysForecast(options);
        mAsyncForecastCall.enqueue(callback);
        return weatherForecasts;
    }

    public void cancelFiveDaysForecastRequest(){
        if(mAsyncForecastCall.isExecuted()){
            mAsyncForecastCall.cancel();
        }
    }

    public interface FavouritesRepositoryObserver {

        void onFavouriteChanged();
    }

    public void refreshFavourites() {
        notifyContentObserver();
    }
}
