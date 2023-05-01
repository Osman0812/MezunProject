package com.example.mezunproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.mezunproject.activities.MainActivity
import com.example.mezunproject.classes.Mezun
import com.example.mezunproject.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class Signup : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var name : String
    private lateinit var surname : String
    private lateinit var entryDate : String
    private lateinit var quitDate : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var mezun : Mezun
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    private var _binding: FragmentSignupBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        mezun = getUserInfo()

        binding.createAccount.setOnClickListener {

            createUser()
            uploadToStorage()
        }
        binding.back.setOnClickListener {
            val action = SignupDirections.actionSignup2ToSignin2()
            Navigation.findNavController(it).navigate(action)
        }

    }

    private fun createUser(){

        if (binding.signupEmailText.text.isEmpty() || binding.passwordText.text.isEmpty()){
            Toast.makeText(context,"Enter Email and Password!",Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(binding.signupEmailText.text.toString(), binding.passwordText.text.toString()).addOnSuccessListener {

                //val user = auth.currentUser
                //set action, user created
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("User",mezun.toString())
                startActivity(intent)
                activity?.finish()

            }.addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }


        }


    }
    private fun uploadToStorage(){

        val mezun = getUserInfo()
        val hashMap = hashMapOf<String, Any>()
        hashMap["userEmail"] = email
        hashMap["userName"] = mezun.name
        hashMap["surname"] = mezun.surname
        hashMap["entryDate"] = mezun.entryDate
        hashMap["quitDate"] = mezun.quitDate
        hashMap["password"] = password



        firestore.collection("Users").document(email).set(hashMap).addOnSuccessListener {
            Toast.makeText(context,"Account Created!",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }

    }


    private fun getUserInfo(): Mezun {

        name = binding.nameText.text.toString()
        surname = binding.surnameText.text.toString()
        entryDate = binding.entryDateText.text.toString()
        quitDate = binding.quitDateText.text.toString()
        email = binding.signupEmailText.text.toString()
        password = binding.passwordText.text.toString()

        return Mezun(name, surname, entryDate, quitDate)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}