package com.example.smartlock

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper

class splashAct : AppCompatActivity() {
    private fun goToMainActivity() {
        Intent(this, login::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.splashkit)

        Handler(Looper.getMainLooper()).postDelayed({
            goToMainActivity()
          }, 2000L)
    }

}