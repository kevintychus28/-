package com.example.cms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Roster;
import com.example.cms.entity.Student;

import java.util.List;

public class GirdViewStudentAdapter extends BaseAdapter {

    private static final String TAG = "Log日志";
    private List<Student> studentList;
    private Context context;

    public GirdViewStudentAdapter(Context context, List<Student> list) {
        this.context = context;
        this.studentList = list;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GirdViewStudentAdapter.GridViewHolder holder = null;
        if (convertView == null) {
            // 设置视图(创建一个laylout的xml)
            convertView = View.inflate(context, R.layout.item_student, null);
            Log.e(TAG, "getView: 设置视图");
            // 创建对象(里面是我们要得到里面的属性)
            holder = new GirdViewStudentAdapter.GridViewHolder();
            // 找控件
            holder.stu_name = (TextView) convertView.findViewById(R.id.stu_name);
            holder.stu_id = (TextView) convertView.findViewById(R.id.stu_id);
            holder.stu_sex = (TextView) convertView.findViewById(R.id.stu_sex);
            holder.stu_date = (TextView) convertView.findViewById(R.id.stu_date);
            holder.stu_class = (TextView) convertView.findViewById(R.id.stu_class);
            holder.stu_college = (TextView) convertView.findViewById(R.id.stu_college);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (GirdViewStudentAdapter.GridViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Student student = studentList.get(position);

        // 设置属性
        holder.stu_name.setText(student.getStu_name());
        holder.stu_id.setText(student.getStu_id());
        holder.stu_sex.setText(student.getStu_sex());
        holder.stu_date.setText(student.getStu_date());
        holder.stu_class.setText(student.getStu_class());
        holder.stu_college.setText(student.getStu_college());

        return convertView;
    }

    class GridViewHolder {
        TextView stu_id;
        TextView stu_name;
        TextView stu_sex;
        TextView stu_date;
        TextView stu_class;
        TextView stu_college;
    }
}
