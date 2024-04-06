package com.example.nc212543_lm212528

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketAdapterAdmin(private val tickets: MutableList<Ticket>) :
    RecyclerView.Adapter<TicketAdapterAdmin.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitulo: TextView = itemView.findViewById(R.id.textViewTituloA)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionA)
        val textViewUsuario: TextView = itemView.findViewById(R.id.textViewUsuarioA)
        val textViewFechaCreacion: TextView = itemView.findViewById(R.id.textViewFechaCreacionA)
        val textViewEstado: TextView = itemView.findViewById(R.id.textViewEstadoA)
        val textViewFechaFinalizacion: TextView =
            itemView.findViewById(R.id.textViewFechaFinalizacionA)
        val btnFinalizar: Button = itemView.findViewById(R.id.btnFinalizar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (tickets.isEmpty()) {
            // Mostrar un mensaje cuando la lista de tickets está vacía
            showMessage(holder.itemView.context)
            holder.itemView.visibility = View.GONE
            return
        }

        val ticket = tickets[position]
        holder.itemView.visibility = View.VISIBLE
        holder.textViewTitulo.text = ticket.titulo
        holder.textViewDescripcion.text = ticket.descripcion
        holder.textViewUsuario.text = ticket.autorTicket
        holder.textViewFechaCreacion.text = ticket.fechaCreacion
        holder.textViewEstado.text = ticket.estado
        holder.textViewFechaFinalizacion.text = ticket.fechaFinalizacion
        holder.btnFinalizar.setOnClickListener {
            // Maneja la acción de finalizar el ticket
            finalizarTicket(ticket, holder)
        }
    }

    private fun finalizarTicket(ticket: Ticket, holder: ViewHolder) {
        val fechaActual = getFechaActual()
        val ticketsReferencia = FirebaseDatabase.getInstance().getReference("tickets")
            .child(ticket.numTicket.toString())
        ticketsReferencia.child("estado").setValue("Finalizado")
        ticketsReferencia.child("fechaFinalizacion").setValue(fechaActual)
        Toast.makeText(
            holder.itemView.context,
            "Ticket actualizados correctamente",
            Toast.LENGTH_SHORT
        ).show()
        tickets.removeAt(holder.adapterPosition)
        notifyItemRemoved(holder.adapterPosition)
    }

    fun getFechaActual(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    override fun getItemCount(): Int {
        return if (tickets.isEmpty()) 1 else tickets.size
    }

    private fun showMessage(context: Context) {
        // Muestra un mensaje cuando la lista de tickets está vacía
        Toast.makeText(context, "La lista de tickets está vacía", Toast.LENGTH_SHORT).show()
    }
}
