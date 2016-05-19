package com.oxo.haiti.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.oxo.haiti.service.SyncService;

/**
 * Created by wadali on 5/17/2016.
 */
public class ConnectivityBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            Intent serviceIntent = new Intent(context, SyncService.class);
            context.startService(serviceIntent);
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}
