package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.databinding.ActivityLoginBinding
import com.example.projectwestay.response.LoginResponse
import com.example.projectwestay.response.Response
import com.example.projectwestay.session.Session
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityLoginBinding

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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeListener()
    }

    fun initializeListener(){
        binding.buttonMasuk.setOnClickListener (object: View.OnClickListener{
            override fun onClick(view: View){
                val username = binding.editTextUsername.text.toString()
                val password = binding.editTextPassword.text.toString()

                if (username.isEmpty()){
                    binding.editTextUsername.error = "Maaf username tidak boleh kosong!"
                    return
                }

                if (password.isEmpty()){
                    binding.editTextPassword.error = "Maaf password tidak boleh kosong!"
                    return
                }

                var call : Call<Response<LoginResponse>> = service.login(username, password)

                call.enqueue(object: Callback<Response<LoginResponse>> {
                    override fun onResponse(
                        call: Call<Response<LoginResponse>>,
                        response: retrofit2.Response<Response<LoginResponse>>
                    ) {
                        var error = response.body()?.error as Boolean
                        if (error) {
                            var message = response.body()?.message as String
                            Log.e("Server Error : ", message)
                            return
                        }

                        var responseData = response.body()?.response as LoginResponse
                        if (responseData.status == 0) {
                            Toast.makeText(
                                applicationContext,
                                "Maaf username atau password salah!",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }

                        var intent: Intent? = null
                        var level = responseData.level

                        Session.save(applicationContext, "user_id", responseData.userId.toString())
                        Session.save(applicationContext, "level", level)
                        Session.save(applicationContext, "nama_lengkap", responseData.namaLengkap)

                        if (level == "pengguna") {
                            intent = Intent(applicationContext, HomePenggunaActivity::class.java)
                        }

                        if (level == "penyewa") {
                            intent = Intent(applicationContext, HomePenyewaActivity::class.java)
                        }

                        if (level == null) {
                            Toast.makeText(
                                applicationContext,
                                "Ada masalah ketika login!",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }

                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<Response<LoginResponse>>, t: Throwable){
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        Log.w("GAGAL API : ", t.message.toString())
                    }
                })
            }
        })

        binding.buttonSignIn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View){
                val intent = Intent(applicationContext, PilihSignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}