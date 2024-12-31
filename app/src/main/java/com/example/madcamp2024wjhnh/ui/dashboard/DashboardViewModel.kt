package com.example.madcamp2024wjhnh.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp2024wjhnh.data.Section

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    private val _sections = mutableListOf<Section>()
    val sections: MutableList<Section> = _sections
}