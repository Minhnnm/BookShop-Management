package com.example.bookshopmanagement.ui.main.order.prepareorder

import android.app.AlertDialog
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.utils.format.FormatDate
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.Order
import com.example.bookshopmanagement.data.model.response.order.OrderHistory
import com.example.bookshopmanagement.databinding.FragmentOrderPrepareBinding
import com.example.bookshopmanagement.ui.adapter.OnItemClickListener
import com.example.bookshopmanagement.ui.adapter.OrderAdapter
import com.example.bookshopmanagement.ui.main.order.OrderViewModel
import com.example.bookshopmanagement.ui.main.order.orderdetail.OrderDetailFragment
import java.time.LocalDateTime

class OrderPrepareFragment : Fragment() {

    companion object {
        fun newInstance() = OrderPrepareFragment()
    }

    private lateinit var viewModel: OrderViewModel
    private var binding: FragmentOrderPrepareBinding? = null
    private lateinit var adapter: OrderAdapter
    private var formatDate = FormatDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPrepareBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OrderAdapter(1)
        initViewModel()
        binding?.apply {
//            loadingLayout.root.visibility = View.VISIBLE
            viewModel.getAllOrderByOrderStatus(1)
            recyclerOrderPrepare.layoutManager = LinearLayoutManager(context)
            recyclerOrderPrepare.adapter = adapter
        }
        updateStatus()
        navToOrderDetail()
    }

    private fun updateStatus() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val order = adapter.getOrder(position)
                order?.orderId?.let {
                    viewModel.updateOrderStatus(it, 2)
                }
                Handler().postDelayed({
                    viewModel.getAllOrderByOrderStatus(1)
                }, 300)
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        val list = mutableListOf<OrderHistory>()
        val currentDate = formatDate.formatDate(LocalDateTime.now().toString())
        val mapOrder: MutableMap<String, MutableList<Order>> = mutableMapOf()
        viewModel.order.observe(viewLifecycleOwner) {
            if (it != null) {
                mapOrder.clear()
                for (order in it) {
                    Log.d("TIMEE ", order.shippedOn.toString())
                    val date: String = if (order.shippedOn == null) {
                        formatDate.formatDate(order.createdOn)
                    } else {
                        formatDate.formatDate(order.shippedOn)
                    }
                    if (date == currentDate) {
                        mapOrder.computeIfAbsent("Hôm nay") { mutableListOf() }.add(order)
                    } else {
                        mapOrder.computeIfAbsent(date) { mutableListOf() }.add(order)
                    }
                }
                list.clear()
                for ((key, value) in mapOrder) {
                    list.add(OrderHistory(key, null))
                    for (values in value) {
                        list.add(OrderHistory(null, values))
                    }
                }
//                if (list.isEmpty()) {
//                    binding?.textOrderHistory?.visibility = View.VISIBLE
//                }
                adapter.setData(list)
//                binding?.loadingLayout?.root?.visibility = View.INVISIBLE
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle(null)
                .setMessage(it.message)
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.cancel()
                }
                .show()

        }
    }

    private fun navToOrderDetail() {
        adapter.setOnItemDetailClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val orderId = adapter.getOrder(position)?.orderId
                val orderStatus = adapter.getOrder(position)?.orderStatus
                val bundle = Bundle()
                bundle.putString("orderId", orderId.toString())
                bundle.putString("orderStatus", orderStatus)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, OrderDetailFragment().apply { arguments = bundle })
                    .addToBackStack("Orderhistory")
                    .commit()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllOrderByOrderStatus(1)
    }
}