package com.oxo.haiti.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by jaswinderwadali on 17/05/16.
 */
public class Connectivity {

    public static boolean InternetAvailable(Context context) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0 || isOnline(context));
        } catch (IOException e) {
            e.printStackTrace();
            return isOnline(context);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return isOnline(context);
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
