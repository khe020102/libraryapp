package com.example.libraryapp

data class Reviews(
    var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var isbnNo: String = "",
    var studentnumber: String = "",
    var score: String = ""
)


data class PostReviews (
    var message: Message
)

data class Message(
    var result: Reviews
)
