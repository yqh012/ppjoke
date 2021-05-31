package com.yqh.ppjoke.ui.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yqh.libannotation.FragmentDestination
import com.yqh.ppjoke.R
import com.yqh.ppjoke.ui.find.FindFragment

@FragmentDestination(pageUrl = "main/tabs/my")
class MyFragment : Fragment() {
    private val TAG = MyFragment::class.simpleName
    private lateinit var notificationsViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(MyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        Log.e(TAG, "MyFragment onCreateView.")
        return root
    }
}