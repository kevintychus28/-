package com.example.cms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Course;
import com.example.cms.entity.Roster;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ExamTimeListAdapter extends BaseAdapter {

    private static final String TAG = "Log日志";
    private List<Course> examTimeList;
    private Context context;

    public ExamTimeListAdapter(Context context, List<Course> list) {
        this.context = context;
        this.examTimeList = list;
    }

    @Override
    public int getCount() {
        return examTimeList.size();
    }

    @Override
    public Object getItem(int position) {
        return examTimeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListViewHolder holder = null;
        if (convertView == null) {
            // 设置视图(创建一个laylout的xml)
            convertView = View.inflate(context, R.layout.item_exam, null);
            Log.e(TAG, "getView: 设置视图");
            // 创建对象(里面是我们要得到里面的属性)
            holder = new ListViewHolder();
            // 找控件
            holder.eName = (TextView) convertView.findViewById(R.id.exam_name);
            holder.eTime = (TextView) convertView.findViewById(R.id.exam_time);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Course course = examTimeList.get(position);

        // 设置名称
        holder.eName.setText(course.getCou_name());


        // 设置时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = dateFormat.parse(dateFormat.format(date));
            d2 = dateFormat.parse(course.getCou_exam_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() - d1.getTime();// 毫米时间差异
        long daf = diff / (1000 * 24 * 60 * 60);// 天数差异
        if (daf >= 0) {
            if (daf == 0) {
                holder.eTime.setText("今日考试");
                holder.eTime.setTextColor(Color.GREEN);
            } else {
                holder.eTime.setText("剩" + daf + "天");
                holder.eTime.setTextColor(Color.RED);
            }
        } else {
            holder.eTime.setText("考试已结束");
            holder.eTime.setTextColor(Color.GRAY);
        }


        return convertView;
    }

    class ListViewHolder {
        TextView eName;
        TextView eTime;
    }


}


