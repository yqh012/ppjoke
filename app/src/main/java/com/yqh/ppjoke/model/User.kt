package com.yqh.ppjoke.model

import com.yqh.libannotation.ModelMap

@ModelMap
data class User(
    val name: String,
    val age: Int,
    val address: String,
    val number: Int
)
