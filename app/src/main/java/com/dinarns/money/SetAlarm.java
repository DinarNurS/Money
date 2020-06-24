package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private static final int ALARM_REQUEST_CODE = 134;
    //set interval notifikasi 1 menit
    private int interval_seconds = 60;
    private int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        Intent alarmIntent = new Intent(SetAlarm.this, AlarmReceiver.class);
    pendingIntent = PendingIntent.getBroadcast(SetAlarm.this, ALARM_REQUEST_CODE, alarmIntent, 0);
}
    public void startAlarmManager(View v) {
        //set waktu sekarang berdasarkan interval
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, interval_seconds);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //set alarm manager dengan memasukkan waktu yang telah dikonversi menjadi milliseconds
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "AlarmManager Start.", Toast.LENGTH_SHORT).show();
    }
    //Stop/Cancel alarm manager
    public void stopAlarmManager(View v) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        //close existing/current notifications
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        //jika app ini mempunyai banyak notifikasi bisa di cancelAll()
        //notificationManager.cancelAll();
        Toast.makeText(this, "AlarmManager Stopped by User.", Toast.LENGTH_SHORT).show();
    }

}