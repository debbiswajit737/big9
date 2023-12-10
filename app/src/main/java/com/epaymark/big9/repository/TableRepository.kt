package com.epaymark.big9.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.epaymark.big9.utils.table.DataEntity
import com.epaymark.big9.utils.table.TableDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class TableRepository @Inject constructor(private val tableDao: TableDao) {
    /*fun getDataPaged(): PagingSource<Int, DataEntity> {
        return tableDao.getData()
    }*/
    @OptIn(ExperimentalPagingApi::class)
    fun getDataPaged(): Pager<Int, DataEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { tableDao.getData() },
            remoteMediator = null // You can use RemoteMediator if data is loaded from the network
        )
    }
    suspend fun insertData(data: DataEntity) {
        tableDao.insertData(data)
    }

    suspend fun deleteAllData(): Int {
        return tableDao.deleteAllData()
    }
}