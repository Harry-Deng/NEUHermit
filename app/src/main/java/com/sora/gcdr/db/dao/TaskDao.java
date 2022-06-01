package com.sora.gcdr.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sora.gcdr.db.entity.Task;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface TaskDao {
    @Insert
    Completable insertTask(Task... tasks);

    @Query("SELECT * FROM t_task WHERE task_date BETWEEN :from AND :to")
    LiveData<List<Task>> getTaskByTime(long from, long to);

    @Delete
    Completable deleteTask(Task... tasks);

    @Update
    Completable updateTask(Task... tasks);

    @Query("DELETE FROM T_TASK")
    void deleteAllTasks();
    
}
