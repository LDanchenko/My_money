package com.ldv.money_tracker.ui.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ldv.money_tracker.MoneyTrackerApplication;
import com.ldv.money_tracker.R;
import com.ldv.money_tracker.rest.NetworkStatusChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
@EActivity(R.layout.login_form)
public class LoginActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{

    public SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;


    @AfterViews
    void ready() {


        SignIn = (SignInButton) findViewById(R.id.sign_in_button);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkCheker();
            }
        });

    }

        public void networkCheker (){

                NetworkStatusChecker networkStatusChecker = new NetworkStatusChecker();
                boolean internet = networkStatusChecker.isNetworkAvailable(getApplicationContext());
                if (internet == false) {
                    Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                } else {
                    signIn();
                }

        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void startMainActivity (){
        Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
        startActivity(intent);
    }

    private void signIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void handleResult (GoogleSignInResult result){

        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();

            MoneyTrackerApplication.saveEmail(account.getDisplayName());
            MoneyTrackerApplication.saveName(account.getEmail());
            MoneyTrackerApplication.savePicture(account.getPhotoUrl().toString());
            startMainActivity();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}