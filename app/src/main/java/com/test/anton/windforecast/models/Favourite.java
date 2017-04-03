package com.test.anton.windforecast.models;


import android.text.TextUtils;

public class Favourite {

    private String mId;

    private String mCountryCode;

    private String mCity;

    public Favourite(String countryCode, String city) {
        mCountryCode = countryCode;
        mCity = city;
    }

    public Favourite(String id, String countryCode, String city) {
        mId = id;
        mCountryCode = countryCode;
        mCity = city;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public boolean isEmpty() {
        return !TextUtils.isEmpty(mCity) && !TextUtils.isEmpty(mCountryCode);
    }
}
