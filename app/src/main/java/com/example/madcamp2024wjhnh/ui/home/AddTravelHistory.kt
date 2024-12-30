package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.SharedViewModel
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.DialogAddTravelDetailBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

class AddTravelHistory : Fragment() {

    private var _binding: DialogAddTravelDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
//    private var selectedImageUri: Uri? = null
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTravelDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.imagePickerButton.setOnClickListener { openGallery() } // 이미지 선택 버튼 클릭 리스너
//        binding.datePickerButton.setOnClickListener { showDatePicker() }
        binding.datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()

            // 시작 날짜 선택
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                startDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }

                // 종료 날짜 선택
                DatePickerDialog(requireContext(), { _, endYear, endMonth, endDayOfMonth ->
                    endDate = Calendar.getInstance().apply {
                        set(endYear, endMonth, endDayOfMonth)
                    }

                    // 날짜를 버튼에 표시
                    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                    binding.datePickerButton.text = "${dateFormat.format(startDate!!.time)} ~ ${dateFormat.format(endDate!!.time)}"
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        return root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent) // 초기화된 Launcher 사용
    }

//    private fun showDatePicker() {
//        val calendar = Calendar.getInstance()
//
//        // 시작 날짜 선택
//        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
//            val start = Calendar.getInstance().apply {
//                set(year, month, dayOfMonth)
//            }
//            startDate = start
//
//            // 종료 날짜 선택
//            DatePickerDialog(requireContext(), { _, endYear, endMonth, endDayOfMonth ->
//                val end = Calendar.getInstance().apply {
//                    set(endYear, endMonth, endDayOfMonth)
//                }
//                endDate = end
//
//                // 날짜를 버튼에 표시
//                val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
//                val dateRange = "${dateFormat.format(start.time)} ~ ${dateFormat.format(end.time)}"
//                binding.datePickerButton.text = dateRange
//            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
