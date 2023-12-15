package com.epaymark.big9.utils.table

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.epaymark.big9.data.model.allReport.WalletLedgerData
import dagger.hilt.android.HiltAndroidApp


@Database(entities = [WalletLedgerData::class], version = 2, exportSchema = false)
abstract class TableDatabase : RoomDatabase() {
    abstract fun tableDao(): TableDao

    companion object {
        @Volatile
        private var INSTANCE: TableDatabase? = null

        fun getDatabase(context: Context): TableDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TableDatabase::class.java,
                    "big9database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}