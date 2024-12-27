package com.example.madcamp2024wjhnh.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val imageResId: Int,       // 리소스 ID (사진 이미지)
    val title: String,         // 사진 제목
    val description: String,    // 사진 설명
    val link: String           // 사진 링크
) : Parcelable
