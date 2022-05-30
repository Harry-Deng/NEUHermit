package com.sora.gcdr.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_task",
indices = {@Index("task_date")})
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "task_date")
    long date;

    @ColumnInfo(name = "task_content")
    String content;

    /**
     * 待办任务的状态, <br>
     * 0表示已结束 <br>
     * 1表示未完成,未开启提醒<br>
     * 2表示未完成,开启提醒<br>
     */
    @ColumnInfo(name = "task_done")
    int status;

    public Task() {
        status=1;
    }

    @Ignore
    public Task(long date, String content, int status) {
        this.date = date;
        this.content = content;
        this.status = status;
    }


    protected Task(Parcel in) {
        id = in.readInt();
        date = in.readLong();
        content = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(date);
        dest.writeString(content);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
