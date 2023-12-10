package com.epaymark.big9.di

import android.content.Context
import com.epaymark.big9.utils.table.TableDao
import com.epaymark.big9.utils.table.TableDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTableDao(tableDatabase: TableDatabase): TableDao {
        return tableDatabase.tableDao()
    }

    @Provides
    @Singleton
    fun provideTableDatabase(@ApplicationContext context: Context): TableDatabase {
        return TableDatabase.getDatabase(context)
    }
}