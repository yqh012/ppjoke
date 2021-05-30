package com.yqh.ppjoke.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BottomBar(
    //选中状态文本颜色
    val activeColor: String,
    //未选中状态文本颜色
    val inActiveColor: String,
    //默认选中的 selectTab
    val selectTab: Int,
    //展示的 tab 元素
    val tabs: List<BottomInfo>
)

@JsonClass(generateAdapter = true)
data class BottomInfo(
    // tab size大小
    val size: Int,
    // tab 配置是否可用, true:可用； false:不可用
    val enable: Boolean,
    // tab 的定义顺序，从小到大一次对应从左到右
    val index: Int,
    // 对应的点击跳转路由地址
    val pageUrl: String,
    // tab 的文本标题
    val title: String?,
    //中间标题为空按钮的着色值
    val tintColor: String?
)


