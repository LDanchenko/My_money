package com.ldv.money_tracker.ui.fragments.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.ldv.money_tracker.R;


public class SettingsFragment extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                addPreferencesFromResource(R.xml.notifications_preferences);

        }
}