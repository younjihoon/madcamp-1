package com.example.madcamp2024wjhnh.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp2024wjhnh.data.Photo

class SharedViewModel : ViewModel() {

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> get() = _photos

    // photos 초기화
    fun setPhotos(photoList: List<Photo>) {
        _photos.value = photoList
    }

    // 즐겨찾기 필터링
    fun getFavoritePhotos(): List<Photo> {
        return _photos.value?.filter { it.isFavorite } ?: emptyList()
    }
}
