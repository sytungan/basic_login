package com.ok.vinova_test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShareDataViewModel() : ViewModel() {
    private var _username = MutableStateFlow("")
    var username = _username.asStateFlow()
    val user = MutableLiveData<String>()

    fun setValue(v: String) {
        println(v)
        viewModelScope.launch {
            _username.emit(v)
        }
    }
}