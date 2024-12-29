package com.example.madcamp2024wjhnh.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Photo
import com.example.madcamp2024wjhnh.databinding.FragmentDashboardBinding
import com.example.madcamp2024wjhnh.ui.PhotoAdapter
import com.example.madcamp2024wjhnh.SharedViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: PhotoAdapter
    private var allPhotos: List<Photo> = listOf() // 전체 사진 목록

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

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        allPhotos = sharedViewModel.getPhotos() // ViewModel에서 전체 사진 데이터를 가져옴

        // RecyclerView 설정
        adapter = PhotoAdapter(requireContext(), allPhotos)
        binding.placesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.placesRecyclerView.adapter = adapter

        // 검색창에 TextWatcher 추가
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전 처리 (사용하지 않음)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 호출
                filterPhotos(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후 처리 (사용하지 않음)
            }
        })
    }

    private fun filterPhotos(query: String) {
        // 검색어에 따라 사진 데이터 필터링
        val filteredPhotos = allPhotos.filter { photo ->
            photo.title.contains(query, ignoreCase = true) // 제목에 검색어 포함 여부 확인
        }
        adapter.updateData(filteredPhotos) // 어댑터 데이터 업데이트
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
