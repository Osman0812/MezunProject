package com.example.mezunproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mezunproject.R
import com.example.mezunproject.fragments.Signin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val fragment = Signin()
        supportFragmentManager.beginTransaction().show(fragment).commit()

        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        //Current User Check


        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("ez",1)
            startActivity(intent)
            finish()
        }






    }
}