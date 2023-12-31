package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class PostPaidMobileTranspherModel {

    @SerializedName("userid"     ) var userid     : String? = null
    @SerializedName("status"     ) var status     : String? = null
    @SerializedName("refillid"   ) var refillid   : String? = null
    @SerializedName("mobileno"   ) var mobileno   : String? = null
    @SerializedName("amount"     ) var amount     : String? = null
    @SerializedName("curramt"    ) var curramt    : String? = null
    @SerializedName("operator"   ) var operator   : String? = null
    @SerializedName("operatorid" ) var operatorid : String? = null
    var image:String?=""

}


