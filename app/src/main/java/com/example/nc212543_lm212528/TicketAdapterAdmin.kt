package com.example.nc212543_lm212528

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapterAdmin(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketAdapterAdmin.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitulo: TextView = itemView.findViewById(R.id.textViewTituloA)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionA)
        val textViewUsuario: TextView = itemView.findViewById(R.id.textViewUsuarioA)
        val textViewFechaCreacion: TextView = itemView.findViewById(R.id.textViewFechaCreacionA)
        val textViewEstado: TextView = itemView.findViewById(R.id.textViewEstadoA)
        val textViewFechaFinalizacion: TextView =
            itemView.findViewById(R.id.textViewFechaFinalizacionA)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.textViewTitulo.text = ticket.titulo
        holder.textViewDescripcion.text = ticket.descripcion
        holder.textViewUsuario.text = ticket.autorTicket
        holder.textViewFechaCreacion.text = ticket.fechaCreacion
        holder.textViewEstado.text = ticket.estado
        holder.textViewFechaFinalizacion.text = ticket.fechaFinalizacion
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}
