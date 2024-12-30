package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp2024wjhnh.DayInfoActivity
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.FragmentHomeBinding
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelAdapter: TravelAdapter
    private val travelList = mutableListOf<Travel>() // 여행 데이터 리스트

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    private var selectedImageUri: Uri? = null
    private var dialogView: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 1) Adapter 초기화
        travelAdapter = TravelAdapter(
            this,
            requireActivity(),
            travelList
        ) { travel ->
            // 아이템 클릭 시 DayInfoActivity로 이동
            val intent = Intent(requireActivity(), DayInfoActivity::class.java)
            intent.putExtra("travel", travel) // 여기서 'travel'은 커스텀 데이터 클래스
            startActivity(intent)
        }

        // 2) RecyclerView 설정
        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelRecyclerView.adapter = travelAdapter

        // 3) ViewModel 연결
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.travels.observe(viewLifecycleOwner) { travels ->
            travelList.clear()
            travelList.addAll(travels) // ViewModel의 데이터를 가져와 RecyclerView에 반영
            travelAdapter.notifyDataSetChanged()
        }

        // 4) 갤러리 런처
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
            showAddTravelDialog()
        }
        return root
    }


    private fun showAddTravelDialog() {
        dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_travel_detail, null)

        val titleEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_title)
        val placeEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_place)
        val tagsEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_tags)
        val memoEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_memo)
        val imagePickerButton = dialogView!!.findViewById<Button>(R.id.imagePickerButton)
        val saveButton = dialogView!!.findViewById<Button>(R.id.saveButton)
        val startDateButton = dialogView!!.findViewById<Button>(R.id.startDateButton)
        val endDateButton = dialogView!!.findViewById<Button>(R.id.endDateButton)

        var startdate = "Start Date"
        var enddate = "End Date"
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

        startDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val startDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                startDateButton.text = dateFormat.format(startDate.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 종료일 버튼
        endDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val endDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
                endDateButton.text = dateFormat.format(endDate.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 추가 버튼 클릭 이벤트 처리
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val place = placeEditText.text.toString().trim()
            val tags = tagsEditText.text.toString().trim()
            val memo = memoEditText.text.toString().trim()
            val sd = startDateButton.text.toString().trim()
            val ed = endDateButton.text.toString().trim()
            val date = "$sd ~ $ed"


            // 필수 필드 확인
            if (title.isNotEmpty()) { //&& selectedImageUri != Uri.EMPTY && selectedImageUri != null
                val newTravel = Travel(
                    title = title,
                    place = place,
                    date = date,
                    tags = tags,
                    memo = memo,
                    thumbnail = selectedImageUri ?: Uri.EMPTY, // URI가 없으면 EMPTY로 설정
                    DayInfos = mutableListOf()
                )
                sharedViewModel.setNewTravel(newTravel)

                Toast.makeText(requireContext(), "새로운 여행이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // 필수 입력값이 비어있을 경우 사용자 알림
                if (title.isEmpty()) titleEditText.error = "제목을 입력하세요"
//                if (selectedImageUri == null || selectedImageUri == Uri.EMPTY) {
//                    Toast.makeText(requireContext(), "이미지를 선택하세요.", Toast.LENGTH_SHORT).show()
//                }
            }
        }
        dialog.show()
    }

    private fun showEditTravelDialog(travel: Travel, position: Int) {
        dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_travel_detail, null)

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

        // 기존 데이터 세팅
        titleEditText.setText(travel.title)
        placeEditText.setText(travel.place)
        tagsEditText.setText(travel.tags)
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

        startdateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
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
            val updatedTitle = titleEditText.text.toString().trim()
            val updatedPlace = placeEditText.text.toString().trim()
            val updatedTags = tagsEditText.text.toString().trim()
            val updatedMemo = memoEditText.text.toString().trim()
            val updatedStartDate = startdateButton.text.toString().trim()
            val updatedEndDate = enddateButton.text.toString().trim()
            val updatedDate = "$updatedStartDate ~ $updatedEndDate"


            if (updatedTitle.isNotEmpty()) {
                // 여행 데이터 업데이트
                travel.title = updatedTitle
                travel.place = updatedPlace
                travel.date = updatedDate
                travel.tags = updatedTags
                travel.memo = updatedMemo
                travel.thumbnail = selectedImageUri ?: Uri.EMPTY

                sharedViewModel.updateTravel(position, travel) // ViewModel 데이터 업데이트
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

        dialog.show()
    }


    private fun showDeleteConfirmationDialog(travel: Travel, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Travel")
            .setMessage("Are you sure you want to delete this travel?")
            .setPositiveButton("Yes") { _, _ ->
                sharedViewModel.deleteTravel(position)
                Toast.makeText(context, "Travel deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    // ========== Adapter에서 호출하기 위해 public 함수로 열어 둠 ==========
    fun openEditDialog(travel: Travel, position: Int) {
        showEditTravelDialog(travel, position)
    }

    fun openDeleteDialog(travel: Travel, position: Int) {
        showDeleteConfirmationDialog(travel, position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
