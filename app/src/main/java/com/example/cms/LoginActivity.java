package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cms.user.Student;
import com.example.cms.user.Teacher;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String username,password;

    Student stu;
    Teacher tec;

    private RadioButton student;
    private RadioButton teacher;
    private EditText et_userName;
    private EditText et_password;
    private CheckBox cb_checkbox;
    private Button btn_login;


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
        et_userName = findViewById(R.id.et_userName);
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
        switch (view.getId()){
            case R.id.btn_login:
                username = et_userName.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(cb_checkbox.isClickable()) {
//                    保存账号密码
                }
                stu = new Student(username,password);
                checkStuLogin(stu);
                break;
        }
    }

    private void checkStuLogin(Student stu) {
        if(stu.getStu_id().equals("17251102126") && stu.getStu_password().equals("123456")) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else {
            Toast toast=Toast.makeText(this,"账号或密码错误",Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}