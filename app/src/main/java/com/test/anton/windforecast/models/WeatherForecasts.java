package com.test.anton.windforecast.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WeatherForecasts implements Serializable{

    @SerializedName("list")
    private List<WeatherForecast> mWeatherForecastList;

    public WeatherForecasts(List<WeatherForecast> weatherForecastList) {
        mWeatherForecastList = weatherForecastList;
    }

    public List<WeatherForecast> getWeatherForecastList() {
        return mWeatherForecastList;
    }

    public void setWeatherForecastList(List<WeatherForecast> weatherForecastList) {
        mWeatherForecastList = weatherForecastList;
    }
}
