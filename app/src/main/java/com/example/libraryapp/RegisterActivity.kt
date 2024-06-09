package com.example.libraryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    var DB: DBHelper? = null
    lateinit var editTextId: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextRePassword: EditText
    lateinit var editTextNick: EditText
    lateinit var editTextPhone: EditText
    lateinit var btnRegister: ImageButton
    lateinit var btnCheckId: Button
    lateinit var btnCheckNick: Button
    var CheckId: Boolean = false
    var CheckNick: Boolean = false

    // Retrofit 객체 및 API 인터페이스 생성
    private val BASE_URL = "http://52.78.146.166:8080/api/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(UserApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        DB = DBHelper(this)
        editTextId = findViewById(R.id.editTextId_Reg)
        editTextPassword = findViewById(R.id.editTextPass_Reg)
        editTextRePassword = findViewById(R.id.editTextRePass_Reg)
        editTextNick = findViewById(R.id.editTextNick_Reg)
        editTextPhone = findViewById(R.id.editTextPhone_Reg)
        btnRegister = findViewById(R.id.btnRegister_Reg)
        btnCheckId = findViewById(R.id.btnCheckId_Reg)
        btnCheckNick = findViewById(R.id.btnCheckNick_Reg)

        // 학번 중복확인 버튼 클릭 이벤트 처리
        btnCheckId.setOnClickListener {
            val studentNumber = editTextId.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = api.checkStudentNumber(studentNumber)
                    if (response == false) {
                        CheckId = true
                        Toast.makeText(this@RegisterActivity, "사용 가능한 학번입니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@RegisterActivity, "이미 존재하는 학번입니다:", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Error occurred: ${e.message}")
                    Toast.makeText(
                        this@RegisterActivity,
                        "학번 중복확인 중 오류가 발생했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // 닉네임 중복확인 버튼 클릭 이벤트 처리
        btnCheckNick.setOnClickListener {
            val nickname = editTextNick.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = api.checkNickname(nickname)
                    if (response == false) {
                        CheckNick = true
                        Toast.makeText(this@RegisterActivity, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@RegisterActivity, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Error occurred: ${e.message}")
                    Toast.makeText(
                        this@RegisterActivity,
                        "닉네임 중복확인 중 오류가 발생했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // 완료 버튼 클릭 이벤트 처리
        btnRegister.setOnClickListener {
            val studentID = editTextId.text.toString().toIntOrNull()
            val password = editTextPassword.text.toString()
            val repassword = editTextRePassword.text.toString()
            val nickname = editTextNick.text.toString()
            val phone_number = editTextPhone.text.toString()

            // 회원가입 로직 수행
            if (studentID == null || studentID == 0 || password.isEmpty() || repassword.isEmpty() || nickname.isEmpty() || phone_number.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "회원정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (CheckId) {
                    val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,15}$"
                    if (Pattern.matches(pwPattern, password)) {
                        if (password == repassword) {
                            if (CheckNick) {
                                val phonePattern = "^(\\+[0-9]+)?[0-9]{10,15}$"
                                if (Pattern.matches(phonePattern, phone_number)) {
                                    // 회원 가입 정보 서버로 전송
                                    CoroutineScope(Dispatchers.Main).launch {
                                        try {
                                            // authorities 매개변수에 빈 리스트를 전달
                                            val user = api.signup(
                                                UserClass(
                                                    studentID = studentID,
                                                    password = password,
                                                    nickname = nickname,
                                                    phone_number = phone_number,
                                                    enabled = true,
                                                    authorities = emptyList(),
                                                    username = "",
                                                    accountNonExpired = true,
                                                    accountNonLocked = true,
                                                    credentialsNonExpired = true
                                                )
                                            )

                                            if (user != null) {
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    "가입되었습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(
                                                    applicationContext,
                                                    Register2Activity::class.java
                                                )
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    "회원가입에 실패했습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } catch (e: Exception) {
                                            Log.e(
                                                "RegisterActivity",
                                                "Error occurred during signup: ${e.message}"
                                            )
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "회원가입 중 오류가 발생했습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "전화번호 형식이 옳지 않습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "닉네임 중복확인을 해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "비밀번호 형식이 옳지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
