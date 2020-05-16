package com.mikeinvents.triviaongo.ui;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.mikeinvents.triviaongo.R;
import com.mikeinvents.triviaongo.receivers.NotificationReceiver;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel =
                new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(){
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, 0);

        Intent broadcastIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(getApplicationContext()
                ,0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Reminder")
                .setContentText("Time for some quiz!")
                .setContentIntent(contentIntent)
                .addAction(R.mipmap.ic_launcher_round,"CANCEL",actionIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this,(R.color.colorAccent)))
                .setSmallIcon(R.drawable.ic_alarm);
    }
}
