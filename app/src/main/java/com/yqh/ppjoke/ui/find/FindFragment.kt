package com.yqh.ppjoke.ui.find

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
import com.yqh.ppjoke.ui.home.HomeFragment

@FragmentDestination(pageUrl = "main/tabs/find")
class FindFragment : Fragment() {
    private val TAG = FindFragment::class.simpleName
    private lateinit var dashboardViewModel: FindViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(FindViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        Log.e(TAG, "FindFragment onCreateView.")

        return root
    }
}