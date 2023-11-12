package com.example.bookshopmanagement.ui.main.book.publisher

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.request.CategoryRequest
import com.example.bookshopmanagement.data.model.request.SupplierRequest
import com.example.bookshopmanagement.databinding.FragmentPublisherBinding
import com.example.bookshopmanagement.databinding.LayoutAddSupplierBinding
import com.example.bookshopmanagement.ui.adapter.BaseAdapter
import com.example.bookshopmanagement.ui.adapter.OnItemClickListener
import com.example.bookshopmanagement.ui.main.book.addbook.AddBookFragment
import com.example.bookshopmanagement.ui.main.book.addbook.AddBookViewModel

class PublisherFragment : Fragment() {

    companion object {
        fun newInstance() = PublisherFragment()
    }

    private lateinit var viewModel: PublisherViewModel
    private lateinit var viewModelAddBook: AddBookViewModel
    private var binding: FragmentPublisherBinding? = null
    private lateinit var adapter: BaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PublisherViewModel::class.java]
        viewModelAddBook = ViewModelProvider(requireActivity())[AddBookViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPublisherBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        val bindingAlert = LayoutAddSupplierBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
            .setView(bindingAlert.root)
        val dialog = builder.create()
        dialog.setCancelable(false)
        adapter = BaseAdapter(false)
        viewModel.getSuppliers()
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            recyclerPublisher.adapter = adapter
            recyclerPublisher.layoutManager = LinearLayoutManager(context)
            imageAddPublisher.setOnClickListener {
                bindingAlert.textAlert.text = resources.getString(R.string.add_supplier)
                bindingAlert.editName.hint = resources.getString(R.string.enter_supplier_name)
                val supplierName = bindingAlert.editName.text
                bindingAlert.textConfirm.setOnClickListener {
                    if (supplierName.isEmpty()) {
                        Toast.makeText(context, "Nhập tên nhà xuất bản cần thêm", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val supplier = SupplierRequest(name = supplierName.toString())
                        viewModel.addSupplier(supplier)
                        viewModel.getSuppliers()
                        bindingAlert.editName.setText("")
                        bindingAlert.editName.hint =
                            resources.getString(R.string.enter_supplier_name)
                        dialog.dismiss()
                    }
                }
                bindingAlert.textCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        selectSupplier()
    }

    private fun initViewModel() {
        viewModel.publisher.observe(viewLifecycleOwner) { publishers ->
            adapter.setPublisher(publishers)
        }
        viewModelAddBook.positionSupplier.observe(viewLifecycleOwner) { positionSelected ->
            positionSelected?.let {
                adapter.setPositonSelected(positionSelected.toInt())
            }
        }
        viewModel.message.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectSupplier() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val supplier = adapter.getSupplier(position)
                viewModelAddBook.setPositionSupplier(position)
                viewModelAddBook.setSupplier(supplier)
                parentFragmentManager.popBackStack()
            }
        })
    }
}