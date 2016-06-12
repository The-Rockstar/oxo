package com.oxo.haiti.service;

import android.content.Context;

import com.oxo.haiti.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaswinderwadali on 5/16/2016.
 */
public class RestAdapter {

    private static RestAdapter restAdapter;

    private ApiService apiService;

    private ApiServiceMain apiServiceMain;

    private RestAdapter(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        Retrofit retrofitMain = new Retrofit.Builder()
//                .baseUrl(context.getString(R.string.base_url_main))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        apiServiceMain = retrofitMain.create(ApiServiceMain.class);
        apiService = retrofit.create(ApiService.class);
    }

    public static RestAdapter getInstance(Context context) {
        if (restAdapter == null)
            restAdapter = new RestAdapter(context);
        return restAdapter;
    }

    public ApiService getApiService() {
        return apiService;
    }

//    public ApiServiceMain getApiServiceMain() {
////        return apiServiceMain;
////    }
}
