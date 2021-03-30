package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.R;
import com.example.cms.adapter.GirdViewStudentAdapter;
import com.example.cms.entity.Course;
import com.example.cms.entity.Student;
import com.example.cms.util.AdminService;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "/*/管理员/*/";

    private List<Student> studentList;
    private GirdViewStudentAdapter GirdViewStudentAdapter;
    private GridView gridViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getStudent();
    }

    /**
     * 获取学生信息
     **/
    public void getStudent() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                String json = adminService.getStudent();
                studentList = JSONObject.parseArray(json, Student.class);
                Log.e(TAG, "studentList: " + studentList);
                Message msg = Message.obtain();
                msg.what = 200;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 展示学生信息
     **/
    public void setStudent() {
        GirdViewStudentAdapter = new GirdViewStudentAdapter(this, studentList);
        gridViewDetail = findViewById(R.id.gridViewDetail);
        gridViewDetail.setAdapter(GirdViewStudentAdapter);
        gridViewDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showStudentDialog(position);
            }
        });
    }

    /**
     * 删除学生信息
     **/
    public void deleteStudent(String stu_id) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.deleteStudent(stu_id).equals("success")) {
                    Log.e(TAG, "学生信息删除成功");
//                    toast("删除成功");
                } else {
//                    toast("删除失败");
                }
                Message msg = Message.obtain();
                msg.what = 100;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 修改学生信息
     **/
    public void editStudent(String stu_id, String stu_password, String stu_name, String stu_sex, String stu_date, String stu_class, String stu_college) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.editStudent(stu_id, stu_password, stu_name, stu_sex, stu_date, stu_class, stu_college)) {
                    Log.e(TAG, "学生信息修改成功");
//                    toast("修改成功");
                } else {
//                    toast("修改失败");
                }
                Message msg = Message.obtain();
                msg.what = 100;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private String studentSex;

    /**
     * 弹窗-学生信息
     **/
    public void showStudentDialog(int position) {
        // 装入自定义View ==> R.layout.dialog_student
        AlertDialog.Builder showCourseDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_student, null);
        showCourseDialogBuilder.setTitle("学生信息");
        showCourseDialogBuilder.setView(dialogView);
        AlertDialog showCourseDialog = showCourseDialogBuilder.create();

        // 展示学生信息
        EditText et_studentID = (EditText) dialogView.findViewById(R.id.et_studentID);
        EditText et_studentPassword = (EditText) dialogView.findViewById(R.id.et_studentPassword);
        EditText et_studentName = (EditText) dialogView.findViewById(R.id.et_studentName);
        RadioGroup rg_studentSex = (RadioGroup) dialogView.findViewById(R.id.rg_studentSex);
        RadioButton rb_studentMan = (RadioButton) dialogView.findViewById(R.id.rb_studentMan);
        RadioButton rb_studentWoman = (RadioButton) dialogView.findViewById(R.id.rb_studentWoman);
        EditText et_studentDate = (EditText) dialogView.findViewById(R.id.et_studentDate);
        EditText et_studentClass = (EditText) dialogView.findViewById(R.id.et_studentClass);
        EditText et_studentCollege = (EditText) dialogView.findViewById(R.id.et_studentCollege);
        Button btn_studentDelete = (Button) dialogView.findViewById(R.id.btn_studentDelete);
        Button btn_studentEdit = (Button) dialogView.findViewById(R.id.btn_studentEdit);

        Student student = studentList.get(position);

        et_studentID.setText(student.getStu_id());
        et_studentID.setEnabled(false);
        et_studentPassword.setText(student.getStu_password());
        et_studentName.setText(student.getStu_name());
        switch (student.getStu_sex()) {
            case "女":
                rb_studentWoman.setChecked(true);
                break;
            default:
                rb_studentMan.setChecked(true);
                break;
        }
        studentSex = student.getStu_sex();
        et_studentDate.setText(student.getStu_date());
        et_studentClass.setText(student.getStu_class());
        et_studentCollege.setText(student.getStu_college());

        rg_studentSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_studentWoman:
                        studentSex = "女";
                        break;
                    default:
                        studentSex = "男";
                        break;
                }
            }
        });
        // 删除学生信息
        btn_studentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent(student.getStu_id());
                getStudent();
                showCourseDialog.dismiss();
            }
        });
        // 修改学生信息
        btn_studentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_studentID.getText())
                        && !TextUtils.isEmpty(et_studentPassword.getText())
                        && !TextUtils.isEmpty(et_studentName.getText())
                        && !TextUtils.isEmpty(et_studentDate.getText())
                        && !TextUtils.isEmpty(et_studentClass.getText())
                        && !TextUtils.isEmpty(et_studentCollege.getText())) {
                    editStudent(et_studentID.getText().toString(), et_studentPassword.getText().toString(), et_studentName.getText().toString(), studentSex, et_studentDate.getText().toString(), et_studentClass.getText().toString(), et_studentCollege.getText().toString());
                    getStudent();
                    showCourseDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(AdminActivity.this, "请补全信息", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        showCourseDialog.show();
    }

    //接受子线程的message
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 100:
                    getStudent();
                    break;
                case 200:
                    setStudent();
                    break;
            }
        }
    };

//    public void toast(String text) {
//        Looper.prepare();
//        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
//        toast.show();
//        Looper.loop();
//    }
}