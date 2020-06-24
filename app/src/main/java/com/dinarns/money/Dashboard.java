package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    Button bt_notif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bt_notif = findViewById(R.id.button4);
        bt_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= "Selamat datang diaplikasi kami, MY_MONEY ^_^" +
                        "Pada aplikasi my_money ini untuk tugas akhir matkul perangkat mobile lanjut";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        Dashboard.this
                )
                        .setSmallIcon(R.drawable.ic_message)
                        .setContentTitle("New Notification")
                        .setContentText(message)
                        .setAutoCancel(true);

                Intent intent= new Intent(Dashboard.this,NotifikasiActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);
                PendingIntent pendingIntent= PendingIntent.getActivity(Dashboard.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager= (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );
                notificationManager.notify(0,builder.build());
            }
        });
    }

    public void tambah(View view) {
        Intent intent = new Intent(Dashboard.this, MainActivity.class);
        startActivity(intent);
    }

    public void author(View view) {
        Intent intent = new Intent(Dashboard.this, Author.class);
        startActivity(intent);
    }

    public void add(View view) {
        Intent intent = new Intent(Dashboard.this, MainActivity.class);
        startActivity(intent);
    }


    public void kirim(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"dewikhoirunn@gmail.com"});

        intent.putExtra(Intent.EXTRA_SUBJECT, "Email dari Aplikasi My_Money");
        intent.putExtra(Intent.EXTRA_TEXT, "Tulis komentar : ");

        try {
            startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"));
        } catch (android.content.ActivityNotFoundException ex) {
            //do something else
        }
    }
}

