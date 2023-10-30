package com.example.bookshopmanagement.ui.main.book.updatebook

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.request.ProductRequest
import com.example.bookshopmanagement.databinding.FragmentAddBookBinding
import com.example.bookshopmanagement.ui.main.book.author.AuthorFragment
import com.example.bookshopmanagement.ui.main.book.category.CategoryFragment
import com.example.bookshopmanagement.ui.main.book.publisher.PublisherFragment

class UpdateBookFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateBookFragment()
    }

    private lateinit var viewModel: UpdateBookViewModel
    private var binding: FragmentAddBookBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateBookViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBinding.inflate(layoutInflater, container, false)
        binding?.apply {
            textBook.setText(R.string.update_book)
            textAddBook.setText(R.string.update)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book = arguments?.getSerializable("book") as ProductRequest
        bindData(book)
        binding?.apply {

            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            editBookAuthor.setOnClickListener {
//                viewModel.clearMessage()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, AuthorFragment())
                    .addToBackStack("AddBook").commit()
            }
            editBookCategory.setOnClickListener {
//                viewModel.clearMessage()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, CategoryFragment())
                    .addToBackStack("AddBook").commit()
            }
            editBookSupplier.setOnClickListener {
//                viewModel.clearMessage()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, PublisherFragment())
                    .addToBackStack("AddBook").commit()
            }
        }
    }
    fun bindData(book:ProductRequest){
        binding?.apply {
            Glide.with(root)
                .load(book.thumbnail)
                .centerCrop().into(imageAvatar)
            editBookName.setText(book.name)
            editDescription.setText(book.description)
            editBookPrice.setText(book.price)
            editBookQuantity.setText(book.quantity.toString())
            editBookDiscountedprice.setText(book.discounted_price)
            editBookAuthor.setText(book.author?.authorName)
            editBookCategory.setText(book.category?.name)
            editBookSupplier.setText(book.supplier?.name)
        }
    }
}