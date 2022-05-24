package com.sora.gcdr.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sora.gcdr.db.Task;
import com.sora.gcdr.db.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void addTask(Task task) {
        repository.insert(task);
    }

    public LiveData<List<Task>> getDayTaskLive() {
        return repository.getDayTaskLive();
    }
}
