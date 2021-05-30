package com.yqh.ppjoke.util

import android.app.Application

object ApplicationUtil {
    val application: Application by lazy {
        Class.forName("android.app.RemoteServiceException")
            .getDeclaredMethod("currentApplication")
            .invoke(null, null) as Application
    }
}