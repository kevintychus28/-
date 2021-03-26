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

import java.util.List;


public class ScoreListAdapter extends BaseAdapter {

    private static final String TAG = "Log日志";
    private List<Roster> scoreList;
    private Context context;

    public ScoreListAdapter(Context context, List<Roster> list) {
        this.context = context;
        this.scoreList = list;
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
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
            convertView = View.inflate(context, R.layout.item_score, null);
            Log.e(TAG, "getView: 设置视图");
            // 创建对象(里面是我们要得到里面的属性)
            holder = new ListViewHolder();
            // 找控件
            holder.rName = (TextView) convertView.findViewById(R.id.item_Name);
            holder.rGrade = (TextView) convertView.findViewById(R.id.item_Grade);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Roster roster = scoreList.get(position);

        // 设置名称
        holder.rName.setText(roster.getName());
        // 设置成绩
        if (roster.getGrade().equals("101")) {
            holder.rGrade.setText("无成绩");
            holder.rGrade.setTextColor(Color.BLACK);
        } else holder.rGrade.setText(roster.getGrade());
        // 分数低于60分为红色
        if (Integer.parseInt(roster.getGrade()) < 60) {
            holder.rGrade.setTextColor(android.graphics.Color.RED);
        }


        return convertView;
    }

    class ListViewHolder {
        TextView rName;
        TextView rGrade;
    }
}


