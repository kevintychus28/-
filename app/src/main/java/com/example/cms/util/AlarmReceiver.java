package com.example.cms.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.cms.R;
import com.example.cms.activity.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    //当BroadcastReceiver接收到Intent广播时调用。
    @Override
    public void onReceive(Context context, Intent intent) {
        //实例化通知管理
        NotificationManager manager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        //点击通知时，跳转到MainActivity
//        Intent playIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //实例化通知，添加属性
        Notification notification = new Notification.Builder(context)
                .setAutoCancel(true)//通知被点击后，自动消失
                .setDefaults(NotificationCompat.DEFAULT_SOUND)//设置通知铃声
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setTicker("课程提醒")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//设置推送的发生时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置推送图标
                .setContentTitle("嘀嘀嘀！")
                .setContentText("距离上课时间还剩20分钟~")
//                .setContentIntent(pendingIntent)//点击跳转到MainActivity
                .build();
        //发出状态栏通知
        manager.notify(1, notification);
    }
}
