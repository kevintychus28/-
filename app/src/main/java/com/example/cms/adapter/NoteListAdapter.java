package com.example.cms.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.activity.MainActivity;
import com.example.cms.entity.Cource;
import com.example.cms.entity.Note;
import com.example.cms.util.NoteService;

import java.util.List;


public class NoteListAdapter extends BaseAdapter {

    private static final String TAG = "Log日志";

    private List<Note> noteList;
    private MainActivity context;

    public NoteListAdapter(MainActivity context, List<Note> noteList) {
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
            holder.nDelete = (ImageView) convertView.findViewById(R.id.item_Delete);
            holder.nEdit = (ImageView) convertView.findViewById(R.id.item_edit);
            // 保存holder对象
            convertView.setTag(holder);
        } else {
            //重新获取holder
            holder = (ListViewHolder) convertView.getTag();
        }

        // 首先得到我们想要的对象
        Note note = noteList.get(position);

        // 设置内容
        holder.nID = note.getNote_id();
        holder.nTitle.setText(note.getTitle());
        holder.nContent.setText(note.getContent());

        // 删除按钮的点击事件监听
        holder.nDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了标题为 " + note.getTitle() + " 笔记的删除按钮，且note_ID为：" + note.getNote_id());
                context.showDeleteNoteDialog(note.getNote_id());
            }
        });
        // 编辑按钮的点击事件监听
        holder.nEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了标题为 " + note.getTitle() + " 笔记的修改按钮，且note_ID为：" + note.getNote_id());
                context.showEditNoteDialog(note.getNote_id(), note.getTitle(), note.getContent());
            }
        });
        return convertView;
    }

    class ListViewHolder {
        String nID;
        TextView nTitle;
        TextView nContent;
        ImageView nEdit;
        ImageView nDelete;
    }

}


