package com.example.bookshopmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookshopmanagement.databinding.ActivityMainBinding
import com.example.bookshopmanagement.ui.main.MainMenuFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val support = supportFragmentManager.beginTransaction()
        support.replace(R.id.container, MainMenuFragment()).commit()
    }
}