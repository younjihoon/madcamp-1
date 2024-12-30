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

    /////////

//    private val _travels = MutableLiveData<List<Travel>>(emptyList())
//    val travels: LiveData<List<Travel>> get() = _travels
//
//    fun setNewTravel(travel: Travel) {
//        val updatedList = _travels.value.orEmpty().toMutableList()
//        updatedList.add(travel)
//        _travels.value = updatedList
//    }

    private val _travels = MutableLiveData<MutableList<Travel>>(mutableListOf())
    val travels: LiveData<MutableList<Travel>> get() = _travels

    fun setNewTravel(travel: Travel) {
        _travels.value?.add(travel)
        _travels.value = _travels.value // LiveData 업데이트 트리거
        Log.e("LOGGER","added ${_travels.value}")
    }
    fun addPhotoToDayInfo(travelTitle: String, newDayInfo: DayInfo) {
        val currentTravels = _travels.value
        if (currentTravels != null) {
            Log.e("NOTNULL","currentTravels is not null in shared viewmodel")
            val targetTravel = currentTravels.find { it.title == travelTitle }
            if (targetTravel == null) {
                Log.e("NULL","targetTravel is null $currentTravels,$travelTitle ")
                return
            }
            targetTravel.DayInfos.add(newDayInfo)
            Log.e("NOTNULL","targetTravel is not null in shared viewmodel")
        }
        else {
            Log.e("NULL","currentTravels is null in shared viewmodel")
        }
    }

}
