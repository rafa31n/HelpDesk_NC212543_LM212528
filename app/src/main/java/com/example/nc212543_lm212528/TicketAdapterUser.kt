package com.example.nc212543_lm212528

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class TicketAdapterUser(private val tickets: MutableList<Ticket>) :
    RecyclerView.Adapter<TicketAdapterUser.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitulo: TextView = itemView.findViewById(R.id.textViewTituloU)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionU)
        val textViewFechaCreacion: TextView = itemView.findViewById(R.id.textViewFechaCreacionU)
        val textViewEstado: TextView = itemView.findViewById(R.id.textViewEstadoU)
        val textViewFechaFinalizacion: TextView =
            itemView.findViewById(R.id.textViewFechaFinalizacionU)
        val btnEliminar:TextView=itemView.findViewById(R.id.btnEliminar)
        val btnEditar:TextView=itemView.findViewById(R.id.btnEditar)

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
        holder.btnEditar.setOnClickListener {
            editarTicket(ticket,holder)
        }
        holder.btnEliminar.setOnClickListener {
            eliminarTicket(ticket,holder)

        }
    }
  private fun eliminarTicket(ticket: Ticket,holder: ViewHolder) {
        FirebaseDatabase.getInstance().getReference("tickets")
            .child(ticket.numTicket.toString())
            .removeValue()  // Directly remove the ticket
            .addOnSuccessListener {
                // Ticket eliminado exitosamente
                Toast.makeText(holder.itemView.context, "Ticket eliminado correctamente", Toast.LENGTH_SHORT).show()

                tickets.removeAt(holder.adapterPosition)

                notifyItemRemoved(holder.adapterPosition)
            }
            .addOnFailureListener {
                // Error al eliminar el ticket
                Toast.makeText(holder.itemView.context, "Error al eliminar el ticket", Toast.LENGTH_SHORT).show()
            }
    }
    private fun editarTicket(ticket: Ticket,holder: ViewHolder){
        val intent=Intent(holder.itemView.context,EditarTicketActivity::class.java)
        intent.putExtra("ticket.numTicket",ticket.numTicket.toString())
        intent.putExtra("ticket.titulo",ticket.titulo)
        intent.putExtra("ticket.descripcion",ticket.descripcion)
        intent.putExtra("ticket.emailAutor",ticket.emailAutor)

        holder.itemView.context.startActivity(intent)
    }
    override fun getItemCount(): Int {
        return tickets.size
    }
}
