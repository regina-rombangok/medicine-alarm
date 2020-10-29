package com.example.maiajam.medcinealram.helper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.DisplayMetrics;

import com.example.maiajam.medcinealram.R;
import com.example.maiajam.medcinealram.data.model.Medcine;
import com.example.maiajam.medcinealram.data.sql.Mysql;
import com.example.maiajam.medcinealram.util.reciver.AlarmReciver;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.AlarmManager.RTC_WAKEUP;

public class HelperMethodes {

    static SharedPreferences selectedLangSP;
    private static Locale locale;
    private static SharedPreferences VibrateModeSP;
    private static String Vibrate_MODE = "VibrateMode";
    private static String SOUND_MODE = "soundMode";
    private static SharedPreferences soundModeSP;
    private static SharedPreferences lightModeSP;
    private static String LIGHT_MODE = "lightMode";
    private static String SELECTED_lANG = "selectedLanguage";
    private static Intent intent;
    private static AlarmManager alarmManager;
    private static PendingIntent operation;
    String selectedLang;

    public static void setSelectedLanguage(Context context, String lang) {

        selectedLangSP = context.getSharedPreferences(SELECTED_lANG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = selectedLangSP.edit();
        editor.putString(SELECTED_lANG, lang);
        editor.apply();
        editor.commit();
    }

    public static String getSelectedLangSP(Context context) {
        selectedLangSP = context.getSharedPreferences(SELECTED_lANG, Context.MODE_PRIVATE);
        return selectedLangSP.getString(SELECTED_lANG, "en");
    }


    public static void setAppLanguage(Context context, String appLang) {

        locale = new Locale(appLang);
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration con = resources.getConfiguration();
        con.locale = locale;
        con.setLayoutDirection(locale);
        resources.updateConfiguration(con, displayMetrics);

    }



    public static void setLayoutDirction(Context context, String locale) {

        Configuration config;
        config = new Configuration(context.getResources().getConfiguration());

        switch (locale) {
            case "ar":
                config.locale = new Locale("ar");
                config.setLayoutDirection(new Locale("ar"));
                break;
            case "en":
                config.locale = new Locale("en");
                config.setLayoutDirection(new Locale("en"));
                break;
        }

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());


    }

    public static void rateTheApp(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void shareTheApp(Context baseContext) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.maiajam.medcinealram \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            baseContext.startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public static void setVibrateMode(Boolean value, Context context) {
        VibrateModeSP = context.getSharedPreferences(Vibrate_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = VibrateModeSP.edit();
        editor.putBoolean(Vibrate_MODE, value);
        editor.apply();
        editor.commit();
    }


    public static void setSoundMode(Boolean value, Activity activity) {
        soundModeSP = activity.getSharedPreferences(SOUND_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = soundModeSP.edit();
        editor.putBoolean(SOUND_MODE, value);
        editor.apply();
        editor.commit();
    }

    public static Boolean getVibrateMode(Context context) {
        return context.getSharedPreferences(Vibrate_MODE, Context.MODE_PRIVATE).getBoolean(Vibrate_MODE, true);
    }

    public static Boolean getSoundMode(Context context) {
        return context.getSharedPreferences(SOUND_MODE, Context.MODE_PRIVATE).getBoolean(SOUND_MODE, true);
    }

    public static boolean getLightMode(Context context) {
        return context.getSharedPreferences(LIGHT_MODE, Context.MODE_PRIVATE).getBoolean(LIGHT_MODE, true);
    }

    public static void setLightMode(Context context, Boolean value) {
        lightModeSP = context.getSharedPreferences(LIGHT_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lightModeSP.edit();
        editor.putBoolean(LIGHT_MODE, value);
        editor.apply();
        editor.commit();
    }

    public static void cancelAlarm(Context context, int medid) {
    intent = new Intent(context, AlarmReciver.class);
     alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
       operation = PendingIntent.getBroadcast(context,medid,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(operation);
    }

    public static void refireTheAlarm(Context context,int medId,int repeatingTime) {
        intent = new Intent(context, AlarmReciver.class);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        operation = PendingIntent.getBroadcast(context,medId,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        switch (repeatingTime) {
            case 1:// No repeating
                alarmManager.setRepeating(RTC_WAKEUP, System.currentTimeMillis(),AlarmManager.INTERVAL_DAY, operation);
                break;
            case 2:// repeat twice
                alarmManager.setRepeating(RTC_WAKEUP,System.currentTimeMillis(),12 * 60 * 60 * 1000, operation);
                break;
            case 3://repeat 3 times
                alarmManager.setRepeating(RTC_WAKEUP,System.currentTimeMillis(),8 * 60 * 60 * 1000,operation);
                break;
        }


    }

    public static void getAllMedicneFromDbAndRefireAlarms(Context context) {
        Mysql db = new Mysql(context);
        List<Medcine> medcineList = db.allMedcine();

        for(Medcine medcine:medcineList){
            refireTheAlarm(context,medcine.getMedcineId(),medcine.getNoTime());
        }
    }
}
