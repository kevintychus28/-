package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.R;
import com.example.cms.entity.Student;
import com.example.cms.entity.Teacher;
import com.example.cms.util.LoginService;
import com.example.cms.util.LoginServiceTec;

import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String userID, password;

    Student stu;
    Teacher tec;

    private Boolean is_stu = true;

    private RadioButton student;
    private RadioButton teacher;
    private EditText et_userID;
    private EditText et_password;
    private CheckBox cb_checkbox;
    private Button btn_login;

    private List<Student> stuData;

    private static final String TAG = "Log日志";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    //实例化
    private void initView() {
        //实例化控件
        student = findViewById(R.id.is_student);
        teacher = findViewById(R.id.is_teacher);
        et_userID = findViewById(R.id.et_userID);
        et_password = findViewById(R.id.et_password);
        cb_checkbox = findViewById(R.id.cb_checkbox);
        btn_login = findViewById(R.id.btn_login);
        //添加点击事件
        student.setOnClickListener(this);
        teacher.setOnClickListener(this);
        cb_checkbox.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_student:
                is_stu = true;
                break;
            case R.id.is_teacher:
                is_stu = false;
                break;
            case R.id.btn_login:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.e(TAG, "run: 开启登录线程");
                        userID = et_userID.getText().toString().trim();
                        password = et_password.getText().toString().trim();

                        //学生登录
                        if (is_stu) {
                            //发送登录请求
                            LoginService loginService = new LoginService();
                            String json = loginService.LoginService(userID, password);
                            List<Student> studentList = JSONObject.parseArray(json, Student.class);
                            if (studentList != null) {
                                Log.e(TAG, "studentList的数据为：" + studentList);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("is_stu", is_stu);
                                bundle.putString("userID", userID);
                                Log.e(TAG, userID);
                                bundle.putString("userName", studentList.get(0).getStu_name());
                                Log.e(TAG, studentList.get(0).getStu_name());
                                bundle.putString("userSex", studentList.get(0).getStu_sex());
                                bundle.putString("userDate", studentList.get(0).getStu_date());
                                bundle.putString("userClass", studentList.get(0).getStu_class());
                                bundle.putString("userCollege", studentList.get(0).getStu_college());
                                Intent i = new Intent();
                                i.putExtras(bundle);
                                i.setClass(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            } else {
                                Looper.prepare();
                                Toast toast = Toast.makeText(getApplicationContext(), "账号或密码错误，请重试", Toast.LENGTH_LONG);
                                toast.show();
                                Looper.loop();
                            }

                        }
                        //教师登录
                        else {
                            //发送登录请求
                            LoginServiceTec loginService = new LoginServiceTec();
                            String json = loginService.LoginService(userID, password);
                            List<Teacher> teacherList = JSONObject.parseArray(json, Teacher.class);
                            if (teacherList != null) {
                                Log.e(TAG, "teacherList：" + teacherList);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("is_stu", is_stu);
                                bundle.putString("userID", userID);
                                Log.e(TAG, userID);
                                bundle.putString("userName", teacherList.get(0).getTec_name());
                                Log.e(TAG, teacherList.get(0).getTec_name());
                                bundle.putString("userCollege", teacherList.get(0).getTec_department());
                                Intent i = new Intent();
                                i.putExtras(bundle);
                                i.setClass(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            } else {
                                Looper.prepare();
                                Toast toast = Toast.makeText(getApplicationContext(), "账号或密码错误，请重试", Toast.LENGTH_LONG);
                                toast.show();
                                Looper.loop();
                            }

                        }
                    }
                }.start();
                break;
        }
    }


}