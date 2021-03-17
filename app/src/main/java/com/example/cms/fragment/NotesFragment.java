package com.example.cms.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cms.R;

import com.example.cms.util.NoteService;


public class NotesFragment extends Fragment {

    private static final String TAG = "Log日志";


    private ImageView iv_addNote;
    private ImageView iv_deleteNote;
    private String userID = "17251102126";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
//        userID = getActivity().getIntent().getStringExtra("userID");
        iv_addNote = view.findViewById(R.id.iv_addNote);
        iv_deleteNote = view.findViewById(R.id.iv_deleteNote);
        return view;
    }

    //接受子线程的message
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    showFailDialog();
                    break;
            }
        }
    };


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        iv_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(TAG, "点击增加笔记按钮");
                showAddNoteDialog();
            }
        });
        iv_deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
    }


    /**
     * 展示添加笔记的弹窗
     */
    public void showAddNoteDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
         * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
         * dialog_customize.xml可自定义更复杂的View
         */
        AlertDialog.Builder addNoteDialog = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_note, null);
        addNoteDialog.setTitle("添加笔记");
        addNoteDialog.setView(dialogView);
        addNoteDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        addNoteDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText et_addTitle = (EditText) dialogView.findViewById(R.id.et_addTitle);
                        EditText et_addContent = (EditText) dialogView.findViewById(R.id.et_addContent);
                        String title = et_addTitle.getText().toString();
                        String content = et_addContent.getText().toString();
                        Log.d(TAG, "userID: " + userID);
                        Log.d(TAG, "新笔记标题: " + title);
                        Log.d(TAG, "新笔记内容: " + content);
                        addNote(userID, title, content);
                    }
                });
        addNoteDialog.show();
    }

    /**
     * 增加笔记
     *
     * @param userID
     * @param title
     * @param content
     */
    public void addNote(String userID, String title, String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                if (noteService.addNote(userID, title, content)) {
                    Log.d(TAG, "笔记增加成功");
                } else {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 删除笔记
     *
     * @param userID
     * @param note_id
     */
    public void deleteNote(String userID, String note_id) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                if (noteService.deleteNote(userID, note_id)) {
                    Log.d(TAG, "笔记删除成功");
                } else {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void showFailDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("error");
        normalDialog.setMessage("操作失败，请重试?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
}