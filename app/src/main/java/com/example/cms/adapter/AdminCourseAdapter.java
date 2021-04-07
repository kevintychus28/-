package com.example.cms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cms.R;
import com.example.cms.activity.AdminActivity;
import com.example.cms.entity.Course;

import java.util.List;

public class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.ViewHolder> {

    private static final String TAG = "Log日志";
    private List<Course> courseList;
    private AdminActivity mContext;


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cou_name;
        TextView cou_teacher;
        TextView cou_classroom;
        TextView cou_weekday;
        TextView cou_period;
        TextView cou_exam_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cou_name = (TextView) itemView.findViewById(R.id.cou_name);
            cou_teacher = (TextView) itemView.findViewById(R.id.cou_teacher);
            cou_classroom = (TextView) itemView.findViewById(R.id.cou_classroom);
            cou_weekday = (TextView) itemView.findViewById(R.id.cou_weekday);
            cou_period = (TextView) itemView.findViewById(R.id.cou_period);
            cou_exam_time = (TextView) itemView.findViewById(R.id.cou_exam_time);
        }
    }

    public AdminCourseAdapter(AdminActivity mContext, List<Course> list) {
        this.mContext = mContext;
        this.courseList = list;
    }

    /**
     * 用于创建ViewHolder实例,并把加载的布局传入到构造函数去,再把ViewHolder实例返回
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public AdminCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * 用于对子项的数据进行赋值,会在每个子项被滚动到屏幕内时执行
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AdminCourseAdapter.ViewHolder holder, int position) {
        // 首先得到我们想要的对象
        Course course = courseList.get(position);
        // 设置属性
        holder.cou_name.setText(course.getCou_name());
        holder.cou_teacher.setText(course.getCou_teacher());
        holder.cou_classroom.setText(course.getCou_classroom());
        holder.cou_weekday.setText(getWeek(course.getCou_weekday()));
        holder.cou_period.setText("第" + course.getCou_period() + "节");
        holder.cou_exam_time.setText(course.getCou_exam_time());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.showCourseDialog(position, "edit");
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    private String getWeek(String weekday) {
        String week;
        switch (weekday) {
            case "1":
                week = "周一";
                break;
            case "2":
                week = "周二";
                break;
            case "3":
                week = "周三";
                break;
            case "4":
                week = "周四";
                break;
            case "5":
                week = "周五";
                break;
            case "6":
                week = "周六";
                break;
            case "7":
                week = "周日";
                break;
            default:
                week = "error";
                break;
        }
        return week;
    }


}
