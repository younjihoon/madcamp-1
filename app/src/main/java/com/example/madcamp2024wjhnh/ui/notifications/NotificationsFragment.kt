package com.example.madcamp2024wjhnh.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.databinding.FragmentNotificationsBinding
import com.example.madcamp2024wjhnh.SharedViewModel
import com.example.madcamp2024wjhnh.data.Photo
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

class NotificationsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private lateinit var sharedViewModel: SharedViewModel
    private val markersMap = mutableMapOf<Marker, Photo>() // 마커와 사진 데이터를 매핑

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
        this.naverMap = naverMap
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val favoritePhotos = sharedViewModel.getFavoritePhotos()

        for (photo in favoritePhotos) {
            val marker = Marker()
            marker.position = LatLng(photo.latitude, photo.longitude)
            marker.map = naverMap
            marker.icon = MarkerIcons.YELLOW //https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/util/MarkerIcons.html
//            marker.icon = OverlayImage.fromResource(R.drawable.ic_pin_star)

            markersMap[marker] = photo

            marker.setOnClickListener {
                showPhotoDialog(photo, marker)
                true
            }
        }
    }

    private fun showPhotoDialog(photo: Photo, marker: Marker) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_photo_detail, null)
        val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val dialogTitleTextView = dialogView.findViewById<TextView>(R.id.dialogTitleTextView)
        val dialogDescriptionTextView = dialogView.findViewById<TextView>(R.id.dialogDescriptionTextView)
        val dialogLinkTextView = dialogView.findViewById<TextView>(R.id.dialogLinkTextView)
        val dialogFavoriteToggleButton = dialogView.findViewById<ToggleButton>(R.id.favoriteToggleButton)

        // 데이터 설정
        dialogImageView.setImageResource(photo.imageResId)
        dialogTitleTextView.text = photo.title
        dialogDescriptionTextView.text = photo.description
        dialogLinkTextView.text = photo.link


        // 즐겨찾기 토글 상태 설정
        dialogFavoriteToggleButton.isChecked = photo.isFavorite

        dialogFavoriteToggleButton.setOnCheckedChangeListener { _, isChecked ->
            photo.isFavorite = isChecked
            sharedViewModel.updatePhoto(photo) // ViewModel을 통해 데이터 업데이트

            if (!isChecked) {
                marker.map = null // 마커 제거
                markersMap.remove(marker) // 매핑에서 제거
            }
        }

        // 다이얼로그 생성
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("닫기") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}