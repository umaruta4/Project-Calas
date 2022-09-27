package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.databinding.ActivityRegisterPenggunaBinding
import com.example.projectwestay.response.LoginResponse
import com.example.projectwestay.response.Response
import com.example.projectwestay.response.StatusResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterPenggunaActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterPenggunaBinding

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
        binding = ActivityRegisterPenggunaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeListener()
    }

    fun initializeListener(){
        binding.buttonDaftar.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                var namaLengkap: String = binding.editTextNamaLengkap.text.toString()
                var email: String = binding.editTextEmail.text.toString()
                var noKtp: String = binding.editTextNoKTP.text.toString()
                var username: String = binding.editTextUsername.text.toString()
                var password: String = binding.editTextPassword.text.toString()
                var confirmPassword: String = binding.editTextUlangiPassword.text.toString()

                if (!password.equals(confirmPassword)){
                    binding.editTextUlangiPassword.error = "Password tidak sama!"
                    return
                }

                if (!namaLengkap.matches(Regex("[a-zA-Z ]+"))){
                    binding.editTextNamaLengkap.error = "Nama tidak boleh mengandung selain huruf a sampai z!"
                    return
                }

                var call: Call<Response<StatusResponse>> = service.registerPengguna(namaLengkap, email, noKtp,username, password)

                call.enqueue(object: Callback<Response<StatusResponse>>{
                    override fun onResponse(call: Call<Response<StatusResponse>>, response: retrofit2.Response<Response<StatusResponse>>){
                        var error = response.body()?.error as Boolean
                        if (error) {
                            var message = response.body()?.message as String
                            Log.e("Server Error : ", message)
                            return
                        }

                        var responseData = response.body()?.response as StatusResponse
                        var status = responseData.status
                        if (status == 0){
                            Toast.makeText(applicationContext, "Tidak bisa daftar, karena username suda ada!", Toast.LENGTH_LONG).show()
                            return
                        }

                        Toast.makeText(applicationContext, "Berhasil didaftarkan!", Toast.LENGTH_LONG).show()
                        var intent = Intent(applicationContext, LoginActivity::class.java)
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