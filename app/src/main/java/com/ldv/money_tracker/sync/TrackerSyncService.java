package com.ldv.money_tracker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by user on 23.10.2016.
 */

//сервис  выполняется в UI потоке, интент сервис запускается в отдельном потоке, сам останавливается
public class TrackerSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static TrackerSyncAdapter sTrackerSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sTrackerSyncAdapter == null) {
                sTrackerSyncAdapter = new TrackerSyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sTrackerSyncAdapter.getSyncAdapterBinder();
    }
}
