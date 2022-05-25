package com.sora.gcdr.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.util.MyUtils;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository {
    private LiveData<List<Task>> dayTasksLive;
    private final TaskDao taskDao;

    public TaskRepository(Context context) {
        TaskDatabase db = TaskDatabase.getDatabase(context);
        this.taskDao = db.getTaskDao();
        this.dayTasksLive = taskDao.getTaskByTime(
                MyUtils.getCurrentDayMinTime(2022,5,25),
                MyUtils.getCurrentDayMaxTime(2022,5,25)
        );
    }

    public void setDayTasksLive(int year,int month,int day) {
        this.dayTasksLive = taskDao.getTaskByTime(
                MyUtils.getCurrentDayMinTime(year, month, day),
                MyUtils.getCurrentDayMaxTime(year, month, day)
        );
    }

    public LiveData<List<Task>> getDayTaskLive() {
        return dayTasksLive;
    }

    public void insert(Task... tasks) {
        taskDao.insertTask(tasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {
                        Toast.makeText(MyApplication.getInstance(), "插入成功。", Toast.LENGTH_SHORT).show();
                        Log.d("sora", "插入成功..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sora", "插入失败.."+e.getMessage());
                    }
                });
    }




}
