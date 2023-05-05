package com.example.mezunproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunproject.classes.Post
import com.example.mezunproject.databinding.SocialRowBinding
import com.squareup.picasso.Picasso

class SocialAdapter (private val postList: ArrayList<Post>) : RecyclerView.Adapter<SocialAdapter.PostHolder>(){

    class PostHolder(val binding: SocialRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = SocialRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.commentText.text = postList.get(position).comment
        holder.binding.nameSurname.text = postList.get(position).email
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.sharedImageView)
    }

}