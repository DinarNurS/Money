package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class DetailDataActivity extends AppCompatActivity {

    private EditText getid, getjudul, gettanggal, getjumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        getid = findViewById(R.id.myID);
        getjudul = findViewById(R.id.myJudul);
        gettanggal = findViewById(R.id.myTanggal);
        getjumlah = findViewById(R.id.myJumlah);

        getDetailData();
    }

    //Mendapatkan data yang akan ditampilkan secara detail
    private void getDetailData(){
        pengeluaran pengeluaran = (pengeluaran) getIntent().getSerializableExtra("detail");

        //Menampilkan data Mahasiswa pada activity
        if(pengeluaran != null){
            getid.setText(pengeluaran.getid());
            getjudul.setText(pengeluaran.getjudul());
            gettanggal.setText(pengeluaran.gettanggal());
            getjumlah.setText(pengeluaran.getjumlah());

        }
    }
}
