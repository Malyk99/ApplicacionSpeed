package com.example.speed

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        // Animación de rotación para el logo (opcional)
        val logo = findViewById<ImageView>(R.id.logoImageView)
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        logo.startAnimation(rotateAnimation)

        // Redirigir a la actividad principal después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 ms = 3 segundos
    }
}
