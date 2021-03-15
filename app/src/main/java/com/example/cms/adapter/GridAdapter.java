package com.example.cms.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Cource;

import java.util.List;


public class GridAdapter extends BaseAdapter {

    private List<Cource> courceList;
    private Context context;

    public GridAdapter(Context context, List<Cource> list) {
        this.context = context;
        this.courceList = list;
    }

    @Override
    public int getCount() {
        return courceList.size();
    }

    @Override
    public Object getItem(int position) {
        return courceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewHolder holder = null;
        if (convertView == null) {
            // 设置视图(创建一个laylout的xml)
            convertView = View.inflate(context,R.layout.cource_item_layout,null);
            // 创建对象(里面是我们要得到里面的属性)
            holder = new GridViewHolder();
            // 找控件
            holder.cName = (TextView) convertView.findViewById(R.id.itemName);
            holder.cClassroom = (TextView) convertView.findViewById(R.id.itemClassroom);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Cource cource = courceList.get(position);

        // 设置内容
        holder.cName.setText(cource.getcName());
        holder.cClassroom.setText(cource.getcClassroom());
        return convertView;
    }

}

class GridViewHolder {
    TextView cName;
    TextView cClassroom;
}
