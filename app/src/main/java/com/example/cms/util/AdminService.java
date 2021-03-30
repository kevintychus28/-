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

public class AdminService {

    private static final String TAG = "/*/管理员/*/";

    public AdminService() {

    }

    String url;
    String stu_url = "http://10.0.2.2:8080/StudentServlet";
    String tec_url = "http://10.0.2.2:8080/TeacherServlet";
    String cou_url = "http://10.0.2.2:8080/CourseServlet";
    String parameter;

    /**
     *
     * 学生
     *
     **/
    public String getStudent() {
        parameter = "?type=get";
        url = stu_url + parameter;
        return doGet(url);
    }

    public Boolean addStudent(String stu_id, String stu_password, String stu_name, String stu_sex, String stu_date, String stu_class, String stu_college) {
        url = stu_url;
        NameValuePair pair1 = new BasicNameValuePair("stu_id", stu_id);
        NameValuePair pair2 = new BasicNameValuePair("stu_password", stu_password);
        NameValuePair pair3 = new BasicNameValuePair("stu_name", stu_name);
        NameValuePair pair4 = new BasicNameValuePair("stu_sex", stu_sex);
        NameValuePair pair5 = new BasicNameValuePair("stu_date", stu_date);
        NameValuePair pair6 = new BasicNameValuePair("stu_class", stu_class);
        NameValuePair pair7 = new BasicNameValuePair("stu_college", stu_college);
        NameValuePair pair8 = new BasicNameValuePair("type", "add");

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        pairList.add(pair6);
        pairList.add(pair7);
        pairList.add(pair8);
        return doPost(url, pairList);
    }

    public Boolean editStudent(String stu_id, String stu_password, String stu_name, String stu_sex, String stu_date, String stu_class, String stu_college) {
        url = stu_url;
        NameValuePair pair1 = new BasicNameValuePair("stu_id", stu_id);
        NameValuePair pair2 = new BasicNameValuePair("stu_password", stu_password);
        NameValuePair pair3 = new BasicNameValuePair("stu_name", stu_name);
        NameValuePair pair4 = new BasicNameValuePair("stu_sex", stu_sex);
        NameValuePair pair5 = new BasicNameValuePair("stu_date", stu_date);
        NameValuePair pair6 = new BasicNameValuePair("stu_class", stu_class);
        NameValuePair pair7 = new BasicNameValuePair("stu_college", stu_college);
        NameValuePair pair8 = new BasicNameValuePair("type", "edit");

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        pairList.add(pair6);
        pairList.add(pair7);
        pairList.add(pair8);
        return doPost(url, pairList);
    }

    public String deleteStudent(String stu_id) {
        parameter = "?type=delete&stu_id=" + stu_id;
        url = stu_url + parameter;
        return doGet(url);
    }

    /**
     *
     * 老师
     *
     **/
    public String getTeacher() {
        parameter = "?type=get";
        url = tec_url + parameter;
        return doGet(url);
    }

    public Boolean addTeacher(String tec_id, String tec_password, String tec_name, String tec_department) {
        url = tec_url;
        NameValuePair pair1 = new BasicNameValuePair("tec_id", tec_id);
        NameValuePair pair2 = new BasicNameValuePair("tec_password", tec_password);
        NameValuePair pair3 = new BasicNameValuePair("tec_name", tec_name);
        NameValuePair pair4 = new BasicNameValuePair("tec_department", tec_department);
        NameValuePair pair5 = new BasicNameValuePair("type", "add");

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        return doPost(url, pairList);
    }

    public Boolean editTeacher(String tec_id, String tec_password, String tec_name, String tec_department) {
        url = tec_url;
        NameValuePair pair1 = new BasicNameValuePair("tec_id", tec_id);
        NameValuePair pair2 = new BasicNameValuePair("tec_password", tec_password);
        NameValuePair pair3 = new BasicNameValuePair("tec_name", tec_name);
        NameValuePair pair4 = new BasicNameValuePair("tec_department", tec_department);
        NameValuePair pair5 = new BasicNameValuePair("type", "edit");

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        return doPost(url, pairList);
    }

    public String deleteTeacher(String tec_id) {
        parameter = "?type=delete&tec_id=" + tec_id;
        url = tec_url + parameter;
        return doGet(url);
    }

    /**
     *
     * 课程
     *
     **/
    public String getCourse() {
        parameter = "?type=get";
        url = cou_url + parameter;
        return doGet(url);
    }

    public Boolean addCourse(String cou_name, String cou_teacher, String cou_classroom, String cou_weekday, String cou_period, String cou_exam_time) {
        url = cou_url;
        NameValuePair pair1 = new BasicNameValuePair("cou_name", cou_name);
        NameValuePair pair2 = new BasicNameValuePair("cou_teacher", cou_teacher);
        NameValuePair pair3 = new BasicNameValuePair("cou_classroom", cou_classroom);
        NameValuePair pair4 = new BasicNameValuePair("cou_weekday", cou_weekday);
        NameValuePair pair5 = new BasicNameValuePair("cou_period", cou_period);
        NameValuePair pair6 = new BasicNameValuePair("cou_exam_time", cou_exam_time);
        NameValuePair pair7 = new BasicNameValuePair("type", "add");

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        pairList.add(pair6);
        pairList.add(pair7);
        return doPost(url, pairList);
    }

    public Boolean editCourse(String cou_id, String cou_name, String cou_teacher, String cou_classroom, String cou_weekday, String cou_period, String cou_exam_time) {
        url = cou_url;
        NameValuePair pair1 = new BasicNameValuePair("cou_id", cou_id);
        NameValuePair pair2 = new BasicNameValuePair("cou_name", cou_name);
        NameValuePair pair3 = new BasicNameValuePair("cou_teacher", cou_teacher);
        NameValuePair pair4 = new BasicNameValuePair("cou_classroom", cou_classroom);
        NameValuePair pair5 = new BasicNameValuePair("cou_weekday", cou_weekday);
        NameValuePair pair6 = new BasicNameValuePair("cou_period", cou_period);
        NameValuePair pair7 = new BasicNameValuePair("cou_exam_time", cou_exam_time);
        NameValuePair pair8 = new BasicNameValuePair("type", "add");

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        pairList.add(pair6);
        pairList.add(pair7);
        pairList.add(pair8);
        return doPost(url, pairList);
    }

    public String deleteCourse(String cou_id) {
        parameter = "?type=delete&cou_id=" + cou_id;
        url = cou_url + parameter;
        return doGet(url);
    }

//  <<---<<---<<---<<---<<---<<--- method --->>--->>--->>--->>--->>--->>
    public String doGet(String url) {
        try {
            Log.e(TAG, "发送get请求");
            // 创建请求方法的实例，并指定请求URL
            HttpGet httpGet = new HttpGet(url);
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

    public Boolean doPost(String url, List<NameValuePair> pairList) {
        try {
            Log.e(TAG, "发起post请求");
            // 创建请求方法的实例，并指定请求URL
            HttpPost httpPost = new HttpPost(url);
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
                Log.e(TAG, "响应：" + sb);
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
