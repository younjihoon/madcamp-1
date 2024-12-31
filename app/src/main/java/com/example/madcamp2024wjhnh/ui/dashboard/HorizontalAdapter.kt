package com.example.madcamp2024wjhnh.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Photo

class HorizontalAdapter(private val context: Context, private val items: List<Photo>,private val onFavoriteStatusChanged: (Photo) -> Unit ) :
    RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>() {

    inner class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.photoTitleTextView)
        val imageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return HorizontalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        val photo = items[position]

        holder.textView.text = items[position].title
        holder.imageView.setImageResource(items[position].imageResId)
        holder.itemView.setOnClickListener {
            showPhotoDialog(photo, position)
        }
        holder.favoriteButton.setImageResource(
            if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )
        holder.favoriteButton.setOnClickListener {
            Log.e("[HoriAdapter]","favoriteButton pressed")
            photo.isFavorite = !photo.isFavorite // 상태 반전
            notifyItemChanged(position) // 변경된 아이템 업데이트
            onFavoriteStatusChanged(photo)
            holder.favoriteButton.setImageResource(
                if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
        }

    }
    override fun getItemCount(): Int = items.size

    // 다이얼로그를 표시하는 함수
    private fun showPhotoDialog(photo: Photo, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_photo_detail, null)
        val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val dialogTitleTextView = dialogView.findViewById<TextView>(R.id.dialogTitleTextView)
        val dialogDescriptionTextView = dialogView.findViewById<TextView>(R.id.dialogDescriptionTextView)
        val dialogLinkTextView = dialogView.findViewById<TextView>(R.id.dialogLinkTextView)
        val dialogFavoriteToggleButton = dialogView.findViewById<ImageButton>(R.id.favoriteToggleButton)

        // 데이터 설정
        dialogImageView.setImageResource(photo.imageResId)
        dialogTitleTextView.text = photo.title
        dialogDescriptionTextView.text = photo.description
        dialogLinkTextView.text = photo.link

        dialogFavoriteToggleButton.setImageResource(
            if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )

        // 즐겨찾기 버튼 클릭 이벤트 처리
        dialogFavoriteToggleButton.setOnClickListener {
            photo.isFavorite = !photo.isFavorite
            dialogFavoriteToggleButton.setImageResource(
                if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
            notifyItemChanged(position) // RecyclerView 갱신
            onFavoriteStatusChanged(photo) // 상태 변경 콜백 호출
        }

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.show()
    }
}