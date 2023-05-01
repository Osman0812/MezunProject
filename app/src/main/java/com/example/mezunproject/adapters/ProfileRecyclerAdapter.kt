package com.example.mezunproject.adapters

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunproject.activities.ProfileActivity
import com.example.mezunproject.classes.ShareClass

import com.example.mezunproject.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso


class ProfileRecyclerAdapter(private val usersList: ArrayList<ShareClass>) : RecyclerView.Adapter<ProfileRecyclerAdapter.ShareHolder>() {

    class ShareHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ShareHolder(binding)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: ShareHolder, position: Int) {
       holder.binding.emailText.text = usersList.get(position).email
       holder.binding.surnameText.text = usersList.get(position).surname
        holder.binding.nameText.text = usersList.get(position).name
        Picasso.get().load(usersList.get(position).pictureUrl).into(holder.binding.profileView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,ProfileActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("email",usersList.get(position).email)
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.emailText.setOnLongClickListener{


            val alert = AlertDialog.Builder(holder.itemView.context)
            alert.setMessage("Are you sure you want to contact with ${usersList.get(position).name}?")
            alert.setPositiveButton("Yes") {_,_ ->
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${usersList.get(position).email}")
                }
                holder.itemView.context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
            alert.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()

            }
            alert.show()
            true

        }

    }

}