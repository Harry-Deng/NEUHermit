package com.sora.gcdr.db.task;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_task",
indices = {@Index("task_date")})
public class

Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "task_date")
    long date;

    @ColumnInfo(name = "task_slogan")
    String slogan;

    @ColumnInfo(name = "task_remark")
    String remark;

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
    public Task(long date, String slogan, String remark, int status) {
        this.date = date;
        this.slogan = slogan;
        this.remark = remark;
        this.status = status;
    }


    protected Task(Parcel in) {
        id = in.readInt();
        date = in.readLong();
        slogan = in.readString();
        remark = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(date);
        dest.writeString(slogan);
        dest.writeString(remark);
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = this.slogan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
