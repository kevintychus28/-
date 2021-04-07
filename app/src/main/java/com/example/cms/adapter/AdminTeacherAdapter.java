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
import com.example.cms.entity.Teacher;

import java.util.List;

public class AdminTeacherAdapter extends RecyclerView.Adapter<AdminTeacherAdapter.ViewHolder> {

    private static final String TAG = "Log日志";
    private List<Teacher> teacherList;
    private AdminActivity mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tec_id;
        TextView tec_password;
        TextView tec_name;
        TextView tec_department;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tec_id = (TextView) itemView.findViewById(R.id.tec_id);
            tec_password = (TextView) itemView.findViewById(R.id.tec_password);
            tec_name = (TextView) itemView.findViewById(R.id.tec_name);
            tec_department = (TextView) itemView.findViewById(R.id.tec_department);
        }
    }

    public AdminTeacherAdapter(AdminActivity mContext, List<Teacher> list) {
        this.mContext = mContext;
        this.teacherList = list;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        // 设置属性
        holder.tec_id.setText(teacher.getTec_id());
        holder.tec_password.setText(teacher.getTec_password());
        holder.tec_name.setText(teacher.getTec_name());
        holder.tec_department.setText(teacher.getTec_department());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.showTeacherDialog(position, "edit");
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }


}
