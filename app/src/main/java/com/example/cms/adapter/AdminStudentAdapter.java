package com.example.cms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cms.R;
import com.example.cms.activity.AdminActivity;
import com.example.cms.activity.MainActivity;
import com.example.cms.entity.Student;

import java.util.List;

public class AdminStudentAdapter extends RecyclerView.Adapter<AdminStudentAdapter.ViewHolder> {

    private static final String TAG = "Log日志";
    private List<Student> studentList;
    private AdminActivity mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView stu_id;
        TextView stu_password;
        TextView stu_name;
        TextView stu_sex;
        TextView stu_date;
        TextView stu_class;
        TextView stu_college;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stu_name = (TextView) itemView.findViewById(R.id.stu_name);
            stu_password = (TextView) itemView.findViewById(R.id.stu_password);
            stu_id = (TextView) itemView.findViewById(R.id.stu_id);
            stu_sex = (TextView) itemView.findViewById(R.id.stu_sex);
            stu_date = (TextView) itemView.findViewById(R.id.stu_date);
            stu_class = (TextView) itemView.findViewById(R.id.stu_class);
            stu_college = (TextView) itemView.findViewById(R.id.stu_college);
        }
    }

    public AdminStudentAdapter(AdminActivity mContext, List<Student> list) {
        this.mContext = mContext;
        this.studentList = list;
    }

    /**
     * 用于创建ViewHolder实例,并把加载的布局传入到构造函数去,再把ViewHolder实例返回
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * 用于对子项的数据进行赋值,会在每个子项被滚动到屏幕内时执行
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AdminStudentAdapter.ViewHolder holder, int position) {
        // 首先得到我们想要的对象
        Student student = studentList.get(position);
        // 设置属性
        holder.stu_name.setText(student.getStu_name());
        holder.stu_password.setText(student.getStu_password());
        holder.stu_id.setText(student.getStu_id());
        holder.stu_sex.setText(student.getStu_sex());
        holder.stu_date.setText(student.getStu_date());
        holder.stu_class.setText(student.getStu_class());
        holder.stu_college.setText(student.getStu_college());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.showStudentDialog(position,"edit");
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }


}
