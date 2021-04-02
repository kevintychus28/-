package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String userID, password;

    //区分学生和老师
    private Boolean is_stu = true;
    //管理员
    int flag = 0;

    private RadioButton student;
    private RadioButton teacher;
    private EditText et_userID;
    private EditText et_password;
    private CheckBox cb_checkbox;
    private Button btn_login;

    //实现记住密码需要用到SharePreferences
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String TAG = "Log日志";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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
        if (sp.getBoolean("checkboxBoolean", false)) {
            et_userID.setText(sp.getString("userID", ""));
            et_password.setText(sp.getString("password", ""));
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_student:
                is_stu = true;
                flag = 0;
                break;
            case R.id.is_teacher:
                is_stu = false;
                flag++;
                if (flag >= 5) {
                    Intent i = new Intent();
                    i.setClass(LoginActivity.this, AdminActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.btn_login:
                userID = et_userID.getText().toString();
                password = et_password.getText().toString();
                // 判断是否已输入文本
                if (userID.trim().equals("")) {
                    Toast.makeText(this,
                            "请您输入账号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.trim().equals("")) {
                    Toast.makeText(this,
                            "请您输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 登录请求
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Log.e(TAG, "Thread run: 开启登录线程");
                        //学生登录
                        if (is_stu) {
                            //发送登录请求
                            LoginService loginService = new LoginService();
                            String json = null;
                            try {
                                json = loginService.studentLoginService(userID, password);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "服务端返回数据为: " + json);
                            List<Student> studentList = JSONObject.parseArray(json, Student.class);
                            if (studentList != null) {
                                rememberInfo(); //记住账号密码
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("is_stu", is_stu);
                                bundle.putString("userID", userID);
                                bundle.putString("userName", studentList.get(0).getStu_name());
                                bundle.putString("userSex", studentList.get(0).getStu_sex());
                                bundle.putString("userDate", studentList.get(0).getStu_date());
                                bundle.putString("userClass", studentList.get(0).getStu_class());
                                bundle.putString("userCollege", studentList.get(0).getStu_college());
                                Intent i = new Intent();
                                i.putExtras(bundle);
                                i.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
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
                            LoginService loginService = new LoginService();
                            String json = null;
                            try {
                                json = loginService.teacherLoginService(userID, password);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            List<Teacher> teacherList = JSONObject.parseArray(json, Teacher.class);
                            if (teacherList != null) {
                                rememberInfo(); //记住账号密码
                                Log.d(TAG, "teacherList：" + teacherList);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("is_stu", is_stu);
                                bundle.putString("userID", userID);
                                bundle.putString("userName", teacherList.get(0).getTec_name());
                                bundle.putString("userCollege", teacherList.get(0).getTec_department());
                                Intent i = new Intent();
                                i.putExtras(bundle);
                                i.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
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

    // 记住密码
    public void rememberInfo() {
        boolean CheckBoxLogin = cb_checkbox.isChecked();
        editor = sp.edit();
        if (CheckBoxLogin) {
            editor.putString("userID", userID);
            editor.putString("password", password);
            editor.putBoolean("checkboxBoolean", true);
        } else editor.clear();
        editor.apply();
    }

}