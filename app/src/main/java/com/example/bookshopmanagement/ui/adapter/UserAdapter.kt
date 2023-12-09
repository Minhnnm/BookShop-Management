package com.example.bookshopmanagement.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.response.User
import com.example.bookshopmanagement.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var userList: MutableList<User> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(user: List<User>) {
        userList.clear()
        userList.addAll(user)
        notifyDataSetChanged()
    }

    fun getUser(position: Int) = userList[position]

    @SuppressLint("NotifyDataSetChanged")
    fun setStatus(position: Int, status: String) {
        userList[position].status = status
        notifyDataSetChanged()
    }

    override fun getItemCount() = userList.size

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                user.avatar?.let {
                    Glide.with(root)
                        .load(it)
                        .into(imageAvatar)
                }
                textUserName.text = user.name
                textEmail.text = user.email
                var mobPhone ="null"
                if(user.mobPhone != "")
                    mobPhone=user.mobPhone
                textPhone.text = mobPhone
                if (user.status.equals("active")) {
                    textStatus.text = root.resources.getString(R.string.active)
                    textStatus.setTextColor(root.resources.getColor(R.color.teal_200))
                } else {
                    textStatus.text = root.resources.getString(R.string.inactive)
                    textStatus.setTextColor(Color.RED)
                }
                imageUserLock.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        onItemClickListener?.onItemClick(position)
                }
            }
        }
    }
}