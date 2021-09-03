package com.ldv.money_tracker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

//у сервиса другой жизненый цикл
public class TrackerAuthenticatorService extends Service {

    private TrackerAuthenticator trackerAuthenticator;

    @Override
    public void onCreate() {
        trackerAuthenticator = new TrackerAuthenticator(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
