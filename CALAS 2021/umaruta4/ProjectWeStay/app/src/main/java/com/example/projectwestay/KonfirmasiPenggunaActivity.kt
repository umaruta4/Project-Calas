package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.databinding.ActivityKonfirmasiPenggunaBinding
import com.example.projectwestay.response.HomestayResponse
import com.example.projectwestay.response.Response
import com.example.projectwestay.response.StatusResponse
import com.example.projectwestay.session.Session
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class KonfirmasiPenggunaActivity : AppCompatActivity() {
    private lateinit var data: HomestayResponse
    private lateinit var tanggalCheckin: Calendar
    private lateinit var tanggalCheckout: Calendar
    private lateinit var catatan: String
    private var pemesanId: Int? = null
    private var timeCheckin: Long? = null
    private var timeCheckout: Long? = null

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
    private val timeFormat = SimpleDateFormat("hh:mm", Locale.CANADA)

    var gson = GsonBuilder()
        .setLenient()
        .create()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(APIUrl.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service = retrofit.create(APIService::class.java)

    private lateinit var binding: ActivityKonfirmasiPenggunaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonfirmasiPenggunaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pemesanId = Session.read(applicationContext, "user_id", "0")!!.toInt()
        data = intent.getParcelableExtra<HomestayResponse>("data") as HomestayResponse
        timeCheckin = intent.getLongExtra("check_in", 0) as Long
        timeCheckout = intent.getLongExtra("check_out", 0) as Long
        catatan = intent.getStringExtra("catatan") as String
        tanggalCheckin = Calendar.getInstance()
        tanggalCheckout = Calendar.getInstance()
        tanggalCheckin.timeInMillis = timeCheckin as Long
        tanggalCheckout.timeInMillis = timeCheckout as Long

        initializeView()
        initializeListener()
    }

    fun initializeView(){
        var namaLengkap = Session.read(applicationContext, "nama_lengkap", "")
        var diff = timeCheckout!! - timeCheckin!!
        var daysDiff: Long = TimeUnit.MILLISECONDS.toDays(diff)
        binding.textViewNama.text = namaLengkap
        binding.textViewFasilitas.text = data.fasilitas
        binding.textViewCheckin.text = dateFormat.format(tanggalCheckin.time)
        binding.textViewCheckout.text = dateFormat.format(tanggalCheckout.time)
        binding.textViewCatatan.text = catatan
        binding.textViewHarga.text = "Rp."+(data.harga * daysDiff).toString()
    }

    fun initializeListener(){
        binding.buttonKonfirmasi.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val checkin = dateFormat.format(tanggalCheckin.time)
                val checkout = dateFormat.format(tanggalCheckout.time)
                var call = service.tambahPesanan(data.homestayId, pemesanId!!, checkin, checkout, catatan)

                call.enqueue(object: Callback<Response<StatusResponse>> {
                    override fun onResponse(call: Call<Response<StatusResponse>>, response: retrofit2.Response<Response<StatusResponse>>){
                        val tambahPesananResponse = response.body()

                        if (tambahPesananResponse == null){
                            Log.d("Responsenya : ", "NULL!!")
                            Toast.makeText(applicationContext, "Gagal memasukkan data!", Toast.LENGTH_LONG).show()
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
                            Toast.makeText(applicationContext, "Maaf, data gagal dimasukkan!", Toast.LENGTH_LONG).show()
                            return
                        }

                        Toast.makeText(applicationContext, "Data berhasil dimasukkan!", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, HomePenggunaActivity::class.java)
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

        binding.buttonBatal.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, HomePenggunaActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}