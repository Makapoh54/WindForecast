package com.test.anton.windforecast;

/**
 * Various endpoints, sync options, etc.
 */
public class Settings {
    // Endpoints
    public static final String DOMAIN_NAME = "http://api.openweathermap.org/";
    public static final String API_KEY = "88fe3f26971517e8be73ecfc910a3a7c";


    // Data Synchronization
    public static final int SYNCHRONIZATION_INTERVAL_ONLINE = 2; // minutes
    public static final int SYNCHRONIZATION_INTERVAL_OFFLINE = 7; //days
    public static final long SYNCHRONIZATION_CACHE_SIZE = 10 * 1024 * 1024;// 10 mb
}