package com.epaymark.epay.utils.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.epaymark.epay.utils.common.MethodClass
import com.epaymark.epay.utils.`interface`.NetworkConnectionListener
import com.epaymark.epay.utils.interfaces.NetworkCallBack


class NoNetworkReceiver(val ntwrklstnr: NetworkCallBack) : BroadcastReceiver(){
    override fun onReceive(ctx: Context?, p1: Intent?) {
        ctx?.let {

            if (!MethodClass.check_networkconnection(it)){
           // if (!MethodClass.checkNetworkConnection(it)){
                ntwrklstnr?.hasNetwork(false)

            }else{
                ntwrklstnr?.hasNetwork(true)
            }
        }

    }


}