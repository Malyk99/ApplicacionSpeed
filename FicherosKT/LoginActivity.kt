package com.example.speed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var dbHelper: UsuarioDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Inicializar las vistas
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)
        signupButton = findViewById(R.id.btnSignup)

        // Inicializar la base de datos
        dbHelper = UsuarioDatabaseHelper(this)

        // Lógica para el botón de iniciar sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Validar las credenciales en la base de datos
                val isValid = dbHelper.validarLogin(email, password)

                if (isValid) {
                    // Si las credenciales son correctas, ir a MiCocheActivity
                    val intent = Intent(this,MiCocheActivity::class.java)
                    startActivity(intent)
                    finish() // Para que no se quede en la pantalla de login
                } else {
                    // Si las credenciales son incorrectas, mostrar un mensaje
                    Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            }

        }

        // Lógica para el botón de registrarse
        signupButton.setOnClickListener {
            // Ir a la actividad de registro
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
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