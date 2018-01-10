package com.example.deviceadminsample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by comp1 on 1/10/2018.
 */

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, MyService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            long interval=5000;
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pi);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
