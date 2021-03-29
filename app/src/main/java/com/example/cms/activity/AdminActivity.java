package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.R;
import com.example.cms.adapter.GirdViewStudentAdapter;
import com.example.cms.entity.Course;
import com.example.cms.entity.Student;
import com.example.cms.util.AdminService;

import java.util.List;

public class AdminActivity extends AppCompatActivity {


    private AdminService adminService;
    private List<Student> studentList;
    private GirdViewStudentAdapter GirdViewStudentAdapter;
    private GridView gridViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getStudent();
    }

    public void getStudent() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                adminService = new AdminService();
                String json = adminService.getStudent();
                studentList = JSONObject.parseArray(json, Student.class);
                Message msg = Message.obtain();
                msg.what = 100;
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    public void setStudent() {
        GirdViewStudentAdapter = new GirdViewStudentAdapter(this, studentList);
        gridViewDetail = findViewById(R.id.gridViewDetail);
        gridViewDetail.setAdapter(GirdViewStudentAdapter);
    }


    //接受子线程的message
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 000:
                    break;
                case 100:
                    setStudent();
                    break;
            }
        }
    };

}