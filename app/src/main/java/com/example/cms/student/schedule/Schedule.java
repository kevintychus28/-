package com.example.cms.student.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.example.cms.R;

import java.util.LinkedList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    private GridView courceDetail;
    LinkedList<Cource> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        courceDetail = (GridView)findViewById(R.id.courceDetail);

        mData.add(new Cource("大学语文", "一教108", "陈先生"));

        mData = new LinkedList<Cource>();
        MyAdapter myAdapter = new MyAdapter(mData, Schedule.this);
        courceDetail.setAdapter(myAdapter);
    }
}