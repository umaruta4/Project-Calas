package com.example.projectwestay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectwestay.databinding.ActivityAccountDetailPenyewaBinding
import com.example.projectwestay.session.Session

class AccountDetailPenyewaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailPenyewaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailPenyewaBinding.inflate(layoutInflater)
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
            val intent = Intent(applicationContext, HomePenyewaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}