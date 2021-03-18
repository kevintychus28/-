package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.R;
import com.example.cms.adapter.NoteListAdapter;
import com.example.cms.adapter.ScoreListAdapter;
import com.example.cms.entity.Cource;
import com.example.cms.entity.Note;
import com.example.cms.fragment.HomePageFragment;
import com.example.cms.fragment.NotesFragment;
import com.example.cms.fragment.ScheduleFragment;
import com.example.cms.fragment.ScoreFragment;
import com.example.cms.util.NoteService;
import com.example.cms.util.ScheduleService;
import com.example.cms.util.ScoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Log日志";

    private LinearLayout fragment_schedule;
    private LinearLayout fragment_score_report;
    private LinearLayout fragment_notes;
    private LinearLayout fragment_homepage;

    private FrameLayout main_body;

    private RelativeLayout bottom_bar_1;
    private RelativeLayout bottom_bar_2;
    private RelativeLayout bottom_bar_3;
    private RelativeLayout bottom_bar_4;

    private ScheduleFragment fragment_1;
    private ScoreFragment fragment_2;
    private NotesFragment fragment_3;
    private HomePageFragment fragment_4;

    private FragmentManager fManager;

    // 课程表
    int itemHeight;
    int marTop, marLeft;
    private List[] courseData = new ArrayList[7];
    private LinearLayout[] weekPanels = new LinearLayout[7];

    // 成绩表
    private ScoreListAdapter scoreListAdapter;
    private List<Cource> scoreData;
    private ListView scoreDetail;

    // 笔记
    private NoteListAdapter noteListAdapter;
    private List<Note> noteData = new ArrayList<>();
    private ListView noteDetail;

    // 个人主页
    private TextView hp_userName;
    private TextView hp_userID;

//    在onCreate()之前你不能getIntent() – 那时根本就没有Intent可用.我相信任何需要Context的事情都是如此.
//    但是,您的匿名内部类仍然可以调用getIntent(),因此您根本不需要将其声明为变量.
//    userID = getIntent().getStringExtra("userID");

//    String userID = "17251102126";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        //初始化视图
        initView();
        //加载课程表
        fManager = getFragmentManager();
        setFragments(bottom_bar_1);
        String userID = getIntent().getStringExtra("userID");
//        String userName = getIntent().getStringExtra("userName");
        getScheduleData(userID);
    }

    //实例化
    private void initView() {
        //承载四大功能的主页面
        main_body = findViewById(R.id.main_body);
        //底部导航栏
        bottom_bar_1 = findViewById(R.id.bottom_bar_1);
        bottom_bar_2 = findViewById(R.id.bottom_bar_2);
        bottom_bar_3 = findViewById(R.id.bottom_bar_3);
        bottom_bar_4 = findViewById(R.id.bottom_bar_4);
        //四大功能页面
        fragment_schedule = findViewById(R.id.fragment_schedule);
        fragment_score_report = findViewById(R.id.fragment_score_report);
        fragment_notes = findViewById(R.id.fragment_notes);
        fragment_homepage = findViewById(R.id.fragment_homepage);
        //添加点击事件
        bottom_bar_1.setOnClickListener(this);
        bottom_bar_2.setOnClickListener(this);
        bottom_bar_3.setOnClickListener(this);
        bottom_bar_4.setOnClickListener(this);
    }


    public String getUserID() {
        return getIntent().getStringExtra("userID");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_bar_1:
                setFragments(bottom_bar_1);
                getScheduleData(getUserID());
//                getScheduleData(userID);
                break;
            case R.id.bottom_bar_2:
                setFragments(bottom_bar_2);
                getScore(getUserID());
//                getScore(userID);
                break;
            case R.id.bottom_bar_3:
                setFragments(bottom_bar_3);
                getNote(getUserID());
//                getNote(userID);
                break;
            case R.id.bottom_bar_4:
                setFragments(bottom_bar_4);
                break;
        }
    }

    //接受子线程的message
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 000:
                    // 展示失败弹窗
                    showFailDialog();
                    break;
                case 100:
                    // 显示课程表
                    setSchedule();
                    break;
                case 200:
                    // 显示成绩表
                    setScore();
                    break;
                case 300:
                    // 显示笔记
                    setNote();
                    break;
                case 301:
                    // 获取笔记内容
                    getNote(getUserID());
//                    getNote(userID);
                    break;
            }
        }
    };

    /**
     * 展示失败重新弹窗
     */
    private void showFailDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("error");
        normalDialog.setMessage("操作失败，请重试！");
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

    /**
     * 设置四大功能区域
     *
     * @param v
     */
    private void setFragments(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideFragments(fTransaction);
        switch (v.getId()) {
            case R.id.bottom_bar_1:
                if (fragment_1 == null) {
                    fragment_1 = new ScheduleFragment();
                    fTransaction.add(R.id.main_body, fragment_1);
                } else {
                    fTransaction.show(fragment_1);
                }
                break;
            case R.id.bottom_bar_2:
                if (fragment_2 == null) {
                    fragment_2 = new ScoreFragment();
                    fTransaction.add(R.id.main_body, fragment_2);
                } else {
                    fTransaction.show(fragment_2);
                }
                break;
            case R.id.bottom_bar_3:
                if (fragment_3 == null) {
                    fragment_3 = new NotesFragment();
                    fTransaction.add(R.id.main_body, fragment_3);
                } else {
                    fTransaction.show(fragment_3);
                }
                break;
            case R.id.bottom_bar_4:
                if (fragment_4 == null) {
                    fragment_4 = new HomePageFragment();
                    fTransaction.add(R.id.main_body, fragment_4);
                } else {
                    fTransaction.show(fragment_4);
                }
                break;
        }
        fTransaction.commit();
    }

    /**
     * 隐藏功能区域
     *
     * @param fTransaction
     */
    private void hideFragments(FragmentTransaction fTransaction) {
        if (fragment_1 != null) {
            fTransaction.hide(fragment_1);
        }
        if (fragment_2 != null) {
            fTransaction.hide(fragment_2);
        }
        if (fragment_3 != null) {
            fTransaction.hide(fragment_3);
        }
        if (fragment_4 != null) {
            fTransaction.hide(fragment_4);
        }
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 课程表 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取每日的课程
     *
     * @param userID
     */
    public void getScheduleData(String userID) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ScheduleService scheduleService = new ScheduleService();
                String json = scheduleService.getSchedule(userID);
                Log.d(TAG, "课程JSON数据: " + json);
                // 把json数据转换为List
                List<Cource> scheduleList = JSONObject.parseArray(json, Cource.class);
//                Log.d(TAG, "scheduleList第一条数据：" + scheduleList.get(1).toString());
                for (int i = 0; i < 7; i++) {
                    List<Cource> list = new ArrayList<Cource>();
                    for (int j = 0; j < scheduleList.size(); j++)
                        if (scheduleList.get(j).getCou_weekday().equals(Integer.toString(i + 1))) {
                            list.add(scheduleList.get(j));
                            Log.d(TAG, "--插入周" + (i + 1) + "课程");
                        }
                    courseData[i] = list;
                    Log.d(TAG, "courseData[" + i + "]: " + courseData[i]);
                }
                Message msg = Message.obtain();
                msg.what = 100;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 插入当天课程
     *
     * @param ll
     * @param data
     */
    public void initWeekPanel(LinearLayout ll, List<Cource> data) {

        if (ll == null || data == null || data.size() < 1) return;
        ll.removeAllViews();
        itemHeight = getResources().getDimensionPixelSize(R.dimen.weekItemHeight);
        marTop = getResources().getDimensionPixelSize(R.dimen.weekItemMarTop);
        marLeft = getResources().getDimensionPixelSize(R.dimen.weekItemMarLeft);
        Cource pre = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            Cource c = data.get(i);
            int cPeriod = Integer.parseInt(c.getCou_period());
            int prePeriod = Integer.parseInt(pre.getCou_period());
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
            if (i > 0) {
                lp.setMargins(marLeft, ((cPeriod - prePeriod - 1) * (itemHeight + marTop) + marTop), 0, 0);
            } else {
                lp.setMargins(marLeft, (cPeriod - 1) * (itemHeight + marTop) + marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.TOP);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(12);
            tv.setTextColor(getResources().getColor(R.color.courseTextColor));
            tv.setText(c.getCou_name() + "\n" + c.getCou_classroom() + "\n" + c.getCou_teacher());
            //tv.setBackgroundColor(getResources().getColor(R.color.classIndex));
            tv.setBackground(getResources().getDrawable(R.drawable.course_shape));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCourceDialog(c);
                }
            });
            ll.addView(tv);
            Log.d(TAG, c.getCou_name());
            pre = c;
        }

    }

    /**
     * 展示单个课程的信息
     */
    public void showCourceDialog(Cource cource) {
        AlertDialog.Builder deleteNoteDialog = new AlertDialog.Builder(MainActivity.this);
        deleteNoteDialog.setTitle(cource.getCou_name());
        deleteNoteDialog.setMessage("教室：" + cource.getCou_classroom() + "\n" + "教师：" + cource.getCou_teacher());
        deleteNoteDialog.show();
    }

    /**
     * 更新课程表
     */
    public void setSchedule() {
        Log.i("Msg", "初始化面板");
        for (int i = 0; i < weekPanels.length; i++) {
            weekPanels[i] = (LinearLayout) findViewById(R.id.weekPanel_1 + i);
            Log.d(TAG, "--更新周" + (i + 1) + "的课程");
            initWeekPanel(weekPanels[i], courseData[i]);
        }
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 成绩表 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取成绩
     *
     * @param userID
     */
    public void getScore(String userID) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ScoreService scoreService = new ScoreService();
                String json = scoreService.getScore(userID);
                Log.d(TAG, "成绩JSON数据: " + json);
                // 把json数据转换为List
                List<Cource> scoreList = JSONObject.parseArray(json, Cource.class);
                scoreData = new ArrayList<>();
                for (int i = 0; i < scoreList.size(); i++) {
                    if (!scoreList.get(i).getGrade().equals("")) {
                        scoreData.add(scoreList.get(i));
                    }
                }
                Message msg = Message.obtain();
                msg.what = 200;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 更新成绩
     */
    public void setScore() {
        scoreListAdapter = new ScoreListAdapter(this, scoreData);
        scoreDetail = findViewById(R.id.scoreDetail);
        scoreDetail.setAdapter(scoreListAdapter);
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 笔记表 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取笔记
     *
     * @param userID
     */
    public void getNote(String userID) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                String json = noteService.getNote(userID);
                Log.d(TAG, "笔记JSON数据: " + json);
                // 把json数据转换为List
                noteData = JSONObject.parseArray(json, Note.class);
                Message msg = Message.obtain();
                msg.what = 300;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 更新笔记
     */
    public void setNote() {
        noteListAdapter = new NoteListAdapter(MainActivity.this, noteData);
        noteDetail = findViewById(R.id.notesDetail);
        noteDetail.setAdapter(noteListAdapter);
        Log.d(TAG, "更新笔记");
    }

    /**
     * 展示添加笔记的弹窗
     */
    public void showAddNoteDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_note
         */
        AlertDialog.Builder addNoteDialog = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_note, null);
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
                        EditText et_addTitle = (EditText) dialogView.findViewById(R.id.et_title);
                        EditText et_addContent = (EditText) dialogView.findViewById(R.id.et_content);
                        String title = et_addTitle.getText().toString();
                        String content = et_addContent.getText().toString();
                        Log.d(TAG, "userID: " + getUserID());
                        Log.d(TAG, "新笔记标题: " + title);
                        Log.d(TAG, "新笔记内容: " + content);
                        addNote(getUserID(), title, content);
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
                    Message msg = Message.obtain();
                    msg.what = 301;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 展示删除笔记的弹窗
     */
    public void showDeleteNoteDialog(String note_id) {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
         * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
         * dialog_customize.xml可自定义更复杂的View
         */
        AlertDialog.Builder deleteNoteDialog = new AlertDialog.Builder(MainActivity.this);
        deleteNoteDialog.setTitle("删除笔记");
        deleteNoteDialog.setMessage("是否确定删除此笔记？");
        deleteNoteDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        deleteNoteDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        deleteNote(getUserID(), note_id);
                    }
                });
        deleteNoteDialog.show();
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
                    Message msg = Message.obtain();
                    msg.what = 301;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 展示修改笔记的弹窗
     */
    public void showEditNoteDialog(String note_id, String title, String content) {
        /* @setView 装入自定义View ==> R.layout.dialog_note
         */
        AlertDialog.Builder editNoteDialog = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_note, null);
        EditText et_editTitle = (EditText) dialogView.findViewById(R.id.et_title);
        EditText et_editContent = (EditText) dialogView.findViewById(R.id.et_content);
        editNoteDialog.setTitle("修改笔记");
        editNoteDialog.setView(dialogView);
        et_editTitle.setText(title);
        et_editContent.setText(content);
        // 输入框光标定位到末尾字符
        CharSequence textTitle = et_editTitle.getText();
        if (textTitle instanceof Spannable) {
            Spannable spanText = (Spannable) textTitle;
            Selection.setSelection(spanText, textTitle.length());
        }
        editNoteDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        editNoteDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        String title = et_editTitle.getText().toString();
                        String content = et_editContent.getText().toString();
                        Log.d(TAG, "userID: " + getUserID());
                        Log.d(TAG, "修改后笔记标题: " + title);
                        Log.d(TAG, "修改后笔记内容: " + content);
                        editNote(getUserID(), note_id, title, content);
                    }
                });
        editNoteDialog.show();
    }

    /**
     * 修改笔记
     *
     * @param userID
     * @param title
     * @param content
     */
    public void editNote(String userID, String note_id, String title, String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                if (noteService.editNote(userID, note_id, title, content)) {
                    Log.d(TAG, "笔记修改成功");
                    Message msg = Message.obtain();
                    msg.what = 301;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 我的页面 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>




}