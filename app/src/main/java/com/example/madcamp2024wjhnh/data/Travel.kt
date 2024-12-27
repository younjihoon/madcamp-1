package com.example.madcamp2024wjhnh.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Travel(
    val title: String,
    val address: String,
    val tags: String,
    val description: String,
    val photoResId: Int,
    val photoListday1: List<Int>, // 사진 리스트
    val photoListday2: List<Int> // 사진 리스트
) : Parcelable

//
//@Parcelize
//data class Travel(
//    val title: String,
//    val address: String,
//    val tags: String,
//    val description: String,
//    val photoResId: Int, // 대표 이미지
//    val photoList: List<Int> // 여러 이미지 리스트
//) : Parcelable