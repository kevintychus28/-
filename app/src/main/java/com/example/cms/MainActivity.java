package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cms.homepage.HomePageFragment;
import com.example.cms.notes.NotesFragment;
import com.example.cms.schedule.ScheduleFragment;
import com.example.cms.scorereport.ScoreFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout fragment_schedule;
    private LinearLayout fragment_score_report;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fManager = getFragmentManager();
        setFragments(bottom_bar_1);

    }

    //实例化
    private void initView() {
        //四大功能页面
        fragment_schedule = findViewById(R.id.fragment_schedule);
        fragment_score_report = findViewById(R.id.fragment_score_report);
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
                break;
            case R.id.bottom_bar_3:
                setFragments(bottom_bar_3);
                break;
            case R.id.bottom_bar_4:
                setFragments(bottom_bar_4);
                break;
        }
    }

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
}
