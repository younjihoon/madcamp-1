package com.example.madcamp2024wjhnh.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.DayInfoActivity
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.TravelR
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.madcamp2024wjhnh.SharedViewModel


class TravelAdapter(
    private val fragment: HomeFragment, // HomeFragment 참조 추가
    private val context: Context,
    private val travels: MutableList<TravelR>,
    private val onItemClick: (TravelR) -> Unit
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val placeTextView: TextView = itemView.findViewById(R.id.tv_place)
        val tagsTextView: TextView = itemView.findViewById(R.id.tv_tags)
        val memoTextView: TextView = itemView.findViewById(R.id.tv_memo)
        val editButton: ImageButton = itemView.findViewById(R.id.btn_edit) // 편집 버튼
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date) // 시작 날짜 버튼
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        Log.e("[TravelAdapter]","CREATE")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travels[position]
        Log.e("[TravelAdapter]","travel: $travel")
        val uri = travel.thumbnail
        Log.e("[TravelAdapter]","travel.thumbnail: $uri")
        holder.photoImageView.setImageURI(uri)
        Log.e("[TravelAdapter]","set: photoImageView")
        holder.photoImageView.contentDescription = travel.id.toString()
        holder.titleTextView.text = travel.title
        holder.placeTextView.text = travel.place
        holder.dateTextView.text = travel.date
        holder.tagsTextView.text = travel.tags
        holder.memoTextView.text = travel.memo
        Log.e("[TravelAdapter]","set: texts")
        Log.e("[TravelAdapter]","travel id : ${travel.id}")
        val newTravelR = TravelR(
            id = travel.id,
            title = travel.title,
            place = travel.place,
            date = travel.date,
            tags = travel.tags,
            memo = travel.memo,
            thumbnail = travel.thumbnail,
            DayInfos = mutableListOf()
        )
        holder.itemView.setOnClickListener {
            onItemClick(newTravelR) // 클릭된 DayInfo를 콜백으로 전달
        }
        holder.editButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Select Action")
                .setItems(arrayOf("Edit", "Delete")) { _, which ->
                    when (which) {
                        0 -> fragment.openEditDialog(travel, position) // 편집 다이얼로그 열기
                        1 -> fragment.openDeleteDialog(travel, position) // 삭제 확인 다이얼로그 열기
                    }
                }
                .show()
        }
    }

    override fun getItemCount(): Int = travels.size
}

