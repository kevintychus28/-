package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cms.user.Student;
import com.example.cms.user.Teacher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String username, password;

    Student stu;
    Teacher tec;

    private RadioButton student;
    private RadioButton teacher;
    private EditText et_userName;
    private EditText et_password;
    private CheckBox cb_checkbox;
    private Button btn_login;


    private String url = "http://10.0.2.2:8080/CourseManagementSystem%20Server/LoginServlet";//服务器接口地址

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
        switch (view.getId()) {
            case R.id.btn_login:
                new Thread() {
                    public void run() {
                        Log.d(TAG, "run: 开启线程");
                        username = et_userName.getText().toString().trim();
                        password = et_password.getText().toString().trim();
                        //保存账号密码
                        if (cb_checkbox.isClickable()) {
                        }
                        stu = new Student(username, password);
//                        checkStuLogin(stu);
                        NameValuePair pair1 = new BasicNameValuePair("username", username);
                        NameValuePair pair2 = new BasicNameValuePair("password", password);
                        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                        pairList.add(pair1);
                        pairList.add(pair2);
                        try {
                            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                            // URl是接口地址
                            HttpPost httpPost = new HttpPost(url);
                            // 将请求体内容加入请求中
                            httpPost.setEntity(requestHttpEntity);
                            // 需要客户端对象来发送请求
                            HttpClient httpClient = new DefaultHttpClient();
                            // 发送请求
                            HttpResponse response = httpClient.execute(httpPost);
                            Log.d(TAG, "发送请求");
                            // 显示响应
                            if (getInfo(response)) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
                break;
        }
    }

    // 收取数据
    private static boolean getInfo(HttpResponse response) throws Exception {
        Log.d(TAG, "getInfo: 接受响应");
        HttpEntity httpEntity = response.getEntity();
        InputStream inputStream = httpEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String result = "";
        String line = "";
        while (null != (line = reader.readLine())) {
            result += line;
        }
        if (result.equals("success")) {
            return true;
        }
        return false;
    }


//    private void checkStuLogin(Student stu) {
//        if (stu.getStu_id().equals("17251102126") && stu.getStu_password().equals("123456")) {
//            Intent i = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        } else {
//            Toast toast = Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }


}