package com.example.maiajam.medcinealram.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {
    public static boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        startServiceWithNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals("ACTION_START_SERVICE")) {
            startServiceWithNotification();
        }
        else stopMyService();
        return START_STICKY;
    }

    // In case the service is deleted or crashes some how
    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    void stopMyService() {
        stopSelf();
        isServiceRunning = false;
    }

    void startServiceWithNotification() {
        if (isServiceRunning) return;
        isServiceRunning = true;

    }
}