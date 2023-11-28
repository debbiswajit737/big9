package com.epaymark.big9.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.epaymark.big9.data.genericmodel.BaseResponse
import com.epaymark.big9.data.model.sample.Test
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetroApi
import com.google.gson.JsonObject
import javax.inject.Inject

class DeliveryOptionsRepository  @Inject constructor(private val api : RetroApi) {

    private val _loginResponseLiveData =
        MutableLiveData< ResponseState<BaseResponse<Test>>>()
    val loginResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _loginResponseLiveData



    suspend fun login(requestBody: JsonObject) {
        _loginResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.login(requestBody)
            _loginResponseLiveData.postValue(ResponseState.create(response,"aa"))
            Log.e("TAG_error", "login:OK "+response)

        } catch (throwable: Throwable) {
            _loginResponseLiveData.postValue(ResponseState.create(throwable))
            Log.e("TAG_error", "login: "+throwable.message)
        }

    }
}


