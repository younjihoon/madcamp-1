package com.example.madcamp2024wjhnh

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.Photo
import com.example.madcamp2024wjhnh.data.Travel

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> get() = _photos

    // photos 초기화
    fun setPhotos(photoList: List<Photo>) {
        _photos.value = photoList
        Log.e("LOGGER","added ${_photos.value}")
    }

    // 즐겨찾기 필터링
    fun getFavoritePhotos(): List<Photo> {
        return _photos.value?.filter { it.isFavorite } ?: emptyList()
    }

    fun getPhotos(): List<Photo> {
        return _photos.value ?: emptyList()
    }
}
