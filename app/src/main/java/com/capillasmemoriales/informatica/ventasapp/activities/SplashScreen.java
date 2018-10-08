package com.capillasmemoriales.informatica.ventasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.capillasmemoriales.informatica.ventasapp.R;

import java.util.Random;

public class SplashScreen extends AppCompatActivity {

    private Boolean btnStatus = false;
    private static final int DURATION = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ImageView app_icon = findViewById(R.id.app_icon);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        app_icon.setAnimation(animation);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (!btnStatus) {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
            }
        },DURATION);
    }

    public void onBackPressed(){
        btnStatus = true;
        super.onBackPressed();
    }
}