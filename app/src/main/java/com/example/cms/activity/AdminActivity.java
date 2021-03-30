package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.cms.adapter.GirdViewCourseAdapter;
import com.example.cms.adapter.GirdViewStudentAdapter;
import com.example.cms.adapter.GirdViewTeacherAdapter;
import com.example.cms.entity.Course;
import com.example.cms.entity.Student;
import com.example.cms.entity.Teacher;
import com.example.cms.util.AdminService;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "/*/管理员/*/";

    private String entity = "student";

    private RadioGroup rg_entity;
    private RadioButton rb_student;
    private RadioButton rb_teacher;
    private RadioButton rb_course;
    private Button add_entity;

    private List<Student> studentList;
    private GirdViewStudentAdapter GirdViewStudentAdapter;

    private List<Teacher> teacherList;
    private GirdViewTeacherAdapter girdViewTeacherAdapter;

    private List<Course> courseList;
    private GirdViewCourseAdapter girdViewCourseAdapter;


    private GridView gridViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
        getStudent();
    }

    public void init() {
        rg_entity = findViewById(R.id.rg_entity);
        rb_student = findViewById(R.id.rb_student);
        rb_teacher = findViewById(R.id.rb_teacher);
        rb_course = findViewById(R.id.rb_course);
        add_entity = findViewById(R.id.add_entity);
        rg_entity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_student:
                        getStudent();
                        entity = "student";
                        break;
                    case R.id.rb_teacher:
                        getTeacher();
                        entity = "teacher";
                        break;
                    case R.id.rb_course:
                        getCourse();
                        entity = "course";
                        break;
                }
            }
        });
        add_entity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (entity) {
                    case "student":
                        showStudentDialog(0, "add");
                        break;
                    case "teacher":
                        showTeacherDialog(0, "add");
                        break;
                    case "course":
                        showCourseDialog(0, "add");
                        break;
                }

            }
        });
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
                msg.what = 101;
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
                showStudentDialog(position, "edit");
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
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 102;
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
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 102;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 新增学生信息
     **/
    public void addStudent(String stu_id, String stu_password, String stu_name, String stu_sex, String stu_date, String stu_class, String stu_college) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.addStudent(stu_id, stu_password, stu_name, stu_sex, stu_date, stu_class, stu_college)) {
                    Log.e(TAG, "学生信息新增成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 102;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private String studentSex = "男";

    /**
     * 弹窗-学生信息
     **/
    public void showStudentDialog(int position, String type) {
        // 装入自定义View ==> R.layout.dialog_student
        AlertDialog.Builder showStudentDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_admin_student, null);
        showStudentDialogBuilder.setTitle("学生信息");
        showStudentDialogBuilder.setView(dialogView);
        AlertDialog showStudentDialog = showStudentDialogBuilder.create();

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

        Student student;
        String studentID = null;

        if (type.equals("edit")) {
            student = studentList.get(position);
            studentID = student.getStu_id();
            et_studentID.setText(studentID);
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
        } else if (type.equals("add")) {
            studentID = String.valueOf(Long.parseLong(studentList.get(studentList.size() - 1).getStu_id()) + 1);
            et_studentID.setText(studentID);
            btn_studentDelete.setVisibility(View.INVISIBLE);
            btn_studentEdit.setText("提交");
        }

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
                deleteStudent(studentList.get(position).getStu_id());
                showStudentDialog.dismiss();
            }
        });
        // 修改或新增学生信息
        String finalStudentID = studentID;
        btn_studentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_studentID.getText())
                        && !TextUtils.isEmpty(et_studentPassword.getText())
                        && !TextUtils.isEmpty(et_studentName.getText())
                        && !TextUtils.isEmpty(et_studentDate.getText())
                        && !TextUtils.isEmpty(et_studentClass.getText())
                        && !TextUtils.isEmpty(et_studentCollege.getText())) {
                    if (type.equals("edit")) {
                        Log.e(TAG, "修改学生信息 ");
                        editStudent(finalStudentID, et_studentPassword.getText().toString(), et_studentName.getText().toString(), studentSex, et_studentDate.getText().toString(), et_studentClass.getText().toString(), et_studentCollege.getText().toString());
                    } else {
                        Log.e(TAG, "新增学生信息 ");
                        addStudent(et_studentID.getText().toString(), et_studentPassword.getText().toString(), et_studentName.getText().toString(), studentSex, et_studentDate.getText().toString(), et_studentClass.getText().toString(), et_studentCollege.getText().toString());
                    }
                    showStudentDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(AdminActivity.this, "请补全信息", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        showStudentDialog.show();
    }


//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>


    /**
     * 获取教师信息
     **/
    public void getTeacher() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                String json = adminService.getTeacher();
                teacherList = JSONObject.parseArray(json, Teacher.class);
                Message msg = Message.obtain();
                msg.what = 201;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 展示教师信息
     **/
    public void setTeacher() {
        girdViewTeacherAdapter = new GirdViewTeacherAdapter(this, teacherList);
        gridViewDetail = findViewById(R.id.gridViewDetail);
        gridViewDetail.setAdapter(girdViewTeacherAdapter);
        gridViewDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTeacherDialog(position, "edit");
            }
        });
    }

    /**
     * 删除教师信息
     **/
    public void deleteTeacher(String tec_id) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.deleteTeacher(tec_id).equals("success")) {
                    Log.e(TAG, "教师信息删除成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 202;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 修改教师信息
     **/
    public void editTeacher(String tec_id, String tec_password, String tec_name, String tec_department) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.editTeacher(tec_id, tec_password, tec_name, tec_department)) {
                    Log.e(TAG, "教师信息修改成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 202;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 新增教师信息
     **/
    public void addTeacher(String tec_id, String tec_password, String tec_name, String tec_department) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.addTeacher(tec_id, tec_password, tec_name, tec_department)) {
                    Log.e(TAG, "教师信息新增成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 202;
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    /**
     * 弹窗-教师信息
     **/
    public void showTeacherDialog(int position, String type) {
        // 装入自定义View ==> R.layout.dialog_student
        AlertDialog.Builder showTeacherDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_admin_teacher, null);
        showTeacherDialogBuilder.setTitle("教师信息");
        showTeacherDialogBuilder.setView(dialogView);
        AlertDialog showTeacherDialog = showTeacherDialogBuilder.create();

        // 展示老师信息
        EditText et_teacherID = (EditText) dialogView.findViewById(R.id.et_teacherID);
        EditText et_teacherPassword = (EditText) dialogView.findViewById(R.id.et_teacherPassword);
        EditText et_teacherName = (EditText) dialogView.findViewById(R.id.et_teacherName);
        EditText et_teacherDepartment = (EditText) dialogView.findViewById(R.id.et_teacherDepartment);
        Button btn_teacherDelete = (Button) dialogView.findViewById(R.id.btn_teacherDelete);
        Button btn_teacherEdit = (Button) dialogView.findViewById(R.id.btn_teacherEdit);

        Teacher teacher = teacherList.get(position);
        String teacherID = null;

        if (type.equals("edit")) {
            teacherID = teacher.getTec_id();
            et_teacherID.setText(teacherID);
            et_teacherPassword.setText(teacher.getTec_password());
            et_teacherName.setText(teacher.getTec_name());
            et_teacherDepartment.setText(teacher.getTec_department());
        } else if (type.equals("add")) {
            teacherID = String.valueOf(Long.parseLong(teacherList.get(teacherList.size() - 1).getTec_id()) + 1);
            btn_teacherDelete.setVisibility(View.INVISIBLE);
            btn_teacherEdit.setText("提交");
        }

        // 删除老师信息
        btn_teacherDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTeacher(teacherList.get(position).getTec_id());
                showTeacherDialog.dismiss();
            }
        });
        // 修改或修改老师信息
        String finalTeacherID = teacherID;
        btn_teacherEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_teacherID.getText())
                        && !TextUtils.isEmpty(et_teacherPassword.getText())
                        && !TextUtils.isEmpty(et_teacherName.getText())
                        && !TextUtils.isEmpty(et_teacherDepartment.getText())) {
                    if (type.equals("edit")) {
                        editTeacher(et_teacherID.getText().toString(), et_teacherPassword.getText().toString(), et_teacherName.getText().toString(), et_teacherDepartment.getText().toString());
                    } else {
                        addTeacher(finalTeacherID, et_teacherPassword.getText().toString(), et_teacherName.getText().toString(), et_teacherDepartment.getText().toString());
                    }
                    showTeacherDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(AdminActivity.this, "请补全信息", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        showTeacherDialog.show();
    }


//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>
//  <<---<<---<<---<<---<<---<<---<<---<<---<<--- 分割 --->>--->>--->>--->>--->>--->>--->>--->>--->>


    /**
     * 获取课程信息
     **/
    public void getCourse() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                String json = adminService.getCourse();
                courseList = JSONObject.parseArray(json, Course.class);
                Message msg = Message.obtain();
                msg.what = 301;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 展示课程信息
     **/
    public void setCourse() {
        girdViewCourseAdapter = new GirdViewCourseAdapter(this, courseList);
        gridViewDetail = findViewById(R.id.gridViewDetail);
        gridViewDetail.setAdapter(girdViewCourseAdapter);
        gridViewDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCourseDialog(position, "edit");
            }
        });
    }

    /**
     * 删除课程信息
     **/
    public void deleteCourse(String cou_id) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.deleteCourse(cou_id).equals("success")) {
                    Log.e(TAG, "课程信息删除成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 302;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 修改课程信息
     **/
    public void editCourse(String cou_id, String cou_name, String cou_teacher, String cou_classroom, String cou_weekday, String cou_period, String cou_exam_time) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.editCourse(cou_id, cou_name, cou_teacher, cou_classroom, cou_weekday, cou_period, cou_exam_time)) {
                    Log.e(TAG, "课程信息修改成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 302;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 新增课程信息
     **/
    public void addCourse(String cou_name, String cou_teacher, String cou_classroom, String cou_weekday, String cou_period, String cou_exam_time) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AdminService adminService = new AdminService();
                if (adminService.addCourse(cou_name, cou_teacher, cou_classroom, cou_weekday, cou_period, cou_exam_time)) {
                    Log.e(TAG, "课程信息新增成功");
                } else {
                }
                Message msg = Message.obtain();
                msg.what = 302;
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    /**
     * 弹窗-课程信息
     **/
    public void showCourseDialog(int position, String type) {
        // 装入自定义View ==> R.layout.dialog_course.xml
        AlertDialog.Builder showCourseDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_admin_course, null);
        showCourseDialogBuilder.setTitle("课程信息");
        showCourseDialogBuilder.setView(dialogView);
        AlertDialog showCourseDialog = showCourseDialogBuilder.create();

        // 展示课程信息
        EditText et_courseName = (EditText) dialogView.findViewById(R.id.et_courseName);
        EditText et_courseTeacher = (EditText) dialogView.findViewById(R.id.et_courseTeacher);
        EditText et_courseClassroom = (EditText) dialogView.findViewById(R.id.et_courseClassroom);
        EditText et_courseWeek = (EditText) dialogView.findViewById(R.id.et_courseWeek);
        EditText et_coursePeriod = (EditText) dialogView.findViewById(R.id.et_coursePeriod);
        EditText et_courseExamTime = (EditText) dialogView.findViewById(R.id.et_courseExamTime);
        Button btn_courseDelete = (Button) dialogView.findViewById(R.id.btn_courseDelete);
        Button btn_courseEdit = (Button) dialogView.findViewById(R.id.btn_courseEdit);

        Course course = courseList.get(position);

        if (type.equals("edit")) {
            et_courseName.setText(course.getCou_name());
            et_courseTeacher.setText(course.getCou_teacher());
            et_courseClassroom.setText(course.getCou_classroom());
            et_courseWeek.setText(course.getCou_weekday());
            et_coursePeriod.setText(course.getCou_period());
            et_courseExamTime.setText(course.getCou_exam_time());
        } else if (type.equals("add")) {
            btn_courseDelete.setVisibility(View.INVISIBLE);
            btn_courseEdit.setText("提交");
        }

        // 删除课程信息
        btn_courseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse(courseList.get(position).getCou_id());
                showCourseDialog.dismiss();
            }
        });
        // 修改或新增课程信息
        btn_courseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_courseName.getText())
                        && !TextUtils.isEmpty(et_courseTeacher.getText())
                        && !TextUtils.isEmpty(et_courseClassroom.getText())
                        && !TextUtils.isEmpty(et_courseWeek.getText())
                        && !TextUtils.isEmpty(et_coursePeriod.getText())
                        && !TextUtils.isEmpty(et_courseExamTime.getText())) {
                    if (type.equals("edit")) {
                        editCourse(course.getCou_id(), et_courseName.getText().toString(), et_courseTeacher.getText().toString(), et_courseClassroom.getText().toString(), et_courseWeek.getText().toString(), et_coursePeriod.getText().toString(), et_courseExamTime.getText().toString());
                    } else {
                        addCourse(et_courseName.getText().toString(), et_courseTeacher.getText().toString(), et_courseClassroom.getText().toString(), et_courseWeek.getText().toString(), et_coursePeriod.getText().toString(), et_courseExamTime.getText().toString());
                    }
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
                case 102:
                    getStudent();
                    break;
                case 101:
                    setStudent();
                    break;
                case 202:
                    getTeacher();
                    break;
                case 201:
                    setTeacher();
                    break;
                case 302:
                    getCourse();
                    break;
                case 301:
                    setCourse();
                    break;
            }
        }
    };


}