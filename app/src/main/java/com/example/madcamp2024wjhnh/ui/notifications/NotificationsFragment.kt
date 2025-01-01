package com.example.madcamp2024wjhnh.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
    private lateinit var sharedPreferences : SharedPreferences
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
        sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)

        val root: View = binding.root
        return root
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val startlikedPlaces = sharedViewModel.getPhotos() // ViewModel에서 전체 사진 데이터를 가져옴
        var favoritePhotos = mutableListOf<Photo>()
        for (photo in startlikedPlaces) {
            var likedPlaces = loadFavoriteIds()
            Log.e("[NotiF]","${likedPlaces} is likedPlaces")
            if (photo.title in likedPlaces) {
                photo.isFavorite = true
                favoritePhotos.add(photo)
                Log.e("[NotiF]","${photo.title} is liked")
            }
        }

        for (photo in favoritePhotos) {
            var marker = Marker()
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

    private fun showPhotoDialog(photo: Photo, markerr: Marker) {
        var marker = markerr
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_map_detail, null)
        val maptitleTextView = dialogView.findViewById<TextView>(R.id.maptitle)
        val maplinkTextView = dialogView.findViewById<TextView>(R.id.maplink)
        val mapdescriptionTextView = dialogView.findViewById<TextView>(R.id.mapdescription)
        val mapfavoriteButton = dialogView.findViewById<ImageButton>(R.id.mapfavoriteButton)


        // 데이터 설정
        maptitleTextView.text = photo.title
        maplinkTextView.text = photo.link
        mapdescriptionTextView.text = photo.description

        // 초기 즐겨찾기 상태 설정
        mapfavoriteButton.setImageResource(
            if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )

        // 즐겨찾기 버튼 클릭 이벤트 처리
        mapfavoriteButton.setOnClickListener {
            photo.isFavorite = !photo.isFavorite // 상태 반전

            // 버튼 아이콘 즉시 갱신
            mapfavoriteButton.setImageResource(
                if (photo.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )

            if (photo.isFavorite) {
                // 즐겨찾기 추가: 새 마커 생성
                if (!markersMap.values.contains(photo)) { // 중복 방지
                    val newMarker = Marker().apply {
                        position = LatLng(photo.latitude, photo.longitude)
                        icon = MarkerIcons.YELLOW
                        map = naverMap
                        setOnClickListener {
                            showPhotoDialog(photo, this)
                            true
                        }
                    }
                    markersMap[newMarker] = photo
                    marker = newMarker
                }

            } else {
                // 즐겨찾기 삭제: 기존 마커 제거
                marker?.map = null
                marker?.let { markersMap.remove(it) }
            }

            // ViewModel에 변경 사항 반영
            sharedViewModel.updatePhoto(photo)
            onFavoriteButtonClick(photo.title,photo.isFavorite)
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
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
        dialog.show()
    }
    private fun saveFavoriteIds(favoriteIds: List<String>) {
        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("favorite_ids", favoriteIds.toSet()) // List를 Set으로 변환하여 저장
        editor.apply() // 비동기 저장
        Log.e("[NotiF]","Saved: ${favoriteIds}")
    }
    private fun loadFavoriteIds(): List<String> {
        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("favorite_ids", emptySet())!!.toList() // Set을 List로 변환
    }

    private fun onFavoriteButtonClick(photoId: String, isFavorite: Boolean) {
        val favoriteIds = loadFavoriteIds().toMutableList()

        if (isFavorite) {
            if (!favoriteIds.contains(photoId)) {
                favoriteIds.add(photoId) // 좋아요 추가
            }
        } else {
            favoriteIds.remove(photoId) // 좋아요 취소
        }

        saveFavoriteIds(favoriteIds) // 업데이트된 리스트 저장
        Log.e("[NotiF]","new Saved: ${favoriteIds}")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}