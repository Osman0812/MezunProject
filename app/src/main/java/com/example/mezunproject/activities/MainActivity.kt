package com.example.mezunproject.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mezunproject.R
import com.example.mezunproject.adapters.ProfileRecyclerAdapter
import com.example.mezunproject.classes.ShareClass
import com.example.mezunproject.databinding.ActivityMainBinding
import com.example.mezunproject.fragments.MainInvisibleFragment
import com.example.mezunproject.fragments.MainInvisibleFragmentDirections
import com.example.mezunproject.fragments.NewArticle
import com.example.mezunproject.fragments.NewSocial
import com.example.mezunproject.fragments.NewsFragment
import com.example.mezunproject.fragments.NewsFragmentDirections
import com.example.mezunproject.fragments.SocialFragment
import com.example.mezunproject.fragments.SocialFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var usersAdapter : ProfileRecyclerAdapter
    private lateinit var usersList : ArrayList<ShareClass>
    private lateinit var shareClass: ShareClass
    private lateinit var bottomNav : BottomNavigationView
    //private lateinit var sharedPreferences : SharedPreferences


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        //bottomnav
        bottomNav = binding.bottomNavigation.findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> {

                    //sharedPreferences.edit().putInt("lastSelectedItem",R.id.home).apply()
                    //to socail
                    binding.recyclerView.visibility = View.INVISIBLE
                    changeFragmentTo(SocialFragment())


                }
                R.id.mezunlar -> {
                    //sharedPreferences.edit().putInt("lastSelectedItem",R.id.mezunlar).apply()
                    binding.recyclerView.visibility = View.VISIBLE
                    changeFragmentTo(MainInvisibleFragment())

                    //to  mezunlar
                }
                R.id.news -> {
                    //sharedPreferences.edit().putInt("lastSelectedItem",R.id.news).apply()
                    binding.recyclerView.visibility = View.INVISIBLE
                    changeFragmentTo(NewsFragment())

                    //to news
                }
            }

            true
        }




        usersList = ArrayList<ShareClass>()

        getDataFromFirebase()


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        usersAdapter = ProfileRecyclerAdapter(usersList)
        binding.recyclerView.adapter = usersAdapter

        usersList.clear()




    }


    private fun changeFragmentTo(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout,fragment).commit()

    }



    private fun getDataFromFirebase(){


        firestore.collection("Users").addSnapshotListener{value, error->

            if (error != null){
                Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
            }else{
                if (value != null && !value.isEmpty){



                    val users = value.documents
                    var pictureUrl : String? = null

                    usersList.clear()

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

        }else if (item.itemId == R.id.comment){

            //userList e dikkat!!!
            if (bottomNav.selectedItemId != R.id.news){
                binding.recyclerView.visibility = View.INVISIBLE
                changeFragmentTo(NewsFragment())
                bottomNav.selectedItemId = R.id.news
            }
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.frameLayout,NewArticle()).commit()

        }else if(item.itemId == R.id.social){

            if (bottomNav.selectedItemId != R.id.home){
                binding.recyclerView.visibility = View.INVISIBLE
                changeFragmentTo(SocialFragment())
                bottomNav.selectedItemId = R.id.home
            }

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout,NewSocial()).commit()


        }


        return super.onOptionsItemSelected(item)

    }


}