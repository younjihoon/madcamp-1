package com.example.madcamp2024wjhnh.data

import android.net.Uri

data class DayInfo(
    val title: String,
    val address: String,
    val description: String,
    val imageUri: Uri? // 이미지 경로를 저장
)
