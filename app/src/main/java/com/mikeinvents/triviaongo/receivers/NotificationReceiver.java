package com.mikeinvents.triviaongo.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent,0);
        alarmManager.cancel(pendingIntent);
        AlertReceiver.notificationHelper.getManager().cancel(1);
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }
}
