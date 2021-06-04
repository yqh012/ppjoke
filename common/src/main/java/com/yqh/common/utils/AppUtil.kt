package com.yqh.common.utils

import android.annotation.SuppressLint
import android.app.Application
import android.util.TypedValue

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
object AppUtil {
    val application: Application by lazy {
        Class.forName("android.app.ActivityThread")
            .getDeclaredMethod("currentApplication")
            .invoke(null) as Application
    }

    /**
     * dp 转 px 转换方法
     */
    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            application.resources.displayMetrics
        ).toInt()
    }
}