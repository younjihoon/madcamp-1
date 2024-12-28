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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class DayInfoActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDayInfoBinding
    private lateinit var dayInfoRecyclerView: RecyclerView
    private lateinit var addDayInfoButton: Button
    private val dayInfoList = mutableListOf<DayInfo>()
    private val context : Context = this
    private lateinit var imageAdapter: DayInfoImageAddAdapter
    private val imageList = mutableListOf<Uri>(Uri.EMPTY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val travel = intent.getParcelableExtra<Travel>("travel")?:Travel(1,"제목","장소","20241228", emptyList(),".",0,mutableListOf(DayInfo(1, mutableListOf("주소"),"DayInfoActivity로 Travel이 넘어오지 않았음", mutableListOf())))

        dayInfoList.addAll(travel.Dayinfos)

        dayInfoRecyclerView = binding.dayInfoRecyclerView
        addDayInfoButton = binding.addDayInfoButton

        val adapter = DayInfoAdapter(dayInfoList)
        dayInfoRecyclerView.adapter = adapter
        dayInfoRecyclerView.layoutManager = LinearLayoutManager(this)

        addDayInfoButton.setOnClickListener {
            showAddDayInfoDialog(adapter)
        }
    }

    private fun showAddDayInfoDialog(adapter: DayInfoAdapter) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_day_info, null)
        val dialogNumberEditText = dialogView.findViewById<EditText>(R.id.addNumber)
        val dialogAddressEditText = dialogView.findViewById<EditText>(R.id.addAddress)
        val dialogDescriptionEditText = dialogView.findViewById<EditText>(R.id.addDescription)
        imageAdapter = DayInfoImageAddAdapter(imageList) {
            // 이미지 추가 버튼 클릭 이벤트 처리
            openImagePicker()
        }
        val imageRecyclerView = dialogView.findViewById<RecyclerView>(R.id.addImagesRecyclerView)
        imageRecyclerView.layoutManager = GridLayoutManager(this, 3) // 3열 Grid
        imageRecyclerView.adapter = imageAdapter
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("추가") { dialog, _ ->
                val number = dialogNumberEditText.text.toString().toIntOrNull()
                val address = dialogAddressEditText.text.toString().split(",").toMutableList()
                val description = dialogDescriptionEditText.text.toString()
                val photoList = mutableListOf(0)
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
            val imageUri = data?.data
            imageUri?.let {
                // RecyclerView에 이미지 추가
                imageList.add(it)
                imageAdapter.notifyItemInserted(imageList.size)
            }
        }
    }
}