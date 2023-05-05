package com.example.mezunproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mezunproject.R
import com.example.mezunproject.databinding.ActivityPasswordResetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PasswordReset : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordResetBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private var password: String?= null
    private val hashMap = hashMapOf<String, Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        firestore = Firebase.firestore






    }

    fun reset(view: View){


        //password = getUsersPassword()
        val oldPassword = binding.oldpassword.text.toString()
        val newPassword = binding.newpassword.text.toString()




        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                Toast.makeText(this,"Password changed!",Toast.LENGTH_LONG).show()
                setNewPassword(newPassword)
            }?.addOnFailureListener { error->

                Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()

            }




    }

    private fun setNewPassword(password: String) {

        hashMap["password"] = password


        firestore.collection("Users").document(auth.currentUser!!.email.toString()).update(hashMap).let {
            it.addOnSuccessListener {
                //Toast.makeText(applicationContext,"Succeed!",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {error ->
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getUsersPassword() : String{

        val docName = auth.currentUser!!.email.toString() // docName -> email
        firestore.collection("Users").document(docName).addSnapshotListener{value, error ->
            if (error != null){
                Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            }else{

                password = value?.get("password") as String


            }
        }
        return password!!



    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}