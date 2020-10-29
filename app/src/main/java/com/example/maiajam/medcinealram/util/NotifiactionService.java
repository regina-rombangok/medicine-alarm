package com.example.maiajam.medcinealram.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import static com.example.maiajam.medcinealram.helper.NotificationHelper.notifyMyAboutTheMedcine;

public class NotifiactionService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotifiactionService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       getMedInfoAndNotifyUser(getBaseContext(),intent);
    }


    private void getMedInfoAndNotifyUser(Context context, Intent intent) {
        String med_name = intent.getStringExtra("med_name");
        String med_note = intent.getStringExtra("med_note");
        String med_Dose = intent.getStringExtra("med_dose");
        String med_member = intent.getStringExtra("med_member");

        notifyMyAboutTheMedcine(context, med_name, med_note, med_member);
    }
}
