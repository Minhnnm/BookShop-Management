package com.example.BookShopApp.utils.format

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FormatDate {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(str: String?): String {
        val str_new = LocalDate.parse(str?.substring(0, 10))
        return str_new.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun convertToUTCVN(dateTimeString: String?):String{
//        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
//        val dateTime = OffsetDateTime.parse(dateTimeString, formatter)
//        return dateTime.withOffsetSameInstant(ZoneOffset.ofHours(7)).toLocalTime().toString()
//    }
}