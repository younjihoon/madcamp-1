package com.example.madcamp2024wjhnh.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Photo
import com.example.madcamp2024wjhnh.databinding.FragmentDashboardBinding
import com.example.madcamp2024wjhnh.ui.PhotoAdapter
import java.io.BufferedReader
import java.io.InputStreamReader

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
//        val photos = listOf(
//            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
//            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
//            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대This is description for 첨성대","https://www.google.com"),
//            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
//            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
//            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대","https://www.google.com"),
//            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
//            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
//            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대","https://www.google.com"),
//            Photo(R.drawable.image1, "Title 1", "This is description for Title 1","https://www.google.com"),
//            Photo(R.drawable.image2, "건물", "This is description for ..", "https://www.google.com"),
//            Photo(R.drawable.image3, "볼거리", "This is description for 첨성대","https://www.google.com")
//        )
        val photos = loadPhotosFromCSV()

        // RecyclerView 설정
        val adapter = PhotoAdapter(requireContext(), photos)
        binding.placesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.placesRecyclerView.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun loadPhotosFromCSV(): List<Photo> {
        val photos = mutableListOf<Photo>()

        // CSV 파일 읽기
        val inputStream = resources.openRawResource(R.raw.photos)
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

        // 첫 번째 줄은 헤더이므로 건너뛰기
        reader.readLine()

        reader.forEachLine { line ->
            val tokens = line.split(",") // CSV 필드 분리
            if (tokens.size == 4) {
                val imageResId = getImageResId(tokens[0].trim())
                val title = tokens[1].trim()
                val description = tokens[2].trim()
                val link = tokens[3].trim()
                photos.add(Photo(imageResId, title, description, link))
            }
        }
        return photos
    }

    private fun getImageResId(imageName: String): Int {
        // 리소스 이름을 기반으로 ID 가져오기
        return resources.getIdentifier(imageName, "drawable", requireContext().packageName)
    }
}
