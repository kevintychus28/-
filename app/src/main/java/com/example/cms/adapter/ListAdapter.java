package com.example.cms.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Score;

import java.util.List;


public class ListAdapter extends BaseAdapter {

    private List<Score> scoreList;
    private Context context;

    public ListAdapter(Context context, List<Score> list) {
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
            convertView = View.inflate(context,R.layout.score_item_layout,null);
            // 创建对象(里面是我们要得到里面的属性)
            holder = new ListViewHolder();
            // 找控件
            holder.cName = (TextView) convertView.findViewById(R.id.item_Name);
            holder.cScore = (TextView) convertView.findViewById(R.id.item_Score);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Score score = scoreList.get(position);

        // 设置内容
        holder.cName.setText(score.getcName());
        holder.cScore.setText(score.getcScore());
        return convertView;
    }

}

class ListViewHolder {
    TextView cName;
    TextView cScore;
}