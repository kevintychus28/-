package com.example.cms;

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

import com.example.cms.entity.Student;
import com.example.cms.entity.Teacher;
import com.example.cms.util.LoginService;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String userID, password;

    Student stu;
    Teacher tec;

    private RadioButton student;
    private RadioButton teacher;
    private EditText et_userID;
    private EditText et_password;
    private CheckBox cb_checkbox;
    private Button btn_login;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    //实例化
    private void initView() {
        //实例化控件
        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
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
            case R.id.btn_login:
                new Thread() {
                    public void run() {
                        Log.d(TAG, "run: 开启线程");
                        userID = et_userID.getText().toString().trim();
                        password = et_password.getText().toString().trim();
                        //发送登录请求
                        LoginService loginService = new LoginService();
                        if (loginService.LoginService(userID, password)) {
                            Intent i = new Intent();
                            i.putExtra("userID",userID);
                            i.setClass(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                        } else {
                            Looper.prepare();
                            Toast toast = Toast.makeText(getApplicationContext(), "账号或密码错误，请重试", Toast.LENGTH_LONG);
                            toast.show();
                            Looper.loop();
                        }
                    }
                }.start();
                break;
        }
    }


}