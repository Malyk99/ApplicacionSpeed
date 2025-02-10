package com.example.speed

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var dbHelper: UsuarioDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        // Inicializar vistas
        nameEditText = findViewById(R.id.etName)
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        registerButton = findViewById(R.id.btnSignup)

        // Inicializar base de datos
        dbHelper = UsuarioDatabaseHelper(this)

        // Lógica para el botón de registro
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val nuevoUsuario = Usuario(nombre = name, email = email, contrasena = password)

                val resultado = dbHelper.registrarUsuario(nuevoUsuario)

                if (resultado != -1L) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad después de registrar al usuario
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Imagen para volver atrás (lo haces dentro de este método)
        val navigationIcon = findViewById<ImageView>(R.id.navigationIcon)
        navigationIcon.setOnClickListener {
            onBackPressed() // Llama al método de retroceso al hacer clic en la imagen
        }
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
