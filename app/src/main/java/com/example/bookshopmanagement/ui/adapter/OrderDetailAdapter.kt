package com.example.bookshopmanagement.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.BookShopApp.utils.format.FormatMoney
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.response.OrderDetailProduct
import com.example.bookshopmanagement.databinding.ItemOrderDetailBinding

class OrderDetailAdapter :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {
    private val formatMoney = FormatMoney()
    private var orderDetailProductList: MutableList<OrderDetailProduct> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OrderDetailAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderDetailBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(orderDetailProducts: List<OrderDetailProduct>) {
        orderDetailProductList = orderDetailProducts as MutableList<OrderDetailProduct>
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OrderDetailAdapter.ViewHolder, position: Int) {
        val orderDetailProductList = orderDetailProductList[position]
        holder.bind(orderDetailProductList)
    }

    override fun getItemCount(): Int = orderDetailProductList.size

    inner class ViewHolder(private val binding: ItemOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(orderDetailProduct: OrderDetailProduct) {
            Glide.with(binding.root)
                .load(orderDetailProduct.image)
                .centerCrop()
                .into(binding.imageProduct)
            binding.textPrice.text =
                orderDetailProduct.subtotal?.let { formatMoney.formatMoney(it.toDouble().toLong()) }
            binding.textName.text = orderDetailProduct.productName
            binding.textDescription.text = orderDetailProduct.productDescription
            binding.textNumber.text = "x" + orderDetailProduct.quantity.toString()
        }
    }
}