package com.epaymark.big9.data.viewMovel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.epaymark.big9.repository.TableRepository
import com.epaymark.big9.utils.table.DataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TableViewModel @Inject constructor(private val repository: TableRepository) : ViewModel() {

    /*val data: Flow<PagingData<DataEntity>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { repository.getDataPaged() }
    ).flow.cachedIn(viewModelScope)*/
    /*val data: LiveData<PagingData<DataEntity>> = liveData {
        repository.getDataPaged().flow.collectLatest { emit(it) }
    }*/
    /*val data: LiveData<PagingData<DataEntity>> = liveData {
        repository.getDataPaged().flow
            .cachedIn(viewModelScope) // Ensure to use cachedIn to prevent illegal state exception
            .collectLatest { emit(it) }
    }*/
    /*private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val data: LiveData<PagingData<DataEntity>> = liveData {
        _isLoading.postValue(true) // Show loader when starting to load data
        repository.getDataPaged().flow
            .cachedIn(viewModelScope)
            .collectLatest {
                _isLoading.postValue(false) // Hide loader when data is loaded
                emit(it)
            }
    }*/


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val data: LiveData<PagingData<DataEntity>> = liveData {
        _isLoading.postValue(true) // Show loader when starting to load data

        val pagingConfig = PagingConfig(
            pageSize = 20, // Number of items to load per page
            prefetchDistance = 3, // The number of items to prefetch in advance
            enablePlaceholders = false
        )

        val pager = Pager(
            config = pagingConfig,
            pagingSourceFactory = { repository.getDataPaged() }
        )

        pager.flow
            .cachedIn(viewModelScope)
            .collectLatest {
                _isLoading.postValue(false) // Hide loader when data is loaded
                emit(it)
            }
    }


    fun insertData(data2: DataEntity) {
        viewModelScope.launch {
            repository.insertData(data2)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            val rowsDeleted = repository.deleteAllData()

            if (rowsDeleted > 0) {
                // Rows were deleted successfully
                // Perform any additional actions or UI updates
            } else {
                // No rows were deleted (or an error occurred)
                // Handle the situation accordingly
            }
        }
    }

    fun getDataInRange(startIndex: Int, endIndex: Int): List<DataEntity?>? {
        var data:List<DataEntity?>? =null
            viewModelScope.launch {

                data= repository.getDataInRange(startIndex, endIndex)
            data?.forEach {
                // Log or process the data
                Log.d("TAG_eee", "showRecyclerView: ${it?.desc}")
            }

        }
        return data
    }



}