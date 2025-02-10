package com.example.speed

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuarioDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_CONTRASENA TEXT NOT NULL,
                $COLUMN_ID_COCHE INTEGER,
                FOREIGN KEY($COLUMN_ID_COCHE) REFERENCES $TABLE_COCHES($COLUMN_ID_COCHE_REF)
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    fun registrarUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, usuario.nombre)
            put(COLUMN_EMAIL, usuario.email)
            put(COLUMN_CONTRASENA, usuario.contrasena)
            put(COLUMN_ID_COCHE, usuario.idCoche)
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    fun validarLogin(email: String, contrasena: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(COLUMN_ID, COLUMN_EMAIL, COLUMN_CONTRASENA), // Recupera columnas adicionales para verificar
            "$COLUMN_EMAIL = ? AND $COLUMN_CONTRASENA = ?",
            arrayOf(email, contrasena),
            null,
            null,
            null
        )

        val isValid = cursor.count > 0

        // Log para revisar datos
        if (cursor.moveToFirst()) {
            val dbEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val dbContrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))
            android.util.Log.d("LoginDebug", "Email en DB: $dbEmail, Contraseña en DB: $dbContrasena")
        } else {
            android.util.Log.d("LoginDebug", "No se encontraron resultados en la consulta")
        }

        cursor.close()
        return isValid
    }

    companion object {
        private const val DATABASE_NAME = "usuarios.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USUARIOS = "Usuarios"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_CONTRASENA = "contrasena"
        private const val COLUMN_ID_COCHE = "idCoche"
        private const val TABLE_COCHES = "Coches"
        private const val COLUMN_ID_COCHE_REF = "id"
    }
}