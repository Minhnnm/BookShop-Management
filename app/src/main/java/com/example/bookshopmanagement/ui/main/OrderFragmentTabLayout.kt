package com.example.bookshopmanagement.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookshopmanagement.databinding.FragmentTabLayoutBinding
import com.example.bookshopmanagement.ui.adapter.ViewPager2Adapter
import com.example.bookshopmanagement.ui.main.order.deliverdorder.DeliveredOrderFragment
import com.example.bookshopmanagement.ui.main.order.prepareorder.OrderPrepareFragment
import com.example.bookshopmanagement.ui.main.order.shippingorder.ShippingOrderFragment
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragmentTabLayout : Fragment() {
    private lateinit var binding: FragmentTabLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTabLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragments = listOf(
            OrderPrepareFragment(),
            ShippingOrderFragment(),
            DeliveredOrderFragment()
        )
        val adapter = ViewPager2Adapter(requireActivity(), fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = true
        binding.apply {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {tab.text = "Chuẩn bị"}
                    1 -> {tab.text = "Đang giao"}
                    2 -> {tab.text = "Đã giao"}
                }
            }.attach()
        }
    }
}