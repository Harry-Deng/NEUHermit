package com.sora.gcdr.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sora.gcdr.db.task.Task;
import com.sora.gcdr.db.task.TaskRepository;

import java.util.List;

/**
 * ViewModel保存数据
 */
public class HomeViewModel extends AndroidViewModel {
    private final TaskRepository repository;

    private int year;
    private int month;
    private int day;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = TaskRepository.getTaskRepository(application);
    }

    public void addTask(Task task) {
        repository.insert(task);
    }

    public LiveData<List<Task>> getDayTaskLive() {
        return repository.getDayTaskLive();
    }
    public void updateDayTaskLive() {
        repository.setDayTasksLive(year,month,day);
    }


    public void delete(Task... tasks) {
        repository.delete(tasks);
    }

    public void update(Task... tasks) {
        repository.update(tasks);
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

}
