package com.sora.gcdr.db.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.sora.gcdr.db.dao.CourseDao;
import com.sora.gcdr.db.entity.Course;
import com.sora.gcdr.db.CalendarDatabase;

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
        CalendarDatabase db = CalendarDatabase.getDatabase(context);
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
    public void clear(Course... courses) {
        dao.clearAllCourses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("sora", "清空全部..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sora", "添加失败了.." + e.getMessage());
                    }
                });
    }

}
