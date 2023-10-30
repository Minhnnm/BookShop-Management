package com.example.bookshopmanagement.ui.main.book

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshopmanagement.data.model.request.ProductRequest
import com.example.bookshopmanagement.data.model.response.product.ProductState
import com.example.bookshopmanagement.data.repository.book.BookRepository
import com.example.bookshopmanagement.data.repository.book.BookRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val _productState = MutableLiveData<ProductState>()
    val bookList: LiveData<ProductState> get() = _productState
    private val _productRequest = MutableLiveData<ProductRequest>()
    val productRequest: LiveData<ProductRequest> get() = _productRequest
    private val bookRepo: BookRepository = BookRepositoryImp(RemoteDataSource())
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message
    private var job: Job? = null

    fun getProducts(limit: Int, page: Int, description: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepo.getProducts(limit, page, description)
            if (response.isSuccessful) {
                _productState.postValue(ProductState(response.body()?.products, true))
            } else {
                Log.d("GetProduct", "NULL")
            }
        }
    }

    fun getSearchProducts(limit: Int, page: Int, description: Int, query: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepo.getSearchProducts(limit, page, description, query)
            if (response.isSuccessful) {
                _productState.postValue(ProductState(response.body()?.products, false))
            } else {
                Log.d("SearchProducts", "NULL")
            }
        }
    }

    fun getBookDetail(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepo.getBookDetail(productId)
            if (response.isSuccessful) {
                _productRequest.postValue(response.body())
            } else {
                Log.d("GetBookDetail", "NULL")
            }
        }
    }

    fun deleteBook(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepo.deleteBook(productId)
            if (response.isSuccessful) {
                _message.postValue(response.body()?.message)
            } else {
                Log.d("DeleteBook", "NUll")
            }
        }
    }
}