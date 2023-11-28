package com.epaymark.big9.utils.common

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.epaymark.big9.R

import com.epaymark.big9.ui.activity.RegActivity
import com.epaymark.big9.utils.helpers.SharedPreff

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject


object MethodClass {
    fun custom_loader(ctx: Context?, msg: String?): Dialog? {
        var dialog: Dialog? = null
        ctx?.let {
            dialog = Dialog(it)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.custom_loader)

            val text = dialog?.findViewById<View>(R.id.text_dialog) as TextView
            text.text = msg
        }
        return dialog



    }
 fun get_error_method(err: ResponseBody): String {
         val jsonErr = JSONObject(err.string())
         val gson = Gson()
         val type = object : TypeToken<String>() {}.type
         val errorResponse: String? = gson.fromJson(jsonErr.toString(), type)
         return "".toString()
     }
    fun check_networkconnection(ctx: Context?): Boolean {
        var isConnected = false
        ctx?.let {
            val connectivityManager: ConnectivityManager? =
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetwork = connectivityManager?.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            isConnected = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        return isConnected

    }

    fun checkNetworkConnection(context: Context?): Boolean {
        var isConnected = false

        context?.let {
            val connectivityManager =
                it.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager?.activeNetwork
                val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)

                isConnected = networkCapabilities?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } ?: false
            } else {
                val activeNetworkInfo = connectivityManager?.activeNetworkInfo
                isConnected = activeNetworkInfo?.isConnectedOrConnecting == true
            }
        }

        return isConnected
    }
    fun Context.userLogout() {
        SharedPreff(this).clearUserData()
        val intent = Intent(this, RegActivity::class.java)
        intent.putExtra("isAlreadyLogin",true)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.startActivity(intent)
        // Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show()
    }
}



