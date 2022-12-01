package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.adapter.HomestayAdapter
import com.example.projectwestay.callback.OnClickListener
import com.example.projectwestay.databinding.ActivityHomePenyewaBinding
import com.example.projectwestay.response.HomestayResponse
import com.example.projectwestay.response.Response
import com.example.projectwestay.session.Session
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class HomePenyewaActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomePenyewaBinding
    private var userId: Int? = null
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
        binding = ActivityHomePenyewaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userId = Session.read(applicationContext, "user_id", "0")?.toInt()
        initializeView()
        initializeListener()
    }

    fun initializeView(){
        val call = service.ambilSemuaHomestayId(userId!!)
        call.enqueue(object: Callback<Response<Array<HomestayResponse>>> {
            override fun onResponse(call: Call<Response<Array<HomestayResponse>>>, response: retrofit2.Response<Response<Array<HomestayResponse>>>){
                var error = response.body()?.error as Boolean
                if (error) {
                    var message = response.body()?.message as String
                    Log.e("Server Error : ", message)
                    return
                }

                var responseData = response.body()?.response as Array<HomestayResponse>
                var adapter = HomestayAdapter(applicationContext, R.layout.layout_homestay, responseData)
                adapter.setOnClickListener(object: OnClickListener<HomestayResponse>{
                    override fun onClick(view: View, item: HomestayResponse){
                        val intent = Intent(applicationContext, DetailHomestayPenyewaActivity::class.java)
                        intent.putExtra("homestay_id", item.homestayId)
                        startActivity(intent)
                        finish()
                    }
                })
                binding.listViewHomescroll.adapter = adapter
            }

            override fun onFailure(call: Call<Response<Array<HomestayResponse>>>, t: Throwable){
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                Log.w("GAGAL API : ", t.message.toString())
            }
        })
    }

    fun initializeListener(){
        binding.buttonTambah.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, AddRumahActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.imageViewAccount.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, AccountDetailPenyewaActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}