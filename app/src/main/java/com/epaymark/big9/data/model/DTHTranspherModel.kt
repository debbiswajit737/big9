package com.epaymark.big9.data.model

import com.google.gson.annotations.SerializedName

data class DTHTranspherModel(
    @SerializedName("userid"        ) var userid       : String? = null,
    @SerializedName("status"        ) var status       : String? = null,
    @SerializedName("refillid"      ) var refillid     : String? = null,
    @SerializedName("subscriber_id" ) var subscriberId : String? = null,
    @SerializedName("amount"        ) var amount       : String? = null,
    @SerializedName("curramt"       ) var curramt      : String? = null,
    @SerializedName("operator"      ) var operator     : String? = null,
    @SerializedName("operatorid"    ) var operatorid   : String? = null

)


