package com.example.maiajam.medcinealram.util.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.maiajam.medcinealram.helper.HelperMethodes;
import com.example.maiajam.medcinealram.util.AlarmService;

import static com.example.maiajam.medcinealram.helper.HelperMethodes.getAllMedicneFromDbAndRefireAlarms;

public class BootReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, AlarmService.class);
        context.startService(startServiceIntent);
//        checkIfForBootCompleted(context,intent);
    }

    private void checkIfForBootCompleted(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
          getAllMedicneFromDbAndRefireAlarms(context);
        }
    }

}
