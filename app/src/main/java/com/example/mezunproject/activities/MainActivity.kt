package com.example.mezunproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mezunproject.R
import com.example.mezunproject.adapters.ProfileRecyclerAdapter
import com.example.mezunproject.classes.ShareClass
import com.example.mezunproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var usersAdapter : ProfileRecyclerAdapter
    private lateinit var usersList : ArrayList<ShareClass>
    private lateinit var shareClass: ShareClass


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage




        usersList = ArrayList<ShareClass>()

        getDataFromFirebase()


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        usersAdapter = ProfileRecyclerAdapter(usersList)
        binding.recyclerView.adapter = usersAdapter

        usersList.clear()




    }
    private fun getDataFromFirebase(){


        firestore.collection("Users").addSnapshotListener{value, error->

            if (error != null){
                Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
            }else{
                if (value != null && !value.isEmpty){



                    val users = value.documents
                    var pictureUrl : String? = null

                    for(user in users) {

                        val name = user.get("userName") as String
                        val surname = user.get("surname") as String
                        val email = user.get("userEmail") as String

                        if (user.contains("pictureUrl")){
                            pictureUrl = user.get("pictureUrl") as String
                        }






                        val mezun = ShareClass(name,surname,email,pictureUrl)
                        usersList.add(mezun)
                        pictureUrl = null


                    }


                    usersAdapter.notifyDataSetChanged()


                }
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_item,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val ez = intent.getIntExtra("ez",0)
        if (item.itemId == R.id.profile){
            usersList.clear()
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("krang",ez)
            startActivity(intent)
        }else if(item.itemId == R.id.logout){

            val pref = getSharedPreferences("com.example.mezunproject.activities", MODE_PRIVATE)
            pref.edit().clear().apply()
            auth.signOut()
            usersList.clear()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()

        }


        return super.onOptionsItemSelected(item)

    }


}