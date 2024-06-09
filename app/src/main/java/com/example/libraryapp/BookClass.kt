package com.example.libraryapp


data class Books(
    var isbnNo: String = "",
    var title: String = "",
    var author: String = "",
    var publish: String = "",
    var pubyear: String = "",
    var num: String = "",
    var location: String = "",
    var imageSrc: String = "",
    var detailSrc: String = "",
    var tags: String = ""
)

data class modifyBook(
    var title: String = "",
    var author: String = "",
    var publish: String = "",
    var pubyear: String = "",
    var num: String = "",
    var location: String = "",
    var imageSrc: String = "",
    var detailSrc: String = "",
    var tags: String = ""
)

data class Keyword(
    var tag1: String = "",
    var tag2: String = "",
)


data class PostBooks (
    var message: Message
)

