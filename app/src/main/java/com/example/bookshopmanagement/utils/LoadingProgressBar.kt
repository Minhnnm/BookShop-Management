package com.example.bookshopmanagement.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.bookshopmanagement.R


class LoadingProgressBar(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }
}