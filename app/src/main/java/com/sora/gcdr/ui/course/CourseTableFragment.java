package com.sora.gcdr.ui.course;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.FragmentCourseTableBinding;
import com.sora.gcdr.db.course.Course;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;

public class CourseTableFragment extends Fragment {

    private CourseTableViewModel mViewModel;
    private FragmentCourseTableBinding binding;
    ContentResolver resolver;
    CourseListAdapter adapter;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    try {
                        if (result != null) {
                            InputStream inputStream = resolver.openInputStream(result);
                            excelToCourse(inputStream);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

    public static CourseTableFragment newInstance() {
        return new CourseTableFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCourseTableBinding.inflate(inflater, container, false);
        binding.toolbar.setTitle("我的课表");
        binding.toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

        mViewModel = new ViewModelProvider(requireActivity()).get(CourseTableViewModel.class);

        adapter = new CourseListAdapter();
        binding.recyclerview.setLayoutManager(new GridLayoutManager(requireActivity(), 8));
        binding.recyclerview.setAdapter(adapter);

        binding.recyclerview.addItemDecoration(new GridDivider(requireActivity()));

        mViewModel.getCurrentWeek().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mViewModel.getCourseList().removeObservers(getViewLifecycleOwner());

                mViewModel.getCourseList().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        List<Course> courseList = CustomReSort(courses, mViewModel.currentWeek.getValue());
                        adapter.setCourseList(courseList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        resolver = requireActivity().getContentResolver();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_course, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_course_import:
                mGetContent.launch("*/*");
                break;
            case R.id.item_set_week:
                showList();
                break;
            case R.id.item_clear_course:
                mViewModel.clearCourses();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Course> CustomReSort(List<Course> courses, int week) {
        List<Course> courseList = Arrays.asList(new Course[96]);
        for (Course course : courses) {
            //只要第五周有课的
            if (course.contains(week)) {
                int loc = course.getX() + (course.getY() - 1) * 8;
                courseList.set(loc, course);
            }
        }
        return courseList;
    }

    private void showList() {
        //默认选中的item
        final String[] items = {
                "第一周", "第二周", "第三周", "第四周", "第五周", "第六周", "第七周",
                "第八周", "第九周", "第十周", "第十一周", "第十二周", "第十三周", "第十四周",
                "第十五周", "第十六周", "第十七周"
        };
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(requireActivity())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("选择周")
                .setCancelable(false)
                .setSingleChoiceItems(items, mViewModel.getCurrentWeek().getValue() - 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewModel.getCurrentWeek().setValue(i + 1);
                    }
                });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        adapter.setCourseList();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create().show();
    }

    public void excelToCourse(InputStream inputStream) {
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);

            List<Range> mergedCells = new ArrayList<>(Arrays.asList(sheet.getMergedCells()));
            // row: 4-15  col: 1 - 7
            Iterator<Range> rangeIterator = mergedCells.iterator();
            while (rangeIterator.hasNext()) {
                Range range = rangeIterator.next();
                Cell cell = range.getTopLeft();
                if (cell.getRow() > 15 || cell.getRow() < 4 || cell.getColumn() < 1 || cell.getColumn() > 7) {
                    rangeIterator.remove();
                }
            }

            for (Range mergedCell : mergedCells) {
                Cell mCell = mergedCell.getTopLeft();
                Cell rCell = mergedCell.getBottomRight();
                //解析内容
                String content = mCell.getContents();
                //所有括号和空格换成 #
                String replace1 = content
                        .replaceAll("\\(+", "#")
                        .replaceAll("\\)+", "#")
                        .replaceAll(" +", "#");


                //分行，一节课占两行
                String[] split = replace1.split("\\n");
                //每次走两行，添加一节课
                for (int i = 0; i < split.length; i += 2) {
                    Course course = new Course();
                    //周末
                    if (mCell.getColumn() == 1) {
                        course.setX(7);
                    } else {
                        course.setX(mCell.getColumn() - 1);
                    }
                    //第几节开始
                    course.setY(mCell.getRow() - 3);
                    //课长度
                    course.setLength(rCell.getRow() - mCell.getRow() + 1);
                    //课程第一行，三个属性
                    String row1 = split[i];
                    //把 # 去掉分割，再去空
                    List<String> fieldRow1 = new ArrayList<>(Arrays.asList(row1.split("#+")));
                    Iterator<String> rowIterator = fieldRow1.iterator();
                    while (rowIterator.hasNext()) {
                        String str = rowIterator.next();
                        if ("".equals(str)) {
                            rowIterator.remove();
                            continue;
                        }
                    }
                    if (fieldRow1.size() == 3) {
                        course.setCourseName(fieldRow1.get(0));
//                        course.setCode(fieldRow1.get(1));
                        course.setTeacher(fieldRow1.get(2));
                    }
                    if (fieldRow1.size() > 3) {
                        course.setCourseName(fieldRow1.get(0) + "(" + fieldRow1.get(1) + ")");
//                        course.setCode(fieldRow1.get(2));
                        course.setTeacher(fieldRow1.get(3));
                    }
                    //课程第二行，至少一个属性
                    String row2 = split[i + 1];
                    List<String> fieldRow2 = new ArrayList<>(Arrays.asList(row2.split("#+")));
                    rowIterator = fieldRow2.iterator();
                    while (rowIterator.hasNext()) {
                        String str = rowIterator.next();
                        if ("".equals(str)) {
                            rowIterator.remove();
                        }
                        try {
                            int val = Integer.parseInt(str);
                            if (val > 20)
                                rowIterator.remove();
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    switch (fieldRow2.size()) {
                        case 3:
                        case 2:
                            course.setLocation(fieldRow2.get(1));
                        case 1:
                            course.setWhichWeek(fieldRow2.get(0));
                    }
                    Log.d("shiro", "excelToCourse: " +
                            course.getX() + "," +
                            course.getY() + "," +
                            course.getLength() + "," +
                            course.getCourseName() + "," +
                            course.getTeacher() + "," +
                            course.getLocation() + "," +
                            course.getWhichWeek()
                    );
                    mViewModel.insert(course);
                }
            }
//            Log.d("shiro", "onClick: " + coursesList.size());
        } catch (Exception e) {
            Log.d("shiro", "onClick: 遇到错误");
        } finally {
            if (workbook != null)
                workbook.close();
        }
    }
}