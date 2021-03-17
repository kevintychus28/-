package com.example.cms.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.entity.Cource;
import com.example.cms.entity.Note;

import java.util.List;


public class NoteListAdapter extends BaseAdapter {

    private List<Note> noteList;
    private Context context;

    public NoteListAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
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
            convertView = View.inflate(context, R.layout.item_note, null);
            // 创建对象(里面是我们要得到里面的属性)
            holder = new ListViewHolder();
            // 找控件
            holder.nTitle = (TextView) convertView.findViewById(R.id.item_Title);
            holder.nContent = (TextView) convertView.findViewById(R.id.item_Content);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Note note = noteList.get(position);

        // 设置内容
        holder.nTitle.setText(note.getTitle());
        holder.nContent.setText(note.getContent());
        return convertView;
    }

    class ListViewHolder {
        TextView nTitle;
        TextView nContent;
    }

}


