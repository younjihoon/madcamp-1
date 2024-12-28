package com.example.madcamp2024wjhnh.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.DayInfo

class DayInfoAdapter(
    private val dayInfoList: MutableList<DayInfo>,
    private val onItemClick: (DayInfo) -> Unit
) :
    RecyclerView.Adapter<DayInfoAdapter.DayInfoViewHolder>() {

    class DayInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayNumberTextView: TextView = itemView.findViewById(R.id.dayNumberTextView)
        val dayAddressTextView: TextView = itemView.findViewById(R.id.dayAddressTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.dayDescriptionTextView)
        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_info, parent, false)
        return DayInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayInfoViewHolder, position: Int) {
        val dayInfo = dayInfoList[position]

        holder.dayNumberTextView.text = dayInfo.number.toString()
        holder.descriptionTextView.text = dayInfo.description
        holder.dayAddressTextView.text = dayInfo.address.joinToString(", ")
        if (dayInfo.photoList.isNotEmpty()) {
            holder.photoImageView.setImageURI(dayInfo.photoList[0])
        } else {
            holder.photoImageView.setImageResource(R.drawable.travel1)
        }
        holder.itemView.setOnClickListener{
            onItemClick(dayInfo)
        }
    }

    override fun getItemCount(): Int = dayInfoList.size

    fun addDayInfo(dayInfo: DayInfo) {
        dayInfoList.add(dayInfo)
        notifyItemInserted(dayInfoList.size - 1)
    }
    fun removeDayInfo(position: Int) {
        if (position in 0 until dayInfoList.size) {
            dayInfoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
