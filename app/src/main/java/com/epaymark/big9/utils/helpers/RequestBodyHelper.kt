package com.epaymark.epay.utils.helpers

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import com.epaymark.epay.BuildConfig
import com.epaymark.epay.R
import com.google.gson.JsonObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.TimeZone

import javax.inject.Inject

class RequestBodyHelper @Inject constructor (@ApplicationContext val context: Context) {

    private fun getCommonProperty(): JsonObject {
        val propertyType = JsonObject()
        propertyType.addProperty("device_os", "and")
        propertyType.addProperty("appversion", BuildConfig.VERSION_NAME)
        return propertyType
    }

    fun getLoginRequest(username: String?,password:String?): JsonObject {
        val request = getCommonProperty()
        val timeZone = TimeZone.getDefault()
         val timezone = timeZone.id
        val appName = context.getString(R.string.app_name)
        val deviceModel = Build.MODEL
        val deviceName = Build.MODEL
        val deviceVersion = Build.VERSION.RELEASE


        request.addProperty("username", username)
        request.addProperty("password", password)
        request.addProperty("device_version", deviceVersion)
        request.addProperty("device_name", deviceName)
        request.addProperty("device_model", deviceModel)
        request.addProperty("appname", appName)
        //request.addProperty("device_token", appSettingsPref.getFCMToken())
        Log.d(TAG, request.toString())
        return request
    }
}