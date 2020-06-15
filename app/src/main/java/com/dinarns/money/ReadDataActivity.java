package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadDataActivity extends AppCompatActivity {

    //Deklarasi Variable
    private  RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase database;
    private ArrayList<pengeluaran> daftarPengeluaran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);
       // getSupportActionBar().setTitle("Daftar Pengeluaran");


        //Inisialisasi ArrayList
        daftarPengeluaran = new ArrayList<>();

        //Inisialisasi RoomDatabase
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "dbPengeluaran").allowMainThreadQueries().build();

        /*
         * Mengambil data pengeluaran dari Database
         * lalu memasukannya ke kedalam ArrayList (daftarpengeluaran)
         */
        daftarPengeluaran.addAll(Arrays.asList(database.pengeluaranDAO().readDataPengeluaran()));

        //Agar ukuran RecyclerView tidak berubah
       // recyclerview.requestFocus();
        recyclerview = findViewById(R.id.dataItem);

       recyclerview.setHasFixedSize(true);

        //Menentukan bagaimana item pada RecyclerView akan tampil
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        //Mamasang adapter pada RecyclerView
        adapter = new RecyclerPengeluaranAdapter(daftarPengeluaran, ReadDataActivity.this);
        recyclerview.setAdapter(adapter);


    }
}
