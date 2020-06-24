package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Constants for the notification actions buttons.
    private static final String ACTION_UPDATE_NOTIFICATION = "com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;

    //Deklarasi Variable
    private EditText id, judul, tanggal, jumlah;
    private AppDatabase database;
    private Button bSimpan, bLihatData,set;

    // Current background color untuk shared preferences
    private int mColor;
    private final String COLOR_KEY = "color";

    //notifikasi
    private NotificationManager mNotifyManager;
    private NotificationReceiver mReceiver = new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NOTIFIKASI
        // Create the notification channel.
        createNotificationChannel();

        // Register the broadcast receiver to receive the update action from
        // the notification.
        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));


        id = findViewById(R.id.id);
        judul = findViewById(R.id.judul);
        tanggal = findViewById(R.id.tanggal);
        jumlah = findViewById(R.id.jumlah);

        bSimpan = findViewById(R.id.save);
        bSimpan.setOnClickListener(this);

        bLihatData = findViewById(R.id.show);
        bLihatData.setOnClickListener(this);

        set = findViewById(R.id.set);
        set.setOnClickListener(this);

        //Inisialisasi dan memanggil Room Database
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dbPengeluaran") //Nama File Database yang akan disimpan
                .build();

        //Sharred Preferences

        mColor = ContextCompat.getColor(this, R.color.default_background);

        // Restore the saved instance state.
        if (savedInstanceState != null) {

            mColor = savedInstanceState.getInt(COLOR_KEY);
            bSimpan.setBackgroundColor(mColor);
            bLihatData.setBackgroundColor(mColor);
            set.setBackgroundColor(mColor);
        }


    }

    //Menjalankan method Insert Data
    @SuppressLint("StaticFieldLeak")
    private void insertData(final pengeluaran pengeluaran) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                //Menjalankan proses insert data
                return database.pengeluaranDAO().insertPengeluaran(pengeluaran);
            }

            @Override
            protected void onPostExecute(Long status) {
                //Menandakan bahwa data berhasil disimpan
                Toast.makeText(MainActivity.this, "Status Row " + status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

  @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:

                //Mengecek Data NIM dan Nama
                if (id.getText().toString().isEmpty() || judul.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "ID atau Judul TIdak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    //Membuat Instance/Objek Dari Class Entity pengeluaran
                    pengeluaran data = new pengeluaran();

                    //Memasukan data yang diinputkan user pada database
                    data.setid(id.getText().toString());
                    data.setjudul(judul.getText().toString());
                    data.settanggal(tanggal.getText().toString());
                    data.setjumlah(jumlah.getText().toString());


                    insertData(data);

                    //Mengembalikan EditText menjadi seperti semula\
                    id.setText("");
                    judul.setText("");
                    tanggal.setText("");
                    jumlah.setText("");

                    //NOTIFIKASI
                    sendNotification();
                }
                break;
            case R.id.show:
                startActivity(new Intent(MainActivity.this, ReadDataActivity.class));
                break;
            case R.id.set:
                startActivity(new Intent(MainActivity.this, SetAlarm.class));
                break;
        }
    }

    //Shared Preferences
    public void changeBackground(View view) {
        int color = ((ColorDrawable) view.getBackground()).getColor();
        bSimpan.setBackgroundColor(mColor);
        bLihatData.setBackgroundColor(mColor);
        set.setBackgroundColor(mColor);
        mColor = color;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(COLOR_KEY, mColor);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            getString(R.string.notification_channel_name),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (getString(R.string.notification_channel_description));

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * OnClick method for the "Notify Me!" button.
     * Creates and delivers a simple notification.
     */
    public void sendNotification() {

        // Sets up the pending intent to update the notification.
        // Corresponds to a press of the Update Me! button.
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        // Build the notification with all of the parameters using helper
        // method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        // Add the action button using the pending intent.
        notifyBuilder.addAction(R.drawable.ic_check,
                getString(R.string.update), updatePendingIntent);

        // Deliver the notification.
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder() {

        // Set up the pending intent that is delivered when the notification
        // is clicked.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification with all of the parameters.
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_logo)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }


 public class NotificationReceiver extends BroadcastReceiver {

     public NotificationReceiver() {
     }

     @Override
     public void onReceive(Context context, Intent intent) {
     }
 }
}



