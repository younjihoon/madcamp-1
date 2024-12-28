package com.example.madcamp2024wjhnh.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.DayInfoActivity
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel
import android.widget.Toast



class TravelAdapter(
    private val context: Context,
    private val travels: MutableList<Travel>
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val placeTextView: TextView = itemView.findViewById(R.id.tv_place)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        val tagsTextView: TextView = itemView.findViewById(R.id.tv_tags)
        val memoTextView: TextView = itemView.findViewById(R.id.tv_memo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travels[position]

//        holder.photoImageView.text = travel.thumbnail
        holder.titleTextView.text = travel.title
        holder.placeTextView.text = travel.place
        holder.dateTextView.text = travel.date
        holder.tagsTextView.text = travel.tags
        holder.memoTextView.text = travel.memo

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = travels.size

    fun addTravel(travel: Travel) {
        travels.add(travel)
        notifyItemInserted(travels.size - 1) // 새 데이터 추가 시 갱신
    }
}
