package com.example.bookshopmanagement.ui.main.book.addbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.Category
import com.example.BookShopApp.data.model.Supplier
import com.example.bookshopmanagement.data.model.Author
import com.example.bookshopmanagement.data.model.request.ProductRequest
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.repository.book.BookRepository
import com.example.bookshopmanagement.data.repository.book.BookRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBookViewModel : ViewModel() {
    private val _author = MutableLiveData<Author?>()
    val author: LiveData<Author?> get() = _author
    private val _positionCategory = MutableLiveData<Int>()
    val positionCategory: LiveData<Int> get() = _positionCategory
    private val _positionSupplier = MutableLiveData<Int>()
    val positionSupplier: LiveData<Int> get() = _positionSupplier
    private val _category = MutableLiveData<Category?>()
    val category: LiveData<Category?> get() = _category
    private val _publisher = MutableLiveData<Supplier?>()
    val publisher: LiveData<Supplier?> get() = _publisher

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message
    private val productRepo: BookRepository = BookRepositoryImp(RemoteDataSource())

    fun setAuthor(author: Author?) {
        _author.value = author
    }

    fun setCategory(category: Category?) {
        _category.value = category
    }

    fun setSupplier(supplier: Supplier?) {
        _publisher.value = supplier
    }

    fun setPositionCategory(position: Int) {
        _positionCategory.value = position
    }

    fun setPositionSupplier(position: Int) {
        _positionSupplier.value = position
    }

    fun addBook(productRequest: ProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepo.addBook(productRequest)
            if (response.isSuccessful) {
                _message.postValue(response.body()?.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, Message::class.java)
                _message.postValue(errorResponse.message)
            }
        }
    }

    fun updateBook(productRequest: ProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepo.updateBook(productRequest)
            if (response.isSuccessful) {
                _message.postValue(response.body()?.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, Message::class.java)
                _message.postValue(errorResponse.message)
            }
        }
    }

    fun checkField(productRequest: ProductRequest, isAdd: Boolean) {
        if (productRequest.isFieldEmpty()) {
            _message.postValue("Nhập đầy đủ thông tin sản phẩm cần thêm")
            return
        } else if (!productRequest.isPriceThanDiscountedPrice()) {
            _message.postValue("Đơn giá khuyến mại phải nhỏ hơn đơn giá của sản phẩm")
        } else {
            if (isAdd) {
                addBook(productRequest)
            } else {
                updateBook(productRequest)
            }
        }
    }

    fun clear() {
        _author.value = Author()
        _category.value = Category()
        _publisher.value = Supplier()
        _positionSupplier.value = -1
        _positionCategory.value = -1
        _message.value = null
    }

    fun clearMessage() {
        _message.value = null
    }
}