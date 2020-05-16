package com.mikeinvents.triviaongo.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.mikeinvents.triviaongo.R;
import com.mikeinvents.triviaongo.ui.MainActivity;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String THEME_PREF_SWITCH = "dark_mode";
    public static final String CHANGE_NAME = "change_name";
    public static final String REMINDER = "set_reminder";
    public static final String FEEDBACK = "feedback";


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s); //set layout for settings page

        Preference darkMode = findPreference(THEME_PREF_SWITCH);
        Preference changeName = findPreference(CHANGE_NAME);
        Preference reminder = findPreference(REMINDER);
        Preference feedback = findPreference(FEEDBACK);

        //change theme
        darkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean darkMode = (Boolean) o;
                if(darkMode){
                    //set the theme for the restarted activity
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    Toast.makeText(getContext(), "Dark Mode Coming soon", Toast.LENGTH_SHORT).show();
                }else{
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(getContext(), "Light Mode in use", Toast.LENGTH_SHORT).show();
                }
                //Objects.requireNonNull(getActivity()).recreate();

                return true;
            }
        });

        //change Trivia Name
        changeName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(CHANGE_NAME,"CHANGE_NAME");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }
        });

        //set a reminder to take question
        reminder.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment timeFragment = new TimeFragment();
                timeFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        getString(R.string.time_picker));

                return true;
            }
        });

        //send feedback through mails
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"akindelemichael65@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Trivia-On-Go Feedback");
                intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.settings_send_feedback_summary));

                //verify if the intent will resolve to at least one activity
                PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(Intent.createChooser(intent,"Send feedback"));
                }
                return true;
            }
        });
    }
}
