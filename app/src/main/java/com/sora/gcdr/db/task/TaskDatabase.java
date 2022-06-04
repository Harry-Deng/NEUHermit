package com.sora.gcdr.db.task;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sora.gcdr.db.course.Course;
import com.sora.gcdr.db.course.CourseDao;

@Database(entities = {Task.class, Course.class}, version = 4, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();
    public abstract CourseDao getCourseDao();
    private static TaskDatabase INSTANCE;

    public static TaskDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskDatabase.class,
                                    "word_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
