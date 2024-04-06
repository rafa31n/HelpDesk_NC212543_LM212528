package com.example.nc212543_lm212528

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
        val ticket = tickets[position]
        holder.textViewTitulo.text = ticket.titulo
        holder.textViewDescripcion.text = ticket.descripcion
        holder.textViewUsuario.text = "Autor ticket: " + ticket.autorTicket
        holder.textViewFechaCreacion.text = "Fecha creación: " + ticket.fechaCreacion
        holder.textViewEstado.text = "Estado: " + ticket.estado
        holder.textViewFechaFinalizacion.text = ticket.fechaFinalizacion
        holder.btnFinalizar.setOnClickListener {
            // Maneja la acción de finalizar el ticket
            finalizarTicket(ticket, holder)
        }
    }

    private fun finalizarTicket(ticket: Ticket, holder: ViewHolder) {
        FirebaseDatabase.getInstance().getReference("tickets")
            .child(ticket.numTicket.toString())
            .child("estado")
            .setValue("Finalizado")
            .addOnSuccessListener {
                // Actualización exitosa
                Toast.makeText(
                    holder.itemView.context,
                    "Estado del ticket actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                // Actualiza la vista del ticket
                holder.textViewFechaFinalizacion.text = "Fecha de finalización: " + getFechaActual()
                tickets.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }
            .addOnFailureListener {
                // Error en la actualización
                Toast.makeText(
                    holder.itemView.context,
                    "Error al actualizar el estado del ticket",
                    Toast.LENGTH_SHORT
                ).show()
            }

        //Cambiar fecha finalizacion
        val fechaActual = getFechaActual()

        FirebaseDatabase.getInstance().getReference("tickets")
            .child(ticket.numTicket.toString())
            .child("fechaFinalizacion")
            .setValue(fechaActual)
            .addOnSuccessListener {
                //
            }
            .addOnFailureListener {
                // Error en la actualización
                Toast.makeText(
                    holder.itemView.context,
                    "Error al actualizar el estado del ticket",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun getFechaActual(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}
