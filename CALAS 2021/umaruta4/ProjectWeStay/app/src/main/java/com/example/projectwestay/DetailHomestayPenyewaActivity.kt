package com.example.projectwestay

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.databinding.ActivityDetailHomestayPenyewaBinding
import com.example.projectwestay.response.HomestayResponse
import com.example.projectwestay.response.Response
import com.example.projectwestay.response.StatusResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailHomestayPenyewaActivity : AppCompatActivity() {
    private var homestayId: Int? = null
    private lateinit var data: HomestayResponse
    private lateinit var binding: ActivityDetailHomestayPenyewaBinding

    var gson = GsonBuilder()
        .setLenient()
        .create()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(APIUrl.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service = retrofit.create(APIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHomestayPenyewaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //data = intent.getParcelableExtra<HomestayResponse>("data") as HomestayResponse
        homestayId = intent.getIntExtra("homestay_id", 0)

        initializeView()
        initializeListener()
    }

    fun initializeView(){
        val call = service.ambilSatuHomestay(homestayId!!)
        call.enqueue(object: Callback<Response<HomestayResponse>> {
            override fun onResponse(call: Call<Response<HomestayResponse>>, response: retrofit2.Response<Response<HomestayResponse>>){
                var error = response.body()?.error as Boolean
                if (error) {
                    var message = response.body()?.message as String
                    Log.e("Server Error : ", message)
                    return
                }

                var responseData = response.body()?.response as HomestayResponse
                data = responseData
                val encodedImage = data?.thumbnail
                val decodedBytes : ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0,decodedBytes.size)
                binding.imageViewThumbnail.setImageBitmap(decodedBitmap)
                binding.textViewNamaHomestay.text = data?.namaHomestay
                binding.textViewLokasi.text = data?.lokasi
                binding.textViewFasilitas.text = data?.fasilitas
                binding.textViewJenisKamar.text = data?.jenisKamar
                binding.textViewHarga.text = "Rp." + data?.harga.toString() + "/malam"
            }

            override fun onFailure(call: Call<Response<HomestayResponse>>, t: Throwable){
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                Log.w("GAGAL API : ", t.message.toString())
            }
        })
    }


    fun initializeListener(){
        binding.imageViewHome.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, HomePenyewaActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.imageViewAccount.setOnClickListener{v->
            val intent = Intent(applicationContext, AccountDetailPenyewaActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.buttonHapus.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                var call = service.hapusHomestay(data.homestayId)

                call.enqueue(object: Callback<Response<StatusResponse>> {
                    override fun onResponse(call: Call<Response<StatusResponse>>, response: retrofit2.Response<Response<StatusResponse>>){
                        val hapusRumahResponse = response.body()

                        if (hapusRumahResponse == null){
                            Log.d("Responsenya : ", "NULL!!")
                            Toast.makeText(applicationContext, "Gagal menghapus data!", Toast.LENGTH_LONG).show()
                            return
                        }

                        var error = response.body()?.error as Boolean
                        if (error) {
                            var message = response.body()?.message as String
                            Log.e("Server Error : ", message)
                            return
                        }
                        Log.d("MASUK : ", error.toString())
                        var responseData = response.body()?.response as StatusResponse
                        var status = responseData.status
                        if (status == 0){
                            Toast.makeText(applicationContext, "Maaf, data gagal dihapus!!", Toast.LENGTH_LONG).show()
                            return
                        }

                        Toast.makeText(applicationContext, "Data berhasil dihapus!!", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, HomePenyewaActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<Response<StatusResponse>>, t: Throwable){
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        Log.w("GAGAL API : ", t.message.toString())
                    }
                })
            }
        })
    }


}