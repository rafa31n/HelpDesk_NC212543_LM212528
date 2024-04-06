package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrarActivity : AppCompatActivity() {

    //Declaracion variables
    private var edtNombres: EditText? = null
    private var edtApellido: EditText? = null
    private var edtCorreo: EditText? = null
    private var edtPassword: EditText? = null
    lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)

        auth = FirebaseAuth.getInstance()
        edtNombres = findViewById<EditText>(R.id.editTextName)
        edtApellido = findViewById<EditText>(R.id.editTextLastName)
        edtCorreo = findViewById<EditText>(R.id.editTextEmail)
        edtPassword = findViewById<EditText>(R.id.editTextPassword)

        val btnRegistrarse = findViewById<Button>(R.id.buttonSignUp)

        //SPINNER
        val spinner: Spinner = findViewById(R.id.spinner)
        val opciones = resources.getStringArray(R.array.opciones_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        btnRegistrarse.setOnClickListener {
            val nombres: String = edtNombres?.text.toString()
            val apellido: String = edtApellido?.text.toString()
            val correo: String = edtCorreo?.text.toString()
            val password: String = edtPassword?.text.toString()
            val departamento: String = spinner.selectedItem.toString()

            if (nombres.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty() || departamento.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val nombreCompleto = nombres.trim() + " " + apellido.trim()
                signUp(correo, password, nombreCompleto, departamento)
            }
        }

        //
        val txtIniciarSesion = findViewById<TextView>(R.id.textViewLogin)
        txtIniciarSesion.setOnClickListener {
            val intent = Intent(getBaseContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(
        email: String,
        password: String,
        nombreCompleto: String,
        departamento: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    guardarInformacion(nombreCompleto, email, password, departamento)
                } else {
                    Toast.makeText(
                        this,
                        "Error al registrar: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun guardarInformacion(
        nombreCompleto: String,
        correo: String,
        password: String,
        departamento: String
    ) {
        val usuario = Usuario(nombreCompleto, correo, password, "user", departamento)
        database = FirebaseDatabase.getInstance().getReference("usuarios")

        database.child(nombreCompleto.trim()).setValue(usuario).addOnSuccessListener {
            Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
            val intent = Intent(getBaseContext(), LoginActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "No se pudo crear la cuenta.", Toast.LENGTH_SHORT).show()
        }
    }
}


