package com.example.bookshopmanagement.ui.main.book.publisher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.Supplier
import com.example.bookshopmanagement.data.model.request.SupplierRequest
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.repository.supplier.SupplierRepository
import com.example.bookshopmanagement.data.repository.supplier.SupplierRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PublisherViewModel : ViewModel() {
    private val _publisher = MutableLiveData<List<Supplier>>()
    val publisher: LiveData<List<Supplier>> get() = _publisher
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message
    private val supplierRepo: SupplierRepository = SupplierRepositoryImp(RemoteDataSource())

    fun getSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = supplierRepo.getSuppliers()
            if (response.isSuccessful) {
                _publisher.postValue(response.body()?.suppliers)
            } else {
                Log.d("GetCategory", "NULL")
            }
        }
    }

    fun addSupplier(supplierRequest: SupplierRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = supplierRepo.addSupplier(supplierRequest)
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
}