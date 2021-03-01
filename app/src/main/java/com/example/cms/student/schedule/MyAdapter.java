package com.example.cms.student.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;

import java.util.List;


public class MyAdapter extends BaseAdapter {

    private List<Cource> courceList;
    private Context context;

    public MyAdapter(List<Cource> list, Context context) {
        this.courceList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cource_item, null);
        }
        TextView cName = (TextView) convertView.findViewById(R.id.cName);
        TextView cClassroom = (TextView) convertView.findViewById(R.id.cClassroom);
        TextView cTeacher = (TextView) convertView.findViewById(R.id.cTeacher);
        //如果有课,那么添加数据
        if( !getItem(position).equals("")) {
            Cource cource = courceList.get(position);
            cName.setText(cource.getcName());
            cClassroom.setText(cource.getcClassroom());
            cTeacher.setText(cource.getcTeacher());
        }
        return convertView;
    }
}
