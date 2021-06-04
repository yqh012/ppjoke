package com.yqh.ppjoke.util

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.FragmentNavigator
import com.yqh.common.utils.AppUtil
import com.yqh.ppjoke.navigator.FixFragmentNavigator

fun NavController.build(
    activity: FragmentActivity,
    childFragmentManager: FragmentManager,
    containerId: Int
) {
    //通过 NavController 中的 navigatorProvider 对象获取到 FragmentNavigator 对象和 ActivityNavigator 对象
//    val fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator::class.java)
    val fragmentNavigator = FixFragmentNavigator(activity, childFragmentManager, containerId)
    navigatorProvider.addNavigator(fragmentNavigator)
    val activityNavigator = navigatorProvider.getNavigator(ActivityNavigator::class.java)

    val navGraph = NavGraph(NavGraphNavigator(navigatorProvider))

    getDestConfig().forEach {
        it.value.also { destination ->
            if (destination.isFragment) {
                //如果页面是一个 fragment，则创建一个 FragmentNavigator 对象
                fragmentNavigator.createDestination().apply {
                    className = destination.className
                    id = destination.id
                    addDeepLink(destination.pageUrl)
                    //把这个 FragmentNavigator 节点对象添加到 navGraph 对象当中
                    navGraph.addDestination(this)
                }
            } else {
                //如果页面是一个 activity，则创建一个 ActivityNavigator 对象
                activityNavigator.createDestination().apply {
                    id = destination.id
                    addDeepLink(destination.pageUrl)
                    setComponentName(
                        ComponentName(
                            AppUtil.application.packageName,
                            destination.className
                        )
                    )
                    //把这个 ActivityNavigator 节点对象添加到 navGraph 对象当中
                    navGraph.addDestination(this)
                }
            }
            if (destination.asStart) navGraph.startDestination = destination.id
        }
    }
    graph = navGraph
}