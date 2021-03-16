package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.cms.adapter.ScoreListAdapter;


public class ScoreReport extends AppCompatActivity {

    private ListView scoreDetail;
//    private List<Score> scoreList;
    private ScoreListAdapter scoreListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_score_report);

//        scoreDetail = findViewById(R.id.scoreDetail);
//
//        Score score1 = new Score("超级无敌计算机原理", "100");
//        Score score2 = new Score("大学数学", "59");
//
//        scoreList = new ArrayList<>();
//
//        scoreList.add(score1);
//        scoreList.add(score2);
//
//        scoreListAdapter = new ScoreListAdapter(this, scoreList);
//        scoreDetail.setAdapter(scoreListAdapter);
    }
}