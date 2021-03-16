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

public class LoginService {

    private static final String TAG = "LoginActivity";

    private String url = "http://10.0.2.2:8080/LoginServlet";//服务器接口地址

    public boolean LoginService(String userID, String password){
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
            Log.d(TAG, "发送请求");
            // 检查状态码
            if (response.getStatusLine().getStatusCode() == 200) {
                // 查看响应结果
                if (getInfo(response)) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 收取数据
    private static boolean getInfo(HttpResponse response) throws Exception {
        Log.d(TAG, "getInfo: 接受响应");
        //包装服务器的响应内容
        HttpEntity httpEntity = response.getEntity();
        //通过httpEntity对象获取服务器的响应内容
        InputStream inputStream = httpEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String result = "";
        String line = "";
        while (null != (line = reader.readLine())) {
            result += line;
        }
        Log.d(TAG, result);
        if (result.equals("success")) {
            return true;
        }
        return false;
    }
}
