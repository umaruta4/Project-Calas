package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectwestay.databinding.ActivityPilihSignInBinding

class PilihSignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPilihSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihSignInBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        initializeListener()
    }

    fun initializeListener(){
        binding.buttonSignInPengguna.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View){
                var intent = Intent(applicationContext, RegisterPenggunaActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.buttonSignInPenyewa.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View){
                var intent = Intent(applicationContext, RegisterPenyewaActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}