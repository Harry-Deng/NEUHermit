package com.sora.gcdr.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.sora.gcdr.R;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.db.TaskRepository;
import com.sora.gcdr.ui.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    //与MainActivity中相同的消息和消息通知的  标识符
    private NotificationManager notifyManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    Task task;

    //收到消息的回调方法
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("shiro", "onReceive: 接收了消息");
        //获取设置了通知的待办
        task = intent.getParcelableExtra("task");
        //通知时间到了，更改状态为1，待办未完成+未设置提醒
        task.setStatus(1);
        //同步数据库和xml文件
        SharedPreferences shp = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.remove(String.valueOf(task.getId()));
        edit.apply();
        TaskRepository.getTaskRepository(context).update(task);

        //开启这段代码将会在通知时启动一个页面
//        Intent resIntent = new Intent(context, MainActivity.class);
//        resIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(resIntent);

        deliverNotification(context);


    }

    private void deliverNotification(Context context) {
        //从系统服务中获取通知管理器
        notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //点击通知的事件
        //创建一个 目的
        Intent contentIntent = new Intent(context, MainActivity.class);
        //创建一个 待办的目的，
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        //构造一个通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                //通知的频道ID
                (context, PRIMARY_CHANNEL_ID)
                //通知的图标，必选，因为要显示在通知条上，其余属性为可选
                .setSmallIcon(R.drawable.ic_baseline_alarm_on_24)
                //通知的标题
                .setContentTitle("时间到了")
                //通知的内容
                .setContentText("差不多是时候了")
                //点击通知后打开刚才创建的待办目的
                .setContentIntent(pendingIntent)
                //通知的优先级
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //点击后自动取消
                .setAutoCancel(true)
                //其余设置为默认
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        //向通道发生通知
        notifyManager.notify(NOTIFICATION_ID, builder.build());
    }
}
