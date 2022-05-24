package com.sora.gcdr.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_task",
indices = {@Index("task_date")})
public class Task {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "task_date")
    long date;
    @ColumnInfo(name = "task_content")
    String content;

    public Task() {
    }

    @ColumnInfo(name = "task_done")
    boolean Done;

    @Ignore
    public Task(long date, String content, boolean done) {
        this.date = date;
        this.content = content;
        Done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return Done;
    }

    public void setDone(boolean done) {
        Done = done;
    }
}
