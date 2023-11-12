package com.example.bookshopmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookshopmanagement.databinding.ActivityMainBinding
import com.example.bookshopmanagement.ui.main.MainMenuFragment
import com.example.bookshopmanagement.ui.signin.SignInFragment
import com.example.bookshopmanagement.utils.MySharedPreferences

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        MySharedPreferences.init(this)
        setContentView(binding?.root)
        val support = supportFragmentManager.beginTransaction()
        support.replace(R.id.container, SignInFragment()).commit()
    }
}