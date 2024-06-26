package com.example.nc212543_lm212528

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditarTicketActivity : AppCompatActivity() {
    private var edtTitulo: EditText? = null
    private var edtDescripcion: EditText? = null
    lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_ticket)

        val datos: Bundle? = intent.extras
        val nombreUsuario = datos?.getString("nombreUsuario").toString()
        val correoUsuario = datos?.getString("correoUsuario").toString()
        val rolUsuario = datos?.getString("rolUsuario").toString()
        val deptoUsuario = datos?.getString("deptoUsuario").toString()

        edtTitulo = findViewById<EditText>(R.id.editTextTitle)
        edtDescripcion = findViewById<EditText>(R.id.editTextDescription)

        val btnEnviar = findViewById<Button>(R.id.buttonEnviar)
        val intent = intent
        edtTitulo?.setText(intent.getStringExtra("ticket.titulo"))
        println("Prueba ID " + intent.getStringExtra("ticket.numTicket").toString())
        println("Prueba Email " + intent.getStringExtra("ticket.emailAutor").toString())

        edtDescripcion?.setText(intent.getStringExtra("ticket.descripcion"))

        btnEnviar.setOnClickListener() {
            val titulo: String = edtTitulo?.text.toString()
            val descripcion: String = edtDescripcion?.text.toString()

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val ticketsReferencia = FirebaseDatabase.getInstance().getReference("tickets")
                    .child(intent.getStringExtra("ticket.numTicket").toString())
                ticketsReferencia.child("titulo").setValue(titulo)
                ticketsReferencia.child("descripcion").setValue(descripcion)
                Toast.makeText(this, "Ticket actualizados correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeUserActivity::class.java)
                intent.putExtra("nombreUsuario", nombreUsuario)
                intent.putExtra("correoUsuario", correoUsuario)
                intent.putExtra("rolUsuario", rolUsuario)
                intent.putExtra("deptoUsuario", deptoUsuario)
                this.startActivity(intent)
            }

        }
    }
}