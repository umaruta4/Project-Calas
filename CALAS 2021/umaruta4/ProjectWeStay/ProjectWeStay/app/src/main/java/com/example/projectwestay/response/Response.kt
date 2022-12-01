package com.example.projectwestay.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Response<T> {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("response")
    @Expose
    val response: T? = null
}