package com.test.anton.windforecast.data.remote;


import com.test.anton.windforecast.CustomApplication;
import com.test.anton.windforecast.Settings;
import com.test.anton.windforecast.network.NetworkManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.test.anton.windforecast.Settings.SYNCHRONIZATION_CACHE_SIZE;
import static com.test.anton.windforecast.Settings.SYNCHRONIZATION_INTERVAL_ONLINE;
import static com.test.anton.windforecast.Settings.SYNCHRONIZATION_INTERVAL_OFFLINE;
import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

public class WeatherInjector {
    private static final String CACHE_CONTROL = "Cache-Control";

    private static Retrofit provideRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(CustomApplication.getInstance().getCacheDir(), "http-cache"), SYNCHRONIZATION_CACHE_SIZE);
        } catch (Exception e) {
            Timber.e(e, "Could not create Cache!");
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.d(message);
            }
        });
        httpLoggingInterceptor.setLevel(HEADERS);
        return httpLoggingInterceptor;
    }

    private static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(SYNCHRONIZATION_INTERVAL_ONLINE, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private static Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!NetworkManager.getInstance().isConnected()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(SYNCHRONIZATION_INTERVAL_OFFLINE, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    public static WeatherService provideWeatherService() {
        return provideRetrofit(Settings.DOMAIN_NAME).create(WeatherService.class);
    }

}
