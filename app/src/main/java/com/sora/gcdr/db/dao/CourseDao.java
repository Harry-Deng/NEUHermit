package com.sora.gcdr.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.sora.gcdr.db.entity.Course;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface CourseDao {
    @Insert
    Completable insertCourse(Course... courses);

    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAllCourses();
}
