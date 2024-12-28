package com.example.madcamp2024wjhnh

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.databinding.ActivityDayInfoDetailBinding

class DayInfoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDayInfoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayInfoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 DayInfo 객체 받기
        val dayInfo = intent.getParcelableExtra<DayInfo>("dayInfo")
        if (dayInfo != null) {
            displayDayInfoDetails(dayInfo)
        }
    }

    private fun displayDayInfoDetails(dayInfo: DayInfo) {
        binding.detailDayNumberTextView.text = dayInfo.number.toString()
        binding.detailAddressTextView.text = dayInfo.address.joinToString(", ")
        binding.detailDescriptionTextView.text = dayInfo.description
        Log.e("Photos","${dayInfo.photoList}")
        val imageAdapter = DayInfoDetailImagePagerAdapter(dayInfo.photoList)
        binding.detailViewPager.adapter = imageAdapter
    }
}
