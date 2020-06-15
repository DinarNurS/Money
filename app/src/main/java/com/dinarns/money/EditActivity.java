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

public class EditActivity extends AppCompatActivity {

    //Deklarasi Variable
    private EditText judul, tanggal, jumlah;
    private AppDatabase database;
    private Button bSimpan;
    private pengeluaran pengeluaran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
}
