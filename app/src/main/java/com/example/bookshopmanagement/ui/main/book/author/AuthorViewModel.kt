package com.example.bookshopmanagement.ui.main.book.author

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshopmanagement.data.model.Author
import com.example.bookshopmanagement.data.model.request.AuthorRequest
import com.example.bookshopmanagement.data.model.response.author.AuthorInfor
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.repository.author.AuthorRepository
import com.example.bookshopmanagement.data.repository.author.AuthorRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorViewModel : ViewModel() {
    private val _author = MutableLiveData<List<Author>>()
    val author: LiveData<List<Author>> get() = _author
    private val _authorInfo = MutableLiveData<AuthorInfor>()
    val authorInFo: LiveData<AuthorInfor> get() = _authorInfo
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message
    private val authorRepo: AuthorRepository = AuthorRepositoryImp(RemoteDataSource())

    fun getAuthors(limit: Int, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authorRepo.getAuthor(limit, page)
            if (response.isSuccessful) {
                _author.postValue(response.body()?.authors)
            } else {
                Log.d("GETAUTHORS", "NULL")
            }
        }
    }

    fun getAuthor(authorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authorRepo.getAuthor(authorId)
            if (response?.isSuccessful == true) {
                _authorInfo.postValue(response.body())
            } else {
                Log.d("AUTHORNULL", "NULL")
            }
        }
    }

    fun addAuthor(author: AuthorRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authorRepo.addAuthor(author)
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

    fun checkFields(author: AuthorRequest) {
        if (author.isFieldEmpty()) {
            _message.postValue("Nhập đầy đủ thông tin tác giả")
            return
        }
        addAuthor(author)
    }
}