package com.example.speed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val btnPiezas: Button = findViewById(R.id.btn_piezas)
        val btnMapa: Button = findViewById(R.id.btn_mapa)
        val btnMiCoche: Button = findViewById(R.id.btn_micoche)

        // Navegar a la actividad de Piezas
        btnPiezas.setOnClickListener {
            val intent = Intent(this, PiezasActivity::class.java)
            startActivity(intent)
        }

        // Navegar a la actividad de Mapa
        btnMapa.setOnClickListener {
            val intent = Intent(this, MapasActivity::class.java)
            startActivity(intent)
        }

        // Navegar a la actividad de Login
        btnMiCoche.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

