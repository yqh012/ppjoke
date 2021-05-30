package com.yqh.ppjoke

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yqh.libannotation.ActivityDestination
import com.yqh.ppjoke.util.build

@ActivityDestination(pageUrl = "main/tabs/mainActivity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        navController.build()

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
}