package com.example.cms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Student;
import com.example.cms.entity.Teacher;

import java.util.List;

public class GirdViewTeacherAdapter extends BaseAdapter {

    private static final String TAG = "Log日志";
    private List<Teacher> teacherList;
    private Context context;


    public GirdViewTeacherAdapter(Context context, List<Teacher> list) {
        this.context = context;
        this.teacherList = list;
    }

    @Override
    public int getCount() {
        return teacherList.size();
    }

    @Override
    public Object getItem(int position) {
        return teacherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GirdViewTeacherAdapter.GridViewHolder holder = null;
        if (convertView == null) {
            // 设置视图(创建一个laylout的xml)
            convertView = View.inflate(context, R.layout.item_teacher, null);
            Log.e(TAG, "getView: 设置视图");
            // 创建对象(里面是我们要得到里面的属性)
            holder = new GirdViewTeacherAdapter.GridViewHolder();
            // 找控件
            holder.tec_id = (TextView) convertView.findViewById(R.id.tec_id);
            holder.tec_password = (TextView) convertView.findViewById(R.id.tec_password);
            holder.tec_name = (TextView) convertView.findViewById(R.id.tec_name);
            holder.tec_department = (TextView) convertView.findViewById(R.id.tec_department);

            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (GirdViewTeacherAdapter.GridViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Teacher teacher = teacherList.get(position);

        // 设置属性
        holder.tec_id.setText(teacher.getTec_id());
        holder.tec_password.setText(teacher.getTec_password());
        holder.tec_name.setText(teacher.getTec_name());
        holder.tec_department.setText(teacher.getTec_department());

        return convertView;
    }

    class GridViewHolder {
        TextView tec_id;
        TextView tec_password;
        TextView tec_name;
        TextView tec_department;
    }



}
