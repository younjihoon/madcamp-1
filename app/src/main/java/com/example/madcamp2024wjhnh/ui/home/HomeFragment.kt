package com.example.madcamp2024wjhnh.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        travelAdapter = TravelAdapter(requireContext(), travelList)
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
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, AddTravelHistory())
                .addToBackStack(null)
                .commit()
        }

        binding.buttonGoToDayInfo.setOnClickListener{
            val intent = Intent(requireContext(), DayInfoActivity::class.java)
            val initInfo = mutableListOf(
                DayInfo(0, mutableListOf("주소"), "description", mutableListOf(0)),
                DayInfo(1, mutableListOf("주소"), "description", mutableListOf(0))
            )
            val travel = Travel("1", "제목", "장소", "20241228", "메모", initInfo)
            intent.putExtra("travel", travel)
            startActivity(intent)
        }

        return root
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