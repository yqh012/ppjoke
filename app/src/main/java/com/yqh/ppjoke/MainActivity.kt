package com.yqh.ppjoke

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yqh.libannotation.ActivityDestination
import com.yqh.ppjoke.util.build
import com.yqh.ppjoke.util.getDestConfig

@ActivityDestination(pageUrl = "main/tabs/mainActivity")
class MainActivity : AppCompatActivity() {
    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }
    private val navView: BottomNavigationView by lazy {
        findViewById(R.id.nav_view)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        navView = findViewById(R.id.nav_view)
//        navController = findNavController(R.id.nav_host_fragment)
//        navView.setupWithNavController(navController)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController.build(this, fragment!!.childFragmentManager, fragment.id)

        //把 BottomNavigation 和 我们的 navController 点击跳转事件关联起来
        navView.setOnNavigationItemSelectedListener {
            navController.navigate(it.itemId)
            /**
             * 最后的返回值 Boolean
             * 如果返回 false ，代表这个按钮不会被选中，也就不会被设置选中的着色效果
             * 如果返回 true ， 代表这个按钮会被选中，也就是会有点击的着色效果
             * 此处我们通过判断 menu 的 Title 是否为空来判断它是否可以被选中
             */
            !it.title.isNullOrBlank()
        }

    }

    override fun onBackPressed() {
        //获取当前正在显示页面的 destinationId
        val id = navController.currentDestination!!.id
        //获取 app 页面路由导航结构图，首页的 destinationId
        val homeDestId = navController.graph.startDestination
        //如果当前页面不是首页，并且点击了返回键，则拦截，并跳转到首页tab
        if (id != homeDestId) {
            navView.selectedItemId = homeDestId
//            finish()
            return
        }
        //否则则直接finish，此处不可以调用 onBackPressed，因为 navigation 会操作回退栈，切换到之前显示的页面
        finish()
    }
}