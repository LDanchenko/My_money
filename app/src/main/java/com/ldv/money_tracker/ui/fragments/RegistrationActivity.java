package com.ldv.money_tracker.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ldv.money_tracker.R;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import static com.ldv.money_tracker.utils.ConstansManager.LOGIN_BUSY;
import static com.ldv.money_tracker.utils.ConstansManager.REGISTRATION_SUCCESS;


@EActivity(R.layout.registration_form)
public class RegistrationActivity extends AppCompatActivity {


    @ViewById(R.id.button_registration)
    Button registration;

    @ViewById(R.id.enter_login)
    EditText login;

    @ViewById(R.id.enter_password)
    EditText password;



    @AfterViews
    void ready() {


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                String enter_login = login.getText().toString();
                String enter_password = password.getText().toString();
                if (enter_login.length() < 5 || enter_password.length() < 5) {
                    CharSequence text = (getString(R.string.short_login_or_password));
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                   // checkInternet();
                }
            }
        });
    }


  /*  public void showResult(String result) {
        if (result.equals(REGISTRATION_SUCCESS)) {
            Toast.makeText(getApplicationContext(), R.string.registration_success, Toast.LENGTH_SHORT).show();
            startLoginActivity();
        } else if (result.equals(LOGIN_BUSY)) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    public void UnknownError() { //неизвестная ошибка
        Toast.makeText(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
    }

    void startLoginActivity() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity_.class);
        startActivity(intent);
    }


    void checkInternet() {
        NetworkStatusChecker networkStatusChecker = new NetworkStatusChecker();
        boolean internet = networkStatusChecker.isNetworkAvailable(getApplicationContext());
        if (internet == false) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            registrationUser.Registration();
        }
    }*/

}

