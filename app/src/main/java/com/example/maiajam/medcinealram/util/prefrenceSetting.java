package com.example.maiajam.medcinealram.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.maiajam.medcinealram.FragmentPrefrence;
import com.example.maiajam.medcinealram.R;
import com.example.maiajam.medcinealram.helper.HelperMethodes;

import java.util.Locale;

import static com.example.maiajam.medcinealram.helper.HelperMethodes.setLightMode;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.setSoundMode;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.setVibrateMode;

public class prefrenceSetting extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    Locale locale;


    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            if (key.equals("lang")) {

                String pre = sp.getString("lang", null);

                if (pre == "العربية") {

                    locale = new Locale("ar");
                    Resources resources = getResources();
                    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                    Configuration con = resources.getConfiguration();
                    con.locale = locale;
                    resources.updateConfiguration(con, displayMetrics);
                    Intent i = new Intent(getBaseContext(), prefrenceSetting.class);
                    startActivity(i);

                } else {
                    if (pre == "Engilsh") {

                        locale = new Locale("en");
                        Resources resources = getResources();
                        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                        Configuration con = resources.getConfiguration();
                        con.locale = locale;
                        resources.updateConfiguration(con, displayMetrics);
                        Intent i = new Intent(getBaseContext(), prefrenceSetting.class);
                        startActivity(i);

                    }
                }
            } else if (key.equals("vibrate")) {

                if (sp.getBoolean("vibrate", false)) {
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    setVibrateMode(true, prefrenceSetting.this);
                } else {
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    setVibrateMode(false, prefrenceSetting.this);
                }
            } else if (key.equals("light")) {
                if (sp.getBoolean("light", false))
                    setLightMode(prefrenceSetting.this, true);
                else
                    setLightMode(prefrenceSetting.this, false);
            }else if(key.equals("sound"))
            {
               setSoundMode(sp.getBoolean("sound",false), prefrenceSetting.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrence_setting);
        getFragmentManager().beginTransaction().replace(R.id.cont, new FragmentPrefrence()).commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).registerOnSharedPreferenceChangeListener(listener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (key.equals("lang")) {

            String pre = sp.getString("lang", null);

            if (pre == "العربية") {

                locale = new Locale("ar");
                Resources resources = getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                Configuration con = resources.getConfiguration();
                con.locale = locale;
                resources.updateConfiguration(con, displayMetrics);
                Intent i = new Intent(this, prefrenceSetting.class);
                startActivity(i);

            } else if (pre == "Engilsh") {

                locale = new Locale("en");
                Resources resources = getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                Configuration con = resources.getConfiguration();
                con.locale = locale;
                resources.updateConfiguration(con, displayMetrics);
                Intent i = new Intent(this, prefrenceSetting.class);
                startActivity(i);

            }
        } else if (key.equals("vibrate")) {


            if (sp.getBoolean("vibrate", false)) {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        } else if (key.equals("light")) {

            if (sp.getBoolean("light", false)) {

            } else {

            }


        }


    }
}
