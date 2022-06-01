package com.sora.gcdr.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.sora.gcdr.databinding.ActivitySetAlarmBinding;
import com.sora.gcdr.db.entity.Task;
import com.sora.gcdr.db.repo.TaskRepository;
import com.sora.gcdr.receiver.NotificationReceiver;
import com.sora.gcdr.util.MyUtils;


/**
 * 闹钟的设置配合SharedPreferences 存储闹钟任务的key，可以通过这个key找到这个闹钟，从而可以实现取消，
 * 在删除任务时，要删除key，同时接收器receiver应该进行检查如果key没了则不应该触发闹钟
 */
public class SetAlarmActivity extends AppCompatActivity {

    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;

    Task task;
    ActivitySetAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //获取传过来的task和初始化数据
        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
        binding.textViewDate.setText(MyUtils.getDateByLong(task.getDate()));
        binding.textViewTime.setText(MyUtils.getTimeByLong(task.getDate()));
        binding.textContent.setText(task.getContent());

        if (task.getStatus() == 2) {
            binding.switchOpenAlarm.setChecked(true);
            binding.timeGroup.setVisibility(View.VISIBLE);
            binding.radioButton.setChecked(true);
        }

        //打开提醒，才能看到选项
        binding.switchOpenAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.timeGroup.setVisibility(View.VISIBLE);
                    //默认选择准时提醒
                    binding.radioButton2.setChecked(true);
                } else {
                    binding.timeGroup.setVisibility(View.GONE);
                }
            }
        });

        //点击取消，返回主页
        binding.buttonCancel.setOnClickListener(v -> finish());

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);





        binding.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setContent(binding.textContent.getText().toString());
                if (binding.switchOpenAlarm.isChecked()) {
                    Intent notifyIntent = new Intent(v.getContext(), NotificationReceiver.class);
                    task.setStatus(2);
                    notifyIntent.putExtra("task", task);

                    final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                            (v.getContext(), task.getId(), notifyIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    //同步到数据存储
                    SharedPreferences shp = getSharedPreferences("alarm", MODE_PRIVATE);
                    SharedPreferences.Editor edit = shp.edit();
                    TaskRepository.getTaskRepository(v.getContext()).update(task);

                    //把定的闹钟的时间写到xml文件
                    edit.putLong(String.valueOf(task.getId()), task.getDate());
                    edit.apply();
                    long triggerTime = task.getDate();
                    if (alarmManager != null) {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                triggerTime,
                                notifyPendingIntent);
                    }
                    createNotificationChannel();
                    Intent resIntent = new Intent();
                    resIntent.putExtra("task", task);
                    setResult(RESULT_OK, resIntent);
                } else {
                    Intent notifyIntent = new Intent(v.getContext(), NotificationReceiver.class);
                    task.setStatus(1);
                    notifyIntent.putExtra("task", task);

                    final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                            (v.getContext(), task.getId(), notifyIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    //同步到数据存储
                    SharedPreferences shp = getSharedPreferences("alarm", MODE_PRIVATE);
                    SharedPreferences.Editor edit = shp.edit();
                    TaskRepository.getTaskRepository(v.getContext()).update(task);


                    edit.remove(String.valueOf(task.getId()));
                    edit.apply();
                    task.setStatus(1);
                    TaskRepository.getTaskRepository(v.getContext()).update(task);
                    mNotificationManager.cancel(task.getId());
                    if (alarmManager != null) {
                        alarmManager.cancel(notifyPendingIntent);
                    }
                }
                finish();
            }
        });
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