package com.example.madcamp2024wjhnh

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class DayInfoImageAddAdapter (
    private val imageList: MutableList<Uri>,
    private val onAddImageClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ADD = 0
        private const val VIEW_TYPE_IMAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_ADD else VIEW_TYPE_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ADD) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day_info_add_image, parent, false)
            AddImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day_info_image, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddImageViewHolder) {
            holder.itemView.setOnClickListener { onAddImageClick() }
        } else if (holder is ImageViewHolder) {
            val imageUri = imageList[position - 1] // 첫 번째 아이템은 버튼이므로 -1
            holder.imageView.setImageURI(imageUri)
        }
    }

    override fun getItemCount(): Int = imageList.size + 1 // "이미지 추가" 버튼 포함

    class AddImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
