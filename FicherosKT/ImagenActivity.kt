package com.example.speed

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImagenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagen)

        val imageView = findViewById<ImageView>(R.id.imageView)
        // Configura acciones relacionadas con la imagen aquí
    }
}