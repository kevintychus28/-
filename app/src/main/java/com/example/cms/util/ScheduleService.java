package com.example.cms.util;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.entity.Cource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ScheduleService {

    private static final String TAG = "MainActivity";

    private String url = "http://10.0.2.2:8080/ScheduleServlet";//服务器接口地址

    public ScheduleService() {
    }

    public String getSchedule(String userID) {
        try {
            Log.d(TAG, "发送请求");
            // 创建请求方法的实例，并指定请求URL
            HttpGet httpGet = new HttpGet(url + "?userID=" + userID);
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Get方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpGet);
            Log.d(TAG, "接收响应");
            // 检查状态码
            if (response.getStatusLine().getStatusCode() == 200) {
                // 建立httpEntity对象
                HttpEntity httpEntity = response.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String temp;
                // 获取服务器的响应内容
                while (null != (temp = reader.readLine())) {
                    sb.append(temp);
                }
                // 把响应内容json格式数据转换为字符串
                String json = sb.toString();
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

