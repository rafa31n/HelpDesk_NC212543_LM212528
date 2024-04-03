package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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

        btnRegistrarse.setOnClickListener {
            val nombres: String = edtNombres?.text.toString()
            val apellido: String = edtApellido?.text.toString()
            val correo: String = edtCorreo?.text.toString()
            val password: String = edtPassword?.text.toString()

            if (nombres.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val nombreCompleto = nombres.trim() + " " + apellido.trim()
                signUp(correo, password, nombreCompleto)
            }
        }
    }

    private fun signUp(email: String, password: String, nombreCompleto: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    guardarInformacion(nombreCompleto, email, password)
                } else {
                    Toast.makeText(
                        this,
                        "Error al registrar: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun guardarInformacion(nombreCompleto: String, correo: String, password: String) {
        val usuario = Usuario(nombreCompleto, correo, password, "user")
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