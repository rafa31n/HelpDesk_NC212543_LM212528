package com.example.nc212543_lm212528

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapterUser(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketAdapterUser.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitulo: TextView = itemView.findViewById(R.id.textViewTituloU)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionU)
        val textViewFechaCreacion: TextView = itemView.findViewById(R.id.textViewFechaCreacionU)
        val textViewEstado: TextView = itemView.findViewById(R.id.textViewEstadoU)
        val textViewFechaFinalizacion: TextView =
            itemView.findViewById(R.id.textViewFechaFinalizacionU)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.textViewTitulo.text = ticket.titulo
        holder.textViewDescripcion.text = ticket.descripcion
        holder.textViewFechaCreacion.text = ticket.fechaCreacion
        holder.textViewEstado.text = ticket.estado
        holder.textViewFechaFinalizacion.text = ticket.fechaFinalizacion
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}
