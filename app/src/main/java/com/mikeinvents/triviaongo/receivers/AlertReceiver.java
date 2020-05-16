package com.mikeinvents.triviaongo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.mikeinvents.triviaongo.ui.NotificationHelper;

public class AlertReceiver extends BroadcastReceiver {
    public static NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
