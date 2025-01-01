package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
import android.content.Context
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp2024wjhnh.DayInfoActivity
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.data.TravelR
import com.example.madcamp2024wjhnh.databinding.FragmentHomeBinding
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.madcamp2024wjhnh.TravelViewModel
import com.example.madcamp2024wjhnh.data.AppDatabase
import com.google.android.material.datepicker.MaterialDatePicker


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelAdapter: TravelAdapter
    private val travelList = mutableListOf<TravelR>() // 여행 데이터 리스트

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    private var selectedImageUri: Uri? = null
    private var dialogView: View? = null

    private lateinit var travelViewModel: TravelViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.e("[HomeFragment]","started")
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        Log.e("[HomeFragment]","$sharedViewModel")
        travelViewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]
        Log.e("[HomeFragment]","$travelViewModel")
        travelAdapter = TravelAdapter(this,requireContext(), travelList) { travel ->
            Log.e("[HomeFragment]","travelAdapter")
            val intent = Intent(requireContext(), DayInfoActivity::class.java)
            Log.e("[HomeFragment]","intent: $intent")
            intent.putExtra("travelId", travel.id)
            startActivity(intent)
        }
        sharedViewModel.travels.observe(viewLifecycleOwner) { travels ->
            travelList.clear() // Clear the existing list
            Log.e("[HomeFragment]","travelList: $travelList")
            travelList.addAll(travels) // Add all items from the LiveData list
            Log.e("[HomeFragment]","travelList: $travelList")
            travelAdapter.notifyDataSetChanged() // Notify the adapter if it's bound to a RecyclerView
        }

        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelRecyclerView.adapter = travelAdapter

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data

                if (selectedImageUri != null) {
                    // 다이얼로그 내 이미지뷰 업데이트
                    val dialogImageView = dialogView?.findViewById<ImageView>(R.id.dialogImageView)
                    if (dialogImageView != null) {
                        dialogImageView.setImageURI(selectedImageUri)
                    } else {
                        Toast.makeText(requireContext(), "이미지뷰를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "이미지를 선택하지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "갤러리 선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 목록 생성 아이콘 클릭
        binding.fabAddItem.setOnClickListener {
            showAddTravelDialog(travelAdapter)
        }
        return root
    }

    // 새 여행 기록 목록 생성 뷰
    private fun showAddTravelDialog(travelAdapter: TravelAdapter) {
        dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_travel_detail, null)

        val titleEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_title)
        val placeEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_place)
        val tagsEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_tags)
        val memoEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_memo)
        val imagePickerButton = dialogView!!.findViewById<Button>(R.id.imagePickerButton)
        val saveButton = dialogView!!.findViewById<Button>(R.id.saveButton)
        val startDateButton = dialogView!!.findViewById<Button>(R.id.startDateButton)
        val endDateButton = dialogView!!.findViewById<Button>(R.id.endDateButton)

        var startdate = "00/00/00"
        var enddate = "00/00/00"
        startDateButton.text = startdate
        endDateButton.text = enddate

        imagePickerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)


        startDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
//                R.style.CustomDatePickerTheme, // 커스텀 테마 적용
                { _, year, month, dayOfMonth ->
                    val startDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                    startDateButton.text = dateFormat.format(startDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 종료일 버튼
        endDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
//                R.style.CustomDatePickerTheme, // 커스텀 테마 적용
                { _, year, month, dayOfMonth ->
                    val endDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                    endDateButton.text = dateFormat.format(endDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 추가 버튼 클릭 이벤트 처리
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val place = placeEditText.text.toString().trim()
            val tags = tagsEditText.text.toString()
            val memo = memoEditText.text.toString().trim()
            val sd = startDateButton.text.toString().trim()
            val ed = endDateButton.text.toString().trim()
            val date = "$sd ~ $ed"

            val processedTags = tags.split(" ")
                .filter { it.isNotBlank() } // 빈 문자열 제거
                .joinToString(" ") { "#$it" }


            // 필수 필드 확인
            if (title.isNotEmpty()) {
//            if (title.isNotEmpty() && selectedImageUri != Uri.EMPTY && {
////            if (title.isNotEmpty() && selectedImageUri != Uri.EMPTY && selectedImageUri != null) {
//                val newTravel = TravelR(
//                    title = title,
//                    place = place,
//                    date = date,
//                    tags = processedTags,
//                    memo = memo,
//                    thumbnail = selectedImageUri ?: Uri.EMPTY, // URI가 없으면 EMPTY로 설정
//                    DayInfos = mutableListOf()
//                )
//                Log.e("NOTATION", "new travel inserted in travelViewModel, $newTravel")
//                var roomID: Int = 0
//                travelViewModel.insert(newTravel) { id ->
//                    Log.d("debug", "New travel inserted with ID: $id")
//                    // 이후 ID를 활용한 작업
//                    roomID = id
//                    val newTravel = TravelR(
//                        id = roomID,
//                        title = title,
//                        place = place,
//                        date = date,
//                        tags = tags,
//                        memo = memo,
//                        thumbnail = selectedImageUri ?: Uri.EMPTY, // URI가 없으면 EMPTY로 설정
//                        DayInfos = mutableListOf()
//                    )
//                    travelList.add(newTravel)
//                    sharedViewModel.setNewTravel(newTravel)
//                    travelAdapter.notifyItemInserted(travelList.size-1)
//                }
//                Toast.makeText(requireContext(), "새로운 여행이 저장되었습니다.", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
//            }selectedImageUri != null) {
                val newTravel = TravelR(
                    title = title,
                    place = place,
                    date = date,
                    tags = processedTags,
                    memo = memo,
                    thumbnail = selectedImageUri ?: Uri.EMPTY, // URI가 없으면 EMPTY로 설정
                    DayInfos = mutableListOf()
                )
                Log.e("NOTATION", "new travel inserted in travelViewModel, $newTravel")
                var roomID: Int = 0
                travelViewModel.insert(newTravel) { id ->
                    Log.d("debug", "New travel inserted with ID: $id")
                    // 이후 ID를 활용한 작업
                    roomID = id
                    val newTravel = TravelR(
                        id = roomID,
                        title = title,
                        place = place,
                        date = date,
                        tags = tags,
                        memo = memo,
                        thumbnail = selectedImageUri ?: Uri.EMPTY, // URI가 없으면 EMPTY로 설정
                        DayInfos = mutableListOf()
                    )
                    travelList.add(newTravel)
                    sharedViewModel.setNewTravel(newTravel)
                    travelAdapter.notifyItemInserted(travelList.size-1)
                }
                Toast.makeText(requireContext(), "새로운 여행이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // 필수 입력값이 비어있을 경우 사용자 알림
                if (title.isEmpty()) titleEditText.error = "제목을 입력하세요"
                if (selectedImageUri == null || selectedImageUri == Uri.EMPTY) {
                    Toast.makeText(requireContext(), "이미지를 선택하세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    private fun showEditTravelDialog(travel: TravelR, position: Int) {
        dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_travel_detail, null)

        val titleEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_title)
        val placeEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_place)
        val tagsEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_tags)
        val memoEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_memo)
        val imagePickerButton = dialogView!!.findViewById<Button>(R.id.imagePickerButton)
        val saveButton = dialogView!!.findViewById<Button>(R.id.saveButton)
        val startdateButton = dialogView!!.findViewById<Button>(R.id.startDateButton)
        val enddateButton = dialogView!!.findViewById<Button>(R.id.endDateButton)
        val dialogImageView = dialogView!!.findViewById<ImageView>(R.id.dialogImageView)

        val dateParts = travel.date.split(" ~ ")
        val existingStartDate = if (dateParts.isNotEmpty()) dateParts[0] else ""
        val existingEndDate = if (dateParts.size > 1) dateParts[1] else ""

        val imageView = dialogView!!.findViewById<ImageView>(R.id.dialogImageView)
        // 기존 데이터 세팅
        imageView.contentDescription = travel.id.toString()
        titleEditText.setText(travel.title)
        placeEditText.setText(travel.place)
        tagsEditText.setText(
            travel.tags.split("#")
                .filter { it.isNotBlank() }
                .joinToString(" ")
        )
        memoEditText.setText(travel.memo)
        startdateButton.text = existingStartDate
        enddateButton.text = existingEndDate
        dialogImageView.setImageURI(travel.thumbnail)
        selectedImageUri = travel.thumbnail

        imagePickerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)


        startdateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
//                R.style.CustomDatePickerTheme, // 커스텀 테마 적용

                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                    startdateButton.text = dateFormat.format(selectedDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 종료일 선택
        enddateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
//                R.style.CustomDatePickerTheme, // 커스텀 테마 적용

                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                    enddateButton.text = dateFormat.format(selectedDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 추가 버튼 클릭 이벤트 처리
        saveButton.setOnClickListener {
            val updatedId = imageView.contentDescription.toString().toInt()
            val updatedTitle = titleEditText.text.toString().trim()
            val updatedPlace = placeEditText.text.toString().trim()
            val updatedTags = tagsEditText.text.toString().trim()
            val updatedMemo = memoEditText.text.toString().trim()
            val updatedStartDate = startdateButton.text.toString().trim()
            val updatedEndDate = enddateButton.text.toString().trim()
            val updatedDate = "$updatedStartDate ~ $updatedEndDate"

            val processedupdatedTags = updatedTags.split(" ")
                .filter { it.isNotBlank() } // 빈 문자열 제거
                .joinToString(" ") { "#$it" }


            if (updatedTitle.isNotEmpty()) {
//            if (updatedTitle.isNotEmpty() && selectedImageUri != Uri.EMPTY && selectedImageUri != null) {
                // 여행 데이터 업데이트
                travel.title = updatedTitle
                travel.place = updatedPlace
                travel.date = updatedDate
                travel.tags = processedupdatedTags
                travel.memo = updatedMemo
                travel.thumbnail = selectedImageUri ?: Uri.EMPTY

                sharedViewModel.updateTravel(position, travel) // ViewModel 데이터 업데이트
                travelViewModel.update(travel)
                Toast.makeText(requireContext(), "여행 기록이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // 필수 입력값이 비어있을 경우 사용자 알림
                if (updatedTitle.isEmpty()) titleEditText.error = "제목을 입력하세요"
                if (selectedImageUri == null || selectedImageUri == Uri.EMPTY) {
                    Toast.makeText(requireContext(), "이미지를 선택하세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
        dialog.show()
    }


    private fun showDeleteConfirmationDialog(travel: TravelR, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Travel")
            .setMessage("Are you sure you want to delete this travel?")
            .setPositiveButton("Yes") { _, _ ->
                sharedViewModel.deleteTravel(position)
                travelViewModel.delete(travel)
                Toast.makeText(context, "Travel deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    // ========== Adapter에서 호출하기 위해 public 함수로 열어 둠 ==========
    fun openEditDialog(travel: TravelR, position: Int) {
        showEditTravelDialog(travel, position)
    }

    fun openDeleteDialog(travel: TravelR, position: Int) {
        showDeleteConfirmationDialog(travel, position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        travelViewModel.allTravels.observeForever { travels ->
            Log.e("debug", "All travels: $travels")
            if (travels != null) {
                sharedViewModel.replaceTravel(travels)
            }
        }
        _binding = null
    }
}