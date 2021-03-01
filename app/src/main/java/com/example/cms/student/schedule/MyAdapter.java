package com.example.cms.student.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;

import java.util.LinkedList;

public class MyAdapter extends BaseAdapter {

    private LinkedList<Cource> mData;
    private Context mContext;

    public MyAdapter(LinkedList<Cource> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.cource_item,parent,false);

        TextView cName = (TextView) convertView.findViewById(R.id.cName);
        TextView cClassroom = (TextView) convertView.findViewById(R.id.cClassroom);
        TextView cTeacher = (TextView) convertView.findViewById(R.id.cTeacher);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        Cource cource = mData.get(position);
        cName.setText(cource.getcName());
        cClassroom.setText(cource.getcClassroom());
        cTeacher.setText(cource.getcTeacher());

        return convertView;
    }
}
