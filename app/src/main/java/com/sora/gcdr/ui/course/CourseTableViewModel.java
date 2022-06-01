package com.sora.gcdr.ui.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sora.gcdr.db.course.Course;
import com.sora.gcdr.db.course.CourseRepository;

import java.util.List;

public class CourseTableViewModel extends AndroidViewModel {
    MutableLiveData<Integer> currentWeek;
    CourseRepository repository;



    public CourseTableViewModel(@NonNull Application application) {
        super(application);
        repository = CourseRepository.getTaskRepository(application);
        currentWeek = new MutableLiveData<>();
        currentWeek.setValue(1);
    }

    public MutableLiveData<Integer> getCurrentWeek() {
        return currentWeek;
    }

    public LiveData<List<Course>> getCourseList() {
        return repository.getCourseListLive();
    }

    public void insert(Course course) {
        repository.insert(course);
    }

    public void clearCourses() {
        repository.clear();
    }
}