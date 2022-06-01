package com.sora.gcdr.ui.course;


import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.sora.gcdr.databinding.CellCourseBinding;
import com.sora.gcdr.databinding.CourseDetailBinding;
import com.sora.gcdr.db.course.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
    private List<Course> courseList;

    public CourseListAdapter() {
        this.courseList =  new ArrayList<>(96);
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {

        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellCourseBinding binding = CellCourseBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (position % 16 == 0) {
            holder.binding.courseDesc.setText((position/8+1)+"\n\n\n"+(position/8+2));
        }

        if (position / 8 % 2 == 1) {
            holder.itemView.setVisibility(View.GONE);
            return;
        }
        if (courseList == null)
            return;
        Course course = courseList.get(position);
        if (course == null) {
//            holder.itemView.setVisibility(View.GONE);
            return;
        }
        holder.binding.courseDesc.append(course.getCourseName() + course.getLocation());
        holder.binding.courseDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                CourseDetailBinding binding = CourseDetailBinding.inflate(LayoutInflater.from(view.getContext()));
                binding.courseName.setText(course.getCourseName());
                binding.teacher.setText(course.getTeacher());
                binding.location.setText(course.getLocation());
                binding.whichWeek.setText(course.getWhichWeek());
                // Get the layout inflater
                builder.setView(binding.getRoot())
                        // Add action buttons
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...

                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (courseList == null)
            return 0;
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CellCourseBinding binding;

        public CourseViewHolder(@NonNull CellCourseBinding cellCourseBinding) {
            super(cellCourseBinding.getRoot());
            binding = cellCourseBinding;
        }
    }
}
