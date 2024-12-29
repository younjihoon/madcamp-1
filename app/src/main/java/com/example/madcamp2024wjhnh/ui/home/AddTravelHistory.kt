package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
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

class AddTravelHistory : Fragment() {

    private var _binding: DialogAddTravelDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

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

        binding.saveButton.setOnClickListener { saveTravelHistory() } // 저장 버튼 클릭 리스너

        return root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent) // 초기화된 Launcher 사용
    }

    private fun saveTravelHistory() {
        val title = binding.etTravelTitle.text.toString()
        val place = binding.etTravelPlace.text.toString()
        val date = binding.etTravelDate.text.toString()
        val tags = binding.etTravelTags.text.toString()
        val memo = binding.etTravelMemo.text.toString()
        val thumbnail = selectedImageUri ?: Uri.EMPTY // thumbnail에 선택된 이미지 URI 할당

        if (title.isEmpty() || place.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "필수 입력값을 모두 작성해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val newTravel = Travel(
            title = title,
            place = place,
            date = date,
            tags = tags,
            memo = memo,
            thumbnail = thumbnail,
            DayInfos = mutableListOf(
                DayInfo(0, mutableListOf("주소"), "description", mutableListOf(Uri.EMPTY)),
                DayInfo(1, mutableListOf("주소"), "description", mutableListOf(Uri.EMPTY))
            )
        )

        sharedViewModel.setNewTravel(newTravel)

        Toast.makeText(requireContext(), "새로운 여행이 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 현재 프래그먼트 종료
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
