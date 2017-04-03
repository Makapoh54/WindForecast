package com.test.anton.windforecast.windcast.favouritesdetails;

import android.support.annotation.NonNull;

import com.test.anton.windforecast.data.FavouritesRepository;
import com.test.anton.windforecast.models.WeatherForecast;
import com.test.anton.windforecast.models.WeatherForecasts;
import com.test.anton.windforecast.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class FavouriteDetailsPresenter implements FavouriteDetailsContract.Presenter {

    private final FavouriteDetailsContract.View mDetailsView;

    private WeatherForecasts mForecast;

    private FavouritesRepository mFavouritesRepository;

    private List<Float> mWindSpeed = new ArrayList<>();

    private List<String> mChartLegend = new ArrayList<>();


    private String mCountryCode, mCityName;

    public FavouriteDetailsPresenter(@NonNull FavouriteDetailsContract.View favouritesView, @NonNull FavouritesRepository repository,
                                     @NonNull String countryCode, @NonNull String cityName) {
        mDetailsView = favouritesView;
        mFavouritesRepository = repository;
        mCountryCode = countryCode;
        mCityName = cityName;
    }

    @Override
    public void loadForecast() {
        mDetailsView.setLoadingIndicator(true);
        mForecast = mFavouritesRepository.getFiveDaysForecastAsync(mCountryCode, mCityName, new Callback<WeatherForecasts>() {
            @Override
            public void onResponse(Call<WeatherForecasts> call, Response<WeatherForecasts> response) {
                if (response.isSuccessful()) {
                    mForecast = response.body();
                    prepareForecastData();
                    mDetailsView.showDetails(mWindSpeed, mChartLegend);
                    mDetailsView.setLoadingIndicator(false);
                    Timber.i("Forecast details were loaded from api.");
                }
            }

            @Override
            public void onFailure(Call<WeatherForecasts> call, Throwable t) {
                if (!call.isCanceled()) {
                    mDetailsView.setLoadingIndicator(false);
                    mDetailsView.showLoadingError();
                    Timber.e(t, "Unable to load user details data.");
                } else {
                    Timber.e(t, "Forecast data request was cancelled.");
                }
            }
        });
    }

    @Override
    public void cancelRequest() {
        mFavouritesRepository.cancelFiveDaysForecastRequest();
    }


    private void prepareForecastData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd", Locale.ENGLISH);
        Calendar tomorrow = Calendar.getInstance();
        boolean isToday = true;
        Date forecastDate;

        tomorrow.add(Calendar.DAY_OF_MONTH, 2);
        tomorrow.set(Calendar.HOUR_OF_DAY, 12);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);

        for (WeatherForecast forecast : mForecast.getWeatherForecastList()) {
            forecastDate = new Date(Long.parseLong(forecast.getTimeStamp()) * 1000L);
            if (forecastDate.before(tomorrow.getTime()) && isToday) {
                mWindSpeed.add(Float.parseFloat(forecast.getWindForecast().getWindSpeed()));
                mChartLegend.add("Today");
                isToday = false;
            } else if (forecastDate.equals(tomorrow.getTime())) {
                mWindSpeed.add(Float.parseFloat(forecast.getWindForecast().getWindSpeed()));
                mChartLegend.add(dateFormat.format(tomorrow.getTime()) + "(" + Utils.getWindCardinalDirection(forecast.getWindForecast().getWindDegree()) + ")");
                tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }
}
