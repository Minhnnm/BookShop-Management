package com.example.bookshopmanagement.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshopmanagement.data.model.Order
import com.example.bookshopmanagement.data.model.Product
import com.example.bookshopmanagement.data.model.response.User
import com.example.bookshopmanagement.data.model.response.category.CategoryBestSeller
import com.example.bookshopmanagement.data.repository.book.BookRepository
import com.example.bookshopmanagement.data.repository.book.BookRepositoryImp
import com.example.bookshopmanagement.data.repository.category.CategoryRepository
import com.example.bookshopmanagement.data.repository.category.CategoryRepositoryImp
import com.example.bookshopmanagement.data.repository.order.OrderRepository
import com.example.bookshopmanagement.data.repository.order.OrderRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private var _totalOrder = MutableLiveData<Int>()
    val totalOrder: LiveData<Int> get() = _totalOrder
    private var _revenueYear = MutableLiveData<Long>()
    val revenueYear: LiveData<Long> get() = _revenueYear
    private var _revenueMonth = MutableLiveData<List<Long>>()
    val revenueMonth: LiveData<List<Long>> get() = _revenueMonth
    private var _revenueToday = MutableLiveData<Long>()
    val revenueToday: LiveData<Long> get() = _revenueToday
    private var _books = MutableLiveData<List<Product>>()
    val books: LiveData<List<Product>> get() = _books
    private var _category = MutableLiveData<List<CategoryBestSeller>>()
    val categoryBestSeller: LiveData<List<CategoryBestSeller>> get() = _category
    private val orderRepo: OrderRepository = OrderRepositoryImp(RemoteDataSource())
    private val bookRepo: BookRepository = BookRepositoryImp(RemoteDataSource())
    private val categoryRepo: CategoryRepository = CategoryRepositoryImp(RemoteDataSource())

    fun getRevenueYear(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = orderRepo.getOrdersByYear(year)
            if (response.isSuccessful) {
                val orders = response.body()?.orders
                orders?.let {
                    var sum = 0L
                    for (order in it) {
                        order.merchandiseSubtotal?.let { merchandiseSubtotal ->
                            sum += merchandiseSubtotal.toDouble().toLong()
                        }
                    }
                    _revenueYear.postValue(sum)
                    _totalOrder.postValue(it.size)
                }
            } else {
                Log.d("GerOrderByYear", "NULL")
            }
        }
    }

    fun getRevenueMonthOfYear(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: MutableList<Long> = mutableListOf()
            for (i in 1..12) {
                val response = orderRepo.getOrderByMonthOfYear(year, i)
                if (response.isSuccessful) {
                    val orders = response.body()?.orders
                    orders?.let {
                        var sum = 0L
                        for (order in it) {
                            order.merchandiseSubtotal?.let { merchandiseSubtotal ->
                                sum += merchandiseSubtotal.toDouble().toLong()
                            }
                        }
                        list.add(sum)
                    }
                } else {
                    Log.d("GetOrderByMonth", "NULL")
                }
            }
            _revenueMonth.postValue(list)
        }
    }

    fun getOrderByToday(today: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = orderRepo.getOrderByToday(today)
            if (response.isSuccessful) {
                val orders = response.body()?.orders
                orders?.let {
                    var sum = 0L
                    for (order in it) {
                        order.merchandiseSubtotal?.let { merchandiseSubtotal ->
                            sum += merchandiseSubtotal.toDouble().toLong()
                        }
                    }
                    _revenueToday.postValue(sum)
                }
            } else {
                Log.d("GetOrderByToday", "NULL")
            }
        }
    }

    fun getProductBestSeller() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepo.getBookBestSeller()
            if (response.isSuccessful) {
                _books.postValue(response.body()?.products)
            } else {
                Log.d("GetBookBestSeller", "NULL")
            }
        }
    }

    fun getCategoryBestSeller() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = categoryRepo.getCategoryBestSeller()
            if (response.isSuccessful) {
                _category.postValue(response.body())
            } else {
                Log.d("GetCategoryBestSeller", "NULL")
            }
        }
    }
}