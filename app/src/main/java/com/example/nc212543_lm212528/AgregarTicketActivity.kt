package com.example.nc212543_lm212528

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
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
                    numeroTicket, titulo, descripcion, "HelpDesk", "Mario",
                    "mario@gmail.com", fechaActual, "Activo", null
                )
                database = FirebaseDatabase.getInstance().getReference("tickets")

                database.child(numeroTicket.toString()).setValue(ticket).addOnSuccessListener {
                    Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(getBaseContext(), HomeActivity::class.java)
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