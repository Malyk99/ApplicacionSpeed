package com.example.speed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.SharedPreferences

class MiCocheActivity : AppCompatActivity() {

    private lateinit var rvPiezas: RecyclerView
    private lateinit var btnGuardar: Button
    private lateinit var btnAñadir: Button
    private lateinit var btnModificar: Button
    private lateinit var btnEliminar: Button
    private lateinit var tvNombreCoche: TextView
    private lateinit var imagenCoche: ImageView
    private lateinit var spinnerCoches: Spinner

    private var piezas = arrayListOf<String>() // Piezas inicializadas vacías
    private val piezasDisponibles = arrayListOf("Espejo", "Faros", "Parachoques", "Asientos")

    private val cochesMasUsados = arrayListOf(
        Coche("Seat León", R.drawable.seat_leon),
        Coche("Volkswagen Golf", R.drawable.golf),
        Coche("Renault Clio", R.drawable.renaultclio),
        Coche("Hyundai Tucson", R.drawable.hyundai_tucson),
        Coche("Toyota Corolla", R.drawable.toyota_corolla)
    )

    private lateinit var cocheSeleccionado: Coche
    private lateinit var piezasAdapter: PiezaAdapterCoche

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.micoche)

        // Inicializar vistas
        spinnerCoches = findViewById(R.id.spinnerCoches)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnAñadir = findViewById(R.id.btnAñadir)
        btnModificar = findViewById(R.id.btnModificar)
        btnEliminar = findViewById(R.id.btnEliminar)
        tvNombreCoche = findViewById(R.id.tvNombreCoche)
        imagenCoche = findViewById(R.id.imagenCoche)
        rvPiezas = findViewById(R.id.rvPiezas)

        sharedPreferences = getSharedPreferences("MiCochePrefs", Context.MODE_PRIVATE)

        // Recuperar las piezas del almacenamiento persistente o inicializarlas con las por defecto
        val storedPiezas = sharedPreferences.getStringSet("piezas", mutableSetOf<String>())?.toList() ?: listOf()
        if (storedPiezas.isNotEmpty()) {
            piezas = ArrayList(storedPiezas)
        } else {
            piezas = arrayListOf("Motor", "Llantas", "Frenos") // Piezas por defecto si no hay almacenamiento
        }

        // Configurar Spinner
        configurarSpinner()

        // Configurar RecyclerView
        piezasAdapter = PiezaAdapterCoche(piezas) { pieza -> mostrarDetallesPieza(pieza) }
        rvPiezas.layoutManager = LinearLayoutManager(this)
        rvPiezas.adapter = piezasAdapter

        // Configurar botones
        configurarBotones()

        // Configurar OnClickListener para la imagenCoche
        imagenCoche.setOnClickListener { mostrarDetalleCoche(cocheSeleccionado) }
    }

    private fun configurarSpinner() {
        // Recuperar el coche almacenado o el primero por defecto
        val storedCocheIndex = sharedPreferences.getInt("selectedCocheIndex", 0)
        cocheSeleccionado = cochesMasUsados[storedCocheIndex]

        // Establecer la imagen y el texto inicial del coche
        mostrarDetalleCoche(cocheSeleccionado)

        // Cargar los nombres de los coches al Spinner
        val nombresCoches = cochesMasUsados.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresCoches)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCoches.adapter = adapter

        spinnerCoches.setSelection(storedCocheIndex)

        spinnerCoches.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cocheSeleccionado = cochesMasUsados[position]
                mostrarDetalleCoche(cocheSeleccionado)

                // Guardar la selección actual del coche en SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putInt("selectedCocheIndex", position)
                editor.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun mostrarDetalleCoche(coche: Coche) {
        tvNombreCoche.text = coche.nombre
        imagenCoche.setImageResource(coche.imageResId)
    }

    private fun configurarBotones() {
        btnGuardar.setOnClickListener {
            guardarPiezas()
            Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
        }

        btnAñadir.setOnClickListener { mostrarDialogoAñadir() }

        btnModificar.setOnClickListener { mostrarDialogoModificar() }

        btnEliminar.setOnClickListener { mostrarDialogoEliminar() }
    }

    private fun guardarPiezas() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("piezas", piezas.toSet())
        editor.apply()
    }

    private fun mostrarDialogoAñadir() {
        if (piezasDisponibles.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Selecciona una pieza para añadir")
                .setItems(piezasDisponibles.toTypedArray()) { _, which ->
                    val pieza = piezasDisponibles[which]
                    piezas.add(pieza)
                    piezasDisponibles.removeAt(which)
                    piezasAdapter.notifyItemInserted(piezas.size - 1)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            Toast.makeText(this, "No hay piezas disponibles para añadir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoModificar() {
        if (piezas.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Selecciona una pieza para modificar")
                .setItems(piezas.toTypedArray()) { _, which ->
                    val pieza = piezas[which]
                    val input = EditText(this)
                    input.setText(pieza)

                    AlertDialog.Builder(this)
                        .setTitle("Modificar pieza")
                        .setView(input)
                        .setPositiveButton("Aceptar") { _, _ ->
                            val nuevoNombre = input.text.toString()
                            if (nuevoNombre.isNotEmpty() && nuevoNombre !in piezas) {
                                piezas[which] = nuevoNombre
                                piezasAdapter.notifyItemChanged(which)
                            } else {
                                Toast.makeText(this, "Nombre inválido o ya existente", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            Toast.makeText(this, "No hay piezas para modificar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoEliminar() {
        if (piezas.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Selecciona una pieza para eliminar")
                .setItems(piezas.toTypedArray()) { _, which ->
                    val pieza = piezas[which]
                    piezas.removeAt(which)
                    piezasDisponibles.add(pieza)
                    piezasAdapter.notifyItemRemoved(which)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            Toast.makeText(this, "No hay piezas para eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDetallesPieza(pieza: String) {
        Toast.makeText(this, "Detalle de la pieza: $pieza", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (isDialogOpen()) {
            // Si existe un diálogo abierto, ciérralo
            getCurrentDialog()?.dismiss()
        } else {
            super.onBackPressed()  // Llama a la funcionalidad de retroceso normal
        }
    }

    // Método para verificar si un diálogo está abierto
    private fun isDialogOpen(): Boolean {
        val currentDialog = this.supportFragmentManager.findFragmentByTag("android:switcher")
        return currentDialog != null
    }

    // Método para obtener el diálogo actual
    private fun getCurrentDialog(): AlertDialog? {
        return this.supportFragmentManager.findFragmentByTag("android:switcher") as? AlertDialog
    }
}



