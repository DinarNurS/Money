package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    //Deklarasi Variable
    private EditText id, judul, tanggal, jumlah;
    private AppDatabase database;
    private Button bSimpan, bLihatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.id);
        judul = findViewById(R.id.judul);
        tanggal = findViewById(R.id.tanggal);
        jumlah = findViewById(R.id.jumlah);
        bSimpan = findViewById(R.id.save);
        bSimpan.setOnClickListener(this);
        bLihatData = findViewById(R.id.show);
        bLihatData.setOnClickListener(this);

        //Inisialisasi dan memanggil Room Database
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dbPengeluaran") //Nama File Database yang akan disimpan
                .build();
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
                }
                break;
            case R.id.show:
                startActivity(new Intent(MainActivity.this, ReadDataActivity.class));
                break;
        }
    }
}



