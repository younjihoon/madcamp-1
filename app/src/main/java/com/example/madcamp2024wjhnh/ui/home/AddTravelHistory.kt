package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.DialogAddTravelDetailBinding
import com.example.madcamp2024wjhnh.SharedViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.data.DayInfo


class AddTravelHistory : Fragment() {

    private var _binding: DialogAddTravelDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel


    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1 // 요청 코드 상수 정의


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTravelDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.imagePickerButton.setOnClickListener { openGallery() }
        binding.saveButton.setOnClickListener { saveTravelHistory() }

        return root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            Log.e("Error","$data")
            binding.dialogImageView.setImageURI(selectedImageUri)
        }
    }

    private fun saveTravelHistory() {
        val title = binding.etTravelTitle.text.toString()
        val place = binding.etTravelPlace.text.toString()
        val date = binding.etTravelDate.text.toString()
        val tags = binding.etTravelTags.text.toString()
        val memo = binding.etTravelMemo.text.toString()
//        val image = selectedImageUri?.toString() ?: ""


//        if (title.isEmpty() || place.isEmpty() || date.isEmpty() || tags.isEmpty() || memo.isEmpty()) {
//            Toast.makeText(requireContext(), "모든 필드를 채우세요", Toast.LENGTH_SHORT).show()
//            return
//        }

        val newTravel = Travel(
            title = title,
            place = place,
            date = date,
            tags = tags,
            memo = memo,
            DayInfos = mutableListOf(
                DayInfo(0, mutableListOf("주소"), "description", mutableListOf(Uri.EMPTY)),
                DayInfo(1, mutableListOf("주소"), "description", mutableListOf(Uri.EMPTY))
            )
//            thumbnail = image
        )

        sharedViewModel.setNewTravel(newTravel)

        // 현재 프래그먼트 종료
        requireActivity().supportFragmentManager.popBackStack()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
