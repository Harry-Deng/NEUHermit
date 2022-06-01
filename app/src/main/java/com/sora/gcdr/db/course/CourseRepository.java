package com.sora.gcdr.db.course;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.sora.gcdr.db.task.TaskDatabase;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseRepository {
    private static CourseRepository INSTANCE;
    private LiveData<List<Course>> courseListLive;
    private final CourseDao dao;

    private CourseRepository(Context context) {
        TaskDatabase db = TaskDatabase.getDatabase(context);
        this.dao = db.getCourseDao();
        this.courseListLive = dao.getAllCourses();
    }

    public LiveData<List<Course>> getCourseListLive() {
        return courseListLive;
    }

    public static CourseRepository getTaskRepository(Context context) {
        synchronized (CourseRepository.class) {
            if (INSTANCE == null) {
                INSTANCE = new CourseRepository(context);
            }
        }
        return INSTANCE;
    }
    public void insert(Course... courses) {
        dao.insertCourse(courses)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("sora", "插入成功..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sora", "添加失败了.." + e.getMessage());
                    }
                });
    }

}
