package com.example.roomkotlinsample.tools

import android.text.TextUtils
import android.util.Log
import com.example.roomkotlinsample.BuildConfig

class Utils {

    fun printLog(log: String) {
        if (BuildConfig.DEBUG) {
            Log.e("ROOM_SAMPLE: ", log)
        }
    }

    fun checkisEmail(emailString: String): Boolean {
        return if(TextUtils.isEmpty(emailString))
            false
        else{
            android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
        }
    }

}