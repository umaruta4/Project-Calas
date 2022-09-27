package com.example.projectwestay.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    /*
    * This field returns True when user is successfully
    * logged in and false when unsuccessful login
    * */
    @SerializedName("status")
    val status: Int,

    @SerializedName("level")
    val level: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("nama_lengkap")
    val namaLengkap: String
)
