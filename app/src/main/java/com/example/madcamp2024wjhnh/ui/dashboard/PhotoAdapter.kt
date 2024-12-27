package com.example.madcamp2024wjhnh.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Photo

class PhotoAdapter(
    private val context: Context,
    private val photos: List<Photo>
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.photoTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.photoImageView.setImageResource(photo.imageResId)
        holder.titleTextView.text = photo.title

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            showPhotoDialog(photo)
        }
    }

    override fun getItemCount(): Int = photos.size

    // 다이얼로그를 표시하는 함수
    private fun showPhotoDialog(photo: Photo) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_photo_detail, null)
        val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val dialogTitleTextView = dialogView.findViewById<TextView>(R.id.dialogTitleTextView)
        val dialogDescriptionTextView = dialogView.findViewById<TextView>(R.id.dialogDescriptionTextView)
        val dialogLinkTextView = dialogView.findViewById<TextView>(R.id.dialogLinkTextView)

        // 데이터 설정
        dialogImageView.setImageResource(photo.imageResId)
        dialogTitleTextView.text = photo.title
        dialogDescriptionTextView.text = photo.description
        dialogLinkTextView.text = photo.link

        // 다이얼로그 생성
        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("닫기") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
