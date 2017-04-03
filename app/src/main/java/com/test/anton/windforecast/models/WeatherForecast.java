package com.test.anton.windforecast.models;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherForecast implements Serializable{

    @SerializedName("dt")
    private String mTimeStamp;

    @SerializedName("wind")
    private WindForecast mWindForecast;

    public WeatherForecast(WindForecast windForecast) {
        mWindForecast = windForecast;
    }

    public WeatherForecast(String timeStamp, WindForecast windForecast) {
        mTimeStamp = timeStamp;
        mWindForecast = windForecast;
    }

    public WindForecast getWindForecast() {
        return mWindForecast;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

    public void setWindForecast(WindForecast windForecast) {
        mWindForecast = windForecast;
    }
}
