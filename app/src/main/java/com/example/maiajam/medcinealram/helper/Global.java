package com.example.maiajam.medcinealram.helper;

import android.media.RingtoneManager;
import android.net.Uri;

public interface Global {

    String arabic = "ar";
    String English= "en";

    int Twice_Aday = 2;
    int Three_Times = 3;
    int Once_Aday = 1;


    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    long[] vibratePattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
}
