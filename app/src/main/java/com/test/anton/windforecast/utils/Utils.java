package com.test.anton.windforecast.utils;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static void hideView(@NonNull View view) {
        view.setVisibility(View.GONE);
    }

    public static void showView(@NonNull View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static String getCountryCode(@NonNull String countryName) {
        Locale l;
        for (String iso : Locale.getISOCountries()) {
            l = new Locale("", iso);
            if (l.getDisplayCountry().equals(countryName)) {
                return iso;
            }
        }
        return countryName;
    }

    public static String getWindCardinalDirection(@NonNull String deg) {
        if (TextUtils.isEmpty(deg)) return "";
        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        return directions[(int) Math.round(((Double.parseDouble(deg) % 360) / 45)) % 8];
    }
}
