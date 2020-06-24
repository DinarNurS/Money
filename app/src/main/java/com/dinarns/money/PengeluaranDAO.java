package com.dinarns.money;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PengeluaranDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPengeluaran(pengeluaran pengeluaran);

    @Query("SELECT * FROM tPengeluaran")
    pengeluaran[] readDataPengeluaran();

    @Update
    int updatePengeluaran(pengeluaran pengeluaran);

    @Delete
    void deletePengeluaran(pengeluaran pengeluaran);

    @Query("SELECT * FROM tPengeluaran WHERE id = :Id LIMIT 1")
    pengeluaran selectDetailPengeluaran(String Id);

  //  @Query("SELECT COALESCE(sum(COALESCE(Jumlah,0)), 0) From tPengeluaran")
    //LiveData<Double> getTotal();
}