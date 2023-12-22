package com.epaymark.big9.utils.`interface`

import android.content.Intent




interface SmsBroadcastReceiverListener {
    fun onSuccess(intent: Intent?)

    fun onFailure()
}