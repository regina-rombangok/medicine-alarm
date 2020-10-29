package com.example.maiajam.medcinealram.util.reciver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.maiajam.medcinealram.helper.Global;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.content.Context.ALARM_SERVICE;
import static com.example.maiajam.medcinealram.helper.NotificationHelper.notifyMyAboutTheMedcine;

/**
 * Created by maiAjam on 9/11/2017.
 */

public class AlarmReciver extends BroadcastReceiver {
    private AlarmManager medicineAlarm;
    private Intent intentReciver;
    private String med_name;
    private String med_Dose, med_note;
    private int med_Id;
    private PendingIntent pendingIntent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        getMedInfoAndNotifyUser(context, intent);
        resetTheNextRepetedAlarm(context, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resetTheNextRepetedAlarm(Context context, Intent intent) {
        int repeatedTime = intent.getIntExtra("repeatedTime", 1);
        setAlarm(context, repeatedTime);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm(Context context, int repeatedTime) {

        medicineAlarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        intentReciver = new Intent(context, AlarmReciver.class);
        intentReciver.putExtra("med_name", med_name)
                .putExtra("med_note", med_note)
                .putExtra("med_dose", med_Dose);

        pendingIntent = PendingIntent.getBroadcast(context, med_Id, intentReciver, PendingIntent.FLAG_UPDATE_CURRENT);
        switch (repeatedTime) {
            case Global.Once_Aday:
                medicineAlarm.setExactAndAllowWhileIdle(RTC_WAKEUP, INTERVAL_DAY, pendingIntent);
                break;
            case Global.Three_Times:
                medicineAlarm.setExactAndAllowWhileIdle(RTC_WAKEUP, 12 * 60 * 60 * 1000, pendingIntent);
                break;
            case Global.Twice_Aday:
                medicineAlarm.setExactAndAllowWhileIdle(RTC_WAKEUP, 8 * 60 * 60 * 1000, pendingIntent);
                break;
        }

    }

    private void getMedInfoAndNotifyUser(Context context, Intent intent) {
        med_name = intent.getStringExtra("med_name");
        med_note = intent.getStringExtra("med_note");
        med_Dose = intent.getStringExtra("med_dose");
        med_Id = intent.getIntExtra("med_Id", 0);

        notifyMyAboutTheMedcine(context, med_name, med_note, med_Dose);
    }


}
