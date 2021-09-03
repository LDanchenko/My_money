package com.ldv.money_tracker.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.ldv.money_tracker.MoneyTrackerApplication;
import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ldv.money_tracker.utils.ConstansManager.SUCCESS;

/**
 * Created by user on 23.10.2016.
 */

public class TrackerSyncAdapter extends AbstractThreadedSyncAdapter {

    private NotificationManager mNotificationManager;

    private boolean isNotificationsEnabled;
    private boolean isVibrateEnabled;
    private boolean isSoundEnabled;
    private boolean isLedEnabled;
    private boolean isForegroundEnabled;

    private static final boolean DEFAULT_VALUE = true;
    private String globalNotificationsKey;
    private String vibrateNotificationsKey;
    private String soundNotificationsKey;
    private String ledNotificationsKey;
    private String foregroundNotificationsKey;


    private static final long[] VIBRATE_PATTERN = new long[]{1000, 1000, 1000};
    private static final int LED_LIGHTS_TIME_ON = 200;
    private static final int LED_LIGHTS_TIME_OFF = 1500;
    private static final int NOTIFICATION_ID = 4004;

    public String statusCategories = "";
    public String statusExpenses = "";

    private final static String LOG_TAG = TrackerSyncAdapter.class.getSimpleName();

    public TrackerSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    //тут написать запросы
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Syncing started");

    }


    public void init() {

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        globalNotificationsKey = getContext().getString(R.string.pref_enable_notifications_key);
        vibrateNotificationsKey = getContext().getString(R.string.pref_enable_vibrate_notifications_key);
        soundNotificationsKey = getContext().getString(R.string.pref_enable_sound_notifications_key);
        ledNotificationsKey = getContext().getString(R.string.pref_enable_led_notifications_key);
        foregroundNotificationsKey = getContext().getString(R.string.pref_enable_foreground_notifications_key);

        isNotificationsEnabled = mSharedPreferences.getBoolean(globalNotificationsKey, DEFAULT_VALUE);
        isVibrateEnabled = mSharedPreferences.getBoolean(vibrateNotificationsKey, DEFAULT_VALUE);
        isSoundEnabled = mSharedPreferences.getBoolean(soundNotificationsKey, DEFAULT_VALUE);
        isLedEnabled = mSharedPreferences.getBoolean(ledNotificationsKey, DEFAULT_VALUE);
        isForegroundEnabled = mSharedPreferences.getBoolean(foregroundNotificationsKey, DEFAULT_VALUE);

        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void sendNotificationSuccess() {

        init();

        if (!isNotificationsEnabled) {
            return;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getContext().getString(R.string.app_name))
                .setContentText(getContext().getString(R.string.syncing_notification_message))
                .setAutoCancel(true);


        if (isLedEnabled) {
            builder.setLights(Color.CYAN, LED_LIGHTS_TIME_ON, LED_LIGHTS_TIME_OFF);
        }

        if (isSoundEnabled) {
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }

        if (isVibrateEnabled) {
            builder.setVibrate(VIBRATE_PATTERN);
        }

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());


    }

    public void sendNotificationError() {
        init();

        if (!isNotificationsEnabled) {
            return;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getContext().getString(R.string.app_name))
                .setContentText(getContext().getString(R.string.syncing_notification_error))
                .setAutoCancel(true);


        if (isLedEnabled) {
            builder.setLights(Color.CYAN, LED_LIGHTS_TIME_ON, LED_LIGHTS_TIME_OFF);
        }

        if (isSoundEnabled) {
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }

        if (isVibrateEnabled) {
            builder.setVibrate(VIBRATE_PATTERN);
        }

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }





    //чтобы сразу запустить синхронизацию
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,
                true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,
                true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    //тут создаем аккаунт
    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name),
                context.getString(R.string.sync_account_type));

        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void onAccountCreated(Account newAccount, Context context) {
        final int SYNC_INTERVAL = 60 * 60 * 24; //время в секуднах через которое производим синхронизацю, раз в день
        final int SYNC_FLEXTIME = SYNC_INTERVAL / 3; //если нет интернета, синк адаптер запомнит время и повторит через определенный промежуток времени
        TrackerSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount,
                context.getString(R.string.content_authority), true);
        ContentResolver.addPeriodicSync(newAccount,
                context.getString(R.string.content_authority),
                Bundle.EMPTY,
                SYNC_INTERVAL);
        syncImmediately(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);//проверка возвращает или создает аккаунт
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //проверка на версию
// we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
