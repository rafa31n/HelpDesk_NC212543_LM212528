package com.example.nc212543_lm212528

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeUserActivity : AppCompatActivity() {
    private lateinit var databaseT: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

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

        val btnCrearTicket = findViewById<Button>(R.id.btnAgregarTicket)
        btnCrearTicket.setOnClickListener {
            val intent = Intent(getBaseContext(), AgregarTicketActivity::class.java)
            //intent.putExtra("nombreUsuario", currentUser.)
            startActivity(intent)
        }

        // VER TICKETS USUARIO
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTicketsU)
        databaseT = FirebaseDatabase.getInstance().getReference("tickets")
        databaseT.orderByChild("emailAutor").equalTo(correoUsuario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tickets = mutableListOf<Ticket>()

                    for (snapshot in dataSnapshot.children) {
                        val numTicket: Int? = snapshot.child("numTicket").getValue(Int::class.java)
                        val titulo: String? = snapshot.child("titulo").getValue(String::class.java)
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
                        val estado: String? = snapshot.child("estado").getValue(String::class.java)
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

                    recyclerView.layoutManager = LinearLayoutManager(this@HomeUserActivity)
                    recyclerView.adapter = TicketAdapterUser(tickets)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores

                }
            })
    }

    /*override fun onStart() {
        val datos: Bundle? = intent.getExtras()
        if (datos == null) {
            val intent = Intent(getBaseContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        super.onStart()
    }*/
}