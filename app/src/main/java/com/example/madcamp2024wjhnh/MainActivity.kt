package com.example.madcamp2024wjhnh

import android.content.Context
import android.content.Intent
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.madcamp2024wjhnh.databinding.ActivityMainBinding
import com.naver.maps.map.NaverMapSdk
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.Photo
import com.example.madcamp2024wjhnh.data.Travel
import com.google.android.gms.common.internal.service.Common
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Naver Map 인증 실패 리스너 등록
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("xzzsdlxqnz")
        NaverMapSdk.getInstance(this).onAuthFailedListener =
            NaverMapSdk.OnAuthFailedListener { exception ->
                // 인증 실패 처리
                handleAuthFailed(exception)
            }
        var sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val travelViewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        travelViewModel.allTravels.observeForever { travels ->
            Log.e("[MainActivity]", "All travels: $travels")
            sharedViewModel.replaceTravel(travels)
        }
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        } else {
            // 권한이 이미 허용된 경우
            Log.d("Permission", "권한이 이미 허용되었습니다.")
        }
        val photos = loadPhotosFromCSV()
        sharedViewModel.setPhotos(photos)
        Log.e("[MainActivity]", "All photos: $photos")
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        //--
        //--
    }
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                .setTitle("권한 필요")
                .setMessage("앱에서 파일을 읽으려면 권한이 필요합니다.")
                .setPositiveButton("확인") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(READ_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                }
                .setNegativeButton("취소", null)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "권한이 허용되었습니다.")
            } else {
                Log.d("Permission", "권한이 거부되었습니다.")
            }
        }
    }
    private fun handleAuthFailed(exception: NaverMapSdk.AuthFailedException) {
        // Logcat에 에러 메시지 출력
        android.util.Log.e(
            "NaverMapAuth",
            "인증 실패: 상태코드=${exception.getErrorCode()}, 메시지=${exception.message}"
        )

        // 사용자에게 Toast로 알림 표시
        Toast.makeText(
            this,
            "지도 인증에 실패했습니다. 상태코드=${exception.getErrorCode()}, 메시지=${exception.message}",
            Toast.LENGTH_LONG
        ).show()
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
            if (tokens.size >= 4) {
                val imageResId = getImageResId(tokens[0].trim())
                val title = tokens[1].trim()
                val description = tokens[2].trim()
                val link = tokens[3].trim()
                val latitude = tokens[4].trim().toDouble()
                val longitude = tokens[5].trim().toDouble()
                photos.add(Photo(imageResId, title, description, link, latitude, longitude))
            }
        }
        return photos
    }

    private fun getImageResId(imageName: String): Int {
        // 리소스 이름을 기반으로 ID 가져오기
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

}