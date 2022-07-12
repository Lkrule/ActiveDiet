package com.example.activediet.utilities.run
import java.util.*
import java.util.concurrent.TimeUnit


object TimeFormatter {

    fun formatTime(ms: Long, isIncludeMs: Boolean = false): String{
        val hrs = TimeUnit.MILLISECONDS.toHours(ms) % 24
        val min = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
        val sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        val mls = ((ms % 1000) / 10).toInt()

        return if(!isIncludeMs) {
            String.format("%02d:%02d:%02d", hrs, min, sec)
        } else {
            String.format("%02d:%02d:%02d:%02d", hrs, min, sec, mls)
        }
    }
}