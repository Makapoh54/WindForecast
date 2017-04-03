package com.test.anton.windforecast.data.remote;


import com.test.anton.windforecast.models.WeatherForecast;
import com.test.anton.windforecast.models.WeatherForecasts;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WeatherService {

    @GET("data/2.5/weather")
    Call<WeatherForecast> getCurrentWindForecast(@QueryMap Map<String, String> options);

    @GET("data/2.5/forecast")
    Call<WeatherForecasts> getFiveDaysForecast(@QueryMap Map<String, String> options);

}
