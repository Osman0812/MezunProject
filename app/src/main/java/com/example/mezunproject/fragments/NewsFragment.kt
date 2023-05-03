package com.example.mezunproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mezunproject.classes.ArticleKeeper
import com.example.mezunproject.classes.ShareClass
import com.example.mezunproject.databinding.FragmentNewsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var article : String? = null
    private lateinit var articleArrayList: ArrayList<ArticleKeeper>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore


        articleArrayList = ArrayList<ArticleKeeper>()

        getDataFromFirebase()



    }

    private fun getDataFromFirebase(){


        firestore.collection("Users").addSnapshotListener{value, error->

            if (error != null){
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()
            }else{
                if (value != null && !value.isEmpty){


                    val users = value.documents



                    for(user in users) {

                        val name = user.get("userName") as String
                        val surname = user.get("surname") as String


                        if (user.contains("myarticle")){
                            article = user.get("myarticle") as String
                        }

                        val article = ArticleKeeper(name,surname, article!!)
                        articleArrayList.add(article)



                    }

                }
            }

        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}