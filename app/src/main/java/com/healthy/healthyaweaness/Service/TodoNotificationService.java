package com.healthy.healthyaweaness.Service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.healthy.healthyaweaness.Activity.ReminderActivity;
import com.healthy.healthyaweaness.R;

import java.util.UUID;


public class TodoNotificationService extends IntentService {
    public static final String TODOTEXT = "com.avjindersekhon.todonotificationservicetext";
    public static final String TODOUUID = "com.avjindersekhon.todonotificationserviceuuid";
    private String mTodoText;
    private UUID mTodoUUID;
    private Context mContext;

    public TodoNotificationService(){
        super("TodoNotificationService");
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoUUID = (UUID)intent.getSerializableExtra(TODOUUID);
        Log.d("OskarSchindler", "onHandleIntent called");

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, ReminderActivity.class);
        i.putExtra(TodoNotificationService.TODOUUID, mTodoUUID);
        Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
        deleteIntent.putExtra(TODOUUID, mTodoUUID);
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notificationtext);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher_round);
        contentView.setTextViewText(R.id.text, mTodoText);

        Notification notification = null;
            notification = new Notification.Builder(this)
                    .setContentTitle(mTodoText)
                    .setSmallIcon(R.drawable.social_care)
//                    .setCustomContentView(contentView)
                    .setSmallIcon(R.drawable.social_care)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.social_care))
                    .setAutoCancel(true)
//                    .setContentText(mTodoText)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
//            notification.contentView=contentView;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";// The id
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), importance);
             notification = new Notification.Builder(this)
                    .setContentTitle(mTodoText)
                    .setSmallIcon(R.drawable.social_care)
                    .setAutoCancel(true)
                     .setChannelId(CHANNEL_ID)
//                     .setContentText(mTodoText)
//                     .setCustomContentView(contentView)
                     .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.social_care))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();

            manager.createNotificationChannel(mChannel);
        }
        manager.notify(100, notification);


    }
}
