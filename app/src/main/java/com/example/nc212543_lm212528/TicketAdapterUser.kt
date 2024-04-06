package com.example.nc212543_lm212528

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TicketAdapterUser(private val tickets: MutableList<Ticket>) :

    RecyclerView.Adapter<TicketAdapterUser.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitulo: TextView = itemView.findViewById(R.id.textViewTituloU)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionU)
        val textViewFechaCreacion: TextView = itemView.findViewById(R.id.textViewFechaCreacionU)
        val textViewEstado: TextView = itemView.findViewById(R.id.textViewEstadoU)
        val textViewFechaFinalizacion: TextView =
            itemView.findViewById(R.id.textViewFechaFinalizacionU)
        val btnEliminar: TextView = itemView.findViewById(R.id.btnEliminar)
        val btnEditar: TextView = itemView.findViewById(R.id.btnEditar)
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
        holder.textViewFechaCreacion.text = "Fecha creación: " + ticket.fechaCreacion
        holder.textViewEstado.text = "Estado: " + ticket.estado
        holder.textViewFechaFinalizacion.text = "Fecha de finalización: " + ticket.fechaFinalizacion

        // Verificar si el estado del ticket está marcado como finalizado
        if (ticket.estado == "Finalizado") {
            // Si el ticket está finalizado, ocultar el botón de editar
            holder.btnEditar.visibility = View.GONE
        } else {
            // Si no está finalizado, asegurarse de que el botón de editar esté visible
            holder.btnEditar.visibility = View.VISIBLE

            // Configurar el listener del botón de editar
            holder.btnEditar.setOnClickListener {
                editarTicket(ticket, holder)
            }
        }

        if (holder.textViewFechaFinalizacion.text.equals(null)) {
            holder.textViewFechaFinalizacion.visibility = View.GONE
        }

        // Configurar el listener del botón de eliminar
        holder.btnEliminar.setOnClickListener {
            eliminarTicket(ticket, holder)
        }
    }

    private fun eliminarTicket(ticket: Ticket, holder: ViewHolder) {
        FirebaseDatabase.getInstance().getReference("tickets")
            .child(ticket.numTicket.toString())
            .removeValue()  // Directly remove the ticket
            .addOnSuccessListener {
                // Ticket eliminado exitosamente
                Toast.makeText(
                    holder.itemView.context,
                    "Ticket eliminado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                tickets.removeAt(holder.adapterPosition)

                notifyItemRemoved(holder.adapterPosition)
            }
            .addOnFailureListener {
                // Error al eliminar el ticket
                Toast.makeText(
                    holder.itemView.context,
                    "Error al eliminar el ticket",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun editarTicket(ticket: Ticket, holder: ViewHolder) {
        val user = FirebaseAuth.getInstance().currentUser
        var nombreUsuario: String = ""
        var correoUsuario: String = ""
        var rolUsuario: String = ""
        val databaseU: DatabaseReference = FirebaseDatabase.getInstance().getReference("usuarios")

        if (user != null) {
            databaseU.orderByChild("correo").equalTo(user.email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            nombreUsuario = snapshot.child("nombre")
                                .getValue(String::class.java).toString()
                            correoUsuario = snapshot.child("correo")
                                .getValue(String::class.java).toString()
                            rolUsuario = snapshot.child("rol")
                                .getValue(String::class.java).toString()
                        }

                        val intent =
                            Intent(holder.itemView.context, EditarTicketActivity::class.java)
                        intent.putExtra("ticket.numTicket", ticket.numTicket.toString())
                        intent.putExtra("ticket.titulo", ticket.titulo)
                        intent.putExtra("ticket.descripcion", ticket.descripcion)
                        intent.putExtra("ticket.emailAutor", ticket.emailAutor)
                        intent.putExtra("nombreUsuario", nombreUsuario)
                        intent.putExtra("correoUsuario", correoUsuario)
                        intent.putExtra("rolUsuario", rolUsuario)

                        holder.itemView.context.startActivity(intent)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "Error al buscar usuario",
                            "Error: ${databaseError.message}"
                        )
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}
