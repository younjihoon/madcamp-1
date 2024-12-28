package com.example.madcamp2024wjhnh.data
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayInfo(
    val number: Int,
    val address: MutableList<String>,
    val description: String,
    val photoList: MutableList<Uri>
) : Parcelable
