package com.oxo.haiti.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jaswinderwadali on 5/16/2016.
 */
public class ContentStorage {
    private static final String SURVEY_ONE_POSITION = "SUR_ONE";
    private static final String SURVEY_TWO_POSITION = "SUR_TWO";

    private static ContentStorage contentStorage;
    private static final String USER_PREF = "USER_PREF";
    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String USER_DATA = "USER_PREF";
    private static final String USER_PASS = "USER_PASS";


    SharedPreferences sharedPreferences;

    public ContentStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREF, 0);
    }


    public static ContentStorage getInstance(Context context) {
        if (contentStorage == null)
            contentStorage = new ContentStorage(context);
        return contentStorage;
    }


    public String getUserID() {
        return sharedPreferences.getString(USER_DATA, null);
    }

    public void loggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(LOGGED_IN, loggedIn).apply();
    }


    public void loggedIn(String username, String pass) {
        sharedPreferences.edit().putString(USER_DATA, username).apply();
        sharedPreferences.edit().putString(USER_PASS, pass).apply();

    }


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }


    public void savePositionSurveyOne(int currentItem, int survey) {
        switch (survey) {
            case 0:
                sharedPreferences.edit().putInt(SURVEY_ONE_POSITION, currentItem).apply();
                break;
            case 1:
                sharedPreferences.edit().putInt(SURVEY_TWO_POSITION, currentItem).apply();
                break;
        }
    }

    public int getPositionSurveyOne(int currentItem) {
        switch (currentItem) {
            case 0:
                return sharedPreferences.getInt(SURVEY_ONE_POSITION, 0);
            case 1:
                return sharedPreferences.getInt(SURVEY_TWO_POSITION, 0);
            default:
                return 0;
        }
    }

}
