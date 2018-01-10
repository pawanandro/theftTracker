package com.example.deviceadminsample;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Date;
import java.util.Timer;

public class MyService extends Service {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private Timer timer = new Timer();
    private static final long UPDATE_INTERVAL = 10000;
    private MyService actual=null;
    private int count=0;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                startJob();
            }
        });
        t.start();
        //startJob();
      return Service.START_STICKY;
    }

    private void startJob(){
        count++;
        //do job here
        if(count%2==0)
        {
            System.out.println("myservice running"+new Date());
            //setDataForSimpleNotification();
        }


        System.out.println("\n Count:"+count);
        //System.out.println("\n Serive update");
        //job completed. Rest for 5 second before doing another one
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //do job again
        startJob();
    }

    /*private void setDataForSimpleNotification() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("From Service")
                .setContentText("Service");
        sendNotification();
    }*/

    /*private void sendNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        if (count == Integer.MAX_VALUE - 1)
            count = 0;
        notificationManager.notify(count, notification);
    }*/
}
