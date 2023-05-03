package com.example.mezunproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunproject.classes.ArticleKeeper
import com.example.mezunproject.classes.ShareClass
import com.example.mezunproject.databinding.NewsRowBinding
import com.example.mezunproject.databinding.RecyclerRowBinding

class NewsAdapter(private val articleList: ArrayList<ArticleKeeper>) : RecyclerView.Adapter<NewsAdapter.ArticleHolder>() {

    class ArticleHolder(var binding: NewsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val binding = NewsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArticleHolder(binding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.binding.name.text = "$articleList.get(position).name + ${articleList.get(position).surname}"

    }
}