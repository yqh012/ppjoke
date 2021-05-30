package com.yqh.ppjoke.ui.publish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PublishModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is publish Activity"
    }
    val text: LiveData<String> = _text
}