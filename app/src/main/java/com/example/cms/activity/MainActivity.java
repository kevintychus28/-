package com.example.cms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.cms.R;
import com.example.cms.adapter.NoteListAdapter;
import com.example.cms.adapter.ScoreListAdapter;
import com.example.cms.entity.Course;
import com.example.cms.entity.Note;
import com.example.cms.entity.Roster;
import com.example.cms.util.AlarmService;
import com.example.cms.util.ExamService;
import com.example.cms.view.HomePageFragment;
import com.example.cms.view.NotesFragment;
import com.example.cms.view.ScheduleFragment;
import com.example.cms.view.ScoreFragment;
import com.example.cms.util.NoteService;
import com.example.cms.util.ScheduleService;
import com.example.cms.util.ScoreService;
import com.example.cms.view.SwipeListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Log日志";

    private LinearLayout fragment_schedule;
    private LinearLayout fragment_score_report;
    private LinearLayout fragment_notes;
    private LinearLayout fragment_homepage;

    private FrameLayout main_body;

    private RadioGroup bottom_bar;
    private RadioButton rb_schedule;
    private RadioButton rb_score;
    private RadioButton rb_note;
    private RadioButton rb_homepage;

    private ScheduleFragment fragment_1;
    private ScoreFragment fragment_2;
    private NotesFragment fragment_3;
    private HomePageFragment fragment_4;

    private FragmentManager fManager;

    // 课程表
    int itemHeight;
    int marTop, marLeft;
    private List<Course> scheduleList;
    private List[] courseData = new ArrayList[7];// 每日课程
    private LinearLayout[] weekPanels = new LinearLayout[7];
    private Intent intent;

    // 成绩表
    private ScoreListAdapter scoreListAdapter;
    private List<String> courseNameList;// 所有课程的名称
    private List<String> studentNameList;// 单门课程中的学生名单
    private List<Roster> showScore;// 展示的成绩
    String courseName;
    String studentName;
    private ListView scoreDetail;
    private Spinner switchCourse;
    private TextView rName;
    private Button edit_score;

    // 笔记
    private NoteListAdapter noteListAdapter;
    private List<Note> noteData = new ArrayList<>();
    private SwipeListView noteDetail;

    // 个人主页
    private List<Course> examTimeList;// 展示的成绩


//    在onCreate()之前你不能getIntent() – 那时根本就没有Intent可用.我相信任何需要Context的事情都是如此.
//    但是,您的匿名内部类仍然可以调用getIntent(),因此您根本不需要将其声明为变量.
//    userID = getIntent().getStringExtra("userID");

    //    String userID = "17251102126";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        //初始化视图
        initView();
        //加载课程表
        fManager = getFragmentManager();
        setFragments(rb_schedule);
        getScheduleData(getUserID(), getIdentity());
    }

    //实例化
    private void initView() {
        //承载四大功能的主页面
        main_body = findViewById(R.id.main_body);
        //底部导航栏
        bottom_bar = findViewById(R.id.rg_bottom_bar);
        rb_schedule = findViewById(R.id.rb_schedule);
        rb_score = findViewById(R.id.rb_score);
        rb_note = findViewById(R.id.rb_note);
        rb_homepage = findViewById(R.id.rb_homepage);
        //四大功能页面
        fragment_schedule = findViewById(R.id.fragment_schedule);
        fragment_score_report = findViewById(R.id.fragment_score_report);
        fragment_notes = findViewById(R.id.fragment_notes);
        fragment_homepage = findViewById(R.id.fragment_homepage);
        //添加点击事件
        rb_schedule.setOnClickListener(this);
        rb_score.setOnClickListener(this);
        rb_note.setOnClickListener(this);
        rb_homepage.setOnClickListener(this);
    }


    public String getUserID() {
        return getIntent().getStringExtra("userID");
    }

    public String getIdentity() {
        if (!getIntent().getBooleanExtra("is_stu", true)) {
            return "teacher";
        }
        return "student";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_schedule:
                setFragments(rb_schedule);
                getScheduleData(getUserID(), getIdentity());
                break;
            case R.id.rb_score:
                setFragments(rb_score);
                getScore(getIdentity());
                break;
            case R.id.rb_note:
                setFragments(rb_note);
                getNote(getUserID());
                break;
            case R.id.rb_homepage:
                setFragments(rb_homepage);
                getExamTime(getUserID(), getIdentity());
                break;
        }
    }

    /**
     * 设置四大功能区域
     *
     * @param v
     */
    private void setFragments(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideFragments(fTransaction);
        switch (v.getId()) {
            case R.id.rb_schedule:
                if (fragment_1 == null) {
                    fragment_1 = new ScheduleFragment();
                    fTransaction.add(R.id.main_body, fragment_1);
                } else {
                    fTransaction.show(fragment_1);
                }
                break;
            case R.id.rb_score:
                if (fragment_2 == null) {
                    fragment_2 = new ScoreFragment();
                    fTransaction.add(R.id.main_body, fragment_2);
                } else {
                    fTransaction.show(fragment_2);
                }
                break;
            case R.id.rb_note:
                if (fragment_3 == null) {
                    fragment_3 = new NotesFragment();
                    fTransaction.add(R.id.main_body, fragment_3);
                } else {
                    fTransaction.show(fragment_3);
                }
                break;
            case R.id.rb_homepage:
                if (fragment_4 == null) {
                    fragment_4 = new HomePageFragment();
                    fTransaction.add(R.id.main_body, fragment_4);
                } else {
                    fTransaction.show(fragment_4);
                }
                break;
        }
        fTransaction.commit();
    }

    /**
     * 隐藏功能区域
     *
     * @param fTransaction
     */
    private void hideFragments(FragmentTransaction fTransaction) {
        if (fragment_1 != null) {
            fTransaction.hide(fragment_1);
        }
        if (fragment_2 != null) {
            fTransaction.hide(fragment_2);
        }
        if (fragment_3 != null) {
            fTransaction.hide(fragment_3);
        }
        if (fragment_4 != null) {
            fTransaction.hide(fragment_4);
        }
    }

//    private void highlightNavigation(int i) {
//        switch (i) {
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//            case 4:
//                break;
//        }
//
//    }

    //接受子线程的message
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 000:
                    // 展示失败弹窗
                    showFailDialog();
                    break;
                case 100:
                    // 显示课程表
                    setSchedule();
                    break;
                case 200:
                    // 显示成绩表
                    setScore();
                    break;
                case 201:
                    // 获取成绩表
                    getScore(getIdentity());
                    break;
                case 300:
                    // 显示笔记
                    setNote();
                    break;
                case 301:
                    // 获取笔记内容
                    getNote(getUserID());
                    break;
            }
        }
    };

    /**
     * 展示失败重新弹窗
     */
    private void showFailDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("error");
        normalDialog.setMessage("操作失败，请重试！");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 课程表 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取每日的课程
     *
     * @param userID
     */
    public void getScheduleData(String userID, String identity) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ScheduleService scheduleService = new ScheduleService();
                String json = scheduleService.getSchedule(userID, identity);
                Log.d(TAG, "课程JSON数据: " + json);
                // 把json数据转换为List
                scheduleList = JSONObject.parseArray(json, Course.class);
                // 一周七天分别插入课程
                for (int i = 0; i < 7; i++) {
                    List<Course> list = new ArrayList<Course>();
                    // 插入单天内的课程
                    for (int j = 0; j < scheduleList.size(); j++)
                        if (scheduleList.get(j).getCou_weekday().equals(Integer.toString(i + 1))) {
                            list.add(scheduleList.get(j));
                            Log.e(TAG, "--插入周" + (i + 1) + "课程");
                        }
                    courseData[i] = list;// 周(i+1)的课程
                    Log.d(TAG, "courseData[" + i + "]: " + courseData[i]);
                }
                // 获取所有课程名
                courseNameList = new ArrayList<>();
                courseNameList.add("");
                for (int j = 0; j < scheduleList.size(); j++) {
                    courseNameList.add(scheduleList.get(j).getCou_name());
                }
                Log.d(TAG, "courseNameList：" + courseNameList);
                courseName = courseNameList.get(0);
                Message msg = Message.obtain();
                msg.what = 100;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 插入当天课程
     *
     * @param ll
     * @param data
     */
    public void initWeekPanel(LinearLayout ll, List<Course> data) {

        if (ll == null || data == null || data.size() < 1) return;
        // 清空旧课程
        ll.removeAllViews();
        itemHeight = getResources().getDimensionPixelSize(R.dimen.weekItemHeight);
        marTop = getResources().getDimensionPixelSize(R.dimen.weekItemMarTop);
        marLeft = getResources().getDimensionPixelSize(R.dimen.weekItemMarLeft);
        // 当天的上一个插入的课
        Course pre = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            // 现在要插入的课
            Course c = data.get(i);
            int cPeriod = Integer.parseInt(c.getCou_period());
            int prePeriod = Integer.parseInt(pre.getCou_period());
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
            if (i > 0) {// 获取并设置离上一门课程的距离
                lp.setMargins(marLeft, ((cPeriod - prePeriod - 1) * (itemHeight + marTop) + marTop), 0, 0);
            } else {// 获取并设置离顶部的距离
                lp.setMargins(marLeft, (cPeriod - 1) * (itemHeight + marTop) + marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.TOP);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(12);
            tv.setTextColor(getResources().getColor(R.color.courseTextColor));
            tv.setText(c.getCou_name() + "\n\n\n" + c.getCou_classroom());// 设置课程信息
            tv.setBackground(getResources().getDrawable(getCourseBackground()));
            // 添加点击课程，显示课程信息及提醒的弹窗的事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCourseDialog(c);
                }
            });
            // 将该课程插入到当天课程表中
            ll.addView(tv);
            Log.d(TAG, c.getCou_name());
            pre = c;
        }
    }

    int colorNum = 0;

    //随机获取课程表中的课程背景色
    public int getCourseBackground() {
        if (colorNum == 6) {
            colorNum = 0;
        }
        int color = R.drawable.course_shape_1;
        switch (colorNum) {
            case 0:
                color = R.drawable.course_shape_5;
                break;
            case 1:
                color = R.drawable.course_shape_3;
                break;
            case 2:
                color = R.drawable.course_shape_6;
                break;
            case 3:
                color = R.drawable.course_shape_4;
                break;
            case 4:
                color = R.drawable.course_shape_2;
                break;
            case 5:
                color = R.drawable.course_shape_1;
                break;
        }
        colorNum++;
        return color;
    }

    /**
     * 展示单个课程的信息
     */
    public void showCourseDialog(Course course) {
        // 装入自定义View ==> R.layout.dialog_course.xml
        AlertDialog.Builder showCourseDialog = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_course, null);
        showCourseDialog.setTitle("课程信息");
        showCourseDialog.setView(dialogView);
        // 展示课程信息
        TextView dl_courseName = (TextView) dialogView.findViewById(R.id.dl_courseName);
        TextView dl_teacherName = (TextView) dialogView.findViewById(R.id.dl_teacherName);
        TextView dl_courseClassroom = (TextView) dialogView.findViewById(R.id.dl_courseClassroom);
        dl_courseName.setText(course.getCou_name());
        dl_teacherName.setText(course.getCou_teacher());
        dl_courseClassroom.setText(course.getCou_classroom());
        // 设置课程提醒
        Button btn_alert = (Button) dialogView.findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getWeek().equals(course.getCou_weekday())) {
                    Toast toast = Toast.makeText(MainActivity.this, "只能提醒当天课程噢~", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.d(TAG, "该课程开始时间为：" + course.getStart_time());
                    intent = new Intent(MainActivity.this, AlarmService.class);
                    intent.putExtra("start_time", course.getStart_time());
                    //开启关闭Service
                    startService(intent);
                    Toast toast = Toast.makeText(MainActivity.this, "提醒设置成功！将在课程开始前20分钟提醒", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        showCourseDialog.show();
    }

    private String getWeek() {
        String week = "";
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            week = "7"; //周日
        } else if (weekday == 2) {
            week = "1"; //周一
        } else if (weekday == 3) {
            week = "2"; //周二
        } else if (weekday == 4) {
            week = "3"; //周三
        } else if (weekday == 5) {
            week = "4"; //周四
        } else if (weekday == 6) {
            week = "5"; //周五
        } else if (weekday == 7) {
            week = "6"; //周六
        }
        return week;
    }

    /**
     * 更新课程表
     */
    public void setSchedule() {
        colorNum = 0;
        Log.i("Msg", "初始化面板");
        for (int i = 0; i < weekPanels.length; i++) {
            weekPanels[i] = (LinearLayout) findViewById(R.id.weekPanel_1 + i);
            Log.e(TAG, "--更新周" + (i + 1) + "的课程");
            initWeekPanel(weekPanels[i], courseData[i]);
        }
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 成绩表 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取成绩
     *
     * @param identify
     */
    public void getScore(String identify) {
        if (identify.equals("student")) {
            stuGetScore(getUserID());
        } else if (identify.equals("teacher")) {
            getCourseGrade(getUserID(), courseName);
        }
    }

    /**
     * 学生获取成绩
     *
     * @param userID
     */
    public void stuGetScore(String userID) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ScoreService scoreService = new ScoreService();
                String json = scoreService.getScore(userID);
                Log.e(TAG, "成绩JSON数据: " + json);
                // 把json数据转换为List
                List<Roster> scoreList = JSONObject.parseArray(json, Roster.class);
                showScore = new ArrayList<>();// 获取该学生所有正常成绩的课程成绩单
                for (int i = 0; i < scoreList.size(); i++) {
                    if (Integer.parseInt(scoreList.get(i).getGrade()) >= 0 && Integer.parseInt(scoreList.get(i).getGrade()) <= 100) {
                        showScore.add(scoreList.get(i));
                    }
                }
                Message msg = Message.obtain();
                msg.what = 200;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 老师获取单门课程中所有学生的成绩
     *
     * @param userID
     */
    public void getCourseGrade(String userID, String cou_name) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.e(TAG, "cou_name：" + cou_name);
                ScoreService scoreService = new ScoreService();
                String json = scoreService.getCourseGrade(userID, cou_name);
                Log.e(TAG, "单门课程中所有学生的成绩JSON数据: " + json);
                // 把json数据转换为List
                List<Roster> scoreList = JSONObject.parseArray(json, Roster.class);
                showScore = new ArrayList<>();// 获取该门课程的成绩单
                studentNameList = new ArrayList<>();// 获取该门课程的学生名单
                for (int i = 0; i < scoreList.size(); i++) {
                    if (Integer.parseInt(scoreList.get(i).getGrade()) >= 0 && Integer.parseInt(scoreList.get(i).getGrade()) <= 101) {
                        showScore.add(scoreList.get(i));
                        studentNameList.add(scoreList.get(i).getName());
                    }
                }
                Log.e(TAG, "showScore: " + showScore);
                Message msg = Message.obtain();
                msg.what = 200;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    boolean isEditGradeFirst = true;
//    boolean doScoreDetail = false;

    /**
     * 更新成绩
     * 200
     */
    public void setScore() {
        if (isEditGradeFirst) {
            editGrade();
            isEditGradeFirst = false;
        }
        scoreListAdapter = new ScoreListAdapter(this, showScore);
        scoreDetail = findViewById(R.id.scoreDetail);
        scoreDetail.setAdapter(scoreListAdapter);
    }

    boolean isSwitchCourseFirst = true;

    /**
     * 展示修改成绩入口
     */
    public void editGrade() {
        switchCourse = findViewById(R.id.switchCourse);
        rName = findViewById(R.id.rName);
        edit_score = findViewById(R.id.edit_score);
        if (!getIntent().getBooleanExtra("is_stu", true)) {
            switchCourse.setVisibility(View.VISIBLE);
            rName.setText("学生姓名");
            edit_score.setVisibility(View.VISIBLE);
        }
        //Spinner中文框显示样式
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, courseNameList);
        //Spinner下拉菜单显示样式
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        switchCourse.setAdapter(adapter);
        //为下拉框添加选择事件
        switchCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //获取选择内容
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSwitchCourseFirst) {
                    courseName = courseNameList.get(position);
                    Log.e(TAG, "选择的课程是：" + courseName);
                    getCourseGrade(getUserID(), courseName);
                } else isSwitchCourseFirst = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //为修改成绩的按钮添加选择事件
        edit_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditScoreDialog();
            }
        });
    }

    /**
     * 展示修改成绩的弹窗
     */
    public void showEditScoreDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_score
         */
        AlertDialog.Builder editNoteDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_score, null);
        editNoteDialogBuilder.setTitle("修改成绩");
        editNoteDialogBuilder.setView(dialogView);
        //创建AlertDialog对象，用于下面去获取确定按钮
        AlertDialog editNoteDialog = editNoteDialogBuilder.create();
        //选择学生的下拉框
        Spinner switchStudent = (Spinner) dialogView.findViewById(R.id.switchStudent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, studentNameList);
        switchStudent.setAdapter(adapter);
        //编辑成绩的输入框
        EditText et_grade = (EditText) dialogView.findViewById(R.id.et_grade);
        //修改成绩的按钮
        Button grade_submit = (Button) dialogView.findViewById(R.id.grade_submit);

        //为输入框添加编辑事件
        et_grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (TextUtils.isEmpty(et_grade.getText())) {
                    grade_submit.setEnabled(false);
                } else {
                    grade_submit.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_grade.getText())) {
                    grade_submit.setEnabled(false);
                } else {
                    grade_submit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //为下拉框添加选择事件
        switchStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //获取选择内容
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentName = studentNameList.get(position);
                Log.e(TAG, "选择的学生是：" + studentName);
                if (showScore.get(position).getGrade().equals("101")) {
                    et_grade.setText(null);
                } else et_grade.setText(showScore.get(position).getGrade());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //为修改按钮添加点击事件
        grade_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditView中的输入内容
                String grade = et_grade.getText().toString();
                if (Integer.parseInt(grade) >= 0 && Integer.parseInt(grade) <= 100) {
                    Log.e(TAG, "userID: " + getUserID());
                    Log.e(TAG, "学生姓名为: " + studentName);
                    Log.e(TAG, "成绩为: " + grade);
                    editScore(getUserID(), courseName, studentName, grade);
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "只能输入正整数0-100噢！", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        // dialog的关闭按钮
        editNoteDialogBuilder.setPositiveButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        editNoteDialogBuilder.show();
    }

    /**
     * 修改成绩
     *
     * @param userID
     * @param cou_name
     * @param stu_name
     * @param grade
     */
    public void editScore(String userID, String cou_name, String stu_name, String grade) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ScoreService scoreService = new ScoreService();
                if (scoreService.editScore(userID, cou_name, stu_name, grade)) {
                    Log.e(TAG, "成绩修改成功");
                    Message msg = Message.obtain();
                    msg.what = 201;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 笔记表 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取笔记
     *
     * @param userID
     */
    public void getNote(String userID) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                String json = noteService.getNote(userID);
                Log.e(TAG, "笔记JSON数据: " + json);
                // 把json数据转换为List
                noteData = JSONObject.parseArray(json, Note.class);
                Message msg = Message.obtain();
                msg.what = 300;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 更新笔记
     */
    public void setNote() {
        noteListAdapter = new NoteListAdapter(MainActivity.this, noteData);
        noteDetail = findViewById(R.id.notesDetail);
        noteDetail.setAdapter(noteListAdapter);
        Log.e(TAG, "更新笔记");
    }

    /**
     * 展示添加笔记的弹窗
     */
    public void showAddNoteDialog() {
        // 装入自定义View ==> R.layout.dialog_note
        AlertDialog.Builder addNoteDialog = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_note, null);
        addNoteDialog.setTitle("添加笔记");
        addNoteDialog.setView(dialogView);
        addNoteDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        addNoteDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText et_addTitle = (EditText) dialogView.findViewById(R.id.et_title);
                        EditText et_addContent = (EditText) dialogView.findViewById(R.id.et_content);
                        String title = et_addTitle.getText().toString();
                        String content = et_addContent.getText().toString();
                        Log.e(TAG, "userID: " + getUserID());
                        Log.e(TAG, "新笔记标题: " + title);
                        Log.e(TAG, "新笔记内容: " + content);
                        addNote(getUserID(), title, content);
                    }
                });
        addNoteDialog.show();
    }

    /**
     * 增加笔记
     *
     * @param userID
     * @param title
     * @param content
     */
    public void addNote(String userID, String title, String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                if (noteService.addNote(userID, title, content)) {
                    Log.e(TAG, "笔记增加成功");
                    Message msg = Message.obtain();
                    msg.what = 301;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 展示删除笔记的弹窗
     */
    public void showDeleteNoteDialog(String note_id) {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
         * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
         * dialog_customize.xml可自定义更复杂的View
         */
        AlertDialog.Builder deleteNoteDialog = new AlertDialog.Builder(MainActivity.this);
        deleteNoteDialog.setTitle("删除笔记");
        deleteNoteDialog.setMessage("是否确定删除此笔记？");
        deleteNoteDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        deleteNoteDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        deleteNote(getUserID(), note_id);
                    }
                });
        deleteNoteDialog.show();
    }

    /**
     * 删除笔记
     *
     * @param userID
     * @param note_id
     */
    public void deleteNote(String userID, String note_id) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                if (noteService.deleteNote(userID, note_id)) {
                    Log.e(TAG, "笔记删除成功");
                    Message msg = Message.obtain();
                    msg.what = 301;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 展示修改笔记的弹窗
     */
    public void showEditNoteDialog(String note_id, String title, String content) {
        /* @setView 装入自定义View ==> R.layout.dialog_note
         */
        AlertDialog.Builder editNoteDialog = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_note, null);
        EditText et_editTitle = (EditText) dialogView.findViewById(R.id.et_title);
        EditText et_editContent = (EditText) dialogView.findViewById(R.id.et_content);
        editNoteDialog.setTitle("修改笔记");
        editNoteDialog.setView(dialogView);
        et_editTitle.setText(title);
        et_editContent.setText(content);
        // 输入框光标定位到末尾字符
        CharSequence textTitle = et_editTitle.getText();
        if (textTitle instanceof Spannable) {
            Spannable spanText = (Spannable) textTitle;
            Selection.setSelection(spanText, textTitle.length());
        }
        editNoteDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        editNoteDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        String title = et_editTitle.getText().toString();
                        String content = et_editContent.getText().toString();
                        Log.e(TAG, "userID: " + getUserID());
                        Log.e(TAG, "修改后笔记标题: " + title);
                        Log.e(TAG, "修改后笔记内容: " + content);
                        editNote(getUserID(), note_id, title, content);
                    }
                });
        editNoteDialog.show();
    }

    /**
     * 修改笔记
     *
     * @param userID
     * @param title
     * @param content
     */
    public void editNote(String userID, String note_id, String title, String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                NoteService noteService = new NoteService();
                if (noteService.editNote(userID, note_id, title, content)) {
                    Log.e(TAG, "笔记修改成功");
                    Message msg = Message.obtain();
                    msg.what = 301;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 000;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

//    <<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<---<<<--- 我的页面 --->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>--->>>

    /**
     * 获取考试时间
     *
     * @param userID
     * @param identify
     */
    public void getExamTime(String userID, String identify) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ExamService examService = new ExamService();
                String json = examService.getExamTime(userID, identify);
                Log.e(TAG, "考试时间JSON数据: " + json);
                // 把json数据转换为List
                examTimeList = JSONObject.parseArray(json, Course.class);
                Log.e(TAG, "examTimeList: " + examTimeList);
            }
        }.start();
    }

    public List<Course> getExamTimeList() {
        return examTimeList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity被关闭后，关闭Service
//        stopService(intent);
    }
}

