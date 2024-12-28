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
import com.example.madcamp2024wjhnh.ui.home.TravelAdapter


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
                    place = "Seaside, CA",
                    date = "01/01~01/15",
                    tags = "#Relaxation#Sunny",
                    memo = "Enjoyed a peaceful day by the sea."
//                    thumbnail = "Uri" // thumbnail 필드를 다시 활성화
                )
            )
        )

        // RecyclerView 설정
        travelAdapter = TravelAdapter(requireContext(), travelList)
        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelRecyclerView.adapter = travelAdapter

        // 플로팅 버튼 클릭 시 AddTravelHistory 열기
        binding.fabAddItem.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, AddTravelHistory())
                .addToBackStack(null)
                .commit()
            addNewTravel(Travel(
                title = "Trip to the Beach",
                place = "Seaside, CA",
                date = "01/01~01/15",
                tags = "#Relaxation#Sunny",
                memo = "Enjoyed a peaceful day by the sea."
//                    thumbnail = "Uri" // thumbnail 필드를 다시 활성화
            ))
        }


        return root
    }

    // 새로운 여행 데이터 추가
    fun addNewTravel(travel: Travel) {
        travelAdapter.addTravel(travel) // 어댑터에 추가
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}