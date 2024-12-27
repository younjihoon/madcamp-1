package com.example.myapplication1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication1.R
import com.example.myapplication1.data.Travel
import com.example.myapplication1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelAdapter: TravelAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 샘플 데이터 생성
//        val sampleData = listOf(
//            Travel(
//                title = "Trip to the Beach",
//                address = "Seaside, CA",
//                tags = "#Relaxation #Sunny",
//                description = "Enjoyed a peaceful day by the sea.",
//                photoResId = R.drawable.photoList
//            ),
//            Travel(
//                title = "Mountain Hiking",
//                address = "Rocky Mountains, CO",
//                tags = "#Adventure #Nature",
//                description = "Explored the rugged mountain trails.",
//                photoResId = R.drawable.travel2
//            )
//        )

//        val sampleData = listOf(
//            Travel(
//                title = "Trip to the Beach",
//                address = "Seaside, CA",
//                tags = "#Relaxation #Sunny",
//                description = "Enjoyed a peaceful day by the sea.",
//                photoResId = R.drawable.travel1,
//                photoListday1 = listOf(
//                    R.drawable.travel1, R.drawable.travel2, R.drawable.travel3,
//                    R.drawable.travel4, R.drawable.travel5, R.drawable.travel6,
//                )
//            ),
//            Travel(
//                title = "Mountain Hiking",
//                address = "Rocky Mountains, CO",
//                tags = "#Adventure #Nature",
//                description = "Explored the rugged mountain trails.",
//                photoResId = R.drawable.travel2,
//                photoListday2 = listOf(
//                    R.drawable.travel1, R.drawable.travel2, R.drawable.travel3,
//                    R.drawable.travel4, R.drawable.travel5, R.drawable.travel6,
//                )
//            )
//        )

        val sampleData = listOf(
            Travel(
                title = "Trip to the Beach",
                address = "Seaside, CA",
                tags = "#Relaxation #Sunny",
                description = "Enjoyed a peaceful day by the sea.",
                photoResId = R.drawable.travel1,
                photoListday1 = listOf(
                    R.drawable.travel1, R.drawable.travel2, R.drawable.travel3,
                    R.drawable.travel4, R.drawable.travel5, R.drawable.travel6
                ),
                photoListday2 = listOf(
                    R.drawable.travel1, R.drawable.travel2, R.drawable.travel3,
                    R.drawable.travel4, R.drawable.travel5, R.drawable.travel6
                )
            ),
            Travel(
                title = "Mountain Hiking",
                address = "Rocky Mountains, CO",
                tags = "#Adventure #Nature",
                description = "Explored the rugged mountain trails.",
                photoResId = R.drawable.travel2,
                photoListday1 = listOf(
                    R.drawable.travel1, R.drawable.travel2, R.drawable.travel3,
                    R.drawable.travel4, R.drawable.travel5, R.drawable.travel6
                ),
                photoListday2 = listOf(
                    R.drawable.travel1, R.drawable.travel2, R.drawable.travel3,
                    R.drawable.travel4, R.drawable.travel5, R.drawable.travel6
                )
            )
        )


        // RecyclerView 설정
        travelAdapter = TravelAdapter(requireContext(), sampleData)
        binding.travelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelRecyclerView.adapter = travelAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
