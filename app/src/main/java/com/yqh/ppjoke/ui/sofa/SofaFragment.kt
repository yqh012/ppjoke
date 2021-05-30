package com.yqh.ppjoke.ui.sofa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yqh.libannotation.FragmentDestination
import com.yqh.ppjoke.R

@FragmentDestination(pageUrl = "main/tabs/sofa")
class SofaFragment : Fragment() {

    private lateinit var SofaViewModelViewModel: SofaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SofaViewModelViewModel =
            ViewModelProvider(this).get(SofaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sofa, container, false)
        val textView: TextView = root.findViewById(R.id.text_sofa)
        SofaViewModelViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}