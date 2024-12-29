package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.example.madcamp2024wjhnh.data.Travel
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class TravelAdapter(
    private val context: Context,
    private val travels: MutableList<Travel>,
    private val onItemClick: (Travel) -> Unit
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val placeTextView: TextView = itemView.findViewById(R.id.tv_place)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        val tagsTextView: TextView = itemView.findViewById(R.id.tv_tags)
        val memoTextView: TextView = itemView.findViewById(R.id.tv_memo)
//        val editButton: ImageButton = itemView.findViewById(R.id.btn_edit) // 편집 버튼

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travels[position]

        holder.photoImageView.setImageURI(travel.thumbnail)
        holder.titleTextView.text = travel.title
        holder.placeTextView.text = travel.place
        holder.dateTextView.text = travel.date
        holder.tagsTextView.text = travel.tags
        holder.memoTextView.text = travel.memo
        holder.itemView.setOnClickListener {
            onItemClick(travel) // 클릭된 DayInfo를 콜백으로 전달
        }

//        holder.editButton.setOnClickListener {
//            openEditDialog(travel, position)
//        }
    }

    override fun getItemCount(): Int = travels.size

//    private fun openEditDialog(travel: Travel, position: Int) {
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_travel_detail, null)
//
//        val titleEditText = dialogView.findViewById<EditText>(R.id.et_travel_title)
//        val placeEditText = dialogView.findViewById<EditText>(R.id.et_travel_place)
//        val dateEditText = dialogView.findViewById<EditText>(R.id.et_travel_date)
//        val tagsEditText = dialogView.findViewById<EditText>(R.id.et_travel_tags)
//        val memoEditText = dialogView.findViewById<EditText>(R.id.et_travel_memo)
//        val imagePickerButton = dialogView.findViewById<Button>(R.id.imagePickerButton)
//        val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
//
//        // 기존 데이터 설정
//        titleEditText.setText(travel.title)
//        placeEditText.setText(travel.place)
//        dateEditText.setText(travel.date)
//        tagsEditText.setText(travel.tags)
//        memoEditText.setText(travel.memo)
//        dialogImageView.setImageURI(travel.thumbnail)
//
//        var selectedImageUri: Uri? = travel.thumbnail
//
//        // 이미지 선택 버튼 처리
//        imagePickerButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
//            (context as Activity).startActivityForResult(intent, 1001) // Image Picker 호출
//        }
//
//        // 다이얼로그 생성
//        AlertDialog.Builder(context)
//            .setView(dialogView)
//            .setTitle("Edit Travel")
//            .setPositiveButton("Save") { _, _ ->
//                // Travel 정보 업데이트
//                travel.title = titleEditText.text.toString()
//                travel.place = placeEditText.text.toString()
//                travel.date = dateEditText.text.toString()
//                travel.tags = tagsEditText.text.toString()
//                travel.memo = memoEditText.text.toString()
//                travel.thumbnail = selectedImageUri
//
//                notifyItemChanged(position) // RecyclerView 업데이트
//                Toast.makeText(context, "Travel updated!", Toast.LENGTH_SHORT).show()
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
}

