package com.epaymark.big9.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.epaymark.big9.R
import com.epaymark.big9.databinding.ActivityNetworkBinding

import com.epaymark.epay.utils.common.MethodClass
import com.epaymark.epay.utils.helpers.NoNetworkReceiver
import com.epaymark.epay.utils.`interface`.NetworkConnectionListener
import com.epaymark.epay.utils.interfaces.NetworkCallBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkActivity : AppCompatActivity() {

    lateinit var nonetwork : NoNetworkReceiver
    private lateinit var binding: ActivityNetworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = ActivityNetworkBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_network)
        /*nonetwork = NoNetworkReceiver(object : NetworkConnectionListener {
            override fun onNetworkConnection(connectionStatus: Boolean) {
                if (connectionStatus) {
                    finish()
                }
            }

        })*/
        nonetwork=NoNetworkReceiver(object : NetworkCallBack{
            override fun hasNetwork(hasnetwork: Boolean) {
                if(hasnetwork){
                   finish()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(nonetwork, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(nonetwork)
    }
}