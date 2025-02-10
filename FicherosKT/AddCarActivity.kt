package com.example.speed

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCarActivity : AppCompatActivity() {

    private lateinit var spinnerMarcas: Spinner
    private lateinit var spinnerModelos: Spinner
    private lateinit var btnGuardarCoche: Button

    private val repository = GlobalRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        // Inicializar los componentes
        spinnerMarcas = findViewById(R.id.spinnerMarca)
        spinnerModelos = findViewById(R.id.spinnerModelo)
        btnGuardarCoche = findViewById(R.id.btnGuardarCoche)

        // Configurar los spinners
        configurarSpinners()

        // Configurar botón de guardar coche
        btnGuardarCoche.setOnClickListener {
            val marcaSeleccionada = spinnerMarcas.selectedItem as? String
            val modeloSeleccionado = spinnerModelos.selectedItem as? String

            if (marcaSeleccionada != null && modeloSeleccionado != null) {
                repository.guardarCoche(marcaSeleccionada, modeloSeleccionado) { success ->
                    if (success) {
                        Toast.makeText(this, "Coche guardado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Finalizar la actividad y volver a la anterior
                    } else {
                        Toast.makeText(this, "Error al guardar el coche", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Selecciona una marca y un modelo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarSpinners() {
        repository.getMarcas { marcas ->
            if (marcas.isNotEmpty()) {
                val marcasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
                marcasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMarcas.adapter = marcasAdapter

                spinnerMarcas.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                        val marcaSeleccionada = marcas[position]
                        repository.getModelos(marcaSeleccionada) { modelos ->
                            val modelosAdapter = ArrayAdapter(this@AddCarActivity, android.R.layout.simple_spinner_item, modelos)
                            modelosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerModelos.adapter = modelosAdapter
                        }
                    }

                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
                })
            } else {
                Toast.makeText(this, "No se encontraron marcas.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}