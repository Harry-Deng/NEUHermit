package com.sora.gcdr.receiver;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import com.sora.gcdr.R;
import com.sora.gcdr.db.task.Task;
import com.sora.gcdr.db.task.TaskRepository;
import com.sora.gcdr.ui.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    Task task;

    //收到消息的回调方法
    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        task = intent.getParcelableExtra("task");
        if (task != null) {
            //同步到数据存储
            SharedPreferences shp = context.getSharedPreferences("alarm", MODE_PRIVATE);
            SharedPreferences.Editor edit = shp.edit();
            long triggerTime = shp.getLong(String.valueOf(task.getId()), 0);
            //验证一下没有从数据库删除
            if (triggerTime != 0) {
                task.setStatus(0);
                TaskRepository.getTaskRepository(context).update(task);
                edit.remove(String.valueOf(task.getId()));

                edit.apply();
                // Deliver the notification.
                deliverNotification(context);
            }
        }
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context) {
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, task.getId(), contentIntent,
                        //如果现在已经打开了这个Intent，那么将会更新数据之后再打开一个Intent
                        PendingIntent.FLAG_UPDATE_CURRENT);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_alarm_on_24)
                .setContentTitle("定的闹钟时间到了。。。")
                .setContentText(task.getContent())
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(task.getId(), builder.build());
    }
}
