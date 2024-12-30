package com.example.madcamp2024wjhnh

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.ActivityDayInfoBinding
import com.example.madcamp2024wjhnh.ui.DayInfoAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.internal.notify


class DayInfoActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDayInfoBinding
    private lateinit var dayInfoRecyclerView: RecyclerView
    private lateinit var addDayInfoButton: Button
    private val dayInfoList = mutableListOf<DayInfo>()
    private val context : Context = this
    private val imageList = mutableListOf<Uri>()
    private lateinit var adapter : DayInfoAdapter
    private lateinit var imageAdapter : DayInfoImageAddAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val travel = intent.getParcelableExtra<Travel>("travel")?:Travel(
            title = "title",
            place = "place",
            startDate = "24/01//01",
            endDate = "24/01/07",
            tags = "tags",
            memo = "memo",
            thumbnail = Uri.EMPTY,
            DayInfos = mutableListOf(
                DayInfo(0, mutableListOf("주소"), "description", mutableListOf(Uri.EMPTY)),
                DayInfo(1, mutableListOf("주소"), "description", mutableListOf(Uri.EMPTY))
            )
        )

        dayInfoList.addAll(travel.DayInfos)
        adapter = DayInfoAdapter(dayInfoList) { dayInfo ->
            val intent = Intent(this, DayInfoDetailActivity::class.java)
            intent.putExtra("dayInfo", dayInfo)
            startActivity(intent)
        }

        dayInfoRecyclerView = binding.dayInfoRecyclerView
        dayInfoRecyclerView.adapter = adapter
        dayInfoRecyclerView.layoutManager = LinearLayoutManager(this)

        addDayInfoButton = binding.addDayInfoButton
        addDayInfoButton.setOnClickListener {
            showAddDayInfoDialog(adapter)
        }
    }

    private fun showAddDayInfoDialog(adapter: DayInfoAdapter) {
        imageAdapter = DayInfoImageAddAdapter(imageList) {
            openImagePicker()
        }
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_day_info, null)
        val imageRecyclerView = dialogView.findViewById<RecyclerView>(R.id.addImagesRecyclerView)
        imageRecyclerView.layoutManager = GridLayoutManager(this, 3) // 3열 Grid
        imageRecyclerView.adapter = imageAdapter

        val dialogNumberEditText = dialogView.findViewById<EditText>(R.id.addNumber)
        val dialogAddressEditText = dialogView.findViewById<EditText>(R.id.addAddress)
        val dialogDescriptionEditText = dialogView.findViewById<EditText>(R.id.addDescription)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("추가") { dialog, _ ->
                val number = dialogNumberEditText.text.toString().toIntOrNull()
                val address = dialogAddressEditText.text.toString().split(",").toMutableList()
                val description = dialogDescriptionEditText.text.toString()
                val photoList = imageList
                if (number != null) {
                    val newDayInfo = DayInfo(number, address, description, photoList)
                    dayInfoList.add(newDayInfo)
                    adapter.notifyItemInserted(dayInfoList.size - 1)
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 다중 선택 가능
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            Log.e("Error","No image : $clipData")
            if (clipData != null) {
                // 다중 선택
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageList.add(imageUri)
                }
                imageAdapter.notifyItemRangeInserted(imageList.size - clipData.itemCount,clipData.itemCount)

            } else {
                // 단일 선택
                val imageUri = data?.data
                imageUri?.let {
                    imageList.add(it)
                    imageAdapter.notifyItemInserted(imageList.size - 1)

                }
            }

        }
    }
}