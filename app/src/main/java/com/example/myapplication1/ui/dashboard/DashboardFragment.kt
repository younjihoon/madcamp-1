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
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1"),
            Photo(R.drawable.image2, "Title 2", "This is description for Title 2"),
            Photo(R.drawable.image3, "Title 3", "This is description for Title 3"),
            Photo(R.drawable.image4, "Title 4", "This is description for Title 4"),
            Photo(R.drawable.image5, "Title 5", "This is description for Title 5"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1"),
            Photo(R.drawable.image2, "Title 2", "This is description for Title 2"),
            Photo(R.drawable.image3, "Title 3", "This is description for Title 3"),
            Photo(R.drawable.image4, "Title 4", "This is description for Title 4"),
            Photo(R.drawable.image5, "Title 5", "This is description for Title 5"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1"),
            Photo(R.drawable.image2, "Title 2", "This is description for Title 2"),
            Photo(R.drawable.image3, "Title 3", "This is description for Title 3"),
            Photo(R.drawable.image4, "Title 4", "This is description for Title 4"),
            Photo(R.drawable.image5, "Title 5", "This is description for Title 5"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1"),
            Photo(R.drawable.image2, "Title 2", "This is description for Title 2"),
            Photo(R.drawable.image3, "Title 3", "This is description for Title 3"),
            Photo(R.drawable.image4, "Title 4", "This is description for Title 4"),
            Photo(R.drawable.image5, "Title 5", "This is description for Title 5"),
            Photo(R.drawable.image1, "Title 1", "This is description for Title 1"),
            Photo(R.drawable.image2, "Title 2", "This is description for Title 2"),
            Photo(R.drawable.image3, "Title 3", "This is description for Title 3"),
            Photo(R.drawable.image4, "Title 4", "This is description for Title 4"),
            Photo(R.drawable.image5, "Title 5", "This is description for Title 5")
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
