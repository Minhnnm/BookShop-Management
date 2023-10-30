package com.example.bookshopmanagement.ui.main.order.orderdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshopmanagement.data.model.OrderDetail
import com.example.bookshopmanagement.data.repository.order.OrderRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailViewModel : ViewModel() {
    private val _orderDetailList = MutableLiveData<OrderDetail>()
    val orderDetailList: MutableLiveData<OrderDetail> get() = _orderDetailList
    private var orderRepository: OrderRepositoryImp =OrderRepositoryImp(RemoteDataSource())

    fun getOrderDetails(orderId:Int) {
        viewModelScope.launch(Dispatchers.IO){
            val response=orderRepository.getOrderDetail(orderId)
            if(response?.isSuccessful==true){
                _orderDetailList.postValue(response.body())
            }else{
                Log.d("OrderDetailNULL", "NULL")
            }
        }
    }
}