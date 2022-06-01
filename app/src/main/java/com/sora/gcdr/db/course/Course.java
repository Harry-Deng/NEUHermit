package com.sora.gcdr.db.course;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "course")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String courseName;//课程名
    private String teacher;//老师
    private String location;//位置
    //无法保存？
    private String whichWeek;//哪周有课

    //课的坐标
    private int x;//周几的课
    private int y;//第几节课
    private int length;//课的长度

    @Ignore
    public Course() {

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getWhichWeek() {
        return whichWeek;
    }

    public void setWhichWeek(String whichWeek) {
        this.whichWeek = whichWeek;
    }

    public Course(String courseName, String teacher, String location, String whichWeek, int x, int y, int length) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.location = location;
        this.whichWeek = whichWeek;
        this.x = x;
        this.y = y;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 根据字符串 1-7，9
     * 解析 week 是否在范围内
     */
    public boolean contains(int week) {
        //1.用，分割
        String[] ranges = whichWeek.split(",");
        //2.判断 含有 '-'
        for (String range : ranges) {
            if (range.contains("-")) {
                //1.用'-'分割，得两个整数
                String[] split = range.split("-");
                if (week >= Integer.parseInt(split[0]) && week <= Integer.parseInt(split[1])) {
                    return true;
                }
            } else if (week == Integer.parseInt(range)) {
                return true;
            }
        }
        return false;
    }
}
