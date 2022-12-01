package com.example.projectwestay

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.adapter.HomestayAdapter
import com.example.projectwestay.callback.OnClickListener
import com.example.projectwestay.databinding.ActivityDetailHomestayPenggunaBinding
import com.example.projectwestay.response.HomestayResponse
import com.example.projectwestay.response.Response
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailHomestayPenggunaActivity : AppCompatActivity() {
    private lateinit var data: HomestayResponse
    private var homestayId: Int? = null
    private lateinit var binding : ActivityDetailHomestayPenggunaBinding

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
        binding = ActivityDetailHomestayPenggunaBinding.inflate(layoutInflater)
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
        binding.buttonPesan.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, FormPemesananActivity::class.java)
                data.thumbnail = ""
                intent.putExtra("data", data)
                startActivity(intent)
            }
        })

        binding.imageViewAccount.setOnClickListener{v->
            val intent = Intent(applicationContext, AccountDetailPenggunaActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.imageViewHome.setOnClickListener{v ->
            val intent = Intent(applicationContext, HomePenggunaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}