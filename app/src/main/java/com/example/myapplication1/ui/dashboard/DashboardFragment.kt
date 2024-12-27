package com.example.myapplication1.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication1.R
import com.example.myapplication1.data.Photo
import com.example.myapplication1.databinding.FragmentDashboardBinding
import com.example.myapplication1.ui.PhotoAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 샘플 데이터 생성
        val photos = listOf(
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대","https://www.google.com"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대","https://www.google.com"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대","https://www.google.com"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대","https://www.google.com")
        )

        // RecyclerView 설정
        val adapter = PhotoAdapter(requireContext(), photos)
        binding.placesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.placesRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
