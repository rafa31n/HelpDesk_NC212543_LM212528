package com.example.nc212543_lm212528

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val btnCrearTicket = findViewById<Button>(R.id.btnAgregarTicket)

        val correoUsuario = currentUser?.email

        btnCrearTicket.setOnClickListener {
            val intent = Intent(getBaseContext(), AgregarTicketActivity::class.java)
            //intent.putExtra("nombreUsuario", currentUser.)
            startActivity(intent)
        }

    }
}