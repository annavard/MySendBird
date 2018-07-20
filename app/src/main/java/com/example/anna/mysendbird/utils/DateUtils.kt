package com.example.anna.mysendbird.utils

import android.util.Log
import com.example.anna.mysendbird.view.activity.MainActivity
import java.util.*

class DateUtils {


    companion object {
        const val TAG = "DateUtils"

        fun millisToDate(millis: Long) : String {
            Log.d(MainActivity.TAG, "millis - $millis")

            var date = Date(millis)
            Log.d(MainActivity.TAG, "time - ${date.time}")

            return date.time.toString()
        }
    }
}