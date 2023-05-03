package com.example.mezunproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mezunproject.R
import com.example.mezunproject.activities.MainActivity
import com.example.mezunproject.databinding.FragmentNewArticleBinding
import com.example.mezunproject.databinding.FragmentNewsBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class NewArticle : Fragment() {

    private var _binding: FragmentNewArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val hashMap = hashMapOf<String, Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        binding.newArticle.requestFocus()

        binding.publish.setOnClickListener {

            onPublishClicked()

        }


    }


    private fun onPublishClicked(){

        if (binding.newArticle.text.isEmpty()){
            Toast.makeText(context,"Write down something to publish!",Toast.LENGTH_LONG).show()
        }else{
            val myArticle = binding.newArticle.text.toString()
            val user = auth.currentUser

            hashMap["myarticle"] = myArticle
            hashMap["date"] = Timestamp.now()
            //hashMap["expireAt"] = Timestamp.now().toDate().time + (1*10*1000)

            firestore.collection("Users").document(user!!.email.toString()).update(hashMap).let {
                it.addOnSuccessListener {

                    Toast.makeText(context,"Published!",Toast.LENGTH_LONG).show()
                    goToNewsFragment()

                }.addOnFailureListener {error ->
                    Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()
                }
            }
        }


    }



    private fun goToNewsFragment(){
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,NewsFragment())
        fragmentTransaction.commit()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}