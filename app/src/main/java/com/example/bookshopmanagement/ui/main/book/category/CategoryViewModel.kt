package com.example.bookshopmanagement.ui.main.book.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.Category
import com.example.bookshopmanagement.data.model.request.CategoryRequest
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.repository.category.CategoryRepository
import com.example.bookshopmanagement.data.repository.category.CategoryRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>> get() = _category
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message
    private val categoryRepo: CategoryRepository = CategoryRepositoryImp(RemoteDataSource())

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = categoryRepo.getCategroies()
            if (response.isSuccessful) {
                _category.postValue(response.body()?.categories)
            } else {
                Log.d("GetCategory", "NULL")
            }
        }
    }

    fun addCategory(category: CategoryRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = categoryRepo.addCategory(category)
            if(response.isSuccessful){
                _message.postValue(response.body()?.message)
            }else{
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, Message::class.java)
                _message.postValue(errorResponse.message)
            }
        }
    }
}