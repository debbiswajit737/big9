package com.epaymark.big9.utils.helpers

import android.content.Context
import com.epaymark.big9.utils.helpers.Constants.EPAY_SHAREDFREFFRENCE
import com.epaymark.big9.utils.helpers.Constants.ISLogin
import com.epaymark.big9.utils.helpers.Constants.TEST
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreff @Inject constructor(@ApplicationContext private val context: Context?){

    private var settings = context?.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
    fun setTestData(phn: String?) {

        context?.let {
//            val settings =
//                context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            val editor = settings?.edit()
            phn?.let { phone ->
                editor?.putString(TEST, phone)
            }
            editor?.apply()
        }
    }

    fun getTestData(): String {
        var phn: String? = null
        context?.let {
//            val shrdprf =
//                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            phn = settings?.getString(TEST, "0")
        }
        return phn ?: "0"

    }


    fun setLoginData() {

        context?.let {
             settings =
                context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            val editor = settings?.edit()

            editor?.putBoolean(ISLogin, true)

            editor?.apply()
        }
    }

    fun checkIsLogin(): Boolean {
        var isLogin: Boolean? = null
        context?.let {
            settings =
                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            isLogin = settings?.getBoolean(ISLogin, false)
        }
        return isLogin ?: false

    }

    fun clearUserData() {
        context?.let {
            settings =
                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
                settings?.edit()?.clear()?.apply()
        }

    }
}