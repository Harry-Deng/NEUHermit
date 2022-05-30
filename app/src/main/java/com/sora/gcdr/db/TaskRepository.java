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
    private static TaskRepository INSTANCE;
    private LiveData<List<Task>> dayTasksLive;
    private final TaskDao taskDao;

    private TaskRepository(Context context) {
        TaskDatabase db = TaskDatabase.getDatabase(context);
        this.taskDao = db.getTaskDao();
        this.dayTasksLive = taskDao.getTaskByTime(0, 0);
    }

    public static TaskRepository getTaskRepository(Context context) {
        synchronized (TaskDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = new TaskRepository(context);
            }
        }
        return INSTANCE;
    }

    public void setDayTasksLive(int year, int month, int day) {
        this.dayTasksLive = taskDao.getTaskByTime(
                MyUtils.getCurrentDayMinTime(year, month, day),
                MyUtils.getCurrentDayMaxTime(year, month, day)
        );
    }

    public LiveData<List<Task>> getDayTaskLive() {
        return dayTasksLive;
    }


    public void update(Task... tasks) {
        taskDao.updateTask(tasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MyApplication.getInstance(), "更新成功", Toast.LENGTH_SHORT).show();
                        Log.d("sora", "更新成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sora", "更新失败.." + e.getMessage());
                    }
                });
    }

    public void delete(Task... tasks) {
        taskDao.deleteTask(tasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MyApplication.getInstance(), "删除成功", Toast.LENGTH_SHORT).show();
                        Log.d("sora", "插入成功..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sora", "插入失败.." + e.getMessage());
                    }
                });
    }


    public void insert(Task... tasks) {
        taskDao.insertTask(tasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MyApplication.getInstance(), "添加成功。", Toast.LENGTH_SHORT).show();
                        Log.d("sora", "插入成功..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sora", "添加失败了.." + e.getMessage());
                    }
                });
    }


}
