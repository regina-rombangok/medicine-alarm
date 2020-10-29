package com.example.maiajam.medcinealram.helper;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.maiajam.medcinealram.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.maiajam.medcinealram.helper.Global.alarmSound;
import static com.example.maiajam.medcinealram.helper.Global.vibratePattern;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.getLightMode;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.getSoundMode;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.getVibrateMode;

/**
 * Created by maiAjam on 9/11/2017.
 */

public class NotificationHelper  {


    String med_name;
    String med_note;
    String med_member;


    public static void getNotification(Context context, String med_name, String med_note, String med_member) {

        NotificationCompat.Builder  builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle("")
                .setContentText("");


        Notification notification  = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notifMang = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);


        notifMang.notify(1,builder.build() );

    }
    public static void notifyMyAboutTheMedcine(Context context, String med_name, String med_note, String med_Dose) {

        String chanelId = "10";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = getSoundMode(context)?NotificationManager.IMPORTANCE_DEFAULT:NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel = new NotificationChannel(chanelId, context.getString(R.string.channel_name), importance);
            channel.setDescription(context.getString(R.string.channel_description));
            channel.enableVibration(getVibrateMode(context)?true:false);
            channel.enableLights(getLightMode(context)?true:false);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(context, "v")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(med_name)
                .setContentText(med_note + med_Dose)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(chanelId)
                .setOnlyAlertOnce(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(med_note))
                .setAutoCancel(true)
        ;

        if (getSoundMode(context))
            builder.setSound(alarmSound);
        if (getVibrateMode(context))
            builder.setVibrate(vibratePattern);
        if (getLightMode(context))
            builder.setLights(Color.BLUE, 500, 500);

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), notification);

    }



}
