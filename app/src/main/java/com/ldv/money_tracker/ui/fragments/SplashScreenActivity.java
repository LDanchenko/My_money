package com.ldv.money_tracker.ui.fragments;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ldv.money_tracker.MoneyTrackerApplication;
import com.ldv.money_tracker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.splash)
public class SplashScreenActivity extends AppCompatActivity {

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    public final int SPLASH_DISPLAY_LENGTH = 800;


    @AfterViews
    void ready() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              if ((MoneyTrackerApplication.getEmail()).equals("")) {//если токена нет, открывает страницувхола
//                     По истечении времени, запускаем главный активити, а Splash Screen закрываем
                   Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity_.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                } else {
                   Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity_.class); //Если есть сразу мейн
                    SplashScreenActivity.this.startActivity(mainIntent);
               }
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
