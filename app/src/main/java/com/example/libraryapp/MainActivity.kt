package com.example.libraryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin: ImageButton
    lateinit var editTextId: EditText
    lateinit var editTextPassword: EditText
    lateinit var btnRegister: Button
    var DB: DBHelper? = null

    private lateinit var api: UserApi // UserApi를 불러오는 부분 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DB = DBHelper(this) //필요 x
        api = RetrofitClientInstance.userApi // RetrofitClientInstance에서 UserApi를 불러오는 부분 추가

        btnLogin = findViewById(R.id.btnLogin)
        editTextId = findViewById(R.id.editTextId)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnRegister = findViewById(R.id.btnRegister)

        // 로그인 버튼 클릭
        btnLogin.setOnClickListener {
            val user = editTextId.text.toString()
            val pass = editTextPassword.text.toString()

            // 빈칸 제출시 Toast
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this@MainActivity, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val loginData = LoginData(username = user, password = pass) // LoginData 객체 생성
                        val response = api.login(loginData) // 수정된 부분
                        // 로그인 성공시

                        if (response != null) {
                            Toast.makeText(this@MainActivity, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()

                        e.printStackTrace()
                    }
                }
            }
        }

        // 회원가입 버튼 클릭시
        btnRegister.setOnClickListener {
            val loginIntent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(loginIntent)
        }
    }
}