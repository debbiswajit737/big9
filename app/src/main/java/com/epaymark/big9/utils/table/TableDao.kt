package com.epaymark.big9.utils.table

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: DataEntity)

    @Query("SELECT * FROM data_table ORDER BY id ASC")
    fun getData(): PagingSource<Int, DataEntity>

    @Query("DELETE FROM data_table")
    suspend fun deleteAllData(): Int
}