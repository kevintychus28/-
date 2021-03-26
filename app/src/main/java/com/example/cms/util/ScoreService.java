package com.example.cms.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScoreService {
    private static final String TAG = "Log日志";

    private String getUrl1 = "http://10.0.2.2:8080/GetScoreServlet";//服务器接口地址
    private String getUrl2 = "http://10.0.2.2:8080/GetCourseGradeServlet";//服务器接口地址
    private String editUrl = "http://10.0.2.2:8080/EditScoreServlet";//服务器接口地址

    public ScoreService() {
    }

    public String getScore(String userID) {
        try {
            Log.e(TAG, "发送获取成绩请求");
            // 创建请求方法的实例，并指定请求URL
            HttpGet httpGet = new HttpGet(getUrl1 + "?userID=" + userID);
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Get方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpGet);
            Log.e(TAG, "接收响应");
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


    public String getCourseGrade(String userID, String cou_name) {
        try {
            Log.e(TAG, "发送获取单门课程中所有成绩的请求");
            // 创建请求方法的实例，并指定请求URL
            HttpGet httpGet = new HttpGet(getUrl2 + "?userID=" + userID + "&cou_name="+ cou_name);
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Get方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpGet);
            Log.e(TAG, "接收响应");
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


    public Boolean editScore(String userID, String cou_name,String stu_name, String grade) {
        Log.e(TAG, "String修改成绩: " + cou_name + " - " + grade);
        NameValuePair pair1 = new BasicNameValuePair("userID", userID);
        NameValuePair pair2 = new BasicNameValuePair("cou_name", cou_name);
        NameValuePair pair3 = new BasicNameValuePair("stu_name", stu_name);
        NameValuePair pair4 = new BasicNameValuePair("grade", grade);
        Log.e(TAG, "NameValuePair修改成绩: " + pair2 + pair3 + pair4);
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        try {
            Log.e(TAG, "发起修改成绩请求");
            // 创建请求方法的实例，并指定请求URL
            HttpPost httpPost = new HttpPost(editUrl);
            // 设置请求体的内容参数
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 在请求的时候加个头，解决问号问题
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Post方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpPost);
            // 检查状态码
            Log.e(TAG, "状态码：" + Integer.toString(response.getStatusLine().getStatusCode()));
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
                if (sb.toString().equals("success")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
