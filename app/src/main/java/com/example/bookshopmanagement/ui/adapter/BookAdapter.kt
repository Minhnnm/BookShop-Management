package com.example.bookshopmanagement.ui.adapter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.BookShopApp.utils.format.FormatMoney
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.Product
import com.example.bookshopmanagement.databinding.ItemBookBinding

class BookAdapter() : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    private var productList: MutableList<Product> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null
    private val formatMoney = FormatMoney()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        productList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        holder.bind(productList[position])
//        holder.itemView.setOnLongClickListener {
//            val product = productList[position]
//            Log.d("PRODUCT", product.toString())
//            false
//        }
    }

    fun getBook(position: Int): Product = productList[position]


    override fun getItemCount(): Int {
        return productList.size
    }


    inner class ViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.apply {
                Glide.with(root).load(product.thumbnail).centerCrop().into(imageProduct)
                if (product.discounted_price != null && product.discounted_price != product.price) {
                    val layoutParams = textPrice.layoutParams as ViewGroup.MarginLayoutParams
                    val newMarginTopInDp = 0
                    textDiscountPrice.visibility = View.VISIBLE
                    layoutParams.topMargin = newMarginTopInDp
                    textPrice.layoutParams = layoutParams
                    textDiscountPrice.text = product.discounted_price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }
                    textPrice.text = setPrice(
                        product.price?.toDouble()?.let { formatMoney.formatMoney(it.toLong()) }
                            .toString()
                    )
                } else {
                    binding.textPrice.text =
                        product.price?.toDouble()?.let { formatMoney.formatMoney(it.toLong()) }
                }

                textQuantityRemaining.text =
                    root.resources.getString(R.string.quantity_remaining) + " " + (product.quantity - product.quantitySold)
                textName.text = product.name
                cardview.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(position)
                    }
                }
                cardview.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemLongClickListener?.onItemLongClick(position)
                    }
                    true
                }
            }
        }
    }

    private fun setPrice(price: String): SpannableString {
        val content = SpannableString(price)
        content.setSpan(
            StrikethroughSpan(), 0, price.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        content.setSpan(
            RelativeSizeSpan(12 / 14f), 0, price.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return content
    }
}