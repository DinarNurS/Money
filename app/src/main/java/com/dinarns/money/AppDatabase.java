package com.dinarns.money;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
 * Membuat Class Database
 * Entity yang digunakan adalah pengeluaran.class
 * Version = 1
 */
@Database(entities = {pengeluaran.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //Untuk mengakses Database menggunakan method abstract
    public abstract PengeluaranDAO pengeluaranDAO();
}
