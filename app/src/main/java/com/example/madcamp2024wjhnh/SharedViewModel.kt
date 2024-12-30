package com.example.madcamp2024wjhnh

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp2024wjhnh.data.Photo
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.data.TravelR

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

    fun getPhotos(): List<Photo> {
        return _photos.value ?: emptyList()
    }

    fun updatePhoto(updatedPhoto: Photo) {
        val updatedPhotos = _photos.value?.map { photo ->
            if (photo.imageResId == updatedPhoto.imageResId) updatedPhoto else photo
        } ?: return // null인 경우 함수 종료

        _photos.value = updatedPhotos
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

    private val _travels = MutableLiveData<MutableList<TravelR>>(mutableListOf())
    val travels: LiveData<MutableList<TravelR>> get() = _travels

    fun setNewTravel(travel: TravelR) {
        _travels.value?.add(travel)
        _travels.value = _travels.value // LiveData 업데이트 트리거
    }

    fun updateTravel(position: Int, updatedTravel: TravelR) {
        val updatedTravels = _travels.value?.toMutableList() ?: mutableListOf()
        updatedTravels[position] = updatedTravel
        _travels.value = updatedTravels
    }

    fun deleteTravel(position: Int) {
        val updatedList = _travels.value?.toMutableList() ?: mutableListOf()
        updatedList.removeAt(position)
        _travels.value = updatedList
    }

    fun replaceTravel(travels : List<TravelR>) {
        _travels.value = travels.toMutableList()
    }



}
