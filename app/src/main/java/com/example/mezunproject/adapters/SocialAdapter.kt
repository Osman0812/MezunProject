package com.example.mezunproject.adapters

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunproject.classes.Post
import com.example.mezunproject.databinding.SocialRowBinding
import com.example.mezunproject.fragments.SocialFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SocialAdapter (private val postList: ArrayList<Post>) : RecyclerView.Adapter<SocialAdapter.PostHolder>(){

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    class PostHolder(val binding: SocialRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = SocialRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        auth = Firebase.auth
        firestore = Firebase.firestore
        return PostHolder(binding)

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.commentText.text = postList.get(position).comment
        holder.binding.nameSurname.text = postList.get(position).email
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.sharedImageView)

        holder.binding.sharedImageView.setOnLongClickListener {

            if (auth.currentUser!!.email.toString().equals(postList.get(position).email)) {
            val alert = AlertDialog.Builder(holder.itemView.context)
            alert.setMessage("Are you sure you want to delete this post?")
            alert.setPositiveButton("Yes") { _, _ ->


                    firestore.collection("Posts").addSnapshotListener { value, error ->
                        if (value != null && !value.isEmpty) {

                            val documents = value.documents

                            for (document in documents) {

                                val email = document.get("userName") as String
                                val com = document.get("comment") as String

                                if (auth.currentUser!!.email.toString()
                                        .equals(email) && postList.get(position).comment.equals(com)
                                ) {

                                    firestore.collection("Posts").document(document.id)
                                        .delete()

                                }
                            }


                        }
                    }

                    //holder.itemView.context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
                }
                alert.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()

                }
                alert.show()
            }







            true
        }
    }

}