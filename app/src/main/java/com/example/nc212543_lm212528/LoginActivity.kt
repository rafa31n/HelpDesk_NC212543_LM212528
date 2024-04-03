package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //Declaracion variables
    private var edtCorreo: EditText? = null
    private var edtPassword: EditText? = null
    private lateinit var auth: FirebaseAuth

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
                            val intent = Intent(getBaseContext(), HomeActivity::class.java)
                            startActivity(intent)
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