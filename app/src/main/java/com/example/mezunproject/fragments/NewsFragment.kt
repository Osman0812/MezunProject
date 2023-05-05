package com.example.mezunproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mezunproject.R
import com.example.mezunproject.adapters.NewsAdapter
import com.example.mezunproject.classes.ArticleKeeper
import com.example.mezunproject.classes.ShareClass
import com.example.mezunproject.databinding.FragmentNewsBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var myArticle : ArticleKeeper
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var article : String? = null
    private var name : String? = null
    private var surname : String? = null
    private lateinit var articleArrayList: ArrayList<ArticleKeeper>
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleArrayList = ArrayList<ArticleKeeper>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore


        //checkExpiredData()

        getDataFromFirebase()

        binding.newsRecyclerview.layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsAdapter(articleArrayList)
        binding.newsRecyclerview.adapter = newsAdapter



        articleArrayList.clear()





    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkExpiredData(){

        firestore.collection("Users").whereLessThan("expireAt",Timestamp.now().toDate().time).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()
            }else{

                val delete = hashMapOf<String,Any>("expireAt" to FieldValue.delete())
                if (value != null){
                    val documents = value.documents
                    for (document in documents){
                        val ref = firestore.collection("Users").document(document.get("email").toString())
                        ref.update(delete).addOnSuccessListener {
                            println("deleted")
                        }
                    }
                    newsAdapter.notifyDataSetChanged()
                }

            }
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFromFirebase(){


        firestore.collection("Users").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{value, error->

            if (error != null){
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()
            }else{
                if (value != null && !value.isEmpty){


                    val users = value.documents

                    articleArrayList.clear()


                    for(user in users) {


                        if (user.contains("myarticle") ){


                            article = user.get("myarticle") as String
                            name = user.get("userName") as String
                            surname = user.get("surname") as String

                            myArticle = ArticleKeeper(name!!,surname!!,article)
                            articleArrayList.add(myArticle)
                            article = null


                        }

                    }
                    newsAdapter.notifyDataSetChanged()



                }
            }

        }


    }




    override fun onPause() {
        super.onPause()
        binding.newsRecyclerview.clearFocus()
        articleArrayList.clear()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        articleArrayList.clear()
    }


}