package com.example.projectwestay.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomestayResponse(

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("homestay_id")
    val homestayId: Int,

    @SerializedName("nama_homestay")
    val namaHomestay: String,

    @SerializedName("fasilitas")
    val fasilitas: String,

    @SerializedName("jenis_kamar")
    val jenisKamar: String,

    @SerializedName("harga")
    val harga: Int,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("thumbnail")
    var thumbnail: String,

    @SerializedName("nama_thumbnail")
    val namaThumbnail: String
): Parcelable
