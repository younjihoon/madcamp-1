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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp2024wjhnh.data.DayInfoListConverter
import com.example.madcamp2024wjhnh.data.TravelR
import okhttp3.internal.notify
import com.example.madcamp2024wjhnh.SharedViewModel

class DayInfoActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDayInfoBinding
    private lateinit var dayInfoRecyclerView: RecyclerView
    private lateinit var addDayInfoButton: Button
    private var travelRId : Int = 0
    private lateinit var travelDayInfos : MutableList<DayInfo>
    private lateinit var adapter : DayInfoAdapter
    private lateinit var imageAdapter : DayInfoImageAddAdapter
    private lateinit var travelViewModel: TravelViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val dayInfoList = mutableListOf<DayInfo>()
    private val context : Context = this
    private val imageList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        travelViewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        travelRId = intent.getIntExtra("travelId",100)
        Log.i("[DIActivity]","intent_received: $travelRId")
        travelDayInfos = mutableListOf()
        travelViewModel.getTravelById(travelRId) { restravelR ->
            if (restravelR != null) {
                sharedViewModel.setNewTravel(restravelR)
                Log.e("[DIActivity]","New dayinfolist: $dayInfoList")
                travelDayInfos = restravelR.DayInfos
                Log.e("[DIActivity]","New dayinfolist: $dayInfoList")
            }
            else {
                Log.e("[DIActivity]","travelR is NULL")
            }
        }
        adapter = DayInfoAdapter(dayInfoList) { dayInfo ->
            val fragment = DayInfoDetailFragment.newInstance(dayInfo, travelRId)
            // Replace the current fragment
            (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.scrollView, fragment) // Use the correct container ID
                ?.addToBackStack(null) // Optional: Add to back stack
                ?.commit()
        }
        sharedViewModel.travels.observe(this) { travels ->
            if (!travels.isNullOrEmpty()) {
                dayInfoList.clear()
                dayInfoList.addAll(travels[0].DayInfos)
                adapter.notifyDataSetChanged()
            }
        }
        dayInfoRecyclerView = binding.dayInfoRecyclerView
        dayInfoRecyclerView.adapter = adapter
        dayInfoRecyclerView.layoutManager = LinearLayoutManager(this)

        addDayInfoButton = binding.addDayInfoButton
        addDayInfoButton.setOnClickListener {
            imageList.clear()
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
                    travelDayInfos.clear()
                    travelDayInfos.addAll(dayInfoList)
                    travelViewModel.updateById(travelRId, travelDayInfos) { success ->
                        if (success) {
                            Log.d("[DayInfoActivity]debug", "TravelR updated successfully!")
                        } else {
                            Log.d("[DayInfoActivity]debug", "Failed to update TravelR.")
                        }
                    }
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
            Log.e("[DayInfoActivity]Error","No image : $clipData")
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