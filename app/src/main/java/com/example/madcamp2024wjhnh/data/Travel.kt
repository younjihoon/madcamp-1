package com.example.madcamp2024wjhnh.data
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Travel(
    var title: String,
    var place: String,
    var startDate: String,
    var endDate: String,
    var tags: String,
    var memo: String,
    var thumbnail: Uri,
    var DayInfos: MutableList<DayInfo>
) : Parcelable
