package com.epaymark.big9.repository


import androidx.paging.PagingSource
import com.epaymark.big9.utils.table.DataEntity
import com.epaymark.big9.utils.table.TableDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TableRepository @Inject constructor(private val tableDao: TableDao) {
    /*fun getDataPaged(): PagingSource<Int, DataEntity> {
        return tableDao.getData()
    }*/
    /*@OptIn(ExperimentalPagingApi::class)
    fun getDataPaged(): Pager<Int, DataEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { tableDao.getData() },
            remoteMediator = null // You can use RemoteMediator if data is loaded from the network
        )
    }*/
    fun getDataPaged(): PagingSource<Int, DataEntity> {
        // Assuming tableDao.getData() returns a PagingSource<Int, DataEntity>
        return tableDao.getData()
    }
    suspend fun insertData(data: DataEntity) {
        tableDao.insertData(data)
    }

    suspend fun deleteAllData(): Int {
        return tableDao.deleteAllData()
    }





    suspend fun getDataInRange(startIndex: Int, endIndex: Int): List<DataEntity?>? {
        return withContext(Dispatchers.IO) {
            tableDao.getDataInRange(startIndex, endIndex)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}