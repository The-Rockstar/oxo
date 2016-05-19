package com.oxo.haiti;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.oxo.haiti.service.SyncService;
import com.oxo.haiti.storage.SnappyNoSQL;

import java.util.Calendar;

/**
 * Created by jaswinderwadali on 5/17/2016.
 */
public class OxoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SnappyNoSQL.init(this);
        startLocationService();
    }

    // Start service every 15 mints
    public void startLocationService() {
        Intent mServiceIntent = new Intent(this, SyncService.class);
        Calendar cal = Calendar.getInstance();
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, mServiceIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                15 * 60 * 1000, pendingIntent);
    }

}
