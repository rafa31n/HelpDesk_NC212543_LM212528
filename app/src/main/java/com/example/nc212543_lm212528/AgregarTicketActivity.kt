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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class AgregarTicketActivity : AppCompatActivity() {

    private var edtTitulo: EditText? = null
    private var edtDescripcion: EditText? = null
    lateinit var database: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_ticket)

        val datos: Bundle? = intent.extras
        val nombreUsuario = datos?.getString("nombreUsuario").toString()
        val correoUsuario = datos?.getString("correoUsuario").toString()
        val rolUsuario = datos?.getString("rolUsuario").toString()

        edtTitulo = findViewById<EditText>(R.id.editTextTitle)
        edtDescripcion = findViewById<EditText>(R.id.editTextDescription)

        val btnEnviar = findViewById<Button>(R.id.buttonEnviar)

        btnEnviar.setOnClickListener {
            val titulo: String = edtTitulo?.text.toString()
            val descripcion: String = edtDescripcion?.text.toString()

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val numeroTicket = Random.nextInt(101)
                val fechaActual = getFechaActual()
                val ticket = Ticket(
                    numeroTicket, titulo, descripcion, "HelpDesk", nombreUsuario,
                    correoUsuario, fechaActual, "Activo", null
                )
                database = FirebaseDatabase.getInstance().getReference("tickets")

                database.child(numeroTicket.toString()).setValue(ticket).addOnSuccessListener {
                    Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(getBaseContext(), HomeUserActivity::class.java)
                    intent.putExtra("nombreUsuario", nombreUsuario)
                    intent.putExtra("correoUsuario", correoUsuario)
                    intent.putExtra("rolUsuario", rolUsuario)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "No se pudo generar el ticket.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getFechaActual(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}