package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectwestay.databinding.ActivityAccountDetailPenggunaBinding
import com.example.projectwestay.session.Session

class AccountDetailPenggunaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailPenggunaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailPenggunaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeListener()
    }

    fun initializeListener(){
        binding.buttonLogout.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                Session.close(applicationContext)
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.imageViewHome.setOnClickListener{v ->
            val intent = Intent(applicationContext, HomePenggunaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}