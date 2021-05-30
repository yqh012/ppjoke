package com.yqh.ppjoke.util

import android.annotation.SuppressLint
import android.app.Application

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
object ApplicationUtil {
    val application: Application by lazy {
        Class.forName("android.app.ActivityThread")
            .getDeclaredMethod("currentApplication")
            .invoke(null) as Application
    }
}