package com.example.cms.util;

import android.content.Intent;
import android.util.Log;

import org.apache.http.Consts;
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

public class NoteService {

    private static final String TAG = "Log日志";

    private String getUrl = "http://10.0.2.2:8080/GetNoteServlet";//服务器接口地址
    private String addUrl = "http://10.0.2.2:8080/AddNoteServlet";//服务器接口地址
    private String deleteUrl = "http://10.0.2.2:8080/DeleteNoteServlet";//服务器接口地址
    private String editUrl = "http://10.0.2.2:8080/EditNoteServlet";//服务器接口地址

    public String getNote(String userID) {
        try {
            Log.e(TAG, "发送获得笔记请求");
            // 创建请求方法的实例，并指定请求URL
            HttpGet httpGet = new HttpGet(getUrl + "?userID=" + userID);
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Get方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpGet);
            Log.e(TAG, "接收获得笔记响应");
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

    public Boolean addNote(String userID, String title, String content) {
        Log.e(TAG, "新增笔记: " + title + content);
        NameValuePair pair1 = new BasicNameValuePair("userID", userID);
        NameValuePair pair2 = new BasicNameValuePair("title", title);
        NameValuePair pair3 = new BasicNameValuePair("content", content);
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        try {
            Log.e(TAG, "发起增加笔记请求");
            // 创建请求方法的实例，并指定请求URL
            HttpPost httpPost = new HttpPost(addUrl);
//            HttpGet httpGet = new HttpGet(addUrl + "?userID=" + userID + "&title=" + title + "&content=" + content);
            // 设置请求体的内容参数，带上编码参数"UTF-8"解决问号问题
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 在请求的时候加个头，解决问号问题
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Post方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpPost);
//            HttpResponse response = httpClient.execute(httpGet);
            Log.e(TAG, "接收增加笔记响应");
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
                if (sb.toString().equals("success")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteNote(String userID, String note_id) {
        try {
            Log.e(TAG, "发送删除笔记请求");
            // 创建请求方法的实例，并指定请求URL
            HttpGet httpGet = new HttpGet(deleteUrl + "?userID=" + userID + "&note_id=" + note_id);
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Get方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpGet);
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

    public Boolean editNote(String userID, String note_id, String title, String content) {
        Log.e(TAG, "String修改笔记: " + title + content);
        NameValuePair pair1 = new BasicNameValuePair("userID", userID);
        NameValuePair pair2 = new BasicNameValuePair("note_id", note_id);
        NameValuePair pair3 = new BasicNameValuePair("title", title);
        NameValuePair pair4 = new BasicNameValuePair("content", content);
        Log.e(TAG, "NameValuePair修改笔记: " + pair3 + pair4);
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        try {
            Log.e(TAG, "发起修改笔记请求");
            // 创建请求方法的实例，并指定请求URL
            HttpPost httpPost = new HttpPost(editUrl);
//            HttpGet httpGet = new HttpGet(editUrl + "?userID=" + userID + "&note_id=" + note_id + "&title=" + title + "&content=" + content);
            // 设置请求体的内容参数
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 在请求的时候加个头，解决问号问题
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            // 创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 客户端调用execute方法，使用Post方式执行请求，获得服务器端的回应response
            HttpResponse response = httpClient.execute(httpPost);
//            HttpResponse response = httpClient.execute(httpGet);
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
