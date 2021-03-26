package com.example.cms.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cms.R;

import com.example.cms.activity.MainActivity;


public class NotesFragment extends Fragment {

    private static final String TAG = "Log日志";

    private MainActivity mContext ;

    private ImageView iv_addNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
//        userID = getActivity().getIntent().getStringExtra("userID");
        iv_addNote = view.findViewById(R.id.iv_addNote);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        iv_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e(TAG, "点击增加笔记按钮");
                mContext.showAddNoteDialog();
            }
        });
    }

    // 解决getActivity()空指针问题
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) getActivity();
    }

}