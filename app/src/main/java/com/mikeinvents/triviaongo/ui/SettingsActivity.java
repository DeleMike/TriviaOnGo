package com.mikeinvents.triviaongo.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mikeinvents.triviaongo.fragments.SettingsFragment;
import com.mikeinvents.triviaongo.receivers.AlertReceiver;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    public static final String THEME_PREF_SWITCH = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //display fragment
        getSupportFragmentManager().beginTransaction().
                replace(android.R.id.content,new SettingsFragment())
                .commit();

//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean darkMode = sharedPref.getBoolean(THEME_PREF_SWITCH,false);
//
//        if(darkMode){
//            Toast.makeText(this, "Dark Mode Coming Soon", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "This is the Light Mode", Toast.LENGTH_SHORT).show();
//        }
//
//        Preference mm =

    }

    public void processTimeSelected(int hourOfDay, int minute) {
        //get time values
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        startAlarm(calendar);

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
    }

    private void startAlarm(@NonNull Calendar calendar){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        //if the alarm time set has past already, we set it to the next day
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
