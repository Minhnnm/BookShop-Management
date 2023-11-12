package com.example.bookshopmanagement.ui.main.book.category

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
import com.example.bookshopmanagement.databinding.FragmentCategoryBinding
import com.example.bookshopmanagement.databinding.LayoutAddSupplierBinding
import com.example.bookshopmanagement.ui.adapter.BaseAdapter
import com.example.bookshopmanagement.ui.adapter.OnItemClickListener
import com.example.bookshopmanagement.ui.main.book.addbook.AddBookFragment
import com.example.bookshopmanagement.ui.main.book.addbook.AddBookViewModel

class CategoryFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private lateinit var viewModelAddBook: AddBookViewModel
    private var binding: FragmentCategoryBinding? = null
    private lateinit var adapter: BaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        viewModelAddBook = ViewModelProvider(requireActivity())[AddBookViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
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
        adapter = BaseAdapter(true)
        viewModel.getCategories()
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            recyclerCategory.adapter = adapter
            recyclerCategory.layoutManager = LinearLayoutManager(context)
            imageAddCategory.setOnClickListener {
                bindingAlert.textAlert.text = resources.getString(R.string.add_category)
                bindingAlert.editDescription.visibility=View.GONE
                val categoryName = bindingAlert.editName.text
                bindingAlert.textConfirm.setOnClickListener {
                    if (categoryName.isEmpty()) {
                        Toast.makeText(context, "Nhập tên thể loại cần thêm", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val category = CategoryRequest(name = categoryName.toString())
                        viewModel.addCategory(category)
                        viewModel.getCategories()
                        bindingAlert.editName.setText("")
                        bindingAlert.editName.hint=resources.getString(R.string.enter_category_name)
                        dialog.dismiss()
                    }
                }
                bindingAlert.textCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
//        val positionSelected = arguments?.getString("positionSelected")
        selectCategory()
    }

    private fun initViewModel() {
        viewModel.category.observe(viewLifecycleOwner) { categories ->
            adapter.setCategories(categories)
        }
        viewModelAddBook.positionCategory.observe(viewLifecycleOwner) { positionSelected ->
            positionSelected?.let {
                adapter.setPositonSelected(positionSelected.toInt())
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectCategory() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val category = adapter.getCategory(position)
                viewModelAddBook.setPositionCategory(position)
                viewModelAddBook.setCategory(category)
                parentFragmentManager.popBackStack()
            }
        })
    }
}