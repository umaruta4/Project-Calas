package com.example.projectwestay.API

import com.example.projectwestay.response.*
import retrofit2.http.*
import javax.net.ssl.SSLEngineResult

interface APIService {

    @FormUrlEncoded
    @POST("login")
    open fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): retrofit2.Call<Response<LoginResponse>>

    @FormUrlEncoded
    @POST("registerPengguna")
    open fun registerPengguna(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("email") email: String,
        @Field("no_ktp") noKTP: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): retrofit2.Call<Response<StatusResponse>>

    @FormUrlEncoded
    @POST("registerPenyewa")
    open fun registerPenyewa(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("email") email: String,
        @Field("no_ktp") noKTP: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): retrofit2.Call<Response<StatusResponse>>

    @GET("ambilSemuaHomestay")
    open fun ambilSemuaHomestay():retrofit2.Call<Response<Array<HomestayResponse>>>

    @GET("ambilSatuHomestay")
    open fun ambilSatuHomestay(
        @Query("homestay_id") homestayId: Int
    ):retrofit2.Call<Response<HomestayResponse>>

    @FormUrlEncoded
    @POST("tambahRumah")
    open fun tambahRumah(
        //@Body insert: ExampleInsert
        @Field("user_id") userId: Int,
        @Field("nama_homestay") namaHomestay: String,
        @Field("fasilitas") fasilitas: String,
        @Field("jenis_kamar") jenisKamar: String,
        @Field("lokasi") lokasi: String,
        @Field("harga") harga: Int,
        @Field("thumbnail") thumbnail: String,
        @Field("nama_thumbnail") namaThumbnail: String

    ): retrofit2.Call<Response<StatusResponse>>

    @FormUrlEncoded
    @POST("tambahPesanan")
    open fun tambahPesanan(
        @Field("homestay_id") homestayId: Int,
        @Field("user_id") userId: Int,
        @Field("check_in") checkIn: String,
        @Field("check_out") checkOut: String,
        @Field("catatan") catatan: String
    ): retrofit2.Call<Response<StatusResponse>>

    @FormUrlEncoded
    @POST("hapusHomestay")
    open fun hapusHomestay(
        @Field("homestay_id") homestayId: Int
    ): retrofit2.Call<Response<StatusResponse>>

    @GET("ambilSemuaHomestayId")
    open fun ambilSemuaHomestayId(
        @Query("user_id") homestayId: Int
    ):retrofit2.Call<Response<Array<HomestayResponse>>>
}