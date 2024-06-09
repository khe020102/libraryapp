package com.example.libraryapp

import com.google.gson.annotations.SerializedName

data class UserClass(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("studentID") var studentID: Int = 0,
    @SerializedName("password") var password: String = "",
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("phone_number") var phone_number: String = "",
    @SerializedName("enabled") var enabled : Boolean = true,
    @SerializedName("authorities") var authorities : List<Author>,
    @SerializedName("username") var username : String = "",
    @SerializedName("accountNonExpired") var accountNonExpired : Boolean = true,
    @SerializedName("accountNonLocked") var accountNonLocked : Boolean = true,
    @SerializedName("credentialsNonExpired") var credentialsNonExpired : Boolean = true,
)



data class Author(
    var authority: String = ""
)

data class User (
    var userClass: UserClass
)
