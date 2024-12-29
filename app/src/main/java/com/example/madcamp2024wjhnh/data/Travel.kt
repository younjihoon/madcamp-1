package com.example.madcamp2024wjhnh.data
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Travel(
    val title: String,
    val place: String,
    val date: String,
    val tags: String,
    val memo: String,
    val thumbnail: Uri,
    val DayInfos: MutableList<DayInfo>
) : Parcelable
