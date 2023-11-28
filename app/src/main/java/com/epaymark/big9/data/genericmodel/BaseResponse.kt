package com.epaymark.big9.data.genericmodel

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("response") var response: Response<T>? = Response()
)
