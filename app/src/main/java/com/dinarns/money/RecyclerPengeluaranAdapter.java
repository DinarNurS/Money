package com.dinarns.money;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;

public class RecyclerPengeluaranAdapter extends RecyclerView.Adapter<RecyclerPengeluaranAdapter.ViewHolder> {


    //Deklarasi Variable
    private ArrayList<pengeluaran> daftarpengeluaran;
    private AppDatabase appDatabase;
    private Context context;

    public RecyclerPengeluaranAdapter(ArrayList<pengeluaran> daftarpengeluaran, Context context) {

        //Inisialisasi data yang akan digunakan
        this.daftarpengeluaran = daftarpengeluaran;
        this.context = context;
        appDatabase = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class, "dbPengeluaran").allowMainThreadQueries().build();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //Deklarasi View yang akan digunakan
        private TextView  judul,jumlah;
        private CardView item;

        ViewHolder(View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.judul);
            jumlah = itemView.findViewById(R.id.jumlah);
            item = itemView.findViewById(R.id.cvMain);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inisialisasi Layout Item untuk RecyclerView
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("recyclerview")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {

        //Deklarasi Variable untuk mendapatkan data dari Database melalui Array
        final String getjudul = daftarpengeluaran.get(position).getjudul();
        final String getjumlah = daftarpengeluaran.get(position).getjumlah();


        //Menampilkan data berdasarkan posisi Item dari RecyclerView

        holder.judul.setText(getjudul);
        holder.jumlah.setText(getjumlah);
        //menampilkan ketika ditekn muncul pilihan edit dan delete

        //detail
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengeluaran pengeluaran = appDatabase.pengeluaranDAO()
                        .selectDetailPengeluaran(daftarpengeluaran.get(position).getid());
                context.startActivity(new Intent(context, DetailDataActivity.class).putExtra("detail", pengeluaran));
            }
        });

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] menuPilihan = {"Edit", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Pilih Aksi")
                        .setItems(menuPilihan, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        onEditData(position, context);
                                        break;

                                    case 1:
                                        onDeleteData(position);
                                        break;
                                }
                            }
                        });
                dialog.create();
                dialog.show();
                return true;
            }
        });


    }

    //Menghapus Data dari Room Database yang dipilih oleh user
    private void onDeleteData(int position){
        appDatabase.pengeluaranDAO().deletePengeluaran(daftarpengeluaran.get(position));
        daftarpengeluaran.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, daftarpengeluaran.size());
        Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
    }
        //Mengirim Data yang akan diedit dari ArrayList berdasarkan posisi item pada RecyclerView
        private void onEditData(int position, Context context){
            context.startActivity(new Intent(context, EditActivity.class).putExtra("data", daftarpengeluaran.get(position)));
            ((Activity)context).finish();
        }


    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return daftarpengeluaran.size();
    }
}