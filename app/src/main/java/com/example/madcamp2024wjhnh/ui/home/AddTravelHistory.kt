package com.example.madcamp2024wjhnh.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.DialogTravelclickDetailBinding

class AddTravelHistory : Fragment() {

    private var _binding: DialogTravelclickDetailBinding? = null
    private val binding get() = _binding!!

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTravelclickDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 이미지 선택 버튼
        binding.imagePickerButton.setOnClickListener {
            openGallery()
        }

        // 저장 버튼
        binding.saveButton.setOnClickListener {
            saveTravelHistory()
        }

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
            binding.dialogImageView.setImageURI(selectedImageUri)
        }
    }

    private fun saveTravelHistory() {
        val title = binding.etTravelTitle.text.toString()
        val address = binding.etTravelAddress.text.toString()
        val tags = binding.etTravelTags.text.toString()
        val description = binding.etTravelDescription.text.toString()


        if (selectedImageUri == null || title.isEmpty() || address.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "모든 필드를 채우세요", Toast.LENGTH_SHORT).show()
            return
        }

//        val newTravel = Travel(
//            title = title,
//            address = address,
//            tags = tags,
//            description = description,
//            photoUri = selectedImageUri,
//            photoListday1 = emptyList(),
//            photoListday2 = emptyList()
//        )
//
//        // 결과 전달 및 현재 프래그먼트 종료
//        (activity as? HomeFragment)?.addNewTravel(newTravel)
//        requireActivity().supportFragmentManager.popBackStack()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
