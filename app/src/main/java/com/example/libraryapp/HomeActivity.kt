package com.example.libraryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libraryapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    // UserApi 인스턴스 생성
    private val userApi = RetrofitClientInstance.userApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_books
        }
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_books -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SearchFragment()).commit()
                    true
                }
                R.id.fragment_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SearchFragment()).commit()
                    true
                }
                R.id.fragment_logout -> {
                    // 로그아웃 요청
                    logoutAndNavigateToMainActivity()
                    true
                }
                else -> false
            }
        }
    }

    private fun logoutAndNavigateToMainActivity() {
        // Coroutine을 사용하여 로그아웃 요청을 비동기적으로 처리
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // logout 호출
                val response = userApi.logout()
                if (response.isSuccessful()) {
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티 종료
                    }
                 else {
                    // 응답이 실패한 경우에 대한 처리
                    runOnUiThread {
                        Toast.makeText(this@HomeActivity, "로그아웃 요청 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // 오류 발생 시 메시지 표시
                runOnUiThread {
                    Toast.makeText(this@HomeActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
