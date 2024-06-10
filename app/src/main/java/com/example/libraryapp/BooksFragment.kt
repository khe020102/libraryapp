package com.example.libraryapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.BookAdapter
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentBooksBinding

class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var bookAdapter: BookAdapter

    private val recommendedBooks = api.getRecommendedBooks()

    private val popularBooks = api.getPopularBooks()
    private val newBooks = getNewBooks()
    val BASE_URL = "http://52.78.146.166:8080/api/"

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val api = retrofit.create(API::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBooksBinding.inflate(inflater, container, false)

        setupRecyclerView()

        setupSpinner(binding.spinnerFilter)

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        bookAdapter = BookAdapter(popularBooks)
        binding.recyclerView.adapter = bookAdapter
    }

    private fun setupSpinner(spinner: Spinner) {
        val options = listOf("AI 추천도서", "인기도서", "신간도서")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> bookAdapter.updateBooks(recommendedBooks) // AI 추천도서
                    1 -> bookAdapter.updateBooks(popularBooks) // 인기도서
                    2 -> bookAdapter.updateBooks(newBooks) // 신간도서
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}
