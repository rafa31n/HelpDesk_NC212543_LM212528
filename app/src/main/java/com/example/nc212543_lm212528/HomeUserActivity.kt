package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
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

class HomeUserActivity : AppCompatActivity() {
    private lateinit var databaseT: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)
        setupViews()
        loadTicketsFromFirebase()
    }

    override fun onResume() {
        super.onResume()
        loadTicketsFromFirebase()
    }

    private fun setupViews() {
        auth = FirebaseAuth.getInstance()
        val txtTitulo = findViewById<TextView>(R.id.textViewTitulo)
        val txtCorreo = findViewById<TextView>(R.id.textViewCorreo)
        val txtRol = findViewById<TextView>(R.id.textViewRol)
        val txtDepto = findViewById<TextView>(R.id.textViewDepto)
        val btnSalir = findViewById<Button>(R.id.btnSalirU)
        val datos: Bundle? = intent.extras
        val nombreUsuario = datos?.getString("nombreUsuario").toString()
        val correoUsuario = datos?.getString("correoUsuario").toString()
        val rolUsuario = datos?.getString("rolUsuario").toString()
        val deptoUsuario = datos?.getString("deptoUsuario").toString()
        txtTitulo.text = "Bienvenido $nombreUsuario"
        txtCorreo.text = correoUsuario
        txtRol.text = "Rol: $rolUsuario"
        txtDepto.text = "Departamento: $deptoUsuario"

        val btnCrearTicket = findViewById<Button>(R.id.btnAgregarTicket)
        btnCrearTicket.setOnClickListener {
            val intent = Intent(this, AgregarTicketActivity::class.java)
            intent.putExtra("nombreUsuario", nombreUsuario)
            intent.putExtra("correoUsuario", correoUsuario)
            intent.putExtra("rolUsuario", rolUsuario)
            intent.putExtra("deptoUsuario", deptoUsuario)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@HomeUserActivity, LoginActivity::class.java))
            Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show()
        }
        recyclerView = findViewById(R.id.recyclerViewTicketsU)
    }

    private fun loadTicketsFromFirebase() {
        val correoUsuario = intent.getStringExtra("correoUsuario") ?: return
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
}