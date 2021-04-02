package com.example.cms.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginService {

    private static final String TAG = "Log日志";

    private String url_stu = "http://10.0.2.2:8080/LoginServlet";//服务器接口地址-学生登录
    private String url_tec = "http://10.0.2.2:8080/LoginServletTec";//服务器接口地址-教师登录
    String result = null;

    public String studentLoginService(String userID, String password) throws IOException {
        // 构造RequestBody
        RequestBody requestBody = new FormBody.Builder()
                .add("userID", userID)
                .add("password", password)
                .build();
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url_stu)
//                .addHeader("Set-Cookie",session) //请求头加入session
                .post(requestBody)
                .build();
        Log.e(TAG, "发送登录请求");
        // 请求加入调度(execute -> 同步请求)
        result = OkHttpUtil.getStringFromExecuteServer(request);
        return result;
    }

    public String teacherLoginService(String userID, String password) throws IOException {
        // 构造RequestBody
        RequestBody requestBody = new FormBody.Builder()
                .add("userID", userID)
                .add("password", password)
                .build();
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url_tec)
                .post(requestBody)
                .build();
        Log.e(TAG, "发送登录请求");
        // 请求加入调度(execute -> 同步请求)
        result = OkHttpUtil.getStringFromExecuteServer(request);
        return result;
    }

}
