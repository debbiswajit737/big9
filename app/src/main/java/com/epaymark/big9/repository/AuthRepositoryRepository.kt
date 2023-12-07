package com.epaymark.big9.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.epaymark.big9.data.genericmodel.BaseResponse
import com.epaymark.big9.data.model.login.LoginResponse
import com.epaymark.big9.data.model.onBoading.DocumentUploadModel
import com.epaymark.big9.data.model.onBoading.RegForm
import com.epaymark.big9.data.model.otp.OtpResponse
import com.epaymark.big9.data.model.profile.profileResponse
import com.epaymark.big9.data.model.sample.Test
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetroApi
import javax.inject.Inject

class AuthRepositoryRepository @Inject constructor(private val api: RetroApi) {
    private val _formResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val formResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _formResponseLiveData


    suspend fun formReg(requestBody: RegForm) {
        _formResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.formReg(requestBody)
            _formResponseLiveData.postValue(ResponseState.create(response, "aa"))
            Log.e("TAG_error", "login:OK " + response)

        } catch (throwable: Throwable) {
            _formResponseLiveData.postValue(ResponseState.create(throwable))
            Log.e("TAG_error", "login: " + throwable.message)
        }

    }


    //Doc upload

    private val _docUploadResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val docUploadResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _docUploadResponseLiveData


    suspend fun docUpload(requestBody: DocumentUploadModel) {
        _docUploadResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.docUpload(requestBody)
            _docUploadResponseLiveData.postValue(ResponseState.create(response, "aa"))
            //Log.e("TAG_error", "login:OK "+response)

        } catch (throwable: Throwable) {
            _docUploadResponseLiveData.postValue(ResponseState.create(throwable))
            //Log.e("TAG_error", "login: "+throwable.message)
        }

    }


    //Login Model

    private val _loginResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<LoginResponse>>>()
    val loginResponseLiveData: LiveData<ResponseState<BaseResponse<LoginResponse>>>
        get() = _loginResponseLiveData


    suspend fun userLogin(loginModel: String) {
        _loginResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response =
                api.epayLogin(loginModel.replace("\n", "").replace("\r", ""), "loginModel")
            _loginResponseLiveData.postValue(ResponseState.create(response, "aa"))


        } catch (throwable: Throwable) {
            _loginResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //OTP

    private val _otpResponseLiveData =
        MutableLiveData<ResponseState<OtpResponse>>()
    val otpResponseLiveData: LiveData<ResponseState<OtpResponse>>
        get() = _otpResponseLiveData


    suspend fun otp(token: String, loginModel: String) {
        _otpResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.otpverify(token, loginModel.replace("\n", "").replace("\r", ""))
            _otpResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _otpResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //profile

    private val _profileResponseLiveData =
        MutableLiveData<ResponseState<profileResponse>>()
    val profileResponseLiveData: LiveData<ResponseState<profileResponse>>
        get() = _profileResponseLiveData


    suspend fun profile(token: String, loginModel: String) {
        _profileResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.profile(token, loginModel.replace("\n", "").replace("\r", ""))
            _profileResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _profileResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

}


