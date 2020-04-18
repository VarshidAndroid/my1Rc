package com.rewardscards.a1rc.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.rewardscards.a1rc.MainActivity;
import com.rewardscards.a1rc.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        try {
       /*     JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            JSONObject data = jsonObject.getJSONObject("data");

            String title = data.getString("title");*/

            Log.e("Spider", "From: " + remoteMessage.getFrom() + " " );
            int notificationId = 1;
            String channelId = "channel-01";
            String channelName = "Channel Name";
            // Log.e("From: ",   jsonObject.getString("title"));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Test")
                    .setContentText("Test");

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            notificationManager.notify(notificationId, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

