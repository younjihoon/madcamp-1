package com.example.madcamp2024wjhnh.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
//    private val onFavoriteStatusChanged: (Photo) -> Unit // 즐겨찾기 상태 변경 콜백


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
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_map_detail, null)
        val maptitleTextView = dialogView.findViewById<TextView>(R.id.maptitle)
        val maplinkTextView = dialogView.findViewById<TextView>(R.id.maplink)
        val mapfavoriteButton = dialogView.findViewById<ImageButton>(R.id.mapfavoriteButton)

        // 데이터 설정
        maptitleTextView.text = photo.title
        maplinkTextView.text = photo.link

        mapfavoriteButton.setImageResource(
            if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )

        // 즐겨찾기 버튼 클릭 이벤트 처리
        mapfavoriteButton.setOnClickListener {
            photo.isFavorite = !photo.isFavorite
            mapfavoriteButton.setImageResource(
                if (photo.isFavorite) R.drawable.ic_star_filled
                else {
                    marker.map = null // 마커 제거
                    markersMap.remove(marker) // 매핑에서 제거
                    R.drawable.ic_star_outline
                }
            )
//            notifyItemChanged(position) // RecyclerView 갱신
//            onFavoriteStatusChanged(photo) // 상태 변경 콜백 호출
            sharedViewModel.updatePhoto(photo) // 상태 업데이트를 sharedViewModel로 전달
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 다이얼로그를 화면 아래에 표시하기 위한 설정
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)
        dialog.window?.attributes = dialog.window?.attributes?.apply {
            gravity = android.view.Gravity.BOTTOM
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}