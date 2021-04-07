package com.example.cms.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.R;
import com.example.cms.activity.LoginActivity;
import com.example.cms.activity.MainActivity;
import com.example.cms.activity.MapActivity;
import com.example.cms.adapter.ExamTimeListAdapter;
import com.example.cms.adapter.ScoreListAdapter;

public class HomePageFragment extends Fragment {

    private static final String TAG = "Log日志";

    private MainActivity mContext;

    private RelativeLayout re_countdown;
    private RelativeLayout re_map;
    private RelativeLayout re_about;
    private RelativeLayout re_exit;

    ExamTimeListAdapter examTimeListAdapter;
    ListView examDetail;

    public HomePageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        Log.e(TAG, "更新个人信息");
        TextView hp_userName = (TextView) view.findViewById(R.id.hp_userName);
        TextView hp_userID = (TextView) view.findViewById(R.id.hp_userID);
        Log.e(TAG, mContext.getIntent().getStringExtra("userName") + mContext.getIntent().getStringExtra("userID"));
        hp_userName.setText(mContext.getIntent().getStringExtra("userName"));
        hp_userID.setText(mContext.getIntent().getStringExtra("userID"));
        Log.e(TAG, hp_userName.getText().toString() + hp_userID.getText().toString());
        initHomePage(view);
        return view;
    }

    public void initHomePage(View view) {
        re_countdown = view.findViewById(R.id.re_countdown);
        re_map = view.findViewById(R.id.re_map);
        re_about = view.findViewById(R.id.re_about);
        re_exit = view.findViewById(R.id.re_exit);
        // 考试倒计时点击事件
        re_countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examDialog();
            }
        });
        // 地图点击事件
        re_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapDialog();
            }
        });
        // 关于点击事件
        re_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"作者：叶添材。邮箱：1602709610@qq.com",Toast.LENGTH_LONG).show();
            }
        });
        // 退出登录点击事件
        re_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }


    // 展示考试倒计时弹窗
    public void examDialog() {
        // 装入自定义View ==> R.layout.dialog_note
        AlertDialog.Builder examDialog = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_exam, null);
        examTimeListAdapter = new ExamTimeListAdapter(mContext, mContext.getExamTimeList());
        examDetail = dialogView.findViewById(R.id.examDetail);
        examDetail.setAdapter(examTimeListAdapter);
        examDialog.setView(dialogView);
        examDialog.show();
    }


    // 地图
    private void showMapDialog() {
        Intent i = new Intent();
        i.setClass(mContext, MapActivity.class);
        startActivity(i);
    }

    // 退出登录弹窗
    private void showLogoutDialog() {
        /*  @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
        normalDialog.setMessage("确定退出吗？");
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logout = new Intent();
                        logout.setClass(mContext, LoginActivity.class);
                        startActivity(logout);
                        mContext.finish();
                    }
                });
        // 显示
        normalDialog.show();
    }

    // 解决getActivity()空指针问题
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) getActivity();
    }
}