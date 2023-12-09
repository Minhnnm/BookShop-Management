package com.example.bookshopmanagement.ui.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshopmanagement.data.model.response.AuthResponse
import com.example.bookshopmanagement.data.model.response.AuthState
import com.example.bookshopmanagement.data.model.response.error.ErrorResponse
import com.example.bookshopmanagement.data.repository.user.UserRepository
import com.example.bookshopmanagement.data.repository.user.UserRepositoryImp
import com.example.bookshopmanagement.datasource.remote.RemoteDataSource
import com.example.bookshopmanagement.data.model.response.error.Error
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private var _loginResponse = MutableLiveData<AuthState>()
    val loginResponse: LiveData<AuthState> get() = _loginResponse
    private val authRepository: UserRepository = UserRepositoryImp(RemoteDataSource())
    fun checkFields(user: AuthResponse) {

        if (user.isSignInFieldEmpty()) {
            _loginResponse.postValue(
                AuthState(
                    Error(message = "Các trường không được để trống!"),
                    null
                )
            )
            return
        }

        if (!user.isValidEmail()) {
            _loginResponse.postValue(
                AuthState(
                    Error(message = "Vui lòng nhập địa chỉ email của bạn!"),
                    null
                )
            )
            return
        }

        if (!user.isPasswordGreaterThan5(user.user.password)) {
            _loginResponse.postValue(
                AuthState(
                    Error(
                        message =
                        "Mật khẩu phải dài hơn 5 kí tự bao gồm cả chữ và số"
                    ),
                    null
                )
            )
            return
        }
        checkLogin(user)

    }

    fun checkLogin(user: AuthResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.login(user.user.email, user.user.password)
            if (response?.isSuccessful == true ) {
                _loginResponse.postValue(
                    AuthState(
                        Error(message = "Đăng nhập thành công!"),
                        response.body()
                    )
                )
            } else {
                val errorBody = response?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _loginResponse.postValue(
                    AuthState(
                        Error(message = errorResponse.error.message),
                        null
                    )
                )
                Log.d("LoginNull", "NULL")
            }
        }
    }
}