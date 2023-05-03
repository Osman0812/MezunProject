package com.example.mezunproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mezunproject.R
import com.example.mezunproject.adapters.SocialAdapter
import com.example.mezunproject.classes.Post
import com.example.mezunproject.databinding.FragmentNewsBinding
import com.example.mezunproject.databinding.FragmentSocialBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class SocialFragment : Fragment() {

    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var socialAdapter : SocialAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        postArrayList = ArrayList<Post>()

        getDataFromFirebase()

        binding.socialRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        socialAdapter = SocialAdapter(postArrayList)
        binding.socialRecyclerView.adapter = socialAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFromFirebase(){



        firestore.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()
            }else{
                if (value != null && !value.isEmpty){

                    val documents = value.documents

                    postArrayList.clear()

                    for(document in documents){

                        val comment = document.get("comment") as String
                        val userEmail = document.get("userName") as String
                        val downloadUrl = document.get("downloadUrl") as String

                        val post = Post(userEmail,comment,downloadUrl)
                        postArrayList.add(post)

                    }

                    socialAdapter.notifyDataSetChanged()

                }
            }

        }






    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}