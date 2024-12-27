//package com.example.madcamp2024wjhnh.ui.home
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.madcamp2024wjhnh.R
//import com.example.madcamp2024wjhnh.data.Travel
//import com.example.madcamp2024wjhnh.databinding.FragmentHomeBinding
//
//class HomeFragment : Fragment() {
//
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var travelAdapter: TravelAdapter
//    private val sampleData = mutableListOf<Travel>() // 데이터 저장
//
//    companion object {
//        private const val PICK_IMAGE_REQUEST = 1 // 이미지 선택 요청 코드
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // 초기 데이터 추가
//        sampleData.add(
//            Travel(
//                title = "Trip to the Beach",
//                address = "Seaside, CA",
//                tags = "#Relaxation #Sunny",
//                description = "Enjoyed a peaceful day by the sea.",
//                photoUri = null,
//                photoListday1 = emptyList(),
//                photoListday2 = emptyList()
//            )
//        )
//
//        // RecyclerView 설정
//        travelAdapter = TravelAdapter(requireContext(), sampleData)
//        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.travelRecyclerView.adapter = travelAdapter
//
//        // 플로팅 버튼 클릭 리스너
//        binding.fabAddItem.setOnClickListener {
//            openGallery()
//        }
//
//        return root
//    }
//
//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//            val selectedImageUri: Uri? = data?.data
//            if (selectedImageUri != null) {
//                showAddItemDialog(selectedImageUri)
//            }
//        }
//    }
//
//    private fun showAddItemDialog(imageUri: Uri) {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_travelclick_detail, null)
//        val titleInput = dialogView.findViewById<EditText>(R.id.title_input)
//        val addressInput = dialogView.findViewById<EditText>(R.id.address_input)
//        val descriptionInput = dialogView.findViewById<EditText>(R.id.description_input)
//
//        AlertDialog.Builder(requireContext())
//            .setTitle("Add Travel")
//            .setView(dialogView)
//            .setPositiveButton("Add") { _, _ ->
//                val title = titleInput.text.toString()
//                val address = addressInput.text.toString()
//                val description = descriptionInput.text.toString()
//
//                val newTravel = Travel(
//                    title = title,
//                    address = address,
//                    tags = "#NewTrip",
//                    description = description,
//                    photoUri = imageUri,
//                    photoListday1 = emptyList(),
//                    photoListday2 = emptyList()
//                )
//                sampleData.add(newTravel)
//                travelAdapter.notifyItemInserted(sampleData.size - 1)
//            }
//            .setNegativeButton("Cancel", null)
//            .create()
//            .show()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


package com.example.madcamp2024wjhnh.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelAdapter: TravelAdapter
    private val travelList = mutableListOf<Travel>() // 여행 데이터 리스트

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 샘플 데이터
        travelList.addAll(
            listOf(
                Travel(
                    title = "Trip to the Beach",
                    address = "Seaside, CA",
                    tags = "#Relaxation #Sunny",
                    description = "Enjoyed a peaceful day by the sea.",
                    photoResId = R.drawable.travel1,
                    photoListday1 = emptyList(),
                    photoListday2 = emptyList()
                ),
                Travel(
                    title = "Mountain Hiking",
                    address = "Rocky Mountains, CO",
                    tags = "#Adventure #Nature",
                    description = "Explored the rugged mountain trails.",
                    photoResId = R.drawable.travel2,
                    photoListday1 = emptyList(),
                    photoListday2 = emptyList()
                )
            )
        )

        // RecyclerView 설정
        travelAdapter = TravelAdapter(requireContext(), travelList)
        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelRecyclerView.adapter = travelAdapter

        // 플로팅 버튼 클릭 시 AddTravelHistory 열기
        binding.fabAddItem.setOnClickListener {
            // AddTravelHistory 화면으로 이동
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, AddTravelHistory())
                .addToBackStack(null)
                .commit()
        }

        return root
    }

    // 새로운 여행 데이터 추가
    fun addNewTravel(travel: Travel) {
        travelList.add(travel)
        travelAdapter.notifyItemInserted(travelList.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}