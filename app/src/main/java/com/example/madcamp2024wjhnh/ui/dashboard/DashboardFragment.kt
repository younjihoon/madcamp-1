package com.example.madcamp2024wjhnh.ui.dashboard

import VerticalAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.madcamp2024wjhnh.data.Section

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var adapter: VerticalAdapter
    private lateinit var sections: List<Section>
    private var allPhotos: List<Photo> = listOf() // 전체 사진 목록
    private var isFavoritesView: Boolean = false // 현재 즐겨찾기 보기 상태를 나타냄
    private val tags = listOf<String>("이 집 잘하네", "서울여행 필수코스", "힐링이 필요해")
    private val tagPhoto = mutableListOf<MutableList<Photo>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]

        for (tag in tags) {
            tagPhoto.add(mutableListOf())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        allPhotos = sharedViewModel.getPhotos() // ViewModel에서 전체 사진 데이터를 가져옴
        for ((i, photo) in allPhotos.withIndex()) {
            tagPhoto[i % tagPhoto.size].add(photo)
        }
        sections = tagPhoto.mapIndexed { index, photos ->
            com.example.madcamp2024wjhnh.data.Section(tags[index], photos)
        }
        for ((i, section) in sections.withIndex()) {
            if (i<dashboardViewModel.sections.size)
                dashboardViewModel.sections[i] = section
            else dashboardViewModel.sections.add(section)
        }
        adapter = VerticalAdapter(requireContext(),sections ){ updatedPhoto ->
            if (isFavoritesView && !updatedPhoto.isFavorite) {
                // 즐겨찾기 상태가 변경되고, 즐겨찾기 보기 상태일 경우
                updateFavoritesView()
            }
        }
        val verticalRecyclerView = binding.placesRecyclerView
        verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        verticalRecyclerView.adapter = adapter



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
                Log.e("[DashboardF]","update: $sections")
                adapter.updateSections(sections)
                binding.favoritesButton.setImageResource(R.drawable.ic_star_outline) // 아이콘 복원
            }
        }
    }

    private fun updateFavoritesView() {
        // 즐겨찾기 항목만 필터링
        Log.e("[DashboardF]","updateFavoritesView")
        val favoriteItems = mutableListOf<MutableList<Photo>>()
        for (tag in tags) {
            favoriteItems.add(mutableListOf())
        }
        for ((i, photo) in allPhotos.withIndex()) {
            if (photo.isFavorite)
                favoriteItems[i % favoriteItems.size].add(photo)
        }
        Log.e("[DashboardF]","favoriteItems: $favoriteItems")
        var newsections = favoriteItems.mapIndexed { index, photos ->
            com.example.madcamp2024wjhnh.data.Section(tags[index], photos)
        }
        Log.e("[DashboardF]","notify: $newsections")
        adapter.updateSections(newsections)
    }

    private fun filterPhotos(query: String) {
        Log.e("[DashboardF]","filterPhotos")
        val favoriteItems = mutableListOf<MutableList<Photo>>()
        for (tag in tags) {
            favoriteItems.add(mutableListOf())
        }
        for ((i, photo) in allPhotos.withIndex()) {
            if (photo.title.contains(query, ignoreCase = true) or photo.description.contains(query, ignoreCase = true))
                when (i % 3) {
                    0 -> favoriteItems[0].add(photo) // "이 집 잘하네" 그룹에 추가
                    1 -> favoriteItems[1].add(photo) // "서울여행 필수코스" 그룹에 추가
                    2 -> favoriteItems[2].add(photo) // "힐링이 필요해" 그룹에 추가
                }
        }
        var newsections = favoriteItems.mapIndexed { index, photos ->
            com.example.madcamp2024wjhnh.data.Section(tags[index], photos)
        }
        adapter.updateSections(newsections)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
