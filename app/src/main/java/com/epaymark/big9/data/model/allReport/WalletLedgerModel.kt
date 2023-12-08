package com.epaymark.big9.data.model.allReport

import com.google.gson.annotations.SerializedName

class WalletLedgerModel {

    @SerializedName("Description"   ) var Description  : String?         = null
    @SerializedName("response_code" ) var responseCode : Int?            = null
    @SerializedName("data"          ) var data         : ArrayList<WalletLedgerData> = arrayListOf()
    @SerializedName("timestamp"     ) var timestamp    : String?         = null



}

data class WalletLedgerData(

    @SerializedName("refillid" ) var refillid : String? = null,
    @SerializedName("iid"      ) var iid      : String? = null,
    @SerializedName("insdate"  ) var insdate  : String? = null,
    @SerializedName("type"     ) var type     : String? = null,
    @SerializedName("status"   ) var status   : String? = null,
    @SerializedName("amount"   ) var amount   : String? = null,
    @SerializedName("curramt"  ) var curramt  : String? = null

)



