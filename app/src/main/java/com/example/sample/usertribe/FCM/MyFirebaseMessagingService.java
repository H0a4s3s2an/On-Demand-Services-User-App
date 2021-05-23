package com.example.sample.usertribe.FCM;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.sample.usertribe.Activities.LoginActivity;
import com.example.sample.usertribe.Activities.PaymentActivity;
import com.example.sample.usertribe.Model.Billing;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    JSONObject p_id,p_img,p_price;
    private static final String TAG = "MyFirebaseMsgService";
    Utilities utils = new Utilities();

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
    if(remoteMessage.getNotification().getTitle().equals("Cancel"))
    {
        android.os.Handler handler=new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyFirebaseMessagingService.this, ""+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    else if(remoteMessage.getNotification().getTitle().equals("Arrived"))
    {
        showArrivedNotification(remoteMessage.getNotification().getBody());
    }
    else if(remoteMessage.getNotification().getTitle().equals("Booking_Cancelled"))
    {
        cancelBooking(remoteMessage.getNotification().getBody());
    }
    else if(remoteMessage.getNotification().getTitle().equals("Job_Started"))
    {
        JobStarted(remoteMessage.getNotification().getBody());
    }
    else if(remoteMessage.getNotification().getTitle().equals("Job_Finished"))
    {
        JobFinished(remoteMessage.getNotification().getBody());
    }
    else if(remoteMessage.getNotification().getTitle().equals("Busy"))
    {
        Busy(remoteMessage.getNotification().getBody());
    }
    else
    {
         Billing object=new Gson().fromJson(remoteMessage.getNotification().getBody(), Billing.class);
        Intent intent=new Intent(getBaseContext(), PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("providerId", object.getUid());
        intent.putExtra("provider_image", object.getImage());
        intent.putExtra("provider_price", object.getPrice());
        intent.putExtra("provider_name",object.getName());
        startActivity(intent);
    }
    }

    private void Busy(String body) {
        String SESSION_NOTIFY_CHANNEL="notifychannel";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, SESSION_NOTIFY_CHANNEL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Driver Busy")
                        .setContentText("Tap on")
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 1000, 1000})
                        .setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.error));

        Intent resultIntent = new Intent(this, LoginActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 1;

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void JobFinished(String body) {
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Job Finished")
                .setContentText(body)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    private void JobStarted(String body) {
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Job Stated")
                .setContentText(body)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    private void cancelBooking(String body) {
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Booking_Cancelled")
                .setContentText(body)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    private void showArrivedNotification(String body) {
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Arrived")
                .setContentText(body)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }
}
