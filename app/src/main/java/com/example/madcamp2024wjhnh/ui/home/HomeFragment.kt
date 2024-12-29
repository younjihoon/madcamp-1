package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
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

        // RecyclerView 설정
        travelAdapter = TravelAdapter(requireContext(), travelList) { travel ->
            val intent = Intent(requireContext(), DayInfoActivity::class.java)
            intent.putExtra("travel", travel)
            startActivity(intent)
        }

        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelRecyclerView.adapter = travelAdapter

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.travels.observe(viewLifecycleOwner) { travels ->
            travelList.clear()
            travelList.addAll(travels) // ViewModel의 데이터를 가져와 RecyclerView에 반영
            travelAdapter.notifyDataSetChanged()
        }

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
        dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_travel_detail, null)

        val titleEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_title)
        val placeEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_place)
        val dateEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_date)
        val tagsEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_tags)
        val memoEditText = dialogView!!.findViewById<EditText>(R.id.et_travel_memo)
        val imagePickerButton = dialogView!!.findViewById<Button>(R.id.imagePickerButton)
        val saveButton = dialogView!!.findViewById<Button>(R.id.saveButton)

        imagePickerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // 추가 버튼 클릭 이벤트 처리
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val place = placeEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val tags = tagsEditText.text.toString()
            val memo = memoEditText.text.toString().trim()

            // 필수 필드 확인
            if (title.isNotEmpty() && place.isNotEmpty() && date.isNotEmpty()) {
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
                dialog.dismiss()
            } else {
                // 필수 입력값이 비어있을 경우 사용자 알림
                titleEditText.error = "제목을 입력하세요"
                placeEditText.error = "장소를 입력하세요"
                dateEditText.error = "날짜를 입력하세요"
            }
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}