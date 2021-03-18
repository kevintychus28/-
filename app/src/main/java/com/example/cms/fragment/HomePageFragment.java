package com.example.cms.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cms.R;
import com.example.cms.activity.MainActivity;

public class HomePageFragment extends Fragment {

    private static final String TAG = "Log日志";

    private MainActivity mContext;

    public HomePageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        Log.d(TAG, "更新个人信息");
        TextView hp_userName = (TextView) view.findViewById(R.id.hp_userName);
        TextView hp_userID = (TextView) view.findViewById(R.id.hp_userID);
        Log.d(TAG, mContext.getIntent().getStringExtra("userName") + mContext.getIntent().getStringExtra("userID"));
        hp_userName.setText(mContext.getIntent().getStringExtra("userName"));
        hp_userID.setText(mContext.getIntent().getStringExtra("userID"));
        Log.d(TAG, hp_userName.getText().toString() + hp_userID.getText().toString());
        return view;
    }

    public void initHomePage() {
        new Thread() {
            @Override
            public void run() {
                super.run();

            }
        }.start();
    }

    // 解决getActivity()空指针问题
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) getActivity();
    }
}