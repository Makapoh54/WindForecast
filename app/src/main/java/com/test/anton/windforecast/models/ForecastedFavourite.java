package com.test.anton.windforecast.models;


public class ForecastedFavourite {

    private Favourite mFavourite;
    private WindForecast mWindForecast;

    public ForecastedFavourite(Favourite favourite, WindForecast windForecast) {
        mFavourite = favourite;
        mWindForecast = windForecast;
    }

    public Favourite getFavourite() {
        return mFavourite;
    }

    public void setFavourite(Favourite favourite) {
        mFavourite = favourite;
    }

    public WindForecast getWindForecast() {
        return mWindForecast;
    }

    public void setWindForecast(WindForecast windForecast) {
        mWindForecast = windForecast;
    }
}
