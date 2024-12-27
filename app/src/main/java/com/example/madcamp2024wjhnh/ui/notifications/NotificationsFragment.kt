package com.example.madcamp2024wjhnh.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.databinding.FragmentNotificationsBinding
import com.example.madcamp2024wjhnh.SharedViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker

class NotificationsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        // 지도 초기화
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        val root: View = binding.root
        return root
    }

    override fun onMapReady(naverMap: NaverMap) {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val favoritePhotos = sharedViewModel.getFavoritePhotos()

        for (photo in favoritePhotos) {
            val marker = Marker()
            marker.position = LatLng(photo.latitude, photo.longitude)
            marker.map = naverMap
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}