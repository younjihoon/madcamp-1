package com.example.madcamp2024wjhnh.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Travel(
    val id: Int,
    val title: String,
    val place: String,
    val date: String,
    val tags: List<String>,
    val memo: String,
    val thumbnail: Int,
    val Dayinfos: MutableList<DayInfo>
) : Parcelable
