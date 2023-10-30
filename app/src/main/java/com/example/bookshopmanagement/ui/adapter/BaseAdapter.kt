package com.example.bookshopmanagement.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.data.model.Category
import com.example.BookShopApp.data.model.Supplier
import com.example.bookshopmanagement.databinding.ItemCategoryBinding

class BaseAdapter(private val isCategory: Boolean) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder>() {
    private var categoryList: MutableList<Category> = mutableListOf()
    private var supplierList: MutableList<Supplier> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private var positionSelected: Int = -1
    private var positionSupplierSelected: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isCategory) {
            holder.bindCategory(categoryList[position])
        } else {
            holder.bindSupplier(supplierList[position])
        }
    }

    fun setPositonSelected(position: Int) {
        positionSelected = position
    }
    fun setPositonSupplierSelected(position: Int) {
        positionSupplierSelected = position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPublisher(suppliers: List<Supplier>) {
        supplierList.clear()
        supplierList.addAll(suppliers)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (isCategory) categoryList.size else supplierList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun getCategory(position: Int) = categoryList[position]
    fun getSupplier(position: Int) = supplierList[position]

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCategory(category: Category) {
            binding.apply {
                textName.text = category.name
                if (positionSelected != -1 && category == categoryList[positionSelected]) {
                    imageCheck.visibility = View.VISIBLE
                }
                textName.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }
        }

        fun bindSupplier(supplier: Supplier) {
            binding.apply {
                textName.text = supplier.name
                if (positionSelected != -1 && supplier == supplierList[positionSelected]) {
                    imageCheck.visibility = View.VISIBLE
                }
                textName.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }
        }
    }
}