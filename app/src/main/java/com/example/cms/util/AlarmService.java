package com.example.cms.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmService extends Service {


    private static final String TAG = "Log日志";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = dateFormat.parse(dateFormat.format(date));
            d2 = dateFormat.parse(intent.getStringExtra("start_time"));
            Log.e(TAG, "现在："+String.valueOf(d1));
            Log.e(TAG, "课程："+String.valueOf(d2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //这是课程与当前时间相差的毫秒数
        long diff = d2.getTime() - d1.getTime();
        Log.e(TAG, "课程与当前时间相差: " + diff);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //SystemClock.elapsedRealtime()（表示1970年1月1日0点至今所经历的时间）加上当前时间与课程相差时间，再提前了20分钟（1200000毫秒）
        long triggerAtTime = SystemClock.elapsedRealtime() + diff - 1200000 ;
        //此处设置开启AlarmReceiver这个Service
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);

    }
} 