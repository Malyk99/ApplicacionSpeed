package com.example.speed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PiezasActivity : AppCompatActivity() {

    private lateinit var rvPiezas: RecyclerView
    private val repository = GlobalRepository() // Instancia del repositorio global

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.piezas)

        // Inicializar RecyclerView
        rvPiezas = findViewById(R.id.rvPiezas)
        rvPiezas.layoutManager = LinearLayoutManager(this)

        // Cargar piezas y configurar RecyclerView
        cargarPiezas()

        // Configurar el botón para ir a la actividad de agregar coche
        findViewById<View>(R.id.btnAnadirCoche).setOnClickListener {
            irAAgregarCoche()
        }
    }

    private fun irAAgregarCoche() {
        val intent = Intent(this, AddCarActivity::class.java)
        startActivity(intent)
    }


    /**
     * Cargar piezas desde Firebase y configurar el RecyclerView
     */
    private fun cargarPiezas() {
        repository.getPiezas { listaPiezas ->
            if (listaPiezas.isNotEmpty()) {
                rvPiezas.adapter = PiezasAdapter(this, listaPiezas) { piezaSeleccionada ->
                    mostrarDialogoTalleres(piezaSeleccionada)
                }
            } else {
                Toast.makeText(this, "No se encontraron piezas.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun mostrarDialogoTalleres(pieza: Piezas) {
        val vistaDialogo = layoutInflater.inflate(R.layout.dialog_talleres_piezas, null)
        val spinnerTalleres = vistaDialogo.findViewById<Spinner>(R.id.spinnerTalleres)

        // Obtener todos los talleres desde el repositorio
        repository.getTalleres { listaTalleres ->
            if (listaTalleres.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(this, "No hay talleres disponibles.", Toast.LENGTH_SHORT).show()
                }
                return@getTalleres
            }

            // Crear una lista con los nombres de los talleres e indicar si tienen stock
            val talleresConStockInfo = listaTalleres.map { taller ->
                val tieneStock = taller.piezas.any { piezasStock ->
                    piezasStock.idPieza == pieza.nombre && piezasStock.stock
                }
                "${taller.nombre} - ${if (tieneStock) "Con stock" else "Sin stock"}"
            }

            // Configurar el Spinner con los nombres de los talleres y su estado de stock
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, talleresConStockInfo)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            runOnUiThread {
                spinnerTalleres.adapter = adapter

                // Mostrar el diálogo con el Spinner
                AlertDialog.Builder(this)
                    .setTitle("Talleres disponibles para '${pieza.nombre}'")
                    .setView(vistaDialogo)
                    .setPositiveButton("Cerrar", null)
                    .show()
            }
        }
    }
}