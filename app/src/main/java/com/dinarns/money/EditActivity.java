package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    // Constants for the notification actions buttons.
    private static final String ACTION_UPDATE_NOTIFICATION = "com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;

    //Deklarasi Variable
    private EditText judul, tanggal, jumlah;
    private AppDatabase database;
    private Button bSimpan;
    private pengeluaran pengeluaran;

    //notifikasi
    private NotificationManager mNotifyManager;
    private NotificationReceiver mReceiver = new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //NOTIFIKASI
        // Create the notification channel.
        createNotificationChannel();

        // Register the broadcast receiver to receive the update action from
        // the notification.
        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        judul = findViewById(R.id.judul);
        tanggal = findViewById(R.id.tanggal);
        jumlah = findViewById(R.id.Jumlah);
        bSimpan = findViewById(R.id.save);

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "dbPengeluaran").build();

        //Menampilkan data mahasiswa yang akan di edit
        getDataPengeluaran();

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengeluaran.setjudul(judul.getText().toString());
                pengeluaran.settanggal(tanggal.getText().toString());
                pengeluaran.setjumlah(jumlah.getText().toString());
                updateData(pengeluaran);

                //NOTIFIKASI
                sendNotification();
            }
        });
    }

    //Method untuk menapilkan data mahasiswa yang akan di edit
    private void getDataPengeluaran() {
        //Mendapatkan data dari Intent
        pengeluaran = (pengeluaran) getIntent().getSerializableExtra("data");

        judul.setText(pengeluaran.getjudul());
        tanggal.setText(pengeluaran.gettanggal());
        jumlah.setText(pengeluaran.getjumlah());
    }

    //Menjalankan method Update Data
    @SuppressLint("StaticFieldLeak")
    private void updateData(final pengeluaran pengeluaran){
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                //Menjalankan proses insert data
                return database.pengeluaranDAO().updatePengeluaran(pengeluaran);
            }

            @Override
            protected void onPostExecute(Integer status) {
                //Menandakan bahwa data berhasil disimpan
                Toast.makeText(EditActivity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditActivity.this, ReadDataActivity.class));
                finish();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditActivity.this, ReadDataActivity.class));
        finish();
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
