package com.example.speed

import android.util.Log
import com.example.speed.Coches
import com.google.firebase.database.*

class GlobalRepository {

    private val database = FirebaseDatabase.getInstance()

    // Obtener lista de coches desde Firebase
    fun getCoches(callback: (List<Coches>) -> Unit) {
        val cochesRef = database.getReference("Coches/Coches")
        cochesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cochesList = mutableListOf<Coches>()
                for (cocheSnapshot in snapshot.children) {
                    val coche = cocheSnapshot.getValue(Coches::class.java)
                    if (coche != null) {
                        cochesList.add(coche)
                    }
                }
                callback(cochesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GlobalRepository", "Error: ${error.message}")
            }
        })
    }

    // Obtener lista de piezas desde Firebase
    fun getPiezas(callback: (List<Piezas>) -> Unit) {
        val piezasRef = database.getReference("Piezas/piezas")
        piezasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val piezasList = mutableListOf<Piezas>()
                for (piezaSnapshot in snapshot.children) {
                    val pieza = piezaSnapshot.getValue(Piezas::class.java)
                    if (pieza != null) {
                        piezasList.add(pieza)
                    }
                }
                callback(piezasList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GlobalRepository", "Error: ${error.message}")
            }
        })
    }

    // Obtener lista de talleres desde Firebase
    fun getTalleres(callback: (List<Talleres>) -> Unit) {
        val talleresRef = database.getReference("Talleres/talleres")
        talleresRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val listaTalleres = snapshot.children.mapNotNull { it.getValue(Talleres::class.java) }
                callback(listaTalleres)
            } else {
                callback(emptyList())
            }
        }.addOnFailureListener {
            callback(emptyList())
        }
    }




    fun getMarcas(callback: (List<String>) -> Unit) {
        val marcasRef = database.getReference("Coches/Coches/marcas")
        marcasRef.get().addOnSuccessListener { snapshot ->
            val marcas = snapshot.children.mapNotNull { it.key }
            Log.d("GlobalRepository", "Marcas obtenidas: $marcas")
            callback(marcas)
        }.addOnFailureListener {
            Log.e("GlobalRepository", "Error obteniendo marcas: ${it.message}")
            callback(emptyList())
        }
    }

    fun getModelos(marca: String, callback: (List<String>) -> Unit) {
        val modelosRef = database.getReference("Coches/Coches/marcas/$marca/modelos")
        modelosRef.get().addOnSuccessListener { snapshot ->
            val modelos = snapshot.children.mapNotNull { it.getValue(String::class.java) }
            Log.d("GlobalRepository", "Modelos para $marca obtenidos: $modelos")
            callback(modelos)
        }.addOnFailureListener {
            Log.e("GlobalRepository", "Error obteniendo modelos para $marca: ${it.message}")
            callback(emptyList())
        }
    }




    fun guardarCoche(marca: String, modelo: String, callback: (Boolean) -> Unit) {
        // Crear un objeto Coche con la marca y modelo proporcionados
        val coche = Coches(marca = marca, modelo = modelo)

        // Guardar el coche en la referencia de Firebase
        val cochesRef = database.getReference("Coches/Coches") // Definir la referencia aquí
        cochesRef.push().setValue(coche)
            .addOnSuccessListener { callback(true) } // Llamar al callback con éxito
            .addOnFailureListener { callback(false) } // Llamar al callback con error
        }
}