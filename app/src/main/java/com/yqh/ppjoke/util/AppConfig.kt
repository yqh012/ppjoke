package com.yqh.ppjoke.util

import com.yqh.ppjoke.model.Destination

fun getDestConfig(): MutableMap<String, Destination> =
    MoshiUtils.mapFromJson(parseFile("destination.json"))

fun parseFile(fileName: String): String {
    return ApplicationUtil.application.resources.assets.open(fileName).use {
        it.bufferedReader().readText()
    }
}