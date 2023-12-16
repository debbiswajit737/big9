package com.epaymark.big9.data.model

import com.google.gson.annotations.SerializedName

data class ChangeUserPasswordModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("timestamp") var timestamp: String? = null
)


