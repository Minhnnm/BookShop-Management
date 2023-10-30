package com.example.bookshopmanagement.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.utils.format.FormatDate
import com.example.BookShopApp.utils.format.FormatMoney
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.Order
import com.example.bookshopmanagement.data.model.response.order.OrderHistory
import com.example.bookshopmanagement.databinding.ItemHeaderOrderBinding
import com.example.bookshopmanagement.databinding.ItemOrderHistoryBinding

class OrderAdapter(val status: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var orderHistoryList: MutableList<OrderHistory> = mutableListOf()
    private val formatMoney = FormatMoney()
    private val formatDate = FormatDate()
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemDetailClickListener: OnItemClickListener? = null

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val binding = ItemHeaderOrderBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemOrderHistoryBinding.inflate(inflater, parent, false)
            ItemViewHolder(binding)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(orderHistories: List<OrderHistory>) {
        orderHistoryList.clear()
        orderHistoryList.addAll(orderHistories)
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnItemDetailClickListener(listener: OnItemClickListener) {
        onItemDetailClickListener = listener
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (orderHistoryList[position].header != null) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    fun getOrder(position: Int): Order? {
        return orderHistoryList[position].order
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val header = orderHistoryList[position].header
        val itemOrder = orderHistoryList[position].order
        when (holder) {
            is HeaderViewHolder -> header?.let { holder.bind(it) }
            is ItemViewHolder -> itemOrder?.let { holder.bind(it) }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: String) {
            binding.textHeader.text = header
        }
    }

    inner class ItemViewHolder(private val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(item: Order) {
            binding.apply {
                textIdOrder.text = "#Item " + item.orderId
                textNumberPro.text = item.totalQuantity + " sản phẩm"
                textPrice.text = item.orderTotal?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
                textStatus.text = item.orderStatus
                when (status) {
                    1 -> textStatus.setTextColor(R.color.red)
                    3 -> {
                        textStatus.setTextColor(R.color.teal_700)
                        textConfirm.visibility = View.INVISIBLE
                    }
                }
                textConfirm.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(position)
                    }
                }
                itemOrder.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemDetailClickListener?.onItemClick(position)
                    }
                }
                if (item.shippedOn == null) {
                    item.createdOn?.let {
                        textTime.text = it.split(" ")[1]
                    }
                } else {
                    textTime.text = item.shippedOn!!.split(" ")[1]
                }
            }
        }
    }

//    fun generateRandomString(length: Int): String {
//        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
//        val random = java.util.Random()
//        return (1..length)
//            .map { charset[random.nextInt(charset.length)] }
//            .joinToString("")
//    }
}
