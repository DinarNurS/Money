package com.dinarns.money;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotifikasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        TextView textView= findViewById(R.id.text_view);
        String message= getIntent().getStringExtra("message");
        textView.setText(message);
    }
}
