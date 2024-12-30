package com.example.madcamp2024wjhnh.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Photo

class PhotoAdapter(
    private val context: Context,
    private var photos: List<Photo>, // var로 변경하여 업데이트 가능하도록 설정
    private val onFavoriteStatusChanged: (Photo) -> Unit // 즐겨찾기 상태 변경 콜백
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.photoTitleTextView)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)

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
            showPhotoDialog(photo, position)
        }
        holder.favoriteButton.setImageResource(
            if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )
        holder.favoriteButton.setOnClickListener {
            photo.isFavorite = !photo.isFavorite // 상태 반전
            notifyItemChanged(position) // 변경된 아이템 업데이트
            onFavoriteStatusChanged(photo) // 상태 변경 콜백 호출
            holder.favoriteButton.setImageResource(
                if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
        }

    }

    override fun getItemCount(): Int = photos.size

    // 어댑터 데이터 업데이트 메서드
    fun updateData(newPhotos: List<Photo>) {
        photos = newPhotos
        notifyDataSetChanged()
    }

    // 다이얼로그를 표시하는 함수
    private fun showPhotoDialog(photo: Photo, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_photo_detail, null)
        val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val dialogTitleTextView = dialogView.findViewById<TextView>(R.id.dialogTitleTextView)
        val dialogDescriptionTextView = dialogView.findViewById<TextView>(R.id.dialogDescriptionTextView)
        val dialogLinkTextView = dialogView.findViewById<TextView>(R.id.dialogLinkTextView)
        val dialogFavoriteToggleButton = dialogView.findViewById<ToggleButton>(R.id.favoriteToggleButton)

        // 데이터 설정
        dialogImageView.setImageResource(photo.imageResId)
        dialogTitleTextView.text = photo.title
        dialogDescriptionTextView.text = photo.description
        dialogLinkTextView.text = photo.link

        // 즐겨찾기 토글 상태 설정
        dialogFavoriteToggleButton.isChecked = photo.isFavorite
        dialogFavoriteToggleButton.setOnCheckedChangeListener { _, isChecked ->
            photo.isFavorite = isChecked
            notifyItemChanged(position) // RecyclerView 갱신
        }

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
