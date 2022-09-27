package com.example.projectwestay.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusResponse(
    @SerializedName("status")
    val status: Int
)
