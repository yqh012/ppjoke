package com.yqh.ppjoke.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.yqh.ppjoke.R
import com.yqh.ppjoke.util.dp2px
import com.yqh.ppjoke.util.getBottomBarConfig
import com.yqh.ppjoke.util.getDestConfig

//fun main() {
//    val status = Array(2) { IntArray(1) }
//    status[0] = IntArray(1) { 1 }
//
//    status.forEach {
//
//        it.forEach { aaa ->
//            println(aaa)
//        }
//
//    }
//}


@SuppressLint("RestrictedApi")
class AppBottomBar(context: Context, attrs: AttributeSet) : BottomNavigationView(context, attrs) {

    companion object {
        val icons = arrayListOf(
            R.mipmap.icon_tab_home,
            R.mipmap.icon_tab_sofa,
            R.mipmap.icon_tab_publish,
            R.mipmap.icon_tab_find,
            R.mipmap.icon_tab_mine,
        )
    }

    init {
        val bottomBar = getBottomBarConfig()
        bottomBar?.tabs?.let {

            //设置点击效果
            val status = Array(2) { IntArray(1) }
            status[0] = IntArray(1) { android.R.attr.state_selected }

            //设置选中状态和未选中状态的效果
            val colors = arrayListOf(
                Color.parseColor(bottomBar.activeColor),
                Color.parseColor(bottomBar.inActiveColor)
            ).toIntArray()

            val colorStateList = ColorStateList(status, colors)

            //针对按钮设置对应的状态和颜色
            itemIconTintList = colorStateList
            itemTextColor = colorStateList
            /**
             * 底部导航栏 效果
             * @see LabelVisibilityMode.LABEL_VISIBILITY_AUTO 底部导航栏按钮的文本会自动判断需不需要显示
             * @see LabelVisibilityMode.LABEL_VISIBILITY_SELECTED 底部导航栏按钮的文本只有在选中的时候才会显示
             * @see LabelVisibilityMode.LABEL_VISIBILITY_LABELED 底部导航栏的文本始终显示
             * @see LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED 底部导航栏的文本始终不显示
             */
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

            for (bottomInfo in it) {
                if (!bottomInfo.enable) continue
                //获取注册的 Fragment 或者 Activity 创建时的 id (由ClassName 的 hashCode 生成的唯一标识)
                val itemId = getItemId(bottomInfo.pageUrl)
                if (itemId < 0) continue

                //创建底部导航栏 button 按钮(tab 按钮)
                val menuItem = menu.add(0, itemId, bottomInfo.index, bottomInfo.title)
                //设置图标
                menuItem.setIcon(icons.get(bottomInfo.index))
            }

//            it.forEach { bottom ->
            for (bottom in it) {
                if (!bottom.enable) continue
                val iconSized = dp2px(bottom.size.toFloat())
                ((getChildAt(0) as? BottomNavigationMenuView)?.getChildAt(bottom.index) as? BottomNavigationItemView)?.apply {
                    setIconSize(iconSized)
                    if (bottom.title.isNullOrBlank()) {
                        val tintColor =
                            if (bottom.tintColor.isNullOrBlank()) Color.parseColor("#ff678f") else Color.parseColor(
                                bottom.tintColor
                            )
                        setIconTintList(ColorStateList.valueOf(tintColor))
                        //禁止掉点按时，上下浮动的效果
                        setShifting(false)
                    }
                }
            }

            /**
             * 设置底部导航栏默认选中项
             */
            if (bottomBar.selectTab != 0) {
                it[bottomBar.selectTab].apply {
                    if (enable) {
                        val selectedId = getItemId(pageUrl)
                        //等待 AppBottomBar 控件加载完成后在设置
                        post { selectedItemId = selectedId }
                    }
                }
            }
        }
    }

    private fun getItemId(pageUrl: String): Int {
        val destination = getDestConfig()[pageUrl]
        return destination?.id ?: -1
    }
}