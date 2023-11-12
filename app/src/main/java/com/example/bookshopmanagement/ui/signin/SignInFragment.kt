package com.example.bookshopmanagement.ui.signin

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.api.RetrofitClient
import com.example.bookshopmanagement.data.model.response.AuthResponse
import com.example.bookshopmanagement.data.model.response.User
import com.example.bookshopmanagement.data.model.response.author.AuthorResponse
import com.example.bookshopmanagement.databinding.FragmentSignInBinding
import com.example.bookshopmanagement.ui.main.MainMenuFragment
import com.example.bookshopmanagement.utils.AlertMessageViewer
import com.example.bookshopmanagement.utils.LoadingProgressBar
import com.example.bookshopmanagement.utils.MySharedPreferences

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel
    private var binding: FragmentSignInBinding? = null
    private var checkVisible = false
    private lateinit var loadingProgressBar: LoadingProgressBar
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        val accessToken = MySharedPreferences.getAccessToken(null)
        val expiresIn = MySharedPreferences.getLogInTime("ExpiresIn", 0)
        val loginTimeFirst = MySharedPreferences.getLogInTime("FirstTime", 0)
        if (accessToken != null) {
            val loginTime = System.currentTimeMillis()
            navToMainScreen()
            RetrofitClient.updateAccessToken(accessToken)
            if ((loginTime - loginTimeFirst) > expiresIn) {
                MySharedPreferences.clearPreferences()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SignInFragment()).commit()
                AlertMessageViewer.showAlertDialogMessage(
                    requireContext(),
                    resources.getString(R.string.messageExpiresIn)
                )
            }
        }
        loadingProgressBar = LoadingProgressBar(requireContext())
        binding?.apply {
            layoutSignIn.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val event =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    event.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                false
            }
            buttonLogin.setOnClickListener {
                val username = editUsername.text.toString()
                val password = editPassword.text.toString()
                val user = AuthResponse(user = User(email = username, password = password))
                viewModel.checkFields(user)
                loadingProgressBar.show()
            }
//            textForgotpass.setOnClickListener {
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.container, ForgotPasswordFragment())
//                    .addToBackStack("SignIn")
//                    .commit()
//            }
            imageEye.setOnClickListener {
                val cursorPosition = editPassword.selectionEnd
                if (!checkVisible) {
                    editPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisible = true
                    imageEye.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    checkVisible = false
                    imageEye.setImageResource(R.drawable.ic_visible_eye)
                }
                if (cursorPosition >= 0) {
                    editPassword.setSelection(cursorPosition)
                }
            }
        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            if (doubleBackToExitPressedOnce) {
//                requireActivity().finish()
//            } else {
//                doubleBackToExitPressedOnce = true
//                Toast.makeText(requireContext(), "Nhấn back lần nữa để thoát", Toast.LENGTH_SHORT)
//                    .show()
//                Handler().postDelayed({
//                    doubleBackToExitPressedOnce = false
//                }, 2500)
//            }
//        }
    }

    fun initViewModel() {
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            loadingProgressBar.cancel()
            if (it?.loginResponse == null) {
                it.error.message.let { it1 ->
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        it1
                    )
                }
            } else {
                if(it.loginResponse.user.role.equals("admin")){
                    navToMainScreen()
                    MySharedPreferences.putAccessToken(it.loginResponse.accessToken)
                    val expiresIn = it.loginResponse.expiresIn.split(" ")[0].toLong()
                    MySharedPreferences.putLogInTime("ExpiresIn", expiresIn * 3600000)
                    MySharedPreferences.putLogInTime("FirstTime", System.currentTimeMillis())
                    RetrofitClient.updateAccessToken(it.loginResponse.accessToken)
                }else{
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        resources.getString(R.string.unauthorized)
                    )
                }
            }
        }
    }

    fun navToMainScreen() {
        parentFragmentManager.beginTransaction().replace(R.id.container, MainMenuFragment())
            .commit()
    }
}