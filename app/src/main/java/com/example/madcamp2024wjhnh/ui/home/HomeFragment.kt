package com.example.madcamp2024wjhnh.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp2024wjhnh.DayInfoActivity
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.Travel
import com.example.madcamp2024wjhnh.databinding.FragmentHomeBinding
import com.example.madcamp2024wjhnh.ui.home.TravelAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.SharedViewModel




class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelAdapter: TravelAdapter
    private val travelList = mutableListOf<Travel>() // 여행 데이터 리스트

    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        // 샘플 데이터
//        travelList.addAll(
//            listOf(
//                Travel(
//                    title = "Trip to the Beach",
//                    place = "Seaside, CA",
//                    date = "01/01~01/15",
//                    tags = "#Relaxation#Sunny",
//                    memo = "Enjoyed a peaceful day by the sea."
////                    thumbnail = "Uri" // thumbnail 필드를 다시 활성화
//                )
//            )
//        )

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

        // 플로팅 버튼 클릭 시 AddTravelHistory 열기
        binding.fabAddItem.setOnClickListener {
            // AddTravelHistory 화면으로 이동
           // requireActivity().supportFragmentManager.beginTransaction()
             //   .replace(R.id.nav_host_fragment_activity_main, AddTravelHistory())
               // .addToBackStack(null)
                //.commit()
            showAddTravelDialog(travelAdapter)
        }
        return root
    }

    private fun showAddTravelDialog(travelAdapter: TravelAdapter) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_travel_detail, null)

        // 다이얼로그 내부 뷰 참조
        val titleEditText = dialogView.findViewById<EditText>(R.id.et_travel_title)
        val placeEditText = dialogView.findViewById<EditText>(R.id.et_travel_place)
        val dateEditText = dialogView.findViewById<EditText>(R.id.et_travel_date)
        val tagsEditText = dialogView.findViewById<EditText>(R.id.et_travel_tags)
        val memoEditText = dialogView.findViewById<EditText>(R.id.et_travel_memo)
        val addButton = dialogView.findViewById<Button>(R.id.saveButton)

        // 다이얼로그 생성 및 설정
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // 추가 버튼 클릭 이벤트 처리
        addButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val place = placeEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val tags = tagsEditText.text.toString()
            val memo = memoEditText.text.toString().trim()

            if (title.isNotEmpty() && place.isNotEmpty() && date.isNotEmpty()) {
                val newTravel = Travel(
                    title = title,
                    place = place,
                    date = date,
                    tags = tags,
                    memo = memo,
                    DayInfos = mutableListOf()
                )
                addNewTravel(newTravel) // 새로운 여행 데이터 추가
                sharedViewModel.setNewTravel(newTravel)
                dialog.dismiss() // 다이얼로그 닫기
            } else {
                // 필수 입력값이 비어있을 경우 사용자 알림
                titleEditText.error = "제목을 입력하세요"
                placeEditText.error = "장소를 입력하세요"
                dateEditText.error = "날짜를 입력하세요"
            }
        }

        dialog.show()
    }

    private fun addNewTravel(travel: Travel) {
        travelList.add(travel) // 리스트에 추가
        travelAdapter.notifyItemInserted(travelList.size - 1) // RecyclerView 업데이트
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}