package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.adapter.ScoreListAdapter;
import com.example.cms.entity.Cource;
import com.example.cms.fragment.HomePageFragment;
import com.example.cms.fragment.NotesFragment;
import com.example.cms.fragment.ScheduleFragment;
import com.example.cms.fragment.ScoreFragment;
import com.example.cms.util.ScheduleService;
import com.example.cms.util.ScoreService;
import com.mysql.cj.util.DnsSrv;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

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

//    在onCreate()之前你不能getIntent() – 那时根本就没有Intent可用.我相信任何需要Context的事情都是如此.
//    但是,您的匿名内部类仍然可以调用getIntent(),因此您根本不需要将其声明为变量.
//    userID = getIntent().getStringExtra("userID");


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
//        String userID = "17251102126";
        Log.d(TAG, "userID为：" + userID);
        getScheduleData(userID);
    }

    //实例化
    private void initView() {
        //四大功能页面
        fragment_schedule = findViewById(R.id.fragment_schedule);
        fragment_score_report = findViewById(R.id.fragment_score_report);
        fragment_notes = findViewById(R.id.fragment_notes);
        fragment_homepage = findViewById(R.id.fragment_homepage);
        //fragment页面
        main_body = findViewById(R.id.main_body);
        //底部导航栏
        bottom_bar_1 = findViewById(R.id.bottom_bar_1);
        bottom_bar_2 = findViewById(R.id.bottom_bar_2);
        bottom_bar_3 = findViewById(R.id.bottom_bar_3);
        bottom_bar_4 = findViewById(R.id.bottom_bar_4);
        //添加点击事件
        bottom_bar_1.setOnClickListener(this);
        bottom_bar_2.setOnClickListener(this);
        bottom_bar_3.setOnClickListener(this);
        bottom_bar_4.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_bar_1:
                setFragments(bottom_bar_1);
                break;
            case R.id.bottom_bar_2:
                setFragments(bottom_bar_2);
                getScore(getIntent().getStringExtra("userID"));
                break;
            case R.id.bottom_bar_3:
                setFragments(bottom_bar_3);
                break;
            case R.id.bottom_bar_4:
                setFragments(bottom_bar_4);
                break;
        }
    }

    //接受子线程的message
    private Handler scheduleHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 100:
                    setSchedule();
                    break;
            }
        }
    };
    //接受子线程的message
    private Handler scoreHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 100:
                    setScore();
                    break;
            }
        }
    };

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
                            Log.d(TAG, "--插入周" + (i+1) + "课程");
                        }
                    courseData[i] = list;
                    Log.d(TAG, "courseData[" + i + "]: " + courseData[i]);
                }
                Message msg_schedule = Message.obtain();
                msg_schedule.what = 100;
                scheduleHandler.sendMessage(msg_schedule);
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
            ll.addView(tv);
            Log.d(TAG, c.getCou_name());
            pre = c;
        }

    }

    /**
     * 更新课程表
     */
    public void setSchedule() {
        Log.i("Msg", "初始化面板");
        for (int i = 0; i < weekPanels.length; i++) {
            weekPanels[i] = (LinearLayout) findViewById(R.id.weekPanel_1 + i);
            Log.d(TAG, "--更新周" + (i+1) + "的课程");
            initWeekPanel(weekPanels[i], courseData[i]);
        }
    }

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
//                Log.d(TAG, "scoreList的第一个课程成绩：" + scoreList.get(0).getGrade());
//                Log.d(TAG, "scoreList[0]：" + scoreList.get(0).toString());
                scoreData = scoreList;
                Message msg_score = Message.obtain();
                msg_score.what = 100;
                scoreHandler.sendMessage(msg_score);
            }
        }.start();
    }


    public void setScore() {
        scoreListAdapter = new ScoreListAdapter(this, scoreData);
        scoreDetail = findViewById(R.id.scoreDetail);
        scoreDetail.setAdapter(scoreListAdapter);
    }


}
