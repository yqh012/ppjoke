package com.yqh.libannotation

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class ActivityDestination(
    val pageUrl: String,
    val needLogin: Boolean = false,
    val asStart: Boolean = false
)
