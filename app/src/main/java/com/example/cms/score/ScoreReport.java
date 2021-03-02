package com.example.cms.score;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.example.cms.R;
import com.example.cms.schedule.Cource;
import com.example.cms.schedule.GridAdapter;

import java.util.ArrayList;
import java.util.List;


public class ScoreReport extends AppCompatActivity {

    private ListView scoreDetail;
    private List<Score> scoreList;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_report);

        scoreDetail = findViewById(R.id.scoreDetail);

        Score score1 = new Score("超级无敌计算机原理", "100");
        Score score2 = new Score("大学数学", "59");

        scoreList = new ArrayList<>();

        scoreList.add(score1);
        scoreList.add(score2);

        listAdapter = new ListAdapter(this, scoreList);
        scoreDetail.setAdapter(listAdapter);
    }
}