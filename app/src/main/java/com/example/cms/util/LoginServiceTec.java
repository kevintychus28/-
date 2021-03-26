package com.example.cms.util;

import android.util.Log;

import com.example.cms.entity.Student;

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

public class LoginServiceTec {

    private static final String TAG = "Log日志";

    private String url = "http://10.0.2.2:8080/LoginServletTec";//服务器接口地址

    public String LoginService(String userID, String password) {
        Student stu = new Student(userID, password);
        NameValuePair pair1 = new BasicNameValuePair("userID", userID);
        NameValuePair pair2 = new BasicNameValuePair("password", password);
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        try {
            // 创建请求方法的实例，并指定请求URL
            HttpPost httpPost = new HttpPost(url);
            // 设置请求体的内容参数
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用POST方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpPost);
            Log.e(TAG, "发送请求");
            // 检查状态码
            if (response.getStatusLine().getStatusCode() == 200) {
                // 查看响应结果
                String info = getInfo(response);
                Log.e(TAG, info);
                if (info != null) {
                    return info;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 收取数据
    private static String getInfo(HttpResponse response) throws Exception {
        Log.e(TAG, "getInfo: 接受响应");
        //包装服务器的响应内容
        HttpEntity httpEntity = response.getEntity();
        //通过httpEntity对象获取服务器的响应内容
        InputStream inputStream = httpEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String temp;
        // 获取服务器的响应内容
        while (null != (temp = reader.readLine())) {
            sb.append(temp);
        }
        String json = sb.toString();
        Log.e(TAG, json);
        if (sb.toString() != null) {
            return json;
        }
        return null;
    }
}
