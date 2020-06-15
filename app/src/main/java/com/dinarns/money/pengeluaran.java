package com.dinarns.money;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tPengeluaran") //Membuat tabel baru dengan nama "mahasiswa"
    public class pengeluaran implements Serializable {

        //Membuat kolom ID
        @NonNull
        @PrimaryKey
        @ColumnInfo(name = "id")
        public
        String Id;

        //Membuat kolom judul
        @ColumnInfo(name = "judul")
        public
        String Judul;


        //tanggal
        @ColumnInfo(name = "tanggal")
        public
        String Tanggal;

         //jumlah
         @ColumnInfo(name = "jumlah")
         public
         String Jumlah;

        //Method untuk mengambil data id
        @NonNull
        public String getid() {
            return Id;
        }

        //Method untuk memasukan data id
        public void setid(@NonNull String id) {
            this.Id = id;
        }

        //Method untuk mengambil data judul
        public String getjudul() {
            return Judul;
        }

        //Method untuk memasukan data judul
          public void setjudul(String judul) {
        this.Judul = judul;
    }


    //Method untuk mengambil data tanggal
        public String gettanggal() {
            return Tanggal;
        }

        //Method untuk memasukan data Tanggal
        public void settanggal(String tanggal) {
            this.Tanggal = tanggal;
        }

        //Method untuk mengambil data jumlah
        public String getjumlah() {
            return Jumlah;
        }

        //Method untuk memasukan data jumlah
        public void setjumlah(String jumlah) {
            this.Jumlah = jumlah;
        }

    }

