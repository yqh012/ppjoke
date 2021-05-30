package com.yqh.ppjoke.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Destination(
    val isFragment: Boolean,
    val asStart: Boolean,
    val needLogin: Boolean,
    val pageUrl: String,
    val className: String,
    val id: Int
)
