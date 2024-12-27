package com.example.madcamp2024wjhnh

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.madcamp2024wjhnh.databinding.ActivityMainBinding
import com.naver.maps.map.NaverMapSdk
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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



}