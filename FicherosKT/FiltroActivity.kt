package com.example.speed

// Archivo 1: FiltroActivity.kt


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FiltroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filtro)

        val buttonAtras = findViewById<Button>(R.id.button20)
        val textMarca = findViewById<TextView>(R.id.textView)
        val textModelo = findViewById<TextView>(R.id.textView11)
        val editMarca = findViewById<EditText>(R.id.editTextText4)
        val editModelo = findViewById<EditText>(R.id.editTextText3)
        val buttonGuardar = findViewById<Button>(R.id.button3)

        buttonAtras.setOnClickListener {
            finish() // Cierra la actividad actual
        }

        buttonGuardar.setOnClickListener {
            val marca = editMarca.text.toString()
            val modelo = editModelo.text.toString()
            // Aquí puedes guardar o procesar los datos
        }
    }
}