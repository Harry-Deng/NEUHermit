//package com.sora.gcdr.db.course;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//@Database(entities = {Course.class}, version = 1, exportSchema = false)
//public abstract class CourseDatabase extends RoomDatabase {
//    public abstract CourseDao getCourseDao();
//    private static CourseDatabase INSTANCE;
//
//    public static CourseDatabase getDatabase(Context context) {
//        if (INSTANCE == null) {
//            synchronized (CourseDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                                    CourseDatabase.class,
//                                    "word_database")
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}