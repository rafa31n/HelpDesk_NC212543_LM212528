package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    //Declaracion variables
    private var edtCorreo: EditText? = null
    private var edtPassword: EditText? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseU: DatabaseReference
    var nombreUsuario: String = ""
    var correoUsuario: String = ""
    var rolUsuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        edtCorreo = findViewById<EditText>(R.id.editTextEmail)
        edtPassword = findViewById<EditText>(R.id.editTextPassword)

        val textViewLogin = findViewById<TextView>(R.id.textViewSignUp)
        textViewLogin.setOnClickListener {
            val intent = Intent(getBaseContext(), RegistrarActivity::class.java)
            startActivity(intent)
        }

        val btnRegistrarse = findViewById<Button>(R.id.buttonLogin)

        btnRegistrarse.setOnClickListener {
            val correo: String = edtCorreo?.text.toString()
            val password: String = edtPassword?.text.toString()

            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.signInWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this, "Inicio de sesión exitoso",
                                Toast.LENGTH_SHORT
                            ).show()

                            databaseU = FirebaseDatabase.getInstance().getReference("usuarios")

                            databaseU.orderByChild("correo").equalTo(correo)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        for (snapshot in dataSnapshot.children) {
                                            nombreUsuario =
                                                snapshot.child("nombre")
                                                    .getValue(String::class.java).toString()
                                            correoUsuario =
                                                snapshot.child("correo")
                                                    .getValue(String::class.java).toString()
                                            rolUsuario =
                                                snapshot.child("rol").getValue(String::class.java)
                                                    .toString()

                                            if (rolUsuario.equals("user")) {
                                                val intent = Intent(
                                                    getBaseContext(),
                                                    HomeUserActivity::class.java
                                                )
                                                intent.putExtra("nombreUsuario", nombreUsuario)
                                                intent.putExtra("correoUsuario", correoUsuario)
                                                intent.putExtra("rolUsuario", rolUsuario)
                                                startActivity(intent)
                                            } else {
                                                val intent = Intent(
                                                    getBaseContext(),
                                                    HomeAdminActivity::class.java
                                                )
                                                intent.putExtra("nombreUsuario", nombreUsuario)
                                                intent.putExtra("correoUsuario", correoUsuario)
                                                intent.putExtra("rolUsuario", rolUsuario)
                                                startActivity(intent)
                                            }
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e(
                                            "Error al buscar usuario",
                                            "Error: ${databaseError.message}"
                                        )
                                    }
                                })
                        } else {
                            Toast.makeText(
                                this,
                                "Error al iniciar sesion: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}