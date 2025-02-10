package com.example.speed


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.speed.Coches

class CochesAdapter(
    private val cochesList: List<Coches>
) : RecyclerView.Adapter<CochesAdapter.CochesViewHolder>() {

    // ViewHolder que representa cada elemento de la lista
    class CochesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marcaTextView: TextView = itemView.findViewById(R.id.marcaTextView)
        val modeloTextView: TextView = itemView.findViewById(R.id.modeloTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CochesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.coche_item, parent, false)
        return CochesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CochesViewHolder, position: Int) {
        val coche = cochesList[position]
        holder.marcaTextView.text = coche.marca
        holder.modeloTextView.text = coche.modelo
    }

    override fun getItemCount(): Int {
        return cochesList.size
    }
}
