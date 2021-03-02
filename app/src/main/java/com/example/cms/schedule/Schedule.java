package com.example.cms.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.cms.R;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    private GridView courceDetail;
    private List<Cource> courceList;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        courceDetail = findViewById(R.id.courceDetail);

        Cource cource1 = new Cource("超级无敌计算机原理", "一教108");
        Cource cource2 = new Cource("大学数学", "一教208");

        courceList = new ArrayList<>();

        courceList.add(cource1);
        courceList.add(cource2);

        gridAdapter = new GridAdapter(this, courceList);
        courceDetail.setAdapter(gridAdapter);
    }
}