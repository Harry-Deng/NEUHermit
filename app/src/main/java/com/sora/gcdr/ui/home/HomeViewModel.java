package com.sora.gcdr.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sora.gcdr.db.Task;
import com.sora.gcdr.db.TaskRepository;

import java.util.Calendar;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final TaskRepository repository;

    private int year;
    private int month;
    private int day;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
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


    public TaskRepository getRepository() {
        return repository;
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

    public String getDate() {
        return year + "-" + month + "-" + day;
    }
}
