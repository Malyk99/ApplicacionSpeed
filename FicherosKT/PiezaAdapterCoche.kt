package com.example.speed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PiezaAdapterCoche(private val piezas: List<String>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<PiezaAdapterCoche.PiezaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiezaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pieza, parent, false)
        return PiezaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PiezaViewHolder, position: Int) {
        val pieza = piezas[position]
        holder.bind(pieza, onClick)
    }

    override fun getItemCount(): Int = piezas.size

    class PiezaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPieza: TextView = itemView.findViewById(R.id.tvNombrePieza)

        fun bind(pieza: String, onClick: (String) -> Unit) {
            tvPieza.text = pieza
            itemView.setOnClickListener { onClick(pieza) }
        }
    }
}




