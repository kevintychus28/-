package com.example.cms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Course;

import java.util.List;

public class GirdViewCourseAdapter extends BaseAdapter {

    private static final String TAG = "Log日志";
    private List<Course> courseList;
    private Context context;


    public GirdViewCourseAdapter(Context context, List<Course> list) {
        this.context = context;
        this.courseList = list;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GirdViewCourseAdapter.GridViewHolder holder = null;
        if (convertView == null) {
            // 设置视图(创建一个laylout的xml)
            convertView = View.inflate(context, R.layout.item_course, null);
            Log.e(TAG, "getView: 设置视图");
            // 创建对象(里面是我们要得到里面的属性)
            holder = new GirdViewCourseAdapter.GridViewHolder();
            // 找控件
//            holder.cou_id = (TextView) convertView.findViewById(R.id.cou_id);
            holder.cou_name = (TextView) convertView.findViewById(R.id.cou_name);
            holder.cou_teacher = (TextView) convertView.findViewById(R.id.cou_teacher);
            holder.cou_classroom = (TextView) convertView.findViewById(R.id.cou_classroom);
            holder.cou_weekday = (TextView) convertView.findViewById(R.id.cou_weekday);
            holder.cou_period = (TextView) convertView.findViewById(R.id.cou_period);
            holder.cou_exam_time = (TextView) convertView.findViewById(R.id.cou_exam_time);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (GirdViewCourseAdapter.GridViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Course course = courseList.get(position);

        // 设置属性
        holder.cou_name.setText(course.getCou_name());
        holder.cou_teacher.setText(course.getCou_teacher());
        holder.cou_classroom.setText(course.getCou_classroom());
        holder.cou_weekday.setText(getWeek(course.getCou_weekday()));
        holder.cou_period.setText("第" + course.getCou_period() + "节");
        holder.cou_exam_time.setText(course.getCou_exam_time());

        return convertView;
    }

    class GridViewHolder {
        TextView cou_name;
        TextView cou_teacher;
        TextView cou_classroom;
        TextView cou_weekday;
        TextView cou_period;
        TextView cou_exam_time;
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
