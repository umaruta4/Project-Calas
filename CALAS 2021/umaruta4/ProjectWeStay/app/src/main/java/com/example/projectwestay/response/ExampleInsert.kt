package com.example.projectwestay.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field


class ExampleInsert {

    @SerializedName("nama_homestay")
    lateinit var namaHomestay: String
    @SerializedName("fasilitas")
    lateinit var fasilitas: String
    @SerializedName("jenis_kamar")
    lateinit var jenisKamar: String
    @SerializedName("lokasi")
    lateinit var lokasi: String
    @SerializedName("harga")
    var harga: Int? = null
    @SerializedName("thumbnail")
    lateinit var thumbnail: String
    @SerializedName("nama_thumbnail")
    lateinit var namaThumbnail: String

     constructor(namaHomestay: String, fasilitas: String, jenisKamar: String, lokasi: String, harga: Int, thumbnail: String, namaThumbnail: String){
        this.namaHomestay = namaHomestay
        this.fasilitas = fasilitas
        this.jenisKamar = jenisKamar
        this.lokasi = lokasi
        this.harga = harga
        this.thumbnail = thumbnail
        this.namaThumbnail = namaThumbnail
    }
}