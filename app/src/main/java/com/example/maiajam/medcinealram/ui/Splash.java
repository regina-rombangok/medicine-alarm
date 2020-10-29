package com.example.maiajam.medcinealram.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.maiajam.medcinealram.R;

import static com.example.maiajam.medcinealram.helper.Global.English;
import static com.example.maiajam.medcinealram.helper.Global.arabic;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.getSelectedLangSP;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.setAppLanguage;
import static java.lang.Thread.sleep;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        changeAppLanguage();
        loadTheLogo();


    }

    private void loadTheLogo() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2*1000);
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void changeAppLanguage() {

        if(getSelectedLangSP(getBaseContext()).equals(arabic))

                setAppLanguage(getBaseContext(),arabic);
             else
                 setAppLanguage(getBaseContext(),English);

    }
}
