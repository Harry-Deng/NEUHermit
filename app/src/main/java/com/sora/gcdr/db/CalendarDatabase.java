package com.sora.gcdr.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sora.gcdr.db.dao.TaskDao;
import com.sora.gcdr.db.entity.Course;
import com.sora.gcdr.db.dao.CourseDao;
import com.sora.gcdr.db.entity.Task;

@Database(entities = {Task.class, Course.class}, version = 2, exportSchema = false)
public abstract class CalendarDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();
    public abstract CourseDao getCourseDao();
    private static CalendarDatabase INSTANCE;

    public static CalendarDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (CalendarDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CalendarDatabase.class,
                                    "word_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
