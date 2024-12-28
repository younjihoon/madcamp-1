package com.example.madcamp2024wjhnh.data
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayInfo(
    val number: Int,
    val address: List<Double>,
    val description: String,
    val photoList: List<Uri>
) : Parcelable