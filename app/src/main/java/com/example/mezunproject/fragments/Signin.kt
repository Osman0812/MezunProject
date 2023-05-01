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
import com.example.mezunproject.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Signin : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentSigninBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setOnClickListener {
            signup(it)
        }

        binding.signin.setOnClickListener {
            signin(it)
        }


    }

    private fun signup(view: View){

        val action = SigninDirections.actionSignin2ToSignup2()
        Navigation.findNavController(view).navigate(action)

    }

    private fun signin(view : View){


        val email = binding.loginEmailText.text.toString()
        val password = binding.passwordtext.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    Toast.makeText(context,"Welcome $email",Toast.LENGTH_LONG).show()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("fromsignin",true)
                    startActivity(intent)
                    activity?.finish()

                }.addOnFailureListener {
                    Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                }
        }else{
            Toast.makeText(context,"Email And Password Should Not Be Empty!",Toast.LENGTH_LONG).show()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}