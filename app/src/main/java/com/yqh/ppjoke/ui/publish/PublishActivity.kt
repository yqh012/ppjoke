package com.yqh.ppjoke.ui.publish

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yqh.libannotation.ActivityDestination
import com.yqh.ppjoke.R
import com.yqh.ppjoke.ui.my.MyFragment

@ActivityDestination(pageUrl = "main/tabs/publish")
class PublishActivity : AppCompatActivity() {
    private val TAG = PublishActivity::class.simpleName
    private lateinit var publishViewModel: PublishModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)

        publishViewModel =
            ViewModelProvider(this).get(PublishModel::class.java)

        val textView: TextView = findViewById(R.id.text_publish)
        publishViewModel.text.removeObserver {
            textView.text = it
        }
        Log.e(TAG, "PublishActivity onCreate.")
    }

}