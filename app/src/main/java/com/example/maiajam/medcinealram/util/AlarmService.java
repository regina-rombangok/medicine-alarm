package com.example.maiajam.medcinealram.util;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.example.maiajam.medcinealram.util.reciver.AlarmReciver;

import static android.app.AlarmManager.RTC_WAKEUP;

public class AlarmService extends IntentService {

    private AlarmManager medicineAlarm;
    private PendingIntent pendingIntent;
    private Intent intentReciver;
    private int noTime;
    private long IntilaDelay;
    private String Med_name;
    private String Med_Note;
    private String Med_Dose;
    private int med_Id;
    // Must create a default constructor
    public AlarmService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onHandleIntent(Intent intent) {
        noTime = (int) intent.getExtras().get("noTime");
        IntilaDelay = (long) intent.getExtras().get("IntilaDelay");
        Med_name = (String) intent.getExtras().get("Med_name");
        Med_Dose = (String) intent.getExtras().get("Med_Dose");
        Med_Note = (String) intent.getExtras().get("Med_Note");
        med_Id = (int) intent.getExtras().get("med_Id");
        initializeAlarm();
        switch (noTime) {
            case 1:// No repeating at the same day
                medicineAlarm.setExactAndAllowWhileIdle(RTC_WAKEUP, IntilaDelay, pendingIntent);
                break;
            case 2:// repeat twice
                medicineAlarm.setRepeating(RTC_WAKEUP, IntilaDelay, 12 * 60 * 60 * 1000, pendingIntent);
                break;
            case 3://repeat 3 times
                medicineAlarm.setRepeating(RTC_WAKEUP, IntilaDelay, 8 * 60 * 60 * 1000, pendingIntent);
                break;
        }
    }

    private void initializeAlarm() {
        medicineAlarm = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
        intentReciver = new Intent(this, AlarmReciver.class);
        intentReciver.putExtra("med_name", Med_name)
                .putExtra("med_note", Med_Note)
                .putExtra("med_dose", Med_Dose)
                .putExtra("repeatedTime", noTime);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), med_Id, intentReciver, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}