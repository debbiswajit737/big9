package com.epaymark.big9.data.model.allReport

import com.google.gson.annotations.SerializedName

class WalletSettleReportModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<DataValue8> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class DataValue8(

    @SerializedName("AccountID") var AccountID: String? = null,
    @SerializedName("LastTransactionAmount") var LastTransactionAmount: String? = null,
    @SerializedName("LastTransactionTime") var LastTransactionTime: String? = null,
    @SerializedName("AmountMode") var AmountMode: String? = null,
    @SerializedName("PaymentBYId") var PaymentBYId: String? = null,
    @SerializedName("ReceiveById") var ReceiveById: String? = null,
    @SerializedName("curBal_sender") var curBalSender: String? = null,
    @SerializedName("curBal_receiver") var curBalReceiver: String? = null,
    @SerializedName("senderID") var senderID: String? = null,
    @SerializedName("senderMobileNo") var senderMobileNo: String? = null,
    @SerializedName("senderName") var senderName: String? = null,
    @SerializedName("receiverID") var receiverID: String? = null,
    @SerializedName("receiverMobileNo") var receiverMobileNo: String? = null,
    @SerializedName("receiverName") var receiverName: String? = null

)



