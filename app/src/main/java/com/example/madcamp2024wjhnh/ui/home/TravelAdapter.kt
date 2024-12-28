
//package com.example.madcamp2024wjhnh.ui.home

//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.GridLayout
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.RecyclerView
//import com.example.madcamp2024wjhnh.R
//import com.example.madcamp2024wjhnh.data.Travel
//
//class TravelAdapter(
//    private val context: Context,
//    private val travels: List<Travel>
//) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {
//
//    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
//        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
//        val addressTextView: TextView = itemView.findViewById(R.id.tv_address)
//        val tagsTextView: TextView = itemView.findViewById(R.id.tv_tags)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
//        return TravelViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
//        val travel = travels[position]
//        holder.photoImageView.setImageResource(travel.photoResId)
//        holder.titleTextView.text = travel.title
//        holder.addressTextView.text = travel.address
//        holder.tagsTextView.text = travel.tags
//        holder.descriptionTextView.text = travel.description
//
//        // 클릭 이벤트 처리
//        holder.itemView.setOnClickListener {
//            showTravelDialog(travel)
//        }
//    }
//
//    override fun getItemCount(): Int = travels.size
//
//private fun showTravelDialog(travel: Travel) {
//    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_travel_detail, null)
//
//    // 1일차 데이터 바인딩
//    val day1GridLayout = dialogView.findViewById<GridLayout>(R.id.day1GridLayout)
//    day1GridLayout.removeAllViews()
//    for (imageResId in travel.photoListday1) { // Travel 객체의 Day 1 사진 리스트
//        val imageView = ImageView(context).apply {
//            layoutParams = GridLayout.LayoutParams().apply {
//                width = 0
//                height = 100
//                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//                setMargins(8, 8, 8, 8)
//            }
//            scaleType = ImageView.ScaleType.CENTER_CROP
//            setImageResource(imageResId)
//        }
//        day1GridLayout.addView(imageView)
//    }
//
//    // 2일차 데이터 바인딩
//    val day2GridLayout = dialogView.findViewById<GridLayout>(R.id.day2GridLayout)
//    day2GridLayout.removeAllViews()
//    for (imageResId in travel.photoListday2) { // Travel 객체의 Day 2 사진 리스트
//        val imageView = ImageView(context).apply {
//            layoutParams = GridLayout.LayoutParams().apply {
//                width = 0
//                height = 100
//                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//                setMargins(8, 8, 8, 8)
//            }
//            scaleType = ImageView.ScaleType.CENTER_CROP
//            setImageResource(imageResId)
//        }
//        day2GridLayout.addView(imageView)
//    }
//
//    // 다이얼로그 생성
//    AlertDialog.Builder(context)
//        .setView(dialogView)
//        .setPositiveButton("닫기") { dialog, _ ->
//            dialog.dismiss()
//        }
//        .create()
//        .show()
//    }
//}
//

package com.example.madcamp2024wjhnh.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.DayInfoActivity
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel

class TravelAdapter(
    private val context: Context,
    private val travels: List<Travel>,
    private val onItemClick: (Travel) -> Unit
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val addressTextView: TextView = itemView.findViewById(R.id.tv_address)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travels[position]
//        travel.photoResId?.let { holder.photoImageView.setImageResource(it) }
//        holder.titleTextView.text = travel.title
//        holder.addressTextView.text = travel.address
//        holder.descriptionTextView.text = travel.description
        holder.itemView.setOnClickListener {
            onItemClick(travel) // 클릭된 DayInfo를 콜백으로 전달
        }
    }

    override fun getItemCount(): Int = travels.size
}
