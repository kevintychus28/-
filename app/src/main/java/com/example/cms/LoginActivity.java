package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


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
    }

    //实例化
    private void initView() {
        //实例化控件
        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
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
            case R.id.student:
                break;
            case R.id.teacher:
                break;
            case R.id.cb_checkbox:
                break;
            case R.id.btn_login:
                Intent i = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}