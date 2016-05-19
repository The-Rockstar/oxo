package com.oxo.haiti.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.oxo.haiti.model.CommonModel;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.utils.Connectivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaswinderwadali on 5/17/2016.
 */
public class SyncService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Connectivity.InternetAvailable(this))
            syncData();
        return Service.START_STICKY;
    }


    private void syncData() {
        if (Connectivity.InternetAvailable(this)) {
            List<String> stringList = SnappyNoSQL.getInstance().getSurveyData();
            for (String data : stringList) {
                syncData(data);
            }
        }
    }

    void syncData(final String data) {
        Call<CommonModel> commonModelCall = RestAdapter.getInstance(this).getApiService().syncData(data);
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                SnappyNoSQL.getInstance().removeData(data);
                Log.d("",data);
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {

            }
        });


    }

}
