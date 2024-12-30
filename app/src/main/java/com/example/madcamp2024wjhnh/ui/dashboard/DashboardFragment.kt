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
    private var isFavoritesView: Boolean = false // 현재 즐겨찾기 보기 상태를 나타냄

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
//        adapter = PhotoAdapter(requireContext(), allPhotos)
        adapter = PhotoAdapter(requireContext(), allPhotos) { updatedPhoto ->
            if (isFavoritesView && !updatedPhoto.isFavorite) {
                // 즐겨찾기 상태가 변경되고, 즐겨찾기 보기 상태일 경우
                updateFavoritesView()
            }
        }
        binding.placesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.placesRecyclerView.adapter = adapter

        // 검색창에 TextWatcher 추가
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filterPhotos(s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Favorites 버튼 클릭 이벤트 처리
        binding.favoritesButton.setOnClickListener {
            isFavoritesView = !isFavoritesView // 상태 반전

            if (isFavoritesView) {
                updateFavoritesView()
                binding.favoritesButton.setImageResource(R.drawable.ic_star_filled) // 아이콘 변경
            } else {
                adapter.updateData(allPhotos)
                binding.favoritesButton.setImageResource(R.drawable.ic_star_outline) // 아이콘 복원
            }
        }
    }

    private fun updateFavoritesView() {
        // 즐겨찾기 항목만 필터링
        val favoriteItems = allPhotos.filter { it.isFavorite }
        adapter.updateData(favoriteItems)
    }

    private fun filterPhotos(query: String) {
        // 검색어에 따라 사진 데이터 필터링
        val filteredPhotos = allPhotos.filter { photo ->
            photo.title.contains(query, ignoreCase = true) // 제목에 검색어 포함 여부 확인
            photo.description.contains(query, ignoreCase = true)
        }
        adapter.updateData(filteredPhotos) // 어댑터 데이터 업데이트
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
