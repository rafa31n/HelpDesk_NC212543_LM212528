package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var databaseT: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        val txtTitulo = findViewById<TextView>(R.id.textViewTitulo)
        val txtCorreo = findViewById<TextView>(R.id.textViewCorreo)
        val txtRol = findViewById<TextView>(R.id.textViewRol)
        val datos: Bundle? = intent.getExtras()
        val nombreUsuario = datos?.getString("nombreUsuario").toString()
        val correoUsuario = datos?.getString("correoUsuario").toString()
        val rolUsuario = datos?.getString("rolUsuario").toString()
        txtTitulo.text = "Bienvenido " + nombreUsuario
        txtCorreo.text = correoUsuario
        txtRol.text = "Rol: " + rolUsuario

        val btnSalir = findViewById<Button>(R.id.btnSalirA)
        btnSalir.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this@HomeAdminActivity, LoginActivity::class.java))
            Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTicketsA)
        databaseT = FirebaseDatabase.getInstance().getReference("tickets")
        databaseT.orderByChild("estado").equalTo("Activo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tickets = mutableListOf<Ticket>()

                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val numTicket: Int? =
                                snapshot.child("numTicket").getValue(Int::class.java)
                            val titulo: String? =
                                snapshot.child("titulo").getValue(String::class.java)
                            val descripcion: String? =
                                snapshot.child("descripcion").getValue(String::class.java)
                            val departamento: String? =
                                snapshot.child("departamento").getValue(String::class.java)
                            val autor: String? =
                                snapshot.child("autorTicket").getValue(String::class.java)
                            val email: String? =
                                snapshot.child("emailAutor").getValue(String::class.java)
                            val fechaCreacion: String? =
                                snapshot.child("fechaCreacion").getValue(String::class.java)
                            val estado: String? =
                                snapshot.child("estado").getValue(String::class.java)
                            val fechaFinalizacion: String? =
                                snapshot.child("fechaFinalizacion").getValue(String::class.java)

                            val ticket =
                                Ticket(
                                    numTicket,
                                    titulo,
                                    descripcion,
                                    departamento,
                                    autor,
                                    email,
                                    fechaCreacion,
                                    estado,
                                    fechaFinalizacion
                                )
                            tickets.add(ticket)
                        }

                        recyclerView.layoutManager = LinearLayoutManager(this@HomeAdminActivity)
                        recyclerView.adapter = TicketAdapterAdmin(tickets)
                    } else {
                        recyclerView.visibility = View.GONE
                        val txtMensaje = findViewById<TextView>(R.id.textViewMensaje)
                        txtMensaje.visibility = View.VISIBLE
                        txtMensaje.text = "No hay tickets activos."
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores
                }
            })
    }
}