package com.epaymark.big9.data.model

import com.google.gson.annotations.SerializedName

data class CreditCardVerifyOtpModel(
    @SerializedName("userid"      ) var userid      : String? = null,
    @SerializedName("ref_id"      ) var refId       : String? = null,
    @SerializedName("transDate"   ) var transDate   : String? = null,
    @SerializedName("utr"         ) var utr         : String? = null,
    @SerializedName("intid"       ) var intid       : String? = null,
    @SerializedName("network"     ) var network     : String? = null,
    @SerializedName("usercurrbal" ) var usercurrbal : String? = null,
    @SerializedName("amount"      ) var amount      : String? = null,
    @SerializedName("status"      ) var status      : String? = null

)
