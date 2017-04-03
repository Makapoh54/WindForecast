package com.test.anton.windforecast.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WindForecast implements Serializable {

    @SerializedName("speed")
    private String mWindSpeed;

    @SerializedName("deg")
    private String mWindDegree;

    public WindForecast(String windSpeed, String windDegree) {
        mWindSpeed = windSpeed;
        mWindDegree = windDegree;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        mWindSpeed = windSpeed;
    }

    public String getWindDegree() {
        return mWindDegree;
    }

    public void setWindDegree(String windDegree) {
        mWindDegree = windDegree;
    }
}
