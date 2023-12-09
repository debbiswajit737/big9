package com.epaymark.big9.data.model.allReport

import com.google.gson.annotations.SerializedName

class commissionReportModel {

    @SerializedName("Description"   ) var Description  : String?         = null
    @SerializedName("response_code" ) var responseCode : String?            = null
    @SerializedName("data"          ) var data         : ArrayList<commissionReportData> = arrayListOf()
    @SerializedName("timestamp"     ) var timestamp    : String?         = null


}

data class commissionReportData(

    @SerializedName("opname" ) var opname : String? = null,
    @SerializedName("comm"   ) var comm   : String? = null,
    @SerializedName("type"   ) var type   : String? = null

)



