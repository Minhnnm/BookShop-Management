package com.example.bookshopmanagement.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshopmanagement.data.model.Author
import com.example.bookshopmanagement.databinding.ItemAuthorBinding

class AuthorAdapter : RecyclerView.Adapter<AuthorAdapter.ViewHolder>() {
    private var authorList: MutableList<Author> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAuthorBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(authors: List<Author>) {
        authorList.clear()
        authorList.addAll(authors)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AuthorAdapter.ViewHolder, position: Int) {
        holder.bind(authorList[position])
    }

    fun getAuthor(position: Int) = authorList[position]
    override fun getItemCount() = authorList.size

    inner class ViewHolder(private val binding: ItemAuthorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(author: Author) {
            binding.apply {
                Glide.with(binding.root)
                    .load(author.authorAvatar)
                    .centerCrop().into(imageAuthor)
                textNameAuthor.text = author.authorName
                textDescription.text = author.authorDescription
                contraintAuthor.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }
        }
    }
}