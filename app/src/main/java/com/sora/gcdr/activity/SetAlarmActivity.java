package com.sora.gcdr.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.databinding.ActivitySetAlarmBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.db.TaskRepository;
import com.sora.gcdr.receiver.NotificationReceiver;
import com.sora.gcdr.util.MyUtils;

import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity {

    //通知相关参数
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotificationManager;


    Task task;
    boolean noSet;
    ActivitySetAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //获取传过来的task和初始化数据
        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
        binding.textViewDate.setText(MyUtils.getDateByLong(task.getDate()));
        binding.textViewTime.setText(MyUtils.getTimeByLong(task.getDate()));
        binding.textContent.setText(task.getContent());

        //根据ID读xml文件，确定是否已经开启了提醒
        SharedPreferences shp = getSharedPreferences("alarm", MODE_PRIVATE);
        noSet = shp.getLong(String.valueOf(task.getId()), 0) == 0;

//        //打开提醒，才能看到选项
//        binding.switchOpenAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    binding.timeGroup.setVisibility(View.VISIBLE);
//                    //默认选择准时提醒
//                    binding.radioButton2.setChecked(true);
//                } else {
//                    binding.timeGroup.setVisibility(View.GONE);
//                }
//            }
//        });

        //如果时间已经过去了，不显示提醒按钮
//        if (task.getStatus() == 0)
//            binding.switchOpenAlarm.setVisibility(View.GONE);
//        else if (noSet) {
//            //没有定过提醒
//            binding.switchOpenAlarm.setChecked(false);
//            binding.radioButton2.setChecked(true);
//        } else {
//            //已经定过提醒
//            binding.switchOpenAlarm.setChecked(true);
//            binding.radioButton.setChecked(true);
//        }
        //点击提交，由主页面开启提醒
        binding.buttonOK.setOnClickListener(this::onOpenNotificationOpenClicked);
        //点击取消，返回主页
        binding.buttonCancel.setOnClickListener(v -> finish());
    }

    private void onOpenNotificationOpenClicked(View v) {
        //闹钟将开启通知服务，警报计时管理服务
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //警报管理的目标接收器
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        //接收时把task传过去，修改它的状态
        notificationIntent.putExtra("task", task);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences shp = getSharedPreferences("alarm", MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        if (binding.switchOpenAlarm.isChecked()) {
            task.setStatus(2);
            TaskRepository.getTaskRepository(this).update(task);
            //把定的闹钟的时间写到xml文件
            edit.putLong(String.valueOf(task.getId()), task.getDate());
            edit.apply();

            long triggerTime = shp.getLong(String.valueOf(task.getId()), 0);
            //获取系统服务可能失败
            if (alarmManager != null) {
                //设置精确的不重复提醒
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        notifyPendingIntent
                );
            }
            createNotificationChannel();
        } else {
            edit.remove(String.valueOf(task.getId()));
            edit.apply();
            //取消闹钟
            mNotificationManager.cancelAll();
            alarmManager.cancel(notifyPendingIntent);
        }
        Intent resIntent = new Intent();
        resIntent.putExtra("task", task);
        setResult(RESULT_OK, resIntent);
        finish();
    }


    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Create the NotificationChannel with all the parameters.
        NotificationChannel notificationChannel = new NotificationChannel
                (PRIMARY_CHANNEL_ID,
                        "Stand up notification",
                        NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("Notifies every 15 minutes to " +
                "stand up and walk");
        mNotificationManager.createNotificationChannel(notificationChannel);

    }
}