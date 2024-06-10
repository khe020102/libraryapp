package com.example.libraryapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookinformationFragment : Fragment() {

    private lateinit var textViewBookInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookinformation, container, false)
        textViewBookInfo = view.findViewById(R.id.textViewBookInfo)
        fetchBookInformation("your_isbn_no") // Provide the actual ISBN number
        return view
    }

    private fun fetchBookInformation(isbnNo: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = RetrofitClientInstance.bookApi.getBook(isbnNo)
                if (response.isSuccessful) {
                    val book = response.body()
                    textViewBookInfo.text = book?.let { formatBookInfo(it) } ?: "No book information found."
                } else {
                    textViewBookInfo.text = "Failed to fetch book information."
                }
            } catch (e: Exception) {
                textViewBookInfo.text = "An error occurred: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    private fun formatBookInfo(book: Book): String {
        return """
            Title: ${book.title}
            Author: ${book.author}
            Publish: ${book.publish}
            Year: ${book.pubyear}
            Location: ${book.location}
            Tags: ${book.tags}
        """.trimIndent()
    }
}
