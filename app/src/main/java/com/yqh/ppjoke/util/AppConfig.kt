package com.yqh.ppjoke.util

import com.yqh.common.utils.AppUtil
import com.yqh.common.utils.MoshiUtils
import com.yqh.ppjoke.model.BottomBar
import com.yqh.ppjoke.model.Destination

/**
 * 获取 Fragment 或者 Activity 页面 navigation 路由结构
 */
fun getDestConfig(): MutableMap<String, Destination> =
    MoshiUtils.mapFromJson(parseFile("destination.json"))

/**
 * 获取底部导航栏 导航布局及内容摆放规则
 */
fun getBottomBarConfig(): BottomBar? =
    MoshiUtils.fromJson(parseFile("main_tabs_config.json"))

fun parseFile(fileName: String): String {
    return AppUtil.application.resources.assets.open(fileName).use {
        it.bufferedReader().readText()
    }
}