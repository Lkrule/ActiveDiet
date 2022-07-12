package com.example.activediet.utilities.run
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object TimeFormatter {

    fun formatTime(ms: Long, isIncludeMs: Boolean = false): String{
        return System.currentTimeMillis().toString()

    }
}