package com.ldv.money_tracker;


import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.activeandroid.ActiveAndroid;
import com.facebook.stetho.Stetho;

import static com.ldv.money_tracker.utils.ConstansManager.EMAIL_KEY;
import static com.ldv.money_tracker.utils.ConstansManager.GOOGLE_TOKEN_KEY;
import static com.ldv.money_tracker.utils.ConstansManager.NAME_KEY;
import static com.ldv.money_tracker.utils.ConstansManager.PICTURE_KEY;
import static com.ldv.money_tracker.utils.ConstansManager.SHARE_PREF_FILE_NAME;
import static com.ldv.money_tracker.utils.ConstansManager.TOKEN_KEY;


//класс нужен для инициализации базы,этот класс нужно регистировать в манифесте
public class MoneyTrackerApplication extends Application { //этот компонент инициализируется в первую очередь, перед всеми активити

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        Stetho.initializeWithDefaults(this);

        sharedPreferences = getSharedPreferences(SHARE_PREF_FILE_NAME, MODE_PRIVATE);//тут находим файл по имени и задаем режим - приватный, потому что там токен
        //токен если какое то соц сети приложение нужно шифровать
        //sharedpreference - файл,который хранится в папке приложения
        //это хмл файл, где хранятся  ключ - значение, типа стрингс.хмл
        //работать в классе приложения
    }




    public static void saveGoogleAuthToken(String token) {
        sharedPreferences.edit().putString(GOOGLE_TOKEN_KEY, token).apply();//передали токен в файл
    }

    public static String getGoogleAuthToken() {
        return sharedPreferences.getString(GOOGLE_TOKEN_KEY, "");//вытянули токен по ключу, второй параметр - значение по умолчанию
        //если нет такого токена прдет пустая строка
    }

    public static void saveEmail(String email) {

        sharedPreferences.edit().putString(EMAIL_KEY, email).apply();
    }

    public static String getEmail() {
        return sharedPreferences.getString(EMAIL_KEY, "");
    }

    public static void saveName(String name) {

        sharedPreferences.edit().putString(NAME_KEY, name).apply();
    }

    public static String getName() {
        return sharedPreferences.getString(NAME_KEY, "");
    }

    public static void savePicture(String picture) {

        sharedPreferences.edit().putString(PICTURE_KEY, picture).apply();
    }

    public static String getPicture() {
        return sharedPreferences.getString(PICTURE_KEY, "");
    }
}
